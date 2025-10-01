package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para validación de descuentos en el carrito.
 * Proporciona información detallada sobre la aplicabilidad de códigos de descuento.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(description = "Resultado de validación de código de descuento")
public class ValidacionDescuentoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(
    description = "Indica si el descuento es válido y aplicable",
    example = "true"
  )
  private boolean valido;

  @Schema(description = "Código de descuento validado", example = "DESCUENTO20")
  private String codigo;

  @Schema(description = "Tipo de descuento", example = "PORCENTAJE")
  private String tipoDescuento;

  @Schema(description = "Valor del descuento", example = "20.00")
  private BigDecimal valorDescuento;

  @Schema(
    description = "Monto mínimo requerido para aplicar el descuento",
    example = "100.00"
  )
  private BigDecimal montoMinimo;

  @Schema(
    description = "Monto del descuento que se aplicaría",
    example = "15.50"
  )
  private BigDecimal montoDescuentoCalculado;

  @Schema(description = "Fecha de expiración del descuento")
  private LocalDateTime fechaExpiracion;

  @Schema(description = "Número de usos restantes del descuento", example = "5")
  private Integer usosRestantes;

  @Schema(
    description = "Indica si el descuento es de uso único por usuario",
    example = "true"
  )
  private boolean usoUnicoPorUsuario;

  @Schema(
    description = "Indica si el usuario ya utilizó este descuento",
    example = "false"
  )
  private boolean yaUtilizadoPorUsuario;

  @Schema(description = "Lista de mensajes de error o advertencias")
  private List<String> mensajes;

  @Schema(
    description = "Mensaje principal describiendo el resultado",
    example = "Descuento válido y aplicable"
  )
  private String mensajePrincipal;

  @Schema(description = "Productos específicos elegibles para el descuento")
  private List<Long> productosElegibles;

  @Schema(description = "Categorías elegibles para el descuento")
  private List<String> categoriasElegibles;

  // Constructores
  public ValidacionDescuentoDto() {}

  public ValidacionDescuentoDto(boolean valido, String mensaje) {
    this.valido = valido;
    this.mensajePrincipal = mensaje;
  }

  public static ValidacionDescuentoDto invalido(String mensaje) {
    return new ValidacionDescuentoDto(false, mensaje);
  }

  public static ValidacionDescuentoDto valido(String codigo, String mensaje) {
    ValidacionDescuentoDto dto = new ValidacionDescuentoDto(true, mensaje);
    dto.setCodigo(codigo);
    return dto;
  }

  // Getters y Setters
  public boolean isValido() {
    return valido;
  }

  public void setValido(boolean valido) {
    this.valido = valido;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTipoDescuento() {
    return tipoDescuento;
  }

  public void setTipoDescuento(String tipoDescuento) {
    this.tipoDescuento = tipoDescuento;
  }

  public BigDecimal getValorDescuento() {
    return valorDescuento;
  }

  public void setValorDescuento(BigDecimal valorDescuento) {
    this.valorDescuento = valorDescuento;
  }

  public BigDecimal getMontoMinimo() {
    return montoMinimo;
  }

  public void setMontoMinimo(BigDecimal montoMinimo) {
    this.montoMinimo = montoMinimo;
  }

  public BigDecimal getMontoDescuentoCalculado() {
    return montoDescuentoCalculado;
  }

  public void setMontoDescuentoCalculado(BigDecimal montoDescuentoCalculado) {
    this.montoDescuentoCalculado = montoDescuentoCalculado;
  }

  public LocalDateTime getFechaExpiracion() {
    return fechaExpiracion;
  }

  public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
    this.fechaExpiracion = fechaExpiracion;
  }

  public Integer getUsosRestantes() {
    return usosRestantes;
  }

  public void setUsosRestantes(Integer usosRestantes) {
    this.usosRestantes = usosRestantes;
  }

  public boolean isUsoUnicoPorUsuario() {
    return usoUnicoPorUsuario;
  }

  public void setUsoUnicoPorUsuario(boolean usoUnicoPorUsuario) {
    this.usoUnicoPorUsuario = usoUnicoPorUsuario;
  }

  public boolean isYaUtilizadoPorUsuario() {
    return yaUtilizadoPorUsuario;
  }

  public void setYaUtilizadoPorUsuario(boolean yaUtilizadoPorUsuario) {
    this.yaUtilizadoPorUsuario = yaUtilizadoPorUsuario;
  }

  public List<String> getMensajes() {
    return mensajes;
  }

  public void setMensajes(List<String> mensajes) {
    this.mensajes = mensajes;
  }

  public String getMensajePrincipal() {
    return mensajePrincipal;
  }

  public void setMensajePrincipal(String mensajePrincipal) {
    this.mensajePrincipal = mensajePrincipal;
  }

  public List<Long> getProductosElegibles() {
    return productosElegibles;
  }

  public void setProductosElegibles(List<Long> productosElegibles) {
    this.productosElegibles = productosElegibles;
  }

  public List<String> getCategoriasElegibles() {
    return categoriasElegibles;
  }

  public void setCategoriasElegibles(List<String> categoriasElegibles) {
    this.categoriasElegibles = categoriasElegibles;
  }

  // Métodos de utilidad
  public boolean esExpirado() {
    return (
      fechaExpiracion != null && fechaExpiracion.isBefore(LocalDateTime.now())
    );
  }

  public boolean tieneUsosRestantes() {
    return usosRestantes == null || usosRestantes > 0;
  }

  public boolean esAplicableAProducto(Long productoId) {
    return (
      productosElegibles == null ||
      productosElegibles.isEmpty() ||
      productosElegibles.contains(productoId)
    );
  }

  @Override
  public String toString() {
    return (
      "ValidacionDescuentoDto{" +
      "valido=" +
      valido +
      ", codigo='" +
      codigo +
      '\'' +
      ", tipoDescuento='" +
      tipoDescuento +
      '\'' +
      ", montoDescuentoCalculado=" +
      montoDescuentoCalculado +
      ", mensajePrincipal='" +
      mensajePrincipal +
      '\'' +
      '}'
    );
  }
}
