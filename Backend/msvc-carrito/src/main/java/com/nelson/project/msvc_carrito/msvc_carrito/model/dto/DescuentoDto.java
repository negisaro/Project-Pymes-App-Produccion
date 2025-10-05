package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para gestión de descuentos aplicados al carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoDto {

  /**
   * ID único del descuento.
   */
  private Long id;

  /**
   * Código del descuento.
   */
  private String codigo;

  /**
   * Nombre descriptivo del descuento.
   */
  private String nombre;

  /**
   * Descripción detallada del descuento.
   */
  private String descripcion;

  /**
   * Tipo de descuento (PORCENTAJE, MONTO_FIJO, ENVIO_GRATIS, etc.).
   */
  private String tipoDescuento;

  /**
   * Valor del descuento.
   */
  private BigDecimal valor;

  /**
   * Porcentaje del descuento (si aplica).
   */
  private BigDecimal porcentaje;

  /**
   * Monto mínimo requerido para aplicar el descuento.
   */
  private BigDecimal montoMinimo;

  /**
   * Monto máximo de descuento aplicable.
   */
  private BigDecimal montoMaximo;

  /**
   * Fecha de inicio de vigencia del descuento.
   */
  private LocalDateTime fechaInicio;

  /**
   * Fecha de fin de vigencia del descuento.
   */
  private LocalDateTime fechaFin;

  /**
   * Indica si el descuento está activo.
   */
  @Builder.Default
  private Boolean activo = true;

  /**
   * Número máximo de usos del descuento.
   */
  private Integer usoMaximo;

  /**
   * Número actual de usos del descuento.
   */
  @Builder.Default
  private Integer usoActual = 0;

  /**
   * Indica si el descuento es de un solo uso por usuario.
   */
  @Builder.Default
  private Boolean usoUnicoPorUsuario = false;

  /**
   * Lista de categorías a las que aplica el descuento.
   */
  private String categoriasAplicables;

  /**
   * Lista de productos específicos a los que aplica.
   */
  private String productosAplicables;

  /**
   * ID del usuario que creó el descuento.
   */
  private Long creadoPor;

  /**
   * Fecha de creación del descuento.
   */
  @Builder.Default
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  /**
   * Fecha de última modificación.
   */
  private LocalDateTime fechaModificacion;

  /**
   * Monto del descuento aplicado (calculado).
   */
  private BigDecimal montoDescuento;

  /**
   * Indica si el descuento fue aplicado automáticamente.
   */
  @Builder.Default
  private Boolean aplicadoAutomaticamente = false;

  /**
   * Prioridad del descuento (para conflictos).
   */
  @Builder.Default
  private Integer prioridad = 1;

  /**
   * Reglas adicionales del descuento en formato JSON.
   */
  private String reglasAdicionales;
}
