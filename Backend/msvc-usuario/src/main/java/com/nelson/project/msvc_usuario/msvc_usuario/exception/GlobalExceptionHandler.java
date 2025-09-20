package com.nelson.project.msvc_usuario.msvc_usuario.exception;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para el MSVC de usuario.
 * Centraliza el manejo de errores, formatea las respuestas y asegura trazabilidad en consola.
 * Captura errores de validación, de negocio (CustomException), errores generales y de stack overflow.
 * Todos los errores relevantes quedan registrados en consola y en la respuesta HTTP.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(
    GlobalExceptionHandler.class
  );

  /**
   * Maneja errores de recursividad infinita (StackOverflowError).
   * @param ex excepción capturada
   * @return respuesta HTTP 500 con detalles del error
   */
  @ExceptionHandler(StackOverflowError.class)
  public ResponseEntity<?> handleStackOverflow(StackOverflowError ex) {
    return ResponseEntity.status(500).body(
      Map.of(
        "status",
        500,
        "error",
        "STACK_OVERFLOW",
        "message",
        "Error interno: recursividad infinita detectada",
        "detalle",
        ex.getMessage()
      )
    );
  }

  /**
   * Maneja errores de validación de argumentos en los controladores (DTOs).
   * @param ex excepción de validación
   * @return respuesta HTTP 400 con detalles de los campos inválidos
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationError(
    MethodArgumentNotValidException ex
  ) {
    Map<String, Object> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
      );
    logger.warn("[GlobalExceptionHandler] Validation error: {}", errors);
    return ResponseEntity.badRequest()
      .body(
        Map.of(
          "status",
          400,
          "error",
          "VALIDATION_ERROR",
          "message",
          "Error de validación en uno o más campos.",
          "validationErrors",
          errors
        )
      );
  }

  /**
   * Maneja excepciones de negocio personalizadas y responde con status, error y mensaje.
   */
  /**
   * Maneja excepciones de negocio personalizadas lanzadas por servicios y controladores.
   * @param ex excepción personalizada
   * @return respuesta HTTP con status, error y mensaje de negocio
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException ex) {
    logger.warn(
      "[GlobalExceptionHandler] CustomException: status={}, error={}, message={}, detalle={}",
      ex.getStatus(),
      ex.getError(),
      ex.getMessage(),
      ex.getDetalle()
    );
    int status = ex.getStatus() > 0 ? ex.getStatus() : 500;
    String error = ex.getError() != null ? ex.getError() : "BUSINESS_ERROR";
    Map<String, Object> body = new HashMap<>();
    body.put("status", status);
    body.put("error", error);
    body.put("message", ex.getMessage());
    if (ex.getDetalle() != null) {
      body.put("detalle", ex.getDetalle());
    }
    return ResponseEntity.status(status).body(body);
  }

  /**
   * Maneja cualquier excepción no controlada en la aplicación.
   * @param ex excepción genérica
   * @return respuesta HTTP 500 con mensaje de error interno
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralError(Exception ex) {
    logger.error("[GlobalExceptionHandler] Excepción no controlada", ex);
    return ResponseEntity.status(500).body(
      Map.of(
        "status",
        500,
        "error",
        "INTERNAL_ERROR",
        "message",
        "Error inesperado en la aplicación",
        "detalle",
        ex.getMessage()
      )
    );
  }

  /**
   * Maneja excepciones de inicialización de bootstrap (roles, admin, etc).
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleBootstrapException(RuntimeException ex) {
    if (ex.getMessage() != null && ex.getMessage().contains("BOOTSTRAP")) {
      logger.error(
        "[GlobalExceptionHandler] Error en bootstrap: {}",
        ex.getMessage(),
        ex
      );
      return ResponseEntity.status(500).body(
        Map.of(
          "status",
          500,
          "error",
          "BOOTSTRAP_ERROR",
          "message",
          ex.getMessage(),
          "detalle",
          ex.getCause() != null ? ex.getCause().getMessage() : null
        )
      );
    }
    // Si no es de bootstrap, delegar al handler general
    return handleGeneralError(ex);
  }
}
