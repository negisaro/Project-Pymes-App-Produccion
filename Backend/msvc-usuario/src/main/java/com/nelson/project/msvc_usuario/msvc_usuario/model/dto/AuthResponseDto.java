package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Respuesta estándar para autenticación y verificación de sesión.
 * Incluye access token y refresh token (si aplica), más metadatos.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {

  private String accessToken;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
  private Instant expiresAt;

  private String refreshToken;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
  private Instant refreshExpiresAt;

  private String username;
  private List<String> roles;
}