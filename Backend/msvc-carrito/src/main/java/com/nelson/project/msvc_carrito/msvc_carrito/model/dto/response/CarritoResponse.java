package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para operaciones del carrito con información adicional
 * MIGRADO A LOMBOK: Eliminado código boilerplate, clases internas optimizadas
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
  description = "Respuesta completa del carrito con información de cálculos y validaciones"
)
public class CarritoResponse {

  @Schema(description = "Información completa del carrito")
  private CarritoDto carrito;

  @Schema(description = "Cálculos detallados del carrito")
  private CalculosCarrito calculos;

  @Schema(description = "Información de validaciones y disponibilidad")
  private ValidacionCarrito validacion;

  @Schema(description = "Recomendaciones de productos relacionados")
  private List<RecomendacionProducto> recomendaciones;

  @Schema(description = "Información de descuentos disponibles")
  private List<DescuentoDisponible> descuentosDisponibles;

  // ================================
  // FACTORY METHODS
  // ================================

  /**
   * Crea una respuesta simple con solo el carrito
   */
  public static CarritoResponse simple(CarritoDto carrito) {
    return CarritoResponse.builder().carrito(carrito).build();
  }

  /**
   * Crea una respuesta completa con toda la información
   */
  public static CarritoResponse completa(
    CarritoDto carrito,
    CalculosCarrito calculos,
    ValidacionCarrito validacion
  ) {
    return CarritoResponse.builder()
      .carrito(carrito)
      .calculos(calculos)
      .validacion(validacion)
      .build();
  }

  // ================================
  // CLASES INTERNAS OPTIMIZADAS
  // ================================

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Cálculos detallados del carrito")
  public static class CalculosCarrito {

    @Schema(description = "Subtotal antes de descuentos", example = "199.99")
    private BigDecimal subtotalBruto;

    @Schema(description = "Total de descuentos aplicados", example = "20.00")
    private BigDecimal totalDescuentos;

    @Schema(description = "Subtotal después de descuentos", example = "179.99")
    private BigDecimal subtotalNeto;

    @Schema(description = "Total de impuestos", example = "18.00")
    private BigDecimal totalImpuestos;

    @Schema(description = "Costo de envío estimado", example = "5.99")
    private BigDecimal costoEnvio;

    @Schema(description = "Total final", example = "203.98")
    private BigDecimal totalFinal;

    @Schema(description = "Peso total del carrito en kg", example = "2.5")
    private BigDecimal pesoTotal;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Información de validación del carrito")
  public static class ValidacionCarrito {

    @Schema(
      description = "Indica si todos los productos están disponibles",
      example = "true"
    )
    private boolean todosDisponibles;

    @Schema(
      description = "Indica si hay suficiente stock para todos los items",
      example = "true"
    )
    private boolean suficienteStock;

    @Schema(description = "Lista de items con problemas de stock")
    private List<String> itemsConProblemas;

    @Schema(description = "Mensajes de advertencia")
    private List<String> advertencias;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Recomendación de producto")
  public static class RecomendacionProducto {

    @Schema(description = "ID del producto recomendado", example = "789")
    private Long productoId;

    @Schema(
      description = "Nombre del producto",
      example = "Producto complementario"
    )
    private String nombre;

    @Schema(description = "Precio del producto", example = "29.99")
    private BigDecimal precio;

    @Schema(
      description = "Motivo de la recomendación",
      example = "Frecuentemente comprado junto"
    )
    private String motivo;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Descuento disponible")
  public static class DescuentoDisponible {

    @Schema(description = "Código del descuento", example = "PRIMERAVEZ")
    private String codigo;

    @Schema(
      description = "Descripción del descuento",
      example = "10% de descuento para nuevos clientes"
    )
    private String descripcion;

    @Schema(description = "Porcentaje de descuento", example = "10")
    private BigDecimal porcentaje;

    @Schema(description = "Monto mínimo requerido", example = "50.00")
    private BigDecimal montoMinimo;
  }
}
