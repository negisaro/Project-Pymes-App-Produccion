package com.nelson.project.msvc_categoria.msvc_categoria.exception;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Filtro global para capturar y manejar excepciones relacionadas con JWT.
 */
@ControllerAdvice
@RestController
public class GlobalJwtExceptionFilter {

  private static final Logger logger = LoggerFactory.getLogger(
    GlobalJwtExceptionFilter.class
  );

  @ExceptionHandler(JwtException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<String> handleJwtException(JwtException ex) {
    logger.warn("JWT inválido o expirado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      "Token JWT inválido o expirado: " + ex.getMessage()
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleOtherExceptions(Exception ex) {
    logger.error("Error inesperado en autenticación JWT: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      "Error interno en autenticación: " + ex.getMessage()
    );
  }
}
