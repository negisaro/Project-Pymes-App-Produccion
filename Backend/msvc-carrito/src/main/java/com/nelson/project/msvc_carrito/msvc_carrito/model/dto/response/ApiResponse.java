package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO base para todas las respuestas de la API
 * REFACTORIZACIÓN PENDIENTE: Candidato para Lombok (@Data) manteniendo métodos factory
 * Cambios aplicados: Nombres uniformizados, documentación mejorada, constructores optimizados
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta base de la API")
public class ApiResponse<T> {

  @Schema(description = "Indica si la operación fue exitosa", example = "true")
  private boolean exitoso;

  @Schema(
    description = "Mensaje descriptivo de la operación",
    example = "Operación completada exitosamente"
  )
  private String mensaje;

  @Schema(
    description = "Código de error en caso de fallo",
    example = "CARRITO_NOT_FOUND"
  )
  private String codigoError;

  @Schema(description = "Datos de respuesta")
  private T datos;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Timestamp de la respuesta",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime timestamp;

  @Schema(
    description = "ID de seguimiento de la operación",
    example = "op-12345"
  )
  private String traceId;

  // Constructores
  public ApiResponse() {
    this.timestamp = LocalDateTime.now();
  }

  public ApiResponse(boolean exitoso, String mensaje) {
    this.exitoso = exitoso;
    this.mensaje = mensaje;
    this.timestamp = LocalDateTime.now();
  }

  public ApiResponse(boolean exitoso, String mensaje, T datos) {
    this.exitoso = exitoso;
    this.mensaje = mensaje;
    this.datos = datos;
    this.timestamp = LocalDateTime.now();
  }

  // Métodos estáticos para facilitar la creación
  public static <T> ApiResponse<T> exitoso(T datos) {
    return new ApiResponse<>(true, "Operación exitosa", datos);
  }

  public static <T> ApiResponse<T> exitoso(String mensaje, T datos) {
    return new ApiResponse<>(true, mensaje, datos);
  }

  public static <T> ApiResponse<T> error(String mensaje) {
    return new ApiResponse<>(false, mensaje);
  }

  public static <T> ApiResponse<T> error(String mensaje, String codigoError) {
    ApiResponse<T> response = new ApiResponse<>(false, mensaje);
    response.setCodigoError(codigoError);
    return response;
  }

  // Getters y Setters
  public boolean isExitoso() {
    return exitoso;
  }

  public void setExitoso(boolean exitoso) {
    this.exitoso = exitoso;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public String getCodigoError() {
    return codigoError;
  }

  public void setCodigoError(String codigoError) {
    this.codigoError = codigoError;
  }

  public T getDatos() {
    return datos;
  }

  public void setDatos(T datos) {
    this.datos = datos;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  @Override
  public String toString() {
    return (
      "ApiResponse{" +
      "exitoso=" +
      exitoso +
      ", mensaje='" +
      mensaje +
      '\'' +
      ", codigoError='" +
      codigoError +
      '\'' +
      ", timestamp=" +
      timestamp +
      ", traceId='" +
      traceId +
      '\'' +
      '}'
    );
  }
}
