package com.nelson.project.msvc_usuario.msvc_usuario.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuración profesional y robusta para el manejo de JWT en la aplicación.
 * Lee la clave secreta desde la configuración externa (YAML/ENV) para garantizar uniformidad con el Gateway.
 * Proporciona claves, prefijos y cabeceras estándar para seguridad empresarial.
 */
@Component
public class TokenJwtConfig {

  private static final Logger logger = LoggerFactory.getLogger(
    TokenJwtConfig.class
  );

  /**
   * Puede contener uno o varios secretos Base64 separados por coma.
   * El primero se usa para firmar; todos se aceptan para verificación (rotación simple).
   */
  @Value("${jwt.secret}")
  private String jwtSecretsRaw;

  @Value("${jwt.expiration-minutes:60}")
  private int expirationMinutes;

  @Value("${jwt.refresh-expiration-minutes:43200}") // 30 días por defecto
  private int refreshExpirationMinutes;

  @Value("${jwt.issuer:project-pymes}")
  private String issuer;

  @Value("${jwt.audience:project-pymes-clients}")
  private String audience;

  private SecretKey primarySecretKey;
  private List<SecretKey> allSecretKeys = new ArrayList<>();

  @PostConstruct
  public void init() {
    if (
      jwtSecretsRaw == null ||
      jwtSecretsRaw.isEmpty() ||
      "GATEWAY_JWT_SECRET".equals(jwtSecretsRaw)
    ) {
      logger.error(
        "GATEWAY_JWT_SECRET no está definida o es inválida. Aborta el arranque."
      );
      throw new IllegalStateException(
        "Falta la variable de entorno GATEWAY_JWT_SECRET o valor no válido"
      );
    }
    String[] parts = jwtSecretsRaw.split(",");
    for (int i = 0; i < parts.length; i++) {
      String part = parts[i].trim();
      if (part.isEmpty()) continue;
      try {
        byte[] decoded = Decoders.BASE64.decode(part);
        int bitLength = decoded.length * 8;
        if (bitLength < 256) {
          throw new IllegalArgumentException(
            "Clave JWT con longitud insuficiente (" +
            bitLength +
            " bits). Requiere >=256 bits"
          );
        }
        SecretKey key = Keys.hmacShaKeyFor(decoded);
        if (i == 0) {
          this.primarySecretKey = key;
        }
        this.allSecretKeys.add(key);
      } catch (Exception e) {
        logger.error(
          "Clave JWT inválida en posición {}. Detalle: {}",
          i,
          e.getMessage()
        );
        throw new IllegalArgumentException("Clave JWT inválida", e);
      }
    }
    logger.info(
      "JWT SecretKey primaria inicializada y {} claves totales cargadas (rotación básica).",
      allSecretKeys.size()
    );
  }

  public SecretKey getSecretKey() {
    return primarySecretKey;
  }

  /**
   * Todas las claves aceptadas para verificación.
   */
  public List<SecretKey> getAllSecretKeys() {
    return Collections.unmodifiableList(allSecretKeys);
  }

  public int getExpirationMinutes() {
    return expirationMinutes;
  }

  public int getRefreshExpirationMinutes() {
    return refreshExpirationMinutes;
  }

  public static final String PREFIX_TOKEN = "Bearer ";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String CONTENT_TYPE = "application/json";

  public String getIssuer() {
    return issuer;
  }

  public String getAudience() {
    return audience;
  }
}
