package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.EstadoCarrito;
import com.nelson.project.msvc_carrito.msvc_carrito.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO completo para transferencia de datos del carrito de compras
 * Incluye validaciones, documentación y campos calculados
 * MIGRADO A LOMBOK: Implementa Validation Groups para contextos específicos
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Datos completos del carrito de compras")
public class CarritoDto {

  @Null(
    groups = ValidationGroups.OnCreate.class,
    message = "El ID debe ser nulo al crear"
  )
  @NotNull(
    groups = {
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnDelete.class,
      ValidationGroups.OnPayment.class,
    },
    message = "El ID es obligatorio para esta operación"
  )
  @Schema(description = "ID único del carrito", example = "1")
  private Long id;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnPayment.class,
    },
    message = "El ID del usuario es obligatorio"
  )
  @Positive(message = "El ID del usuario debe ser positivo")
  @Schema(
    description = "ID del usuario propietario del carrito",
    example = "123",
    required = true
  )
  private Long usuarioId;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnCartOperation.class,
    },
    message = "El estado del carrito es obligatorio"
  )
  @Schema(description = "Estado actual del carrito", required = true)
  private EstadoCarrito estado;

  @Valid
  @NotEmpty(
    groups = ValidationGroups.OnPayment.class,
    message = "El carrito debe tener items para proceder al pago"
  )
  @Schema(description = "Lista de items en el carrito")
  private List<ItemCarritoDto> items;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El subtotal no puede ser negativo"
  )
  @DecimalMin(
    value = "0.01",
    groups = ValidationGroups.OnPayment.class,
    message = "El carrito debe tener un subtotal mayor a 0 para proceder al pago"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El subtotal debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(
    description = "Subtotal sin descuentos ni impuestos",
    example = "99.99"
  )
  private BigDecimal subtotal;

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
  @Schema(description = "Total de descuentos aplicados", example = "10.00")
  private BigDecimal totalDescuentos;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "Los impuestos no pueden ser negativos"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "Los impuestos deben tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(description = "Total de impuestos calculados", example = "8.99")
  private BigDecimal totalImpuestos;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El total no puede ser negativo"
  )
  @DecimalMin(
    value = "0.01",
    groups = ValidationGroups.OnPayment.class,
    message = "El total debe ser mayor a 0 para proceder al pago"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El total debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(description = "Total final a pagar", example = "98.98")
  private BigDecimal total;

  @Min(value = 0, message = "La cantidad de items no puede ser negativa")
  @Min(
    value = 1,
    groups = ValidationGroups.OnPayment.class,
    message = "Debe haber al menos 1 item para proceder al pago"
  )
  @Schema(description = "Cantidad total de items en el carrito", example = "3")
  private Integer cantidadItems;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de creación del carrito",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime fechaCreacion;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de última modificación",
    example = "2024-01-15T10:35:00"
  )
  private LocalDateTime fechaModificacion;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de expiración del carrito",
    example = "2024-01-22T10:30:00"
  )
  private LocalDateTime fechaExpiracion;

  @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
  @Schema(
    description = "Notas adicionales del carrito",
    example = "Carrito para regalo"
  )
  private String notas;

  @Schema(description = "Código de cupón aplicado", example = "DESCUENTO10")
  private String codigoCupon;

  @Pattern(
    regexp = "^[A-Z]{3}$",
    message = "La moneda debe ser un código ISO de 3 letras"
  )
  @Schema(description = "Código de moneda ISO", example = "USD")
  private String moneda;

  @Schema(
    description = "Versión para control de concurrencia optimista",
    example = "1"
  )
  private Long version;

  // ================================
  // MÉTODOS DE LÓGICA DE NEGOCIO
  // ================================

  /**
   * Verifica si el carrito tiene items
   */
  public boolean tieneItems() {
    return items != null && !items.isEmpty();
  }

  /**
   * Verifica si el carrito está vacío
   */
  public boolean estaVacio() {
    return !tieneItems();
  }

  /**
   * Verifica si el carrito está activo
   */
  public boolean estaActivo() {
    return estado == EstadoCarrito.ACTIVO;
  }

  /**
   * Verifica si el carrito puede ser modificado
   */
  public boolean puedeModificarse() {
    return estado != null && estado.esModificable();
  }

  /**
   * Verifica si el carrito está en estado final
   */
  public boolean estaEnEstadoFinal() {
    return estado != null && estado.esFinal();
  }

  /**
   * Verifica si el carrito está abandonado
   */
  public boolean estaAbandonado() {
    return estado == EstadoCarrito.ABANDONADO;
  }

  /**
   * Verifica si el carrito está procesado
   */
  public boolean estaProcesado() {
    return estado == EstadoCarrito.PROCESADO;
  }

  /**
   * Verifica si el carrito está expirado
   */
  public boolean estaExpirado() {
    return estado == EstadoCarrito.EXPIRADO;
  }

  /**
   * Verifica si el carrito está bloqueado
   */
  public boolean estaBloqueado() {
    return estado == EstadoCarrito.BLOQUEADO;
  }
}
