package com.nelson.project.msvc_categoria.msvc_categoria.exception;

/**
 * Excepción específica para cuando una categoría no es encontrada.
 * Aplica principio de Single Responsibility para manejo de errores.
 */
public class CategoriaNotFoundException extends RuntimeException {

  public CategoriaNotFoundException(Long id) {
    super("Categoría no encontrada con ID: " + id);
  }

  public CategoriaNotFoundException(String message) {
    super(message);
  }

  public CategoriaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
