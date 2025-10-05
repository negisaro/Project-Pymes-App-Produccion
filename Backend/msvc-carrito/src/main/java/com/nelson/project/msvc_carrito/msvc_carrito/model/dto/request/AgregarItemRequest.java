package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import com.nelson.project.msvc_carrito.msvc_carrito.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de agregar un item al carrito
 * MIGRADO A LOMBOK: Eliminado código boilerplate, agregados factory methods
 * IMPLEMENTA: Validation Groups y validaciones mejoradas
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para agregar un item al carrito")
public class AgregarItemRequest {

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class, ValidationGroups.OnCartOperation.class,
    },
    message = "El ID del producto es obligatorio"
  )
  @Positive(message = "El ID del producto debe ser positivo")
  @Schema(
    description = "ID del producto a agregar",
    example = "456",
    required = true
  )
  private Long productoId;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class, ValidationGroups.OnCartOperation.class,
    },
    message = "La cantidad es obligatoria"
  )
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
  @Max(
    value = 50,
    groups = ValidationGroups.OnInventoryCheck.class,
    message = "La cantidad no puede exceder 50 unidades para verificación de inventario"
  )
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

  // ================================
  // FACTORY METHODS
  // ================================

  /**
   * Crea una solicitud simple con producto y cantidad
   */
  public static AgregarItemRequest simple(Long productoId, Integer cantidad) {
    return AgregarItemRequest.builder()
      .productoId(productoId)
      .cantidad(cantidad)
      .build();
  }

  /**
   * Crea una solicitud con notas adicionales
   */
  public static AgregarItemRequest conNotas(
    Long productoId,
    Integer cantidad,
    String notas
  ) {
    return AgregarItemRequest.builder()
      .productoId(productoId)
      .cantidad(cantidad)
      .notas(notas)
      .build();
  }

  /**
   * Crea una solicitud para una unidad del producto
   */
  public static AgregarItemRequest unidad(Long productoId) {
    return AgregarItemRequest.builder()
      .productoId(productoId)
      .cantidad(1)
      .build();
  }

  /**
   * Crea una solicitud para múltiples unidades
   */
  public static AgregarItemRequest multiples(
    Long productoId,
    Integer cantidad
  ) {
    return AgregarItemRequest.builder()
      .productoId(productoId)
      .cantidad(cantidad)
      .build();
  }

  /**
   * Crea una solicitud para regalo con notas especiales
   */
  public static AgregarItemRequest regalo(
    Long productoId,
    Integer cantidad,
    String notasRegalo
  ) {
    return AgregarItemRequest.builder()
      .productoId(productoId)
      .cantidad(cantidad)
      .notas("REGALO: " + notasRegalo)
      .build();
  }
}
