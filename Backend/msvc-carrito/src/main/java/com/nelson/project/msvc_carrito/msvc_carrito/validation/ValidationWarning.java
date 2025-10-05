package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una advertencia de validación.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationWarning {

  /**
   * Campo relacionado con la advertencia.
   */
  private String field;

  /**
   * Mensaje de advertencia.
   */
  private String message;

  /**
   * Código de advertencia.
   */
  private String code;

  /**
   * Valor que generó la advertencia.
   */
  private Object value;

  /**
   * Recomendación para resolver la advertencia.
   */
  private String recommendation;
}
