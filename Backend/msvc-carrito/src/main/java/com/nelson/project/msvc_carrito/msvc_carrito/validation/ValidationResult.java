package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resultado de validaciones de negocio.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

  /**
   * Indica si la validación fue exitosa.
   */
  @Builder.Default
  private Boolean isValid = true;

  /**
   * Lista de errores de validación.
   */
  @Builder.Default
  private List<ValidationError> errors = new ArrayList<>();

  /**
   * Lista de advertencias de validación.
   */
  @Builder.Default
  private List<ValidationWarning> warnings = new ArrayList<>();

  /**
   * Contexto de la validación.
   */
  private String context;

  /**
   * Timestamp de cuando se realizó la validación.
   */
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  /**
   * Agrega un error de validación.
   */
  public ValidationResult addError(String field, String message, String code) {
    this.isValid = false;
    this.errors.add(
        ValidationError.builder()
          .field(field)
          .message(message)
          .code(code)
          .build()
      );
    return this;
  }

  /**
   * Agrega una advertencia de validación.
   */
  public ValidationResult addWarning(
    String field,
    String message,
    String code
  ) {
    this.warnings.add(
        ValidationWarning.builder()
          .field(field)
          .message(message)
          .code(code)
          .build()
      );
    return this;
  }

  /**
   * Combina resultados de validación.
   */
  public ValidationResult combine(ValidationResult other) {
    if (other != null) {
      this.errors.addAll(other.getErrors());
      this.warnings.addAll(other.getWarnings());
      if (!other.getIsValid()) {
        this.isValid = false;
      }
    }
    return this;
  }

  /**
   * Establece el contexto de la validación.
   */
  public ValidationResult context(String context) {
    this.context = context;
    return this;
  }

  /**
   * Verifica si hay errores críticos.
   */
  public boolean hasCriticalErrors() {
    return errors
      .stream()
      .anyMatch(error -> error.getSeverity() == ValidationSeverity.CRITICAL);
  }

  /**
   * Obtiene el número total de errores.
   */
  public int getErrorCount() {
    return errors.size();
  }

  /**
   * Obtiene el número total de advertencias.
   */
  public int getWarningCount() {
    return warnings.size();
  }

  /**
   * Crea un resultado exitoso.
   */
  public static ValidationResult success() {
    return ValidationResult.builder().isValid(true).build();
  }

  /**
   * Crea un resultado con error.
   */
  public static ValidationResult failure(
    String field,
    String message,
    String code
  ) {
    return ValidationResult.builder()
      .isValid(false)
      .build()
      .addError(field, message, code);
  }
}
