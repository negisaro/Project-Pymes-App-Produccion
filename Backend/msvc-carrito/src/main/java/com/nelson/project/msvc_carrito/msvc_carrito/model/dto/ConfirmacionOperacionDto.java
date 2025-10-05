package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para confirmación de operaciones en el carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmacionOperacionDto {

  /**
   * Indica si la operación fue exitosa.
   */
  private Boolean exitoso;

  /**
   * Mensaje descriptivo de la operación.
   */
  private String mensaje;

  /**
   * Código de la operación para tracking.
   */
  private String codigoOperacion;

  /**
   * Timestamp de cuando se realizó la operación.
   */
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  /**
   * Información adicional de la operación (opcional).
   */
  private String detalleAdicional;

  /**
   * Impacto monetario de la operación (si aplica).
   */
  private BigDecimal impactoMonetario;

  /**
   * ID de la entidad afectada por la operación.
   */
  private Long entidadAfectadaId;

  /**
   * Tipo de la entidad afectada (carrito, item, descuento, etc.).
   */
  private String tipoEntidadAfectada;
}
