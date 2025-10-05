package com.nelson.project.msvc_carrito.msvc_carrito.service.impl;

// import com.nelson.project.msvc_carrito.msvc_carrito.clientfeign.ProductoFeignClient;
import com.nelson.project.msvc_carrito.msvc_carrito.event.ItemAgregadoEvent;
import com.nelson.project.msvc_carrito.msvc_carrito.exception.BusinessException;
import com.nelson.project.msvc_carrito.msvc_carrito.mapper.CarritoMapper;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ConfirmacionOperacionDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.ItemCarrito;
import com.nelson.project.msvc_carrito.msvc_carrito.repository.CarritoRepository;
// import com.nelson.project.msvc_carrito.msvc_carrito.repository.ItemCarritoQueryRepository;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoCoreService;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoItemService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de items de carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarritoItemServiceImpl implements CarritoItemService {

  private static final Logger log = LoggerFactory.getLogger(
    CarritoItemServiceImpl.class
  );

  private static final int MAX_ITEMS_POR_CARRITO = 50;
  private static final int MAX_CANTIDAD_POR_ITEM = 999;
  // TODO: Implementar validación de precio mínimo
  // private static final BigDecimal PRECIO_MINIMO_ITEM = new BigDecimal("0.01");
  private static final String CACHE_CARRITO = "carritos";

  private final CarritoRepository carritoRepository;
  // TODO: Implementar queries personalizadas
  // private final ItemCarritoQueryRepository itemCarritoQueryRepository;
  private final CarritoMapper carritoMapper;
  // TODO: Implementar sincronización con servicio de productos
  // private final ProductoFeignClient productoClient;
  private final ApplicationEventPublisher eventPublisher;
  private final CarritoCoreService carritoCoreService;

  @Override
  @Transactional
  @CacheEvict(value = CACHE_CARRITO, key = "#usuarioId")
  public CarritoDto agregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    Long usuarioId
  ) {
    log.info(
      "Agregando item al carrito {}: producto {} cantidad {}",
      carritoId,
      productoId,
      cantidad
    );

    try {
      // Validaciones básicas
      validarParametrosAgregarItem(carritoId, productoId, cantidad, usuarioId);

      // Obtener carrito y validar permisos
      Carrito carrito = obtenerCarritoConValidacion(carritoId, usuarioId);

      // Validar límites de negocio
      validarLimitesCarrito(carrito, cantidad);

      // Validar disponibilidad de stock
      if (!validarDisponibilidadStock(productoId, cantidad)) {
        throw new BusinessException(
          "Stock insuficiente para el producto",
          "STOCK_INSUFICIENTE"
        );
      }

      // Verificar si el item ya existe en el carrito
      Optional<ItemCarrito> itemExistente = carrito
        .getItems()
        .stream()
        .filter(item -> item.getProductoId().equals(productoId))
        .findFirst();

      if (itemExistente.isPresent()) {
        // Actualizar cantidad del item existente
        ItemCarrito item = itemExistente.get();
        Integer nuevaCantidad = item.getCantidad() + cantidad;
        return actualizarCantidadItem(
          carritoId,
          item.getId(),
          nuevaCantidad,
          usuarioId
        );
      } else {
        // Crear nuevo item
        ItemCarrito nuevoItem = crearNuevoItem(carrito, productoId, cantidad);
        carrito.getItems().add(nuevoItem);
      }

      // Recalcular totales y guardar
      recalcularTotalesCarrito(carrito);
      carrito = carritoRepository.save(carrito);

      // Publicar evento de item agregado
      eventPublisher.publishEvent(
        new ItemAgregadoEvent(this, carritoId, usuarioId, productoId, cantidad)
      );

      CarritoDto carritoDto = carritoMapper.toDto(carrito);

      log.info(
        "Item agregado exitosamente al carrito {}: producto {} cantidad {}",
        carritoId,
        productoId,
        cantidad
      );

      return carritoDto;
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error agregando item al carrito {}: {}",
        carritoId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error agregando item al carrito",
        "ITEM_AGREGAR_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional
  @CacheEvict(value = CACHE_CARRITO, key = "#usuarioId")
  public CarritoDto actualizarCantidadItem(
    Long carritoId,
    Long itemId,
    Integer nuevaCantidad,
    Long usuarioId
  ) {
    log.info(
      "Actualizando cantidad del item {} en carrito {}: nueva cantidad {}",
      itemId,
      carritoId,
      nuevaCantidad
    );

    try {
      // Validaciones básicas
      if (nuevaCantidad == null || nuevaCantidad < 0) {
        throw new BusinessException("Cantidad inválida", "CANTIDAD_INVALIDA");
      }

      if (nuevaCantidad > MAX_CANTIDAD_POR_ITEM) {
        throw new BusinessException(
          "Cantidad excede el máximo permitido",
          "CANTIDAD_MAXIMA_EXCEDIDA"
        );
      }

      // Obtener carrito y validar permisos
      Carrito carrito = obtenerCarritoConValidacion(carritoId, usuarioId);

      // Buscar el item en el carrito
      ItemCarrito item = carrito
        .getItems()
        .stream()
        .filter(i -> i.getId().equals(itemId))
        .findFirst()
        .orElseThrow(() ->
          new BusinessException(
            "Item no encontrado en el carrito",
            "ITEM_NO_ENCONTRADO"
          )
        );

      // Si la nueva cantidad es 0, eliminar el item
      if (nuevaCantidad == 0) {
        eliminarItemInterno(carrito, item, usuarioId);
        // Retornar el carrito actualizado después de eliminar el item
        return carritoMapper.toDto(carritoRepository.save(carrito));
      }

      // Validar disponibilidad de stock
      if (!validarDisponibilidadStock(item.getProductoId(), nuevaCantidad)) {
        throw new BusinessException(
          "Stock insuficiente para la cantidad solicitada",
          "STOCK_INSUFICIENTE"
        );
      }

      // Actualizar cantidad
      item.setCantidad(nuevaCantidad);
      item.setSubtotal(
        item.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad))
      );
      item.setFechaModificacion(LocalDateTime.now());

      // Recalcular totales y guardar
      recalcularTotalesCarrito(carrito);
      carrito = carritoRepository.save(carrito);

      // TODO: Publicar evento de actualización

      CarritoDto carritoDto = carritoMapper.toDto(carrito);

      log.info(
        "Cantidad del item {} actualizada exitosamente en carrito {}",
        itemId,
        carritoId
      );

      return carritoDto;
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error actualizando cantidad del item {} en carrito {}: {}",
        itemId,
        carritoId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error actualizando cantidad del item",
        "ITEM_ACTUALIZAR_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional
  @CacheEvict(value = CACHE_CARRITO, key = "#usuarioId")
  public ConfirmacionOperacionDto eliminarItem(
    Long carritoId,
    Long itemId,
    Long usuarioId
  ) {
    log.info("Eliminando item {} del carrito {}", itemId, carritoId);

    try {
      // Obtener carrito y validar permisos
      Carrito carrito = obtenerCarritoConValidacion(carritoId, usuarioId);

      // Buscar el item en el carrito
      ItemCarrito item = carrito
        .getItems()
        .stream()
        .filter(i -> i.getId().equals(itemId))
        .findFirst()
        .orElseThrow(() ->
          new BusinessException(
            "Item no encontrado en el carrito",
            "ITEM_NO_ENCONTRADO"
          )
        );

      return eliminarItemInterno(carrito, item, usuarioId);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error eliminando item {} del carrito {}: {}",
        itemId,
        carritoId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error eliminando item del carrito",
        "ITEM_ELIMINAR_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional
  @CacheEvict(value = CACHE_CARRITO, key = "#usuarioId")
  public ConfirmacionOperacionDto vaciarCarrito(
    Long carritoId,
    Long usuarioId
  ) {
    log.info("Vaciando carrito {}", carritoId);

    try {
      // Obtener carrito y validar permisos
      Carrito carrito = obtenerCarritoConValidacion(carritoId, usuarioId);

      // Eliminar todos los items
      carrito.getItems().clear();

      // Recalcular totales y guardar
      recalcularTotalesCarrito(carrito);
      carritoRepository.save(carrito);

      // TODO: Publicar evento

      log.info("Carrito {} vaciado exitosamente", carritoId);

      return ConfirmacionOperacionDto.builder()
        .exitoso(true)
        .mensaje("Carrito vaciado exitosamente")
        .build();
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error("Error vaciando carrito {}: {}", carritoId, e.getMessage(), e);
      throw new BusinessException(
        "Error vaciando carrito",
        "CARRITO_VACIAR_ERROR",
        e
      );
    }
  }

  @Override
  public List<ItemCarritoDto> obtenerItemsCarrito(
    Long carritoId,
    Long usuarioId
  ) {
    log.info(
      "Obteniendo items del carrito {} para usuario {}",
      carritoId,
      usuarioId
    );

    try {
      // Obtener carrito y validar permisos
      Carrito carrito = obtenerCarritoConValidacion(carritoId, usuarioId);

      // TODO: Enriquecer items con información adicional del producto

      return carrito
        .getItems()
        .stream()
        .map(item ->
          ItemCarritoDto.builder()
            .id(item.getId())
            .productoId(item.getProductoId())
            .cantidad(item.getCantidad())
            .precioUnitario(item.getPrecioUnitario())
            .subtotal(item.getSubtotal())
            .fechaAgregado(item.getFechaCreacion())
            .build()
        )
        .toList();
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error obteniendo items del carrito {}: {}",
        carritoId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error obteniendo items del carrito",
        "ITEMS_OBTENER_ERROR",
        e
      );
    }
  }

  @Override
  public boolean validarDisponibilidadStock(
    Long productoId,
    Integer cantidadSolicitada
  ) {
    try {
      // TODO: Implementar validación real con ProductoFeignClient
      log.debug(
        "Validando stock para producto {} cantidad {}",
        productoId,
        cantidadSolicitada
      );
      return (
        cantidadSolicitada > 0 && cantidadSolicitada <= MAX_CANTIDAD_POR_ITEM
      );
    } catch (Exception e) {
      log.warn(
        "Error validando stock para producto {}: {}",
        productoId,
        e.getMessage()
      );
      return false;
    }
  }

  @Override
  public CarritoDto sincronizarPrecios(Long carritoId) {
    // TODO: Implementar sincronización de precios
    log.info("Sincronizando precios del carrito {}", carritoId);
    throw new BusinessException(
      "Funcionalidad en desarrollo",
      "FUNCIONALIDAD_DESARROLLO"
    );
  }

  @Override
  public CarritoDto aplicarPromocionesAutomaticas(Long carritoId) {
    // TODO: Implementar aplicación de promociones automáticas
    log.info("Aplicando promociones automáticas al carrito {}", carritoId);
    throw new BusinessException(
      "Funcionalidad en desarrollo",
      "FUNCIONALIDAD_DESARROLLO"
    );
  }

  // ===============================
  // MÉTODOS PRIVADOS DE SOPORTE
  // ===============================

  private void validarParametrosAgregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    Long usuarioId
  ) {
    if (carritoId == null || carritoId <= 0) {
      throw new BusinessException(
        "ID de carrito inválido",
        "CARRITO_ID_INVALIDO"
      );
    }
    if (productoId == null || productoId <= 0) {
      throw new BusinessException(
        "ID de producto inválido",
        "PRODUCTO_ID_INVALIDO"
      );
    }
    if (cantidad == null || cantidad <= 0) {
      throw new BusinessException(
        "Cantidad debe ser mayor a cero",
        "CANTIDAD_INVALIDA"
      );
    }
    if (cantidad > MAX_CANTIDAD_POR_ITEM) {
      throw new BusinessException(
        "Cantidad excede el máximo permitido",
        "CANTIDAD_MAXIMA_EXCEDIDA"
      );
    }
    if (usuarioId == null || usuarioId <= 0) {
      throw new BusinessException(
        "ID de usuario inválido",
        "USUARIO_ID_INVALIDO"
      );
    }
  }

  private Carrito obtenerCarritoConValidacion(Long carritoId, Long usuarioId) {
    Optional<CarritoDto> carritoDto = carritoCoreService.obtenerCarritoPorId(
      carritoId,
      usuarioId
    );
    if (carritoDto.isEmpty()) {
      throw new BusinessException(
        "Carrito no encontrado",
        "CARRITO_NO_ENCONTRADO"
      );
    }

    return carritoRepository
      .findById(carritoId)
      .orElseThrow(() ->
        new BusinessException("Carrito no encontrado", "CARRITO_NO_ENCONTRADO")
      );
  }

  private void validarLimitesCarrito(Carrito carrito, Integer nuevaCantidad) {
    if (carrito.getItems().size() >= MAX_ITEMS_POR_CARRITO) {
      throw new BusinessException(
        "Carrito ha alcanzado el máximo de items permitidos",
        "CARRITO_LIMITE_ITEMS"
      );
    }
  }

  private ItemCarrito crearNuevoItem(
    Carrito carrito,
    Long productoId,
    Integer cantidad
  ) {
    // TODO: Obtener información del producto desde ProductoFeignClient
    BigDecimal precioUnitario = new BigDecimal("10.00"); // Stub price

    ItemCarrito nuevoItem = new ItemCarrito();
    nuevoItem.setCarrito(carrito);
    nuevoItem.setProductoId(productoId);
    nuevoItem.setCantidad(cantidad);
    nuevoItem.setPrecioUnitario(precioUnitario);
    nuevoItem.setSubtotal(
      precioUnitario.multiply(BigDecimal.valueOf(cantidad))
    );
    nuevoItem.setFechaCreacion(LocalDateTime.now());

    return nuevoItem;
  }

  private void recalcularTotalesCarrito(Carrito carrito) {
    int totalItems = carrito
      .getItems()
      .stream()
      .mapToInt(ItemCarrito::getCantidad)
      .sum();

    BigDecimal subtotal = carrito
      .getItems()
      .stream()
      .map(ItemCarrito::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    carrito.setTotalItems(totalItems);
    carrito.setSubtotal(subtotal);
    carrito.setTotal(subtotal); // TODO: Considerar descuentos e impuestos
    carrito.setFechaModificacion(LocalDateTime.now());
  }

  private ConfirmacionOperacionDto eliminarItemInterno(
    Carrito carrito,
    ItemCarrito item,
    Long usuarioId
  ) {
    carrito.getItems().remove(item);

    // Recalcular totales y guardar
    recalcularTotalesCarrito(carrito);
    carritoRepository.save(carrito);

    // TODO: Publicar evento

    log.info(
      "Item {} eliminado exitosamente del carrito {}",
      item.getId(),
      carrito.getId()
    );

    return ConfirmacionOperacionDto.builder()
      .exitoso(true)
      .mensaje("Item eliminado exitosamente del carrito")
      .build();
  }
}
