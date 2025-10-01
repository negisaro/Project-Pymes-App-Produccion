package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para la solicitud de crear un nuevo carrito
 * REFACTORIZACIÓN PENDIENTE: Candidato ideal para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * Cambios aplicados: Nombres uniformizados, documentación mejorada
 */
@Schema(description = "Solicitud para crear un nuevo carrito")
public class CrearCarritoRequest {

  @NotNull(message = "El ID del usuario es obligatorio")
  @Positive(message = "El ID del usuario debe ser positivo")
  @Schema(
    description = "ID del usuario propietario del carrito",
    example = "123",
    required = true
  )
  private Long usuarioId;

  @Schema(
    description = "Notas iniciales del carrito",
    example = "Carrito para compras navideñas"
  )
  private String notasIniciales;

  @Schema(description = "Código de moneda ISO", example = "USD")
  private String moneda = "USD";

  // Constructores
  public CrearCarritoRequest() {}

  public CrearCarritoRequest(Long usuarioId) {
    this.usuarioId = usuarioId;
    this.moneda = "USD";
  }

  public CrearCarritoRequest(Long usuarioId, String moneda) {
    this.usuarioId = usuarioId;
    this.moneda = moneda;
  }

  // Getters y Setters
  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getNotasIniciales() {
    return notasIniciales;
  }

  public void setNotasIniciales(String notasIniciales) {
    this.notasIniciales = notasIniciales;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  @Override
  public String toString() {
    return (
      "CrearCarritoRequest{" +
      "usuarioId=" +
      usuarioId +
      ", notasIniciales='" +
      notasIniciales +
      '\'' +
      ", moneda='" +
      moneda +
      '\'' +
      '}'
    );
  }
}
