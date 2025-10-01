package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.CarritoHistorial;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.CarritoHistorial.TipoOperacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestión de historial de carritos.
 * Proporciona consultas especializadas para auditoría y trazabilidad.
 *
 * Características:
 * - Consultas optimizadas con índices
 * - Paginación para grandes volúmenes
 * - Filtros por diferentes criterios
 * - Análisis de comportamiento de usuarios
 */
@Repository
public interface CarritoHistorialRepository
  extends JpaRepository<CarritoHistorial, Long> {
  // ========== Consultas por Carrito ==========

  /**
   * Obtiene historial completo de un carrito ordenado por fecha descendente
   */
  List<CarritoHistorial> findByCarritoIdOrderByFechaOperacionDesc(
    Long carritoId
  );

  /**
   * Obtiene historial paginado de un carrito
   */
  Page<CarritoHistorial> findByCarritoIdOrderByFechaOperacionDesc(
    Long carritoId,
    Pageable pageable
  );

  /**
   * Obtiene el último evento de un carrito
   */
  Optional<CarritoHistorial> findFirstByCarritoIdOrderByFechaOperacionDesc(
    Long carritoId
  );

  /**
   * Obtiene historial de un carrito filtrado por tipo de operación
   */
  List<
    CarritoHistorial
  > findByCarritoIdAndTipoOperacionOrderByFechaOperacionDesc(
    Long carritoId,
    TipoOperacion tipoOperacion
  );

  // ========== Consultas por Usuario ==========

  /**
   * Obtiene historial de todos los carritos de un usuario
   */
  @Query(
    """
    SELECT h FROM CarritoHistorial h
    JOIN Carrito c ON h.carritoId = c.id
    WHERE c.usuarioId = :usuarioId
    ORDER BY h.fechaOperacion DESC
    """
  )
  List<CarritoHistorial> findByUsuarioIdOrderByFechaOperacionDesc(
    @Param("usuarioId") Long usuarioId
  );

  /**
   * Obtiene historial paginado de un usuario
   */
  @Query(
    """
    SELECT h FROM CarritoHistorial h
    JOIN Carrito c ON h.carritoId = c.id
    WHERE c.usuarioId = :usuarioId
    ORDER BY h.fechaOperacion DESC
    """
  )
  Page<CarritoHistorial> findByUsuarioIdOrderByFechaOperacionDesc(
    @Param("usuarioId") Long usuarioId,
    Pageable pageable
  );

  // ========== Consultas por Operación ==========

  /**
   * Busca por tipo de operación en un rango de fechas
   */
  List<
    CarritoHistorial
  > findByTipoOperacionAndFechaOperacionBetweenOrderByFechaOperacionDesc(
    TipoOperacion tipoOperacion,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Cuenta operaciones por tipo en un período
   */
  @Query(
    """
    SELECT COUNT(h) FROM CarritoHistorial h
    WHERE h.tipoOperacion = :tipoOperacion
    AND h.fechaOperacion BETWEEN :fechaInicio AND :fechaFin
    """
  )
  Long countByTipoOperacionAndFechaOperacionBetween(
    @Param("tipoOperacion") TipoOperacion tipoOperacion,
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Consultas por Producto ==========

  /**
   * Obtiene historial de operaciones de un producto específico
   */
  List<CarritoHistorial> findByProductoIdOrderByFechaOperacionDesc(
    Long productoId
  );

  /**
   * Obtiene historial de un producto en un carrito específico
   */
  List<CarritoHistorial> findByCarritoIdAndProductoIdOrderByFechaOperacionDesc(
    Long carritoId,
    Long productoId
  );

  /**
   * Cuenta cuántas veces se ha agregado un producto
   */
  @Query(
    """
    SELECT COUNT(h) FROM CarritoHistorial h
    WHERE h.productoId = :productoId
    AND h.tipoOperacion = :tipoOperacion
    """
  )
  Long countByProductoIdAndTipoOperacion(
    @Param("productoId") Long productoId,
    @Param("tipoOperacion") TipoOperacion tipoOperacion
  );

  // ========== Consultas por Fecha ==========

  /**
   * Obtiene historial en un rango de fechas
   */
  List<CarritoHistorial> findByFechaOperacionBetweenOrderByFechaOperacionDesc(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Obtiene historial paginado en un rango de fechas
   */
  Page<CarritoHistorial> findByFechaOperacionBetweenOrderByFechaOperacionDesc(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Pageable pageable
  );

  /**
   * Obtiene operaciones del día actual
   */
  @Query(
    """
    SELECT h FROM CarritoHistorial h
    WHERE DATE(h.fechaOperacion) = CURRENT_DATE
    ORDER BY h.fechaOperacion DESC
    """
  )
  List<CarritoHistorial> findOperacionesDeHoy();

  // ========== Análisis y Estadísticas ==========

  /**
   * Obtiene los productos más agregados en un período
   */
  @Query(
    """
    SELECT h.productoId, h.nombreProducto, COUNT(h) as total
    FROM CarritoHistorial h
    WHERE h.tipoOperacion = 'ITEM_AGREGADO'
    AND h.fechaOperacion BETWEEN :fechaInicio AND :fechaFin
    AND h.productoId IS NOT NULL
    GROUP BY h.productoId, h.nombreProducto
    ORDER BY total DESC
    """
  )
  List<Object[]> findProductosMasAgregados(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene estadísticas de operaciones por día
   */
  @Query(
    """
    SELECT DATE(h.fechaOperacion) as fecha,
           h.tipoOperacion,
           COUNT(h) as total
    FROM CarritoHistorial h
    WHERE h.fechaOperacion BETWEEN :fechaInicio AND :fechaFin
    GROUP BY DATE(h.fechaOperacion), h.tipoOperacion
    ORDER BY fecha DESC, h.tipoOperacion
    """
  )
  List<Object[]> findEstadisticasPorDia(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene carritos abandonados (sin actividad en X horas)
   */
  @Query(
    """
    SELECT DISTINCT h.carritoId
    FROM CarritoHistorial h
    WHERE h.carritoId NOT IN (
        SELECT h2.carritoId
        FROM CarritoHistorial h2
        WHERE h2.fechaOperacion > :fechaLimite
    )
    AND h.tipoOperacion NOT IN ('CARRITO_PROCESADO', 'CARRITO_VACIADO')
    """
  )
  List<Long> findCarritosAbandonados(
    @Param("fechaLimite") LocalDateTime fechaLimite
  );

  // ========== Consultas por IP y Usuario ==========

  /**
   * Obtiene historial por IP cliente (para detección de patrones)
   */
  List<CarritoHistorial> findByIpClienteOrderByFechaOperacionDesc(
    String ipCliente
  );

  /**
   * Obtiene historial por usuario de operación
   */
  List<CarritoHistorial> findByUsuarioOperacionOrderByFechaOperacionDesc(
    String usuarioOperacion
  );

  /**
   * Detecta actividad sospechosa (muchas operaciones desde la misma IP)
   */
  @Query(
    """
    SELECT h.ipCliente, COUNT(h) as total
    FROM CarritoHistorial h
    WHERE h.fechaOperacion > :fechaLimite
    AND h.ipCliente IS NOT NULL
    GROUP BY h.ipCliente
    HAVING COUNT(h) > :limite
    ORDER BY total DESC
    """
  )
  List<Object[]> findActividadSospechosaPorIP(
    @Param("fechaLimite") LocalDateTime fechaLimite,
    @Param("limite") Integer limite
  );

  // ========== Operaciones de Limpieza ==========

  /**
   * Elimina historial antiguo (para mantenimiento)
   */
  @Query("DELETE FROM CarritoHistorial h WHERE h.fechaOperacion < :fechaLimite")
  void deleteHistorialAntiguo(@Param("fechaLimite") LocalDateTime fechaLimite);

  /**
   * Cuenta registros por eliminar
   */
  Long countByFechaOperacionBefore(LocalDateTime fechaLimite);

  // ========== Consultas de Auditoría ==========

  /**
   * Obtiene operaciones críticas para auditoría
   */
  @Query(
    """
    SELECT h FROM CarritoHistorial h
    WHERE h.tipoOperacion IN ('CARRITO_PROCESADO', 'CARRITO_VACIADO', 'DESCUENTO_APLICADO')
    AND h.fechaOperacion BETWEEN :fechaInicio AND :fechaFin
    ORDER BY h.fechaOperacion DESC
    """
  )
  List<CarritoHistorial> findOperacionesCriticas(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Verifica integridad: carritos con operaciones pero sin creación
   */
  @Query(
    """
    SELECT DISTINCT h.carritoId
    FROM CarritoHistorial h
    WHERE h.carritoId NOT IN (
        SELECT h2.carritoId
        FROM CarritoHistorial h2
        WHERE h2.tipoOperacion = 'CARRITO_CREADO'
    )
    """
  )
  List<Long> findCarritosSinCreacion();

  // ========== Métodos de Registro ==========

  /**
   * Registra una operación en el historial
   */
  default void registrarOperacion(
    Long carritoId,
    Long usuarioId,
    String tipoOperacion,
    Object estadoAnterior,
    Object estadoNuevo,
    String descripcion,
    String usuarioOperacion,
    java.util.Map<String, Object> metadatos
  ) {
    // Crear y guardar el registro de historial
    CarritoHistorial historial = new CarritoHistorial(
      carritoId,
      TipoOperacion.valueOf(tipoOperacion),
      usuarioOperacion
    );
    historial.setDescripcion(descripcion);
    historial.setFechaOperacion(LocalDateTime.now());

    save(historial);
  }
}
