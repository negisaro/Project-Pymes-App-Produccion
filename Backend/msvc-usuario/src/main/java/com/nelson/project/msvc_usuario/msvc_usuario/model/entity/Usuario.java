package com.nelson.project.msvc_usuario.msvc_usuario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
public class Usuario extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(length = 50, nullable = false)
  private String name;

  @NotBlank
  @Column(length = 50, nullable = false)
  private String lastname;

  //@ExistsByUsername
  @NotBlank
  @Size(min = 4, max = 20)
  @Column(unique = true, nullable = false)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank
  @Column(nullable = false)
  private String password;

  @NotBlank
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
  private final List<Rol> roles = new ArrayList<>();

  @Builder.Default
  @Column(nullable = false)
  private boolean active = true;

  @Column(name = "reset_token")
  private String resetToken;

  @Column(name = "reset_token_expiry")
  private LocalDateTime resetTokenExpiry;

  // Métodos para manipular roles de forma controlada (encapsulamiento)
  public void addRol(Rol rol) {
    if (rol != null && !roles.contains(rol)) {
      roles.add(rol);
    }
  }

  public void removeRol(Rol rol) {
    roles.remove(rol);
  }

  public List<Rol> getRoles() {
    return Collections.unmodifiableList(roles);
  }

  // Métodos de actualización controlada para atributos sensibles
  public void updatePassword(String newPassword) {
    if (newPassword != null && !newPassword.isBlank()) {
      this.password = newPassword;
    }
  }

  /**
   * Actualiza el nombre del usuario de forma controlada.
   * @param newName Nuevo nombre (no nulo ni vacío)
   */
  public void updateName(String newName) {
    if (newName != null && !newName.isBlank()) {
      this.name = newName;
    }
  }

  /**
   * Actualiza el apellido del usuario de forma controlada.
   * @param newLastname Nuevo apellido (no nulo ni vacío)
   */
  public void updateLastname(String newLastname) {
    if (newLastname != null && !newLastname.isBlank()) {
      this.lastname = newLastname;
    }
  }

  /**
   * Actualiza el nombre de usuario de forma controlada.
   * @param newUsername Nuevo username (no nulo ni vacío)
   */
  public void updateUsername(String newUsername) {
    if (newUsername != null && !newUsername.isBlank()) {
      this.username = newUsername;
    }
  }

  /**
   * Actualiza el email del usuario de forma controlada.
   * @param newEmail Nuevo email (no nulo ni vacío)
   */
  public void updateEmail(String newEmail) {
    if (newEmail != null && !newEmail.isBlank()) {
      this.email = newEmail;
    }
  }

  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  public LocalDateTime getResetTokenExpiry() {
    return resetTokenExpiry;
  }

  public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
    this.resetTokenExpiry = resetTokenExpiry;
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }
}
