package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de error estandarizadas en el API REST.
 *
 * Proporciona un formato consistente para todos los errores del sistema,
 * incluyendo información detallada para debugging y mensajes user-friendly.
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta estándar para errores del API")
public class ErrorResponseDto {

  @Schema(
    description = "Timestamp del error en formato ISO 8601",
    example = "2024-01-15T10:30:45"
  )
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime timestamp;

  @Schema(description = "Código de estado HTTP", example = "400")
  private Integer status;

  @Schema(description = "Nombre del error HTTP", example = "Bad Request")
  private String error;

  @Schema(
    description = "Mensaje principal del error",
    example = "Datos de entrada inválidos"
  )
  private String message;

  @Schema(
    description = "Código de error interno del sistema",
    example = "CART_001"
  )
  private String errorCode;

  @Schema(description = "Descripción detallada del error para debugging")
  private String details;

  @Schema(
    description = "Ruta del endpoint donde ocurrió el error",
    example = "/api/v1/carrito/123/items"
  )
  private String path;

  @Schema(description = "Método HTTP utilizado", example = "POST")
  private String method;

  @Schema(description = "Lista de errores de validación específicos")
  private List<FieldErrorDto> fieldErrors;

  @Schema(description = "Parámetros adicionales relacionados con el error")
  private Map<String, Object> parameters;

  @Schema(
    description = "ID de correlación para trazabilidad",
    example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  )
  private String correlationId;

  @Schema(description = "Sugerencias para resolver el error")
  private List<String> suggestions;

  @Schema(description = "Enlaces a documentación relacionada")
  private List<String> documentationLinks;

  /**
   * DTO para errores específicos de campos en validaciones.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Error específico de un campo en validación")
  public static class FieldErrorDto {

    @Schema(description = "Nombre del campo con error", example = "cantidad")
    private String field;

    @Schema(description = "Valor rechazado", example = "-1")
    private Object rejectedValue;

    @Schema(
      description = "Mensaje de error del campo",
      example = "La cantidad debe ser mayor a 0"
    )
    private String message;

    @Schema(
      description = "Código de error específico del campo",
      example = "FIELD_MIN_VALUE"
    )
    private String errorCode;
  }

  /**
   * Factory method para crear errores de validación.
   */
  public static ErrorResponseDto validationError(
    String message,
    List<FieldErrorDto> fieldErrors
  ) {
    return ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(400)
      .error("Bad Request")
      .message(message)
      .errorCode("VALIDATION_ERROR")
      .fieldErrors(fieldErrors)
      .build();
  }

  /**
   * Factory method para crear errores de negocio.
   */
  public static ErrorResponseDto businessError(
    String message,
    String errorCode,
    String details
  ) {
    return ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(409)
      .error("Business Rule Violation")
      .message(message)
      .errorCode(errorCode)
      .details(details)
      .build();
  }

  /**
   * Factory method para crear errores de recurso no encontrado.
   */
  public static ErrorResponseDto notFoundError(
    String message,
    String errorCode
  ) {
    return ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(404)
      .error("Not Found")
      .message(message)
      .errorCode(errorCode)
      .build();
  }

  /**
   * Factory method para crear errores internos del servidor.
   */
  public static ErrorResponseDto internalServerError(
    String message,
    String correlationId
  ) {
    return ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(500)
      .error("Internal Server Error")
      .message(message)
      .errorCode("INTERNAL_ERROR")
      .correlationId(correlationId)
      .build();
  }
}
