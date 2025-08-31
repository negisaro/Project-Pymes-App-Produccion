package com.project.nelson.msvc_carrito.carrito.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long usuarioId;

  private LocalDateTime creadoEn;

  @OneToMany(
    mappedBy = "carrito",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<ItemCarrito> items;

  public Carrito() {}

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

  public LocalDateTime getCreadoEn() {
    return creadoEn;
  }

  public void setCreadoEn(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn;
  }

  public List<ItemCarrito> getItems() {
    return items;
  }

  public void setItems(List<ItemCarrito> items) {
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
    Carrito other = (Carrito) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
