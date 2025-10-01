package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de respuesta para el resumen del carrito
 * Versión ligera con información esencial
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resumen ligero del carrito de compras")
public class ResumenCarritoResponse {

  @Schema(description = "ID del carrito", example = "1")
  private Long carritoId;

  @Schema(description = "ID del usuario", example = "123")
  private Long usuarioId;

  @Schema(description = "Cantidad total de items", example = "5")
  private Integer cantidadItems;

  @Schema(description = "Cantidad total de productos únicos", example = "3")
  private Integer cantidadProductos;

  @Schema(description = "Subtotal del carrito", example = "199.99")
  private BigDecimal subtotal;

  @Schema(description = "Total de descuentos", example = "20.00")
  private BigDecimal totalDescuentos;

  @Schema(description = "Total final", example = "179.99")
  private BigDecimal totalFinal;

  @Schema(description = "Moneda del carrito", example = "USD")
  private String moneda;

  @Schema(description = "Indica si el carrito está vacío", example = "false")
  private boolean vacio;

  @Schema(description = "Indica si hay items no disponibles", example = "false")
  private boolean tieneItemsNoDisponibles;

  @Schema(description = "Lista de nombres de productos en el carrito")
  private List<String> nombresProductos;

  @Schema(description = "Información de ahorro total")
  private AhorroInfo ahorro;

  // Constructores
  public ResumenCarritoResponse() {}

  public ResumenCarritoResponse(Long carritoId, Long usuarioId) {
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

  public Integer getCantidadItems() {
    return cantidadItems;
  }

  public void setCantidadItems(Integer cantidadItems) {
    this.cantidadItems = cantidadItems;
  }

  public Integer getCantidadProductos() {
    return cantidadProductos;
  }

  public void setCantidadProductos(Integer cantidadProductos) {
    this.cantidadProductos = cantidadProductos;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getTotalDescuentos() {
    return totalDescuentos;
  }

  public void setTotalDescuentos(BigDecimal totalDescuentos) {
    this.totalDescuentos = totalDescuentos;
  }

  public BigDecimal getTotalFinal() {
    return totalFinal;
  }

  public void setTotalFinal(BigDecimal totalFinal) {
    this.totalFinal = totalFinal;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public boolean isVacio() {
    return vacio;
  }

  public void setVacio(boolean vacio) {
    this.vacio = vacio;
  }

  public boolean isTieneItemsNoDisponibles() {
    return tieneItemsNoDisponibles;
  }

  public void setTieneItemsNoDisponibles(boolean tieneItemsNoDisponibles) {
    this.tieneItemsNoDisponibles = tieneItemsNoDisponibles;
  }

  public List<String> getNombresProductos() {
    return nombresProductos;
  }

  public void setNombresProductos(List<String> nombresProductos) {
    this.nombresProductos = nombresProductos;
  }

  public AhorroInfo getAhorro() {
    return ahorro;
  }

  public void setAhorro(AhorroInfo ahorro) {
    this.ahorro = ahorro;
  }

  // Clase interna para información de ahorro
  @Schema(description = "Información sobre el ahorro obtenido")
  public static class AhorroInfo {

    @Schema(description = "Monto total ahorrado", example = "25.00")
    private BigDecimal montoAhorrado;

    @Schema(description = "Porcentaje de ahorro", example = "12.5")
    private BigDecimal porcentajeAhorro;

    @Schema(
      description = "Descripción del ahorro",
      example = "Ahorras $25.00 con descuentos aplicados"
    )
    private String descripcion;

    // Getters y Setters
    public BigDecimal getMontoAhorrado() {
      return montoAhorrado;
    }

    public void setMontoAhorrado(BigDecimal montoAhorrado) {
      this.montoAhorrado = montoAhorrado;
    }

    public BigDecimal getPorcentajeAhorro() {
      return porcentajeAhorro;
    }

    public void setPorcentajeAhorro(BigDecimal porcentajeAhorro) {
      this.porcentajeAhorro = porcentajeAhorro;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }
  }

  @Override
  public String toString() {
    return (
      "ResumenCarritoResponse{" +
      "carritoId=" +
      carritoId +
      ", usuarioId=" +
      usuarioId +
      ", cantidadItems=" +
      cantidadItems +
      ", totalFinal=" +
      totalFinal +
      ", vacio=" +
      vacio +
      '}'
    );
  }
}
