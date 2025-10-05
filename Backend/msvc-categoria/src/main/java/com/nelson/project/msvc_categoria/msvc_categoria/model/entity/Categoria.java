package com.nelson.project.msvc_categoria.msvc_categoria.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

/**
 * Entidad Categoria robusta y empresarial para gestión de categorías de productos.
 * Incluye campos para jerarquía, SEO, configuración, métricas y auditoría completa.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
  name = "categorias",
  indexes = {
    @Index(name = "idx_categoria_nombre", columnList = "nombre"),
    @Index(name = "idx_categoria_codigo", columnList = "codigo"),
    @Index(name = "idx_categoria_activo", columnList = "activo"),
    @Index(name = "idx_categoria_parent", columnList = "categoria_padre_id"),
    @Index(name = "idx_categoria_orden", columnList = "orden_visualizacion"),
    @Index(name = "idx_categoria_deleted", columnList = "eliminado"),
  },
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_categoria_codigo", columnNames = "codigo"),
    @UniqueConstraint(name = "uk_categoria_slug", columnNames = "slug"),
  }
)
@EntityListeners(AuditingEntityListener.class)
public class Categoria extends AuditableEntity {

  // ========================================
  // CAMPOS BÁSICOS
  // ========================================

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  @NotBlank(message = "El nombre es obligatorio")
  @Size(
    min = 2,
    max = 100,
    message = "El nombre debe tener entre 2 y 100 caracteres"
  )
  private String nombre;

  @Column(length = 20, nullable = false, unique = true)
  @NotBlank(message = "El código es obligatorio")
  @Pattern(
    regexp = "^[A-Z0-9_-]+$",
    message = "El código solo puede contener letras mayúsculas, números, guiones y guiones bajos"
  )
  private String codigo;

  @Column(length = 2000)
  @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
  private String descripcion;

  @Column(nullable = false)
  @Builder.Default
  private Boolean activo = true;

  // ========================================
  // JERARQUÍA DE CATEGORÍAS
  // ========================================

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "categoria_padre_id",
    foreignKey = @ForeignKey(name = "fk_categoria_padre")
  )
  private Categoria categoriaPadre;

  @OneToMany(
    mappedBy = "categoriaPadre",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL
  )
  @Builder.Default
  private List<Categoria> subcategorias = new ArrayList<>();

  @Column(nullable = false)
  @Min(value = 0, message = "El nivel no puede ser negativo")
  @Max(value = 10, message = "El nivel máximo es 10")
  @Builder.Default
  private Integer nivel = 0;

  @Column(length = 500)
  private String rutaCompleta; // Ej: "Electrónicos > Smartphones > Android"

  // ========================================
  // VISUALIZACIÓN Y ORDENAMIENTO
  // ========================================

  @Column(name = "orden_visualizacion", nullable = false)
  @Min(value = 0, message = "El orden de visualización no puede ser negativo")
  @Builder.Default
  private Integer ordenVisualizacion = 0;

  @Column(length = 7)
  @Pattern(
    regexp = "^#[0-9A-Fa-f]{6}$",
    message = "El color debe ser un código hexadecimal válido"
  )
  private String colorHex; // Para UI/UX

  @Column(length = 50)
  private String icono; // Nombre del icono (ej: "fa-laptop", "material-icons:phone")

  @Column(length = 500)
  private String imagenUrl;

  @Column(length = 500)
  private String imagenThumbnailUrl;

  // ========================================
  // SEO Y MARKETING
  // ========================================

  @Column(length = 150, unique = true)
  @Pattern(
    regexp = "^[a-z0-9-]+$",
    message = "El slug solo puede contener letras minúsculas, números y guiones"
  )
  private String slug; // URL amigable: "smartphones-android"

  @Column(length = 160)
  @Size(max = 160, message = "El meta título no puede exceder 160 caracteres")
  private String metaTitulo; // Para SEO

  @Column(length = 320)
  @Size(
    max = 320,
    message = "La meta descripción no puede exceder 320 caracteres"
  )
  private String metaDescripcion; // Para SEO

  @Column(length = 500)
  private String palabrasClave; // Keywords separadas por comas

  // ========================================
  // CONFIGURACIÓN DE NEGOCIO
  // ========================================

  @Column(nullable = false)
  @Builder.Default
  private Boolean permiteProductos = true; // Si puede tener productos directamente

  @Column(nullable = false)
  @Builder.Default
  private Boolean requiereAprobacion = false; // Si los productos necesitan aprobación

  @Column(nullable = false)
  @Builder.Default
  private Boolean visibleEnMenu = true; // Si aparece en menús públicos

  @Column(nullable = false)
  @Builder.Default
  private Boolean destacada = false; // Para categorías promocionales

  @Column(precision = 5, scale = 2)
  @DecimalMin(value = "0.00", message = "La comisión no puede ser negativa")
  @DecimalMax(value = "100.00", message = "La comisión no puede exceder 100%")
  private BigDecimal comisionPorcentaje; // Para marketplaces

  // ========================================
  // CONFIGURACIÓN DE PRODUCTOS
  // ========================================

  @Column(precision = 12, scale = 2)
  @DecimalMin(
    value = "0.00",
    message = "El precio mínimo no puede ser negativo"
  )
  private BigDecimal precioMinimo;

  @Column(precision = 12, scale = 2)
  private BigDecimal precioMaximo;

  @Column(nullable = false)
  @Builder.Default
  private Boolean requiereInventario = true;

  @Column(nullable = false)
  @Builder.Default
  private Boolean permiteVariantes = true; // Tallas, colores, etc.

  // ========================================
  // MÉTRICAS Y ANALÍTICAS
  // ========================================

  @Column(nullable = false)
  @Builder.Default
  private Long totalProductos = 0L;

  @Column(nullable = false)
  @Builder.Default
  private Long totalVentas = 0L;

  @Column(precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal ingresosTotales = BigDecimal.ZERO;

  @Column(nullable = false)
  @Builder.Default
  private Long vistasTotal = 0L; // Para analytics

  @Column(nullable = false)
  @Builder.Default
  private Double popularidad = 0.0; // Calculada automáticamente

  // ========================================
  // CONFIGURACIÓN AVANZADA
  // ========================================

  @Column(length = 2000)
  private String configuracionJson; // Configuraciones específicas en JSON

  @Column(length = 1000)
  private String plantillaProducto; // Template específico para productos

  @Column(length = 100)
  private String departamento; // Ej: "Tecnología", "Moda", "Hogar"

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Builder.Default
  private TipoCategoria tipo = TipoCategoria.PRODUCTO;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @Builder.Default
  private EstadoCategoria estadoAprobacion = EstadoCategoria.APROBADA;

  // ========================================
  // AUDITORÍA EXTENDIDA
  // ========================================

  @Column(length = 100)
  private String creadoPor; // Usuario que creó

  @Column(length = 100)
  private String modificadoPor; // Usuario que modificó

  @Column(length = 500)
  private String motivoUltimoCambio; // Razón del último cambio

  // ========================================
  // SOFT DELETE
  // ========================================

  @Column(nullable = false)
  @Builder.Default
  private Boolean eliminado = false;

  @Column(length = 100)
  private String eliminadoPor;

  @Column(length = 500)
  private String motivoEliminacion;

  // ========================================
  // ENUMS
  // ========================================

  public enum TipoCategoria {
    PRODUCTO,
    SERVICIO,
    DIGITAL,
    FISICO,
    VIRTUAL,
    CATEGORIA_PADRE,
  }

  public enum EstadoCategoria {
    BORRADOR,
    PENDIENTE_APROBACION,
    APROBADA,
    RECHAZADA,
    INACTIVA,
    ARCHIVADA,
  }

  // ========================================
  // MÉTODOS DE UTILIDAD
  // ========================================

  /**
   * Verifica si esta categoría es una categoría raíz (sin padre).
   */
  public boolean esRaiz() {
    return categoriaPadre == null;
  }

  /**
   * Verifica si esta categoría tiene subcategorías.
   */
  public boolean tieneSubcategorias() {
    return subcategorias != null && !subcategorias.isEmpty();
  }

  /**
   * Obtiene la cantidad total de subcategorías.
   */
  public int cantidadSubcategorias() {
    return subcategorias != null ? subcategorias.size() : 0;
  }

  /**
   * Verifica si la categoría está activa y no eliminada.
   */
  public boolean estaDisponible() {
    return activo && !eliminado;
  }

  /**
   * Genera el código automáticamente basado en el nombre.
   * NOTA: Para evitar conflictos de unicidad, el servicio debe validar
   * que el código generado no exista ya en la base de datos.
   */
  public void generarCodigo() {
    if (nombre != null && !StringUtils.hasText(codigo)) {
      this.codigo = nombre
        .toUpperCase()
        .replaceAll("[^A-Z0-9\\s]", "")
        .replaceAll("\\s+", "_")
        .replaceAll("_+", "_")
        .replaceAll("^_|_$", "");

      // Limitar a 15 caracteres para dejar espacio para sufijos
      if (this.codigo.length() > 15) {
        this.codigo = this.codigo.substring(0, 15);
      }

      // Agregar sufijo si está vacío
      if (this.codigo.isEmpty()) {
        this.codigo = "CAT_" + (System.currentTimeMillis() % 10000);
      }
    }
  }

  /**
   * Genera el slug automáticamente basado en el nombre.
   */
  public void generarSlug() {
    if (nombre != null) {
      this.slug = nombre
        .toLowerCase()
        .replaceAll("[^a-z0-9\\s-]", "")
        .replaceAll("\\s+", "-")
        .replaceAll("-+", "-")
        .replaceAll("^-|-$", "");
    }
  }

  /**
   * Genera la ruta completa basada en la jerarquía.
   */
  public void generarRutaCompleta() {
    if (categoriaPadre != null) {
      this.rutaCompleta = categoriaPadre.getNombre() + " > " + nombre;
    } else {
      this.rutaCompleta = nombre;
    }
  }

  /**
   * Incrementa el contador de vistas.
   */
  public void incrementarVistas() {
    this.vistasTotal++;
  }

  /**
   * Calcula la popularidad basada en productos, ventas y vistas.
   */
  public void calcularPopularidad() {
    double factorProductos = totalProductos * 0.3;
    double factorVentas = totalVentas * 0.5;
    double factorVistas = vistasTotal * 0.2;
    this.popularidad = factorProductos + factorVentas + factorVistas;
  }
}
