package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para creación/actualización de Producto.
 * No incluye campos autogenerados ni de sistema.
 */
public class ProductoCreateDto {

  @NotBlank
  private String nombre;

  @Size(max = 1000)
  private String descripcion;

  @NotNull
  @DecimalMin("0.00")
  private BigDecimal precio;

  @NotNull
  @Min(0)
  private Integer stock;

  @NotNull
  private Long categoriaId;

  @NotNull
  private Long proveedorId;

  private List<@NotBlank String> imagenes;

  @NotNull
  private Boolean estado;

  // Getters y setters (puedes usar Lombok @Data si lo prefieres)
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

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public Long getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Long categoriaId) {
    this.categoriaId = categoriaId;
  }

  public Long getProveedorId() {
    return proveedorId;
  }

  public void setProveedorId(Long proveedorId) {
    this.proveedorId = proveedorId;
  }

  public List<String> getImagenes() {
    return imagenes;
  }

  public void setImagenes(List<String> imagenes) {
    this.imagenes = imagenes;
  }

  public Boolean getEstado() {
    return estado;
  }

  public void setEstado(Boolean estado) {
    this.estado = estado;
  }
}
