package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ConfirmacionOperacionDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;
import java.util.List;

/**
 * Servicio especializado para gestión de items del carrito.
 *
 * Responsabilidades:
 * - Agregar, actualizar y eliminar items
 * - Validaciones de cantidad y disponibilidad
 * - Gestión de promociones a nivel de item
 * - Sincronización con inventario
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface CarritoItemService {
  /**
   * Agrega un item al carrito con validaciones completas.
   *
   * @param carritoId ID del carrito
   * @param productoId ID del producto
   * @param cantidad Cantidad a agregar
   * @param usuarioId ID del usuario para validaciones
   * @return CarritoDto actualizado
   */
  CarritoDto agregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    Long usuarioId
  );

  /**
   * Actualiza la cantidad de un item específico en el carrito.
   *
   * @param carritoId ID del carrito
   * @param itemId ID del item a actualizar
   * @param nuevaCantidad Nueva cantidad
   * @param usuarioId ID del usuario para validaciones
   * @return CarritoDto actualizado
   */
  CarritoDto actualizarCantidadItem(
    Long carritoId,
    Long itemId,
    Integer nuevaCantidad,
    Long usuarioId
  );

  /**
   * Elimina un item específico del carrito.
   *
   * @param carritoId ID del carrito
   * @param itemId ID del item a eliminar
   * @param usuarioId ID del usuario para validaciones
   * @return ConfirmacionOperacionDto con el resultado
   */
  ConfirmacionOperacionDto eliminarItem(
    Long carritoId,
    Long itemId,
    Long usuarioId
  );

  /**
   * Elimina todos los items del carrito (vaciar carrito).
   *
   * @param carritoId ID del carrito
   * @param usuarioId ID del usuario para validaciones
   * @return ConfirmacionOperacionDto con el resultado
   */
  ConfirmacionOperacionDto vaciarCarrito(Long carritoId, Long usuarioId);

  /**
   * Obtiene todos los items de un carrito con información enriquecida.
   *
   * @param carritoId ID del carrito
   * @param usuarioId ID del usuario para validaciones
   * @return Lista de ItemCarritoDto
   */
  List<ItemCarritoDto> obtenerItemsCarrito(Long carritoId, Long usuarioId);

  /**
   * Valida la disponibilidad de stock para un producto específico.
   *
   * @param productoId ID del producto
   * @param cantidadSolicitada Cantidad solicitada
   * @return true si hay stock suficiente
   */
  boolean validarDisponibilidadStock(
    Long productoId,
    Integer cantidadSolicitada
  );

  /**
   * Sincroniza los precios de todos los items del carrito con el catálogo actual.
   *
   * @param carritoId ID del carrito
   * @return CarritoDto con precios actualizados
   */
  CarritoDto sincronizarPrecios(Long carritoId);

  /**
   * Aplica promociones automáticas a los items del carrito.
   *
   * @param carritoId ID del carrito
   * @return CarritoDto con promociones aplicadas
   */
  CarritoDto aplicarPromocionesAutomaticas(Long carritoId);
}
