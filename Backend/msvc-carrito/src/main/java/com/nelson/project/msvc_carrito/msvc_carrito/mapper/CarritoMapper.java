package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request.CrearCarritoRequest;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response.CarritoResponse;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response.ResumenCarritoResponse;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper completo para conversiones entre entidades y DTOs del carrito
 * Utiliza MapStruct para generación automática de código de mapeo
 */
@Mapper(
  componentModel = "spring",
  uses = { ItemCarritoMapper.class },
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CarritoMapper {
  /**
   * Convierte una entidad Carrito a CarritoDto
   */
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(source = "creadoEn", target = "fechaCreacion")
  @Mapping(source = "actualizadoEn", target = "fechaModificacion")
  @Mapping(source = "descuento", target = "totalDescuentos")
  @Mapping(target = "totalImpuestos", constant = "0")
  @Mapping(source = "expiraEn", target = "fechaExpiracion")
  @Mapping(
    target = "cantidadItems",
    expression = "java(calcularCantidadItems(carrito))"
  )
  @Mapping(target = "moneda", constant = "USD")
  @Mapping(source = "version", target = "version")
  CarritoDto toDto(Carrito carrito);

  /**
   * Convierte una lista de entidades Carrito a lista de CarritoDtos
   */
  List<CarritoDto> toDtoList(List<Carrito> carritos);

  /**
   * Convierte un CarritoDto a entidad Carrito
   */
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter - usar aplicarDescuento()
  Carrito toEntity(CarritoDto carritoDto);

  /**
   * Convierte un CrearCarritoRequest a entidad Carrito
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "items", ignore = true)
  @Mapping(target = "estado", constant = "ACTIVO")
  @Mapping(target = "subtotal", ignore = true) // Sin setter - se calcula automáticamente
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(target = "total", ignore = true) // Sin setter - se calcula automáticamente
  @Mapping(target = "expiraEn", ignore = true)
  @Mapping(target = "notas", source = "notasIniciales")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "ipCliente", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  Carrito fromCrearRequest(CrearCarritoRequest request);

  /**
   * Convierte una entidad Carrito a CarritoResponse
   */
  @Mapping(source = ".", target = "carrito")
  @Mapping(target = "calculos", ignore = true)
  @Mapping(target = "validacion", ignore = true)
  @Mapping(target = "recomendaciones", ignore = true)
  @Mapping(target = "descuentosDisponibles", ignore = true)
  CarritoResponse toCarritoResponse(Carrito carrito);

  /**
   * Convierte una entidad Carrito a ResumenCarritoResponse
   */
  @Mapping(source = "id", target = "carritoId")
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(
    target = "cantidadItems",
    expression = "java(calcularCantidadItems(carrito))"
  )
  @Mapping(source = "subtotal", target = "subtotal")
  @Mapping(source = "descuento", target = "totalDescuentos")
  @Mapping(source = "total", target = "totalFinal")
  @Mapping(target = "moneda", constant = "USD")
  @Mapping(
    target = "cantidadProductos",
    expression = "java(calcularCantidadProductos(carrito))"
  )
  @Mapping(
    target = "vacio",
    expression = "java(carrito.getItems() == null || carrito.getItems().isEmpty())"
  )
  @Mapping(target = "tieneItemsNoDisponibles", ignore = true)
  @Mapping(
    target = "nombresProductos",
    expression = "java(extraerNombresProductos(carrito))"
  )
  @Mapping(target = "ahorro", ignore = true)
  ResumenCarritoResponse toResumenResponse(Carrito carrito);

  /**
   * Actualiza una entidad Carrito con datos de un CarritoDto
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  void updateEntityFromDto(CarritoDto dto, @MappingTarget Carrito carrito);

  /**
   * Mapeo parcial para actualizar solo campos específicos
   */
  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  void updateEntityPartial(CarritoDto dto, @MappingTarget Carrito carrito);

  // Métodos de utilidad para expresiones personalizadas
  default Integer calcularCantidadProductos(Carrito carrito) {
    if (carrito.getItems() == null) {
      return 0;
    }
    return carrito.getItems().size();
  }

  default Integer calcularCantidadItems(Carrito carrito) {
    if (carrito.getItems() == null) {
      return 0;
    }
    return carrito
      .getItems()
      .stream()
      .mapToInt(item -> item.getCantidad() != null ? item.getCantidad() : 0)
      .sum();
  }

  default List<String> extraerNombresProductos(Carrito carrito) {
    if (carrito.getItems() == null) {
      return List.of();
    }
    return carrito
      .getItems()
      .stream()
      .map(item -> item.getNombreProducto())
      .filter(nombre -> nombre != null)
      .toList();
  }

  /**
   * Mapeo después de la conversión para configuraciones adicionales
   */
  @AfterMapping
  default void afterToDto(@MappingTarget CarritoDto dto, Carrito entity) {
    // Configuraciones adicionales después del mapeo
    if (dto.getItems() != null) {
      dto.setCantidadItems(
        dto
          .getItems()
          .stream()
          .mapToInt(item -> item.getCantidad() != null ? item.getCantidad() : 0)
          .sum()
      );
    }
  }

  @AfterMapping
  default void afterToEntity(@MappingTarget Carrito entity, CarritoDto dto) {
    // Configuraciones adicionales después del mapeo
    if (entity.getItems() != null) {
      entity.getItems().forEach(item -> item.setCarrito(entity));
    }

    // Aplicar descuento si se especifica
    if (dto.getTotalDescuentos() != null && dto.getCodigoCupon() != null) {
      entity.aplicarDescuento(dto.getTotalDescuentos(), dto.getCodigoCupon());
    }
  }

  @AfterMapping
  default void afterUpdateFromDto(
    @MappingTarget Carrito entity,
    CarritoDto dto
  ) {
    // Aplicar descuento si se especifica
    if (dto.getTotalDescuentos() != null && dto.getCodigoCupon() != null) {
      entity.aplicarDescuento(dto.getTotalDescuentos(), dto.getCodigoCupon());
    }
  }
}
