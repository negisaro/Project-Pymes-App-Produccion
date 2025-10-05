package com.nelson.project.msvc_categoria.msvc_categoria.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO estándar para respuestas de API empresarial.
 * Proporciona estructura consistente para todas las respuestas del microservicio.
 * 
 * @param <T> Tipo de datos contenidos en la respuesta
 * 
 * @author Nelson Laza
 * @since 1.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estructura estándar de respuesta para todas las APIs del microservicio")
public class ApiResponse<T> {

    /**
     * Indicador de éxito de la operación
     */
    @NotNull
    @Schema(description = "Indica si la operación fue exitosa", example = "true", required = true)
    private Boolean success;

    /**
     * Mensaje descriptivo de la respuesta
     */
    @Schema(description = "Mensaje descriptivo del resultado", example = "Operación completada exitosamente")
    private String message;

    /**
     * Código de respuesta específico de la aplicación
     */
    @Schema(description = "Código específico de la aplicación", example = "CATEGORIA_CREATED")
    private String code;

    /**
     * Datos principales de la respuesta
     */
    @Schema(description = "Datos principales de la respuesta")
    private T data;

    /**
     * Metadatos adicionales de la respuesta
     */
    @Schema(description = "Metadatos adicionales como paginación, contadores, etc.")
    private Map<String, Object> metadata;

    /**
     * Lista de errores de validación o advertencias
     */
    @Schema(description = "Lista de errores o advertencias")
    private List<ErrorDetail> errors;

    /**
     * Enlaces HATEOAS para navegación
     */
    @Schema(description = "Enlaces relacionados siguiendo principios HATEOAS")
    private Map<String, String> links;

    /**
     * Timestamp de la respuesta
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp de cuando se generó la respuesta", example = "2025-10-01T14:30:00")
    private LocalDateTime timestamp;

    /**
     * ID de traza para seguimiento en logs
     */
    @Schema(description = "ID de traza para seguimiento en logs", example = "550e8400-e29b-41d4-a716-446655440000")
    private String traceId;

    /**
     * Versión de la API utilizada
     */
    @Schema(description = "Versión de la API", example = "1.0.0")
    private String apiVersion;

    /**
     * Información del servidor que procesó la request
     */
    @Schema(description = "Información del servidor", example = "msvc-categoria-v1.0.0")
    private String server;

    /**
     * Crea una respuesta exitosa con datos
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Operación completada exitosamente")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta exitosa con datos y mensaje personalizado
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta exitosa con datos, mensaje y código
     */
    public static <T> ApiResponse<T> success(T data, String message, String code) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .code(code)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con mensaje
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con mensaje y código
     */
    public static <T> ApiResponse<T> error(String message, String code) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con lista de errores
     */
    public static <T> ApiResponse<T> error(String message, List<ErrorDetail> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Añade metadatos a la respuesta
     */
    public ApiResponse<T> withMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Añade enlaces HATEOAS a la respuesta
     */
    public ApiResponse<T> withLinks(Map<String, String> links) {
        this.links = links;
        return this;
    }

    /**
     * Añade información de traza
     */
    public ApiResponse<T> withTrace(String traceId) {
        this.traceId = traceId;
        return this;
    }

    /**
     * Añade información de versión de API
     */
    public ApiResponse<T> withApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    /**
     * Añade información del servidor
     */
    public ApiResponse<T> withServer(String server) {
        this.server = server;
        return this;
    }
}