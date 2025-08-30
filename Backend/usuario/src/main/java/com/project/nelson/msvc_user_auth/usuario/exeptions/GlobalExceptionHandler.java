package com.project.nelson.msvc_user_auth.usuario.exeptions;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(StackOverflowError.class)
  public ResponseEntity<?> handleStackOverflow(StackOverflowError ex) {
    return ResponseEntity.internalServerError()
      .body(
        Map.of(
          "error",
          "Error interno: recursividad infinita detectada",
          "detalle",
          ex.getMessage()
        )
      );
  }

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
    return ResponseEntity.badRequest().body(Map.of("validationErrors", errors));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralError(Exception ex) {
    return ResponseEntity.internalServerError()
      .body(
        Map.of(
          "error",
          "Error inesperado en la aplicación",
          "detalle",
          ex.getMessage()
        )
      );
  }
}
