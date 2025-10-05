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
 * OPTIMIZADO PARA LOMBOK: Compatible con @Builder(toBuilder = true)
 */
@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  builder = @Builder(disableBuilder = true) // Usa Lombok builders
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

    // OPTIMIZADO: Evitar múltiples operaciones BigDecimal usando valueOf con cache
    BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

    if (descuento != null) {
      subtotal = subtotal.subtract(descuento);
    }

    return subtotal.max(BigDecimal.ZERO);
  }

  /**
   * NUEVO: Método optimizado para calcular subtotal con validaciones
   */
  default BigDecimal calcularSubtotalSeguro(
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal descuento
  ) {
    try {
      if (
        cantidad == null ||
        cantidad <= 0 ||
        precioUnitario == null ||
        precioUnitario.compareTo(BigDecimal.ZERO) <= 0
      ) {
        return BigDecimal.ZERO;
      }

      // OPTIMIZADO: Usar MathContext para operaciones más eficientes
      java.math.MathContext mc = new java.math.MathContext(
        10,
        java.math.RoundingMode.HALF_UP
      );
      BigDecimal subtotal = precioUnitario.multiply(
        BigDecimal.valueOf(cantidad),
        mc
      );

      if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {
        subtotal = subtotal.subtract(descuento, mc);
      }

      return subtotal.max(BigDecimal.ZERO);
    } catch (Exception e) {
      System.err.println("Error calculando subtotal: " + e.getMessage());
      return BigDecimal.ZERO;
    }
  }

  /**
   * NUEVO: Método para actualización batch de precios
   */
  default void actualizarPreciosBatch(
    List<ItemCarrito> items,
    BigDecimal nuevoPrecio
  ) {
    if (items == null || items.isEmpty() || nuevoPrecio == null) {
      return;
    }

    // OPTIMIZADO: Procesar en paralelo para listas grandes
    items
      .parallelStream()
      .forEach(item -> {
        try {
          item.setPrecioUnitario(nuevoPrecio);
          if (item.getCantidad() != null) {
            item.calcularSubtotal();
          }
        } catch (Exception e) {
          System.err.println(
            "Error actualizando precio para item " +
            item.getId() +
            ": " +
            e.getMessage()
          );
        }
      });
  }

  /**
   * NUEVO: Validación optimizada para listas grandes
   */
  default boolean validarItemsEnBatch(List<ItemCarrito> items) {
    if (items == null || items.isEmpty()) {
      return true;
    }

    // OPTIMIZADO: Usar parallel stream con early termination
    return items
      .parallelStream()
      .allMatch(item -> {
        try {
          return (
            item.getProductoId() != null &&
            item.getProductoId() > 0 &&
            item.getCantidad() != null &&
            item.getCantidad() > 0 &&
            item.getPrecioUnitario() != null &&
            item.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0
          );
        } catch (Exception e) {
          return false;
        }
      });
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

    // NUEVAS VALIDACIONES ROBUSTAS
    validateItemCarritoDto(dto);
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

    // NUEVAS VALIDACIONES ROBUSTAS
    validateItemCarritoEntity(entity);
  }

  @AfterMapping
  default void afterFromRequest(
    @MappingTarget ItemCarrito entity,
    AgregarItemRequest request
  ) {
    // Configuraciones iniciales para nuevo item - usar métodos de negocio
    entity.aplicarDescuento(BigDecimal.ZERO);

    // NUEVAS VALIDACIONES ROBUSTAS
    validateItemCarritoEntity(entity);
  }

  @AfterMapping
  default void afterUpdateFromDto(
    @MappingTarget ItemCarrito entity,
    ItemCarritoDto dto
  ) {
    // Recalcular subtotal después de actualización
    if (entity.getCantidad() != null && entity.getPrecioUnitario() != null) {
      try {
        entity.calcularSubtotal();
      } catch (Exception e) {
        System.err.println(
          "Warning: Error recalculando subtotal para item " +
          entity.getId() +
          ": " +
          e.getMessage()
        );
      }
    }

    // NUEVAS VALIDACIONES ROBUSTAS
    validateItemCarritoEntity(entity);
  }

  // ================================
  // VALIDACIONES ROBUSTAS AÑADIDAS
  // ================================

  /**
   * Valida la coherencia de datos en ItemCarritoDto
   */
  default void validateItemCarritoDto(ItemCarritoDto dto) {
    if (dto == null) return;

    // Validar campos obligatorios
    if (dto.getProductoId() == null || dto.getProductoId() <= 0) {
      throw new IllegalArgumentException(
        "ID del producto debe ser válido y positivo"
      );
    }

    if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
      throw new IllegalArgumentException("Cantidad debe ser positiva");
    }

    if (
      dto.getPrecioUnitario() == null ||
      dto.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0
    ) {
      throw new IllegalArgumentException("Precio unitario debe ser positivo");
    }

    // Validar límites de cantidad
    if (dto.getCantidad() > 1000) {
      throw new IllegalArgumentException(
        "Cantidad excede el límite máximo de 1000 unidades"
      );
    }

    // Validar descuentos no negativos
    if (
      dto.getDescuentoAplicado() != null &&
      dto.getDescuentoAplicado().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException("El descuento no puede ser negativo");
    }

    // Validar subtotal coherente
    if (
      dto.getSubtotal() != null &&
      dto.getSubtotal().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException("El subtotal no puede ser negativo");
    }

    // Verificar cálculo de subtotal
    if (
      dto.getSubtotal() != null &&
      dto.getCantidad() != null &&
      dto.getPrecioUnitario() != null
    ) {
      BigDecimal subtotalCalculado = dto.calcularTotal();
      BigDecimal diferencia = dto
        .getSubtotal()
        .subtract(subtotalCalculado)
        .abs();
      if (diferencia.compareTo(BigDecimal.valueOf(0.01)) > 0) {
        System.err.println(
          "Warning: Inconsistencia en subtotal para item producto " +
          dto.getProductoId()
        );
        dto.setSubtotal(subtotalCalculado); // Corregir automáticamente
      }
    }

    // Validar descuento no mayor al subtotal sin descuento
    if (
      dto.getDescuentoAplicado() != null &&
      dto.getCantidad() != null &&
      dto.getPrecioUnitario() != null
    ) {
      BigDecimal subtotalSinDescuento = dto
        .getPrecioUnitario()
        .multiply(BigDecimal.valueOf(dto.getCantidad()));
      if (dto.getDescuentoAplicado().compareTo(subtotalSinDescuento) > 0) {
        throw new IllegalArgumentException(
          "El descuento no puede ser mayor al subtotal sin descuento"
        );
      }
    }

    // Validar peso no negativo
    if (
      dto.getPesoProducto() != null &&
      dto.getPesoProducto().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException(
        "El peso del producto no puede ser negativo"
      );
    }

    // Validar nombres no vacíos
    if (
      dto.getNombreProducto() != null &&
      dto.getNombreProducto().trim().isEmpty()
    ) {
      throw new IllegalArgumentException(
        "El nombre del producto no puede estar vacío"
      );
    }

    // Validar fechas lógicas
    if (dto.getFechaAgregado() != null && dto.getFechaModificacion() != null) {
      if (dto.getFechaModificacion().isBefore(dto.getFechaAgregado())) {
        throw new IllegalArgumentException(
          "La fecha de modificación no puede ser anterior a la de agregado"
        );
      }
    }
  }

  /**
   * Valida la coherencia de datos en entidad ItemCarrito
   */
  default void validateItemCarritoEntity(ItemCarrito entity) {
    if (entity == null) return;

    // Validar campos obligatorios de la entidad
    if (entity.getProductoId() == null || entity.getProductoId() <= 0) {
      throw new IllegalArgumentException(
        "ID del producto en entidad debe ser válido y positivo"
      );
    }

    if (entity.getCantidad() == null || entity.getCantidad() <= 0) {
      throw new IllegalArgumentException(
        "Cantidad en entidad debe ser positiva"
      );
    }

    if (
      entity.getPrecioUnitario() == null ||
      entity.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0
    ) {
      throw new IllegalArgumentException(
        "Precio unitario en entidad debe ser positivo"
      );
    }

    // Validar coherencia entre precios
    if (
      entity.getPrecioOriginal() != null && entity.getPrecioUnitario() != null
    ) {
      if (
        entity.getPrecioUnitario().compareTo(entity.getPrecioOriginal()) > 0
      ) {
        System.err.println(
          "Warning: Precio unitario mayor al original para producto " +
          entity.getProductoId()
        );
      }
    }

    // Validar descuento de item
    if (
      entity.getDescuentoItem() != null &&
      entity.getDescuentoItem().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException(
        "Descuento de item no puede ser negativo"
      );
    }

    // Validar subtotal
    if (
      entity.getSubtotal() != null &&
      entity.getSubtotal().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException(
        "Subtotal de entidad no puede ser negativo"
      );
    }

    // Verificar cálculo de subtotal en entidad
    if (
      entity.getSubtotal() != null &&
      entity.getCantidad() != null &&
      entity.getPrecioUnitario() != null
    ) {
      try {
        BigDecimal subtotalCalculado = entity
          .getPrecioUnitario()
          .multiply(BigDecimal.valueOf(entity.getCantidad()));
        if (entity.getDescuentoItem() != null) {
          subtotalCalculado = subtotalCalculado.subtract(
            entity.getDescuentoItem()
          );
        }

        BigDecimal diferencia = entity
          .getSubtotal()
          .subtract(subtotalCalculado)
          .abs();
        if (diferencia.compareTo(BigDecimal.valueOf(0.01)) > 0) {
          System.err.println(
            "Warning: Subtotal inconsistente en entidad para producto " +
            entity.getProductoId()
          );
          entity.calcularSubtotal(); // Recalcular
        }
      } catch (Exception e) {
        System.err.println(
          "Error validando subtotal en entidad: " + e.getMessage()
        );
      }
    }

    // Validar peso
    if (
      entity.getPeso() != null &&
      entity.getPeso().compareTo(BigDecimal.ZERO) < 0
    ) {
      throw new IllegalArgumentException(
        "Peso en entidad no puede ser negativo"
      );
    }

    // Validar nombres
    if (
      entity.getNombreProducto() != null &&
      entity.getNombreProducto().trim().isEmpty()
    ) {
      throw new IllegalArgumentException(
        "Nombre del producto en entidad no puede estar vacío"
      );
    }

    // Validar límites de cantidad
    if (entity.getCantidad() > 1000) {
      throw new IllegalArgumentException(
        "Cantidad en entidad excede límite máximo"
      );
    }
  }
}
