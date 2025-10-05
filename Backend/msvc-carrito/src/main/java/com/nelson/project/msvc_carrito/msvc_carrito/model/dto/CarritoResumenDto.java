package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resumen de carritos en listados históricos.
 * Optimizado para consultas de listado con información esencial.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(description = "Resumen de carrito para listados históricos")
public class CarritoResumenDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ID único del carrito", example = "1")
  private Long id;

  @Schema(description = "Estado del carrito", example = "PROCESADO")
  private String estado;

  @Schema(description = "Fecha de creación del carrito")
  private LocalDateTime fechaCreacion;

  @Schema(description = "Fecha de última modificación")
  private LocalDateTime fechaModificacion;

  @Schema(description = "Fecha de procesamiento (si aplica)")
  private LocalDateTime fechaProcesamiento;

  @Schema(description = "Total de items únicos", example = "3")
  private Integer totalItems;

  @Schema(description = "Total de unidades", example = "5")
  private Integer totalUnidades;

  @Schema(description = "Valor total del carrito", example = "299.99")
  private BigDecimal valorTotal;

  @Schema(description = "Descuentos aplicados", example = "30.00")
  private BigDecimal descuentos;

  @Schema(
    description = "Código de descuento utilizado",
    example = "DESCUENTO20"
  )
  private String codigoDescuento;

  @Schema(
    description = "ID del pedido generado (si fue procesado)",
    example = "456"
  )
  private Long pedidoId;

  // Constructores
  public CarritoResumenDto() {}

  public CarritoResumenDto(
    Long id,
    String estado,
    LocalDateTime fechaCreacion,
    BigDecimal valorTotal
  ) {
    this.id = id;
    this.estado = estado;
    this.fechaCreacion = fechaCreacion;
    this.valorTotal = valorTotal;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  public LocalDateTime getFechaProcesamiento() {
    return fechaProcesamiento;
  }

  public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
    this.fechaProcesamiento = fechaProcesamiento;
  }

  public Integer getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems;
  }

  public Integer getTotalUnidades() {
    return totalUnidades;
  }

  public void setTotalUnidades(Integer totalUnidades) {
    this.totalUnidades = totalUnidades;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public BigDecimal getDescuentos() {
    return descuentos;
  }

  public void setDescuentos(BigDecimal descuentos) {
    this.descuentos = descuentos;
  }

  public String getCodigoDescuento() {
    return codigoDescuento;
  }

  public void setCodigoDescuento(String codigoDescuento) {
    this.codigoDescuento = codigoDescuento;
  }

  public Long getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(Long pedidoId) {
    this.pedidoId = pedidoId;
  }

  // Métodos de utilidad
  public boolean fueProcesado() {
    return "PROCESADO".equals(estado) && pedidoId != null;
  }

  public boolean fueAbandonado() {
    return "ABANDONADO".equals(estado);
  }

  public boolean tuvoDescuento() {
    return descuentos != null && descuentos.compareTo(BigDecimal.ZERO) > 0;
  }

  @Override
  public String toString() {
    return (
      "CarritoResumenDto{" +
      "id=" +
      id +
      ", estado='" +
      estado +
      '\'' +
      ", valorTotal=" +
      valorTotal +
      ", totalItems=" +
      totalItems +
      '}'
    );
  }
}
