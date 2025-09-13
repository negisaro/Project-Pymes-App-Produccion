package com.nelson.project.msvc_producto.msvc_producto.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nombre;

  @Column(length = 1000)
  private String descripcion;

  @Column(nullable = false)
  private BigDecimal precio;

  @Column(nullable = false)
  private Integer stock;

  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  // Relación moderna: solo guardamos el id, la consulta se hace vía REST a
  // microservicio Categoria
  @Column(name = "categoria_id", nullable = false)
  private Long categoriaId;

  // Relación moderna: solo guardamos el id, la consulta se hace vía REST a
  // microservicio Proveedor
  @Column(name = "proveedor_id", nullable = false)
  private Long proveedorId;

  // Imágenes: lista de URLs
  @ElementCollection
  @CollectionTable(
    name = "producto_imagenes",
    joinColumns = @JoinColumn(name = "producto_id")
  )
  @Column(name = "url")
  private List<String> imagenes;

  @Column(nullable = false)
  private Boolean estado;

  @PrePersist
  protected void onCreate() {
    fechaCreacion = LocalDateTime.now();
    fechaActualizacion = fechaCreacion;
  }

  @PreUpdate
  protected void onUpdate() {
    fechaActualizacion = LocalDateTime.now();
  }

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

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
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
