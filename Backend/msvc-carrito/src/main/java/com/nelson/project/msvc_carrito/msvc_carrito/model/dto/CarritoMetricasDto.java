package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para métricas y KPIs del carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoMetricasDto {

  /**
   * ID del usuario para el cual se generan las métricas.
   */
  private Long usuarioId;

  /**
   * Número total de carritos creados.
   */
  private Integer totalCarritosCreados;

  /**
   * Número de carritos completados (convertidos en compras).
   */
  private Integer carritoCompletados;

  /**
   * Número de carritos abandonados.
   */
  private Integer carritosAbandonados;

  /**
   * Tasa de conversión (completados/creados).
   */
  private BigDecimal tasaConversion;

  /**
   * Valor promedio de los carritos.
   */
  private BigDecimal valorPromedioCarrito;

  /**
   * Valor total de todos los carritos.
   */
  private BigDecimal valorTotalCarritos;

  /**
   * Tiempo promedio de vida de un carrito (en horas).
   */
  private Double tiempoPromedioVidaCarrito;

  /**
   * Número promedio de items por carrito.
   */
  private Double promedioItemsPorCarrito;

  /**
   * Categoría de producto más agregada.
   */
  private String categoriaMasAgregada;

  /**
   * Hora del día con más actividad de carrito.
   */
  private Integer horaPicoActividad;

  /**
   * Día de la semana con más actividad.
   */
  private String diaPicoActividad;

  /**
   * Período de análisis desde.
   */
  private LocalDateTime periodoDesde;

  /**
   * Período de análisis hasta.
   */
  private LocalDateTime periodoHasta;

  /**
   * Timestamp de generación de las métricas.
   */
  @Builder.Default
  private LocalDateTime fechaGeneracion = LocalDateTime.now();

  /**
   * Número de productos únicos agregados.
   */
  private Integer productosUnicosAgregados;

  /**
   * Valor del carrito actual.
   */
  private BigDecimal valorCarritoActual;

  /**
   * Ahorro total por descuentos aplicados.
   */
  private BigDecimal ahorroTotalDescuentos;
}
