package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.ItemCarrito;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio especializado para consultas avanzadas de items de carrito.
 * Complementa la funcionalidad del CarritoRepository para análisis detallados.
 *
 * Características:
 * - Análisis granular por producto
 * - Métricas de rendimiento de productos
 * - Optimización de inventario
 * - Consultas de recomendaciones
 */
@Repository
public interface ItemCarritoQueryRepository
  extends JpaRepository<ItemCarrito, Long> {
  // ========== Consultas por Carrito ==========

  /**
   * Obtiene items activos de un carrito específico
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE i.carrito.id = :carritoId
    AND i.carrito.estado = 'ACTIVO'
    ORDER BY i.agregadoEn DESC
    """
  )
  List<ItemCarrito> findItemsActivosByCarritoId(
    @Param("carritoId") Long carritoId
  );

  /**
   * Busca un item específico por producto en un carrito
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE i.carrito.id = :carritoId
    AND i.productoId = :productoId
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  Optional<ItemCarrito> findItemByCarritoAndProducto(
    @Param("carritoId") Long carritoId,
    @Param("productoId") Long productoId
  );

  /**
   * Cuenta items en un carrito
   */
  @Query(
    """
    SELECT COUNT(i) FROM ItemCarrito i
    WHERE i.carrito.id = :carritoId
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  Long countItemsByCarritoId(@Param("carritoId") Long carritoId);

  // ========== Consultas por Producto ==========

  /**
   * Encuentra todos los carritos que contienen un producto específico
   */
  List<ItemCarrito> findByProductoIdOrderByAgregadoEnDesc(Long productoId);

  /**
   * Obtiene items de un producto en carritos activos
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE i.productoId = :productoId
    AND i.carrito.estado = 'ACTIVO'
    ORDER BY i.agregadoEn DESC
    """
  )
  List<ItemCarrito> findItemsActivosByProductoId(
    @Param("productoId") Long productoId
  );

  /**
   * Cuenta cuántos carritos activos contienen un producto
   */
  @Query(
    """
    SELECT COUNT(DISTINCT i.carrito.id) FROM ItemCarrito i
    WHERE i.productoId = :productoId
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  Long countCarritosActivosConProducto(@Param("productoId") Long productoId);

  /**
   * Obtiene la cantidad total de un producto en carritos activos
   */
  @Query(
    """
    SELECT COALESCE(SUM(i.cantidad), 0) FROM ItemCarrito i
    WHERE i.productoId = :productoId
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  Long sumCantidadActivaByProductoId(@Param("productoId") Long productoId);

  // ========== Consultas por Usuario ==========

  /**
   * Obtiene historial de items comprados por un usuario
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE i.carrito.usuarioId = :usuarioId
    AND i.carrito.estado = 'PROCESADO'
    ORDER BY i.carrito.creadoEn DESC, i.agregadoEn DESC
    """
  )
  List<ItemCarrito> findItemsCompradosByUsuario(
    @Param("usuarioId") Long usuarioId
  );

  /**
   * Obtiene productos únicos comprados por un usuario
   */
  @Query(
    """
    SELECT DISTINCT i.productoId, i.nombreProducto,
           COUNT(i) as vecesComprado,
           SUM(i.cantidad) as cantidadTotal,
           AVG(i.precioUnitario) as precioPromedio,
           MAX(i.carrito.creadoEn) as ultimaCompra
    FROM ItemCarrito i
    WHERE i.carrito.usuarioId = :usuarioId
    AND i.carrito.estado = 'PROCESADO'
    GROUP BY i.productoId, i.nombreProducto
    ORDER BY vecesComprado DESC, ultimaCompra DESC
    """
  )
  List<Object[]> findProductosCompradosByUsuario(
    @Param("usuarioId") Long usuarioId
  );

  /**
   * Obtiene items actuales del carrito activo de un usuario
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE i.carrito.usuarioId = :usuarioId
    AND i.carrito.estado = 'ACTIVO'
    ORDER BY i.agregadoEn DESC
    """
  )
  List<ItemCarrito> findItemsCarritoActivoByUsuario(
    @Param("usuarioId") Long usuarioId
  );

  // ========== Análisis de Productos ==========

  /**
   * Productos más agregados a carritos en un período
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           COUNT(i) as vecesAgregado,
           SUM(i.cantidad) as cantidadTotal,
           COUNT(DISTINCT i.carrito.id) as carritosUnicos,
           AVG(i.precioUnitario) as precioPromedio,
           SUM(i.subtotal) as ventaTotalPotencial
    FROM ItemCarrito i
    WHERE i.agregadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    ORDER BY vecesAgregado DESC, cantidadTotal DESC
    """
  )
  List<Object[]> findProductosMasAgregados(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Productos más convertidos (en carritos procesados)
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           COUNT(i) as vecesVendido,
           SUM(i.cantidad) as cantidadVendida,
           SUM(i.subtotal) as ventaTotal,
           AVG(i.precioUnitario) as precioPromedio,
           COUNT(DISTINCT i.carrito.usuarioId) as usuariosUnicos
    FROM ItemCarrito i
    WHERE i.carrito.estado = 'PROCESADO'
    AND i.carrito.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    ORDER BY ventaTotal DESC, cantidadVendida DESC
    """
  )
  List<Object[]> findProductosMasVendidos(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Productos con mayor tasa de abandono
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           COUNT(CASE WHEN i.carrito.estado IN ('ABANDONADO', 'EXPIRADO') THEN 1 END) as abandonos,
           COUNT(CASE WHEN i.carrito.estado = 'PROCESADO' THEN 1 END) as conversiones,
           COUNT(i) as totalApariciones,
           (COUNT(CASE WHEN i.carrito.estado IN ('ABANDONADO', 'EXPIRADO') THEN 1 END) * 100.0 / COUNT(i)) as tasaAbandono
    FROM ItemCarrito i
    WHERE i.agregadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    HAVING COUNT(i) >= :minimaApariciones
    ORDER BY tasaAbandono DESC, totalApariciones DESC
    """
  )
  List<Object[]> findProductosConMayorAbandono(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("minimaApariciones") Integer minimaApariciones
  );

  // ========== Análisis de Precios ==========

  /**
   * Detecta cambios de precio en items
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           MIN(i.precioUnitario) as precioMinimo,
           MAX(i.precioUnitario) as precioMaximo,
           AVG(i.precioUnitario) as precioPromedio,
           COUNT(DISTINCT i.precioUnitario) as variacionesPrecios,
           COUNT(i) as totalItems
    FROM ItemCarrito i
    WHERE i.agregadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    HAVING COUNT(DISTINCT i.precioUnitario) > 1
    ORDER BY variacionesPrecios DESC, totalItems DESC
    """
  )
  List<Object[]> findProductosConVariacionPrecios(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Productos con precios por encima/debajo del promedio
   */
  @Query(
    """
    WITH PromedioPrecios AS (
        SELECT AVG(i.precioUnitario) as precioGlobalPromedio
        FROM ItemCarrito i
        WHERE i.agregadoEn BETWEEN :fechaInicio AND :fechaFin
    )
    SELECT i.productoId, i.nombreProducto,
           AVG(i.precioUnitario) as precioPromedio,
           pp.precioGlobalPromedio,
           (AVG(i.precioUnitario) - pp.precioGlobalPromedio) as diferencia,
           COUNT(i) as totalItems
    FROM ItemCarrito i, PromedioPrecios pp
    WHERE i.agregadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto, pp.precioGlobalPromedio
    HAVING ABS(AVG(i.precioUnitario) - pp.precioGlobalPromedio) > :tolerancia
    ORDER BY ABS(diferencia) DESC
    """
  )
  List<Object[]> findProductosConPreciosAnomalos(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("tolerancia") BigDecimal tolerancia
  );

  // ========== Análisis de Cantidades ==========

  /**
   * Distribución de cantidades por producto
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           AVG(i.cantidad) as cantidadPromedio,
           MIN(i.cantidad) as cantidadMinima,
           MAX(i.cantidad) as cantidadMaxima,
           COUNT(CASE WHEN i.cantidad = 1 THEN 1 END) as comprasUnitarias,
           COUNT(CASE WHEN i.cantidad > 5 THEN 1 END) as comprasVolumen,
           COUNT(i) as totalCompras
    FROM ItemCarrito i
    WHERE i.carrito.estado = 'PROCESADO'
    AND i.carrito.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    ORDER BY cantidadPromedio DESC, totalCompras DESC
    """
  )
  List<Object[]> findDistribucionCantidades(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Recomendaciones y Cross-Selling ==========

  /**
   * Encuentra productos frecuentemente comprados después de un producto específico
   */
  @Query(
    """
    SELECT i2.productoId, i2.nombreProducto,
           COUNT(*) as frecuencia,
           AVG(i2.precioUnitario) as precioPromedio
    FROM ItemCarrito i1
    JOIN ItemCarrito i2 ON i1.carrito.usuarioId = i2.carrito.usuarioId
    WHERE i1.productoId = :productoBase
    AND i2.productoId != :productoBase
    AND i1.carrito.estado = 'PROCESADO'
    AND i2.carrito.estado = 'PROCESADO'
    AND i2.carrito.creadoEn > i1.carrito.creadoEn
    AND i2.carrito.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i2.productoId, i2.nombreProducto
    HAVING COUNT(*) >= :minimaFrecuencia
    ORDER BY frecuencia DESC
    """
  )
  List<Object[]> findProductosCompradosDespuesDe(
    @Param("productoBase") Long productoBase,
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("minimaFrecuencia") Integer minimaFrecuencia
  );

  /**
   * Productos recomendados para un usuario basado en su historial
   */
  @Query(
    """
    SELECT DISTINCT i2.productoId, i2.nombreProducto,
           COUNT(*) as relevancia,
           AVG(i2.precioUnitario) as precioPromedio
    FROM ItemCarrito i1
    JOIN ItemCarrito i2 ON i1.carrito.usuarioId != i2.carrito.usuarioId
    WHERE i1.carrito.usuarioId = :usuarioId
    AND i1.carrito.estado = 'PROCESADO'
    AND i2.carrito.estado = 'PROCESADO'
    AND i2.productoId NOT IN (
        SELECT DISTINCT ic.productoId
        FROM ItemCarrito ic
        WHERE ic.carrito.usuarioId = :usuarioId
        AND ic.carrito.estado = 'PROCESADO'
    )
    AND EXISTS (
        SELECT 1 FROM ItemCarrito i3
        WHERE i3.carrito.usuarioId = i2.carrito.usuarioId
        AND i3.productoId = i1.productoId
        AND i3.carrito.estado = 'PROCESADO'
    )
    GROUP BY i2.productoId, i2.nombreProducto
    ORDER BY relevancia DESC, precioPromedio ASC
    """
  )
  List<Object[]> findProductosRecomendadosParaUsuario(
    @Param("usuarioId") Long usuarioId
  );

  // ========== Operaciones de Mantenimiento ==========

  /**
   * Actualiza precios de items en carritos activos
   */
  @Modifying
  @Query(
    """
    UPDATE ItemCarrito i
    SET i.precioUnitario = :nuevoPrecio,
        i.actualizadoEn = :fechaActual
    WHERE i.productoId = :productoId
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  int actualizarPreciosEnCarritosActivos(
    @Param("productoId") Long productoId,
    @Param("nuevoPrecio") BigDecimal nuevoPrecio,
    @Param("fechaActual") LocalDateTime fechaActual
  );

  /**
   * Elimina items de productos descontinuados
   */
  @Modifying
  @Query(
    """
    DELETE FROM ItemCarrito i
    WHERE i.productoId IN :productosDescontinuados
    AND i.carrito.estado = 'ACTIVO'
    """
  )
  int eliminarItemsProductosDescontinuados(
    @Param("productosDescontinuados") List<Long> productosDescontinuados
  );

  // ========== Consultas de Inventario ==========

  /**
   * Productos con alta demanda en carritos activos
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           SUM(i.cantidad) as demandaTotal,
           COUNT(DISTINCT i.carrito.id) as carritosConProducto,
           AVG(i.cantidad) as cantidadPromedioPorCarrito
    FROM ItemCarrito i
    WHERE i.carrito.estado = 'ACTIVO'
    GROUP BY i.productoId, i.nombreProducto
    HAVING SUM(i.cantidad) >= :demandaMinima
    ORDER BY demandaTotal DESC, carritosConProducto DESC
    """
  )
  List<Object[]> findProductosAltaDemanda(
    @Param("demandaMinima") Integer demandaMinima
  );

  /**
   * Resumen de inventario comprometido por producto
   */
  @Query(
    """
    SELECT i.productoId, i.nombreProducto,
           SUM(i.cantidad) as cantidadComprometida,
           COUNT(DISTINCT i.carrito.id) as carritosAfectados,
           SUM(i.subtotal) as valorComprometido
    FROM ItemCarrito i
    WHERE i.carrito.estado = 'ACTIVO'
    AND i.productoId IN :productosInteres
    GROUP BY i.productoId, i.nombreProducto
    ORDER BY cantidadComprometida DESC
    """
  )
  List<Object[]> findInventarioComprometido(
    @Param("productosInteres") List<Long> productosInteres
  );

  // ========== Consultas de Auditoría ==========

  /**
   * Items con inconsistencias en subtotales
   */
  @Query(
    """
    SELECT i FROM ItemCarrito i
    WHERE ABS(i.subtotal - (i.cantidad * i.precioUnitario)) > :tolerancia
    ORDER BY i.actualizadoEn DESC
    """
  )
  List<ItemCarrito> findItemsConInconsistencias(
    @Param("tolerancia") BigDecimal tolerancia
  );

  /**
   * Items duplicados en el mismo carrito (por producto)
   */
  @Query(
    """
    SELECT i.carrito.id, i.productoId, COUNT(*) as duplicados
    FROM ItemCarrito i
    WHERE i.carrito.estado = 'ACTIVO'
    GROUP BY i.carrito.id, i.productoId
    HAVING COUNT(*) > 1
    """
  )
  List<Object[]> findItemsDuplicados();
}
