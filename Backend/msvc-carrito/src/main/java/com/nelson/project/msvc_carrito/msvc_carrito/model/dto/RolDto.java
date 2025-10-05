package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Rol.
 * MIGRADO A LOMBOK: Eliminado código boilerplate, mantenidos métodos de lógica de negocio
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
  @Builder.Default
  private boolean activo = true;

  // ================================
  // MÉTODOS DE LÓGICA DE NEGOCIO
  // ================================

  /**
   * Verifica si el rol está activo
   */
  public boolean estaActivo() {
    return activo;
  }

  /**
   * Verifica si es un rol de administrador
   */
  public boolean esAdministrador() {
    return (
      name != null &&
      (name.equalsIgnoreCase("ADMIN") || name.equalsIgnoreCase("ADMINISTRATOR"))
    );
  }

  /**
   * Verifica si es un rol de usuario normal
   */
  public boolean esUsuario() {
    return name != null && name.equalsIgnoreCase("USER");
  }

  /**
   * Verifica si es un rol de moderador
   */
  public boolean esModerador() {
    return name != null && name.equalsIgnoreCase("MODERATOR");
  }
}
