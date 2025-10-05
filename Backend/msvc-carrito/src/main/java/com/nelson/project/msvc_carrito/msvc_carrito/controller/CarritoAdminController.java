package com.nelson.project.msvc_carrito.msvc_carrito.controller;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.*;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operaciones administrativas del carrito de compras.
 *
 * Proporciona endpoints especializados para administradores del sistema:
 * - Monitoreo y estadísticas globales
 * - Gestión de carritos de múltiples usuarios
 * - Reportes avanzados y análisis de datos
 * - Operaciones de mantenimiento y limpieza
 * - Configuración de límites y parámetros del sistema
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/v1/admin/carrito")
@Tag(
  name = "Administración de Carritos",
  description = "API administrativa para gestión avanzada de carritos"
)
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class CarritoAdminController {

  private static final Logger log = LoggerFactory.getLogger(
    CarritoAdminController.class
  );

  private final CarritoService carritoService;

  // ============================================
  // MONITOREO Y ESTADÍSTICAS GLOBALES
  // ============================================

  @Operation(
    summary = "Dashboard de carritos activos",
    description = "Obtiene un resumen ejecutivo de todos los carritos activos en el sistema"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Dashboard recuperado exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/dashboard")
  public ResponseEntity<Map<String, Object>> getDashboard() {
    log.info("Generando dashboard administrativo de carritos");

    Map<String, Object> dashboard = Map.of(
      "timestamp",
      LocalDateTime.now(),
      "carritosActivos",
      carritoService.contarCarritosActivos(),
      "totalItems",
      carritoService.contarItemsGlobales(),
      "valorPromedio",
      carritoService.calcularValorPromedioCarritos(),
      "tendenciaSemanal",
      carritoService.obtenerTendenciaSemanal(),
      "topProductos",
      carritoService.obtenerTopProductosEnCarritos(10),
      "alertas",
      carritoService.obtenerAlertasAdministrativas()
    );

    log.info("Dashboard generado exitosamente");
    return ResponseEntity.ok(dashboard);
  }

  @Operation(
    summary = "Estadísticas detalladas por período",
    description = "Recupera análisis estadístico completo de carritos en un rango de fechas"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Estadísticas recuperadas exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/estadisticas")
  public ResponseEntity<Map<String, Object>> getEstadisticasDetalladas(
    @Parameter(description = "Fecha de inicio (ISO 8601)") @RequestParam(
      required = false
    ) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaInicio,
    @Parameter(description = "Fecha de fin (ISO 8601)") @RequestParam(
      required = false
    ) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaFin,
    @Parameter(
      description = "Agrupar por período",
      example = "DAY"
    ) @RequestParam(defaultValue = "DAY") String agrupadoPor
  ) {
    log.info(
      "Generando estadísticas detalladas - Período: {} a {}, Agrupado por: {}",
      fechaInicio,
      fechaFin,
      agrupadoPor
    );

    Map<String, Object> estadisticas =
      carritoService.obtenerEstadisticasDetalladas(
        fechaInicio,
        fechaFin,
        agrupadoPor
      );

    log.info("Estadísticas detalladas generadas exitosamente");
    return ResponseEntity.ok(estadisticas);
  }

  // ============================================
  // GESTIÓN MASIVA DE CARRITOS
  // ============================================

  @Operation(
    summary = "Listar todos los carritos con filtros",
    description = "Obtiene lista paginada de carritos con capacidades avanzadas de filtrado"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Lista de carritos recuperada exitosamente",
        content = @Content(schema = @Schema(implementation = Page.class))
      ),
    }
  )
  @GetMapping("/todos")
  public ResponseEntity<Page<CarritoDto>> listarTodosLosCarritos(
    @Parameter(description = "Número de página (0-based)") @RequestParam(
      defaultValue = "0"
    ) int page,
    @Parameter(description = "Tamaño de página") @RequestParam(
      defaultValue = "20"
    ) int size,
    @Parameter(description = "Campo de ordenamiento") @RequestParam(
      defaultValue = "fechaCreacion"
    ) String sortBy,
    @Parameter(description = "Dirección de ordenamiento") @RequestParam(
      defaultValue = "desc"
    ) String sortDir,
    @Parameter(description = "Filtrar por estado del carrito") @RequestParam(
      required = false
    ) String estado,
    @Parameter(description = "Valor mínimo del carrito") @RequestParam(
      required = false
    ) Double valorMinimo,
    @Parameter(description = "Valor máximo del carrito") @RequestParam(
      required = false
    ) Double valorMaximo,
    @Parameter(description = "ID del usuario (filtro)") @RequestParam(
      required = false
    ) Long usuarioId
  ) {
    log.info(
      "Listando todos los carritos - Página: {}, Tamaño: {}, Filtros aplicados",
      page,
      size
    );

    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<CarritoDto> carritos = carritoService.listarTodosLosCarritos(
      pageable,
      estado,
      valorMinimo,
      valorMaximo,
      usuarioId
    );

    log.info(
      "Lista de carritos recuperada exitosamente - {} carritos encontrados",
      carritos.getTotalElements()
    );
    return ResponseEntity.ok(carritos);
  }

  @Operation(
    summary = "Buscar carritos por criterios",
    description = "Búsqueda avanzada de carritos usando múltiples criterios"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Resultados de búsqueda recuperados exitosamente",
        content = @Content(schema = @Schema(implementation = Page.class))
      ),
    }
  )
  @PostMapping("/buscar")
  public ResponseEntity<Page<CarritoDto>> buscarCarritos(
    @Parameter(description = "Criterios de búsqueda") @RequestBody Map<
      String,
      Object
    > criterios,
    @Parameter(description = "Número de página") @RequestParam(
      defaultValue = "0"
    ) int page,
    @Parameter(description = "Tamaño de página") @RequestParam(
      defaultValue = "20"
    ) int size
  ) {
    log.info(
      "Búsqueda avanzada de carritos con {} criterios",
      criterios.size()
    );

    Pageable pageable = PageRequest.of(page, size);
    Page<CarritoDto> resultados = carritoService.buscarCarritosPorCriterios(
      criterios,
      pageable
    );

    log.info(
      "Búsqueda completada - {} carritos encontrados",
      resultados.getTotalElements()
    );
    return ResponseEntity.ok(resultados);
  }

  // ============================================
  // OPERACIONES DE MANTENIMIENTO
  // ============================================

  @Operation(
    summary = "Limpiar carritos abandonados",
    description = "Remueve carritos que han estado inactivos por un período especificado"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Limpieza completada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @DeleteMapping("/limpiar-abandonados")
  public ResponseEntity<Map<String, Object>> limpiarCarritosAbandonados(
    @Parameter(
      description = "Días de inactividad para considerar abandonado"
    ) @RequestParam(defaultValue = "30") int diasInactividad,
    @Parameter(description = "Confirmar la operación") @RequestParam(
      defaultValue = "false"
    ) boolean confirmar
  ) {
    if (!confirmar) {
      log.warn("Intento de limpieza sin confirmación explícita");
      return ResponseEntity.badRequest()
        .body(
          Map.of("error", "Debe confirmar la operación con confirmar=true")
        );
    }

    log.info(
      "Iniciando limpieza de carritos abandonados - {} días de inactividad",
      diasInactividad
    );

    int carritosEliminados = carritoService.limpiarCarritosAbandonados(
      diasInactividad
    );

    Map<String, Object> resultado = Map.of(
      "carritosEliminados",
      carritosEliminados,
      "diasInactividad",
      diasInactividad,
      "timestamp",
      LocalDateTime.now()
    );

    log.info(
      "Limpieza completada - {} carritos eliminados",
      carritosEliminados
    );
    return ResponseEntity.ok(resultado);
  }

  @Operation(
    summary = "Optimizar base de datos",
    description = "Ejecuta operaciones de optimización en la base de datos de carritos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Optimización completada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @PostMapping("/optimizar-bd")
  public ResponseEntity<Map<String, Object>> optimizarBaseDatos() {
    log.info("Iniciando optimización de base de datos");

    Map<String, Object> resultado = carritoService.optimizarBaseDatos();
    resultado.put("timestamp", LocalDateTime.now());

    log.info("Optimización de base de datos completada");
    return ResponseEntity.ok(resultado);
  }

  // ============================================
  // CONFIGURACIÓN Y PARÁMETROS
  // ============================================

  @Operation(
    summary = "Obtener configuración del sistema",
    description = "Recupera la configuración actual del sistema de carritos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Configuración recuperada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/configuracion")
  public ResponseEntity<Map<String, Object>> obtenerConfiguracion() {
    log.info("Obteniendo configuración del sistema");

    Map<String, Object> configuracion =
      carritoService.obtenerConfiguracionSistema();

    log.info("Configuración del sistema recuperada");
    return ResponseEntity.ok(configuracion);
  }

  @Operation(
    summary = "Actualizar configuración del sistema",
    description = "Modifica parámetros de configuración del sistema de carritos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Configuración actualizada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @PutMapping("/configuracion")
  public ResponseEntity<Map<String, Object>> actualizarConfiguracion(
    @Parameter(
      description = "Nueva configuración del sistema"
    ) @RequestBody Map<String, Object> nuevaConfiguracion
  ) {
    log.info(
      "Actualizando configuración del sistema - {} parámetros",
      nuevaConfiguracion.size()
    );

    Map<String, Object> configuracionActualizada =
      carritoService.actualizarConfiguracionSistema(nuevaConfiguracion);

    log.info("Configuración del sistema actualizada exitosamente");
    return ResponseEntity.ok(configuracionActualizada);
  }

  // ============================================
  // REPORTES Y EXPORTACIÓN
  // ============================================

  @Operation(
    summary = "Generar reporte personalizado",
    description = "Crea reportes personalizados con métricas específicas"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Reporte generado exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @PostMapping("/reportes")
  public ResponseEntity<Map<String, Object>> generarReportePersonalizado(
    @Parameter(description = "Configuración del reporte") @RequestBody Map<
      String,
      Object
    > configuracionReporte
  ) {
    log.info("Generando reporte personalizado");

    Map<String, Object> reporte = carritoService.generarReportePersonalizado(
      configuracionReporte
    );
    reporte.put("timestamp", LocalDateTime.now());

    log.info("Reporte personalizado generado exitosamente");
    return ResponseEntity.ok(reporte);
  }

  @Operation(
    summary = "Programar exportación masiva",
    description = "Programa una exportación masiva de datos para procesamiento en segundo plano"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "202",
        description = "Exportación programada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @PostMapping("/exportar-masivo")
  public ResponseEntity<Map<String, Object>> programarExportacionMasiva(
    @Parameter(
      description = "Configuración de la exportación"
    ) @RequestBody Map<String, Object> configuracionExportacion
  ) {
    log.info("Programando exportación masiva de datos");

    String jobId = carritoService.programarExportacionMasiva(
      configuracionExportacion
    );

    Map<String, Object> respuesta = Map.of(
      "jobId",
      jobId,
      "estado",
      "PROGRAMADO",
      "estimadoCompletado",
      LocalDateTime.now().plusMinutes(30),
      "mensaje",
      "La exportación ha sido programada y se procesará en segundo plano"
    );

    log.info("Exportación masiva programada - JobID: {}", jobId);
    return ResponseEntity.accepted().body(respuesta);
  }

  // ============================================
  // MONITOREO Y ALERTAS
  // ============================================

  @Operation(
    summary = "Obtener métricas de rendimiento",
    description = "Recupera métricas detalladas de rendimiento del sistema"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Métricas recuperadas exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/metricas-rendimiento")
  public ResponseEntity<Map<String, Object>> obtenerMetricasRendimiento() {
    log.info("Obteniendo métricas de rendimiento del sistema");

    Map<String, Object> metricas = carritoService.obtenerMetricasRendimiento();
    metricas.put("timestamp", LocalDateTime.now());

    log.info("Métricas de rendimiento recuperadas");
    return ResponseEntity.ok(metricas);
  }

  @Operation(
    summary = "Obtener alertas activas",
    description = "Lista todas las alertas administrativas activas en el sistema"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Alertas recuperadas exitosamente",
        content = @Content(schema = @Schema(implementation = List.class))
      ),
    }
  )
  @GetMapping("/alertas")
  public ResponseEntity<List<Map<String, Object>>> obtenerAlertasActivas() {
    log.info("Obteniendo alertas administrativas activas");

    List<Map<String, Object>> alertas =
      carritoService.obtenerAlertasAdministrativas();

    log.info("{} alertas administrativas encontradas", alertas.size());
    return ResponseEntity.ok(alertas);
  }

  @Operation(
    summary = "Estado de salud del sistema",
    description = "Verifica el estado de salud de todos los componentes del sistema de carritos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Estado de salud recuperado exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/health-check")
  public ResponseEntity<Map<String, Object>> verificarSaludSistema() {
    log.info("Verificando estado de salud del sistema");

    Map<String, Object> estadoSalud = carritoService.verificarSaludSistema();
    estadoSalud.put("timestamp", LocalDateTime.now());

    boolean sistemasSaludables = (Boolean) estadoSalud.getOrDefault(
      "saludable",
      false
    );

    log.info(
      "Verificación de salud completada - Estado: {}",
      sistemasSaludables ? "SALUDABLE" : "CON_PROBLEMAS"
    );

    return ResponseEntity.ok(estadoSalud);
  }
}
