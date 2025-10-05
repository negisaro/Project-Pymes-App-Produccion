package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad ItemCarrito mejorada siguiendo principios SOLID y DDD.
 *
 * Responsabilidades:
 * - Gestionar información del producto en el carrito
 * - Calcular subtotal del item
 * - Mantener datos desnormalizados para performance
 * - Validar cantidades y precios
 *
 * Principios aplicados:
 * - Single Responsibility: Solo gestiona items individuales
 * - Encapsulación: Métodos de negocio encapsulados
 * - Validación: Constraints de negocio aplicados
 */
@Entity
@Table(
  name = "items_carrito",
  indexes = {
    @Index(name = "idx_item_carrito_id", columnList = "carrito_id"),
    @Index(name = "idx_item_producto_id", columnList = "producto_id"),
    @Index(name = "idx_item_agregado_en", columnList = "agregado_en"),
  }
)
public class ItemCarrito extends BaseEntityCorrected {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Positive
  @Column(name = "producto_id", nullable = false)
  private Long productoId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "nombre_producto", nullable = false, length = 255)
  private String nombreProducto;

  @Size(max = 500)
  @Column(name = "descripcion_producto", length = 500)
  private String descripcionProducto;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer = 10, fraction = 2)
  @Column(name = "precio_unitario", precision = 12, scale = 2, nullable = false)
  private BigDecimal precioUnitario;

  @NotNull
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 999, message = "La cantidad no puede exceder 999")
  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "subtotal", precision = 12, scale = 2, nullable = false)
  private BigDecimal subtotal;

  @Column(name = "precio_original", precision = 12, scale = 2)
  private BigDecimal precioOriginal;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "descuento_item", precision = 12, scale = 2)
  private BigDecimal descuentoItem = BigDecimal.ZERO;

  @Column(name = "imagen_url", length = 500)
  private String imagenUrl;

  @Column(name = "categoria_id")
  private Long categoriaId;

  @Size(max = 100)
  @Column(name = "categoria_nombre", length = 100)
  private String categoriaNombre;

  @Column(name = "peso", precision = 8, scale = 3)
  private BigDecimal peso;

  @Size(max = 20)
  @Column(name = "unidad_medida", length = 20)
  private String unidadMedida;

  @Column(name = "agregado_en", nullable = false)
  private LocalDateTime agregadoEn;

  @Column(name = "actualizado_en")
  private LocalDateTime actualizadoEn;

  @Size(max = 200)
  @Column(name = "notas", length = 200)
  private String notas;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "carrito_id", nullable = false)
  private Carrito carrito;

  // Constructors
  public ItemCarrito() {
    super();
    this.agregadoEn = LocalDateTime.now();
    this.cantidad = 1;
    this.descuentoItem = BigDecimal.ZERO;
  }

  public ItemCarrito(
    Long productoId,
    String nombreProducto,
    BigDecimal precioUnitario,
    Integer cantidad
  ) {
    this();
    this.productoId = productoId;
    this.nombreProducto = nombreProducto;
    this.precioUnitario = precioUnitario;
    this.precioOriginal = precioUnitario;
    this.cantidad = cantidad;
    calcularSubtotal();
  }

  // Métodos de negocio principales

  /**
   * Actualiza la cantidad del item y recalcula el subtotal
   */
  public void setCantidad(Integer nuevaCantidad) {
    validarCantidad(nuevaCantidad);
    this.cantidad = nuevaCantidad;
    this.actualizadoEn = LocalDateTime.now();
    calcularSubtotal();
  }

  /**
   * Incrementa la cantidad en el valor especificado
   */
  public void incrementarCantidad(Integer incremento) {
    if (incremento == null || incremento <= 0) {
      throw new IllegalArgumentException("El incremento debe ser mayor a 0");
    }
    setCantidad(this.cantidad + incremento);
  }

  /**
   * Decrementa la cantidad en el valor especificado
   */
  public void decrementarCantidad(Integer decremento) {
    if (decremento == null || decremento <= 0) {
      throw new IllegalArgumentException("El decremento debe ser mayor a 0");
    }

    int nuevaCantidad = this.cantidad - decremento;
    if (nuevaCantidad <= 0) {
      throw new IllegalArgumentException(
        "La cantidad resultante debe ser mayor a 0"
      );
    }

    setCantidad(nuevaCantidad);
  }

  /**
   * Actualiza el precio unitario y recalcula el subtotal
   */
  public void actualizarPrecio(BigDecimal nuevoPrecio) {
    if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El precio debe ser mayor a 0");
    }

    this.precioUnitario = nuevoPrecio.setScale(2, RoundingMode.HALF_UP);
    this.actualizadoEn = LocalDateTime.now();
    calcularSubtotal();
  }

  /**
   * Aplica un descuento específico al item
   */
  public void aplicarDescuento(BigDecimal descuento) {
    if (descuento == null) {
      descuento = BigDecimal.ZERO;
    }

    if (descuento.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El descuento no puede ser negativo");
    }

    BigDecimal precioConDescuento = precioUnitario.subtract(descuento);
    if (precioConDescuento.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(
        "El descuento no puede ser mayor al precio unitario"
      );
    }

    this.descuentoItem = descuento.setScale(2, RoundingMode.HALF_UP);
    this.actualizadoEn = LocalDateTime.now();
    calcularSubtotal();
  }

  /**
   * Quita el descuento del item
   */
  public void quitarDescuento() {
    this.descuentoItem = BigDecimal.ZERO;
    this.actualizadoEn = LocalDateTime.now();
    calcularSubtotal();
  }

  /**
   * Actualiza la información del producto (para sincronización)
   */
  public void actualizarInfoProducto(
    String nombre,
    String descripcion,
    BigDecimal precio,
    String imagenUrl
  ) {
    if (nombre != null && !nombre.trim().isEmpty()) {
      this.nombreProducto = nombre.trim();
    }

    if (descripcion != null) {
      this.descripcionProducto = descripcion.trim();
    }

    if (precio != null && precio.compareTo(BigDecimal.ZERO) > 0) {
      // Solo actualizar si el precio ha cambiado significativamente
      if (
        this.precioUnitario.subtract(precio)
          .abs()
          .compareTo(new BigDecimal("0.01")) >
        0
      ) {
        this.precioUnitario = precio.setScale(2, RoundingMode.HALF_UP);
        calcularSubtotal();
      }
    }

    if (imagenUrl != null) {
      this.imagenUrl = imagenUrl.trim();
    }

    this.actualizadoEn = LocalDateTime.now();
  }

  // Métodos de cálculo

  /**
   * Calcula el subtotal del item considerando cantidad y descuentos
   */
  public void calcularSubtotal() {
    if (precioUnitario == null || cantidad == null) {
      this.subtotal = BigDecimal.ZERO;
      return;
    }

    BigDecimal precioConDescuento = precioUnitario.subtract(
      descuentoItem != null ? descuentoItem : BigDecimal.ZERO
    );

    this.subtotal = precioConDescuento
      .multiply(new BigDecimal(cantidad))
      .setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Obtiene el precio efectivo (precio unitario - descuento)
   */
  public BigDecimal getPrecioEfectivo() {
    return precioUnitario
      .subtract(descuentoItem != null ? descuentoItem : BigDecimal.ZERO)
      .setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Obtiene el total de descuento aplicado al item
   */
  public BigDecimal getTotalDescuento() {
    if (
      descuentoItem == null || descuentoItem.compareTo(BigDecimal.ZERO) == 0
    ) {
      return BigDecimal.ZERO;
    }

    return descuentoItem
      .multiply(new BigDecimal(cantidad))
      .setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Calcula el peso total del item
   */
  public BigDecimal getPesoTotal() {
    if (peso == null || cantidad == null) {
      return BigDecimal.ZERO;
    }

    return peso
      .multiply(new BigDecimal(cantidad))
      .setScale(3, RoundingMode.HALF_UP);
  }

  // Métodos de consulta

  /**
   * Verifica si el item tiene descuento aplicado
   */
  public boolean tieneDescuento() {
    return (
      descuentoItem != null && descuentoItem.compareTo(BigDecimal.ZERO) > 0
    );
  }

  /**
   * Verifica si el precio ha cambiado desde que se agregó
   */
  public boolean precioCambio() {
    return (
      precioOriginal != null && precioUnitario.compareTo(precioOriginal) != 0
    );
  }

  /**
   * Obtiene la diferencia de precio si ha cambiado
   */
  public BigDecimal getDiferenciaPrecio() {
    if (precioOriginal == null) {
      return BigDecimal.ZERO;
    }
    return precioUnitario.subtract(precioOriginal);
  }

  /**
   * Verifica si el item es reciente (agregado en las últimas horas)
   */
  public boolean esReciente(int horas) {
    if (agregadoEn == null) {
      return false;
    }
    return agregadoEn.isAfter(LocalDateTime.now().minusHours(horas));
  }

  // Métodos de validación

  private void validarCantidad(Integer cantidad) {
    if (cantidad == null) {
      throw new IllegalArgumentException("La cantidad no puede ser nula");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
    }
    if (cantidad > 999) {
      throw new IllegalArgumentException("La cantidad no puede exceder 999");
    }
  }

  // Lifecycle callbacks

  @PrePersist
  protected void prePersist() {
    super.prePersist();
    if (this.agregadoEn == null) {
      this.agregadoEn = LocalDateTime.now();
    }
    calcularSubtotal();
  }

  @PreUpdate
  protected void preUpdate() {
    super.preUpdate();
    this.actualizadoEn = LocalDateTime.now();
    calcularSubtotal();
  }

  // Getters y Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public String getDescripcionProducto() {
    return descripcionProducto;
  }

  public void setDescripcionProducto(String descripcionProducto) {
    this.descripcionProducto = descripcionProducto;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
    calcularSubtotal();
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  /**
   * Setter directo para subtotal (para casos especiales de MapStruct)
   * NOTA: Normalmente se calcula automáticamente
   */
  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
  }

  public BigDecimal getPrecioOriginal() {
    return precioOriginal;
  }

  public void setPrecioOriginal(BigDecimal precioOriginal) {
    this.precioOriginal = precioOriginal;
  }

  public BigDecimal getDescuentoItem() {
    return descuentoItem;
  }

  /**
   * Setter directo para descuentoItem (para MapStruct)
   * NOTA: Se recomienda usar aplicarDescuento() para lógica de negocio
   */
  public void setDescuentoItem(BigDecimal descuentoItem) {
    this.descuentoItem = descuentoItem != null
      ? descuentoItem
      : BigDecimal.ZERO;
    calcularSubtotal();
  }

  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
  }

  public Long getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Long categoriaId) {
    this.categoriaId = categoriaId;
  }

  public String getCategoriaNombre() {
    return categoriaNombre;
  }

  public void setCategoriaNombre(String categoriaNombre) {
    this.categoriaNombre = categoriaNombre;
  }

  public BigDecimal getPeso() {
    return peso;
  }

  public void setPeso(BigDecimal peso) {
    this.peso = peso;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public LocalDateTime getAgregadoEn() {
    return agregadoEn;
  }

  /**
   * Setter para agregadoEn (para MapStruct y casos especiales)
   * NOTA: Normalmente se inicializa automáticamente
   */
  public void setAgregadoEn(LocalDateTime agregadoEn) {
    this.agregadoEn = agregadoEn;
  }

  public LocalDateTime getActualizadoEn() {
    return actualizadoEn;
  }

  /**
   * Setter para actualizadoEn (para MapStruct y casos especiales)
   * NOTA: Normalmente se maneja automáticamente en callbacks
   */
  public void setActualizadoEn(LocalDateTime actualizadoEn) {
    this.actualizadoEn = actualizadoEn;
  }

  /**
   * Método de conveniencia para compatibilidad con otros sistemas
   */
  public LocalDateTime getFechaCreacion() {
    return getAgregadoEn();
  }

  /**
   * Método de conveniencia para compatibilidad con otros sistemas
   */
  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    setAgregadoEn(fechaCreacion);
  }

  /**
   * Método de conveniencia para compatibilidad con otros sistemas
   */
  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    setActualizadoEn(fechaModificacion);
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
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
    ItemCarrito that = (ItemCarrito) obj;
    return (
      Objects.equals(id, that.id) ||
      (id == null &&
        Objects.equals(productoId, that.productoId) &&
        Objects.equals(carrito, that.carrito))
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id != null ? id : productoId, carrito);
  }

  @Override
  public String toString() {
    return String.format(
      "ItemCarrito{id=%d, productoId=%d, nombre='%s', cantidad=%d, precio=%s, subtotal=%s}",
      id,
      productoId,
      nombreProducto,
      cantidad,
      precioUnitario,
      subtotal
    );
  }
}
