package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.CarritoHistorial;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.DescuentoAplicado;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper para entidades de auditoría y descuentos
 * Facilita la conversión entre entidades y DTOs de historial
 */
@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  imports = { LocalDateTime.class }
)
public interface AuditoriaMapper {
  /**
   * DTO simplificado para historial del carrito
   */
  @Mapping(source = "fechaOperacion", target = "fecha")
  @Mapping(source = "tipoOperacion", target = "tipo")
  @Mapping(source = "datosOperacion", target = "datos")
  CarritoHistorialDto toHistorialDto(CarritoHistorial historial);

  /**
   * Lista de historial a DTOs
   */
  List<CarritoHistorialDto> toHistorialDtoList(
    List<CarritoHistorial> historiales
  );

  /**
   * DTO simplificado para descuentos aplicados
   */
  @Mapping(source = "aplicadoEn", target = "fechaAplicacion")
  @Mapping(source = "tipoDescuento", target = "tipo")
  @Mapping(source = "montoDescuento", target = "monto")
  @Mapping(source = "porcentajeDescuento", target = "porcentaje")
  @Mapping(source = "codigoDescuento", target = "codigo")
  @Mapping(source = "descripcion", target = "descripcion")
  @Mapping(source = "activo", target = "activo")
  DescuentoAplicadoDto toDescuentoDto(DescuentoAplicado descuento);

  /**
   * Lista de descuentos a DTOs
   */
  List<DescuentoAplicadoDto> toDescuentoDtoList(
    List<DescuentoAplicado> descuentos
  );

  // DTOs como clases concretas simples
  class CarritoHistorialDto {

    private LocalDateTime fecha;
    private String tipo;
    private String descripcion;
    private String usuarioOperacion;
    private String datos;

    // Constructors
    public CarritoHistorialDto() {}

    public CarritoHistorialDto(
      LocalDateTime fecha,
      String tipo,
      String descripcion,
      String usuarioOperacion,
      String datos
    ) {
      this.fecha = fecha;
      this.tipo = tipo;
      this.descripcion = descripcion;
      this.usuarioOperacion = usuarioOperacion;
      this.datos = datos;
    }

    // Getters y Setters
    public LocalDateTime getFecha() {
      return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
      this.fecha = fecha;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public String getUsuarioOperacion() {
      return usuarioOperacion;
    }

    public void setUsuarioOperacion(String usuarioOperacion) {
      this.usuarioOperacion = usuarioOperacion;
    }

    public String getDatos() {
      return datos;
    }

    public void setDatos(String datos) {
      this.datos = datos;
    }
  }

  class DescuentoAplicadoDto {

    private LocalDateTime fechaAplicacion;
    private String tipo;
    private String codigo;
    private String descripcion;
    private BigDecimal monto;
    private BigDecimal porcentaje;
    private Boolean activo;

    // Constructors
    public DescuentoAplicadoDto() {}

    public DescuentoAplicadoDto(
      LocalDateTime fechaAplicacion,
      String tipo,
      String codigo,
      String descripcion,
      BigDecimal monto,
      BigDecimal porcentaje,
      Boolean activo
    ) {
      this.fechaAplicacion = fechaAplicacion;
      this.tipo = tipo;
      this.codigo = codigo;
      this.descripcion = descripcion;
      this.monto = monto;
      this.porcentaje = porcentaje;
      this.activo = activo;
    }

    // Getters y Setters
    public LocalDateTime getFechaAplicacion() {
      return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDateTime fechaAplicacion) {
      this.fechaAplicacion = fechaAplicacion;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

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

    public BigDecimal getMonto() {
      return monto;
    }

    public void setMonto(BigDecimal monto) {
      this.monto = monto;
    }

    public BigDecimal getPorcentaje() {
      return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
      this.porcentaje = porcentaje;
    }

    public Boolean getActivo() {
      return activo;
    }

    public void setActivo(Boolean activo) {
      this.activo = activo;
    }
  }
}
