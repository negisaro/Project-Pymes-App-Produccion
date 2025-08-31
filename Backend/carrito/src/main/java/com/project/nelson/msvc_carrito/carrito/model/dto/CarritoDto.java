package com.project.nelson.msvc_carrito.carrito.model.dto;

import java.util.List;

public class CarritoDto {

  private Long id;

  private Long usuarioId;

  private List<ItemCarritoDto> items;

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

  public List<ItemCarritoDto> getItems() {
    return items;
  }

  public void setItems(List<ItemCarritoDto> items) {
    this.items = items;
  }
}
