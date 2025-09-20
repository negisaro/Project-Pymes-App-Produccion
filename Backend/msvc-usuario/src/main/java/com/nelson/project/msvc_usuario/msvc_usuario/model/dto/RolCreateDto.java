package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un rol.
 * Solo incluye los campos necesarios para crear un rol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolCreateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Nombre del rol */
  @NotBlank
  private String name;

  /** Estado de activación del rol (opcional, por defecto true) */
  private Boolean activo;
}
