package com.nelson.project.msvc_carrito.msvc_carrito.event;

/**
 * Evento emitido cuando se agrega un item al carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public class ItemAgregadoEvent extends CarritoEvent {

  private final Long productoId;
  private final Integer cantidad;

  public ItemAgregadoEvent(
    Object source,
    Long carritoId,
    Long usuarioId,
    Long productoId,
    Integer cantidad
  ) {
    super(
      source,
      carritoId,
      usuarioId,
      "ITEM_AGREGADO",
      String.format("ProductoId: %d, Cantidad: %d", productoId, cantidad)
    );
    this.productoId = productoId;
    this.cantidad = cantidad;
  }

  public Long getProductoId() {
    return productoId;
  }

  public Integer getCantidad() {
    return cantidad;
  }
}
