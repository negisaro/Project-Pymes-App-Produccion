package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO para la solicitud de agregar un item al carrito
 * REFACTORIZACIÓN PENDIENTE: Candidato ideal para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * Cambios aplicados: Nombres uniformizados, documentación mejorada
 */
@Schema(description = "Solicitud para agregar un item al carrito")
public class AgregarItemRequest {

  @NotNull(message = "El ID del producto es obligatorio")
  @Positive(message = "El ID del producto debe ser positivo")
  @Schema(
    description = "ID del producto a agregar",
    example = "456",
    required = true
  )
  private Long productoId;

  @NotNull(message = "La cantidad es obligatoria")
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
  @Schema(
    description = "Cantidad del producto a agregar",
    example = "2",
    required = true
  )
  private Integer cantidad;

  @Size(max = 200, message = "Las notas no pueden exceder 200 caracteres")
  @Schema(
    description = "Notas adicionales para el item",
    example = "Regalo de cumpleaños"
  )
  private String notas;

  // Constructores
  public AgregarItemRequest() {}

  public AgregarItemRequest(Long productoId, Integer cantidad) {
    this.productoId = productoId;
    this.cantidad = cantidad;
  }

  public AgregarItemRequest(Long productoId, Integer cantidad, String notas) {
    this.productoId = productoId;
    this.cantidad = cantidad;
    this.notas = notas;
  }

  // Getters y Setters
  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }

  @Override
  public String toString() {
    return (
      "AgregarItemRequest{" +
      "productoId=" +
      productoId +
      ", cantidad=" +
      cantidad +
      ", notas='" +
      notas +
      '\'' +
      '}'
    );
  }
}
