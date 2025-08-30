package com.project.nelson.msvc_gateway.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(JwtGlobalFilter.class);

  @Value("${gateway.filter.jwt-secret:SuperClaveSecretaSeguraQueDebesCambiar}")
  private String jwtSecret;

  @Value("${gateway.filter.conditional-path:/api/segura/}")
  private String conditionalPath;

  @Value("${gateway.filter.allowed-roles:ROLE_ADMIN,ROLE_USER}")
  private String allowedRoles;

  @Value("${gateway.filter.role-claim:rol}")
  private String roleClaim;

  @Value("${gateway.filter.email-claim:email}")
  private String emailClaim;

  @Value("${gateway.filter.order:100}")
  private int filterOrder;

  @Value("${gateway.filter.exempt-paths:/login,/register,/public/,/swagger-ui/,/v3/api-docs/,/auth/login,/usuarios/register}")
  private String exemptPaths;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    String method = exchange.getRequest().getMethod().name();
    String contentType = exchange.getRequest().getHeaders().getFirst("Content-Type");

    logger.info("Evaluando petición: {} {} | Content-Type: {}", method, path, contentType);

    // Ignorar preflight OPTIONS
    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
      logger.info("Preflight OPTIONS detectado, exentando: {}", path);
      return chain.filter(exchange);
    }

    // Exención flexible por 'contains'
    List<String> exempt = Arrays.asList(exemptPaths.split(","));
    boolean isExempt = exempt.stream().anyMatch(ex -> path.contains(ex));
    logger.info("¿Ruta exenta? {} para path: {}", isExempt, path);
    if (isExempt) {
      logger.info("Exentando ruta: {}", path);
      return chain.filter(exchange);
    }

    if ("application/octet-stream".equalsIgnoreCase(contentType)) {
      logger.info("Exentando petición binaria: {}", path);
      return chain.filter(exchange);
    }

    if (jwtSecret == null || jwtSecret.getBytes().length < 32) {
      logger.error("La clave JWT debe tener al menos 32 bytes.");
      exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
      return exchange.getResponse().setComplete();
    }

    // Solo validar JWT en rutas protegidas
    if (!path.startsWith(conditionalPath)) {
      logger.info("Ruta fuera de protección JWT: {}", path);
      return chain.filter(exchange);
    }

    List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");
    if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
      logger.warn("No hay token JWT válido en Authorization para ruta protegida: {}", path);
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    String jwt = authHeaders.get(0).replace("Bearer ", "");
    logger.info("JWT recibido: {}... para path: {}", jwt.substring(0, Math.min(jwt.length(), 10)), path);

    try {
      Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
      Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt)
        .getBody();

      String rolClaimValue = claims.get(roleClaim, String.class);
      String email = claims.get(emailClaim, String.class);
      String usuario = claims.getSubject() != null ? claims.getSubject() : "";

      logger.info("Claims extraídos: usuario={}, email={}, rol={}", usuario, email, rolClaimValue);

      if (rolClaimValue == null || rolClaimValue.isEmpty()) {
        logger.warn("No se encontró el claim de roles en el JWT para usuario: {}", usuario);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
      }
      if (email == null || email.isEmpty()) {
        logger.warn("No se encontró el claim de email en el JWT para usuario: {}", usuario);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
      }

      Set<String> userRoles = Arrays.stream(rolClaimValue.split(","))
        .map(String::trim)
        .filter(r -> !r.isEmpty())
        .collect(Collectors.toSet());
      Set<String> allowedRolesSet = Arrays.stream(allowedRoles.split(","))
        .map(String::trim)
        .filter(r -> !r.isEmpty())
        .collect(Collectors.toSet());

      boolean hasAllowedRole = userRoles.stream().anyMatch(allowedRolesSet::contains);
      logger.info("Roles de usuario: {} | Roles permitidos: {} | ¿Autorizado?: {}", userRoles, allowedRolesSet, hasAllowedRole);

      if (!hasAllowedRole) {
        logger.warn("Acceso denegado por roles. Usuario: {} | Roles del usuario: {} | Roles requeridos: {}", usuario, userRoles, allowedRolesSet);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
      }

      ServerHttpRequest mutatedRequest = exchange.getRequest()
        .mutate()
        .header("X-Usuario", usuario)
        .header("X-Roles", String.join(",", userRoles))
        .header("X-Email", email)
        .header("X-JWT-Id", claims.getId() != null ? claims.getId() : "")
        .build();
      ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

      return chain.filter(mutatedExchange)
        .doOnSuccess(aVoid -> logger.info("Petición autorizada para usuario: {} | Roles: {}", usuario, userRoles));
    } catch (ExpiredJwtException ex) {
      logger.warn("JWT expirado para usuario: {}", ex.getClaims().getSubject());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    } catch (Exception e) {
      logger.warn("JWT inválido: {} | Path: {}", e.getMessage(), path);
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  @Override
  public int getOrder() {
    return filterOrder;
  }
}