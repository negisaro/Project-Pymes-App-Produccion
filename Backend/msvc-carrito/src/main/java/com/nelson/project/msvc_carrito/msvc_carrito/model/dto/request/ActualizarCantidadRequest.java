package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO para la solicitud de actualizar la cantidad de un item en el carrito
 * REFACTORIZACIÓN PENDIENTE: Candidato ideal para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * Cambios aplicados: Nombres uniformizados, documentación mejorada
 */
@Schema(description = "Solicitud para actualizar la cantidad de un item")
public class ActualizarCantidadRequest {

  @NotNull(message = "La nueva cantidad es obligatoria")
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
  @Schema(
    description = "Nueva cantidad del producto",
    example = "3",
    required = true
  )
  private Integer nuevaCantidad;

  @Schema(
    description = "Motivo del cambio",
    example = "Cambio de cantidad por el cliente"
  )
  private String motivo;

  // Constructores
  public ActualizarCantidadRequest() {}

  public ActualizarCantidadRequest(Integer nuevaCantidad) {
    this.nuevaCantidad = nuevaCantidad;
  }

  public ActualizarCantidadRequest(Integer nuevaCantidad, String motivo) {
    this.nuevaCantidad = nuevaCantidad;
    this.motivo = motivo;
  }

  // Getters y Setters
  public Integer getNuevaCantidad() {
    return nuevaCantidad;
  }

  public void setNuevaCantidad(Integer nuevaCantidad) {
    this.nuevaCantidad = nuevaCantidad;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  @Override
  public String toString() {
    return (
      "ActualizarCantidadRequest{" +
      "nuevaCantidad=" +
      nuevaCantidad +
      ", motivo='" +
      motivo +
      '\'' +
      '}'
    );
  }
}
