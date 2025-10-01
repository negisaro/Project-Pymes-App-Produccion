package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.EstadoCarrito;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO completo para transferencia de datos del carrito de compras
 * Incluye validaciones, documentación y campos calculados
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Datos completos del carrito de compras")
public class CarritoDto {

  @Schema(description = "ID único del carrito", example = "1")
  private Long id;

  @NotNull(message = "El ID del usuario es obligatorio")
  @Positive(message = "El ID del usuario debe ser positivo")
  @Schema(
    description = "ID del usuario propietario del carrito",
    example = "123",
    required = true
  )
  private Long usuarioId;

  @NotNull(message = "El estado del carrito es obligatorio")
  @Schema(description = "Estado actual del carrito", required = true)
  private EstadoCarrito estado;

  @Valid
  @Schema(description = "Lista de items en el carrito")
  private List<ItemCarritoDto> items;

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
    description = "Subtotal sin descuentos ni impuestos",
    example = "99.99"
  )
  private BigDecimal subtotal;

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
  @Schema(description = "Total de descuentos aplicados", example = "10.00")
  private BigDecimal totalDescuentos;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "Los impuestos no pueden ser negativos"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "Los impuestos deben tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(description = "Total de impuestos calculados", example = "8.99")
  private BigDecimal totalImpuestos;

  @DecimalMin(
    value = "0.0",
    inclusive = true,
    message = "El total no puede ser negativo"
  )
  @Digits(
    integer = 10,
    fraction = 2,
    message = "El total debe tener máximo 10 dígitos enteros y 2 decimales"
  )
  @Schema(description = "Total final a pagar", example = "98.98")
  private BigDecimal total;

  @Min(value = 0, message = "La cantidad de items no puede ser negativa")
  @Schema(description = "Cantidad total de items en el carrito", example = "3")
  private Integer cantidadItems;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de creación del carrito",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime fechaCreacion;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de última modificación",
    example = "2024-01-15T10:35:00"
  )
  private LocalDateTime fechaModificacion;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Fecha de expiración del carrito",
    example = "2024-01-22T10:30:00"
  )
  private LocalDateTime fechaExpiracion;

  @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
  @Schema(
    description = "Notas adicionales del carrito",
    example = "Carrito para regalo"
  )
  private String notas;

  @Schema(description = "Código de cupón aplicado", example = "DESCUENTO10")
  private String codigoCupon;

  @Pattern(
    regexp = "^[A-Z]{3}$",
    message = "La moneda debe ser un código ISO de 3 letras"
  )
  @Schema(description = "Código de moneda ISO", example = "USD")
  private String moneda;

  @Schema(
    description = "Versión para control de concurrencia optimista",
    example = "1"
  )
  private Long version;

  // Constructores
  public CarritoDto() {}

  public CarritoDto(Long usuarioId, EstadoCarrito estado) {
    this.usuarioId = usuarioId;
    this.estado = estado;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public EstadoCarrito getEstado() {
    return estado;
  }

  public void setEstado(EstadoCarrito estado) {
    this.estado = estado;
  }

  public List<ItemCarritoDto> getItems() {
    return items;
  }

  public void setItems(List<ItemCarritoDto> items) {
    this.items = items;
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

  public BigDecimal getTotalImpuestos() {
    return totalImpuestos;
  }

  public void setTotalImpuestos(BigDecimal totalImpuestos) {
    this.totalImpuestos = totalImpuestos;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Integer getCantidadItems() {
    return cantidadItems;
  }

  public void setCantidadItems(Integer cantidadItems) {
    this.cantidadItems = cantidadItems;
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

  public LocalDateTime getFechaExpiracion() {
    return fechaExpiracion;
  }

  public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
    this.fechaExpiracion = fechaExpiracion;
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }

  public String getCodigoCupon() {
    return codigoCupon;
  }

  public void setCodigoCupon(String codigoCupon) {
    this.codigoCupon = codigoCupon;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  // Métodos de utilidad
  public boolean tieneItems() {
    return items != null && !items.isEmpty();
  }

  public boolean estaVacio() {
    return !tieneItems();
  }

  public boolean estaActivo() {
    return estado == EstadoCarrito.ACTIVO;
  }

  public boolean puedeModificarse() {
    return estado != null && estado.esModificable();
  }

  public boolean estaEnEstadoFinal() {
    return estado != null && estado.esFinal();
  }

  public boolean estaAbandonado() {
    return estado == EstadoCarrito.ABANDONADO;
  }

  public boolean estaProcesado() {
    return estado == EstadoCarrito.PROCESADO;
  }

  public boolean estaExpirado() {
    return estado == EstadoCarrito.EXPIRADO;
  }

  public boolean estaBloqueado() {
    return estado == EstadoCarrito.BLOQUEADO;
  }

  @Override
  public String toString() {
    return (
      "CarritoDto{" +
      "id=" +
      id +
      ", usuarioId=" +
      usuarioId +
      ", estado=" +
      estado +
      ", cantidadItems=" +
      cantidadItems +
      ", total=" +
      total +
      '}'
    );
  }
}
