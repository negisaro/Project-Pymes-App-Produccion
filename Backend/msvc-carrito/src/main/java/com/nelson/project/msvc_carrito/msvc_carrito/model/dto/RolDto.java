package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO para transferencia de datos de Rol.
 * REFACTORIZACIÓN PENDIENTE: Candidato ideal para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 * Cambios aplicados: Documentación mejorada, validaciones agregadas, métodos de utilidad
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Información del rol de usuario")
public class RolDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID único del rol", example = "1")
  private Long id;

  @NotBlank(message = "El nombre del rol es obligatorio")
  @Size(
    min = 3,
    max = 50,
    message = "El nombre del rol debe tener entre 3 y 50 caracteres"
  )
  @Schema(description = "Nombre del rol", example = "ADMIN", required = true)
  private String name;

  @Schema(description = "Indica si el rol está activo", example = "true")
  private boolean activo = true;

  /**
   * Constructor vacío requerido por frameworks.
   */
  public RolDto() {}

  /**
   * Constructor con nombre del rol.
   */
  public RolDto(String name) {
    this.name = name;
    this.activo = true;
  }

  /**
   * Constructor completo.
   */
  public RolDto(Long id, String name, boolean activo) {
    this.id = id;
    this.name = name;
    this.activo = activo;
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

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  // Métodos de utilidad
  public boolean estaActivo() {
    return activo;
  }

  public boolean esAdministrador() {
    return (
      name != null &&
      (name.equalsIgnoreCase("ADMIN") || name.equalsIgnoreCase("ADMINISTRATOR"))
    );
  }

  public boolean esUsuario() {
    return name != null && name.equalsIgnoreCase("USER");
  }

  public boolean esModerador() {
    return name != null && name.equalsIgnoreCase("MODERATOR");
  }

  @Override
  public String toString() {
    return (
      "RolDto{" +
      "id=" +
      id +
      ", name='" +
      name +
      '\'' +
      ", activo=" +
      activo +
      '}'
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    RolDto rolDto = (RolDto) obj;
    return id != null && id.equals(rolDto.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
