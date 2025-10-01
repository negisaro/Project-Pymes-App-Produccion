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
    """
    SELECT c.usuarioId,
           AVG(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))/60) as tiempoPromedioMinutos,
           COUNT(c) as totalSesiones,
           AVG(c.total) as valorPromedio
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    AND c.actualizadoEn > c.creadoEn
    GROUP BY c.usuarioId
    HAVING COUNT(c) >= :minimoSesiones
    ORDER BY tiempoPromedioMinutos DESC
    """
  )
  List<Object[]> findPatronesTiempoSesion(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
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
    """
    SELECT
        CASE
            WHEN SUM(c.total) >= :valorVIP THEN 'VIP'
            WHEN SUM(c.total) >= :valorPremium THEN 'Premium'
            WHEN SUM(c.total) >= :valorRegular THEN 'Regular'
            ELSE 'Básico'
        END as segmento,
        COUNT(DISTINCT c.usuarioId) as cantidadUsuarios,
        AVG(SUM(c.total)) as valorPromedioPorUsuario,
        SUM(SUM(c.total)) as valorTotalSegmento
    FROM Carrito c
    WHERE c.estado = 'PROCESADO'
    AND c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY c.usuarioId,
        CASE
            WHEN SUM(c.total) >= :valorVIP THEN 'VIP'
            WHEN SUM(c.total) >= :valorPremium THEN 'Premium'
            WHEN SUM(c.total) >= :valorRegular THEN 'Regular'
            ELSE 'Básico'
        END
    ORDER BY valorTotalSegmento DESC
    """
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
    SELECT EXTRACT(DOW FROM c.creadoEn) as diaSemana,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
           AVG(c.total) as valorPromedio,
           SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasTotales
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY EXTRACT(DOW FROM c.creadoEn)
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
    SELECT EXTRACT(YEAR FROM c.creadoEn) as año,
           EXTRACT(MONTH FROM c.creadoEn) as mes,
           COUNT(c) as totalCarritos,
           COUNT(CASE WHEN c.estado = 'PROCESADO' THEN 1 END) as procesados,
           SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasTotales,
           COUNT(DISTINCT c.usuarioId) as usuariosUnicos,
           AVG(c.total) as ticketPromedio
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY EXTRACT(YEAR FROM c.creadoEn), EXTRACT(MONTH FROM c.creadoEn)
    ORDER BY año DESC, mes DESC
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
    """
    WITH PrimeraCompra AS (
        SELECT usuarioId, MIN(creadoEn) as primeraCompra
        FROM Carrito
        WHERE estado = 'PROCESADO'
        GROUP BY usuarioId
    ),
    UsuariosRecurrentes AS (
        SELECT pc.usuarioId
        FROM PrimeraCompra pc
        JOIN Carrito c ON c.usuarioId = pc.usuarioId
        WHERE c.estado = 'PROCESADO'
        AND c.creadoEn > pc.primeraCompra
        AND c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    )
    SELECT
        COUNT(DISTINCT pc.usuarioId) as usuariosNuevos,
        COUNT(DISTINCT ur.usuarioId) as usuariosRecurrentes,
        (COUNT(DISTINCT ur.usuarioId) * 100.0 / NULLIF(COUNT(DISTINCT pc.usuarioId), 0)) as tasaRetencion
    FROM PrimeraCompra pc
    LEFT JOIN UsuariosRecurrentes ur ON pc.usuarioId = ur.usuarioId
    WHERE pc.primeraCompra BETWEEN :fechaInicio AND :fechaFin
    """
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
    """
    WITH CohorteUsuarios AS (
        SELECT usuarioId,
               DATE_TRUNC('month', MIN(creadoEn)) as cohorte
        FROM Carrito
        WHERE estado = 'PROCESADO'
        GROUP BY usuarioId
    )
    SELECT
        cu.cohorte,
        COUNT(DISTINCT cu.usuarioId) as usuariosCohorte,
        COUNT(DISTINCT c.usuarioId) as usuariosActivos,
        (COUNT(DISTINCT c.usuarioId) * 100.0 / COUNT(DISTINCT cu.usuarioId)) as retencionPorcentaje,
        SUM(CASE WHEN c.estado = 'PROCESADO' THEN c.total ELSE 0 END) as ventasCohorte
    FROM CohorteUsuarios cu
    LEFT JOIN Carrito c ON cu.usuarioId = c.usuarioId
        AND c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    WHERE cu.cohorte BETWEEN :cohorteInicio AND :cohorteFin
    GROUP BY cu.cohorte
    ORDER BY cu.cohorte
    """
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
    """
    SELECT c.usuarioId,
           COUNT(c) as carritosCreados,
           AVG(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))/60) as tiempoPromedioMinutos,
           AVG(c.total) as valorPromedio,
           COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) as abandonos
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY c.usuarioId
    HAVING COUNT(c) > :limiteCarritos
        OR AVG(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))/60) < :limiteMinutosBajo
        OR AVG(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))/60) > :limiteMinutosAlto
        OR (COUNT(CASE WHEN c.estado = 'ABANDONADO' THEN 1 END) * 100.0 / COUNT(c)) > :limiteAbandonoPorcentaje
    ORDER BY carritosCreados DESC, tiempoPromedioMinutos ASC
    """
  )
  List<Object[]> findPatronesAnomalos(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
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
    """
    SELECT
        COUNT(c) as totalOperaciones,
        AVG(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))) as tiempoPromedioSegundos,
        COUNT(CASE WHEN EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn)) > :limiteSegundos THEN 1 END) as operacionesLentas,
        MAX(EXTRACT(EPOCH FROM (c.actualizadoEn - c.creadoEn))) as tiempoMaximoSegundos,
        COUNT(CASE WHEN c.estado = 'ERROR' THEN 1 END) as errores
    FROM Carrito c
    WHERE c.creadoEn BETWEEN :fechaInicio AND :fechaFin
    AND c.actualizadoEn IS NOT NULL
    """
  )
  Object[] findMetricasPerformance(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin,
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
