package com.nelson.project.msvc_carrito.msvc_carrito.recommendation;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoRecomendadoDto;
import java.util.List;

/**
 * Interfaz para motores de recomendación de productos.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
public interface RecommendationEngine {
  /**
   * Genera recomendaciones basadas en el contenido del carrito.
   *
   * @param carritoId ID del carrito
   * @param limite Número máximo de recomendaciones
   * @return Lista de productos recomendados
   */
  List<ProductoRecomendadoDto> generarRecomendacionesPorCarrito(
    Long carritoId,
    Integer limite
  );

  /**
   * Genera recomendaciones basadas en el historial del usuario.
   *
   * @param usuarioId ID del usuario
   * @param limite Número máximo de recomendaciones
   * @return Lista de productos recomendados
   */
  List<ProductoRecomendadoDto> generarRecomendacionesPorUsuario(
    Long usuarioId,
    Integer limite
  );

  /**
   * Genera recomendaciones de productos similares.
   *
   * @param productoId ID del producto base
   * @param limite Número máximo de recomendaciones
   * @return Lista de productos similares
   */
  List<ProductoRecomendadoDto> generarRecomendacionesSimilares(
    Long productoId,
    Integer limite
  );

  /**
   * Genera recomendaciones de productos que suelen comprarse juntos.
   *
   * @param productosIds Lista de IDs de productos en el carrito
   * @param limite Número máximo de recomendaciones
   * @return Lista de productos complementarios
   */
  List<ProductoRecomendadoDto> generarRecomendacionesComplementarias(
    List<Long> productosIds,
    Integer limite
  );

  /**
   * Obtiene productos trending o populares.
   *
   * @param limite Número máximo de recomendaciones
   * @return Lista de productos populares
   */
  List<ProductoRecomendadoDto> obtenerProductosPopulares(Integer limite);
}
