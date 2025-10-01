package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para métricas avanzadas del carrito.
 * Proporciona información analítica y de business intelligence.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(description = "Métricas avanzadas y analytics del carrito")
public class MetricasCarritoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID del carrito analizado", example = "1")
  private Long carritoId;

  @Schema(description = "ID del usuario propietario", example = "123")
  private Long usuarioId;

  @Schema(description = "Valor total actual del carrito", example = "299.99")
  private BigDecimal valorTotal;

  @Schema(description = "Valor promedio por item", example = "99.99")
  private BigDecimal valorPromedioPorItem;

  @Schema(description = "Total de items únicos en el carrito", example = "3")
  private Integer totalItemsUnicos;

  @Schema(description = "Total de unidades (suma de cantidades)", example = "5")
  private Integer totalUnidades;

  @Schema(
    description = "Ahorros totales por descuentos aplicados",
    example = "30.00"
  )
  private BigDecimal ahorrosPorDescuentos;

  @Schema(description = "Porcentaje de descuento aplicado", example = "10.5")
  private BigDecimal porcentajeDescuento;

  @Schema(
    description = "Tiempo desde la creación del carrito en minutos",
    example = "45"
  )
  private Long tiempoVidaMinutos;

  @Schema(
    description = "Tiempo desde la última modificación en minutos",
    example = "15"
  )
  private Long tiempoUltimaModificacionMinutos;

  @Schema(
    description = "Probabilidad de conversión basada en patrones históricos",
    example = "75.5"
  )
  private BigDecimal probabilidadConversion;

  @Schema(
    description = "Categoria más común en el carrito",
    example = "Electrónicos"
  )
  private String categoriaPrincipal;

  @Schema(description = "Distribución de categorías en el carrito")
  private List<CategoriaDistribucionDto> distribucionCategorias;

  @Schema(
    description = "Precio promedio histórico del usuario",
    example = "150.00"
  )
  private BigDecimal precioPromedioHistorico;

  @Schema(
    description = "Comparación con el precio promedio histórico",
    example = "SUPERIOR"
  )
  private String comparacionHistorica; // SUPERIOR, INFERIOR, SIMILAR

  @Schema(description = "Items recomendados para aumentar el valor del carrito")
  private List<ProductoRecomendadoDto> itemsRecomendados;

  @Schema(description = "Productos frecuentemente comprados juntos")
  private List<ProductoRelacionadoDto> productosRelacionados;

  @Schema(description = "Riesgo de abandono del carrito", example = "MEDIO")
  private String riesgoAbandono; // BAJO, MEDIO, ALTO

  @Schema(description = "Factores que influyen en el riesgo de abandono")
  private List<String> factoresRiesgo;

  @Schema(description = "Sugerencias para mejorar la conversión")
  private List<String> sugerenciasConversion;

  // Nested DTOs
  @Schema(description = "Distribución de productos por categoría")
  public static class CategoriaDistribucionDto implements Serializable {

    private String categoria;
    private Integer cantidad;
    private BigDecimal valor;
    private BigDecimal porcentaje;

    // Constructores, getters y setters
    public CategoriaDistribucionDto() {}

    public CategoriaDistribucionDto(
      String categoria,
      Integer cantidad,
      BigDecimal valor,
      BigDecimal porcentaje
    ) {
      this.categoria = categoria;
      this.cantidad = cantidad;
      this.valor = valor;
      this.porcentaje = porcentaje;
    }

    public String getCategoria() {
      return categoria;
    }

    public void setCategoria(String categoria) {
      this.categoria = categoria;
    }

    public Integer getCantidad() {
      return cantidad;
    }

    public void setCantidad(Integer cantidad) {
      this.cantidad = cantidad;
    }

    public BigDecimal getValor() {
      return valor;
    }

    public void setValor(BigDecimal valor) {
      this.valor = valor;
    }

    public BigDecimal getPorcentaje() {
      return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
      this.porcentaje = porcentaje;
    }
  }

  // Constructores
  public MetricasCarritoDto() {}

  public MetricasCarritoDto(Long carritoId, Long usuarioId) {
    this.carritoId = carritoId;
    this.usuarioId = usuarioId;
  }

  // Getters y Setters
  public Long getCarritoId() {
    return carritoId;
  }

  public void setCarritoId(Long carritoId) {
    this.carritoId = carritoId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public BigDecimal getValorPromedioPorItem() {
    return valorPromedioPorItem;
  }

  public void setValorPromedioPorItem(BigDecimal valorPromedioPorItem) {
    this.valorPromedioPorItem = valorPromedioPorItem;
  }

  public Integer getTotalItemsUnicos() {
    return totalItemsUnicos;
  }

  public void setTotalItemsUnicos(Integer totalItemsUnicos) {
    this.totalItemsUnicos = totalItemsUnicos;
  }

  public Integer getTotalUnidades() {
    return totalUnidades;
  }

  public void setTotalUnidades(Integer totalUnidades) {
    this.totalUnidades = totalUnidades;
  }

  public BigDecimal getAhorrosPorDescuentos() {
    return ahorrosPorDescuentos;
  }

  public void setAhorrosPorDescuentos(BigDecimal ahorrosPorDescuentos) {
    this.ahorrosPorDescuentos = ahorrosPorDescuentos;
  }

  public BigDecimal getPorcentajeDescuento() {
    return porcentajeDescuento;
  }

  public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
    this.porcentajeDescuento = porcentajeDescuento;
  }

  public Long getTiempoVidaMinutos() {
    return tiempoVidaMinutos;
  }

  public void setTiempoVidaMinutos(Long tiempoVidaMinutos) {
    this.tiempoVidaMinutos = tiempoVidaMinutos;
  }

  public Long getTiempoUltimaModificacionMinutos() {
    return tiempoUltimaModificacionMinutos;
  }

  public void setTiempoUltimaModificacionMinutos(
    Long tiempoUltimaModificacionMinutos
  ) {
    this.tiempoUltimaModificacionMinutos = tiempoUltimaModificacionMinutos;
  }

  public BigDecimal getProbabilidadConversion() {
    return probabilidadConversion;
  }

  public void setProbabilidadConversion(BigDecimal probabilidadConversion) {
    this.probabilidadConversion = probabilidadConversion;
  }

  public String getCategoriaPrincipal() {
    return categoriaPrincipal;
  }

  public void setCategoriaPrincipal(String categoriaPrincipal) {
    this.categoriaPrincipal = categoriaPrincipal;
  }

  public List<CategoriaDistribucionDto> getDistribucionCategorias() {
    return distribucionCategorias;
  }

  public void setDistribucionCategorias(
    List<CategoriaDistribucionDto> distribucionCategorias
  ) {
    this.distribucionCategorias = distribucionCategorias;
  }

  public BigDecimal getPrecioPromedioHistorico() {
    return precioPromedioHistorico;
  }

  public void setPrecioPromedioHistorico(BigDecimal precioPromedioHistorico) {
    this.precioPromedioHistorico = precioPromedioHistorico;
  }

  public String getComparacionHistorica() {
    return comparacionHistorica;
  }

  public void setComparacionHistorica(String comparacionHistorica) {
    this.comparacionHistorica = comparacionHistorica;
  }

  public List<ProductoRecomendadoDto> getItemsRecomendados() {
    return itemsRecomendados;
  }

  public void setItemsRecomendados(
    List<ProductoRecomendadoDto> itemsRecomendados
  ) {
    this.itemsRecomendados = itemsRecomendados;
  }

  public List<ProductoRelacionadoDto> getProductosRelacionados() {
    return productosRelacionados;
  }

  public void setProductosRelacionados(
    List<ProductoRelacionadoDto> productosRelacionados
  ) {
    this.productosRelacionados = productosRelacionados;
  }

  public String getRiesgoAbandono() {
    return riesgoAbandono;
  }

  public void setRiesgoAbandono(String riesgoAbandono) {
    this.riesgoAbandono = riesgoAbandono;
  }

  public List<String> getFactoresRiesgo() {
    return factoresRiesgo;
  }

  public void setFactoresRiesgo(List<String> factoresRiesgo) {
    this.factoresRiesgo = factoresRiesgo;
  }

  public List<String> getSugerenciasConversion() {
    return sugerenciasConversion;
  }

  public void setSugerenciasConversion(List<String> sugerenciasConversion) {
    this.sugerenciasConversion = sugerenciasConversion;
  }

  // Métodos de utilidad
  public boolean esCarritoAltoValor() {
    return (
      valorTotal != null && valorTotal.compareTo(new BigDecimal("500")) > 0
    );
  }

  public boolean tieneRiesgoAltoAbandono() {
    return "ALTO".equals(riesgoAbandono);
  }

  public boolean esCandidatoDescuento() {
    return (
      tiempoVidaMinutos != null &&
      tiempoVidaMinutos > 30 &&
      probabilidadConversion != null &&
      probabilidadConversion.compareTo(new BigDecimal("60")) < 0
    );
  }

  @Override
  public String toString() {
    return (
      "MetricasCarritoDto{" +
      "carritoId=" +
      carritoId +
      ", valorTotal=" +
      valorTotal +
      ", totalItemsUnicos=" +
      totalItemsUnicos +
      ", probabilidadConversion=" +
      probabilidadConversion +
      ", riesgoAbandono='" +
      riesgoAbandono +
      '\'' +
      '}'
    );
  }
}
