package com.nelson.project.msvc_orden.msvc_orden.model.dto;

import java.math.BigDecimal;

public class OrdenItemDto {

  private Long id;

  private Integer cantidad;

  private BigDecimal precio;

  private Long productoId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }
}
