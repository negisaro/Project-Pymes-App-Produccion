package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción lanzada cuando se excede el límite máximo permitido del carrito
 */
public class LimiteCarritoExcedidoException extends BusinessException {

  private final Integer limiteMaximo;
  private final Integer cantidadActual;

  public LimiteCarritoExcedidoException(String mensaje) {
    super(mensaje);
    this.limiteMaximo = null;
    this.cantidadActual = null;
  }

  public LimiteCarritoExcedidoException(
    Integer limiteMaximo,
    Integer cantidadActual
  ) {
    super(
      "Límite de carrito excedido. Máximo: " +
      limiteMaximo +
      ", Actual: " +
      cantidadActual
    );
    this.limiteMaximo = limiteMaximo;
    this.cantidadActual = cantidadActual;
  }

  public LimiteCarritoExcedidoException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.limiteMaximo = null;
    this.cantidadActual = null;
  }

  public LimiteCarritoExcedidoException(String mensaje, String codigo) {
    super(mensaje, codigo);
    this.limiteMaximo = null;
    this.cantidadActual = null;
  }

  public Integer getLimiteMaximo() {
    return limiteMaximo;
  }

  public Integer getCantidadActual() {
    return cantidadActual;
  }
}
