package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del producto */
  private Long id;

  /** Nombre del producto */
  private String nombre;

  /** Descripción del producto */
  private String descripcion;

  /** Fecha de creación */
  private LocalDateTime creadoEn;

  /** Fecha de última actualización */
  private LocalDateTime actualizadoEn;

  /** Estado del producto (activo/inactivo) */
  private Boolean estado;
}
