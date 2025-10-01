package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para productos relacionados basado en patrones de compra.
 * Utilizado para mostrar productos frecuentemente comprados juntos.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(
  description = "Producto frecuentemente comprado junto con items del carrito"
)
public class ProductoRelacionadoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID del producto relacionado", example = "456")
  private Long productoId;

  @Schema(
    description = "Nombre del producto relacionado",
    example = "Teclado Mecánico"
  )
  private String nombre;

  @Schema(description = "Precio del producto", example = "89.99")
  private BigDecimal precio;

  @Schema(
    description = "Frecuencia de compra conjunta (0-100)",
    example = "65.2"
  )
  private BigDecimal frecuenciaCompraConjunta;

  @Schema(description = "Productos del carrito con los que se relaciona")
  private java.util.List<Long> productosRelacionados;

  @Schema(
    description = "Categoría del producto relacionado",
    example = "Periféricos"
  )
  private String categoria;

  @Schema(description = "URL de la imagen del producto")
  private String imagenUrl;

  @Schema(description = "Descripción breve del producto")
  private String descripcionCorta;

  // Constructores
  public ProductoRelacionadoDto() {}

  public ProductoRelacionadoDto(
    Long productoId,
    String nombre,
    BigDecimal precio,
    BigDecimal frecuencia
  ) {
    this.productoId = productoId;
    this.nombre = nombre;
    this.precio = precio;
    this.frecuenciaCompraConjunta = frecuencia;
  }

  // Getters y Setters
  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public BigDecimal getFrecuenciaCompraConjunta() {
    return frecuenciaCompraConjunta;
  }

  public void setFrecuenciaCompraConjunta(BigDecimal frecuenciaCompraConjunta) {
    this.frecuenciaCompraConjunta = frecuenciaCompraConjunta;
  }

  public java.util.List<Long> getProductosRelacionados() {
    return productosRelacionados;
  }

  public void setProductosRelacionados(
    java.util.List<Long> productosRelacionados
  ) {
    this.productosRelacionados = productosRelacionados;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
  }

  public String getDescripcionCorta() {
    return descripcionCorta;
  }

  public void setDescripcionCorta(String descripcionCorta) {
    this.descripcionCorta = descripcionCorta;
  }

  // Métodos de utilidad
  public boolean esFrecuentementeComprado() {
    return (
      frecuenciaCompraConjunta != null &&
      frecuenciaCompraConjunta.compareTo(new BigDecimal("50")) >= 0
    );
  }

  @Override
  public String toString() {
    return (
      "ProductoRelacionadoDto{" +
      "productoId=" +
      productoId +
      ", nombre='" +
      nombre +
      '\'' +
      ", frecuenciaCompraConjunta=" +
      frecuenciaCompraConjunta +
      '}'
    );
  }
}
