package com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity;

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

  @PrePersist
  public void prePersist() {
    this.creadoEn = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.actualizadoEn = LocalDateTime.now();
  }
}
