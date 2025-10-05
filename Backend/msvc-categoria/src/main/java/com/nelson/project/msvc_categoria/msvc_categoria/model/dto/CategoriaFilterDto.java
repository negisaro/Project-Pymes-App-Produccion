package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.EstadoCategoria;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.TipoCategoria;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para filtrar y buscar categorías.
 * Incluye criterios de búsqueda avanzada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaFilterDto implements Serializable {

  private static final long serialVersionUID = 1L;

  // ========================================
  // FILTROS BÁSICOS
  // ========================================

  /** Búsqueda por texto en nombre o descripción */
  @Size(
    max = 255,
    message = "El texto de búsqueda no puede exceder 255 caracteres"
  )
  private String texto;

  /** Filtro por estado activo/inactivo */
  private Boolean activo;

  /** Filtros por código */
  @Pattern(
    regexp = "^[A-Z0-9_-]*$",
    message = "El código solo puede contener letras mayúsculas, números, guiones y guiones bajos"
  )
  private String codigo;

  /** Lista de IDs específicos */
  private List<Long> ids;

  // ========================================
  // FILTROS DE JERARQUÍA
  // ========================================

  /** Filtro por categoría padre */
  private Long categoriaPadreId;

  /** Solo categorías raíz (sin padre) */
  private Boolean soloRaiz;

  /** Solo categorías que tienen subcategorías */
  private Boolean soloConSubcategorias;

  /** Filtro por nivel específico */
  private Integer nivel;

  /** Nivel mínimo */
  private Integer nivelMinimo;

  /** Nivel máximo */
  private Integer nivelMaximo;

  // ========================================
  // FILTROS DE VISUALIZACIÓN
  // ========================================

  /** Filtro por visibilidad en menú */
  private Boolean visibleEnMenu;

  /** Solo categorías destacadas */
  private Boolean destacada;

  /** Filtro por departamento */
  @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
  private String departamento;

  /** Filtro por tipo de categoría */
  private TipoCategoria tipo;

  /** Lista de tipos de categoría */
  private List<TipoCategoria> tipos;

  // ========================================
  // FILTROS DE CONFIGURACIÓN
  // ========================================

  /** Solo categorías que permiten productos */
  private Boolean permiteProductos;

  /** Solo categorías que requieren aprobación */
  private Boolean requiereAprobacion;

  /** Filtro por estado de aprobación */
  private EstadoCategoria estadoAprobacion;

  /** Lista de estados de aprobación */
  private List<EstadoCategoria> estadosAprobacion;

  // ========================================
  // FILTROS DE MÉTRICAS
  // ========================================

  /** Mínimo número de productos */
  private Long totalProductosMinimo;

  /** Máximo número de productos */
  private Long totalProductosMaximo;

  /** Mínimo número de ventas */
  private Long totalVentasMinimo;

  /** Máximo número de ventas */
  private Long totalVentasMaximo;

  /** Ingresos mínimos */
  private BigDecimal ingresosMínimos;

  /** Ingresos máximos */
  private BigDecimal ingresosMaximos;

  /** Score mínimo de popularidad */
  private Double popularidadMinima;

  /** Score máximo de popularidad */
  private Double popularidadMaxima;

  // ========================================
  // FILTROS TEMPORALES
  // ========================================

  /** Fecha de creación desde */
  private LocalDateTime fechaCreacionDesde;

  /** Fecha de creación hasta */
  private LocalDateTime fechaCreacionHasta;

  /** Fecha de modificación desde */
  private LocalDateTime fechaModificacionDesde;

  /** Fecha de modificación hasta */
  private LocalDateTime fechaModificacionHasta;

  /** Creado por usuario específico */
  @Size(
    max = 100,
    message = "El nombre de usuario no puede exceder 100 caracteres"
  )
  private String creadoPor;

  /** Modificado por usuario específico */
  @Size(
    max = 100,
    message = "El nombre de usuario no puede exceder 100 caracteres"
  )
  private String modificadoPor;

  // ========================================
  // FILTROS DE SOFT DELETE
  // ========================================

  /** Incluir categorías eliminadas */
  private Boolean incluirEliminadas;

  /** Solo categorías eliminadas */
  private Boolean soloEliminadas;

  /** Eliminado por usuario específico */
  @Size(
    max = 100,
    message = "El nombre de usuario no puede exceder 100 caracteres"
  )
  private String eliminadoPor;

  // ========================================
  // CONFIGURACIÓN DE ORDENAMIENTO
  // ========================================

  /** Campo para ordenar */
  private String ordenarPor;

  /** Dirección del ordenamiento */
  private String direccionOrden; // ASC, DESC

  // ========================================
  // UTILIDADES
  // ========================================

  /** Verifica si hay filtros aplicados */
  public boolean tieneParametros() {
    return (
      texto != null ||
      activo != null ||
      codigo != null ||
      (ids != null && !ids.isEmpty()) ||
      categoriaPadreId != null ||
      soloRaiz != null ||
      nivel != null ||
      visibleEnMenu != null ||
      destacada != null ||
      departamento != null ||
      tipo != null ||
      totalProductosMinimo != null ||
      fechaCreacionDesde != null
    );
  }

  /** Verifica si es búsqueda por texto */
  public boolean esBusquedaTexto() {
    return texto != null && !texto.trim().isEmpty();
  }
}
