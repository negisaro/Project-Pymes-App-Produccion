package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas y consultas de Producto.
 * Incluye todos los campos relevantes y es serializable para transferencias entre servicios.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del producto */
  private Long id;

  /** Nombre del producto */
  private String nombre;

  /** Descripción del producto */
  private String descripcion;

  /** Precio del producto */
  private BigDecimal precio;

  /** Stock disponible */
  private Integer stock;

  /** Fecha de creación */
  private LocalDateTime creadoEn;

  /** Fecha de última actualización */
  private LocalDateTime actualizadoEn;

  /** ID de la categoría asociada */
  private Long categoriaId;

  /** ID del proveedor asociado */
  private Long proveedorId;

  /** Lista de URLs de imágenes */
  private List<String> imagenes;

  /** Estado del producto (activo/inactivo) */
  private Boolean estado;
}
