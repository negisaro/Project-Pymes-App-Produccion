package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción lanzada cuando no se encuentra un carrito solicitado
 */
public class CarritoNotFoundException extends BusinessException {

  private final Long carritoId;

  public CarritoNotFoundException(Long carritoId) {
    super("Carrito no encontrado con ID: " + carritoId);
    this.carritoId = carritoId;
  }

  public CarritoNotFoundException(Long usuarioId, String tipo) {
    super(
      "Carrito " + tipo + " no encontrado para usuario con ID: " + usuarioId
    );
    this.carritoId = null;
  }

  public CarritoNotFoundException(String mensaje) {
    super(mensaje);
    this.carritoId = null;
  }

  public CarritoNotFoundException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.carritoId = null;
  }

  public CarritoNotFoundException(String mensaje, String codigo) {
    super(mensaje, codigo);
    this.carritoId = null;
  }

  public Long getCarritoId() {
    return carritoId;
  }
}
