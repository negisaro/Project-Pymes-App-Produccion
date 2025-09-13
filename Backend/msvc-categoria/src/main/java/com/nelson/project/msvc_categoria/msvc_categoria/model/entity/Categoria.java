package com.nelson.project.msvc_categoria.msvc_categoria.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nombre;

  @Column(length = 1000)
  private String descripcion;

  @Column(nullable = false)
  private boolean estado;

  // Auditoría básica
  @Column(updatable = false)
  private LocalDateTime creadoEn;

  private LocalDateTime actualizadoEn;

  // Relación con productos (asumiendo entidad Producto en otro microservicio)
  @Transient // Solo referencia, no persistente aquí
  private List<Long> productosId;

  public Categoria() {}

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

  @PrePersist
  public void prePersist() {
    this.creadoEn = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.actualizadoEn = LocalDateTime.now();
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
