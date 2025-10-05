package com.nelson.project.msvc_categoria.msvc_categoria.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manejador global de excepciones para el microservicio de categorías.
 * Aplica principios SOLID para manejo centralizado y consistente de errores.
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Maneja excepciones de validación de campos.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleValidationExceptions(
    MethodArgumentNotValidException ex
  ) {
    log.warn("Error de validación: {}", ex.getMessage());
    Map<String, String> fieldErrors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error ->
        fieldErrors.put(error.getField(), error.getDefaultMessage())
      );

    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation Failed");
    response.put("message", "Error en validación de campos");
    response.put("fieldErrors", fieldErrors);

    return response;
  }

  /**
   * Maneja excepciones cuando una categoría no es encontrada.
   */
  @ExceptionHandler(CategoriaNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, Object> handleCategoriaNotFoundException(
    CategoriaNotFoundException ex
  ) {
    log.warn("Categoría no encontrada: {}", ex.getMessage());
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.NOT_FOUND.value());
    response.put("error", "Not Found");
    response.put("message", ex.getMessage());

    return response;
  }

  /**
   * Maneja excepciones de reglas de negocio.
   */
  @ExceptionHandler(CategoriaBusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleCategoriaBusinessException(
    CategoriaBusinessException ex
  ) {
    log.warn("Error de regla de negocio: {}", ex.getMessage());
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Business Rule Violation");
    response.put("message", ex.getMessage());

    return response;
  }

  /**
   * Maneja cualquier otra excepción no específica.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    log.error("Error interno no manejado: {}", ex.getMessage(), ex);
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", "Ha ocurrido un error interno");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      response
    );
  }
}
