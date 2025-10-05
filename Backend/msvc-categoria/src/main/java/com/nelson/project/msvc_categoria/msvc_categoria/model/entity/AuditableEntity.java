package com.nelson.project.msvc_categoria.msvc_categoria.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Clase base para auditoría automática en entidades JPA.
 * Incluye campos de auditoría robustos con información del usuario.
 */
@MappedSuperclass
public abstract class AuditableEntity {

  @CreatedDate
  @Column(name = "fecha_creacion", updatable = false, nullable = false)
  private LocalDateTime fechaCreacion;

  @LastModifiedDate
  @Column(name = "fecha_modificacion", nullable = false)
  private LocalDateTime fechaModificacion;

  @Column(name = "creado_por", length = 100, updatable = false)
  private String creadoPor;

  @Column(name = "modificado_por", length = 100)
  private String modificadoPor;

  @Column(name = "version_registro", nullable = false)
  private Long versionRegistro = 1L;

  // ========================================
  // GETTERS
  // ========================================

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public String getCreadoPor() {
    return creadoPor;
  }

  public String getModificadoPor() {
    return modificadoPor;
  }

  public Long getVersionRegistro() {
    return versionRegistro;
  }

  // ========================================
  // SETTERS (PROTEGIDOS)
  // ========================================

  protected void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  protected void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  protected void setCreadoPor(String creadoPor) {
    this.creadoPor = creadoPor;
  }

  protected void setModificadoPor(String modificadoPor) {
    this.modificadoPor = modificadoPor;
  }

  protected void setVersionRegistro(Long versionRegistro) {
    this.versionRegistro = versionRegistro;
  }

  // ========================================
  // MÉTODOS DE AUDITORÍA
  // ========================================

  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.fechaCreacion = now;
    this.fechaModificacion = now;
    this.creadoPor = obtenerUsuarioActual();
    this.modificadoPor = obtenerUsuarioActual();
    this.versionRegistro = 1L;
  }

  @PreUpdate
  public void preUpdate() {
    this.fechaModificacion = LocalDateTime.now();
    this.modificadoPor = obtenerUsuarioActual();
    this.versionRegistro++;
  }

  /**
   * Obtiene el nombre del usuario actual desde el contexto de seguridad.
   *
   * @return nombre del usuario o "sistema" si no hay usuario autenticado
   */
  private String obtenerUsuarioActual() {
    try {
      Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
      if (
        authentication != null &&
        authentication.isAuthenticated() &&
        !"anonymousUser".equals(authentication.getPrincipal())
      ) {
        return authentication.getName();
      }
    } catch (Exception e) {
      // En caso de error, continuar con valor por defecto
    }
    return "sistema";
  }

  /**
   * Verifica si la entidad fue creada recientemente (menos de 1 hora).
   */
  public boolean esReciente() {
    return (
      fechaCreacion != null &&
      fechaCreacion.isAfter(LocalDateTime.now().minusHours(1))
    );
  }

  /**
   * Verifica si la entidad fue modificada recientemente (menos de 1 hora).
   */
  public boolean fueModificadaRecientemente() {
    return (
      fechaModificacion != null &&
      fechaModificacion.isAfter(LocalDateTime.now().minusHours(1))
    );
  }

  /**
   * Obtiene la edad de la entidad en días.
   */
  public long getEdadEnDias() {
    if (fechaCreacion != null) {
      return java.time.temporal.ChronoUnit.DAYS.between(
        fechaCreacion,
        LocalDateTime.now()
      );
    }
    return 0;
  }
}
