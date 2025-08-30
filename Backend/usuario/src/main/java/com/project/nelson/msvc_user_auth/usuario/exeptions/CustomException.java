package com.project.nelson.msvc_user_auth.usuario.exeptions;

/**
 * Excepción personalizada empresarial para el MSVC de usuario.
 * Permite manejar códigos de estado y mensajes personalizados.
 */
public class CustomException extends RuntimeException {

  public CustomException(String message) {
    super(message);
    this.status = 500;
    this.error = null;
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
      '}'
    );
  }

  private int status;
  private String error;

  public CustomException(String message, int status, String error) {
    super(message);
    this.status = status;
    this.error = error;
  }

  public CustomException(String message, int status) {
    super(message);
    this.status = status;
    this.error = null;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }
}
