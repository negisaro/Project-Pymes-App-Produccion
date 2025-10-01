package com.nelson.project.msvc_usuario.msvc_usuario.security.service;

import com.nelson.project.msvc_usuario.msvc_usuario.security.TokenJwtConfig;
import io.jsonwebtoken.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Servicio profesional para manejo de JWT.
 * Clave y configuración obtenidas desde TokenJwtConfig para uniformidad y seguridad.
 * Métodos de generación, validación, parsing y refresh de tokens JWT.
 */
@Service
public class JwtService {

  private static final Logger logger = LoggerFactory.getLogger(
    JwtService.class
  );

  private final TokenJwtConfig tokenJwtConfig;

  // Eliminado valor fijo; se usa configuración externa en TokenJwtConfig

  public JwtService(TokenJwtConfig tokenJwtConfig) {
    this.tokenJwtConfig = tokenJwtConfig;
  }

  // ================= MÉTODOS PÚBLICOS PRINCIPALES =================

  /**
   * Genera un JWT válido para el usuario autenticado.
   */
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

    int expirationMinutes = tokenJwtConfig.getExpirationMinutes();
    String token = Jwts.builder()
      .setClaims(claims)
      .setSubject(userDetails.getUsername())
      .setIssuer(tokenJwtConfig.getIssuer())
      .setAudience(tokenJwtConfig.getAudience())
      .setIssuedAt(new Date())
      .setExpiration(
        new Date(
          System.currentTimeMillis() + (long) expirationMinutes * 60 * 1000
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

  /**
   * Parsea el token y devuelve los claims.
   */
  public Claims parseToken(String token) {
    return extractAllClaims(token);
  }

  /**
   * Refresca el token usando los claims del anterior.
   */
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

  /**
   * Extrae el username del token JWT.
   */
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

  /**
   * Valida si el token es correcto y pertenece al usuario.
   */
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

  // ================= MÉTODOS PRIVADOS AUXILIARES =================

  /**
   * Verifica si el token está expirado.
   */
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

  /**
   * Extrae todos los claims del token JWT.
   */
  private Claims extractAllClaims(String token) {
    try {
      List<SecretKey> keys = tokenJwtConfig.getAllSecretKeys();
      if (keys == null || keys.isEmpty()) {
        logger.error(
          "[JwtService] No hay claves JWT inicializadas en extractAllClaims"
        );
        throw new IllegalStateException("Sin claves JWT para verificación");
      }
      Claims claims = null;
      RuntimeException lastError = null;
      for (SecretKey k : keys) {
        try {
          claims = Jwts.parserBuilder()
            .setSigningKey(k)
            .build()
            .parseClaimsJws(token)
            .getBody();
          break; // verificación exitosa
        } catch (JwtException ex) {
          lastError = new RuntimeException(ex);
        }
      }
      if (claims == null) {
        throw new JwtException(
          "No se pudo verificar el token con ninguna clave",
          lastError
        );
      }
      // Validación de issuer y audience configurados
      String expectedIssuer = tokenJwtConfig.getIssuer();
      if (expectedIssuer != null && !expectedIssuer.isBlank()) {
        if (
          claims.getIssuer() == null ||
          !expectedIssuer.equals(claims.getIssuer())
        ) {
          throw new JwtException("Issuer inválido");
        }
      }
      String expectedAudience = tokenJwtConfig.getAudience();
      if (expectedAudience != null && !expectedAudience.isBlank()) {
        String tokenAudience = claims.getAudience();
        if (
          tokenAudience == null || !tokenAudience.contains(expectedAudience)
        ) {
          throw new JwtException("Audience inválida");
        }
      }
      logger.debug("[JwtService] Claims extraídos: {}", claims);
      return claims;
    } catch (JwtException ex) {
      logger.error("[JwtService] Error al parsear claims: {}", ex.getMessage());
      throw ex;
    }
  }
}
