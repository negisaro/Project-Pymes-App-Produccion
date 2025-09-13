package com.nelson.project.msvc_orden.msvc_orden.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OrdenItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer cantidad;

  private BigDecimal precio;

  @Column(name = "producto_id", nullable = false)
  private List<Long> productoId;

  @Column(nullable = false)
  private boolean estado;

  @Column(updatable = false)
  private LocalDateTime creadoEn;

  private LocalDateTime actualizadoEn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_id")
  private Orden orden;

  public OrdenItem() {}

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

  public List<Long> getProductoId() {
    return productoId;
  }

  public void setProductoId(List<Long> productoId) {
    this.productoId = productoId;
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

  public Orden getOrden() {
    return orden;
  }

  public void setOrden(Orden orden) {
    this.orden = orden;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    OrdenItem other = (OrdenItem) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
