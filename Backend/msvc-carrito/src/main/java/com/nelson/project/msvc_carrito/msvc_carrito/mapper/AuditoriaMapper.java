package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.CarritoHistorial;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.DescuentoAplicado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

/**
 * Mapper para entidades de auditoría y descuentos
 * REFACTORIZADO: Uniformizado con Lombok para consistencia
 * MIGRADO A LOMBOK: Eliminado código boilerplate en DTOs internos
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
  @Mapping(source = "usuarioOperacion", target = "usuarioOperacion")
  @Mapping(source = "descripcion", target = "descripcion")
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

  /**
   * Convierte HistorialDto a entidad (para operaciones de escritura)
   */
  @Mapping(source = "fecha", target = "fechaOperacion")
  @Mapping(source = "tipo", target = "tipoOperacion")
  @Mapping(source = "datos", target = "datosOperacion")
  @Mapping(source = "usuarioOperacion", target = "usuarioOperacion")
  @Mapping(source = "descripcion", target = "descripcion")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carritoId", ignore = true)
  CarritoHistorial toHistorialEntity(CarritoHistorialDto dto);

  /**
   * Convierte DescuentoDto a entidad (para operaciones de escritura)
   */
  @Mapping(source = "fechaAplicacion", target = "aplicadoEn")
  @Mapping(source = "tipo", target = "tipoDescuento")
  @Mapping(source = "monto", target = "montoDescuento")
  @Mapping(source = "porcentaje", target = "porcentajeDescuento")
  @Mapping(source = "codigo", target = "codigoDescuento")
  @Mapping(source = "descripcion", target = "descripcion")
  @Mapping(source = "activo", target = "activo")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carritoId", ignore = true)
  DescuentoAplicado toDescuentoEntity(DescuentoAplicadoDto dto);

  /**
   * Validaciones post-mapeo para historial
   */
  @AfterMapping
  default void validateHistorial(
    @MappingTarget CarritoHistorialDto dto,
    CarritoHistorial entity
  ) {
    if (dto.getFecha() == null) {
      dto.setFecha(LocalDateTime.now());
    }
    if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
      throw new IllegalArgumentException("Tipo de operación es requerido");
    }
  }

  /**
   * Validaciones post-mapeo para descuentos
   */
  @AfterMapping
  default void validateDescuento(
    @MappingTarget DescuentoAplicadoDto dto,
    DescuentoAplicado entity
  ) {
    if (
      dto.getMonto() != null && dto.getMonto().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException(
        "El monto del descuento no puede ser negativo"
      );
    }
    if (
      dto.getPorcentaje() != null &&
      (dto.getPorcentaje().compareTo(BigDecimal.ZERO) < 0 ||
        dto.getPorcentaje().compareTo(BigDecimal.valueOf(100)) > 0)
    ) {
      throw new IllegalArgumentException(
        "El porcentaje debe estar entre 0 y 100"
      );
    }
  }

  // ================================
  // DTOs MIGRADOS A LOMBOK
  // ================================

  /**
   * DTO para historial del carrito con Lombok
   * MIGRADO: Eliminado código boilerplate manual
   */
  @Data
  @Builder(toBuilder = true)
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Historial de operaciones del carrito")
  class CarritoHistorialDto {

    @Schema(
      description = "Fecha de la operación",
      example = "2024-01-15T10:30:00"
    )
    private LocalDateTime fecha;

    @NotNull
    @Schema(
      description = "Tipo de operación",
      example = "AGREGAR_ITEM",
      required = true
    )
    private String tipo;

    @Schema(
      description = "Descripción de la operación",
      example = "Item agregado al carrito"
    )
    private String descripcion;

    @Schema(
      description = "Usuario que realizó la operación",
      example = "user123"
    )
    private String usuarioOperacion;

    @Schema(description = "Datos adicionales de la operación en JSON")
    private String datos;

    // ================================
    // FACTORY METHODS
    // ================================

    public static CarritoHistorialDto crear(
      String tipo,
      String descripcion,
      String usuario
    ) {
      return CarritoHistorialDto.builder()
        .fecha(LocalDateTime.now())
        .tipo(tipo)
        .descripcion(descripcion)
        .usuarioOperacion(usuario)
        .build();
    }

    public static CarritoHistorialDto crearConDatos(
      String tipo,
      String descripcion,
      String usuario,
      String datos
    ) {
      return CarritoHistorialDto.builder()
        .fecha(LocalDateTime.now())
        .tipo(tipo)
        .descripcion(descripcion)
        .usuarioOperacion(usuario)
        .datos(datos)
        .build();
    }
  }

  /**
   * DTO para descuentos aplicados con Lombok
   * MIGRADO: Eliminado código boilerplate manual
   */
  @Data
  @Builder(toBuilder = true)
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Descuento aplicado al carrito")
  class DescuentoAplicadoDto {

    @Schema(
      description = "Fecha de aplicación",
      example = "2024-01-15T10:30:00"
    )
    private LocalDateTime fechaAplicacion;

    @NotNull
    @Schema(
      description = "Tipo de descuento",
      example = "CUPÓN",
      required = true
    )
    private String tipo;

    @Schema(description = "Código del descuento", example = "PRIMERAVEZ")
    private String codigo;

    @Schema(
      description = "Descripción del descuento",
      example = "10% de descuento para nuevos clientes"
    )
    private String descripcion;

    @Schema(description = "Monto fijo del descuento", example = "10.00")
    private BigDecimal monto;

    @Schema(description = "Porcentaje del descuento", example = "10.5")
    private BigDecimal porcentaje;

    @Schema(description = "Estado del descuento", example = "true")
    private Boolean activo;

    // ================================
    // FACTORY METHODS
    // ================================

    public static DescuentoAplicadoDto porcentaje(
      String codigo,
      String descripcion,
      BigDecimal porcentaje
    ) {
      return DescuentoAplicadoDto.builder()
        .fechaAplicacion(LocalDateTime.now())
        .tipo("PORCENTAJE")
        .codigo(codigo)
        .descripcion(descripcion)
        .porcentaje(porcentaje)
        .activo(true)
        .build();
    }

    public static DescuentoAplicadoDto montoFijo(
      String codigo,
      String descripcion,
      BigDecimal monto
    ) {
      return DescuentoAplicadoDto.builder()
        .fechaAplicacion(LocalDateTime.now())
        .tipo("MONTO_FIJO")
        .codigo(codigo)
        .descripcion(descripcion)
        .monto(monto)
        .activo(true)
        .build();
    }

    public static DescuentoAplicadoDto inactivo(String codigo, String razon) {
      return DescuentoAplicadoDto.builder()
        .fechaAplicacion(LocalDateTime.now())
        .tipo("INACTIVO")
        .codigo(codigo)
        .descripcion(razon)
        .activo(false)
        .build();
    }
  }
}
