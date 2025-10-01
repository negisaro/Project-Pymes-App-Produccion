package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request.AgregarItemRequest;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.ItemCarrito;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper completo para conversiones entre entidades y DTOs de items del carrito
 * Utiliza MapStruct para generación automática de código de mapeo
 */
@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ItemCarritoMapper {
  /**
   * Convierte una entidad ItemCarrito a ItemCarritoDto
   */
  @Mapping(source = "agregadoEn", target = "fechaAgregado")
  @Mapping(source = "actualizadoEn", target = "fechaModificacion")
  @Mapping(source = "descuentoItem", target = "descuentoAplicado")
  @Mapping(source = "imagenUrl", target = "imagenProducto")
  @Mapping(source = "categoriaNombre", target = "categoriaProducto")
  @Mapping(source = "peso", target = "pesoProducto")
  @Mapping(target = "marcaProducto", ignore = true)
  @Mapping(target = "dimensionesProducto", ignore = true)
  @Mapping(target = "disponible", constant = "true")
  @Mapping(target = "stockDisponible", ignore = true)
  @Mapping(target = "skuProducto", ignore = true)
  @Mapping(source = "version", target = "version")
  ItemCarritoDto toDto(ItemCarrito itemCarrito);

  /**
   * Convierte una lista de entidades ItemCarrito a lista de ItemCarritoDtos
   */
  List<ItemCarritoDto> toDtoList(List<ItemCarrito> items);

  /**
   * Convierte un ItemCarritoDto a entidad ItemCarrito
   */
  @Mapping(target = "carrito", ignore = true)
  @Mapping(source = "fechaAgregado", target = "agregadoEn")
  @Mapping(source = "fechaModificacion", target = "actualizadoEn")
  @Mapping(source = "descuentoAplicado", target = "descuentoItem")
  @Mapping(source = "imagenProducto", target = "imagenUrl")
  @Mapping(source = "categoriaProducto", target = "categoriaNombre")
  @Mapping(source = "pesoProducto", target = "peso")
  @Mapping(target = "precioOriginal", source = "precioUnitario")
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "notas", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  ItemCarrito toEntity(ItemCarritoDto itemCarritoDto);

  /**
   * Convierte un AgregarItemRequest a entidad ItemCarrito
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(target = "precioUnitario", ignore = true)
  @Mapping(target = "precioOriginal", ignore = true)
  @Mapping(target = "descuentoItem", constant = "0")
  @Mapping(target = "nombreProducto", ignore = true)
  @Mapping(target = "descripcionProducto", ignore = true)
  @Mapping(target = "imagenUrl", ignore = true)
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "categoriaNombre", ignore = true)
  @Mapping(target = "peso", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "agregadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(source = "notas", target = "notas")
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  ItemCarrito fromAgregarRequest(AgregarItemRequest request);

  /**
   * Actualiza una entidad ItemCarrito con datos de un ItemCarritoDto
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(source = "fechaAgregado", target = "agregadoEn")
  @Mapping(source = "fechaModificacion", target = "actualizadoEn")
  @Mapping(source = "descuentoAplicado", target = "descuentoItem")
  @Mapping(source = "imagenProducto", target = "imagenUrl")
  @Mapping(source = "categoriaProducto", target = "categoriaNombre")
  @Mapping(source = "pesoProducto", target = "peso")
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  void updateEntityFromDto(
    ItemCarritoDto dto,
    @MappingTarget ItemCarrito itemCarrito
  );

  /**
   * Mapeo parcial para actualizar solo campos específicos
   */
  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(target = "productoId", ignore = true)
  @Mapping(source = "fechaAgregado", target = "agregadoEn")
  @Mapping(source = "fechaModificacion", target = "actualizadoEn")
  @Mapping(source = "descuentoAplicado", target = "descuentoItem")
  @Mapping(source = "imagenProducto", target = "imagenUrl")
  @Mapping(source = "categoriaProducto", target = "categoriaNombre")
  @Mapping(source = "pesoProducto", target = "peso")
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  void updateEntityPartial(
    ItemCarritoDto dto,
    @MappingTarget ItemCarrito itemCarrito
  );

  /**
   * Actualiza solo la cantidad de un item
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(target = "productoId", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  void updateCantidad(
    Integer nuevaCantidad,
    @MappingTarget ItemCarrito itemCarrito
  );

  // Métodos de utilidad para expresiones personalizadas
  default BigDecimal calcularSubtotal(
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal descuento
  ) {
    if (cantidad == null || precioUnitario == null) {
      return BigDecimal.ZERO;
    }

    BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

    if (descuento != null) {
      subtotal = subtotal.subtract(descuento);
    }

    return subtotal.max(BigDecimal.ZERO);
  }

  /**
   * Mapeo después de la conversión para configuraciones adicionales
   */
  @AfterMapping
  default void afterToDto(
    @MappingTarget ItemCarritoDto dto,
    ItemCarrito entity
  ) {
    // Calcular subtotal si no está presente
    if (
      dto.getSubtotal() == null &&
      dto.getCantidad() != null &&
      dto.getPrecioUnitario() != null
    ) {
      dto.setSubtotal(dto.calcularTotal());
    }
  }

  @AfterMapping
  default void afterToEntity(
    @MappingTarget ItemCarrito entity,
    ItemCarritoDto dto
  ) {
    // Recalcular subtotal después del mapeo
    if (entity.getCantidad() != null && entity.getPrecioUnitario() != null) {
      entity.calcularSubtotal();
    }
  }

  @AfterMapping
  default void afterFromRequest(
    @MappingTarget ItemCarrito entity,
    AgregarItemRequest request
  ) {
    // Configuraciones iniciales para nuevo item - usar métodos de negocio
    entity.aplicarDescuento(BigDecimal.ZERO);
  }
}
