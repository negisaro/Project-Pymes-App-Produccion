package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio especializado para análisis y métricas avanzadas de carritos.
 * Proporciona consultas complejas para business intelligence y reportes.
 *
 * Características:
 * - Análisis de comportamiento de usuarios
 * - Métricas de rendimiento del negocio
 * - Patrones de compra y abandono
 * - KPIs y dashboard data
 */
@Repository
public interface CarritoAnalyticsRepository
  extends JpaRepository<Carrito, Long> {
  // ========== Análisis de Comportamiento de Usuarios ==========

  /**
   * Obtiene patrones de tiempo de sesión promedio por usuario
   */
  @Query(
    value = """
      SELECT c.usuario_id,
             AVG(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en) / 60.0) as tiempoPromedioMinutos,
             COUNT(*) as totalSesiones,
             AVG(c.total) as valorPromedio
      FROM carritos c
      WHERE c.creado_en BETWEEN :fechaInicio AND :fechaFin
        AND c.actualizado_en > c.creado_en
        AND (SELECT COUNT(*) FROM items_carrito i WHERE i.carrito_id = c.id) > 0
      GROUP BY c.usuario_id
      HAVING COUNT(*) >= :minimoSesiones
      ORDER BY tiempoPromedioMinutos DESC
    """,
    nativeQuery = true
  )
  List<Object[]> findPatronesTiempoSesion(
    @Param("fechaInicio") java.sql.Timestamp fechaInicio,
    @Param("fechaFin") java.sql.Timestamp fechaFin,
    @Param("minimoSesiones") Integer minimoSesiones
  );

  /**
   * Análisis de frecuencia de compra por usuario
   */
  @Query(
    """
    SELECT c.usuarioId,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as carritosProcesados,
           (COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) * 100.0 / COUNT(c)) as tasaConversion,
           SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as valorTotalCompras,
           AVG(CASE WHEN c.estado = 'PROCESADO' THEN c.total END) as ticketPromedio
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY c.usuarioId
    HAVING COUNT(c) >= :minimoCarritos
    ORDER BY carritosProcesados DESC, valorTotalCompras DESC
    """
  )
  List<Object[]> findAnalisisFrecuenciaCompra(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("minimoCarritos") Integer minimoCarritos
  );

  /**
   * Segmentación de usuarios por valor de compra
   */
  @Query(
    value = """
    /* Subconsulta: totales por usuario dentro del rango */
    SELECT seg.segmento,
           COUNT(*) AS cantidadUsuarios,
           SUM(seg.total_usuario) AS valorTotalSegmento,
           (SUM(seg.total_usuario) / NULLIF(COUNT(*),0)) AS valorPromedioPorUsuario
    FROM (
        SELECT c.usuario_id AS usuario_id,
               SUM(c.total) AS total_usuario,
               CASE
                   WHEN SUM(c.total) >= :valorVIP THEN 'VIP'
                   WHEN SUM(c.total) >= :valorPremium THEN 'Premium'
                   WHEN SUM(c.total) >= :valorRegular THEN 'Regular'
                   ELSE 'Básico'
               END AS segmento
        FROM carritos c
        WHERE c.estado = 'PROCESADO'
          AND c.creado_en BETWEEN :fechaInicio AND :fechaFin
        GROUP BY c.usuario_id
    ) seg
    GROUP BY seg.segmento
    ORDER BY valorTotalSegmento DESC
    """,
    nativeQuery = true
  )
  List<Object[]> findSegmentacionUsuarios(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("valorVIP") BigDecimal valorVIP,
    @Param("valorPremium") BigDecimal valorPremium,
    @Param("valorRegular") BigDecimal valorRegular
  );

  // ========== Análisis de Productos y Cross-Selling ==========

  /**
   * Productos más frecuentemente comprados juntos
   */
  @Query(
    """
    SELECT i1.productoId as producto1,
           i1.nombreProducto as nombreProducto1,
           i2.productoId as producto2,
           i2.nombreProducto as nombreProducto2,
           COUNT(*) as frecuencia,
           AVG(c.total) as valorPromedioCarrito
    FROM Carrito c
    JOIN c.items i1
    JOIN c.items i2 ON i2.id != i1.id
    WHERE c.estado = 'PROCESADO'
    AND c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    AND i1.productoId < i2.productoId
    GROUP BY i1.productoId, i1.nombreProducto, i2.productoId, i2.nombreProducto
    HAVING COUNT(*) >= :minimaFrecuencia
    ORDER BY frecuencia DESC, valorPromedioCarrito DESC
    """
  )
  List<Object[]> findProductosComplementarios(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("minimaFrecuencia") Integer minimaFrecuencia
  );

  /**
   * Análisis de productos más abandonados
   */
  @Query(
    """
    SELECT i.productoId,
           i.nombreProducto,
           COUNT(CASE WHEN c.estado IN ('ABANDONADO', 'EXPIRADO') THEN 1 END) as abandonos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as conversiones,
           COUNT(*) as totalApariciones,
           (COUNT(CASE WHEN c.estado IN ('ABANDONADO', 'EXPIRADO') THEN 1 END) * 100.0 / COUNT(*)) as tasaAbandono,
           AVG(i.precioUnitario) as precioPromedio
    FROM Carrito c
    JOIN c.items i
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY i.productoId, i.nombreProducto
    HAVING COUNT(*) >= :minimaApariciones
    ORDER BY tasaAbandono DESC, totalApariciones DESC
    """
  )
  List<Object[]> findProductosMasAbandonados(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
    @Param("minimaApariciones") Integer minimaApariciones
  );

  // ========== Análisis Temporal y Estacionalidad ==========

  /**
   * Análisis de patrones por hora del día
   */
  @Query(
    """
    SELECT EXTRACT(HOUR FROM c.creadoEn) as hora,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
           AVG(c.total) as valorPromedio,
           AVG(SIZE(c.items)) as itemsPromedio
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY EXTRACT(HOUR FROM c.creadoEn)
    ORDER BY hora
    """
  )
  List<Object[]> findPatronesPorHora(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Análisis de patrones por día de la semana
   */
  @Query(
    """
    SELECT FUNCTION('dayofweek', c.creadoEn) as diaSemana,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
           AVG(c.total) as valorPromedio,
           SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasTotales
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY FUNCTION('dayofweek', c.creadoEn)
    ORDER BY diaSemana
    """
  )
  List<Object[]> findPatronesPorDiaSemana(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Tendencias mensuales de crecimiento
   */
  @Query(
    """
    SELECT EXTRACT(YEAR FROM c.creadoEn) as anio,
             EXTRACT(MONTH FROM c.creadoEn) as mes,
             COUNT(c) as totalCarritos,
             COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
             SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasTotales,
             COUNT(DISTINCT c.usuarioId) as usuariosUnicos,
             AVG(c.total) as ticketPromedio
      FROM Carrito c
      WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
      GROUP BY EXTRACT(YEAR FROM c.creadoEn), EXTRACT(MONTH FROM c.creadoEn)
    ORDER BY anio DESC, mes DESC
      """
  )
  List<Object[]> findTendenciasMensuales(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== KPIs y Métricas de Negocio ==========

  /**
   * Dashboard principal con KPIs
   */
  @Query(
    """
    SELECT
        COUNT(c) as totalCarritos,
        COUNT(CASE WHEN c.estado = 'ACTIVO' THEN 1 END) as carritosActivos,
        COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as carritosProcesados,
        COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) as carritosAbandonados,
        (COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) * 100.0 / NULLIF(COUNT(c), 0)) as tasaConversion,
        SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasTotales,
        AVG(CASE WHEN c.estado = 'PROCESADO' THEN c.total END) as ticketPromedio,
        SUM(CASE WHEN c.estado = 'PROCESADO' THEN SIZE(c.items) ELSE 0 END) as itemsVendidos,
        COUNT(DISTINCT c.usuarioId) as usuariosUnicos,
        COUNT(DISTINCT CASE WHEN c.estado = 'PROCESADO' THEN c.usuarioId END) as usuariosCompradores
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    """
  )
  Object[] findKPIsPrincipales(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Métricas de retención de usuarios
   */
  @Query(
    value = """
    /*
      Retención inmediata de usuarios:
      - usuariosNuevos: usuarios cuya primera compra (primer carrito PROCESADO) cae dentro del rango.
      - usuariosRecurrentes: subconjunto de esos nuevos que realizan al menos una compra adicional en el mismo rango.
      - tasaRetencion: (recurrentes / nuevos) * 100.
      Notas:
        * Se usa tabla física 'carritos' y columnas snake_case.
        * Evitamos divisiones por cero con NULLIF.
        * Se consideran solo carritos con estado PROCESADO.
    */
    WITH primera_compra AS (
        SELECT c.usuario_id, MIN(c.creado_en) AS primera_compra
        FROM carritos c
        WHERE c.estado = 'PROCESADO'
        GROUP BY c.usuario_id
    ), nuevos AS (
        SELECT pc.usuario_id, pc.primera_compra
        FROM primera_compra pc
        WHERE pc.primera_compra BETWEEN :fechaInicio AND :fechaFin
    ), recurrentes AS (
        SELECT DISTINCT c.usuario_id
        FROM carritos c
        JOIN nuevos n ON n.usuario_id = c.usuario_id
        WHERE c.estado = 'PROCESADO'
          AND c.creado_en > n.primera_compra
          AND c.creado_en BETWEEN :fechaInicio AND :fechaFin
    )
    SELECT
        (SELECT COUNT(*) FROM nuevos) AS usuariosNuevos,
        (SELECT COUNT(*) FROM recurrentes) AS usuariosRecurrentes,
        (
          CASE WHEN (SELECT COUNT(*) FROM nuevos) = 0 THEN 0
               ELSE (SELECT COUNT(*) FROM recurrentes) * 100.0 / (SELECT COUNT(*) FROM nuevos)
          END
        ) AS tasaRetencion
    """,
    nativeQuery = true
  )
  Object[] findMetricasRetencion(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Análisis de Cohortes ==========

  /**
   * Análisis de cohortes por mes de primera compra
   */
  @Query(
    value = """
       /* Cohorte: primer carrito procesado por usuario, normalizado al primer día del mes */
     WITH primera_compra AS (
       SELECT c.usuario_id,
          MIN(c.creado_en) AS primera_fecha
       FROM carritos c
       WHERE c.estado = 'PROCESADO'
       GROUP BY c.usuario_id
     ), cohortes AS (
       SELECT pc.usuario_id,
          DATE_FORMAT(pc.primera_fecha, '%Y-%m-01 00:00:00') AS cohorte
       FROM primera_compra pc
     )
       SELECT
           coh.cohorte AS cohorte,
           COUNT(DISTINCT coh.usuario_id) AS usuariosCohorte,
           COUNT(DISTINCT act.usuario_id) AS usuariosActivos,
           (COUNT(DISTINCT act.usuario_id) * 100.0 / NULLIF(COUNT(DISTINCT coh.usuario_id),0)) AS retencionPorcentaje,
           SUM(CASE WHEN act.estado = 'PROCESADO' THEN act.total ELSE 0 END) AS ventasCohorte
       FROM cohortes coh
    LEFT JOIN carritos act ON act.usuario_id = coh.usuario_id
       AND act.creado_en BETWEEN :fechaInicio AND :fechaFin
       WHERE coh.cohorte BETWEEN DATE_FORMAT(:cohorteInicio, '%Y-%m-01 00:00:00')
                             AND DATE_FORMAT(:cohorteFin, '%Y-%m-01 23:59:59')
       GROUP BY coh.cohorte
       ORDER BY coh.cohorte
       """,
    nativeQuery = true
  )
  List<Object[]> findAnalisisCohortes(
    @Param("cohorteInicio") LocalDateTime cohorteInicio,
    @Param("cohorteFin") LocalDateTime cohorteFin,
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Predicciones y Alertas ==========

  /**
   * Identifica usuarios en riesgo de abandono
   */
  @Query(
    """
    SELECT c.usuarioId,
           MAX(c.actualizadoEn) as ultimaActividad,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as carritosCompletados,
           AVG(c.total) as valorPromedio,
           SUM(CASE WHEN c.estado = 'ACTIVO' THEN 1 ELSE 0 END) as carritosActivos
    FROM Carrito c
    WHERE c.creadoEn >= :fechaLimite
    GROUP BY c.usuarioId
    HAVING MAX(c.actualizadoEn) < :fechaRiesgo
        AND COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) > 0
        AND SUM(CASE WHEN c.estado = 'ACTIVO' THEN 1 ELSE 0 END) > 0
    ORDER BY ultimaActividad ASC, valorPromedio DESC
    """
  )
  List<Object[]> findUsuariosEnRiesgoAbandono(
    @Param("fechaLimite") LocalDateTime fechaLimite,
    @Param("fechaRiesgo") LocalDateTime fechaRiesgo
  );

  /**
   * Detecta patrones anómalos en el comportamiento
   */
  @Query(
    value = "SELECT c.usuario_id AS usuarioId, " +
    "COUNT(c.id) AS carritosCreados, " +
    "AVG(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en) / 60.0) AS tiempoPromedioMinutos, " +
    "AVG(c.total) AS valorPromedio, " +
    "COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) AS abandonos " +
    "FROM carritos c " +
    "WHERE c.creado_en BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY c.usuario_id " +
    "HAVING COUNT(c.id) > :limiteCarritos " +
    "   OR AVG(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en) / 60.0) < :limiteMinutosBajo " +
    "   OR AVG(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en) / 60.0) > :limiteMinutosAlto " +
    "   OR (COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) * 100.0 / COUNT(c.id)) > :limiteAbandonoPorcentaje " +
    "ORDER BY carritosCreados DESC, tiempoPromedioMinutos ASC",
    nativeQuery = true
  )
  List<Object[]> findPatronesAnomalos(
    @Param("fechaInicio") java.sql.Timestamp fechaInicio,
    @Param("fechaFin") java.sql.Timestamp fechaFin,
    @Param("limiteCarritos") Integer limiteCarritos,
    @Param("limiteMinutosBajo") Double limiteMinutosBajo,
    @Param("limiteMinutosAlto") Double limiteMinutosAlto,
    @Param("limiteAbandonoPorcentaje") Double limiteAbandonoPorcentaje
  );

  // ========== Análisis de Performance ==========

  /**
   * Métricas de performance del sistema
   */
  @Query(
    value = """
        SELECT
            COUNT(*) as totalOperaciones,
            AVG(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en)) as tiempoPromedioSegundos,
            COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en) > :limiteSegundos THEN 1 END) as operacionesLentas,
            MAX(TIMESTAMPDIFF(SECOND, c.creado_en, c.actualizado_en)) as tiempoMaximoSegundos,
            COUNT(CASE WHEN c.estado = 'ERROR' THEN 1 END) as errores
        FROM carritos c
        WHERE c.creado_en BETWEEN :fechaInicio AND :fechaFin
          AND c.actualizado_en IS NOT NULL
    """,
    nativeQuery = true
  )
  Object[] findMetricasPerformance(
    @Param("fechaInicio") java.sql.Timestamp fechaInicio,
    @Param("fechaFin") java.sql.Timestamp fechaFin,
    @Param("limiteSegundos") Double limiteSegundos
  );

  // ========== Métodos de Registro de Eventos ==========

  /**
   * Registra evento de agregar item al carrito
   */
  default void registrarEventoAgregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    BigDecimal valor
  ) {
    // Implementación por defecto - se puede override en implementación personalizada
    // O usar un enfoque de eventos con @EventListener
  }

  /**
   * Registra evento de actualizar cantidad de item
   */
  default void registrarEventoActualizarCantidad(
    Long carritoId,
    Long productoId,
    Integer cantidadAnterior,
    Integer cantidadNueva
  ) {
    // Implementación por defecto
  }

  /**
   * Registra evento de remover item del carrito
   */
  default void registrarEventoRemoverItem(
    Long carritoId,
    Long productoId,
    BigDecimal valor
  ) {
    // Implementación por defecto
  }

  /**
   * Registra evento de vaciar carrito completo
   */
  default void registrarEventoVaciarCarrito(
    Long carritoId,
    BigDecimal valorTotal,
    int cantidadItems
  ) {
    // Implementación por defecto
  }

  /**
   * Registra evento de abandono de carrito
   */
  default void registrarEventoAbandonoCarrito(
    Long carritoId,
    Long usuarioId,
    BigDecimal valorTotal,
    int cantidadItems
  ) {
    // Implementación por defecto
  }
}
