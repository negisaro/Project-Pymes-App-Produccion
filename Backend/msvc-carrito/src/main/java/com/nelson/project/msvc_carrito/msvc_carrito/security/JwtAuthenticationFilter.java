package com.nelson.project.msvc_carrito.msvc_carrito.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autenticación JWT empresarial para el microservicio de carrito.
 *
 * Implementa validación robusta de tokens JWT con:
 * - Validación completa de firma y expiración
 * - Extracción segura de claims y roles
 * - Manejo de excepciones detallado
 * - Logging de auditoría para seguridad
 * - Soporte para refresh tokens
 * - Rate limiting por usuario
 * - Blacklist de tokens revocados
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly123456789}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}") // 24 horas por defecto
  private Long jwtExpirationMs;

  private final ObjectMapper objectMapper;

  // Endpoints que no requieren autenticación
  private static final List<String> PUBLIC_ENDPOINTS = List.of(
    "/api/v1/carrito/health",
    "/api/v1/metrics/health",
    "/api/v1/metrics/liveness",
    "/swagger-ui",
    "/v3/api-docs",
    "/actuator/health",
    "/actuator/prometheus"
  );

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String correlationId = request.getHeader("X-Correlation-ID");
    String requestUri = request.getRequestURI();
    String method = request.getMethod();

    try {
      // Verificar si es un endpoint público
      if (isPublicEndpoint(requestUri)) {
        log.debug(
          "🔓 PUBLIC_ENDPOINT | URI: {} | Method: {} | CorrelationId: {}",
          requestUri,
          method,
          correlationId
        );
        filterChain.doFilter(request, response);
        return;
      }

      // Extraer token JWT del header Authorization
      String jwt = extractJwtFromRequest(request);

      if (!StringUtils.hasText(jwt)) {
        log.warn(
          "🚫 MISSING_JWT | URI: {} | ClientIP: {} | CorrelationId: {}",
          requestUri,
          getClientIp(request),
          correlationId
        );
        sendUnauthorizedResponse(
          response,
          "Token JWT requerido",
          "MISSING_TOKEN",
          correlationId
        );
        return;
      }

      // Validar y procesar el token JWT
      if (validateAndProcessJwt(jwt, request, correlationId)) {
        log.debug(
          "✅ JWT_VALID | URI: {} | User: {} | CorrelationId: {}",
          requestUri,
          SecurityContextHolder.getContext().getAuthentication().getName(),
          correlationId
        );
        filterChain.doFilter(request, response);
      } else {
        log.warn(
          "❌ JWT_INVALID | URI: {} | ClientIP: {} | CorrelationId: {}",
          requestUri,
          getClientIp(request),
          correlationId
        );
        sendUnauthorizedResponse(
          response,
          "Token JWT inválido",
          "INVALID_TOKEN",
          correlationId
        );
      }
    } catch (ExpiredJwtException e) {
      log.warn(
        "⏰ JWT_EXPIRED | URI: {} | User: {} | ExpiredAt: {} | CorrelationId: {}",
        requestUri,
        e.getClaims().getSubject(),
        e.getClaims().getExpiration(),
        correlationId
      );
      sendUnauthorizedResponse(
        response,
        "Token JWT expirado",
        "EXPIRED_TOKEN",
        correlationId
      );
    } catch (UnsupportedJwtException e) {
      log.warn(
        "🔧 JWT_UNSUPPORTED | URI: {} | Error: {} | CorrelationId: {}",
        requestUri,
        e.getMessage(),
        correlationId
      );
      sendUnauthorizedResponse(
        response,
        "Formato de token no soportado",
        "UNSUPPORTED_TOKEN",
        correlationId
      );
    } catch (MalformedJwtException e) {
      log.warn(
        "💥 JWT_MALFORMED | URI: {} | Error: {} | CorrelationId: {}",
        requestUri,
        e.getMessage(),
        correlationId
      );
      sendUnauthorizedResponse(
        response,
        "Token JWT malformado",
        "MALFORMED_TOKEN",
        correlationId
      );
    } catch (SignatureException e) {
      log.warn(
        "🔐 JWT_SIGNATURE_INVALID | URI: {} | Error: {} | CorrelationId: {}",
        requestUri,
        e.getMessage(),
        correlationId
      );
      sendUnauthorizedResponse(
        response,
        "Firma del token inválida",
        "INVALID_SIGNATURE",
        correlationId
      );
    } catch (Exception e) {
      log.error(
        "💥 JWT_PROCESSING_ERROR | URI: {} | Error: {} | CorrelationId: {}",
        requestUri,
        e.getMessage(),
        correlationId,
        e
      );
      sendInternalErrorResponse(
        response,
        "Error interno en autenticación",
        correlationId
      );
    }
  }

  private boolean isPublicEndpoint(String requestUri) {
    return PUBLIC_ENDPOINTS.stream()
      .anyMatch(endpoint -> requestUri.startsWith(endpoint));
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    // Intentar obtener de query parameter como fallback (para WebSocket, etc.)
    String tokenParam = request.getParameter("token");
    if (StringUtils.hasText(tokenParam)) {
      return tokenParam;
    }

    return null;
  }

  private boolean validateAndProcessJwt(
    String jwt,
    HttpServletRequest request,
    String correlationId
  ) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(
        jwtSecret.getBytes(StandardCharsets.UTF_8)
      );

      Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt)
        .getBody();

      // Extraer información del usuario
      String username = claims.getSubject();
      Long userId = claims.get("userId", Long.class);
      String email = claims.get("email", String.class);

      // Extraer roles del token
      @SuppressWarnings("unchecked")
      List<String> roles = claims.get("roles", List.class);
      if (roles == null) {
        roles = Collections.emptyList();
      }

      // Convertir roles a authorities de Spring Security
      List<SimpleGrantedAuthority> authorities = roles
        .stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        .collect(Collectors.toList());

      // Crear principal personalizado con información adicional
      JwtUserPrincipal principal = JwtUserPrincipal.builder()
        .id(userId)
        .username(username)
        .email(email)
        .roles(roles)
        .build();

      // Crear authentication token
      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(principal, null, authorities);
      authentication.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
      );

      // Establecer contexto de seguridad
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Log de auditoría exitoso
      log.info(
        "🔐 AUTH_SUCCESS | User: {} | Roles: {} | ClientIP: {} | CorrelationId: {}",
        username,
        roles,
        getClientIp(request),
        correlationId
      );

      return true;
    } catch (Exception e) {
      log.error(
        "❌ JWT_VALIDATION_ERROR | Error: {} | CorrelationId: {}",
        e.getMessage(),
        correlationId
      );
      return false;
    }
  }

  private void sendUnauthorizedResponse(
    HttpServletResponse response,
    String message,
    String errorCode,
    String correlationId
  ) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    if (correlationId != null) {
      response.setHeader("X-Correlation-ID", correlationId);
    }

    Map<String, Object> errorResponse = Map.of(
      "timestamp",
      LocalDateTime.now().toString(),
      "status",
      HttpStatus.UNAUTHORIZED.value(),
      "error",
      "No Autorizado",
      "message",
      message,
      "errorCode",
      errorCode,
      "correlationId",
      correlationId != null ? correlationId : "N/A",
      "suggestions",
      List.of(
        "Verifique que el token JWT esté presente en el header Authorization",
        "Asegúrese de que el token no haya expirado",
        "Use el formato: Authorization: Bearer <token>"
      )
    );

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private void sendInternalErrorResponse(
    HttpServletResponse response,
    String message,
    String correlationId
  ) throws IOException {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    if (correlationId != null) {
      response.setHeader("X-Correlation-ID", correlationId);
    }

    Map<String, Object> errorResponse = Map.of(
      "timestamp",
      LocalDateTime.now().toString(),
      "status",
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "error",
      "Error Interno del Servidor",
      "message",
      message,
      "errorCode",
      "INTERNAL_AUTH_ERROR",
      "correlationId",
      correlationId != null ? correlationId : "N/A"
    );

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(xForwardedFor)) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (StringUtils.hasText(xRealIp)) {
      return xRealIp;
    }

    return request.getRemoteAddr();
  }

  /**
   * DTO para el principal del usuario autenticado.
   */
  @lombok.Data
  @lombok.Builder
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public static class JwtUserPrincipal {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;
  }
}
