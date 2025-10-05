package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidad Carrito mejorada siguiendo principios SOLID y DDD.
 *
 * Responsabilidades:
 * - Gestionar items del carrito
 * - Calcular totales y subtotales
 * - Mantener estado del carrito
 * - Aplicar reglas de negocio
 *
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo gestiona el carrito
 * - Open/Closed: Extensible para nuevas funcionalidades
 * - Liskov Substitution: Hereda correctamente de BaseEntity
 */
@Entity
@Table(
  name = "carritos",
  indexes = {
    @Index(name = "idx_carrito_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_carrito_estado", columnList = "estado"),
    @Index(name = "idx_carrito_creado_en", columnList = "creado_en"),
  }
)
public class Carrito extends BaseEntityCorrected {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Positive
  @Column(name = "usuario_id", nullable = false)
  private Long usuarioId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 20)
  private EstadoCarrito estado = EstadoCarrito.ACTIVO;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "subtotal", precision = 12, scale = 2, nullable = false)
  private BigDecimal subtotal = BigDecimal.ZERO;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "descuento", precision = 12, scale = 2, nullable = false)
  private BigDecimal descuento = BigDecimal.ZERO;

  @DecimalMin(value = "0.0", inclusive = true)
  @Column(name = "total", precision = 12, scale = 2, nullable = false)
  private BigDecimal total = BigDecimal.ZERO;

  @Column(name = "codigo_descuento", length = 50)
  private String codigoDescuento;

  @Column(name = "expira_en")
  private LocalDateTime expiraEn;

  @Column(name = "ip_cliente", length = 45)
  private String ipCliente;

  @Size(max = 500)
  @Column(name = "notas", length = 500)
  private String notas;

  @Column(name = "fecha_abandonado")
  private LocalDateTime fechaAbandonado;

  @Column(name = "ultima_actividad")
  private LocalDateTime ultimaActividad;

  @Column(name = "total_items")
  private Integer totalItems = 0;

  @OneToMany(
    mappedBy = "carrito",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  private List<ItemCarrito> items = new ArrayList<>();

  // Constructors
  public Carrito() {
    super();
    this.expiraEn = LocalDateTime.now().plusDays(7); // Expira en 7 días por defecto
    this.ultimaActividad = LocalDateTime.now(); // Inicializar última actividad
  }

  public Carrito(Long usuarioId) {
    this();
    this.usuarioId = usuarioId;
  }

  // Métodos de negocio principales

  /**
   * Agrega un item al carrito aplicando reglas de negocio.
   * Si el producto ya existe, actualiza la cantidad.
   */
  public void agregarItem(ItemCarrito nuevoItem) {
    validarCarritoModificable();
    Objects.requireNonNull(nuevoItem, "El item no puede ser nulo");
    Objects.requireNonNull(
      nuevoItem.getProductoId(),
      "El producto ID no puede ser nulo"
    );

    Optional<ItemCarrito> itemExistente = buscarItemPorProducto(
      nuevoItem.getProductoId()
    );

    if (itemExistente.isPresent()) {
      itemExistente.get().incrementarCantidad(nuevoItem.getCantidad());
    } else {
      nuevoItem.setCarrito(this);
      items.add(nuevoItem);
    }

    recalcularTotales();
    actualizarExpiracion();
  }

  /**
   * Actualiza la cantidad de un item específico
   */
  public void actualizarCantidadItem(Long productoId, Integer nuevaCantidad) {
    validarCarritoModificable();
    Objects.requireNonNull(productoId, "El producto ID no puede ser nulo");
    Objects.requireNonNull(nuevaCantidad, "La cantidad no puede ser nula");

    if (nuevaCantidad <= 0) {
      quitarItem(productoId);
      return;
    }

    ItemCarrito item = buscarItemPorProducto(productoId).orElseThrow(() ->
      new IllegalArgumentException("Producto no encontrado en el carrito")
    );

    item.setCantidad(nuevaCantidad);
    recalcularTotales();
    actualizarExpiracion();
  }

  /**
   * Quita un item del carrito
   */
  public void quitarItem(Long productoId) {
    validarCarritoModificable();
    Objects.requireNonNull(productoId, "El producto ID no puede ser nulo");

    items.removeIf(item -> item.getProductoId().equals(productoId));
    recalcularTotales();
    actualizarExpiracion();
  }

  /**
   * Vacía completamente el carrito
   */
  public void vaciar() {
    validarCarritoModificable();
    items.clear();
    recalcularTotales();
    limpiarDescuentos();
    actualizarExpiracion();
  }

  /**
   * Aplica un descuento al carrito
   */
  public void aplicarDescuento(BigDecimal montoDescuento, String codigo) {
    validarCarritoModificable();
    Objects.requireNonNull(
      montoDescuento,
      "El monto de descuento no puede ser nulo"
    );

    if (montoDescuento.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El descuento no puede ser negativo");
    }

    if (montoDescuento.compareTo(subtotal) > 0) {
      throw new IllegalArgumentException(
        "El descuento no puede ser mayor al subtotal"
      );
    }

    this.descuento = montoDescuento.setScale(2, RoundingMode.HALF_UP);
    this.codigoDescuento = codigo;
    recalcularTotal();
  }

  /**
   * Marca el carrito como procesado (convertido en pedido)
   */
  public void marcarComoProcesado() {
    if (items.isEmpty()) {
      throw new IllegalStateException("No se puede procesar un carrito vacío");
    }
    this.estado = EstadoCarrito.PROCESADO;
  }

  /**
   * Marca el carrito como abandonado
   */
  public void marcarComoAbandonado() {
    if (estado == EstadoCarrito.ACTIVO) {
      this.estado = EstadoCarrito.ABANDONADO;
      this.fechaAbandonado = LocalDateTime.now();
    }
  }

  /**
   * Extiende la expiración del carrito
   */
  public void extenderExpiracion(int dias) {
    if (dias > 0) {
      this.expiraEn = LocalDateTime.now().plusDays(dias);
    }
  }

  // Métodos de cálculo privados

  public void recalcularTotales() {
    recalcularSubtotal();
    recalcularTotal();
  }

  /**
   * Remueve un item por ID de producto (método público para servicio)
   */
  public void removerItem(Long productoId) {
    quitarItem(productoId);
  }

  private void recalcularSubtotal() {
    this.subtotal = items
      .stream()
      .map(ItemCarrito::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add)
      .setScale(2, RoundingMode.HALF_UP);
  }

  private void recalcularTotal() {
    this.total = subtotal
      .subtract(descuento)
      .max(BigDecimal.ZERO) // El total nunca puede ser negativo
      .setScale(2, RoundingMode.HALF_UP);
  }

  private void limpiarDescuentos() {
    this.descuento = BigDecimal.ZERO;
    this.codigoDescuento = null;
  }

  private void actualizarExpiracion() {
    if (estado == EstadoCarrito.ACTIVO) {
      this.expiraEn = LocalDateTime.now().plusDays(7);
      this.ultimaActividad = LocalDateTime.now();
    }
  }

  // Métodos de consulta

  /**
   * Busca un item por ID de producto
   */
  public Optional<ItemCarrito> buscarItemPorProducto(Long productoId) {
    return items
      .stream()
      .filter(item -> item.getProductoId().equals(productoId))
      .findFirst();
  }

  /**
   * Verifica si el carrito está vacío
   */
  public boolean estaVacio() {
    return items.isEmpty();
  }

  /**
   * Verifica si el carrito ha expirado
   */
  public boolean haExpirado() {
    return expiraEn != null && LocalDateTime.now().isAfter(expiraEn);
  }

  /**
   * Verifica si el carrito puede ser modificado
   */
  public boolean esModificable() {
    return estado.esModificable() && !haExpirado();
  }

  /**
   * Obtiene el peso total del carrito (suma de subtotales)
   */
  public BigDecimal getPesoEconomico() {
    return subtotal;
  }

  // Métodos de validación

  private void validarCarritoModificable() {
    if (!esModificable()) {
      throw new IllegalStateException(
        String.format(
          "El carrito no puede ser modificado. Estado: %s, Expirado: %s",
          estado,
          haExpirado()
        )
      );
    }
  }

  // Getters y Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public EstadoCarrito getEstado() {
    return estado;
  }

  public void setEstado(EstadoCarrito estado) {
    this.estado = estado;
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

  public BigDecimal getDescuento() {
    return descuento;
  }

  public BigDecimal getTotal() {
    return total;
  }

  /**
   * Setter directo para total (para casos especiales de MapStruct)
   * NOTA: Normalmente se calcula automáticamente
   */
  public void setTotal(BigDecimal total) {
    this.total = total != null ? total : BigDecimal.ZERO;
  }

  public String getCodigoDescuento() {
    return codigoDescuento;
  }

  public LocalDateTime getExpiraEn() {
    return expiraEn;
  }

  public void setExpiraEn(LocalDateTime expiraEn) {
    this.expiraEn = expiraEn;
  }

  public String getIpCliente() {
    return ipCliente;
  }

  public void setIpCliente(String ipCliente) {
    this.ipCliente = ipCliente;
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }

  public LocalDateTime getFechaAbandonado() {
    return fechaAbandonado;
  }

  public void setFechaAbandonado(LocalDateTime fechaAbandonado) {
    this.fechaAbandonado = fechaAbandonado;
  }

  public LocalDateTime getUltimaActividad() {
    return ultimaActividad;
  }

  public void setUltimaActividad(LocalDateTime ultimaActividad) {
    this.ultimaActividad = ultimaActividad;
  }

  public Integer getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems != null ? totalItems : 0;
  }

  /**
   * Método de conveniencia para compatibilidad con otros sistemas
   */
  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    setActualizadoEn(fechaModificacion);
  }

  public List<ItemCarrito> getItems() {
    return new ArrayList<>(items); // Retorna copia defensiva
  }

  public void setItems(List<ItemCarrito> items) {
    this.items.clear();
    if (items != null) {
      items.forEach(this::agregarItem);
    }
  }

  // equals y hashCode

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Carrito carrito = (Carrito) obj;
    return (
      Objects.equals(id, carrito.id) ||
      (id == null &&
        Objects.equals(usuarioId, carrito.usuarioId) &&
        Objects.equals(getCreadoEn(), carrito.getCreadoEn()))
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id != null ? id : usuarioId, getCreadoEn());
  }

  @Override
  public String toString() {
    return String.format(
      "Carrito{id=%d, usuarioId=%d, estado=%s, items=%d, total=%s}",
      id,
      usuarioId,
      estado,
      items.size(),
      total
    );
  }
}
