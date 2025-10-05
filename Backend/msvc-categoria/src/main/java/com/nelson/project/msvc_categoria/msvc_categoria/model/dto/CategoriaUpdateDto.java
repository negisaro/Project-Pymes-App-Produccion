package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.EstadoCategoria;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.TipoCategoria;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar categorías existentes.
 * Incluye solo los campos que pueden ser modificados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaUpdateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  // ========================================
  // CAMPOS BÁSICOS MODIFICABLES
  // ========================================

  /** Nombre de la categoría */
  @Size(
    min = 2,
    max = 100,
    message = "El nombre debe tener entre 2 y 100 caracteres"
  )
  private String nombre;

  /** Descripción detallada */
  @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
  private String descripcion;

  /** Estado activo/inactivo */
  private Boolean activo;

  // ========================================
  // JERARQUÍA MODIFICABLE
  // ========================================

  /** Cambiar categoría padre */
  private Long categoriaPadreId;

  // ========================================
  // VISUALIZACIÓN MODIFICABLE
  // ========================================

  /** Orden para mostrar en listas */
  @Min(value = 0, message = "El orden de visualización no puede ser negativo")
  private Integer ordenVisualizacion;

  /** Color asociado en hexadecimal */
  @Pattern(
    regexp = "^#[0-9A-Fa-f]{6}$",
    message = "El color debe ser un código hexadecimal válido"
  )
  private String colorHex;

  /** Nombre del icono */
  @Size(
    max = 50,
    message = "El nombre del icono no puede exceder 50 caracteres"
  )
  private String icono;

  /** URL de imagen principal */
  @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
  private String imagenUrl;

  /** URL de thumbnail */
  @Size(
    max = 500,
    message = "La URL de thumbnail no puede exceder 500 caracteres"
  )
  private String imagenThumbnailUrl;

  // ========================================
  // SEO MODIFICABLE
  // ========================================

  /** Slug para URLs amigables */
  @Pattern(
    regexp = "^[a-z0-9-]*$",
    message = "El slug solo puede contener letras minúsculas, números y guiones"
  )
  @Size(max = 150, message = "El slug no puede exceder 150 caracteres")
  private String slug;

  /** Meta título para SEO */
  @Size(max = 160, message = "El meta título no puede exceder 160 caracteres")
  private String metaTitulo;

  /** Meta descripción para SEO */
  @Size(
    max = 320,
    message = "La meta descripción no puede exceder 320 caracteres"
  )
  private String metaDescripcion;

  /** Palabras clave separadas por comas */
  @Size(
    max = 500,
    message = "Las palabras clave no pueden exceder 500 caracteres"
  )
  private String palabrasClave;

  // ========================================
  // CONFIGURACIÓN DE NEGOCIO MODIFICABLE
  // ========================================

  /** Permite productos directamente */
  private Boolean permiteProductos;

  /** Requiere aprobación para productos */
  private Boolean requiereAprobacion;

  /** Visible en menús públicos */
  private Boolean visibleEnMenu;

  /** Categoría destacada */
  private Boolean destacada;

  /** Porcentaje de comisión para marketplace */
  @DecimalMin(value = "0.00", message = "La comisión no puede ser negativa")
  @DecimalMax(value = "100.00", message = "La comisión no puede exceder 100%")
  private BigDecimal comisionPorcentaje;

  // ========================================
  // CONFIGURACIÓN DE PRODUCTOS MODIFICABLE
  // ========================================

  /** Precio mínimo permitido */
  @DecimalMin(
    value = "0.00",
    message = "El precio mínimo no puede ser negativo"
  )
  private BigDecimal precioMinimo;

  /** Precio máximo permitido */
  private BigDecimal precioMaximo;

  /** Requiere control de inventario */
  private Boolean requiereInventario;

  /** Permite variantes (tallas, colores) */
  private Boolean permiteVariantes;

  // ========================================
  // CONFIGURACIÓN AVANZADA MODIFICABLE
  // ========================================

  /** Configuraciones específicas en JSON */
  @Size(
    max = 2000,
    message = "La configuración JSON no puede exceder 2000 caracteres"
  )
  private String configuracionJson;

  /** Template específico para productos */
  @Size(
    max = 1000,
    message = "La plantilla de producto no puede exceder 1000 caracteres"
  )
  private String plantillaProducto;

  /** Departamento empresarial */
  @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
  private String departamento;

  /** Tipo de categoría */
  private TipoCategoria tipo;

  /** Estado de aprobación */
  private EstadoCategoria estadoAprobacion;

  /** Motivo del cambio (para auditoría) */
  @Size(
    max = 500,
    message = "El motivo del cambio no puede exceder 500 caracteres"
  )
  private String motivoUltimoCambio;

  // ========================================
  // VALIDACIONES CRUZADAS
  // ========================================

  @AssertTrue(message = "El precio máximo debe ser mayor que el precio mínimo")
  private boolean isPrecioMaximoValido() {
    if (precioMinimo == null || precioMaximo == null) {
      return true; // Si alguno es nulo, no validamos
    }
    return precioMaximo.compareTo(precioMinimo) >= 0;
  }
}
