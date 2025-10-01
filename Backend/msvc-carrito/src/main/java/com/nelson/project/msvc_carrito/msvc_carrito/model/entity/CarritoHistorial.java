package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad para mantener historial de cambios en el carrito.
 * Implementa patrón Audit Trail para trazabilidad completa.
 *
 * Principios aplicados:
 * - Single Responsibility: Solo auditoría
 * - Immutable: Los registros de historial no se modifican
 * - Trazabilidad: Registro completo de operaciones
 *
 * REFACTORIZADA: Nombres de campos uniformizados
 */
@Entity
@Table(
  name = "carrito_historial",
  indexes = {
    @Index(name = "idx_historial_carrito_id", columnList = "carrito_id"),
    @Index(name = "idx_historial_fecha", columnList = "fecha_operacion"),
    @Index(name = "idx_historial_operacion", columnList = "tipo_operacion"),
    @Index(name = "idx_historial_usuario", columnList = "usuario_operacion"),
  }
)
public class CarritoHistorial {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "carrito_id", nullable = false)
  private Long carritoId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_operacion", nullable = false, length = 30)
  private TipoOperacion tipoOperacion;

  @Size(max = 100)
  @Column(name = "usuario_operacion", length = 100)
  private String usuarioOperacion;

  @NotNull
  @Column(name = "fecha_operacion", nullable = false)
  private LocalDateTime fechaOperacion;

  @Column(name = "ip_cliente", length = 45)
  private String ipCliente;

  @Size(max = 200)
  @Column(name = "user_agent", length = 200)
  private String userAgent;

  @Lob
  @Column(name = "datos_operacion", columnDefinition = "JSON")
  private String datosOperacion;

  @Size(max = 500)
  @Column(name = "descripcion", length = 500)
  private String descripcion;

  @Column(name = "producto_id")
  private Long productoId;

  @Column(name = "cantidad_anterior")
  private Integer cantidadAnterior;

  @Column(name = "cantidad_nueva")
  private Integer cantidadNueva;

  @Column(name = "precio_unitario", precision = 12, scale = 2)
  private java.math.BigDecimal precioUnitario;

  @Size(max = 100)
  @Column(name = "nombre_producto", length = 100)
  private String nombreProducto;

  // Enum para tipos de operación
  public enum TipoOperacion {
    CARRITO_CREADO("Carrito creado"),
    ITEM_AGREGADO("Item agregado"),
    ITEM_ACTUALIZADO("Item actualizado"),
    ITEM_ELIMINADO("Item eliminado"),
    CARRITO_VACIADO("Carrito vaciado"),
    DESCUENTO_APLICADO("Descuento aplicado"),
    DESCUENTO_REMOVIDO("Descuento removido"),
    CARRITO_PROCESADO("Carrito procesado"),
    CARRITO_ABANDONADO("Carrito abandonado"),
    CARRITO_EXPIRADO("Carrito expirado"),
    PRECIO_ACTUALIZADO("Precio actualizado"),
    CARRITO_RESTAURADO("Carrito restaurado");

    private final String descripcion;

    TipoOperacion(String descripcion) {
      this.descripcion = descripcion;
    }

    public String getDescripcion() {
      return descripcion;
    }
  }

  // Constructors
  public CarritoHistorial() {
    this.fechaOperacion = LocalDateTime.now();
  }

  public CarritoHistorial(
    Long carritoId,
    TipoOperacion tipoOperacion,
    String usuarioOperacion
  ) {
    this();
    this.carritoId = carritoId;
    this.tipoOperacion = tipoOperacion;
    this.usuarioOperacion = usuarioOperacion;
  }

  // Factory methods para diferentes tipos de operaciones

  public static CarritoHistorial carritoCreado(
    Long carritoId,
    String usuario,
    String ip
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.CARRITO_CREADO,
      usuario
    );
    historial.setIpCliente(ip);
    historial.setDescripcion("Nuevo carrito creado para el usuario");
    return historial;
  }

  public static CarritoHistorial itemAgregado(
    Long carritoId,
    String usuario,
    Long productoId,
    String nombreProducto,
    Integer cantidad,
    java.math.BigDecimal precio
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.ITEM_AGREGADO,
      usuario
    );
    historial.setProductoId(productoId);
    historial.setNombreProducto(nombreProducto);
    historial.setCantidadNueva(cantidad);
    historial.setPrecioUnitario(precio);
    historial.setDescripcion(
      String.format("Agregado %d unidades de %s", cantidad, nombreProducto)
    );
    return historial;
  }

  public static CarritoHistorial itemActualizado(
    Long carritoId,
    String usuario,
    Long productoId,
    String nombreProducto,
    Integer cantidadAnterior,
    Integer cantidadNueva
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.ITEM_ACTUALIZADO,
      usuario
    );
    historial.setProductoId(productoId);
    historial.setNombreProducto(nombreProducto);
    historial.setCantidadAnterior(cantidadAnterior);
    historial.setCantidadNueva(cantidadNueva);
    historial.setDescripcion(
      String.format(
        "Cantidad de %s cambió de %d a %d",
        nombreProducto,
        cantidadAnterior,
        cantidadNueva
      )
    );
    return historial;
  }

  public static CarritoHistorial itemEliminado(
    Long carritoId,
    String usuario,
    Long productoId,
    String nombreProducto,
    Integer cantidadAnterior
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.ITEM_ELIMINADO,
      usuario
    );
    historial.setProductoId(productoId);
    historial.setNombreProducto(nombreProducto);
    historial.setCantidadAnterior(cantidadAnterior);
    historial.setDescripcion(
      String.format(
        "Eliminado %s (cantidad: %d)",
        nombreProducto,
        cantidadAnterior
      )
    );
    return historial;
  }

  public static CarritoHistorial carritoVaciado(
    Long carritoId,
    String usuario,
    int itemsEliminados
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.CARRITO_VACIADO,
      usuario
    );
    historial.setDescripcion(
      String.format("Carrito vaciado - %d items eliminados", itemsEliminados)
    );
    return historial;
  }

  public static CarritoHistorial descuentoAplicado(
    Long carritoId,
    String usuario,
    String codigoDescuento,
    java.math.BigDecimal montoDescuento
  ) {
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.DESCUENTO_APLICADO,
      usuario
    );
    historial.setDescripcion(
      String.format(
        "Descuento aplicado: %s por $%s",
        codigoDescuento,
        montoDescuento
      )
    );
    return historial;
  }

  // Métodos de consulta

  public boolean esOperacionDeItem() {
    return (
      tipoOperacion == TipoOperacion.ITEM_AGREGADO ||
      tipoOperacion == TipoOperacion.ITEM_ACTUALIZADO ||
      tipoOperacion == TipoOperacion.ITEM_ELIMINADO
    );
  }

  public boolean esOperacionDeCarrito() {
    return (
      tipoOperacion == TipoOperacion.CARRITO_CREADO ||
      tipoOperacion == TipoOperacion.CARRITO_VACIADO ||
      tipoOperacion == TipoOperacion.CARRITO_PROCESADO ||
      tipoOperacion == TipoOperacion.CARRITO_ABANDONADO
    );
  }

  public boolean esOperacionCritica() {
    return (
      tipoOperacion == TipoOperacion.CARRITO_PROCESADO ||
      tipoOperacion == TipoOperacion.CARRITO_VACIADO
    );
  }

  // Lifecycle callbacks

  @PrePersist
  protected void prePersist() {
    if (this.fechaOperacion == null) {
      this.fechaOperacion = LocalDateTime.now();
    }
    if (this.descripcion == null && this.tipoOperacion != null) {
      this.descripcion = this.tipoOperacion.getDescripcion();
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

  public TipoOperacion getTipoOperacion() {
    return tipoOperacion;
  }

  public void setTipoOperacion(TipoOperacion tipoOperacion) {
    this.tipoOperacion = tipoOperacion;
  }

  public String getUsuarioOperacion() {
    return usuarioOperacion;
  }

  public void setUsuarioOperacion(String usuarioOperacion) {
    this.usuarioOperacion = usuarioOperacion;
  }

  public LocalDateTime getFechaOperacion() {
    return fechaOperacion;
  }

  public void setFechaOperacion(LocalDateTime fechaOperacion) {
    this.fechaOperacion = fechaOperacion;
  }

  public String getIpCliente() {
    return ipCliente;
  }

  public void setIpCliente(String ipCliente) {
    this.ipCliente = ipCliente;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getDatosOperacion() {
    return datosOperacion;
  }

  public void setDatosOperacion(String datosOperacion) {
    this.datosOperacion = datosOperacion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  public Integer getCantidadAnterior() {
    return cantidadAnterior;
  }

  public void setCantidadAnterior(Integer cantidadAnterior) {
    this.cantidadAnterior = cantidadAnterior;
  }

  public Integer getCantidadNueva() {
    return cantidadNueva;
  }

  public void setCantidadNueva(Integer cantidadNueva) {
    this.cantidadNueva = cantidadNueva;
  }

  public java.math.BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(java.math.BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  // equals y hashCode

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CarritoHistorial that = (CarritoHistorial) obj;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.format(
      "CarritoHistorial{id=%d, carritoId=%d, tipoOperacion=%s, usuario='%s', fecha=%s}",
      id,
      carritoId,
      tipoOperacion,
      usuarioOperacion,
      fechaOperacion
    );
  }
}
