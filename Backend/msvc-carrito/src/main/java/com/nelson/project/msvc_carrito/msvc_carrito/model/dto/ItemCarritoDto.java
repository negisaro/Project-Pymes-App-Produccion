package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO completo para transferencia de datos de items del carrito
 * Incluye validaciones, documentación y campos desnormalizados del producto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
  description = "Item del carrito de compras con información desnormalizada del producto"
)
public class ItemCarritoDto {

  @Schema(description = "ID único del item en el carrito", example = "1")
  private Long id;

  @NotNull(message = "El ID del producto es obligatorio")
  @Positive(message = "El ID del producto debe ser positivo")
  @Schema(description = "ID del producto", example = "456", required = true)
  private Long productoId;

  @NotNull(message = "La cantidad es obligatoria")
  @Min(value = 1, message = "La cantidad debe ser al menos 1")
  @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
  @Schema(
    description = "Cantidad del producto en el carrito",
    example = "2",
    required = true
  )
  private Integer cantidad;

  @NotNull(message = "El precio unitario es obligatorio")
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

  // Constructores
  public ItemCarritoDto() {}

  public ItemCarritoDto(
    Long productoId,
    Integer cantidad,
    BigDecimal precioUnitario
  ) {
    this.productoId = productoId;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public BigDecimal getDescuentoAplicado() {
    return descuentoAplicado;
  }

  public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
    this.descuentoAplicado = descuentoAplicado;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public String getDescripcionProducto() {
    return descripcionProducto;
  }

  public void setDescripcionProducto(String descripcionProducto) {
    this.descripcionProducto = descripcionProducto;
  }

  public String getSkuProducto() {
    return skuProducto;
  }

  public void setSkuProducto(String skuProducto) {
    this.skuProducto = skuProducto;
  }

  public String getImagenProducto() {
    return imagenProducto;
  }

  public void setImagenProducto(String imagenProducto) {
    this.imagenProducto = imagenProducto;
  }

  public String getCategoriaProducto() {
    return categoriaProducto;
  }

  public void setCategoriaProducto(String categoriaProducto) {
    this.categoriaProducto = categoriaProducto;
  }

  public String getMarcaProducto() {
    return marcaProducto;
  }

  public void setMarcaProducto(String marcaProducto) {
    this.marcaProducto = marcaProducto;
  }

  public BigDecimal getPesoProducto() {
    return pesoProducto;
  }

  public void setPesoProducto(BigDecimal pesoProducto) {
    this.pesoProducto = pesoProducto;
  }

  public String getDimensionesProducto() {
    return dimensionesProducto;
  }

  public void setDimensionesProducto(String dimensionesProducto) {
    this.dimensionesProducto = dimensionesProducto;
  }

  public Boolean getDisponible() {
    return disponible;
  }

  public void setDisponible(Boolean disponible) {
    this.disponible = disponible;
  }

  public Integer getStockDisponible() {
    return stockDisponible;
  }

  public void setStockDisponible(Integer stockDisponible) {
    this.stockDisponible = stockDisponible;
  }

  public LocalDateTime getFechaAgregado() {
    return fechaAgregado;
  }

  public void setFechaAgregado(LocalDateTime fechaAgregado) {
    this.fechaAgregado = fechaAgregado;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  // Métodos de utilidad
  public boolean estaDisponible() {
    return disponible != null && disponible;
  }

  public boolean tieneStock(Integer cantidadRequerida) {
    return stockDisponible != null && stockDisponible >= cantidadRequerida;
  }

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

  @Override
  public String toString() {
    return (
      "ItemCarritoDto{" +
      "id=" +
      id +
      ", productoId=" +
      productoId +
      ", nombreProducto='" +
      nombreProducto +
      '\'' +
      ", cantidad=" +
      cantidad +
      ", precioUnitario=" +
      precioUnitario +
      ", subtotal=" +
      subtotal +
      '}'
    );
  }
}
