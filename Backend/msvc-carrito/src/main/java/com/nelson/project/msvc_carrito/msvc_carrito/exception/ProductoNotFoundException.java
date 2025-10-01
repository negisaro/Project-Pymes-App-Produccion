package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción lanzada cuando no se encuentra un producto solicitado
 */
public class ProductoNotFoundException extends BusinessException {

  private final Long productoId;

  public ProductoNotFoundException(Long productoId) {
    super("Producto no encontrado con ID: " + productoId);
    this.productoId = productoId;
  }

  public ProductoNotFoundException(String mensaje) {
    super(mensaje);
    this.productoId = null;
  }

  public ProductoNotFoundException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.productoId = null;
  }

  public ProductoNotFoundException(String mensaje, String codigo) {
    super(mensaje, codigo);
    this.productoId = null;
  }

  public Long getProductoId() {
    return productoId;
  }
}
