package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO base para todas las respuestas de la API
 * MIGRADO A LOMBOK: Eliminado código boilerplate, mantenidos factory methods
 * IMPLEMENTA: Builder pattern y factory methods optimizados
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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

  @Builder.Default
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(
    description = "Timestamp de la respuesta",
    example = "2024-01-15T10:30:00"
  )
  private LocalDateTime timestamp = LocalDateTime.now();

  @Schema(
    description = "ID de seguimiento de la operación",
    example = "op-12345"
  )
  private String traceId;

  // ================================
  // FACTORY METHODS OPTIMIZADOS
  // ================================

  /**
   * Crea una respuesta exitosa con datos
   */
  public static <T> ApiResponse<T> exitoso(T datos) {
    return ApiResponse.<T>builder()
      .exitoso(true)
      .mensaje("Operación exitosa")
      .datos(datos)
      .build();
  }

  /**
   * Crea una respuesta exitosa con mensaje personalizado y datos
   */
  public static <T> ApiResponse<T> exitoso(String mensaje, T datos) {
    return ApiResponse.<T>builder()
      .exitoso(true)
      .mensaje(mensaje)
      .datos(datos)
      .build();
  }

  /**
   * Crea una respuesta de error con mensaje
   */
  public static <T> ApiResponse<T> error(String mensaje) {
    return ApiResponse.<T>builder().exitoso(false).mensaje(mensaje).build();
  }

  /**
   * Crea una respuesta de error con mensaje y código de error
   */
  public static <T> ApiResponse<T> error(String mensaje, String codigoError) {
    return ApiResponse.<T>builder()
      .exitoso(false)
      .mensaje(mensaje)
      .codigoError(codigoError)
      .build();
  }

  /**
   * Crea una respuesta de error con mensaje, código y traceId
   */
  public static <T> ApiResponse<T> errorConTrace(
    String mensaje,
    String codigoError,
    String traceId
  ) {
    return ApiResponse.<T>builder()
      .exitoso(false)
      .mensaje(mensaje)
      .codigoError(codigoError)
      .traceId(traceId)
      .build();
  }

  /**
   * Crea una respuesta exitosa sin datos
   */
  public static <T> ApiResponse<T> exitosoSinDatos(String mensaje) {
    return ApiResponse.<T>builder().exitoso(true).mensaje(mensaje).build();
  }
}
