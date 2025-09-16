package com.nelson.project.msvc_categoria.msvc_categoria.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidad profesional, funcional y escalable para Categoría.
 * Aplica principios SOLID y buenas prácticas modernas.
 */
@Entity
@Table(name = "categorias")
@EntityListeners(AuditingEntityListener.class)
public class Categoria extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 255)
  @Column(nullable = false)
  private String nombre;

  @Size(max = 1000)
  private String descripcion;

  @NotNull
  @Column(nullable = false)
  private Boolean estado;

  public Categoria() {}

  public Categoria(String nombre, String descripcion, Boolean estado) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.estado = estado;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Boolean getEstado() {
    return estado;
  }

  // No setters para id ni auditoría para mantener integridad
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setEstado(Boolean estado) {
    this.estado = estado;
  }
}
