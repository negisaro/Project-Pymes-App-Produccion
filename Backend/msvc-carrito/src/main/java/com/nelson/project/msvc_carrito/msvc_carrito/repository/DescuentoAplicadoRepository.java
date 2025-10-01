package com.nelson.project.msvc_carrito.msvc_carrito.repository;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.DescuentoAplicado;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.DescuentoAplicado.TipoDescuento;
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
 * Repositorio para gestión de descuentos aplicados.
 * Proporciona consultas especializadas para promociones y análisis de efectividad.
 *
 * Características:
 * - Gestión completa de descuentos
 * - Análisis de efectividad de promociones
 * - Validaciones de negocio
 * - Consultas de auditoría
 */
@Repository
public interface DescuentoAplicadoRepository
  extends JpaRepository<DescuentoAplicado, Long> {
  // ========== Consultas por Carrito ==========

  /**
   * Obtiene todos los descuentos activos de un carrito
   */
  List<DescuentoAplicado> findByCarritoIdAndActivoTrueOrderByAplicadoEnDesc(
    Long carritoId
  );

  /**
   * Obtiene todos los descuentos de un carrito (activos e inactivos)
   */
  List<DescuentoAplicado> findByCarritoIdOrderByAplicadoEnDesc(Long carritoId);

  /**
   * Verifica si un carrito tiene descuentos activos
   */
  boolean existsByCarritoIdAndActivoTrue(Long carritoId);

  /**
   * Cuenta descuentos activos en un carrito
   */
  Long countByCarritoIdAndActivoTrue(Long carritoId);

  // ========== Consultas por Código de Descuento ==========

  /**
   * Busca descuento por código y carrito específico
   */
  Optional<DescuentoAplicado> findByCodigoDescuentoAndCarritoId(
    String codigoDescuento,
    Long carritoId
  );

  /**
   * Busca descuento activo por código y carrito
   */
  Optional<DescuentoAplicado> findByCodigoDescuentoAndCarritoIdAndActivoTrue(
    String codigoDescuento,
    Long carritoId
  );

  /**
   * Obtiene todos los usos de un código de descuento
   */
  List<DescuentoAplicado> findByCodigoDescuentoOrderByAplicadoEnDesc(
    String codigoDescuento
  );

  /**
   * Cuenta cuántas veces se ha usado un código
   */
  Long countByCodigoDescuento(String codigoDescuento);

  /**
   * Cuenta usos activos de un código
   */
  Long countByCodigoDescuentoAndActivoTrue(String codigoDescuento);

  // ========== Consultas por Tipo de Descuento ==========

  /**
   * Obtiene descuentos por tipo en un período
   */
  List<
    DescuentoAplicado
  > findByTipoDescuentoAndAplicadoEnBetweenOrderByAplicadoEnDesc(
    TipoDescuento tipoDescuento,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Cuenta descuentos por tipo
   */
  Long countByTipoDescuento(TipoDescuento tipoDescuento);

  /**
   * Obtiene descuentos activos por tipo
   */
  List<DescuentoAplicado> findByTipoDescuentoAndActivoTrueOrderByAplicadoEnDesc(
    TipoDescuento tipoDescuento
  );

  // ========== Consultas por Usuario ==========

  /**
   * Obtiene descuentos aplicados por un usuario específico
   */
  List<DescuentoAplicado> findByUsuarioAplicacionOrderByAplicadoEnDesc(
    String usuarioAplicacion
  );

  /**
   * Obtiene descuentos de todos los carritos de un usuario
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    JOIN Carrito c ON d.carritoId = c.id
    WHERE c.usuarioId = :usuarioId
    ORDER BY d.aplicadoEn DESC
    """
  )
  List<DescuentoAplicado> findByUsuarioIdOrderByAplicadoEnDesc(
    @Param("usuarioId") Long usuarioId
  );

  /**
   * Obtiene descuentos activos de un usuario
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    JOIN Carrito c ON d.carritoId = c.id
    WHERE c.usuarioId = :usuarioId
    AND d.activo = true
    ORDER BY d.aplicadoEn DESC
    """
  )
  List<DescuentoAplicado> findDescuentosActivosByUsuarioId(
    @Param("usuarioId") Long usuarioId
  );

  // ========== Consultas por Fecha ==========

  /**
   * Obtiene descuentos aplicados en un rango de fechas
   */
  List<DescuentoAplicado> findByAplicadoEnBetweenOrderByAplicadoEnDesc(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Obtiene descuentos aplicados hoy
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    WHERE DATE(d.aplicadoEn) = CURRENT_DATE
    ORDER BY d.aplicadoEn DESC
    """
  )
  List<DescuentoAplicado> findDescuentosDeHoy();

  /**
   * Obtiene descuentos que expiran pronto
   */
  List<
    DescuentoAplicado
  > findByValidoHastaBetweenAndActivoTrueOrderByValidoHastaAsc(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Obtiene descuentos expirados que aún están activos
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    WHERE d.validoHasta < :fechaActual
    AND d.activo = true
    ORDER BY d.validoHasta DESC
    """
  )
  List<DescuentoAplicado> findDescuentosExpiradosActivos(
    @Param("fechaActual") LocalDateTime fechaActual
  );

  // ========== Análisis y Estadísticas ==========

  /**
   * Obtiene estadísticas de descuentos por tipo
   */
  @Query(
    """
    SELECT d.tipoDescuento,
           COUNT(d) as totalAplicados,
           SUM(d.descuentoCalculado) as montoTotalDescuento,
           AVG(d.descuentoCalculado) as promedioDescuento
    FROM DescuentoAplicado d
    WHERE d.aplicadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY d.tipoDescuento
    ORDER BY totalAplicados DESC
    """
  )
  List<Object[]> findEstadisticasPorTipo(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene códigos de descuento más populares
   */
  @Query(
    """
    SELECT d.codigoDescuento,
           COUNT(d) as usos,
           SUM(d.descuentoCalculado) as montoTotal,
           AVG(d.descuentoCalculado) as promedio
    FROM DescuentoAplicado d
    WHERE d.aplicadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY d.codigoDescuento
    ORDER BY usos DESC
    """
  )
  List<Object[]> findCodigosMasPopulares(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Calcula el monto total de descuentos en un período
   */
  @Query(
    """
    SELECT COALESCE(SUM(d.descuentoCalculado), 0)
    FROM DescuentoAplicado d
    WHERE d.aplicadoEn BETWEEN :fechaInicio AND :fechaFin
    AND d.activo = true
    """
  )
  BigDecimal calculateMontoTotalDescuentos(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  /**
   * Obtiene top usuarios que más descuentos han usado
   */
  @Query(
    """
    SELECT c.usuarioId,
           COUNT(d) as totalDescuentos,
           SUM(d.descuentoCalculado) as montoAhorrado
    FROM DescuentoAplicado d
    JOIN Carrito c ON d.carritoId = c.id
    WHERE d.aplicadoEn BETWEEN :fechaInicio AND :fechaFin
    GROUP BY c.usuarioId
    ORDER BY totalDescuentos DESC
    """
  )
  List<Object[]> findTopUsuariosConDescuentos(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Consultas por Campaña ==========

  /**
   * Obtiene descuentos de una campaña específica
   */
  List<DescuentoAplicado> findByCampanaIdOrderByAplicadoEnDesc(
    String campanaId
  );

  /**
   * Obtiene estadísticas de una campaña
   */
  @Query(
    """
    SELECT COUNT(d) as usos,
           SUM(d.descuentoCalculado) as montoTotal,
           AVG(d.descuentoCalculado) as promedio,
           MIN(d.aplicadoEn) as primerUso,
           MAX(d.aplicadoEn) as ultimoUso
    FROM DescuentoAplicado d
    WHERE d.campanaId = :campanaId
    """
  )
  Object[] findEstadisticasCampana(@Param("campanaId") String campanaId);

  // ========== Consultas por Origen ==========

  /**
   * Obtiene descuentos por origen (web, app, etc.)
   */
  List<DescuentoAplicado> findByOrigenOrderByAplicadoEnDesc(String origen);

  /**
   * Estadísticas por origen
   */
  @Query(
    """
    SELECT d.origen,
           COUNT(d) as total,
           SUM(d.descuentoCalculado) as monto
    FROM DescuentoAplicado d
    WHERE d.aplicadoEn BETWEEN :fechaInicio AND :fechaFin
    AND d.origen IS NOT NULL
    GROUP BY d.origen
    ORDER BY total DESC
    """
  )
  List<Object[]> findEstadisticasPorOrigen(
    @Param("fechaInicio") LocalDateTime fechaInicio,
    @Param("fechaFin") LocalDateTime fechaFin
  );

  // ========== Operaciones de Actualización ==========

  /**
   * Marca descuentos como removidos por expiración
   */
  @Modifying
  @Query(
    """
    UPDATE DescuentoAplicado d
    SET d.activo = false,
        d.removidoEn = :fechaActual,
        d.motivoRemocion = 'Expirado automáticamente'
    WHERE d.validoHasta < :fechaActual
    AND d.activo = true
    """
  )
  int marcarDescuentosExpirados(
    @Param("fechaActual") LocalDateTime fechaActual
  );

  /**
   * Extiende validez de descuentos de una campaña
   */
  @Modifying
  @Query(
    """
    UPDATE DescuentoAplicado d
    SET d.validoHasta = :nuevaFecha
    WHERE d.campanaId = :campanaId
    AND d.activo = true
    AND (d.validoHasta IS NULL OR d.validoHasta < :nuevaFecha)
    """
  )
  int extenderValidezCampana(
    @Param("campanaId") String campanaId,
    @Param("nuevaFecha") LocalDateTime nuevaFecha
  );

  // ========== Consultas de Validación ==========

  /**
   * Verifica si un código ya está siendo usado en un carrito activo
   */
  @Query(
    """
    SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
    FROM DescuentoAplicado d
    WHERE d.codigoDescuento = :codigo
    AND d.carritoId = :carritoId
    AND d.activo = true
    """
  )
  boolean existeCodigoActivoEnCarrito(
    @Param("codigo") String codigo,
    @Param("carritoId") Long carritoId
  );

  /**
   * Obtiene descuentos que requieren validación de monto mínimo
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    WHERE d.carritoId = :carritoId
    AND d.activo = true
    AND d.montoMinimo IS NOT NULL
    AND d.montoMinimo > 0
    """
  )
  List<DescuentoAplicado> findDescuentosConMontoMinimo(
    @Param("carritoId") Long carritoId
  );

  // ========== Consultas de Limpieza ==========

  /**
   * Elimina descuentos antiguos removidos (para mantenimiento)
   */
  @Modifying
  @Query(
    """
    DELETE FROM DescuentoAplicado d
    WHERE d.activo = false
    AND d.removidoEn < :fechaLimite
    """
  )
  int eliminarDescuentosAntiguos(
    @Param("fechaLimite") LocalDateTime fechaLimite
  );

  /**
   * Cuenta descuentos por eliminar
   */
  Long countByActivoFalseAndRemovidoEnBefore(LocalDateTime fechaLimite);

  // ========== Consultas Personalizadas ==========

  /**
   * Busca descuentos similares para evitar duplicados
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    WHERE d.carritoId = :carritoId
    AND d.tipoDescuento = :tipo
    AND d.activo = true
    AND (
        (d.codigoDescuento = :codigo) OR
        (d.campanaId = :campanaId AND d.campanaId IS NOT NULL)
    )
    """
  )
  List<DescuentoAplicado> findDescuentosSimilares(
    @Param("carritoId") Long carritoId,
    @Param("tipo") TipoDescuento tipo,
    @Param("codigo") String codigo,
    @Param("campanaId") String campanaId
  );

  /**
   * Obtiene descuentos que pueden ser combinables
   */
  @Query(
    """
    SELECT d FROM DescuentoAplicado d
    WHERE d.carritoId = :carritoId
    AND d.activo = true
    AND d.tipoDescuento != :tipoExcluir
    """
  )
  List<DescuentoAplicado> findDescuentosCombinable(
    @Param("carritoId") Long carritoId,
    @Param("tipoExcluir") TipoDescuento tipoExcluir
  );
}
