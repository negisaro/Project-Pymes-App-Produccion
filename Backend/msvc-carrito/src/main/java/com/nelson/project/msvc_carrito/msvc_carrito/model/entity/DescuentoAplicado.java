package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad para gestionar descuentos aplicados a carritos.
 * Mantiene historial de promociones y permite análisis de efectividad.
 *
 * Principios aplicados:
 * - Single Responsibility: Solo gestiona descuentos
 * - Validation: Reglas de negocio en constraints
 * - Audit Trail: Trazabilidad de promociones
 */
@Entity
@Table(
  name = "descuentos_aplicados",
  indexes = {
    @Index(name = "idx_descuento_carrito_id", columnList = "carrito_id"),
    @Index(name = "idx_descuento_codigo", columnList = "codigo_descuento"),
    @Index(name = "idx_descuento_fecha", columnList = "aplicado_en"),
    @Index(name = "idx_descuento_tipo", columnList = "tipo_descuento"),
  }
)
public class DescuentoAplicado extends BaseEntityCorrected {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "carrito_id", nullable = false)
  private Long carritoId;

  @NotBlank
  @Size(max = 100)
  @Column(name = "codigo_descuento", nullable = false, length = 100)
  private String codigoDescuento;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_descuento", nullable = false, length = 20)
  private TipoDescuento tipoDescuento;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "monto_descuento", precision = 12, scale = 2)
  private BigDecimal montoDescuento;

  @DecimalMin(value = "0.0", inclusive = true)
  @DecimalMax(value = "100.0", inclusive = true)
  @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
  private BigDecimal porcentajeDescuento;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "monto_minimo", precision = 12, scale = 2)
  private BigDecimal montoMinimo;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "descuento_maximo", precision = 12, scale = 2)
  private BigDecimal descuentoMaximo;

  @NotNull
  @Column(name = "aplicado_en", nullable = false)
  private LocalDateTime aplicadoEn;

  @Column(name = "valido_hasta")
  private LocalDateTime validoHasta;

  @Column(name = "removido_en")
  private LocalDateTime removidoEn;

  @Size(max = 200)
  @Column(name = "motivo_remocion", length = 200)
  private String motivoRemocion;

  @NotNull
  @Column(name = "activo", nullable = false)
  private Boolean activo = true;

  @Size(max = 500)
  @Column(name = "descripcion", length = 500)
  private String descripcion;

  @Column(name = "subtotal_original", precision = 12, scale = 2)
  private BigDecimal subtotalOriginal;

  @Column(name = "descuento_calculado", precision = 12, scale = 2)
  private BigDecimal descuentoCalculado;

  @Size(max = 50)
  @Column(name = "campana_id", length = 50)
  private String campanaId;

  @Size(max = 100)
  @Column(name = "origen", length = 100)
  private String origen;

  @Column(name = "usuario_aplicacion", length = 100)
  private String usuarioAplicacion;

  // Relación con carrito
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "carrito_id", insertable = false, updatable = false)
  private Carrito carrito;

  // Enum para tipos de descuento
  public enum TipoDescuento {
    PORCENTAJE("Descuento por porcentaje"),
    MONTO_FIJO("Descuento por monto fijo"),
    ENVIO_GRATIS("Envío gratuito"),
    SEGUNDA_UNIDAD("Segunda unidad con descuento"),
    CATEGORIA("Descuento por categoría"),
    CANTIDAD("Descuento por cantidad"),
    PRIMER_COMPRA("Descuento primera compra"),
    FIDELIZACION("Descuento por fidelización"),
    TEMPORADA("Descuento de temporada"),
    LIQUIDACION("Descuento de liquidación");

    private final String descripcion;

    TipoDescuento(String descripcion) {
      this.descripcion = descripcion;
    }

    public String getDescripcion() {
      return descripcion;
    }
  }

  // Constructors
  public DescuentoAplicado() {
    super();
    this.aplicadoEn = LocalDateTime.now();
    this.activo = true;
  }

  public DescuentoAplicado(
    Long carritoId,
    String codigoDescuento,
    TipoDescuento tipo
  ) {
    this();
    this.carritoId = carritoId;
    this.codigoDescuento = codigoDescuento;
    this.tipoDescuento = tipo;
  }

  // Factory methods para diferentes tipos de descuentos

  public static DescuentoAplicado crearDescuentoPorcentaje(
    Long carritoId,
    String codigo,
    BigDecimal porcentaje,
    String usuario
  ) {
    DescuentoAplicado descuento = new DescuentoAplicado(
      carritoId,
      codigo,
      TipoDescuento.PORCENTAJE
    );
    descuento.setPorcentajeDescuento(porcentaje);
    descuento.setUsuarioAplicacion(usuario);
    descuento.setDescripcion(
      String.format("Descuento del %s%% aplicado", porcentaje)
    );
    return descuento;
  }

  public static DescuentoAplicado crearDescuentoFijo(
    Long carritoId,
    String codigo,
    BigDecimal monto,
    String usuario
  ) {
    DescuentoAplicado descuento = new DescuentoAplicado(
      carritoId,
      codigo,
      TipoDescuento.MONTO_FIJO
    );
    descuento.setMontoDescuento(monto);
    descuento.setUsuarioAplicacion(usuario);
    descuento.setDescripcion(
      String.format("Descuento fijo de $%s aplicado", monto)
    );
    return descuento;
  }

  public static DescuentoAplicado crearEnvioGratis(
    Long carritoId,
    String codigo,
    String usuario
  ) {
    DescuentoAplicado descuento = new DescuentoAplicado(
      carritoId,
      codigo,
      TipoDescuento.ENVIO_GRATIS
    );
    descuento.setUsuarioAplicacion(usuario);
    descuento.setDescripcion("Envío gratuito aplicado");
    return descuento;
  }

  // Métodos de negocio

  /**
   * Calcula el descuento basado en el subtotal del carrito
   */
  public BigDecimal calcularDescuento(BigDecimal subtotal) {
    if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }

    // Validar monto mínimo si existe
    if (montoMinimo != null && subtotal.compareTo(montoMinimo) < 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal descuentoCalculado = BigDecimal.ZERO;

    switch (tipoDescuento) {
      case PORCENTAJE:
        if (porcentajeDescuento != null) {
          descuentoCalculado = subtotal
            .multiply(porcentajeDescuento)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        break;
      case MONTO_FIJO:
        if (montoDescuento != null) {
          descuentoCalculado = montoDescuento;
        }
        break;
      default:
        descuentoCalculado = montoDescuento != null
          ? montoDescuento
          : BigDecimal.ZERO;
    }

    // Aplicar límite máximo si existe
    if (
      descuentoMaximo != null &&
      descuentoCalculado.compareTo(descuentoMaximo) > 0
    ) {
      descuentoCalculado = descuentoMaximo;
    }

    // El descuento no puede ser mayor al subtotal
    if (descuentoCalculado.compareTo(subtotal) > 0) {
      descuentoCalculado = subtotal;
    }

    this.subtotalOriginal = subtotal;
    this.descuentoCalculado = descuentoCalculado;

    return descuentoCalculado.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Marca el descuento como removido
   */
  public void remover(String motivo) {
    this.activo = false;
    this.removidoEn = LocalDateTime.now();
    this.motivoRemocion = motivo;
  }

  /**
   * Verifica si el descuento está vigente
   */
  public boolean estaVigente() {
    if (!activo || removidoEn != null) {
      return false;
    }

    if (validoHasta != null) {
      return LocalDateTime.now().isBefore(validoHasta);
    }

    return true;
  }

  /**
   * Verifica si el descuento ha expirado
   */
  public boolean haExpirado() {
    return validoHasta != null && LocalDateTime.now().isAfter(validoHasta);
  }

  /**
   * Extiende la validez del descuento
   */
  public void extenderValidez(int horas) {
    if (horas > 0) {
      LocalDateTime nuevaFecha =
        (validoHasta != null ? validoHasta : LocalDateTime.now()).plusHours(
            horas
          );
      this.validoHasta = nuevaFecha;
    }
  }

  /**
   * Obtiene la descripción completa del descuento
   */
  public String getDescripcionCompleta() {
    if (descripcion != null && !descripcion.trim().isEmpty()) {
      return descripcion;
    }

    return switch (tipoDescuento) {
      case PORCENTAJE -> String.format(
        "Descuento del %s%%",
        porcentajeDescuento
      );
      case MONTO_FIJO -> String.format("Descuento de $%s", montoDescuento);
      case ENVIO_GRATIS -> "Envío gratuito";
      default -> tipoDescuento.getDescripcion();
    };
  }

  /**
   * Verifica si es un descuento significativo (mayor al 5%)
   */
  public boolean esSignificativo(BigDecimal subtotal) {
    if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
      return false;
    }

    BigDecimal descuento = calcularDescuento(subtotal);
    BigDecimal porcentajeReal = descuento
      .multiply(new BigDecimal("100"))
      .divide(subtotal, 2, RoundingMode.HALF_UP);

    return porcentajeReal.compareTo(new BigDecimal("5")) >= 0;
  }

  // Lifecycle callbacks

  @PrePersist
  protected void prePersist() {
    super.prePersist();
    if (this.aplicadoEn == null) {
      this.aplicadoEn = LocalDateTime.now();
    }
  }

  @PreUpdate
  protected void preUpdate() {
    super.preUpdate();
    // No permitir modificar fechas críticas una vez establecidas
    if (aplicadoEn == null) {
      aplicadoEn = LocalDateTime.now();
    }
  }

  // Getters y Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCarritoId() {
    return carritoId;
  }

  public void setCarritoId(Long carritoId) {
    this.carritoId = carritoId;
  }

  public String getCodigoDescuento() {
    return codigoDescuento;
  }

  public void setCodigoDescuento(String codigoDescuento) {
    this.codigoDescuento = codigoDescuento;
  }

  public TipoDescuento getTipoDescuento() {
    return tipoDescuento;
  }

  public void setTipoDescuento(TipoDescuento tipoDescuento) {
    this.tipoDescuento = tipoDescuento;
  }

  public BigDecimal getMontoDescuento() {
    return montoDescuento;
  }

  public void setMontoDescuento(BigDecimal montoDescuento) {
    this.montoDescuento = montoDescuento;
  }

  public BigDecimal getPorcentajeDescuento() {
    return porcentajeDescuento;
  }

  public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
    this.porcentajeDescuento = porcentajeDescuento;
  }

  public BigDecimal getMontoMinimo() {
    return montoMinimo;
  }

  public void setMontoMinimo(BigDecimal montoMinimo) {
    this.montoMinimo = montoMinimo;
  }

  public BigDecimal getDescuentoMaximo() {
    return descuentoMaximo;
  }

  public void setDescuentoMaximo(BigDecimal descuentoMaximo) {
    this.descuentoMaximo = descuentoMaximo;
  }

  public LocalDateTime getAplicadoEn() {
    return aplicadoEn;
  }

  public void setAplicadoEn(LocalDateTime aplicadoEn) {
    this.aplicadoEn = aplicadoEn;
  }

  public LocalDateTime getValidoHasta() {
    return validoHasta;
  }

  public void setValidoHasta(LocalDateTime validoHasta) {
    this.validoHasta = validoHasta;
  }

  public LocalDateTime getRemovidoEn() {
    return removidoEn;
  }

  public void setRemovidoEn(LocalDateTime removidoEn) {
    this.removidoEn = removidoEn;
  }

  public String getMotivoRemocion() {
    return motivoRemocion;
  }

  public void setMotivoRemocion(String motivoRemocion) {
    this.motivoRemocion = motivoRemocion;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public BigDecimal getSubtotalOriginal() {
    return subtotalOriginal;
  }

  public void setSubtotalOriginal(BigDecimal subtotalOriginal) {
    this.subtotalOriginal = subtotalOriginal;
  }

  public BigDecimal getDescuentoCalculado() {
    return descuentoCalculado;
  }

  public void setDescuentoCalculado(BigDecimal descuentoCalculado) {
    this.descuentoCalculado = descuentoCalculado;
  }

  public String getCampanaId() {
    return campanaId;
  }

  public void setCampanaId(String campanaId) {
    this.campanaId = campanaId;
  }

  public String getOrigen() {
    return origen;
  }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public String getUsuarioAplicacion() {
    return usuarioAplicacion;
  }

  public void setUsuarioAplicacion(String usuarioAplicacion) {
    this.usuarioAplicacion = usuarioAplicacion;
  }

  public Carrito getCarrito() {
    return carrito;
  }

  public void setCarrito(Carrito carrito) {
    this.carrito = carrito;
  }

  // equals y hashCode

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    DescuentoAplicado that = (DescuentoAplicado) obj;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.format(
      "DescuentoAplicado{id=%d, carritoId=%d, codigo='%s', tipo=%s, activo=%s}",
      id,
      carritoId,
      codigoDescuento,
      tipoDescuento,
      activo
    );
  }
}
