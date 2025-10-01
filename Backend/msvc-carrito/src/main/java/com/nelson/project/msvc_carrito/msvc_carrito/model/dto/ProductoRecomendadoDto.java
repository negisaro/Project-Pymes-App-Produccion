package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para productos recomendados en el carrito.
 * Utilizado por sistemas de recomendación y cross-selling.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(description = "Producto recomendado para el carrito")
public class ProductoRecomendadoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID único del producto", example = "123")
  private Long productoId;

  @Schema(description = "Nombre del producto", example = "Mouse Gaming RGB")
  private String nombre;

  @Schema(description = "Precio actual del producto", example = "49.99")
  private BigDecimal precio;

  @Schema(description = "URL de la imagen del producto")
  private String imagenUrl;

  @Schema(description = "Puntuación de recomendación (0-100)", example = "85.5")
  private BigDecimal puntuacionRecomendacion;

  @Schema(
    description = "Razón de la recomendación",
    example = "Frequently bought together"
  )
  private String razonRecomendacion;

  @Schema(description = "Tipo de recomendación", example = "CROSS_SELL")
  private String tipoRecomendacion; // CROSS_SELL, UP_SELL, SIMILAR, POPULAR

  @Schema(description = "Categoría del producto", example = "Accesorios")
  private String categoria;

  @Schema(description = "Calificación promedio del producto", example = "4.5")
  private BigDecimal calificacionPromedio;

  @Schema(description = "Número de reviews del producto", example = "127")
  private Integer numeroReviews;

  @Schema(
    description = "Indica si el producto está en oferta",
    example = "true"
  )
  private boolean enOferta;

  @Schema(
    description = "Porcentaje de descuento si está en oferta",
    example = "15.0"
  )
  private BigDecimal porcentajeDescuento;

  @Schema(description = "Stock disponible del producto", example = "25")
  private Integer stockDisponible;

  // Constructores
  public ProductoRecomendadoDto() {}

  public ProductoRecomendadoDto(
    Long productoId,
    String nombre,
    BigDecimal precio
  ) {
    this.productoId = productoId;
    this.nombre = nombre;
    this.precio = precio;
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

  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
  }

  public BigDecimal getPuntuacionRecomendacion() {
    return puntuacionRecomendacion;
  }

  public void setPuntuacionRecomendacion(BigDecimal puntuacionRecomendacion) {
    this.puntuacionRecomendacion = puntuacionRecomendacion;
  }

  public String getRazonRecomendacion() {
    return razonRecomendacion;
  }

  public void setRazonRecomendacion(String razonRecomendacion) {
    this.razonRecomendacion = razonRecomendacion;
  }

  public String getTipoRecomendacion() {
    return tipoRecomendacion;
  }

  public void setTipoRecomendacion(String tipoRecomendacion) {
    this.tipoRecomendacion = tipoRecomendacion;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public BigDecimal getCalificacionPromedio() {
    return calificacionPromedio;
  }

  public void setCalificacionPromedio(BigDecimal calificacionPromedio) {
    this.calificacionPromedio = calificacionPromedio;
  }

  public Integer getNumeroReviews() {
    return numeroReviews;
  }

  public void setNumeroReviews(Integer numeroReviews) {
    this.numeroReviews = numeroReviews;
  }

  public boolean isEnOferta() {
    return enOferta;
  }

  public void setEnOferta(boolean enOferta) {
    this.enOferta = enOferta;
  }

  public BigDecimal getPorcentajeDescuento() {
    return porcentajeDescuento;
  }

  public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
    this.porcentajeDescuento = porcentajeDescuento;
  }

  public Integer getStockDisponible() {
    return stockDisponible;
  }

  public void setStockDisponible(Integer stockDisponible) {
    this.stockDisponible = stockDisponible;
  }

  // Métodos de utilidad
  public boolean tieneStockSuficiente(int cantidadSolicitada) {
    return stockDisponible != null && stockDisponible >= cantidadSolicitada;
  }

  public boolean esRecomendacionFuerte() {
    return (
      puntuacionRecomendacion != null &&
      puntuacionRecomendacion.compareTo(new BigDecimal("70")) >= 0
    );
  }

  public BigDecimal getPrecioConDescuento() {
    if (enOferta && porcentajeDescuento != null && precio != null) {
      BigDecimal descuento = precio
        .multiply(porcentajeDescuento)
        .divide(new BigDecimal("100"));
      return precio.subtract(descuento);
    }
    return precio;
  }

  @Override
  public String toString() {
    return (
      "ProductoRecomendadoDto{" +
      "productoId=" +
      productoId +
      ", nombre='" +
      nombre +
      '\'' +
      ", precio=" +
      precio +
      ", puntuacionRecomendacion=" +
      puntuacionRecomendacion +
      ", tipoRecomendacion='" +
      tipoRecomendacion +
      '\'' +
      '}'
    );
  }
}

/**
 * DTO para productos relacionados basado en patrones de compra.
 */
@Schema(
  description = "Producto frecuentemente comprado junto con items del carrito"
)
class ProductoRelacionadoDto implements Serializable {

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
