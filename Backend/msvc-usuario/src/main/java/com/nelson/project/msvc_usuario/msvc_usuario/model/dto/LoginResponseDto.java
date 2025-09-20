package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para login de usuario.
 * Profesional, funcional, moderno y uniforme con otros DTOs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del usuario */
  private Long id;

  /** Nombre del usuario */
  private String name;

  /** Apellido del usuario */
  private String lastname;

  /** Correo electrónico del usuario */
  private String email;

  /** Nombre de usuario */
  private String username;

  /** Estado de activación */
  private Boolean active;

  /** Roles asignados */
  private List<RolDto> roles;

  /** Token JWT de autenticación */
  private String token;
}
