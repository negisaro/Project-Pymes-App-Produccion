package com.project.nelson.msvc_carrito.carrito.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long productoId;
  
  private Integer cantidad;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "carrito_id")
  private Carrito carrito;

  public ItemCarrito() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public Carrito getCarrito() {
    return carrito;
  }

  public void setCarrito(Carrito carrito) {
    this.carrito = carrito;
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
    ItemCarrito other = (ItemCarrito) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
