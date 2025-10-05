package com.nelson.project.msvc_carrito.msvc_carrito.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para análisis detallado de carrito (Business Intelligence).
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoAnalyticsDto {

  /**
   * ID del carrito analizado.
   */
  private Long carritoId;

  /**
   * ID del usuario propietario del carrito.
   */
  private Long usuarioId;

  /**
   * Estado actual del carrito.
   */
  private String estado;

  /**
   * Valor total del carrito.
   */
  private BigDecimal valorTotal;

  /**
   * Número de items en el carrito.
   */
  private Integer numeroItems;

  /**
   * Fecha de creación del carrito.
   */
  private LocalDateTime fechaCreacion;

  /**
   * Fecha de última modificación.
   */
  private LocalDateTime fechaUltimaModificacion;

  /**
   * Fecha de abandono (si aplica).
   */
  private LocalDateTime fechaAbandono;

  /**
   * Tiempo total de vida del carrito (en horas).
   */
  private Double tiempoVidaHoras;

  /**
   * Categorías de productos en el carrito.
   */
  private List<String> categoriasProductos;

  /**
   * Patrones de comportamiento identificados.
   */
  private List<String> patronesComportamiento;

  /**
   * Score de probabilidad de conversión (0.0 - 1.0).
   */
  private Double scoreConversion;

  /**
   * Razones de posible abandono.
   */
  private List<String> razonesAbandonoPotencial;

  /**
   * Productos recomendados para recovery.
   */
  private List<ProductoRecomendadoDto> recomendacionesRecovery;

  /**
   * Canal de origen del carrito.
   */
  private String canalOrigen;

  /**
   * Dispositivo utilizado.
   */
  private String dispositivo;

  /**
   * Ubicación geográfica (si disponible).
   */
  private String ubicacion;

  /**
   * Número de sesiones del usuario.
   */
  private Integer numeroSesionesUsuario;

  /**
   * Descuentos aplicados.
   */
  private BigDecimal descuentosAplicados;

  /**
   * Ahorro total del usuario.
   */
  private BigDecimal ahorroTotal;

  /**
   * Frecuencia de compra del usuario.
   */
  private String frecuenciaCompra;

  /**
   * Valor de vida del cliente (CLV).
   */
  private BigDecimal valorVidaCliente;

  /**
   * Timestamp del análisis.
   */
  @Builder.Default
  private LocalDateTime fechaAnalisis = LocalDateTime.now();
}
