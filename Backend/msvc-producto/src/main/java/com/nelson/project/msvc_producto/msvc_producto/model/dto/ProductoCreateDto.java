package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para creación/actualización de Producto.
 * No incluye campos autogenerados ni de sistema. Incluye validaciones y es serializable.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Nombre del producto */
  @NotBlank
  private String nombre;

  /** Descripción del producto */
  @Size(max = 1000)
  private String descripcion;

  /** Precio del producto */
  @NotNull
  @DecimalMin("0.00")
  private BigDecimal precio;

  /** Stock disponible */
  @NotNull
  @Min(0)
  private Integer stock;

  /** ID de la categoría asociada */
  @NotNull
  private Long categoriaId;

  /** ID del proveedor asociado */
  @NotNull
  private Long proveedorId;

  /** Lista de URLs de imágenes */
  private List<@NotBlank String> imagenes;

  /** Estado del producto (activo/inactivo) */
  @NotNull
  private Boolean estado;
}
