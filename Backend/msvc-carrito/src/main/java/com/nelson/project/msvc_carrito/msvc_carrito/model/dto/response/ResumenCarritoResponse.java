package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el resumen del carrito
 * Versión ligera con información esencial
 * MIGRADO A LOMBOK: Eliminado código boilerplate, agregados factory methods
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resumen ligero del carrito de compras")
public class ResumenCarritoResponse {

  @Schema(description = "ID del carrito", example = "1")
  private Long carritoId;

  @Schema(description = "ID del usuario", example = "123")
  private Long usuarioId;

  @Schema(description = "Cantidad total de items", example = "5")
  private Integer cantidadItems;

  @Schema(description = "Cantidad total de productos únicos", example = "3")
  private Integer cantidadProductos;

  @Schema(description = "Subtotal del carrito", example = "199.99")
  private BigDecimal subtotal;

  @Schema(description = "Total de descuentos", example = "20.00")
  private BigDecimal totalDescuentos;

  @Schema(description = "Total final", example = "179.99")
  private BigDecimal totalFinal;

  @Schema(description = "Moneda del carrito", example = "USD")
  private String moneda;

  @Schema(description = "Indica si el carrito está vacío", example = "false")
  private boolean vacio;

  @Schema(description = "Indica si hay items no disponibles", example = "false")
  private boolean tieneItemsNoDisponibles;

  @Schema(description = "Lista de nombres de productos en el carrito")
  private List<String> nombresProductos;

  @Schema(description = "Información de ahorro total")
  private AhorroInfo ahorro;

  // ================================
  // FACTORY METHODS
  // ================================

  /**
   * Crea un resumen básico con carrito y usuario
   */
  public static ResumenCarritoResponse basico(Long carritoId, Long usuarioId) {
    return ResumenCarritoResponse.builder()
      .carritoId(carritoId)
      .usuarioId(usuarioId)
      .build();
  }

  /**
   * Crea un resumen vacío
   */
  public static ResumenCarritoResponse vacio(Long carritoId, Long usuarioId) {
    return ResumenCarritoResponse.builder()
      .carritoId(carritoId)
      .usuarioId(usuarioId)
      .cantidadItems(0)
      .cantidadProductos(0)
      .vacio(true)
      .totalFinal(BigDecimal.ZERO)
      .build();
  }

  /**
   * Crea un resumen completo con cálculos
   */
  public static ResumenCarritoResponse completo(
    Long carritoId,
    Long usuarioId,
    Integer cantidadItems,
    BigDecimal totalFinal
  ) {
    return ResumenCarritoResponse.builder()
      .carritoId(carritoId)
      .usuarioId(usuarioId)
      .cantidadItems(cantidadItems)
      .totalFinal(totalFinal)
      .vacio(cantidadItems == null || cantidadItems == 0)
      .build();
  }

  // Clase interna para información de ahorro
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Información sobre el ahorro obtenido")
  public static class AhorroInfo {

    @Schema(description = "Monto total ahorrado", example = "25.00")
    private BigDecimal montoAhorrado;

    @Schema(description = "Porcentaje de ahorro", example = "12.5")
    private BigDecimal porcentajeAhorro;

    @Schema(
      description = "Descripción del ahorro",
      example = "Ahorras $25.00 con descuentos aplicados"
    )
    private String descripcion;
  }
}
