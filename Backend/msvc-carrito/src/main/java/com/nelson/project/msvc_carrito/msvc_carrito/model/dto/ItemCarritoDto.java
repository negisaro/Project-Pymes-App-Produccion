package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_carrito.msvc_carrito.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO completo para transferencia de datos de items del carrito
 * Incluye validaciones, documentación y campos desnormalizados del producto
 * MIGRADO A LOMBOK: Implementa Validation Groups para contextos específicos
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
  description = "Item del carrito de compras con información desnormalizada del producto"
)
public class ItemCarritoDto {

  @Null(
    groups = ValidationGroups.OnCreate.class,
    message = "El ID debe ser nulo al crear el item"
  )
  @NotNull(
    groups = {
      ValidationGroups.OnUpdate.class, ValidationGroups.OnDelete.class,
    },
    message = "El ID del item es obligatorio para esta operación"
  )
  @Schema(description = "ID único del item en el carrito", example = "1")
  private Long id;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnInventoryCheck.class,
    },
    message = "El ID del producto es obligatorio"
  )
  @Positive(message = "El ID del producto debe ser positivo")
  @Schema(description = "ID del producto", example = "456", required = true)
  private Long productoId;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnCartOperation.class,
    },
    message = "La cantidad es obligatoria"
  )
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
  @Max(
    value = 50,
    groups = ValidationGroups.OnPayment.class,
    message = "La cantidad no puede exceder 50 unidades para el pago"
  )
  @Schema(
    description = "Cantidad del producto en el carrito",
    example = "2",
    required = true
  )
  private Integer cantidad;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnPayment.class,
    },
    message = "El precio unitario es obligatorio"
  )
  @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a 0")
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(
    description = "Precio unitario del producto al momento de agregarlo",
    example = "49.99",
    required = true
  )
  private BigDecimal precioUnitario;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El descuento no puede ser negativo"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El descuento debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(description = "Descuento aplicado al item", example = "5.00")
  private BigDecimal descuentoAplicado;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El subtotal no puede ser negativo"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El subtotal debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(
    description = "Subtotal calculado (cantidad * precio - descuento)",
    example = "94.98"
  )
  private BigDecimal subtotal;

  // Campos desnormalizados del producto
  @NotBlank(message = "El nombre del producto es obligatorio")
  @Size(
    max = 200,
    message = "El nombre del producto no puede exceder 200 caracteres"
  )
  @Schema(
    description = "Nombre del producto (desnormalizado)",
    example = "Laptop Gaming",
    required = true
  )
  private String nombreProducto;

  @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
  @Schema(
    description = "Descripción del producto (desnormalizada)",
    example = "Laptop para gaming con RTX 4060"
  )
  private String descripcionProducto;

  @Size(max = 100, message = "El SKU no puede exceder 100 caracteres")
  @Schema(description = "Código SKU del producto", example = "LAP-GAM-001")
  private String skuProducto;

  @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
  @Schema(
    description = "URL de la imagen principal del producto",
    example = "https://example.com/images/laptop1.jpg"
  )
  private String imagenProducto;

  @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
  @Schema(description = "Categoría del producto", example = "Electrónicos")
  private String categoriaProducto;

  @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
  @Schema(description = "Marca del producto", example = "TechBrand")
  private String marcaProducto;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El peso no puede ser negativo"
  )
  @Digits(
    integer = 5,
    fraction = 3,
    message = "El peso debe tener máximo 5 dígitos enteros y 3 decimales"
  )
  @Schema(description = "Peso del producto en kilogramos", example = "2.500")
  private BigDecimal pesoProducto;

  @Size(max = 50, message = "Las dimensiones no pueden exceder 50 caracteres")
  @Schema(description = "Dimensiones del producto", example = "35x25x2 cm")
  private String dimensionesProducto;

  @Schema(description = "Disponibilidad del producto", example = "true")
  private Boolean disponible;

  @Min(value = 0, message = "El stock no puede ser negativo")
  @Schema(description = "Stock disponible del producto", example = "10")
  private Integer stockDisponible;

  // Campos de auditoría
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha cuando se agregó el item al carrito",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime fechaAgregado;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de última modificación del item",
    example = "2024-01-15T10:35:00"
  )
  private LocalDateTime fechaModificacion;

  @Schema(
    description = "Versión para control de concurrencia optimista",
    example = "1"
  )
  private Long version;

  // ================================
  // MÉTODOS DE LÓGICA DE NEGOCIO
  // ================================

  /**
   * Verifica si el producto está disponible
   */
  public boolean estaDisponible() {
    return disponible != null && disponible;
  }

  /**
   * Verifica si hay stock suficiente para la cantidad requerida
   */
  public boolean tieneStock(Integer cantidadRequerida) {
    return stockDisponible != null && stockDisponible >= cantidadRequerida;
  }

  /**
   * Calcula el total del item considerando cantidad, precio y descuentos
   */
  public BigDecimal calcularTotal() {
    if (subtotal != null) {
      return subtotal;
    }
    if (cantidad != null && precioUnitario != null) {
      BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
      if (descuentoAplicado != null) {
        total = total.subtract(descuentoAplicado);
      }
      return total;
    }
    return BigDecimal.ZERO;
  }
}
