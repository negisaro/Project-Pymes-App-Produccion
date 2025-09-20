package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Rol.
 * Profesional, funcional, moderno y uniforme con otros DTOs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del rol */
  private Long id;

  /** Nombre del rol */
  private String name;

  /** Estado de activación del rol */
  private Boolean activo;
}
