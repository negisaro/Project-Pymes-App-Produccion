package com.nelson.project.msvc_usuario.msvc_usuario.exception;

import lombok.Getter;

/**
 * Excepción personalizada empresarial para el MSVC de usuario.
 * Permite manejar códigos de estado y mensajes personalizados.
 */
@Getter
public class CustomException extends RuntimeException {

  /** Código de estado HTTP asociado al error. */
  private final int status;
  /** Código de error estándar para el frontend. */
  private final String error;
  /** Detalle técnico opcional para debugging. */
  private final String detalle;

  /**
   * Constructor con mensaje, status y código de error.
   * @param message Mensaje para el usuario
   * @param status Código de estado HTTP
   * @param error Código de error estándar
   */
  public CustomException(String message, int status, String error) {
    super(message);
    this.status = status;
    this.error = error;
    this.detalle = null;
  }

  /**
   * Constructor con mensaje, status, código de error y detalle técnico.
   * @param message Mensaje para el usuario
   * @param status Código de estado HTTP
   * @param error Código de error estándar
   * @param detalle Detalle técnico opcional
   */
  public CustomException(
    String message,
    int status,
    String error,
    String detalle
  ) {
    super(message);
    this.status = status;
    this.error = error;
    this.detalle = detalle;
  }

  /**
   * Constructor con mensaje y status (error genérico).
   * @param message Mensaje para el usuario
   * @param status Código de estado HTTP
   */
  public CustomException(String message, int status) {
    super(message);
    this.status = status;
    this.error = null;
    this.detalle = null;
  }

  /**
   * Constructor solo con mensaje (status 500, error nulo).
   * @param message Mensaje para el usuario
   */
  public CustomException(String message) {
    super(message);
    this.status = 500;
    this.error = null;
    this.detalle = null;
  }

  @Override
  public String toString() {
    return (
      "CustomException{" +
      "status=" +
      status +
      ", error='" +
      error +
      '\'' +
      ", message='" +
      getMessage() +
      '\'' +
      (detalle != null ? ", detalle='" + detalle + '\'' : "") +
      '}'
    );
  }
}
