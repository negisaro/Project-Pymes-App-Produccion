package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ConfirmacionOperacionDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.DescuentoDto;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio especializado para gestión de descuentos y promociones del carrito.
 *
 * Responsabilidades:
 * - Aplicación y remoción de códigos de descuento
 * - Validación de promociones y cupones
 * - Cálculo de descuentos automáticos
 * - Gestión de reglas de negocio para ofertas
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface CarritoDescuentoService {
  /**
   * Aplica un código de descuento al carrito.
   *
   * @param carritoId ID del carrito
   * @param codigoDescuento Código del descuento a aplicar
   * @param usuarioId ID del usuario para validaciones
   * @return CarritoDto con el descuento aplicado
   */
  CarritoDto aplicarCodigoDescuento(
    Long carritoId,
    String codigoDescuento,
    Long usuarioId
  );

  /**
   * Remueve un descuento aplicado del carrito.
   *
   * @param carritoId ID del carrito
   * @param descuentoId ID del descuento a remover
   * @param usuarioId ID del usuario para validaciones
   * @return ConfirmacionOperacionDto con el resultado
   */
  ConfirmacionOperacionDto removerDescuento(
    Long carritoId,
    Long descuentoId,
    Long usuarioId
  );

  /**
   * Valida si un código de descuento es válido y aplicable.
   *
   * @param codigoDescuento Código a validar
   * @param carritoId ID del carrito
   * @param usuarioId ID del usuario
   * @return DescuentoDto con información del descuento si es válido
   */
  DescuentoDto validarCodigoDescuento(
    String codigoDescuento,
    Long carritoId,
    Long usuarioId
  );

  /**
   * Obtiene todos los descuentos aplicados a un carrito.
   *
   * @param carritoId ID del carrito
   * @return Lista de DescuentoDto aplicados
   */
  List<DescuentoDto> obtenerDescuentosAplicados(Long carritoId);

  /**
   * Calcula descuentos automáticos basados en reglas de negocio.
   *
   * @param carritoId ID del carrito
   * @return CarritoDto con descuentos automáticos aplicados
   */
  CarritoDto calcularDescuentosAutomaticos(Long carritoId);

  /**
   * Calcula el total de descuentos aplicados al carrito.
   *
   * @param carritoId ID del carrito
   * @return BigDecimal con el total de descuentos
   */
  BigDecimal calcularTotalDescuentos(Long carritoId);

  /**
   * Obtiene descuentos disponibles para un usuario específico.
   *
   * @param usuarioId ID del usuario
   * @return Lista de DescuentoDto disponibles
   */
  List<DescuentoDto> obtenerDescuentosDisponibles(Long usuarioId);

  /**
   * Verifica si un descuento específico se puede aplicar al carrito.
   *
   * @param descuentoId ID del descuento
   * @param carritoId ID del carrito
   * @return true si el descuento es aplicable
   */
  boolean esDescuentoAplicable(Long descuentoId, Long carritoId);

  /**
   * Recalcula todos los descuentos del carrito tras cambios en items.
   *
   * @param carritoId ID del carrito
   * @return CarritoDto con descuentos recalculados
   */
  CarritoDto recalcularDescuentos(Long carritoId);
}
