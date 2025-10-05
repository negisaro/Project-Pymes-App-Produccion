package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.TipoCategoria;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para listados de categorías.
 * Solo incluye los campos esenciales para optimizar performance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriaSummaryDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único */
  private Long id;

  /** Nombre de la categoría */
  private String nombre;

  /** Código único */
  private String codigo;

  /** Descripción corta */
  private String descripcion;

  /** Estado activo/inactivo */
  private Boolean activo;

  /** ID de la categoría padre */
  private Long categoriaPadreId;

  /** Nombre de la categoría padre */
  private String categoriaPadreNombre;

  /** Nivel en la jerarquía */
  private Integer nivel;

  /** Orden de visualización */
  private Integer ordenVisualizacion;

  /** Color asociado */
  private String colorHex;

  /** Icono asociado */
  private String icono;

  /** URL de thumbnail */
  private String imagenThumbnailUrl;

  /** Slug para URLs */
  private String slug;

  /** Visible en menús */
  private Boolean visibleEnMenu;

  /** Es destacada */
  private Boolean destacada;

  /** Tipo de categoría */
  private TipoCategoria tipo;

  /** Total de productos */
  private Long totalProductos;

  /** Score de popularidad */
  private Double popularidad;

  /** Está eliminada */
  private Boolean eliminado;

  // ========================================
  // MÉTODOS DE UTILIDAD
  // ========================================

  /** Indica si es categoría raíz */
  public boolean esRaiz() {
    return categoriaPadreId == null;
  }

  /** Está disponible (activo y no eliminado) */
  public boolean estaDisponible() {
    return Boolean.TRUE.equals(activo) && !Boolean.TRUE.equals(eliminado);
  }
}
