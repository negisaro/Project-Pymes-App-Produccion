package com.nelson.project.msvc_categoria.msvc_categoria.security.service;

import com.nelson.project.msvc_categoria.msvc_categoria.security.TokenJwtConfig;
import io.jsonwebtoken.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final Logger logger = LoggerFactory.getLogger(
    JwtService.class
  );

  private final TokenJwtConfig tokenJwtConfig;
  private static final int TOKEN_EXPIRATION_MINUTES = 60;

  public JwtService(TokenJwtConfig tokenJwtConfig) {
    this.tokenJwtConfig = tokenJwtConfig;
  }

  public String generateToken(
    UserDetails userDetails,
    Collection<? extends GrantedAuthority> authorities,
    String email
  ) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(
      "roles",
      authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","))
    );
    claims.put("email", email);
    claims.put("username", userDetails.getUsername());

    SecretKey key = tokenJwtConfig.getSecretKey();
    if (key == null) {
      logger.error(
        "[JwtService] SECRET_KEY no inicializada. No se puede firmar el token."
      );
      throw new IllegalStateException(
        "Clave JWT no inicializada. Revisa la configuración de TokenJwtConfig."
      );
    }

    String token = Jwts.builder()
      .setClaims(claims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(
        new Date(
          System.currentTimeMillis() + TOKEN_EXPIRATION_MINUTES * 60 * 1000
        )
      )
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();

    logger.info(
      "[JwtService] Token generado para usuario: {}",
      userDetails.getUsername()
    );
    return token;
  }

  public Claims parseToken(String token) {
    return extractAllClaims(token);
  }

  public String refreshToken(String oldToken, String email) {
    Claims claims = extractAllClaims(oldToken);
    String username = claims.get("username", String.class);
    String roles = claims.get("roles", String.class);

    org.springframework.security.core.userdetails.User userDetails =
      new org.springframework.security.core.userdetails.User(
        username,
        "",
        roles != null
          ? Arrays.stream(roles.split(","))
            .map(String::trim)
            .filter(r -> !r.isEmpty())
            .map(r -> (GrantedAuthority) () -> r)
            .collect(Collectors.toList())
          : Collections.emptyList()
      );
    return generateToken(userDetails, userDetails.getAuthorities(), email);
  }

  public String extractUsername(String token) {
    try {
      return extractAllClaims(token).getSubject();
    } catch (Exception ex) {
      logger.error(
        "[JwtService] Error al extraer username: {}",
        ex.getMessage()
      );
      return null;
    }
  }

  public String extractRolesString(String token) {
    try {
      Claims claims = extractAllClaims(token);
      return claims.get("roles", String.class);
    } catch (Exception ex) {
      logger.error("[JwtService] Error al extraer roles: {}", ex.getMessage());
      return null;
    }
  }

  public List<String> extractRoles(String token) {
    String rolesString = extractRolesString(token);
    if (
      rolesString == null || rolesString.isEmpty()
    ) return Collections.emptyList();
    return Arrays.stream(rolesString.split(","))
      .map(String::trim)
      .filter(r -> !r.isEmpty())
      .collect(Collectors.toList());
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      String username = extractUsername(token);
      boolean valid =
        username != null &&
        username.equals(userDetails.getUsername()) &&
        !isTokenExpired(token);
      if (!valid) {
        logger.warn(
          "[JwtService] Token inválido para usuario: {}",
          userDetails.getUsername()
        );
      }
      return valid;
    } catch (Exception ex) {
      logger.error("[JwtService] Error al validar token: {}", ex.getMessage());
      return false;
    }
  }

  private boolean isTokenExpired(String token) {
    try {
      Date expiration = extractAllClaims(token).getExpiration();
      boolean expired = expiration.before(new Date());
      if (expired) {
        logger.warn("[JwtService] Token expirado");
      }
      return expired;
    } catch (Exception ex) {
      logger.error(
        "[JwtService] Error al verificar expiración: {}",
        ex.getMessage()
      );
      return true;
    }
  }

  private Claims extractAllClaims(String token) {
    try {
      SecretKey key = tokenJwtConfig.getSecretKey();
      if (key == null) {
        logger.error(
          "[JwtService] SECRET_KEY no inicializada en extractAllClaims"
        );
        throw new IllegalStateException("Clave JWT no inicializada.");
      }
      Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
      logger.debug("[JwtService] Claims extraídos: {}", claims);
      return claims;
    } catch (JwtException ex) {
      logger.error("[JwtService] Error al parsear claims: {}", ex.getMessage());
      throw ex;
    }
  }
}
