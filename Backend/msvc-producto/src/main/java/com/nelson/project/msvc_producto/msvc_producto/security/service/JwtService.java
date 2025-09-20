package com.nelson.project.msvc_producto.msvc_producto.security.service;

import com.nelson.project.msvc_producto.msvc_producto.security.TokenJwtConfig;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  // Clave secreta y configuración obtenida de TokenJwtConfig
  private static final SecretKey SECRET_KEY = TokenJwtConfig.SECRET_KEY;

  @SuppressWarnings("unused")
  private static final int TOKEN_EXPIRATION_MINUTES = 60;

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  @SuppressWarnings("unchecked")
  public List<String> extractRoles(String token) {
    return extractAllClaims(token).get("roles", List.class);
  }

  public boolean isTokenValid(String token, String username) {
    String tokenUsername = extractUsername(token);
    return (
      tokenUsername != null &&
      tokenUsername.equals(username) &&
      !isTokenExpired(token)
    );
  }

  public boolean isTokenExpired(String token) {
    Date expiration = extractAllClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(SECRET_KEY)
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
