package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para validación completa del carrito.
 * Proporciona información detallada sobre inconsistencias y validaciones.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
@Schema(description = "Resultado de validación completa del carrito")
public class ValidacionCarritoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(
    description = "Indica si el carrito es válido en general",
    example = "true"
  )
  private boolean carritoValido;

  @Schema(description = "Puntuación de validación (0-100)", example = "95.5")
  private BigDecimal puntuacionValidacion;

  @Schema(description = "Lista de errores críticos encontrados")
  private List<ErrorValidacionDto> erroresCriticos;

  @Schema(description = "Lista de advertencias encontradas")
  private List<ErrorValidacionDto> advertencias;

  @Schema(description = "Lista de sugerencias de mejora")
  private List<String> sugerencias;

  @Schema(description = "Items con problemas de validación")
  private List<ItemValidacionDto> itemsConProblemas;

  @Schema(
    description = "Indica si hay productos con precios desactualizados",
    example = "false"
  )
  private boolean preciosDesactualizados;

  @Schema(
    description = "Indica si hay productos sin stock suficiente",
    example = "false"
  )
  private boolean problemasStock;

  @Schema(
    description = "Indica si hay descuentos inválidos o expirados",
    example = "false"
  )
  private boolean descuentosInvalidos;

  @Schema(description = "Número total de items validados", example = "5")
  private Integer totalItemsValidados;

  @Schema(description = "Número de items con problemas", example = "0")
  private Integer itemsConProblemasCount;

  @Schema(description = "Fecha y hora de la validación")
  private LocalDateTime fechaValidacion;

  // Nested DTOs
  @Schema(description = "Error de validación específico")
  public static class ErrorValidacionDto implements Serializable {

    private String codigo;
    private String mensaje;
    private String severidad; // CRITICO, ADVERTENCIA, INFO
    private String componente; // ITEM, DESCUENTO, TOTAL, GENERAL
    private Long itemId;

    public ErrorValidacionDto() {}

    public ErrorValidacionDto(String codigo, String mensaje, String severidad) {
      this.codigo = codigo;
      this.mensaje = mensaje;
      this.severidad = severidad;
    }

    // Getters y setters
    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    public String getMensaje() {
      return mensaje;
    }

    public void setMensaje(String mensaje) {
      this.mensaje = mensaje;
    }

    public String getSeveridad() {
      return severidad;
    }

    public void setSeveridad(String severidad) {
      this.severidad = severidad;
    }

    public String getComponente() {
      return componente;
    }

    public void setComponente(String componente) {
      this.componente = componente;
    }

    public Long getItemId() {
      return itemId;
    }

    public void setItemId(Long itemId) {
      this.itemId = itemId;
    }
  }

  @Schema(description = "Validación específica de un item del carrito")
  public static class ItemValidacionDto implements Serializable {

    private Long productoId;
    private String nombreProducto;
    private boolean stockSuficiente;
    private boolean precioActualizado;
    private boolean productoActivo;
    private BigDecimal precioOriginal;
    private BigDecimal precioActual;
    private Integer stockDisponible;
    private Integer cantidadSolicitada;
    private List<String> problemas;

    // Constructores, getters y setters
    public ItemValidacionDto() {}

    public Long getProductoId() {
      return productoId;
    }

    public void setProductoId(Long productoId) {
      this.productoId = productoId;
    }

    public String getNombreProducto() {
      return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
      this.nombreProducto = nombreProducto;
    }

    public boolean isStockSuficiente() {
      return stockSuficiente;
    }

    public void setStockSuficiente(boolean stockSuficiente) {
      this.stockSuficiente = stockSuficiente;
    }

    public boolean isPrecioActualizado() {
      return precioActualizado;
    }

    public void setPrecioActualizado(boolean precioActualizado) {
      this.precioActualizado = precioActualizado;
    }

    public boolean isProductoActivo() {
      return productoActivo;
    }

    public void setProductoActivo(boolean productoActivo) {
      this.productoActivo = productoActivo;
    }

    public BigDecimal getPrecioOriginal() {
      return precioOriginal;
    }

    public void setPrecioOriginal(BigDecimal precioOriginal) {
      this.precioOriginal = precioOriginal;
    }

    public BigDecimal getPrecioActual() {
      return precioActual;
    }

    public void setPrecioActual(BigDecimal precioActual) {
      this.precioActual = precioActual;
    }

    public Integer getStockDisponible() {
      return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
      this.stockDisponible = stockDisponible;
    }

    public Integer getCantidadSolicitada() {
      return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
      this.cantidadSolicitada = cantidadSolicitada;
    }

    public List<String> getProblemas() {
      return problemas;
    }

    public void setProblemas(List<String> problemas) {
      this.problemas = problemas;
    }
  }

  // Constructores
  public ValidacionCarritoDto() {
    this.fechaValidacion = LocalDateTime.now();
  }

  public static ValidacionCarritoDto carritoValido() {
    ValidacionCarritoDto dto = new ValidacionCarritoDto();
    dto.setCarritoValido(true);
    dto.setPuntuacionValidacion(new BigDecimal("100"));
    return dto;
  }

  public static ValidacionCarritoDto carritoInvalido(String mensaje) {
    ValidacionCarritoDto dto = new ValidacionCarritoDto();
    dto.setCarritoValido(false);
    dto.setPuntuacionValidacion(BigDecimal.ZERO);
    return dto;
  }

  // Getters y Setters
  public boolean isCarritoValido() {
    return carritoValido;
  }

  public void setCarritoValido(boolean carritoValido) {
    this.carritoValido = carritoValido;
  }

  public BigDecimal getPuntuacionValidacion() {
    return puntuacionValidacion;
  }

  public void setPuntuacionValidacion(BigDecimal puntuacionValidacion) {
    this.puntuacionValidacion = puntuacionValidacion;
  }

  public List<ErrorValidacionDto> getErroresCriticos() {
    return erroresCriticos;
  }

  public void setErroresCriticos(List<ErrorValidacionDto> erroresCriticos) {
    this.erroresCriticos = erroresCriticos;
  }

  public List<ErrorValidacionDto> getAdvertencias() {
    return advertencias;
  }

  public void setAdvertencias(List<ErrorValidacionDto> advertencias) {
    this.advertencias = advertencias;
  }

  public List<String> getSugerencias() {
    return sugerencias;
  }

  public void setSugerencias(List<String> sugerencias) {
    this.sugerencias = sugerencias;
  }

  public List<ItemValidacionDto> getItemsConProblemas() {
    return itemsConProblemas;
  }

  public void setItemsConProblemas(List<ItemValidacionDto> itemsConProblemas) {
    this.itemsConProblemas = itemsConProblemas;
  }

  public boolean isPreciosDesactualizados() {
    return preciosDesactualizados;
  }

  public void setPreciosDesactualizados(boolean preciosDesactualizados) {
    this.preciosDesactualizados = preciosDesactualizados;
  }

  public boolean isProblemasStock() {
    return problemasStock;
  }

  public void setProblemasStock(boolean problemasStock) {
    this.problemasStock = problemasStock;
  }

  public boolean isDescuentosInvalidos() {
    return descuentosInvalidos;
  }

  public void setDescuentosInvalidos(boolean descuentosInvalidos) {
    this.descuentosInvalidos = descuentosInvalidos;
  }

  public Integer getTotalItemsValidados() {
    return totalItemsValidados;
  }

  public void setTotalItemsValidados(Integer totalItemsValidados) {
    this.totalItemsValidados = totalItemsValidados;
  }

  public Integer getItemsConProblemasCount() {
    return itemsConProblemasCount;
  }

  public void setItemsConProblemasCount(Integer itemsConProblemasCount) {
    this.itemsConProblemasCount = itemsConProblemasCount;
  }

  public LocalDateTime getFechaValidacion() {
    return fechaValidacion;
  }

  public void setFechaValidacion(LocalDateTime fechaValidacion) {
    this.fechaValidacion = fechaValidacion;
  }

  // Métodos de utilidad
  public boolean tieneErroresCriticos() {
    return erroresCriticos != null && !erroresCriticos.isEmpty();
  }

  public boolean requiereActualizacion() {
    return preciosDesactualizados || problemasStock || descuentosInvalidos;
  }

  public boolean esValidacionExitosa() {
    return carritoValido && !tieneErroresCriticos();
  }

  @Override
  public String toString() {
    return (
      "ValidacionCarritoDto{" +
      "carritoValido=" +
      carritoValido +
      ", puntuacionValidacion=" +
      puntuacionValidacion +
      ", erroresCriticos=" +
      (erroresCriticos != null ? erroresCriticos.size() : 0) +
      ", advertencias=" +
      (advertencias != null ? advertencias.size() : 0) +
      '}'
    );
  }
}
