package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nelson.project.msvc_carrito.msvc_carrito.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Producto.
 * MIGRADO A LOMBOK: Eliminado código boilerplate, mantenidos métodos de lógica de negocio
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 * IMPLEMENTA: Validation Groups para contextos específicos
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Información completa del producto")
public class ProductoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Null(
    groups = ValidationGroups.OnCreate.class,
    message = "El ID debe ser nulo al crear"
  )
  @NotNull(
    groups = {
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnDelete.class,
      ValidationGroups.OnInventoryCheck.class,
    },
    message = "El ID del producto es obligatorio para esta operación"
  )
  @Schema(description = "ID único del producto", example = "1")
  private Long id;

  @NotBlank(
    groups = {
      ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class,
    },
    message = "El nombre del producto es obligatorio"
  )
  @Size(
    min = 2,
    max = 200,
    message = "El nombre debe tener entre 2 y 200 caracteres"
  )
  @Schema(
    description = "Nombre del producto",
    example = "Laptop Gaming",
    required = true
  )
  private String nombre;

  @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
  @Schema(
    description = "Descripción detallada del producto",
    example = "Laptop para gaming con RTX 4060"
  )
  private String descripcion;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnInventoryCheck.class,
    },
    message = "El precio es obligatorio"
  )
  @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(
    description = "Precio del producto",
    example = "999.99",
    required = true
  )
  private BigDecimal precio;

  @NotNull(
    groups = {
      ValidationGroups.OnCreate.class,
      ValidationGroups.OnUpdate.class,
      ValidationGroups.OnInventoryCheck.class,
    },
    message = "El stock es obligatorio"
  )
  @Min(value = 0, message = "El stock no puede ser negativo")
  @Min(
    value = 1,
    groups = ValidationGroups.OnInventoryCheck.class,
    message = "Debe haber stock disponible"
  )
  @Schema(description = "Stock disponible", example = "50", required = true)
  private Integer stock;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de creación del producto",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime fechaCreacion;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de última actualización",
    example = "2024-01-15T10:35:00"
  )
  private LocalDateTime fechaActualizacion;

  @NotNull(message = "La categoría es obligatoria")
  @Positive(message = "El ID de categoría debe ser positivo")
  @Schema(
    description = "ID de la categoría del producto",
    example = "5",
    required = true
  )
  private Long categoriaId;

  @Positive(message = "El ID de proveedor debe ser positivo")
  @Schema(description = "ID del proveedor del producto", example = "10")
  private Long proveedorId;

  @Schema(description = "Lista de URLs de imágenes del producto")
  private List<
    @Size(
      max = 500,
      message = "La URL de imagen no puede exceder 500 caracteres"
    ) String
  > imagenes;

  @Schema(description = "Indica si el producto está activo", example = "true")
  @JsonProperty("activo")
  @Builder.Default
  private Boolean activo = true;

  // ================================
  // MÉTODOS DE LÓGICA DE NEGOCIO
  // ================================

  /**
   * Verifica si el producto está activo
   */
  public boolean estaActivo() {
    return activo != null && activo;
  }

  /**
   * Verifica si el producto tiene stock disponible
   */
  public boolean tieneStock() {
    return stock != null && stock > 0;
  }

  /**
   * Verifica si hay stock suficiente para la cantidad requerida
   */
  public boolean tieneStock(Integer cantidadRequerida) {
    return (
      stock != null && cantidadRequerida != null && stock >= cantidadRequerida
    );
  }

  /**
   * Verifica si el producto tiene imágenes
   */
  public boolean tieneImagenes() {
    return imagenes != null && !imagenes.isEmpty();
  }

  /**
   * Verifica si el producto está disponible (activo y con stock)
   */
  public boolean estaDisponible() {
    return estaActivo() && tieneStock();
  }

  /**
   * Obtiene la primera imagen del producto
   */
  public String getPrimeraImagen() {
    return tieneImagenes() ? imagenes.get(0) : null;
  }
}
