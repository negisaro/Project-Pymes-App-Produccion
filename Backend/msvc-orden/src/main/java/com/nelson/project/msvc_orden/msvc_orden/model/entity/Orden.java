package com.nelson.project.msvc_orden.msvc_orden.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Orden {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Relación moderna: solo guardamos el id, la consulta se hace vía REST a
  // microservicio Usuario
  @Column(name = "usuario_id", nullable = false)
  private Long usuarioId;

  @Column(nullable = false)
  private boolean estado;

  @Column(updatable = false)
  private LocalDateTime creadoEn;

  private LocalDateTime actualizadoEn;

  private BigDecimal total;

  @OneToMany(
    mappedBy = "orden",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<OrdenItem> items;

  public Orden() {}

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

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public List<OrdenItem> getItems() {
    return items;
  }

  public void setItems(List<OrdenItem> items) {
    this.items = items;
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
    Orden other = (Orden) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
