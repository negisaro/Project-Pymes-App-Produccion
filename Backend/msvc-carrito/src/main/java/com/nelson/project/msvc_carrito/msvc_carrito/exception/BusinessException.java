package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción base para errores de negocio en el microservicio de carrito.
 * Proporciona estructura común para manejo de errores empresariales.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String errorCode;
  private final Object[] parameters;

  public BusinessException(String mensaje) {
    super(mensaje);
    this.errorCode = "BUSINESS_ERROR";
    this.parameters = new Object[0];
  }

  public BusinessException(String mensaje, String errorCode) {
    super(mensaje);
    this.errorCode = errorCode;
    this.parameters = new Object[0];
  }

  public BusinessException(
    String mensaje,
    String errorCode,
    Object... parameters
  ) {
    super(mensaje);
    this.errorCode = errorCode;
    this.parameters = parameters;
  }

  public BusinessException(String mensaje, Throwable causa) {
    super(mensaje, causa);
    this.errorCode = "BUSINESS_ERROR";
    this.parameters = new Object[0];
  }

  public BusinessException(String mensaje, String errorCode, Throwable causa) {
    super(mensaje, causa);
    this.errorCode = errorCode;
    this.parameters = new Object[0];
  }

  public String getErrorCode() {
    return errorCode;
  }

  public Object[] getParameters() {
    return parameters != null ? parameters.clone() : new Object[0];
  }

  /**
   * Obtiene detalles adicionales del error para el manejo de excepciones.
   * Por defecto retorna null, las subclases pueden sobrescribir.
   */
  public String getDetails() {
    return null;
  }

  @Override
  public String toString() {
    return String.format(
      "BusinessException{errorCode='%s', message='%s'}",
      errorCode,
      getMessage()
    );
  }
}
