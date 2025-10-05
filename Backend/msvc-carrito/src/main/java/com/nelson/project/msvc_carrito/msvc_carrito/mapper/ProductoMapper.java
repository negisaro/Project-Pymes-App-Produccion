package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.ItemCarrito;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper completo para conversiones de datos de productos
 * Especializado en mapeo de información desnormalizada del producto
 * NUEVO: Creado para completar la arquitectura de mappers
 * OPTIMIZADO PARA LOMBOK: Compatible con @Builder(toBuilder = true)
 */
@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  builder = @Builder(disableBuilder = true) // Usa Lombok builders
)
public interface ProductoMapper {
  /**
   * Convierte datos desnormalizados de ItemCarrito a ProductoDto
   * Útil para extraer información del producto desde items del carrito
   */
  @Mapping(source = "productoId", target = "id")
  @Mapping(source = "nombreProducto", target = "nombre")
  @Mapping(source = "descripcionProducto", target = "descripcion")
  @Mapping(source = "precioUnitario", target = "precio")
  @Mapping(
    source = "imagenUrl",
    target = "imagenes",
    qualifiedByName = "urlToList"
  )
  @Mapping(source = "categoriaNombre", target = "categoriaId")
  @Mapping(target = "activo", constant = "true")
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @Mapping(target = "stock", ignore = true)
  ProductoDto fromItemCarrito(ItemCarrito item);

  /**
   * Convierte lista de items del carrito a lista de ProductoDtos únicos
   */
  List<ProductoDto> fromItemCarritoList(List<ItemCarrito> items);

  /**
   * Actualiza datos desnormalizados en ItemCarrito desde ProductoDto
   * Útil cuando se actualiza información del producto
   */
  @Mapping(source = "id", target = "productoId")
  @Mapping(source = "nombre", target = "nombreProducto")
  @Mapping(source = "descripcion", target = "descripcionProducto")
  @Mapping(source = "precio", target = "precioUnitario")
  @Mapping(source = "precio", target = "precioOriginal")
  @Mapping(
    source = "imagenes",
    target = "imagenUrl",
    qualifiedByName = "listToFirstUrl"
  )
  @Mapping(source = "categoriaId", target = "categoriaNombre")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(target = "cantidad", ignore = true)
  @Mapping(target = "descuentoItem", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "notas", ignore = true)
  @Mapping(target = "agregadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "peso", ignore = true)
  void updateItemFromProducto(
    ProductoDto producto,
    @MappingTarget ItemCarrito item
  );

  /**
   * Sincroniza precios en items del carrito cuando cambia el precio del producto
   */
  @Mapping(source = "precio", target = "precioUnitario")
  @Mapping(source = "precio", target = "precioOriginal")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "carrito", ignore = true)
  @Mapping(target = "productoId", ignore = true)
  @Mapping(target = "cantidad", ignore = true)
  @Mapping(target = "nombreProducto", ignore = true)
  @Mapping(target = "descripcionProducto", ignore = true)
  @Mapping(target = "imagenUrl", ignore = true)
  @Mapping(target = "categoriaNombre", ignore = true)
  @Mapping(target = "peso", ignore = true)
  @Mapping(target = "descuentoItem", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  @Mapping(target = "categoriaId", ignore = true)
  @Mapping(target = "unidadMedida", ignore = true)
  @Mapping(target = "notas", ignore = true)
  @Mapping(target = "agregadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  void sincronizarPrecio(ProductoDto producto, @MappingTarget ItemCarrito item);

  /**
   * Crea ProductoDto básico desde datos mínimos
   */
  @Mapping(target = "activo", constant = "true")
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @Mapping(target = "stock", ignore = true)
  @Mapping(target = "imagenes", ignore = true)
  ProductoDto crearBasico(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String categoria
  );

  // ================================
  // MÉTODOS DE UTILIDAD
  // ================================

  /**
   * Convierte URL única a lista
   */
  @Named("urlToList")
  default List<String> urlToList(String url) {
    return url != null && !url.trim().isEmpty() ? List.of(url) : List.of();
  }

  /**
   * Obtiene la primera URL de una lista
   */
  @Named("listToFirstUrl")
  default String listToFirstUrl(List<String> urls) {
    return urls != null && !urls.isEmpty() ? urls.get(0) : null;
  }

  /**
   * Calcula descuento porcentual
   */
  default BigDecimal calcularDescuentoPorcentaje(
    BigDecimal precioOriginal,
    BigDecimal precioConDescuento
  ) {
    if (
      precioOriginal == null ||
      precioConDescuento == null ||
      precioOriginal.compareTo(BigDecimal.ZERO) <= 0
    ) {
      return BigDecimal.ZERO;
    }

    BigDecimal diferencia = precioOriginal.subtract(precioConDescuento);
    return diferencia
      .divide(precioOriginal, 4, java.math.RoundingMode.HALF_UP)
      .multiply(BigDecimal.valueOf(100));
  }

  // ================================
  // VALIDACIONES POST-MAPEO
  // ================================

  /**
   * Validaciones después de crear ProductoDto desde ItemCarrito
   */
  @AfterMapping
  default void validateProductoFromItem(
    @MappingTarget ProductoDto dto,
    ItemCarrito item
  ) {
    // Validar que el ID del producto esté presente
    if (dto.getId() == null) {
      throw new IllegalArgumentException("ID del producto es requerido");
    }

    // Validar precio positivo
    if (
      dto.getPrecio() != null && dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0
    ) {
      throw new IllegalArgumentException("El precio debe ser mayor a cero");
    }

    // Validar nombre no vacío
    if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
      throw new IllegalArgumentException("Nombre del producto es requerido");
    }
  }

  /**
   * Validaciones después de actualizar ItemCarrito desde ProductoDto
   */
  @AfterMapping
  default void validateItemFromProducto(
    @MappingTarget ItemCarrito item,
    ProductoDto dto
  ) {
    // Recalcular subtotal después de actualizar precios
    if (item.getCantidad() != null && item.getPrecioUnitario() != null) {
      try {
        item.calcularSubtotal();
      } catch (Exception e) {
        // Log error pero no fallar el mapeo
        System.err.println(
          "Warning: No se pudo recalcular subtotal para item " + item.getId()
        );
      }
    }

    // Validar coherencia de datos
    if (item.getPrecioUnitario() != null && item.getPrecioOriginal() != null) {
      if (item.getPrecioUnitario().compareTo(item.getPrecioOriginal()) > 0) {
        System.err.println(
          "Warning: Precio unitario mayor al precio original para producto " +
          item.getProductoId()
        );
      }
    }
  }

  /**
   * Configuraciones después de sincronizar precios
   */
  @AfterMapping
  default void afterSincronizarPrecio(
    @MappingTarget ItemCarrito item,
    ProductoDto dto
  ) {
    // Recalcular subtotal con nuevo precio
    if (item.getCantidad() != null && item.getPrecioUnitario() != null) {
      try {
        item.calcularSubtotal();
      } catch (Exception e) {
        System.err.println(
          "Error recalculando subtotal después de sincronizar precio: " +
          e.getMessage()
        );
      }
    }

    // Limpiar descuentos si el nuevo precio es menor
    if (dto.getPrecio() != null && item.getDescuentoItem() != null) {
      BigDecimal subtotalSinDescuento = dto
        .getPrecio()
        .multiply(BigDecimal.valueOf(item.getCantidad()));
      if (item.getDescuentoItem().compareTo(subtotalSinDescuento) >= 0) {
        item.aplicarDescuento(BigDecimal.ZERO); // Remover descuento excesivo
      }
    }
  }
}
