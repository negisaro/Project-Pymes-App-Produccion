package com.nelson.project.msvc_carrito.msvc_carrito.validation;

/**
 * Niveles de severidad para validaciones.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public enum ValidationSeverity {
  /**
   * Información general.
   */
  INFO,

  /**
   * Advertencia que no impide la operación.
   */
  WARNING,

  /**
   * Error que impide la operación.
   */
  ERROR,

  /**
   * Error crítico que puede afectar el sistema.
   */
  CRITICAL,
}
