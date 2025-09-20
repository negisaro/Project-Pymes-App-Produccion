package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para login de usuario.
 * Profesional, funcional, moderno y uniforme con otros DTOs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Nombre de usuario */
  @NotBlank
  private String username;

  /** Contraseña */
  @NotBlank
  private String password;
}
