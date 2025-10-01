package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO para transferencia de datos de Usuario.
 * REFACTORIZACIÓN PENDIENTE: Candidato para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 * Cambios aplicados: Documentación mejorada, validaciones consistentes, métodos de utilidad
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Datos del usuario con información de roles y carritos")
public class UsuarioDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID único del usuario", example = "1")
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(
    min = 2,
    max = 50,
    message = "El nombre debe tener entre 2 y 50 caracteres"
  )
  @Schema(description = "Nombre del usuario", example = "Juan", required = true)
  private String name;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(
    min = 2,
    max = 50,
    message = "El apellido debe tener entre 2 y 50 caracteres"
  )
  @Schema(
    description = "Apellido del usuario",
    example = "Pérez",
    required = true
  )
  private String lastname;

  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(
    min = 4,
    max = 20,
    message = "El nombre de usuario debe tener entre 4 y 20 caracteres"
  )
  @Schema(
    description = "Nombre de usuario único",
    example = "juanperez",
    required = true
  )
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(
    min = 8,
    max = 100,
    message = "La contraseña debe tener entre 8 y 100 caracteres"
  )
  @Schema(
    description = "Contraseña del usuario",
    example = "********",
    required = true
  )
  private String password;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El email debe tener un formato válido")
  @Size(max = 100, message = "El email no puede exceder 100 caracteres")
  @Schema(
    description = "Email del usuario",
    example = "juan.perez@email.com",
    required = true
  )
  private String email;

  @Valid
  @Schema(description = "Lista de roles asignados al usuario")
  private List<RolDto> roles;

  @Schema(description = "Indica si el usuario está activo", example = "true")
  private boolean active = true;

  @Schema(description = "Lista de IDs de carritos asociados al usuario")
  private List<Long> carritoId;

  /**
   * Constructor vacío requerido por frameworks.
   */
  public UsuarioDto() {}

  /**
   * Constructor con campos básicos obligatorios.
   */
  public UsuarioDto(
    String name,
    String lastname,
    String username,
    String email
  ) {
    this.name = name;
    this.lastname = lastname;
    this.username = username;
    this.email = email;
    this.active = true;
  }

  /**
   * Constructor completo.
   */
  public UsuarioDto(
    Long id,
    String name,
    String lastname,
    String username,
    String password,
    String email,
    List<RolDto> roles,
    List<Long> carritoId,
    boolean active
  ) {
    this.id = id;
    this.name = name;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = roles;
    this.carritoId = carritoId;
    this.active = active;
  }

  // Getters y Setters
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<RolDto> getRoles() {
    return roles;
  }

  public void setRoles(List<RolDto> roles) {
    this.roles = roles;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<Long> getCarritoById() {
    return carritoId;
  }

  public void setCarritoById(List<Long> carritoId) {
    this.carritoId = carritoId;
  }

  // Métodos de utilidad
  public boolean estaActivo() {
    return active;
  }

  public String getNombreCompleto() {
    if (name != null && lastname != null) {
      return name + " " + lastname;
    }
    return name != null ? name : lastname;
  }

  public boolean tieneRoles() {
    return roles != null && !roles.isEmpty();
  }

  public boolean tieneCarritos() {
    return carritoId != null && !carritoId.isEmpty();
  }

  @Override
  public String toString() {
    return (
      "UsuarioDto{" +
      "id=" +
      id +
      ", username='" +
      username +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", active=" +
      active +
      '}'
    );
  }
}
