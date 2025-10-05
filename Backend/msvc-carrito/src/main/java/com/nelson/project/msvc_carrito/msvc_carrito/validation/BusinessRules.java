package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;

/**
 * Reglas de negocio para validaciones del carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface BusinessRules {
  /**
   * Valida un carrito completo.
   */
  ValidationResult validarCarrito(CarritoDto carrito);

  /**
   * Valida la creación de un nuevo carrito.
   */
  ValidationResult validarCreacionCarrito(Long usuarioId, String ipCliente);

  /**
   * Valida la adición de un item al carrito.
   */
  ValidationResult validarAgregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    Long usuarioId
  );

  /**
   * Valida la actualización de cantidad de un item.
   */
  ValidationResult validarActualizarCantidad(
    Long carritoId,
    Long itemId,
    Integer nuevaCantidad,
    Long usuarioId
  );

  /**
   * Valida la aplicación de un descuento.
   */
  ValidationResult validarAplicarDescuento(
    Long carritoId,
    String codigoDescuento,
    Long usuarioId
  );

  /**
   * Valida los límites del carrito.
   */
  ValidationResult validarLimitesCarrito(CarritoDto carrito);

  /**
   * Valida la disponibilidad de stock.
   */
  ValidationResult validarStock(Long productoId, Integer cantidadSolicitada);

  /**
   * Valida permisos del usuario sobre el carrito.
   */
  ValidationResult validarPermisos(Long carritoId, Long usuarioId);

  /**
   * Valida que un item sea válido.
   */
  ValidationResult validarItem(ItemCarritoDto item);

  /**
   * Valida reglas de negocio específicas del dominio.
   */
  ValidationResult validarReglasNegocio(CarritoDto carrito, String operacion);
}
