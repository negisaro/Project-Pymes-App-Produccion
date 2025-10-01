package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción lanzada cuando un descuento es inválido o no aplicable
 */
public class DescuentoInvalidoException extends BusinessException {

  private final String codigoDescuento;

  public DescuentoInvalidoException(String codigoDescuento, String razon) {
    super("Descuento inválido '" + codigoDescuento + "': " + razon);
    this.codigoDescuento = codigoDescuento;
  }

  public DescuentoInvalidoException(String mensaje) {
    super(mensaje);
    this.codigoDescuento = null;
  }

  public DescuentoInvalidoException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.codigoDescuento = null;
  }

  public DescuentoInvalidoException(
    String mensaje,
    String codigoError,
    boolean esCodigoError
  ) {
    super(mensaje, codigoError);
    this.codigoDescuento = null;
  }

  public String getCodigoDescuento() {
    return codigoDescuento;
  }
}
