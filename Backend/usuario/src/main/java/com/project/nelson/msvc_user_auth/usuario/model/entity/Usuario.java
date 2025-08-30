package com.project.nelson.msvc_user_auth.usuario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.nelson.msvc_user_auth.usuario.validation.ExistsByUsername;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario para gestión de usuarios en el sistema.
 * Incluye campos auditables y buenas prácticas empresariales.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Column(length = 50, nullable = false)
  private String name;

  @NotEmpty
  @Column(length = 50, nullable = false)
  private String lastname;

  @ExistsByUsername
  @NotBlank
  @Size(min = 4, max = 20)
  @Column(unique = true, nullable = false)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank
  @Column(nullable = false)
  private String password;

  @NotEmpty
  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @JsonIgnoreProperties({ "usuarios", "handler", "hibernateLazyInitializer" })
  @ManyToMany
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(
      name = "rol_id",
      referencedColumnName = "id"
    ),
    uniqueConstraints = {
      @UniqueConstraint(columnNames = { "user_id", "rol_id" }),
    }
  )
  private List<Rol> roles = new ArrayList<>();

  // Campo persistente para compatibilidad con el frontend y el controlador
  @Column(nullable = false)
  private boolean active = true;

  /**
   * Campo transitorio para lógica de negocio, no persistente.
   */

  /**
   * Fecha de creación del usuario.
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  /**
   * Fecha de última modificación del usuario.
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.active = true;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public Usuario() {
    // Constructor vacío requerido por JPA
  }

  // Getters y setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  // Getter para el campo active (compatibilidad frontend)
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<Rol> getRoles() {
    return roles;
  }

  public void setRoles(List<Rol> roles) {
    this.roles = roles;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Usuario other = (Usuario) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (username == null) {
      if (other.username != null) return false;
    } else if (!username.equals(other.username)) return false;
    return true;
  }
}
