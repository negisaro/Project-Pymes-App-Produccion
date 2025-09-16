package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para usuario.
 * Representa los datos transferidos entre servicios y capas de la aplicación.
 * Implementa Serializable para compatibilidad con frameworks y transferencias.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del usuario */
  private Long id;

  /** Nombre de usuario */
  private String username;

  /** Correo electrónico */
  private String email;

  /** Roles asignados al usuario */
  private List<String> roles;

  /** Estado de activación del usuario */
  private boolean active;
}
