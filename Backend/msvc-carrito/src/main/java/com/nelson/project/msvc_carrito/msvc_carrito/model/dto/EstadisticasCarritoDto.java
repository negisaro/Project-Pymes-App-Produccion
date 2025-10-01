package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para estadísticas detalladas del carrito en períodos específicos.
 * Utilizado para business intelligence y analytics avanzados.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(
  description = "Estadísticas avanzadas del carrito para business intelligence"
)
public class EstadisticasCarritoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID del usuario analizado")
  private Long usuarioId;

  @Schema(description = "Período de análisis - fecha inicio")
  private LocalDateTime fechaInicio;

  @Schema(description = "Período de análisis - fecha fin")
  private LocalDateTime fechaFin;

  @Schema(description = "Número total de carritos creados")
  private Integer totalCarritos;

  @Schema(description = "Número de carritos procesados")
  private Integer carritosCompletados;

  @Schema(description = "Número de carritos abandonados")
  private Integer carritosAbandonados;

  @Schema(description = "Tasa de conversión (0-100)")
  private BigDecimal tasaConversion;

  @Schema(description = "Valor total de compras completadas")
  private BigDecimal valorTotalCompras;

  @Schema(description = "Valor promedio por carrito")
  private BigDecimal valorPromedioCarrito;

  @Schema(description = "Tiempo promedio hasta completar compra (minutos)")
  private Long tiempoPromedioCompra;

  @Schema(description = "Tiempo promedio hasta abandono (minutos)")
  private Long tiempoPromedioAbandono;

  @Schema(description = "Total de descuentos utilizados")
  private BigDecimal totalDescuentos;

  @Schema(description = "Producto más agregado al carrito")
  private String productoMasAgregado;

  @Schema(description = "Categoría más popular")
  private String categoriaMasPopular;

  @Schema(description = "Número total de items agregados")
  private Integer totalItemsAgregados;

  @Schema(description = "Número total de items removidos")
  private Integer totalItemsRemovidos;

  @Schema(description = "Valor total de carritos abandonados")
  private BigDecimal valorCarritosAbandonados;

  // Constructores
  public EstadisticasCarritoDto() {}

  public EstadisticasCarritoDto(
    Long usuarioId,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  ) {
    this.usuarioId = usuarioId;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
  }

  // Getters y Setters
  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public LocalDateTime getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDateTime fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public LocalDateTime getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDateTime fechaFin) {
    this.fechaFin = fechaFin;
  }

  public Integer getTotalCarritos() {
    return totalCarritos;
  }

  public void setTotalCarritos(Integer totalCarritos) {
    this.totalCarritos = totalCarritos;
  }

  public Integer getCarritosCompletados() {
    return carritosCompletados;
  }

  public void setCarritosCompletados(Integer carritosCompletados) {
    this.carritosCompletados = carritosCompletados;
  }

  public Integer getCarritosAbandonados() {
    return carritosAbandonados;
  }

  public void setCarritosAbandonados(Integer carritosAbandonados) {
    this.carritosAbandonados = carritosAbandonados;
  }

  public BigDecimal getTasaConversion() {
    return tasaConversion;
  }

  public void setTasaConversion(BigDecimal tasaConversion) {
    this.tasaConversion = tasaConversion;
  }

  public BigDecimal getValorTotalCompras() {
    return valorTotalCompras;
  }

  public void setValorTotalCompras(BigDecimal valorTotalCompras) {
    this.valorTotalCompras = valorTotalCompras;
  }

  public BigDecimal getValorPromedioCarrito() {
    return valorPromedioCarrito;
  }

  public void setValorPromedioCarrito(BigDecimal valorPromedioCarrito) {
    this.valorPromedioCarrito = valorPromedioCarrito;
  }

  public Long getTiempoPromedioCompra() {
    return tiempoPromedioCompra;
  }

  public void setTiempoPromedioCompra(Long tiempoPromedioCompra) {
    this.tiempoPromedioCompra = tiempoPromedioCompra;
  }

  public Long getTiempoPromedioAbandono() {
    return tiempoPromedioAbandono;
  }

  public void setTiempoPromedioAbandono(Long tiempoPromedioAbandono) {
    this.tiempoPromedioAbandono = tiempoPromedioAbandono;
  }

  public BigDecimal getTotalDescuentos() {
    return totalDescuentos;
  }

  public void setTotalDescuentos(BigDecimal totalDescuentos) {
    this.totalDescuentos = totalDescuentos;
  }

  public String getProductoMasAgregado() {
    return productoMasAgregado;
  }

  public void setProductoMasAgregado(String productoMasAgregado) {
    this.productoMasAgregado = productoMasAgregado;
  }

  public String getCategoriaMasPopular() {
    return categoriaMasPopular;
  }

  public void setCategoriaMasPopular(String categoriaMasPopular) {
    this.categoriaMasPopular = categoriaMasPopular;
  }

  public Integer getTotalItemsAgregados() {
    return totalItemsAgregados;
  }

  public void setTotalItemsAgregados(Integer totalItemsAgregados) {
    this.totalItemsAgregados = totalItemsAgregados;
  }

  public Integer getTotalItemsRemovidos() {
    return totalItemsRemovidos;
  }

  public void setTotalItemsRemovidos(Integer totalItemsRemovidos) {
    this.totalItemsRemovidos = totalItemsRemovidos;
  }

  public BigDecimal getValorCarritosAbandonados() {
    return valorCarritosAbandonados;
  }

  public void setValorCarritosAbandonados(BigDecimal valorCarritosAbandonados) {
    this.valorCarritosAbandonados = valorCarritosAbandonados;
  }

  // Métodos de utilidad
  public boolean tieneBuenaTasaConversion() {
    return (
      tasaConversion != null &&
      tasaConversion.compareTo(new BigDecimal("70")) >= 0
    );
  }

  public boolean tieneAltaActividadAbandono() {
    return (
      carritosAbandonados != null &&
      totalCarritos != null &&
      (carritosAbandonados * 100) / totalCarritos > 50
    );
  }

  public BigDecimal calcularValorPerdidoPorAbandono() {
    return valorCarritosAbandonados != null
      ? valorCarritosAbandonados
      : BigDecimal.ZERO;
  }

  @Override
  public String toString() {
    return (
      "EstadisticasCarritoDto{" +
      "usuarioId=" +
      usuarioId +
      ", totalCarritos=" +
      totalCarritos +
      ", tasaConversion=" +
      tasaConversion +
      ", valorTotalCompras=" +
      valorTotalCompras +
      '}'
    );
  }
}
