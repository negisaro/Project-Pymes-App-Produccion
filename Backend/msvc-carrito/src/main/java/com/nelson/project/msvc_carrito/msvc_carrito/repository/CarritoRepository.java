package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.EstadoCarrito;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio mejorado para gestión completa de carritos.
 * Proporciona consultas especializadas para operaciones de negocio robustas.
 *
 * Características:
 * - Consultas optimizadas con índices
 * - Gestión de estados del carrito
 * - Análisis de abandonos y conversiones
 * - Operaciones de mantenimiento
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
  // ========== Consultas Básicas Mejoradas ==========

  /**
   * Busca carrito activo por usuario
   */
  Optional<Carrito> findByUsuarioId(Long usuarioId);

  /**
   * Busca carrito activo por usuario (método de conveniencia)
   */
  default Optional<Carrito> findCarritoActivoPorUsuario(Long usuarioId) {
    return findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO);
  }

  /**
   * Busca carrito por usuario y estado específico
   */
  Optional<Carrito> findByUsuarioIdAndEstado(
    Long usuarioId,
    EstadoCarrito estado
  );

  /**
   * Verifica si un usuario tiene carrito activo
   */
  boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoCarrito estado);

  /**
   * Obtiene todos los carritos de un usuario (histórico)
   */
  List<Carrito> findByUsuarioIdOrderByCreadoEnDesc(Long usuarioId);

  // ========== Consultas por Estado ==========

  /**
   * Obtiene carritos por estado
   */
  List<Carrito> findByEstadoOrderByCreadoEnDesc(EstadoCarrito estado);

  /**
   * Obtiene carritos activos con paginación
   */
  Page<Carrito> findByEstadoOrderByCreadoEnDesc(
    EstadoCarrito estado,
    Pageable pageable
  );

  /**
   * Cuenta carritos por estado
   */
  Long countByEstado(EstadoCarrito estado);

  /**
   * Obtiene carritos en múltiples estados
   */
  List<Carrito> findByEstadoInOrderByCreadoEnDesc(List<EstadoCarrito> estados);

  // ========== Consultas por Fecha ==========

  /**
   * Obtiene carritos creados en un rango de fechas
   */
  List<Carrito> findByCreadoEnBetweenOrderByCreadoEnDesc(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Obtiene carritos creados hoy
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE c.creadoEn >= :inicioDia AND c.creadoEn < :finDia
    ORDER BY c.creadoEn DESC
    """
  )
  List<Carrito> findCarritosCreadosHoy(
    @Param("inicioDia") LocalDateTime inicioDia,
    @Param("finDia") LocalDateTime finDia
  );

  /**
   * Método de conveniencia por defecto para obtener carritos de hoy sin repetir lógica de rango.
   */
  default List<Carrito> findCarritosCreadosHoyConvenience() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime start = now.toLocalDate().atStartOfDay();
    LocalDateTime end = start.plusDays(1);
    return findCarritosCreadosHoy(start, end);
  }

  /**
   * Obtiene carritos modificados recientemente
   */
  List<Carrito> findByActualizadoEnAfterOrderByActualizadoEnDesc(
    LocalDateTime fecha
  );

  // ========== Análisis de Abandono ==========

  /**
   * Obtiene carritos abandonados (sin actividad en X tiempo)
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE c.estado = 'ACTIVO'
    AND c.actualizadoEn < :fechaLimite
    AND SIZE(c.items) > 0
    ORDER BY c.actualizadoEn ASC
    """
  )
  List<Carrito> findCarritosAbandonados(
    @Param("fechaLimite") LocalDateTime fechaLimite
  );

  /**
   * Obtiene carritos próximos a expirar
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE c.estado = 'ACTIVO'
    AND c.expiraEn BETWEEN :fechaInicio AND :fechaFin
    ORDER BY c.expiraEn ASC
    """
  )
  List<Carrito> findCarritosProximosAExpirar(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene carritos expirados pero aún activos
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE c.estado = 'ACTIVO'
    AND c.expiraEn < :fechaActual
    ORDER BY c.expiraEn ASC
    """
  )
  List<Carrito> findCarritosExpirados(
    @Param("fechaActual") LocalDateTime fechaActual
  );

  // ========== Análisis por Valor ==========

  /**
   * Obtiene carritos con total mayor a un monto
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE c.total >= :montoMinimo
    AND c.estado = :estado
    ORDER BY c.total DESC
    """
  )
  List<Carrito> findCarritosPorMontoMinimo(
    @Param("montoMinimo") BigDecimal montoMinimo,
    @Param("estado") EstadoCarrito estado
  );

  /**
   * Obtiene carritos con más de X items
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE SIZE(c.items) >= :cantidadMinima
    AND c.estado = :estado
    ORDER BY SIZE(c.items) DESC
    """
  )
  List<Carrito> findCarritosPorCantidadItems(
    @Param("cantidadMinima") Integer cantidadMinima,
    @Param("estado") EstadoCarrito estado
  );

  /**
   * Calcula valor promedio de carritos activos
   */
  @Query(
    """
    SELECT AVG(c.total)
    FROM Carrito c
    WHERE c.estado = 'ACTIVO'
    AND c.total > 0
    """
  )
  BigDecimal findValorPromedioCarritosActivos();

  // ========== Estadísticas y Análisis ==========

  /**
   * Obtiene estadísticas diarias de carritos
   */
  @Query(
    """
    SELECT DATE(c.creadoEn) as fecha,
           COUNT(c) as total,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
           COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) as abandonados,
           AVG(c.total) as valorPromedio
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY DATE(c.creadoEn)
    ORDER BY fecha DESC
    """
  )
  List<Object[]> findEstadisticasDiarias(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene top usuarios por valor de carrito
   */
  @Query(
    """
    SELECT c.usuarioId,
           COUNT(c) as totalCarritos,
           SUM(c.total) as valorTotal,
           AVG(c.total) as valorPromedio,
           MAX(c.total) as mayorCompra
    FROM Carrito c
    WHERE c.estado = 'PROCESADO'
    AND c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY c.usuarioId
    ORDER BY valorTotal DESC
    """
  )
  List<Object[]> findTopUsuariosPorValor(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Calcula tasa de conversión por período
   */
  @Query(
    """
    SELECT
        COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) * 100.0 / COUNT(c) as tasaConversion,
        COUNT(c) as totalCarritos,
        COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    """
  )
  Object[] findTasaConversion(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Consultas por Productos ==========

  /**
   * Busca carritos que contienen un producto específico
   */
  @Query(
    """
    SELECT DISTINCT c FROM Carrito c
    JOIN c.items i
    WHERE i.productoId = :productoId
    AND c.estado = :estado
    ORDER BY c.actualizadoEn DESC
    """
  )
  List<Carrito> findCarritosConProducto(
    @Param("productoId") Long productoId,
    @Param("estado") EstadoCarrito estado
  );

  /**
   * Cuenta carritos que contienen un producto
   */
  @Query(
    """
    SELECT COUNT(DISTINCT c) FROM Carrito c
    JOIN c.items i
    WHERE i.productoId = :productoId
    AND c.estado = :estado
    """
  )
  Long countCarritosConProducto(
    @Param("productoId") Long productoId,
    @Param("estado") EstadoCarrito estado
  );

  // ========== Operaciones de Mantenimiento ==========

  /**
   * Marca carritos como expirados automáticamente
   */
  @Modifying
  @Query(
    """
    UPDATE Carrito c
    SET c.estado = 'EXPIRADO',
        c.actualizadoEn = :fechaActual
    WHERE c.estado = 'ACTIVO'
    AND c.expiraEn < :fechaActual
    """
  )
  int marcarCarritosExpirados(@Param("fechaActual") LocalDateTime fechaActual);

  /**
   * Marca carritos como abandonados
   */
  @Modifying
  @Query(
    """
    UPDATE Carrito c
    SET c.estado = 'ABANDONADO',
        c.actualizadoEn = :fechaActual
    WHERE c.estado = 'ACTIVO'
    AND c.actualizadoEn < :fechaLimite
    AND SIZE(c.items) > 0
    """
  )
  int marcarCarritosAbandonados(
    @Param("fechaLimite") LocalDateTime fechaLimite,
    @Param("fechaActual") LocalDateTime fechaActual
  );

  /**
   * Elimina carritos vacíos antiguos
   */
  @Modifying
  @Query(
    """
    DELETE FROM Carrito c
    WHERE c.estado IN ('EXPIRADO', 'ABANDONADO')
    AND SIZE(c.items) = 0
    AND c.creadoEn < :fechaLimite
    """
  )
  int eliminarCarritosVaciosAntiguos(
    @Param("fechaLimite") LocalDateTime fechaLimite
  );

  // ========== Consultas de Validación ==========

  /**
   * Verifica integridad: carritos sin items pero con total
   */
  @Query(
    """
    SELECT c FROM Carrito c
    WHERE SIZE(c.items) = 0
    AND c.total > 0
    """
  )
  List<Carrito> findCarritosConInconsistencias();

  /**
   * Busca carritos duplicados por usuario en estado activo
   */
  @Query(
    """
    SELECT c.usuarioId, COUNT(c) as total
    FROM Carrito c
    WHERE c.estado = 'ACTIVO'
    GROUP BY c.usuarioId
    HAVING COUNT(c) > 1
    """
  )
  List<Object[]> findUsuariosConCarritosDuplicados();

  // ========== Consultas Personalizadas ==========

  /**
   * Busca carritos similares para recomendaciones
   */
  @Query(
    """
    SELECT DISTINCT c2 FROM Carrito c1
    JOIN c1.items i1
    JOIN Carrito c2 ON c2.id != c1.id
    JOIN c2.items i2 ON i2.productoId = i1.productoId
    WHERE c1.id = :carritoId
    AND c2.estado = 'PROCESADO'
    GROUP BY c2.id
    HAVING COUNT(DISTINCT i2.productoId) >= :minimoCoincidencias
    """
  )
  List<Carrito> findCarritosSimilares(
    @Param("carritoId") Long carritoId,
    @Param("minimoCoincidencias") Long minimoCoincidencias
  );

  /**
   * Obtiene carritos con descuentos aplicados
   */
  @Query(
    """
    SELECT DISTINCT c FROM Carrito c
    WHERE c.descuento > 0
    AND c.estado = :estado
    ORDER BY c.descuento DESC
    """
  )
  List<Carrito> findCarritosConDescuentos(
    @Param("estado") EstadoCarrito estado
  );
}
