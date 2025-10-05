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
 * DTO para crear nuevas categorías.
 * Incluye solo los campos necesarios para la creación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaCreateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  // ========================================
  // CAMPOS BÁSICOS REQUERIDOS
  // ========================================

  /** Nombre de la categoría (REQUERIDO) */
  @NotBlank(message = "El nombre es obligatorio")
  @Size(
    min = 2,
    max = 100,
    message = "El nombre debe tener entre 2 y 100 caracteres"
  )
  private String nombre;

  /** Código único alfanumérico (OPCIONAL - se genera automáticamente si no se proporciona) */
  @Pattern(
    regexp = "^[A-Z0-9_-]+$",
    message = "El código solo puede contener letras mayúsculas, números, guiones y guiones bajos"
  )
  @Size(max = 20, message = "El código no puede exceder 20 caracteres")
  private String codigo;

  /** Descripción detallada (OPCIONAL) */
  @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
  private String descripcion;

  /** Estado activo/inactivo (DEFAULT: true) */
  @Builder.Default
  private Boolean activo = true;

  // ========================================
  // JERARQUÍA (OPCIONAL)
  // ========================================

  /** ID de la categoría padre para crear subcategorías */
  private Long categoriaPadreId;

  // ========================================
  // VISUALIZACIÓN (OPCIONAL)
  // ========================================

  /** Orden para mostrar en listas */
  @Min(value = 0, message = "El orden de visualización no puede ser negativo")
  @Builder.Default
  private Integer ordenVisualizacion = 0;

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

  // ========================================
  // SEO (OPCIONAL)
  // ========================================

  /** Slug para URLs amigables (se genera automáticamente si no se proporciona) */
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
  // CONFIGURACIÓN DE NEGOCIO (OPCIONALES CON DEFAULTS)
  // ========================================

  /** Permite productos directamente */
  @Builder.Default
  private Boolean permiteProductos = true;

  /** Requiere aprobación para productos */
  @Builder.Default
  private Boolean requiereAprobacion = false;

  /** Visible en menús públicos */
  @Builder.Default
  private Boolean visibleEnMenu = true;

  /** Categoría destacada */
  @Builder.Default
  private Boolean destacada = false;

  /** Porcentaje de comisión para marketplace */
  @DecimalMin(value = "0.00", message = "La comisión no puede ser negativa")
  @DecimalMax(value = "100.00", message = "La comisión no puede exceder 100%")
  private BigDecimal comisionPorcentaje;

  // ========================================
  // CONFIGURACIÓN DE PRODUCTOS (OPCIONALES)
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
  @Builder.Default
  private Boolean requiereInventario = true;

  /** Permite variantes (tallas, colores) */
  @Builder.Default
  private Boolean permiteVariantes = true;

  // ========================================
  // CONFIGURACIÓN AVANZADA (OPCIONAL)
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
  @Builder.Default
  private TipoCategoria tipo = TipoCategoria.PRODUCTO;

  /** Estado de aprobación inicial */
  @Builder.Default
  private EstadoCategoria estadoAprobacion = EstadoCategoria.APROBADA;

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
