package com.nelson.project.msvc_categoria.msvc_categoria.exception;

/**
 * Excepción específica para violaciones de reglas de negocio de categorías.
 * Aplica principio de Single Responsibility para manejo de errores de dominio.
 */
public class CategoriaBusinessException extends RuntimeException {

  public CategoriaBusinessException(String message) {
    super(message);
  }

  public CategoriaBusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}
