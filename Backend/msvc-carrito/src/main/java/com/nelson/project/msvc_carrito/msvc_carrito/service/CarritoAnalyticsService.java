package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoAnalyticsDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoMetricasDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoRecomendadoDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio especializado para analytics y business intelligence del carrito.
 *
 * Responsabilidades:
 * - Generación de métricas y KPIs
 * - Motor de recomendaciones
 * - Análisis de comportamiento del usuario
 * - Reportes de abandono de carrito
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface CarritoAnalyticsService {
  /**
   * Obtiene métricas generales del carrito para un usuario.
   *
   * @param usuarioId ID del usuario
   * @return CarritoMetricasDto con las métricas
   */
  CarritoMetricasDto obtenerMetricasCarrito(Long usuarioId);

  /**
   * Genera recomendaciones de productos basadas en el carrito actual.
   *
   * @param carritoId ID del carrito
   * @param limite Número máximo de recomendaciones
   * @return Lista de ProductoRecomendadoDto
   */
  List<ProductoRecomendadoDto> generarRecomendaciones(
    Long carritoId,
    Integer limite
  );

  /**
   * Obtiene análisis detallado del comportamiento del carrito.
   *
   * @param carritoId ID del carrito
   * @return CarritoAnalyticsDto con análisis completo
   */
  CarritoAnalyticsDto obtenerAnalyticsCarrito(Long carritoId);

  /**
   * Obtiene carritos abandonados para análisis de recovery.
   *
   * @param desde Fecha desde la cual buscar
   * @param hasta Fecha hasta la cual buscar
   * @param pageable Paginación
   * @return Page con carritos abandonados
   */
  Page<CarritoAnalyticsDto> obtenerCarritosAbandonados(
    LocalDateTime desde,
    LocalDateTime hasta,
    Pageable pageable
  );

  /**
   * Calcula el valor promedio de carritos para un período.
   *
   * @param desde Fecha desde la cual calcular
   * @param hasta Fecha hasta la cual calcular
   * @return BigDecimal con el valor promedio
   */
  BigDecimal calcularValorPromedioCarritos(
    LocalDateTime desde,
    LocalDateTime hasta
  );

  /**
   * Obtiene los productos más agregados al carrito.
   *
   * @param limite Número máximo de productos a retornar
   * @param periodo Período en días para el análisis
   * @return Lista de productos más populares
   */
  List<ProductoRecomendadoDto> obtenerProductosMasAgregados(
    Integer limite,
    Integer periodo
  );

  /**
   * Genera métricas de conversión de carrito a compra.
   *
   * @param usuarioId ID del usuario (opcional, null para métricas generales)
   * @param desde Fecha desde la cual calcular
   * @param hasta Fecha hasta la cual calcular
   * @return CarritoMetricasDto con métricas de conversión
   */
  CarritoMetricasDto obtenerMetricasConversion(
    Long usuarioId,
    LocalDateTime desde,
    LocalDateTime hasta
  );

  /**
   * Analiza patrones de compra para mejorar recomendaciones.
   *
   * @param usuarioId ID del usuario
   * @return Lista de patrones identificados
   */
  List<String> analizarPatronesCompra(Long usuarioId);

  /**
   * Registra evento de analytics para machine learning.
   *
   * @param carritoId ID del carrito
   * @param evento Tipo de evento (agregar_item, remover_item, etc.)
   * @param metadata Información adicional del evento
   */
  void registrarEventoAnalytics(Long carritoId, String evento, String metadata);

  /**
   * Obtiene tiempo promedio que los usuarios mantienen items en carrito.
   *
   * @param productoId ID del producto (opcional)
   * @param periodo Período en días para el análisis
   * @return Tiempo promedio en horas
   */
  Double obtenerTiempoPromedioEnCarrito(Long productoId, Integer periodo);
}
