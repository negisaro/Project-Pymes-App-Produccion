package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CategoriaCreateDto {

  private Long id;

  @NotBlank
  private String nombre;

  @Size(max = 1000)
  private String descripcion;

  @NotNull
  private boolean estado;

  @NotNull
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

  public List<Long> getProductosId() {
    return productosId;
  }

  public void setProductosId(List<Long> productosId) {
    this.productosId = productosId;
  }
}
