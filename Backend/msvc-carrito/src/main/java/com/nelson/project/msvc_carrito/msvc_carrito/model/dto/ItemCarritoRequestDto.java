package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para solicitudes de agregar/actualizar items en el carrito.
 * Optimizado para operaciones de entrada con validaciones empresariales.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(
  description = "Solicitud para agregar o actualizar un item en el carrito"
)
public class ItemCarritoRequestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "El ID del producto es obligatorio")
  @Positive(message = "El ID del producto debe ser positivo")
  @Schema(description = "ID único del producto", example = "1", required = true)
  private Long productoId;

  @NotNull(message = "La cantidad es obligatoria")
  @Min(value = 1, message = "La cantidad mínima es 1")
  @Max(value = 999, message = "La cantidad máxima permitida es 999")
  @Schema(
    description = "Cantidad del producto a agregar",
    example = "2",
    required = true
  )
  private Integer cantidad;

  @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(
    description = "Precio unitario del producto (opcional, se obtiene del servicio si no se proporciona)",
    example = "99.99"
  )
  private BigDecimal precioUnitario;

  @Size(
    max = 200,
    message = "El nombre del producto no puede exceder 200 caracteres"
  )
  @Schema(
    description = "Nombre del producto (opcional, se obtiene del servicio si no se proporciona)",
    example = "Laptop Gaming"
  )
  private String nombreProducto;

  @Schema(
    description = "Indica si se debe forzar la actualización de precio desde el servicio",
    example = "false"
  )
  private boolean forzarActualizacionPrecio = false;

  @Schema(
    description = "Indica si se debe validar stock antes de agregar",
    example = "true"
  )
  private boolean validarStock = true;

  // Constructores
  public ItemCarritoRequestDto() {}

  public ItemCarritoRequestDto(Long productoId, Integer cantidad) {
    this.productoId = productoId;
    this.cantidad = cantidad;
    this.validarStock = true;
  }

  public ItemCarritoRequestDto(
    Long productoId,
    Integer cantidad,
    BigDecimal precioUnitario
  ) {
    this.productoId = productoId;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    this.validarStock = true;
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

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public boolean isForzarActualizacionPrecio() {
    return forzarActualizacionPrecio;
  }

  public void setForzarActualizacionPrecio(boolean forzarActualizacionPrecio) {
    this.forzarActualizacionPrecio = forzarActualizacionPrecio;
  }

  public boolean isValidarStock() {
    return validarStock;
  }

  public void setValidarStock(boolean validarStock) {
    this.validarStock = validarStock;
  }

  // Métodos de utilidad
  public boolean tienePrecioUnitario() {
    return (
      precioUnitario != null && precioUnitario.compareTo(BigDecimal.ZERO) > 0
    );
  }

  public boolean tieneNombreProducto() {
    return nombreProducto != null && !nombreProducto.trim().isEmpty();
  }

  @Override
  public String toString() {
    return (
      "ItemCarritoRequestDto{" +
      "productoId=" +
      productoId +
      ", cantidad=" +
      cantidad +
      ", precioUnitario=" +
      precioUnitario +
      ", nombreProducto='" +
      nombreProducto +
      '\'' +
      ", validarStock=" +
      validarStock +
      '}'
    );
  }
}
