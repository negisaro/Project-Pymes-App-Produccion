package com.nelson.project.msvc_carrito.msvc_carrito.event;

/**
 * Evento emitido cuando un carrito es abandonado.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public class CarritoAbandonadoEvent extends CarritoEvent {

  private final Integer totalItems;
  private final String valorTotal;

  public CarritoAbandonadoEvent(
    Object source,
    Long carritoId,
    Long usuarioId,
    Integer totalItems,
    String valorTotal
  ) {
    super(
      source,
      carritoId,
      usuarioId,
      "CARRITO_ABANDONADO",
      String.format("Items: %d, Valor: %s", totalItems, valorTotal)
    );
    this.totalItems = totalItems;
    this.valorTotal = valorTotal;
  }

  public Integer getTotalItems() {
    return totalItems;
  }

  public String getValorTotal() {
    return valorTotal;
  }
}
