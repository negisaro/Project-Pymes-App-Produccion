package com.nelson.project.msvc_orden.msvc_orden.model.dto;

import java.util.List;

public class OrdenDto {

  private Long id;

  private Long usuarioId;

  private boolean estado;

  private List<OrdenItemDto> items;

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

  public boolean isEstado() {
    return estado;
  }

  public void setEstado(boolean estado) {
    this.estado = estado;
  }

  public List<OrdenItemDto> getItems() {
    return items;
  }

  public void setItems(List<OrdenItemDto> items) {
    this.items = items;
  }
}
