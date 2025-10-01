package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio empresarial para gestión avanzada de carritos de compras.
 *
 * Principios SOLID aplicados:
 * - Single Responsibility: Gestión completa del dominio carrito
 * - Open/Closed: Extensible para nuevas funcionalidades
 * - Interface Segregation: Métodos cohesivos y específicos
 * - Dependency Inversion: Abstracción para implementaciones
 *
 * Características empresariales:
 * - Operaciones transaccionales robustas
 * - Integración con microservicios externos
 * - Cache distribuido para performance
 * - Business Intelligence integrado
 * - Audit Trail completo
 * - Validaciones de negocio avanzadas
 * - Manejo de eventos y notificaciones
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
public interface CarritoService {
  // ===============================
  // OPERACIONES PRINCIPALES DE CARRITO
  // ===============================

  /**
   * Obtiene el carrito activo de un usuario.
   * Si no existe, crea uno nuevo automáticamente.
   * Utiliza cache distribuido para optimizar performance.
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto con información completa del carrito
   * @throws UsuarioNotFoundException si el usuario no existe
   * @throws BusinessException si hay errores de validación
   */
  CarritoDto obtenerCarritoPorUsuario(Long usuarioId);

  /**
   * Obtiene el carrito por ID específico con validaciones de seguridad.
   * Verifica que el usuario tenga permisos sobre el carrito.
   *
   * @param carritoId ID del carrito
   * @param usuarioId ID del usuario (para validación de permisos)
   * @return Optional<CarritoDto> carrito si existe y tiene permisos
   */
  Optional<CarritoDto> obtenerCarritoPorId(Long carritoId, Long usuarioId);

  /**
   * Crea un nuevo carrito para el usuario especificado.
   * Utiliza IP del request actual si está disponible.
   *
   * @param usuarioId ID del usuario
   * @param ipCliente IP del cliente para auditoría
   * @return CarritoDto carrito recién creado
   */
  CarritoDto crearCarrito(Long usuarioId, String ipCliente);

  /**
   * Crea un nuevo carrito para el usuario especificado (sobrecarga sin IP).
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto carrito recién creado
   */
  default CarritoDto crearCarrito(Long usuarioId) {
    return crearCarrito(usuarioId, null);
  }

  // ===============================
  // GESTIÓN DE ITEMS
  // ===============================

  /**
   * Agrega un item al carrito con validaciones empresariales completas.
   *
   * Validaciones aplicadas:
   * - Existencia y disponibilidad del producto
   * - Stock suficiente
   * - Límites de cantidad por producto
   * - Límites de items totales en carrito
   * - Precios actualizados en tiempo real
   *
   * @param usuarioId ID del usuario
   * @param itemRequest Datos del item a agregar
   * @return CarritoDto carrito actualizado
   */
  CarritoDto agregarItem(Long usuarioId, ItemCarritoRequestDto itemRequest);

  /**
   * Actualiza la cantidad de un item específico en el carrito.
   * Incluye validaciones de stock y reglas de negocio.
   *
   * @param usuarioId ID del usuario
   * @param productoId ID del producto
   * @param nuevaCantidad Nueva cantidad solicitada
   * @return CarritoDto carrito actualizado
   */
  CarritoDto actualizarCantidadItem(
    Long usuarioId,
    Long productoId,
    Integer nuevaCantidad
  );

  /**
   * Remueve completamente un item del carrito.
   * Actualiza totales y descuentos aplicables.
   *
   * @param usuarioId ID del usuario
   * @param productoId ID del producto a remover
   * @return CarritoDto carrito actualizado
   */
  CarritoDto removerItem(Long usuarioId, Long productoId);

  /**
   * Elimina completamente un item del carrito (alias de removerItem).
   *
   * @param usuarioId ID del usuario
   * @param productoId ID del producto a eliminar
   * @return CarritoDto carrito actualizado
   */
  default CarritoDto eliminarItem(Long usuarioId, Long productoId) {
    return removerItem(usuarioId, productoId);
  }

  /**
   * Agrega múltiples items al carrito en una sola operación.
   *
   * @param usuarioId ID del usuario
   * @param itemsRequest Lista de items a agregar
   * @return CarritoDto carrito actualizado
   */
  CarritoDto agregarItemsMasivo(
    Long usuarioId,
    List<ItemCarritoRequestDto> itemsRequest
  );

  /**
   * Vacía completamente el carrito manteniendo el historial.
   * Marca el carrito como abandonado para analytics.
   *
   * @param usuarioId ID del usuario
   */
  void vaciarCarrito(Long usuarioId);

  // ===============================
  // GESTIÓN DE DESCUENTOS Y PROMOCIONES
  // ===============================

  /**
   * Aplica un código de descuento al carrito.
   * Valida vigencia, condiciones y límites de uso.
   *
   * @param usuarioId ID del usuario
   * @param codigoDescuento Código del descuento a aplicar
   * @return CarritoDto carrito con descuento aplicado
   */
  CarritoDto aplicarDescuento(Long usuarioId, String codigoDescuento);

  /**
   * Remueve el descuento actual del carrito.
   * Recalcula totales automáticamente.
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto carrito sin descuentos
   */
  CarritoDto removerDescuento(Long usuarioId);

  /**
   * Valida si un código de descuento es aplicable al carrito actual.
   * No aplica el descuento, solo valida condiciones.
   *
   * @param usuarioId ID del usuario
   * @param codigoDescuento Código a validar
   * @return ValidacionDescuentoDto resultado de la validación
   */
  ValidacionDescuentoDto validarDescuento(
    Long usuarioId,
    String codigoDescuento
  );

  // ===============================
  // GESTIÓN DE ESTADOS Y PROCESOS
  // ===============================

  /**
   * Marca el carrito como procesado (convertido en pedido).
   * Finaliza el ciclo de vida del carrito.
   *
   * @param usuarioId ID del usuario
   * @param pedidoId ID del pedido generado
   * @return CarritoDto carrito marcado como procesado
   */
  CarritoDto marcarComoProcesado(Long usuarioId, Long pedidoId);

  /**
   * Marca carritos como abandonados basándose en tiempo de inactividad.
   * Proceso automatizado para analytics y remarketing.
   *
   * @param horasInactividad Horas de inactividad para considerar abandono
   * @return List<Long> IDs de carritos marcados como abandonados
   */
  List<Long> marcarCarritosAbandonados(int horasInactividad);

  /**
   * Recupera un carrito abandonado y lo marca como activo.
   * Actualiza precios y disponibilidad de productos.
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto carrito recuperado y actualizado
   */
  CarritoDto recuperarCarritoAbandonado(Long usuarioId);

  // ===============================
  // CONSULTAS Y ANALYTICS
  // ===============================

  /**
   * Obtiene el historial de carritos del usuario con paginación.
   * Incluye carritos procesados y abandonados.
   *
   * @param usuarioId ID del usuario
   * @param pageable Configuración de paginación
   * @return Page<CarritoResumenDto> historial paginado
   */
  Page<CarritoResumenDto> obtenerHistorialCarritos(
    Long usuarioId,
    Pageable pageable
  );

  /**
   * Calcula métricas del carrito en tiempo real.
   * Incluye savings, comparaciones de precios, etc.
   *
   * @param usuarioId ID del usuario
   * @return MetricasCarritoDto métricas calculadas
   */
  MetricasCarritoDto calcularMetricasCarrito(Long usuarioId);

  /**
   * Obtiene recomendaciones personalizadas para el carrito.
   * Basado en historial, productos relacionados y analytics.
   *
   * @param usuarioId ID del usuario
   * @param limite Número máximo de recomendaciones
   * @return List<ProductoRecomendadoDto> productos recomendados
   */
  List<ProductoRecomendadoDto> obtenerRecomendaciones(
    Long usuarioId,
    int limite
  );

  // ===============================
  // VALIDACIONES DE NEGOCIO
  // ===============================

  /**
   * Valida la consistencia completa del carrito.
   * Verifica precios, stock, descuentos y reglas de negocio.
   *
   * @param usuarioId ID del usuario
   * @return ValidacionCarritoDto resultado de validaciones
   */
  ValidacionCarritoDto validarCarrito(Long usuarioId);

  /**
   * Sincroniza precios y disponibilidad con el servicio de productos.
   * Actualiza automáticamente items desactualizados.
   *
   * @param usuarioId ID del usuario
   * @return CarritoDto carrito sincronizado
   */
  CarritoDto sincronizarCarrito(Long usuarioId);

  // ===============================
  // OPERACIONES MASIVAS
  // ===============================

  /**
   * Agrega múltiples items al carrito en una sola transacción.
   * Optimizado para imports masivos y APIs batch.
   *
   * @param usuarioId ID del usuario
   * @param items Lista de items a agregar
   * @return CarritoDto carrito con todos los items agregados
   */
  CarritoDto agregarMultiplesItems(
    Long usuarioId,
    List<ItemCarritoRequestDto> items
  );

  /**
   * Transfiere items de un carrito temporal/anónimo a un carrito de usuario.
   * Utilizado en procesos de login post-navegación anónima.
   *
   * @param carritoTemporalId ID del carrito temporal
   * @param usuarioId ID del usuario destino
   * @return CarritoDto carrito del usuario con items transferidos
   */
  CarritoDto transferirCarrito(Long carritoTemporalId, Long usuarioId);

  // ===============================
  // MÉTRICAS Y REPORTING
  // ===============================

  /**
   * Obtiene estadísticas avanzadas del carrito para business intelligence.
   * Incluye patrones de comportamiento y métricas de conversión.
   *
   * @param usuarioId ID del usuario
   * @param fechaInicio Fecha de inicio del período
   * @param fechaFin Fecha de fin del período
   * @return Map<String, Object> métricas del período (temporal hasta resolver visibilidad)
   */
  Map<String, Object> obtenerEstadisticas(
    Long usuarioId,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Calcula el valor total de carritos abandonados para remarketing.
   * Métricas empresariales para recuperación de ventas.
   *
   * @param fechaInicio Fecha de inicio del período
   * @param fechaFin Fecha de fin del período
   * @return BigDecimal valor total de carritos abandonados
   */
  BigDecimal calcularValorCarritosAbandonados(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  // ===============================
  // MÉTODOS ADICIONALES PARA CONTROLADOR
  // ===============================

  /**
   * Calcula los totales del carrito del usuario.
   *
   * @param usuarioId ID del usuario
   * @return CarritoResumenDto resumen con totales calculados
   */
  CarritoResumenDto calcularTotales(Long usuarioId);

  /**
   * Obtiene métricas específicas del carrito.
   *
   * @param usuarioId ID del usuario
   * @param fechaInicio Fecha de inicio
   * @param fechaFin Fecha de fin
   * @return MetricasCarritoDto métricas del carrito
   */
  MetricasCarritoDto obtenerMetricasCarrito(
    Long usuarioId,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Obtiene estadísticas globales del sistema.
   *
   * @param fechaInicio Fecha de inicio
   * @param fechaFin Fecha de fin
   * @return EstadisticasCarritoDto estadísticas globales
   */
  Map<String, Object> obtenerEstadisticasGlobales(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  /**
   * Exporta datos del carrito en formato específico.
   *
   * @param formato Formato de exportación (CSV, EXCEL, PDF)
   * @param fechaInicio Fecha de inicio
   * @param fechaFin Fecha de fin
   * @return byte[] datos exportados
   */
  byte[] exportarDatos(
    String formato,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  );

  // ===============================
  // MÉTODOS ADMINISTRATIVOS AVANZADOS
  // ===============================

  /**
   * Cuenta el número total de carritos activos.
   *
   * @return long número de carritos activos
   */
  long contarCarritosActivos();

  /**
   * Cuenta el número total de items en todos los carritos.
   *
   * @return long número total de items globales
   */
  long contarItemsGlobales();

  /**
   * Calcula el valor promedio de todos los carritos.
   *
   * @return BigDecimal valor promedio de carritos
   */
  BigDecimal calcularValorPromedioCarritos();

  /**
   * Obtiene la tendencia semanal de carritos.
   *
   * @return List<Map<String, Object>> datos de tendencia semanal
   */
  List<Map<String, Object>> obtenerTendenciaSemanal();

  /**
   * Obtiene los productos más agregados a carritos.
   *
   * @param limite número de productos a retornar
   * @return List<Map<String, Object>> top productos en carritos
   */
  List<Map<String, Object>> obtenerTopProductosEnCarritos(int limite);

  /**
   * Obtiene alertas administrativas del sistema.
   *
   * @return List<Map<String, Object>> alertas administrativas
   */
  List<Map<String, Object>> obtenerAlertasAdministrativas();

  /**
   * Obtiene estadísticas detalladas del sistema.
   *
   * @param fechaInicio fecha de inicio del periodo
   * @param fechaFin fecha de fin del periodo
   * @param filtroUsuario filtro opcional por usuario
   * @return EstadisticasCarritoDto estadísticas detalladas
   */
  Map<String, Object> obtenerEstadisticasDetalladas(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    String filtroUsuario
  );

  /**
   * Lista todos los carritos con filtros opcionales.
   *
   * @param pageable información de paginación
   * @param estado filtro por estado
   * @param montoMinimo filtro por monto mínimo
   * @param montoMaximo filtro por monto máximo
   * @param usuarioId filtro por usuario
   * @return Page<CarritoDto> página de carritos
   */
  Page<CarritoDto> listarTodosLosCarritos(
    Pageable pageable,
    String estado,
    Double montoMinimo,
    Double montoMaximo,
    Long usuarioId
  );

  /**
   * Busca carritos por criterios específicos.
   *
   * @param criterios mapa de criterios de búsqueda
   * @param pageable información de paginación
   * @return Page<CarritoDto> página de carritos encontrados
   */
  Page<CarritoDto> buscarCarritosPorCriterios(
    Map<String, Object> criterios,
    Pageable pageable
  );

  /**
   * Limpia carritos abandonados.
   *
   * @param diasAbandonados número de días para considerar abandono
   * @return int número de carritos eliminados
   */
  int limpiarCarritosAbandonados(int diasAbandonados);

  /**
   * Optimiza la base de datos.
   *
   * @return Map<String, Object> resultado de la optimización
   */
  Map<String, Object> optimizarBaseDatos();

  /**
   * Obtiene la configuración del sistema.
   *
   * @return Map<String, Object> configuración actual
   */
  Map<String, Object> obtenerConfiguracionSistema();

  /**
   * Actualiza la configuración del sistema.
   *
   * @param configuracion nueva configuración
   * @return Map<String, Object> configuración actualizada
   */
  Map<String, Object> actualizarConfiguracionSistema(
    Map<String, Object> configuracion
  );

  /**
   * Genera un reporte personalizado.
   *
   * @param parametros parámetros del reporte
   * @return Map<String, Object> datos del reporte
   */
  Map<String, Object> generarReportePersonalizado(
    Map<String, Object> parametros
  );

  /**
   * Programa una exportación masiva de datos.
   *
   * @param parametros parámetros de la exportación
   * @return String ID del trabajo programado
   */
  String programarExportacionMasiva(Map<String, Object> parametros);

  /**
   * Obtiene métricas de rendimiento del sistema.
   *
   * @return Map<String, Object> métricas de rendimiento
   */
  Map<String, Object> obtenerMetricasRendimiento();

  /**
   * Verifica la salud del sistema.
   *
   * @return Map<String, Object> estado de salud del sistema
   */
  Map<String, Object> verificarSaludSistema();
}
