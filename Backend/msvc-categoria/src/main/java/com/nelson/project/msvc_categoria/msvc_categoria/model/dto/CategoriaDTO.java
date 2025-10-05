package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.EstadoCategoria;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria.TipoCategoria;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO completo para transferencia de datos de Categoria.
 * Incluye todos los campos para respuestas completas de API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  // ========================================
  // CAMPOS BÁSICOS
  // ========================================

  /** Identificador único de la categoría */
  private Long id;

  /** Nombre de la categoría */
  private String nombre;

  /** Código único alfanumérico */
  private String codigo;

  /** Descripción detallada */
  private String descripcion;

  /** Estado activo/inactivo */
  private Boolean activo;

  // ========================================
  // JERARQUÍA
  // ========================================

  /** ID de la categoría padre (si es subcategoría) */
  private Long categoriaPadreId;

  /** Nombre de la categoría padre */
  private String categoriaPadreNombre;

  /** Lista de subcategorías */
  private List<CategoriaDTO> subcategorias;

  /** Nivel en la jerarquía (0 = raíz) */
  private Integer nivel;

  /** Ruta completa en texto */
  private String rutaCompleta;

  // ========================================
  // VISUALIZACIÓN
  // ========================================

  /** Orden para mostrar en listas */
  private Integer ordenVisualizacion;

  /** Color asociado en hexadecimal */
  private String colorHex;

  /** Nombre del icono */
  private String icono;

  /** URL de imagen principal */
  private String imagenUrl;

  /** URL de thumbnail */
  private String imagenThumbnailUrl;

  // ========================================
  // SEO Y MARKETING
  // ========================================

  /** Slug para URLs amigables */
  private String slug;

  /** Meta título para SEO */
  private String metaTitulo;

  /** Meta descripción para SEO */
  private String metaDescripcion;

  /** Palabras clave separadas por comas */
  private String palabrasClave;

  // ========================================
  // CONFIGURACIÓN DE NEGOCIO
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
  private BigDecimal comisionPorcentaje;

  // ========================================
  // CONFIGURACIÓN DE PRODUCTOS
  // ========================================

  /** Precio mínimo permitido */
  private BigDecimal precioMinimo;

  /** Precio máximo permitido */
  private BigDecimal precioMaximo;

  /** Requiere control de inventario */
  private Boolean requiereInventario;

  /** Permite variantes (tallas, colores) */
  private Boolean permiteVariantes;

  // ========================================
  // MÉTRICAS Y ANALÍTICAS
  // ========================================

  /** Total de productos en la categoría */
  private Long totalProductos;

  /** Total de ventas realizadas */
  private Long totalVentas;

  /** Ingresos totales generados */
  private BigDecimal ingresosTotales;

  /** Total de vistas de la categoría */
  private Long vistasTotal;

  /** Score de popularidad calculado */
  private Double popularidad;

  // ========================================
  // CONFIGURACIÓN AVANZADA
  // ========================================

  /** Configuraciones específicas en JSON */
  private String configuracionJson;

  /** Template específico para productos */
  private String plantillaProducto;

  /** Departamento empresarial */
  private String departamento;

  /** Tipo de categoría */
  private TipoCategoria tipo;

  /** Estado de aprobación */
  private EstadoCategoria estadoAprobacion;

  // ========================================
  // AUDITORÍA
  // ========================================

  /** Fecha de creación */
  private LocalDateTime fechaCreacion;

  /** Fecha de última modificación */
  private LocalDateTime fechaModificacion;

  /** Usuario que creó la categoría */
  private String creadoPor;

  /** Usuario que modificó por última vez */
  private String modificadoPor;

  /** Versión del registro */
  private Long versionRegistro;

  /** Motivo del último cambio */
  private String motivoUltimoCambio;

  // ========================================
  // SOFT DELETE
  // ========================================

  /** Indica si está eliminada lógicamente */
  private Boolean eliminado;

  /** Usuario que eliminó */
  private String eliminadoPor;

  /** Motivo de la eliminación */
  private String motivoEliminacion;

  // ========================================
  // CAMPOS CALCULADOS
  // ========================================

  /** Indica si es categoría raíz */
  public boolean esRaiz() {
    return categoriaPadreId == null;
  }

  /** Indica si tiene subcategorías */
  public boolean tieneSubcategorias() {
    return subcategorias != null && !subcategorias.isEmpty();
  }

  /** Cantidad de subcategorías */
  public int cantidadSubcategorias() {
    return subcategorias != null ? subcategorias.size() : 0;
  }

  /** Está disponible (activo y no eliminado) */
  public boolean estaDisponible() {
    return Boolean.TRUE.equals(activo) && !Boolean.TRUE.equals(eliminado);
  }
}
