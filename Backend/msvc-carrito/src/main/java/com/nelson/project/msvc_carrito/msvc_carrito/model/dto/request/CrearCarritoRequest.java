package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import com.nelson.project.msvc_carrito.msvc_carrito.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de crear un nuevo carrito
 * MIGRADO A LOMBOK: Eliminado código boilerplate, agregados factory methods
 * IMPLEMENTA: Validation Groups y validaciones mejoradas
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para crear un nuevo carrito")
public class CrearCarritoRequest {

  @NotNull(
    groups = ValidationGroups.OnCreate.class,
    message = "El ID del usuario es obligatorio"
  )
  @Positive(message = "El ID del usuario debe ser positivo")
  @Schema(
    description = "ID del usuario propietario del carrito",
    example = "123",
    required = true
  )
  private Long usuarioId;

  @Size(
    max = 500,
    message = "Las notas iniciales no pueden exceder 500 caracteres"
  )
  @Schema(
    description = "Notas iniciales del carrito",
    example = "Carrito para compras navideñas"
  )
  private String notasIniciales;

  @Pattern(
    regexp = "^[A-Z]{3}$",
    message = "La moneda debe ser un código ISO de 3 letras"
  )
  @Builder.Default
  @Schema(description = "Código de moneda ISO", example = "USD")
  private String moneda = "USD";

  // ================================
  // FACTORY METHODS
  // ================================

  /**
   * Crea una solicitud simple con solo el ID de usuario
   */
  public static CrearCarritoRequest parUsuario(Long usuarioId) {
    return CrearCarritoRequest.builder().usuarioId(usuarioId).build();
  }

  /**
   * Crea una solicitud con usuario y notas
   */
  public static CrearCarritoRequest conNotas(Long usuarioId, String notas) {
    return CrearCarritoRequest.builder()
      .usuarioId(usuarioId)
      .notasIniciales(notas)
      .build();
  }

  /**
   * Crea una solicitud con moneda específica
   */
  public static CrearCarritoRequest enMoneda(Long usuarioId, String moneda) {
    return CrearCarritoRequest.builder()
      .usuarioId(usuarioId)
      .moneda(moneda)
      .build();
  }

  /**
   * Crea una solicitud completa
   */
  public static CrearCarritoRequest completa(
    Long usuarioId,
    String notas,
    String moneda
  ) {
    return CrearCarritoRequest.builder()
      .usuarioId(usuarioId)
      .notasIniciales(notas)
      .moneda(moneda)
      .build();
  }
}
