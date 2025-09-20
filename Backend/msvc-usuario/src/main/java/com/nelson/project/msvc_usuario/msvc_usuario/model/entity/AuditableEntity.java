package com.nelson.project.msvc_usuario.msvc_usuario.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Clase base para auditoría automática en entidades JPA.
 */
@MappedSuperclass
public abstract class AuditableEntity {

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime creadoEn;

  @LastModifiedDate
  private LocalDateTime actualizadoEn;

  public LocalDateTime getCreadoEn() {
    return creadoEn;
  }

  public LocalDateTime getActualizadoEn() {
    return actualizadoEn;
  }

  // Setters protegidos para frameworks de persistencia y testeo
  protected void setCreadoEn(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn;
  }

  protected void setActualizadoEn(LocalDateTime actualizadoEn) {
    this.actualizadoEn = actualizadoEn;
  }

  /**
   * Asigna la fecha de creación antes de persistir la entidad.
   */
  @PrePersist
  public void prePersist() {
    this.creadoEn = LocalDateTime.now();
  }

  /**
   * Asigna la fecha de actualización antes de actualizar la entidad.
   */
  @PreUpdate
  public void preUpdate() {
    this.actualizadoEn = LocalDateTime.now();
  }
}
