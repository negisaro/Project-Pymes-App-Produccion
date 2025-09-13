package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CategoriaDTO {

  private Long id;
  private String nombre;
  private String descripcion;
  private boolean estado;
  private LocalDateTime creadoEn;
  private LocalDateTime actualizadoEn;
  private List<Long> productosId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public boolean isEstado() {
    return estado;
  }

  public void setEstado(boolean estado) {
    this.estado = estado;
  }

  public LocalDateTime getCreadoEn() {
    return creadoEn;
  }

  public void setCreadoEn(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn;
  }

  public LocalDateTime getActualizadoEn() {
    return actualizadoEn;
  }

  public void setActualizadoEn(LocalDateTime actualizadoEn) {
    this.actualizadoEn = actualizadoEn;
  }

  public List<Long> getProductosId() {
    return productosId;
  }

  public void setProductosId(List<Long> productosId) {
    this.productosId = productosId;
  }
}
