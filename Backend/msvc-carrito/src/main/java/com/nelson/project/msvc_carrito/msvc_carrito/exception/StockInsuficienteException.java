package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción lanzada cuando no hay stock suficiente de un producto
 */
public class StockInsuficienteException extends BusinessException {

  private final Long productoId;
  private final Integer stockDisponible;
  private final Integer cantidadSolicitada;

  public StockInsuficienteException(String mensaje) {
    super(mensaje);
    this.productoId = null;
    this.stockDisponible = null;
    this.cantidadSolicitada = null;
  }

  public StockInsuficienteException(
    Long productoId,
    Integer stockDisponible,
    Integer cantidadSolicitada
  ) {
    super(
      "Stock insuficiente para producto " +
      productoId +
      ". Disponible: " +
      stockDisponible +
      ", Solicitado: " +
      cantidadSolicitada
    );
    this.productoId = productoId;
    this.stockDisponible = stockDisponible;
    this.cantidadSolicitada = cantidadSolicitada;
  }

  public StockInsuficienteException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.productoId = null;
    this.stockDisponible = null;
    this.cantidadSolicitada = null;
  }

  public StockInsuficienteException(String mensaje, String codigo) {
    super(mensaje, codigo);
    this.productoId = null;
    this.stockDisponible = null;
    this.cantidadSolicitada = null;
  }

  public Long getProductoId() {
    return productoId;
  }

  public Integer getStockDisponible() {
    return stockDisponible;
  }

  public Integer getCantidadSolicitada() {
    return cantidadSolicitada;
  }
}
