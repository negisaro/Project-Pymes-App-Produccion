package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para categoría.
 * Representa los datos transferidos de categorías.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único de la categoría */
  private Long id;

  /** Nombre de la categoría */
  private String nombre;

  /** Descripción de la categoría */
  private String descripcion;
}
