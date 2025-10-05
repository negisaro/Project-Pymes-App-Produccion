package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import java.util.Optional;

/**
 * Servicio especializado para operaciones core del carrito.
 *
 * Responsabilidades:
 * - Creación y gestión básica de carritos
 * - Validaciones de existencia y permisos
 * - Operaciones de estado del carrito
 * - Control de expiración
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface CarritoCoreService {
  /**
   * Obtiene el carrito activo para un usuario específico.
   * Si no existe, crea uno nuevo automáticamente.
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto con la información del carrito
   */
  CarritoDto obtenerCarritoPorUsuario(Long usuarioId);

  /**
   * Obtiene un carrito específico por ID validando permisos del usuario.
   *
   * @param carritoId ID del carrito
   * @param usuarioId ID del usuario para validar permisos
   * @return Optional con el carrito si existe y tiene permisos
   */
  Optional<CarritoDto> obtenerCarritoPorId(Long carritoId, Long usuarioId);

  /**
   * Crea un nuevo carrito para un usuario específico.
   *
   * @param usuarioId ID del usuario
   * @param ipCliente IP del cliente para audit trail
   * @return CarritoDto del carrito creado
   */
  CarritoDto crearCarrito(Long usuarioId, String ipCliente);

  /**
   * Marca un carrito como abandonado cuando expira.
   *
   * @param carritoId ID del carrito a marcar como abandonado
   */
  void marcarCarritoComoAbandonado(Long carritoId);

  /**
   * Actualiza la última actividad del carrito.
   *
   * @param carritoId ID del carrito
   */
  void actualizarUltimaActividad(Long carritoId);

  /**
   * Verifica si un carrito ha expirado según las reglas de negocio.
   *
   * @param carritoId ID del carrito
   * @return true si el carrito ha expirado
   */
  boolean carritoHaExpirado(Long carritoId);

  /**
   * Valida la existencia de un usuario mediante el cliente Feign.
   *
   * @param usuarioId ID del usuario a validar
   * @throws com.nelson.project.msvc_carrito.msvc_carrito.exception.BusinessException si el usuario no existe
   */
  void validarExistenciaUsuario(Long usuarioId);
}
