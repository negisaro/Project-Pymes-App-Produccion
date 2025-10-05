package com.nelson.project.msvc_categoria.msvc_categoria.controller;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaSummaryDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.response.ApiResponse;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.response.PagedResponse;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 🌐 CONTROLADOR PÚBLICO DE CATEGORÍAS
 *
 * API pública optimizada para aplicaciones cliente y consumo externo.
 * Implementa cache agresivo, respuestas optimizadas y endpoints específicos
 * para casos de uso públicos como navegación, búsqueda y catálogos.
 *
 * Características:
 * - Solo categorías activas y disponibles
 * - Cache optimizado para alta concurrencia
 * - Respuestas ligeras para mejor performance
 * - Rate limiting preparado
 * - CDN-friendly headers
 *
 * @author Nelson Laza
 * @version 1.0.0
 * @since Spring Boot 3.5.5
 */
@Slf4j
@Validated
@RestController
@RequestMapping(
  value = "/public/categorias",
  produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Tag(
  name = "Categorías Públicas",
  description = "API pública para consulta de categorías optimizada para frontend y aplicaciones cliente"
)
public class CategoriaPublicController {

  private final CategoriaService categoriaService;

  private static final String API_VERSION = "1.0.0";
  private static final String SERVER_INFO = "msvc-categoria-public-v1.0.0";

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 📋 OPERACIONES PÚBLICAS OPTIMIZADAS
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * 🔍 LISTAR CATEGORÍAS ACTIVAS CON PAGINACIÓN
   *
   * Endpoint público optimizado para navegación de catálogo.
   * Solo retorna categorías activas con cache agresivo.
   */
  @Operation(
    summary = "Obtener categorías activas (público)",
    description = "Retorna lista paginada de categorías activas optimizada para aplicaciones cliente"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Categorías obtenidas exitosamente",
        content = @Content(
          schema = @Schema(implementation = PagedResponse.class)
        )
      ),
    }
  )
  @GetMapping
  @Cacheable(
    value = "public-categorias",
    key = "#page + '_' + #size + '_' + #sort + '_' + #direction"
  )
  public ResponseEntity<
    ApiResponse<PagedResponse<CategoriaSummaryDto>>
  > getAllActivePublic(
    @Parameter(
      description = "Número de página (base 0)",
      example = "0"
    ) @RequestParam(defaultValue = "0") @Min(0) Integer page,
    @Parameter(description = "Tamaño de página", example = "20") @RequestParam(
      defaultValue = "20"
    ) @Min(1) @Max(100) Integer size,
    @Parameter(
      description = "Campo de ordenamiento",
      example = "nombre"
    ) @RequestParam(defaultValue = "nombre") @Pattern(
      regexp = "nombre|orden|popularidad"
    ) String sort,
    @Parameter(
      description = "Dirección de ordenamiento",
      example = "ASC"
    ) @RequestParam(defaultValue = "ASC") @Pattern(
      regexp = "ASC|DESC"
    ) String direction,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug("Consulta pública de categorías - TraceId: {}", traceId);

    try {
      // Timeout para evitar spinners infinitos en el frontend
      long startTime = System.currentTimeMillis();

      // Configurar ordenamiento
      Sort sortConfig = Sort.by(Sort.Direction.fromString(direction), sort);
      Pageable pageable = PageRequest.of(page, size, sortConfig);

      // Solo categorías activas para API pública
      Page<CategoriaSummaryDto> categorias =
        categoriaService.findAllActiveSummary(pageable);

      long duration = System.currentTimeMillis() - startTime;

      // Verificar si la operación demoró demasiado
      if (duration > 15000) { // 15 segundos máximo
        log.warn(
          "⚠️  Consulta pública demoró {} ms - TraceId: {}",
          duration,
          traceId
        );

        // Retornar respuesta degradada pero exitosa
        PagedResponse<CategoriaSummaryDto> emptyResponse = PagedResponse.<
          CategoriaSummaryDto
        >builder()
          .content(List.of())
          .page(
            PagedResponse.PageMetadata.builder()
              .number(0)
              .size(size)
              .totalElements(0)
              .totalPages(0)
              .numberOfElements(0)
              .first(true)
              .last(true)
              .hasPrevious(false)
              .hasNext(false)
              .empty(true)
              .build()
          )
          .build();
        Map<String, Object> degradedMetadata = new HashMap<>();
        degradedMetadata.put("degraded", true);
        degradedMetadata.put("reason", "timeout");
        degradedMetadata.put("duration", duration);
        emptyResponse.withMetadata(degradedMetadata);

        ApiResponse<PagedResponse<CategoriaSummaryDto>> degradedApiResponse =
          ApiResponse.success(
            emptyResponse,
            "Servicio respondiendo lentamente, intenta nuevamente",
            "SERVICE_DEGRADED"
          ).withTrace(traceId);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(
          degradedApiResponse
        );
      }

      // Respuesta ligera optimizada para cliente
      String baseUrl = buildBaseUrl(request);
      PagedResponse<CategoriaSummaryDto> pagedResponse = PagedResponse.from(
        categorias,
        baseUrl
      );

      // Metadatos mínimos para API pública
      Map<String, Object> metadata = new HashMap<>();
      metadata.put("cached", true);
      metadata.put("ttl", 300); // 5 minutos

      // Mejorar UX para lista vacía
      String mensaje;
      String codigo;
      if (categorias.getTotalElements() == 0) {
        mensaje = page == 0
          ? "No hay categorías disponibles. ¡Crea la primera categoría!"
          : "No hay más categorías en esta página";
        codigo = page == 0 ? "NO_CATEGORIES_YET" : "NO_MORE_CATEGORIES";
        metadata.put("isEmpty", true);
        metadata.put("isFirstTime", page == 0);
      } else {
        mensaje = "Categorías activas obtenidas";
        codigo = "ACTIVE_CATEGORIES_RETRIEVED";
        metadata.put("isEmpty", false);
      }

      pagedResponse.withMetadata(metadata);

      ApiResponse<PagedResponse<CategoriaSummaryDto>> response =
        ApiResponse.success(pagedResponse, mensaje, codigo)
          .withTrace(traceId)
          .withApiVersion(API_VERSION)
          .withServer(SERVER_INFO);

      // Headers para cache público
      return ResponseEntity.ok()
        .header("Cache-Control", "public, max-age=300") // 5 minutos
        .header("ETag", String.valueOf(categorias.hashCode()))
        .body(response);
    } catch (Exception e) {
      log.error(
        "Error en consulta pública de categorías - TraceId: {}",
        traceId,
        e
      );

      // Diferencias entre tipos de error para mejor UX
      String mensaje;
      String codigo;
      HttpStatus status;

      if (
        e instanceof java.util.concurrent.TimeoutException ||
        e.getCause() instanceof java.util.concurrent.TimeoutException ||
        e.getMessage().contains("timeout")
      ) {
        mensaje =
          "El servicio está tardando más de lo normal. Intenta nuevamente en unos momentos.";
        codigo = "SERVICE_TIMEOUT";
        status = HttpStatus.REQUEST_TIMEOUT;
      } else if (e instanceof org.springframework.dao.DataAccessException) {
        mensaje = "Problema temporal con la base de datos. Intenta nuevamente.";
        codigo = "DATABASE_UNAVAILABLE";
        status = HttpStatus.SERVICE_UNAVAILABLE;
      } else {
        mensaje =
          "Servicio temporalmente no disponible. Si el problema persiste, contacta soporte.";
        codigo = "SERVICE_UNAVAILABLE";
        status = HttpStatus.SERVICE_UNAVAILABLE;
      }

      // Respuesta de fallback más amigable
      PagedResponse<CategoriaSummaryDto> emptyResponse = PagedResponse.<
        CategoriaSummaryDto
      >builder()
        .content(List.of())
        .page(
          PagedResponse.PageMetadata.builder()
            .number(0)
            .size(size)
            .totalElements(0)
            .totalPages(0)
            .numberOfElements(0)
            .first(true)
            .last(true)
            .hasPrevious(false)
            .hasNext(false)
            .empty(true)
            .build()
        )
        .build();
      Map<String, Object> errorMetadata = new HashMap<>();
      errorMetadata.put("error", true);
      errorMetadata.put("canRetry", true);
      errorMetadata.put("suggestedAction", "refresh");
      errorMetadata.put("timestamp", System.currentTimeMillis());
      emptyResponse.withMetadata(errorMetadata);

      ApiResponse<PagedResponse<CategoriaSummaryDto>> errorResponse =
        ApiResponse.success(
          // Usar success para evitar que el frontend trate como error crítico
          emptyResponse,
          mensaje,
          codigo
        ).withTrace(traceId);

      return ResponseEntity.status(status).body(errorResponse);
    }
  }

  /**
   * 📋 ALIAS PARA LISTADO PAGINADO
   *
   * Endpoint adicional para compatibilidad con clientes que usan /list
   */
  @Operation(
    summary = "Obtener categorías activas (alias /list)",
    description = "Alias del endpoint principal para obtener categorías activas paginadas"
  )
  @GetMapping("/list")
  @Cacheable(
    value = "public-categorias-list",
    key = "#page + '_' + #size + '_' + #sort + '_' + #direction"
  )
  public ResponseEntity<
    ApiResponse<PagedResponse<CategoriaSummaryDto>>
  > getAllActivePublicList(
    @Parameter(
      description = "Número de página (base 0)",
      example = "0"
    ) @RequestParam(defaultValue = "0") @Min(0) Integer page,
    @Parameter(description = "Tamaño de página", example = "20") @RequestParam(
      defaultValue = "20"
    ) @Min(1) @Max(100) Integer size,
    @Parameter(
      description = "Campo de ordenamiento",
      example = "nombre"
    ) @RequestParam(defaultValue = "nombre") @Pattern(
      regexp = "nombre|orden|popularidad"
    ) String sort,
    @Parameter(
      description = "Dirección de ordenamiento",
      example = "ASC"
    ) @RequestParam(defaultValue = "ASC") @Pattern(
      regexp = "ASC|DESC"
    ) String direction,
    HttpServletRequest request
  ) {
    // Redirigir al método principal
    return getAllActivePublic(page, size, sort, direction, request);
  }

  /**
   * 🌳 OBTENER MENÚ DE NAVEGACIÓN
   *
   * Retorna estructura jerárquica optimizada para menús de navegación.
   */
  @Operation(
    summary = "Obtener menú de navegación",
    description = "Retorna estructura jerárquica de categorías optimizada para menús de navegación"
  )
  @GetMapping("/menu")
  @Cacheable(value = "public-menu", key = "#maxNiveles")
  public ResponseEntity<
    ApiResponse<List<CategoriaSummaryDto>>
  > getNavigationMenu(
    @Parameter(
      description = "Niveles máximos de jerarquía",
      example = "3"
    ) @RequestParam(defaultValue = "3") @Min(1) @Max(5) Integer maxNiveles,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug(
      "Consulta de menú de navegación - Niveles: {}, TraceId: {}",
      maxNiveles,
      traceId
    );

    try {
      List<CategoriaSummaryDto> menuItems = categoriaService.findNavigationMenu(
        maxNiveles
      );

      ApiResponse<List<CategoriaSummaryDto>> response = ApiResponse.success(
        menuItems,
        "Menú de navegación obtenido",
        "NAVIGATION_MENU_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      // Cache agresivo para menús
      return ResponseEntity.ok()
        .header("Cache-Control", "public, max-age=600") // 10 minutos
        .header("ETag", String.valueOf(menuItems.hashCode()))
        .body(response);
    } catch (Exception e) {
      log.error("Error en consulta de menú - TraceId: {}", traceId, e);

      ApiResponse<List<CategoriaSummaryDto>> errorResponse = ApiResponse.<
        List<CategoriaSummaryDto>
      >error("Menú temporalmente no disponible", "MENU_UNAVAILABLE").withTrace(
        traceId
      );

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        errorResponse
      );
    }
  }

  /**
   * 🏆 OBTENER CATEGORÍAS DESTACADAS
   *
   * Retorna categorías más populares para destacar en homepage.
   */
  @Operation(
    summary = "Obtener categorías destacadas",
    description = "Retorna categorías más populares optimizadas para homepage y secciones destacadas"
  )
  @GetMapping("/destacadas")
  @Cacheable(value = "public-destacadas", key = "#limite")
  public ResponseEntity<
    ApiResponse<List<CategoriaSummaryDto>>
  > getFeaturedCategories(
    @Parameter(
      description = "Número de categorías destacadas",
      example = "8"
    ) @RequestParam(defaultValue = "8") @Min(1) @Max(20) Integer limite,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug(
      "Consulta de categorías destacadas - Límite: {}, TraceId: {}",
      limite,
      traceId
    );

    try {
      List<CategoriaSummaryDto> featured =
        categoriaService.findFeaturedCategoriesSummary(limite);

      // Metadatos para categorías destacadas
      Map<String, Object> metadata = new HashMap<>();
      metadata.put("algoritmo", "popularidad_ponderada");
      metadata.put("ultimaActualizacion", System.currentTimeMillis());

      ApiResponse<List<CategoriaSummaryDto>> response = ApiResponse.success(
        featured,
        "Categorías destacadas obtenidas",
        "FEATURED_CATEGORIES_RETRIEVED"
      )
        .withMetadata(metadata)
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      // Cache extendido para contenido destacado
      return ResponseEntity.ok()
        .header("Cache-Control", "public, max-age=900") // 15 minutos
        .header("ETag", String.valueOf(featured.hashCode()))
        .body(response);
    } catch (Exception e) {
      log.error("Error en consulta de destacadas - TraceId: {}", traceId, e);

      ApiResponse<List<CategoriaSummaryDto>> errorResponse = ApiResponse.<
        List<CategoriaSummaryDto>
      >error(
        "Destacadas temporalmente no disponibles",
        "FEATURED_UNAVAILABLE"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        errorResponse
      );
    }
  }

  /**
   * 🔎 BÚSQUEDA PÚBLICA DE CATEGORÍAS
   *
   * Endpoint optimizado para autocompletado y búsqueda rápida.
   */
  @Operation(
    summary = "Búsqueda pública de categorías",
    description = "Búsqueda optimizada para autocompletado y sugerencias en tiempo real"
  )
  @GetMapping("/buscar")
  @Cacheable(value = "public-search", key = "#q + '_' + #limite")
  public ResponseEntity<ApiResponse<List<CategoriaSummaryDto>>> searchPublic(
    @Parameter(
      description = "Término de búsqueda",
      example = "electrónicos",
      required = true
    ) @RequestParam String q,
    @Parameter(
      description = "Límite de resultados",
      example = "10"
    ) @RequestParam(defaultValue = "10") @Min(1) @Max(25) Integer limite,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug("Búsqueda pública - Término: '{}', TraceId: {}", q, traceId);

    try {
      // Validación básica del término
      if (q.trim().length() < 2) {
        ApiResponse<List<CategoriaSummaryDto>> errorResponse = ApiResponse.<
          List<CategoriaSummaryDto>
        >error(
          "Término de búsqueda muy corto",
          "SEARCH_TERM_TOO_SHORT"
        ).withTrace(traceId);

        return ResponseEntity.badRequest().body(errorResponse);
      }

      List<CategoriaSummaryDto> results =
        categoriaService.searchActiveCategoriesSummary(q.trim(), limite);

      // Metadatos de búsqueda
      Map<String, Object> metadata = new HashMap<>();
      metadata.put("terminoBusqueda", q.trim());
      metadata.put("resultadosEncontrados", results.size());
      metadata.put("busquedaOptimizada", true);

      ApiResponse<List<CategoriaSummaryDto>> response = ApiResponse.success(
        results,
        "Búsqueda completada",
        "PUBLIC_SEARCH_COMPLETED"
      )
        .withMetadata(metadata)
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      // Cache corto para búsquedas
      return ResponseEntity.ok()
        .header("Cache-Control", "public, max-age=120") // 2 minutos
        .body(response);
    } catch (Exception e) {
      log.error(
        "Error en búsqueda pública - Término: '{}', TraceId: {}",
        q,
        traceId,
        e
      );

      ApiResponse<List<CategoriaSummaryDto>> errorResponse = ApiResponse.<
        List<CategoriaSummaryDto>
      >error(
        "Búsqueda temporalmente no disponible",
        "SEARCH_UNAVAILABLE"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        errorResponse
      );
    }
  }

  /**
   * 📊 OBTENER DETALLES DE CATEGORÍA PÚBLICA
   *
   * Retorna detalles básicos de una categoría para páginas de producto.
   */
  @Operation(
    summary = "Obtener detalles de categoría",
    description = "Retorna información básica de una categoría para contexto de productos"
  )
  @GetMapping("/list/{id}")
  @Cacheable(value = "public-categoria", key = "#id")
  public ResponseEntity<ApiResponse<CategoriaSummaryDto>> getByIdPublic(
    @Parameter(
      description = "ID de la categoría",
      example = "1",
      required = true
    ) @PathVariable @Min(1) Long id,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug(
      "Consulta pública de categoría - ID: {}, TraceId: {}",
      id,
      traceId
    );

    try {
      return categoriaService
        .findByIdActiveSummary(id)
        .map(categoria -> {
          // Enlaces básicos para navegación
          Map<String, String> links = new HashMap<>();
          String baseUrl = buildBaseUrl(request);

          links.put("self", baseUrl + "/" + id);
          links.put("productos", "/api/public/v1/productos?categoriaId=" + id);
          links.put("menu", baseUrl + "/menu");

          if (categoria.getCategoriaPadreId() != null) {
            links.put("padre", baseUrl + "/" + categoria.getCategoriaPadreId());
          }

          ApiResponse<CategoriaSummaryDto> response = ApiResponse.success(
            categoria,
            "Categoría encontrada",
            "PUBLIC_CATEGORIA_FOUND"
          )
            .withLinks(links)
            .withTrace(traceId)
            .withApiVersion(API_VERSION)
            .withServer(SERVER_INFO);

          return ResponseEntity.ok()
            .header("Cache-Control", "public, max-age=300") // 5 minutos
            .header("ETag", String.valueOf(categoria.hashCode()))
            .body(response);
        })
        .orElseGet(() -> {
          log.debug(
            "Categoría pública no encontrada - ID: {}, TraceId: {}",
            id,
            traceId
          );

          ApiResponse<CategoriaSummaryDto> errorResponse = ApiResponse.<
            CategoriaSummaryDto
          >error(
            "Categoría no disponible",
            "CATEGORIA_NOT_AVAILABLE"
          ).withTrace(traceId);

          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            errorResponse
          );
        });
    } catch (Exception e) {
      log.error(
        "Error en consulta pública de categoría - ID: {}, TraceId: {}",
        id,
        traceId,
        e
      );

      ApiResponse<CategoriaSummaryDto> errorResponse = ApiResponse.<
        CategoriaSummaryDto
      >error(
        "Categoría temporalmente no disponible",
        "CATEGORIA_UNAVAILABLE"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        errorResponse
      );
    }
  }

  /**
   * � HEALTH CHECK RÁPIDO
   *
   * Endpoint para verificar que el servicio esté disponible sin cargar datos pesados.
   */
  @Operation(
    summary = "Verificar estado del servicio",
    description = "Endpoint rápido para verificar disponibilidad del servicio de categorías"
  )
  @GetMapping("/health")
  public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
    String traceId = generateTraceId();

    try {
      long startTime = System.currentTimeMillis();

      // Verificación simple y rápida
      long totalCategorias = categoriaService.countAllActive();

      long duration = System.currentTimeMillis() - startTime;

      Map<String, Object> health = new HashMap<>();
      health.put("status", "UP");
      health.put("totalCategorias", totalCategorias);
      health.put("responseTime", duration + "ms");
      health.put("timestamp", System.currentTimeMillis());
      health.put("hasCategories", totalCategorias > 0);

      String mensaje = totalCategorias > 0
        ? "Servicio disponible con " + totalCategorias + " categorías"
        : "Servicio disponible - sin categorías creadas";

      ApiResponse<Map<String, Object>> response = ApiResponse.success(
        health,
        mensaje,
        "SERVICE_HEALTHY"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok()
        .header("Cache-Control", "no-cache") // No cachear health checks
        .body(response);
    } catch (Exception e) {
      log.error("Health check falló - TraceId: {}", traceId, e);

      Map<String, Object> health = new HashMap<>();
      health.put("status", "DOWN");
      health.put("error", e.getMessage());
      health.put("timestamp", System.currentTimeMillis());

      ApiResponse<Map<String, Object>> response = ApiResponse.success(
        health,
        "Servicio temporalmente no disponible",
        "SERVICE_DOWN"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        response
      );
    }
  }

  /**
   * �📈 ESTADÍSTICAS PÚBLICAS BÁSICAS
   *
   * Retorna métricas básicas para dashboards públicos.
   */
  @Operation(
    summary = "Obtener estadísticas públicas",
    description = "Retorna métricas básicas del catálogo para información pública"
  )
  @GetMapping("/estadisticas")
  @Cacheable(value = "public-stats", key = "'basic'")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicStatistics(
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.debug("Consulta de estadísticas públicas - TraceId: {}", traceId);

    try {
      Map<String, Object> stats = new HashMap<>();
      stats.put("totalCategorias", categoriaService.countAllActive());
      stats.put("categoriasRaiz", categoriaService.countActiveRootCategories());
      stats.put("ultimaActualizacion", System.currentTimeMillis());

      ApiResponse<Map<String, Object>> response = ApiResponse.success(
        stats,
        "Estadísticas públicas obtenidas",
        "PUBLIC_STATS_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      // Cache extendido para estadísticas
      return ResponseEntity.ok()
        .header("Cache-Control", "public, max-age=1800") // 30 minutos
        .body(response);
    } catch (Exception e) {
      log.error("Error en estadísticas públicas - TraceId: {}", traceId, e);

      ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<
        Map<String, Object>
      >error(
        "Estadísticas temporalmente no disponibles",
        "STATS_UNAVAILABLE"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
        errorResponse
      );
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 🛠️ MÉTODOS UTILITARIOS
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * Genera un ID único para seguimiento de requests públicos
   */
  private String generateTraceId() {
    return "pub-" + UUID.randomUUID().toString().substring(0, 8);
  }

  /**
   * Construye la URL base para enlaces HATEOAS públicos
   */
  private String buildBaseUrl(HttpServletRequest request) {
    return (
      request.getRequestURL().toString().replace(request.getRequestURI(), "") +
      request.getContextPath() +
      "/api/public/v1/categorias"
    );
  }
}
