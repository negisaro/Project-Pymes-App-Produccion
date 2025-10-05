package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un error de validación.
 * 
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    /**
     * Campo que falló la validación.
     */
    private String field;

    /**
     * Mensaje de error.
     */
    private String message;

    /**
     * Código de error.
     */
    private String code;

    /**
     * Severidad del error.
     */
    @Builder.Default
    private ValidationSeverity severity = ValidationSeverity.ERROR;

    /**
     * Valor que causó el error.
     */
    private Object rejectedValue;

    /**
     * Información adicional del error.
     */
    private String additionalInfo;
}