package com.nelson.project.msvc_categoria.msvc_categoria.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenJwtConfig {

  private static final Logger logger = LoggerFactory.getLogger(
    TokenJwtConfig.class
  );

  @Value("${jwt.secret.jwt-secret}")
  private String jwtSecretBase64;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    if (
      jwtSecretBase64 == null ||
      jwtSecretBase64.isEmpty() ||
      "GATEWAY_JWT_SECRET".equals(jwtSecretBase64)
    ) {
      logger.error(
        "GATEWAY_JWT_SECRET no está definida o es inválida. Aborta el arranque."
      );
      throw new IllegalStateException(
        "Falta la variable de entorno GATEWAY_JWT_SECRET o valor no válido"
      );
    }
    try {
      byte[] decoded = Decoders.BASE64.decode(jwtSecretBase64);
      this.secretKey = Keys.hmacShaKeyFor(decoded);
      logger.info(
        "JWT SecretKey inicializada correctamente (sin mostrar valor)."
      );
    } catch (Exception e) {
      logger.error(
        "Clave JWT inválida. Revisa la variable GATEWAY_JWT_SECRET."
      );
      throw new IllegalArgumentException("Clave JWT inválida", e);
    }
  }

  public SecretKey getSecretKey() {
    return secretKey;
  }

  public static final String PREFIX_TOKEN = "Bearer ";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String CONTENT_TYPE = "application/json";
}
