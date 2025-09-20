package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Usuario.
 * Profesional, funcional, moderno y uniforme con otros MSVC.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del usuario */
  private Long id;

  /** Nombre del usuario */
  @NotBlank
  @Size(max = 50)
  private String name;

  /** Apellido del usuario */
  @NotBlank
  @Size(max = 50)
  private String lastname;

  /** Nombre de usuario */
  @NotBlank
  @Size(min = 4, max = 12)
  private String username;

  /** Contraseña del usuario */
  @NotBlank
  @Size(min = 6, max = 100)
  private String password;

  /** Correo electrónico del usuario */
  @NotBlank
  @Email
  private String email;

  /** Roles asignados al usuario */
  private List<RolDto> roles;

  /** Estado de activación */
  private Boolean active;
}
