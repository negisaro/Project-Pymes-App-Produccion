package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Usuario.
 * MIGRADO A LOMBOK: Eliminado código boilerplate, mantenidos métodos de lógica de negocio
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
  @Builder.Default
  private boolean activo = true;

  @Schema(description = "Lista de IDs de carritos asociados al usuario")
  private List<Long> carritoId;

  // ================================
  // MÉTODOS DE LÓGICA DE NEGOCIO
  // ================================

  /**
   * Verifica si el usuario está activo
   */
  public boolean isActive() {
    return activo;
  }

  /**
   * Obtiene el nombre completo del usuario
   */
  public String getNombreCompleto() {
    if (name != null && lastname != null) {
      return name + " " + lastname;
    }
    return name != null ? name : lastname;
  }

  /**
   * Verifica si el usuario tiene roles asignados
   */
  public boolean tieneRoles() {
    return roles != null && !roles.isEmpty();
  }

  /**
   * Verifica si el usuario tiene carritos asociados
   */
  public boolean tieneCarritos() {
    return carritoId != null && !carritoId.isEmpty();
  }
}
