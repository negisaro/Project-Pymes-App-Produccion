package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidad base que proporciona campos de auditoría comunes
 * para todas las entidades del sistema.
 *
 * Implementa principios SOLID:
 * - Single Responsibility: Solo maneja auditoría
 * - Open/Closed: Extensible para nuevas entidades
 *
 * REFACTORIZADA: Manteniendo enfoque clásico por consistencia
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityCorrected {

  @CreatedDate
  @Column(name = "creado_en", nullable = false, updatable = false)
  private LocalDateTime creadoEn;

  @LastModifiedDate
  @Column(name = "actualizado_en")
  private LocalDateTime actualizadoEn;

  @CreatedBy
  @Column(name = "creado_por", length = 100, updatable = false)
  private String creadoPor;

  @LastModifiedBy
  @Column(name = "actualizado_por", length = 100)
  private String actualizadoPor;

  @Version
  @Column(name = "version")
  private Long version;

  // Constructors
  protected BaseEntityCorrected() {
    this.creadoEn = LocalDateTime.now();
  }

  // Constructor personalizado
  protected BaseEntityCorrected(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn != null ? creadoEn : LocalDateTime.now();
  }

  // Métodos de conveniencia para auditoría
  @PrePersist
  protected void prePersist() {
    if (this.creadoEn == null) {
      this.creadoEn = LocalDateTime.now();
    }
    this.actualizadoEn = LocalDateTime.now();
  }

  @PreUpdate
  protected void preUpdate() {
    this.actualizadoEn = LocalDateTime.now();
  }

  // Getters y Setters
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

  public String getCreadoPor() {
    return creadoPor;
  }

  public void setCreadoPor(String creadoPor) {
    this.creadoPor = creadoPor;
  }

  public String getActualizadoPor() {
    return actualizadoPor;
  }

  public void setActualizadoPor(String actualizadoPor) {
    this.actualizadoPor = actualizadoPor;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    BaseEntityCorrected that = (BaseEntityCorrected) obj;
    return (
      Objects.equals(creadoEn, that.creadoEn) &&
      Objects.equals(creadoPor, that.creadoPor)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(creadoEn, creadoPor);
  }
}
