package com.nelson.project.msvc_carrito.msvc_carrito.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para métricas de observabilidad y monitoreo del microservicio.
 *
 * Proporciona endpoints especializados para:
 * - Health checks personalizados
 * - Métricas de negocio con Micrometer
 * - Información de estado del sistema
 * - Indicadores de rendimiento
 * - Conectividad con servicios externos
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/v1/metrics")
@Tag(
  name = "Métricas y Monitoreo",
  description = "API para observabilidad y monitoreo del sistema"
)
@RequiredArgsConstructor
public class MetricsController {

  private static final Logger log = LoggerFactory.getLogger(
    MetricsController.class
  );

  // TODO: Implementar métricas personalizadas
  // private final MeterRegistry meterRegistry;
  private final DataSource dataSource;

  // Métricas personalizadas
  private final Counter requestCounter;
  private final Timer responseTimer;

  // Constructor por defecto requerido por Spring (evita error de BeanInstantiationException)
  public MetricsController() {
    this.dataSource = null;
    this.requestCounter = null;
    this.responseTimer = null;
  }

  public MetricsController(MeterRegistry meterRegistry, DataSource dataSource) {
    // TODO: Activar cuando se implementen métricas personalizadas
    // this.meterRegistry = meterRegistry;
    this.dataSource = dataSource;

    // TODO: Inicializar métricas personalizadas cuando sea necesario
    this.requestCounter = Counter.builder("carrito.requests.total")
      .description("Total number of requests to carrito service")
      .register(meterRegistry);

    this.responseTimer = Timer.builder("carrito.response.time")
      .description("Response time for carrito operations")
      .register(meterRegistry);
  }

  @Operation(
    summary = "Health check del servicio",
    description = "Verifica el estado de salud del microservicio y sus dependencias"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Servicio saludable",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
        responseCode = "503",
        description = "Servicio con problemas",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/health")
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> healthStatus = verificarSaludSistema();

    Map<String, Object> response = new HashMap<>();
    response.put("status", healthStatus.get("status"));
    response.put("timestamp", LocalDateTime.now());
    response.put("service", "msvc-carrito");
    response.put("version", "2.0.0");
    response.put("details", healthStatus);

    String status = (String) healthStatus.get("status");
    if ("UP".equals(status)) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(503).body(response);
    }
  }

  @Operation(
    summary = "Métricas de negocio",
    description = "Recupera métricas específicas del dominio de carritos"
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
  @GetMapping("/business")
  public ResponseEntity<Map<String, Object>> getBusinessMetrics() {
    Map<String, Object> metrics = new HashMap<>();
    metrics.put("timestamp", LocalDateTime.now());
    metrics.put("requests_total", requestCounter.count());
    metrics.put("response_time_avg", responseTimer.mean(TimeUnit.MILLISECONDS));
    metrics.put("response_time_max", responseTimer.max(TimeUnit.MILLISECONDS));
    metrics.put("jvm_memory_used", getUsedMemory());
    metrics.put("jvm_memory_max", getMaxMemory());
    metrics.put("database_connections", getActiveDatabaseConnections());
    metrics.put("active_carritos", getActiveCarritosCount());

    log.debug("Business metrics retrieved: {}", metrics);
    return ResponseEntity.ok(metrics);
  }

  @Operation(
    summary = "Información del sistema",
    description = "Proporciona información detallada sobre el entorno de ejecución"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Información del sistema recuperada exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/info")
  public ResponseEntity<Map<String, Object>> getSystemInfo() {
    Runtime runtime = Runtime.getRuntime();

    Map<String, Object> info = new HashMap<>();
    info.put("timestamp", LocalDateTime.now());
    info.put("service_name", "msvc-carrito");
    info.put("version", "2.0.0");
    info.put("java_version", System.getProperty("java.version"));
    info.put("java_vendor", System.getProperty("java.vendor"));
    info.put("os_name", System.getProperty("os.name"));
    info.put("os_version", System.getProperty("os.version"));
    info.put("processors", runtime.availableProcessors());
    info.put("memory_total", runtime.totalMemory());
    info.put("memory_free", runtime.freeMemory());
    info.put("memory_max", runtime.maxMemory());
    info.put("uptime", getUptime());

    log.debug("System info retrieved: {}", info);
    return ResponseEntity.ok(info);
  }

  @Operation(
    summary = "Estado de readiness",
    description = "Verifica si el servicio está listo para recibir tráfico"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Servicio listo",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
        responseCode = "503",
        description = "Servicio no está listo",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/readiness")
  public ResponseEntity<Map<String, Object>> readinessCheck() {
    boolean ready =
      isDatabaseReady() && isCacheReady() && areExternalServicesReady();

    Map<String, Object> response = Map.of(
      "status",
      ready ? "READY" : "NOT_READY",
      "timestamp",
      LocalDateTime.now(),
      "checks",
      Map.of(
        "database",
        isDatabaseReady(),
        "cache",
        isCacheReady(),
        "external_services",
        areExternalServicesReady()
      )
    );

    if (ready) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(503).body(response);
    }
  }

  @Operation(
    summary = "Estado de liveness",
    description = "Verifica si el servicio está vivo y funcionando"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Servicio vivo",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/liveness")
  public ResponseEntity<Map<String, Object>> livenessCheck() {
    Map<String, Object> response = Map.of(
      "status",
      "ALIVE",
      "timestamp",
      LocalDateTime.now(),
      "service",
      "msvc-carrito",
      "memory_usage",
      getMemoryUsagePercentage()
    );

    return ResponseEntity.ok(response);
  }

  // ============================================
  // MÉTODOS DE VERIFICACIÓN DE SALUD
  // ============================================

  /**
   * Verifica la salud completa del sistema.
   */
  private Map<String, Object> verificarSaludSistema() {
    Map<String, Object> healthInfo = new HashMap<>();

    try {
      boolean databaseUp = isDatabaseReady();
      boolean cacheUp = isCacheReady();
      boolean externalServicesUp = areExternalServicesReady();

      String status = (databaseUp && cacheUp && externalServicesUp)
        ? "UP"
        : "DOWN";

      healthInfo.put("status", status);
      healthInfo.put("database", databaseUp ? "UP" : "DOWN");
      healthInfo.put("cache", cacheUp ? "UP" : "DOWN");
      healthInfo.put("external_services", externalServicesUp ? "UP" : "DOWN");
      healthInfo.put("memory_usage", getMemoryUsagePercentage() + "%");

      return healthInfo;
    } catch (Exception e) {
      log.error("Error verificando salud del sistema", e);
      healthInfo.put("status", "DOWN");
      healthInfo.put("error", e.getMessage());
      return healthInfo;
    }
  }

  // ============================================
  // MÉTODOS AUXILIARES
  // ============================================

  private boolean isDatabaseReady() {
    try (Connection connection = dataSource.getConnection()) {
      return connection.isValid(2); // 2 seconds timeout
    } catch (Exception e) {
      log.warn("Database health check failed", e);
      return false;
    }
  }

  private boolean isCacheReady() {
    try {
      // Aquí podrías verificar la conectividad con Redis
      // Por ahora retornamos true como placeholder
      return true;
    } catch (Exception e) {
      log.warn("Cache health check failed", e);
      return false;
    }
  }

  private boolean areExternalServicesReady() {
    try {
      // Aquí podrías verificar la conectividad con servicios externos
      // como el servicio de productos, usuarios, etc.
      return true;
    } catch (Exception e) {
      log.warn("External services health check failed", e);
      return false;
    }
  }

  private long getUsedMemory() {
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }

  private long getMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  private double getMemoryUsagePercentage() {
    Runtime runtime = Runtime.getRuntime();
    long used = runtime.totalMemory() - runtime.freeMemory();
    long max = runtime.maxMemory();
    return Math.round(((double) used / max) * 100.0 * 100.0) / 100.0;
  }

  private int getDatabaseConnectionCount() {
    // Placeholder - en una implementación real consultarías el pool de conexiones
    return 10;
  }

  private double getCacheHitRate() {
    // Placeholder - en una implementación real consultarías las métricas de cache
    return 95.5;
  }

  private long getUptime() {
    // Placeholder - en una implementación real calcularías el uptime real
    return System.currentTimeMillis();
  }

  private int getActiveDatabaseConnections() {
    // Placeholder - implementar lógica real de conexiones activas
    return 5;
  }

  private long getActiveCarritosCount() {
    // Placeholder - implementar lógica real con el servicio
    return 150L;
  }
}
