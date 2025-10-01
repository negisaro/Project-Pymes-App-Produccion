package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferencia de datos de Producto.
 * REFACTORIZACIÓN PENDIENTE: Candidato para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * IMPORTANTE: Nombres de campos mantenidos para compatibilidad con microservicios via Feign
 * Cambios aplicados: Validaciones agregadas, documentación mejorada, métodos de utilidad
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Información completa del producto")
public class ProductoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID único del producto", example = "1")
  private Long id;

  @NotBlank(message = "El nombre del producto es obligatorio")
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

  @NotNull(message = "El precio es obligatorio")
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

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
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
  private Boolean estado = true;

  /**
   * Constructor vacío requerido por frameworks.
   */
  public ProductoDto() {}

  /**
   * Constructor con campos básicos obligatorios.
   */
  public ProductoDto(
    String nombre,
    BigDecimal precio,
    Integer stock,
    Long categoriaId
  ) {
    this.nombre = nombre;
    this.precio = precio;
    this.stock = stock;
    this.categoriaId = categoriaId;
    this.estado = true;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }

  public Long getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Long categoriaId) {
    this.categoriaId = categoriaId;
  }

  public Long getProveedorId() {
    return proveedorId;
  }

  public void setProveedorId(Long proveedorId) {
    this.proveedorId = proveedorId;
  }

  public List<String> getImagenes() {
    return imagenes;
  }

  public void setImagenes(List<String> imagenes) {
    this.imagenes = imagenes;
  }

  public Boolean getEstado() {
    return estado;
  }

  public void setEstado(Boolean estado) {
    this.estado = estado;
  }

  // Métodos de utilidad
  public boolean estaActivo() {
    return estado != null && estado;
  }

  public boolean tieneStock() {
    return stock != null && stock > 0;
  }

  public boolean tieneStock(Integer cantidadRequerida) {
    return (
      stock != null && cantidadRequerida != null && stock >= cantidadRequerida
    );
  }

  public boolean tieneImagenes() {
    return imagenes != null && !imagenes.isEmpty();
  }

  public boolean estaDisponible() {
    return estaActivo() && tieneStock();
  }

  public String getPrimeraImagen() {
    return tieneImagenes() ? imagenes.get(0) : null;
  }

  @Override
  public String toString() {
    return (
      "ProductoDto{" +
      "id=" +
      id +
      ", nombre='" +
      nombre +
      '\'' +
      ", precio=" +
      precio +
      ", stock=" +
      stock +
      ", estado=" +
      estado +
      '}'
    );
  }
}
