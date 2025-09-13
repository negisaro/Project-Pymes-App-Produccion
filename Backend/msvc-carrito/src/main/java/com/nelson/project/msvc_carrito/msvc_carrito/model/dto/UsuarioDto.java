package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO para transferencia de datos de Usuario.
 * Profesional, funcional y escalable.
 */
public class UsuarioDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  @Size(max = 50)
  private String lastname;

  @NotBlank
  @Size(min = 4, max = 12)
  private String username;

  @NotBlank
  @Size(min = 6, max = 100)
  private String password;

  @NotBlank
  @Email
  private String email;

  private List<RolDto> roles;
  private boolean active;

  // Relación con productos (IDs de productos en el microservicio producto)
  private List<Long> carritoId;

  /**
   * Constructor vacío requerido por frameworks.
   */
  public UsuarioDto() {}

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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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

  public List<Long> getCarritoById() {
    return carritoId;
  }

  public void setCarritoById(List<Long> carritoId) {
    this.carritoId = carritoId;
  }
}
