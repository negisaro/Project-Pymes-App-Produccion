package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de respuesta para operaciones del carrito con información adicional
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
  description = "Respuesta completa del carrito con información de cálculos y validaciones"
)
public class CarritoResponse {

  @Schema(description = "Información completa del carrito")
  private CarritoDto carrito;

  @Schema(description = "Cálculos detallados del carrito")
  private CalculosCarrito calculos;

  @Schema(description = "Información de validaciones y disponibilidad")
  private ValidacionCarrito validacion;

  @Schema(description = "Recomendaciones de productos relacionados")
  private List<RecomendacionProducto> recomendaciones;

  @Schema(description = "Información de descuentos disponibles")
  private List<DescuentoDisponible> descuentosDisponibles;

  // Constructores
  public CarritoResponse() {}

  public CarritoResponse(CarritoDto carrito) {
    this.carrito = carrito;
  }

  // Getters y Setters
  public CarritoDto getCarrito() {
    return carrito;
  }

  public void setCarrito(CarritoDto carrito) {
    this.carrito = carrito;
  }

  public CalculosCarrito getCalculos() {
    return calculos;
  }

  public void setCalculos(CalculosCarrito calculos) {
    this.calculos = calculos;
  }

  public ValidacionCarrito getValidacion() {
    return validacion;
  }

  public void setValidacion(ValidacionCarrito validacion) {
    this.validacion = validacion;
  }

  public List<RecomendacionProducto> getRecomendaciones() {
    return recomendaciones;
  }

  public void setRecomendaciones(List<RecomendacionProducto> recomendaciones) {
    this.recomendaciones = recomendaciones;
  }

  public List<DescuentoDisponible> getDescuentosDisponibles() {
    return descuentosDisponibles;
  }

  public void setDescuentosDisponibles(
    List<DescuentoDisponible> descuentosDisponibles
  ) {
    this.descuentosDisponibles = descuentosDisponibles;
  }

  // Clases internas para información adicional
  @Schema(description = "Cálculos detallados del carrito")
  public static class CalculosCarrito {

    @Schema(description = "Subtotal antes de descuentos", example = "199.99")
    private BigDecimal subtotalBruto;

    @Schema(description = "Total de descuentos aplicados", example = "20.00")
    private BigDecimal totalDescuentos;

    @Schema(description = "Subtotal después de descuentos", example = "179.99")
    private BigDecimal subtotalNeto;

    @Schema(description = "Total de impuestos", example = "18.00")
    private BigDecimal totalImpuestos;

    @Schema(description = "Costo de envío estimado", example = "5.99")
    private BigDecimal costoEnvio;

    @Schema(description = "Total final", example = "203.98")
    private BigDecimal totalFinal;

    @Schema(description = "Peso total del carrito en kg", example = "2.5")
    private BigDecimal pesoTotal;

    // Getters y Setters
    public BigDecimal getSubtotalBruto() {
      return subtotalBruto;
    }

    public void setSubtotalBruto(BigDecimal subtotalBruto) {
      this.subtotalBruto = subtotalBruto;
    }

    public BigDecimal getTotalDescuentos() {
      return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
      this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getSubtotalNeto() {
      return subtotalNeto;
    }

    public void setSubtotalNeto(BigDecimal subtotalNeto) {
      this.subtotalNeto = subtotalNeto;
    }

    public BigDecimal getTotalImpuestos() {
      return totalImpuestos;
    }

    public void setTotalImpuestos(BigDecimal totalImpuestos) {
      this.totalImpuestos = totalImpuestos;
    }

    public BigDecimal getCostoEnvio() {
      return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
      this.costoEnvio = costoEnvio;
    }

    public BigDecimal getTotalFinal() {
      return totalFinal;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
      this.totalFinal = totalFinal;
    }

    public BigDecimal getPesoTotal() {
      return pesoTotal;
    }

    public void setPesoTotal(BigDecimal pesoTotal) {
      this.pesoTotal = pesoTotal;
    }
  }

  @Schema(description = "Información de validación del carrito")
  public static class ValidacionCarrito {

    @Schema(
      description = "Indica si todos los productos están disponibles",
      example = "true"
    )
    private boolean todosDisponibles;

    @Schema(
      description = "Indica si hay suficiente stock para todos los items",
      example = "true"
    )
    private boolean suficienteStock;

    @Schema(description = "Lista de items con problemas de stock")
    private List<String> itemsConProblemas;

    @Schema(description = "Mensajes de advertencia")
    private List<String> advertencias;

    // Getters y Setters
    public boolean isTodosDisponibles() {
      return todosDisponibles;
    }

    public void setTodosDisponibles(boolean todosDisponibles) {
      this.todosDisponibles = todosDisponibles;
    }

    public boolean isSuficienteStock() {
      return suficienteStock;
    }

    public void setSuficienteStock(boolean suficienteStock) {
      this.suficienteStock = suficienteStock;
    }

    public List<String> getItemsConProblemas() {
      return itemsConProblemas;
    }

    public void setItemsConProblemas(List<String> itemsConProblemas) {
      this.itemsConProblemas = itemsConProblemas;
    }

    public List<String> getAdvertencias() {
      return advertencias;
    }

    public void setAdvertencias(List<String> advertencias) {
      this.advertencias = advertencias;
    }
  }

  @Schema(description = "Recomendación de producto")
  public static class RecomendacionProducto {

    @Schema(description = "ID del producto recomendado", example = "789")
    private Long productoId;

    @Schema(
      description = "Nombre del producto",
      example = "Producto complementario"
    )
    private String nombre;

    @Schema(description = "Precio del producto", example = "29.99")
    private BigDecimal precio;

    @Schema(
      description = "Motivo de la recomendación",
      example = "Frecuentemente comprado junto"
    )
    private String motivo;

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

    public String getMotivo() {
      return motivo;
    }

    public void setMotivo(String motivo) {
      this.motivo = motivo;
    }
  }

  @Schema(description = "Descuento disponible")
  public static class DescuentoDisponible {

    @Schema(description = "Código del descuento", example = "PRIMERAVEZ")
    private String codigo;

    @Schema(
      description = "Descripción del descuento",
      example = "10% de descuento para nuevos clientes"
    )
    private String descripcion;

    @Schema(description = "Porcentaje de descuento", example = "10")
    private BigDecimal porcentaje;

    @Schema(description = "Monto mínimo requerido", example = "50.00")
    private BigDecimal montoMinimo;

    // Getters y Setters
    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public BigDecimal getPorcentaje() {
      return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
      this.porcentaje = porcentaje;
    }

    public BigDecimal getMontoMinimo() {
      return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
      this.montoMinimo = montoMinimo;
    }
  }
}
