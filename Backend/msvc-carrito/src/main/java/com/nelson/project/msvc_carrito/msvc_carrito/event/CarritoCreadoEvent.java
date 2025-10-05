package com.nelson.project.msvc_carrito.msvc_carrito.event;

/**
 * Evento emitido cuando se crea un nuevo carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public class CarritoCreadoEvent extends CarritoEvent {

  public CarritoCreadoEvent(
    Object source,
    Long carritoId,
    Long usuarioId,
    String ipCliente
  ) {
    super(
      source,
      carritoId,
      usuarioId,
      "CARRITO_CREADO",
      String.format("IP: %s", ipCliente)
    );
  }
}
