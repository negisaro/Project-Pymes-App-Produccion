package com.nelson.project.msvc_categoria.msvc_categoria.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * DTO para detalles de errores en respuestas de API.
 * Proporciona información específica sobre errores de validación o procesamiento.
 * 
 * @author Nelson Laza
 * @since 1.0.0
 */
@Data
@Builder
@Schema(description = "Detalle específico de un error en la respuesta")
public class ErrorDetail {

    /**
     * Campo específico donde ocurrió el error (para errores de validación)
     */
    @Schema(description = "Campo donde ocurrió el error", example = "nombre")
    private String field;

    /**
     * Código específico del error
     */
    @Schema(description = "Código específico del error", example = "FIELD_REQUIRED")
    private String code;

    /**
     * Mensaje descriptivo del error
     */
    @Schema(description = "Mensaje descriptivo del error", example = "El campo nombre es obligatorio")
    private String message;

    /**
     * Valor rechazado que causó el error
     */
    @Schema(description = "Valor que causó el error", example = "")
    private Object rejectedValue;

    /**
     * Ubicación del error en la estructura de datos
     */
    @Schema(description = "Ubicación del error", example = "categoria.nombre")
    private String location;

    /**
     * Crea un ErrorDetail para validación de campo
     */
    public static ErrorDetail fieldError(String field, String message, Object rejectedValue) {
        return ErrorDetail.builder()
                .field(field)
                .message(message)
                .rejectedValue(rejectedValue)
                .code("VALIDATION_ERROR")
                .build();
    }

    /**
     * Crea un ErrorDetail para error de negocio
     */
    public static ErrorDetail businessError(String code, String message) {
        return ErrorDetail.builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * Crea un ErrorDetail para error de sistema
     */
    public static ErrorDetail systemError(String message) {
        return ErrorDetail.builder()
                .code("SYSTEM_ERROR")
                .message(message)
                .build();
    }
}