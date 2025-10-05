package com.nelson.project.msvc_categoria.msvc_categoria.controller;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.*;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 🚀 CONTROLADOR EMPRESARIAL DE CATEGORÍAS
 *
 * Implementación de nivel empresarial para la gestión de categorías de ecommerce.
 * Sigue las mejores prácticas de arquitectura REST, incluyendo:
 * - Patrones HATEOAS para navegabilidad
 * - Respuestas estructuradas con metadatos
 * - Validaciones robustas y manejo de errores
 * - Cache estratégico para optimización
 * - Documentación OpenAPI completa
 * - Principios SOLID y Clean Architecture
 *
 * @author Nelson Laza
 * @version 1.0.0
 * @since Spring Boot 3.5.5
 */
@Slf4j
@Validated
@RestController
@RequestMapping(
  value = "/categorias",
  produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Tag(
  name = "Categorías",
  description = "API empresarial para gestión completa de categorías de ecommerce"
)
public class CategoriaController {

  private final CategoriaService categoriaService;

  private static final String API_VERSION = "1.0.0";
  private static final String SERVER_INFO = "msvc-categoria-v1.0.0";

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 📋 OPERACIONES CRUD EMPRESARIALES
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * 🔍 LISTAR CATEGORÍAS CON PAGINACIÓN AVANZADA
   *
   * Obtiene una lista paginada de categorías con filtros opcionales y ordenamiento.
   * Implementa cache inteligente y enlaces HATEOAS para navegación.
   */
  @Operation(
    summary = "Obtener categorías paginadas",
    description = "Retorna una lista paginada de categorías con filtros opcionales, ordenamiento y metadatos de navegación"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Lista de categorías obtenida exitosamente",
        content = @Content(
          schema = @Schema(implementation = PagedResponse.class)
        )
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Parámetros de consulta inválidos"
      ),
    }
  )
  @GetMapping("/list")
  @Cacheable(
    value = "categorias",
    key = "#page + '_' + #size + '_' + #sort + '_' + #direction + '_' + #activo"
  )
  public ResponseEntity<ApiResponse<PagedResponse<CategoriaDTO>>> getAllPaged(
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
      regexp = "nombre|codigo|fechaCreacion|popularidad"
    ) String sort,
    @Parameter(
      description = "Dirección de ordenamiento",
      example = "ASC"
    ) @RequestParam(defaultValue = "ASC") @Pattern(
      regexp = "ASC|DESC"
    ) String direction,
    @Parameter(
      description = "Filtrar solo categorías activas",
      example = "true"
    ) @RequestParam(defaultValue = "true") Boolean activo,
    @Parameter(
      description = "Filtrar por nombre (búsqueda parcial)",
      example = "electrónicos"
    ) @RequestParam(required = false) String nombre,
    @Parameter(
      description = "Filtrar por código específico",
      example = "ELEC001"
    ) @RequestParam(required = false) String codigo,
    @Parameter(
      description = "Filtrar por categoría padre",
      example = "1"
    ) @RequestParam(required = false) Long categoriaPadreId,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Iniciando consulta paginada de categorías - TraceId: {}",
      traceId
    );

    try {
      // Configurar ordenamiento
      Sort sortConfig = Sort.by(Sort.Direction.fromString(direction), sort);
      Pageable pageable = PageRequest.of(page, size, sortConfig);

      // Aplicar filtros usando el service
      Page<CategoriaDTO> categorias;

      if (nombre != null || codigo != null || categoriaPadreId != null) {
        // Usar método de filtrado avanzado
        CategoriaFilterDto filter = CategoriaFilterDto.builder()
          .texto(nombre)
          .codigo(codigo)
          .categoriaPadreId(categoriaPadreId)
          .activo(activo)
          .build();

        categorias = categoriaService.findByFilters(filter, pageable);
      } else {
        // Consulta simple con filtro de activo
        if (activo) {
          categorias = categoriaService.findAllActive(pageable);
        } else {
          categorias = categoriaService.findAll(pageable);
        }
      }

      // Construir respuesta paginada con enlaces HATEOAS
      String baseUrl = buildBaseUrl(request);
      PagedResponse<CategoriaDTO> pagedResponse = PagedResponse.from(
        categorias,
        baseUrl
      );

      // Añadir metadatos específicos
      Map<String, Object> metadata = new HashMap<>();
      metadata.put("totalActivas", categoriaService.countAllActive());
      metadata.put("totalInactivas", categoriaService.countAllInactive());
      metadata.put(
        "aplicandoFiltros",
        nombre != null || codigo != null || categoriaPadreId != null
      );
      pagedResponse.withMetadata(metadata);

      // Crear respuesta empresarial
      ApiResponse<PagedResponse<CategoriaDTO>> response = ApiResponse.success(
        pagedResponse,
        "Categorías obtenidas exitosamente",
        "CATEGORIAS_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      log.info(
        "Consulta paginada completada exitosamente - TraceId: {}, Total: {}",
        traceId,
        categorias.getTotalElements()
      );

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error(
        "Error en consulta paginada de categorías - TraceId: {}",
        traceId,
        e
      );

      ApiResponse<PagedResponse<CategoriaDTO>> errorResponse = ApiResponse.<
        PagedResponse<CategoriaDTO>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 🔍 OBTENER CATEGORÍA POR ID
   *
   * Retorna una categoría específica con todos sus detalles y enlaces relacionados.
   */
  @Operation(
    summary = "Obtener categoría por ID",
    description = "Retorna una categoría específica con detalles completos y enlaces HATEOAS"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Categoría encontrada exitosamente"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Categoría no encontrada"
      ),
    }
  )
  @GetMapping("/list/{id}")
  @Cacheable(value = "categoria", key = "#id")
  public ResponseEntity<ApiResponse<CategoriaDTO>> getById(
    @Parameter(
      description = "ID de la categoría",
      example = "1",
      required = true
    ) @PathVariable @Min(1) Long id,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info("Consultando categoría por ID: {} - TraceId: {}", id, traceId);

    try {
      return categoriaService
        .findById(id)
        .map(categoria -> {
          // Crear enlaces HATEOAS
          Map<String, String> links = buildEntityLinks(categoria, request);

          ApiResponse<CategoriaDTO> response = ApiResponse.success(
            categoria,
            "Categoría encontrada",
            "CATEGORIA_FOUND"
          )
            .withLinks(links)
            .withTrace(traceId)
            .withApiVersion(API_VERSION)
            .withServer(SERVER_INFO);

          log.info(
            "Categoría encontrada exitosamente - ID: {}, TraceId: {}",
            id,
            traceId
          );
          return ResponseEntity.ok(response);
        })
        .orElseGet(() -> {
          log.warn(
            "Categoría no encontrada - ID: {}, TraceId: {}",
            id,
            traceId
          );

          ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<
            CategoriaDTO
          >error("Categoría no encontrada", "CATEGORIA_NOT_FOUND").withTrace(
            traceId
          );

          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            errorResponse
          );
        });
    } catch (Exception e) {
      log.error(
        "Error al consultar categoría por ID: {} - TraceId: {}",
        id,
        traceId,
        e
      );

      ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<CategoriaDTO>error(
        "Error interno del servidor",
        "INTERNAL_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * ✨ CREAR NUEVA CATEGORÍA
   *
   * Crea una nueva categoría aplicando todas las validaciones de negocio.
   */
  @Operation(
    summary = "Crear nueva categoría",
    description = "Crea una nueva categoría aplicando validaciones de negocio y generando valores automáticos"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201",
        description = "Categoría creada exitosamente"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "Conflicto - categoría ya existe"
      ),
    }
  )
  @PostMapping("/create")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @CacheEvict(
    value = { "categorias", "categorias-activas", "categorias-jerarquia" },
    allEntries = true
  )
  public ResponseEntity<ApiResponse<CategoriaDTO>> create(
    @Parameter(
      description = "Datos de la nueva categoría",
      required = true
    ) @Valid @RequestBody CategoriaCreateDto categoriaCreateDto,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Iniciando creación de categoría - TraceId: {}, Datos: {}",
      traceId,
      categoriaCreateDto
    );

    try {
      CategoriaDTO created = categoriaService.save(categoriaCreateDto);

      // Crear enlaces HATEOAS
      Map<String, String> links = buildEntityLinks(created, request);

      // Construir URI de la nueva categoría
      URI location = URI.create(buildBaseUrl(request) + "/" + created.getId());

      ApiResponse<CategoriaDTO> response = ApiResponse.success(
        created,
        "Categoría creada exitosamente",
        "CATEGORIA_CREATED"
      )
        .withLinks(links)
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      log.info(
        "Categoría creada exitosamente - ID: {}, TraceId: {}",
        created.getId(),
        traceId
      );

      return ResponseEntity.created(location).body(response);
    } catch (IllegalArgumentException e) {
      log.warn(
        "Error de validación en creación de categoría - TraceId: {}, Error: {}",
        traceId,
        e.getMessage()
      );

      ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<CategoriaDTO>error(
        e.getMessage(),
        "VALIDATION_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.badRequest().body(errorResponse);
    } catch (Exception e) {
      log.error(
        "Error interno en creación de categoría - TraceId: {}",
        traceId,
        e
      );

      ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<CategoriaDTO>error(
        "Error interno del servidor",
        "INTERNAL_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 🔄 ACTUALIZAR CATEGORÍA EXISTENTE
   *
   * Actualiza una categoría existente manteniendo integridad de datos.
   */
  @Operation(
    summary = "Actualizar categoría",
    description = "Actualiza una categoría existente aplicando validaciones de negocio"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Categoría actualizada exitosamente"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Categoría no encontrada"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos"
      ),
    }
  )
  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Caching(
    evict = {
      @CacheEvict(value = "categoria", key = "#id"),
      @CacheEvict(
        value = { "categorias", "categorias-activas", "categorias-jerarquia" },
        allEntries = true
      ),
    }
  )
  public ResponseEntity<ApiResponse<CategoriaDTO>> update(
    @Parameter(
      description = "ID de la categoría a actualizar",
      example = "1",
      required = true
    ) @PathVariable @Min(1) Long id,
    @Parameter(
      description = "Nuevos datos de la categoría",
      required = true
    ) @Valid @RequestBody CategoriaUpdateDto categoriaUpdateDto,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Iniciando actualización de categoría - ID: {}, TraceId: {}",
      id,
      traceId
    );

    try {
      // Verificar existencia
      if (categoriaService.findById(id).isEmpty()) {
        log.warn(
          "Categoría no encontrada para actualización - ID: {}, TraceId: {}",
          id,
          traceId
        );

        ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<
          CategoriaDTO
        >error("Categoría no encontrada", "CATEGORIA_NOT_FOUND").withTrace(
          traceId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }

      CategoriaDTO updated = categoriaService.update(id, categoriaUpdateDto);

      // Crear enlaces HATEOAS
      Map<String, String> links = buildEntityLinks(updated, request);

      ApiResponse<CategoriaDTO> response = ApiResponse.success(
        updated,
        "Categoría actualizada exitosamente",
        "CATEGORIA_UPDATED"
      )
        .withLinks(links)
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      log.info(
        "Categoría actualizada exitosamente - ID: {}, TraceId: {}",
        id,
        traceId
      );

      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      log.warn(
        "Error de validación en actualización - ID: {}, TraceId: {}, Error: {}",
        id,
        traceId,
        e.getMessage()
      );

      ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<CategoriaDTO>error(
        e.getMessage(),
        "VALIDATION_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.badRequest().body(errorResponse);
    } catch (Exception e) {
      log.error(
        "Error interno en actualización - ID: {}, TraceId: {}",
        id,
        traceId,
        e
      );

      ApiResponse<CategoriaDTO> errorResponse = ApiResponse.<CategoriaDTO>error(
        "Error interno del servidor",
        "INTERNAL_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 🗑️ ELIMINAR CATEGORÍA (SOFT DELETE)
   *
   * Realiza eliminación lógica de una categoría aplicando validaciones de integridad.
   */
  @Operation(
    summary = "Eliminar categoría",
    description = "Realiza eliminación lógica de una categoría con validaciones de integridad"
  )
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "204",
        description = "Categoría eliminada exitosamente"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Categoría no encontrada"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "No se puede eliminar - tiene dependencias"
      ),
    }
  )
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Caching(
    evict = {
      @CacheEvict(value = "categoria", key = "#id"),
      @CacheEvict(
        value = { "categorias", "categorias-activas", "categorias-jerarquia" },
        allEntries = true
      ),
    }
  )
  public ResponseEntity<ApiResponse<Void>> delete(
    @Parameter(
      description = "ID de la categoría a eliminar",
      example = "1",
      required = true
    ) @PathVariable @Min(1) Long id,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Iniciando eliminación de categoría - ID: {}, TraceId: {}",
      id,
      traceId
    );

    try {
      // Verificar existencia
      if (categoriaService.findById(id).isEmpty()) {
        log.warn(
          "Categoría no encontrada para eliminación - ID: {}, TraceId: {}",
          id,
          traceId
        );

        ApiResponse<Void> errorResponse = ApiResponse.<Void>error(
          "Categoría no encontrada",
          "CATEGORIA_NOT_FOUND"
        ).withTrace(traceId);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }

      categoriaService.deleteById(id);

      ApiResponse<Void> response = ApiResponse.<Void>success(
        null,
        "Categoría eliminada exitosamente",
        "CATEGORIA_DELETED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      log.info(
        "Categoría eliminada exitosamente - ID: {}, TraceId: {}",
        id,
        traceId
      );

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    } catch (IllegalStateException e) {
      log.warn(
        "No se puede eliminar categoría - ID: {}, TraceId: {}, Razón: {}",
        id,
        traceId,
        e.getMessage()
      );

      ApiResponse<Void> errorResponse = ApiResponse.<Void>error(
        e.getMessage(),
        "CANNOT_DELETE"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    } catch (Exception e) {
      log.error(
        "Error interno en eliminación - ID: {}, TraceId: {}",
        id,
        traceId,
        e
      );

      ApiResponse<Void> errorResponse = ApiResponse.<Void>error(
        "Error interno del servidor",
        "INTERNAL_ERROR"
      ).withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 🌳 OPERACIONES DE JERARQUÍA Y NAVEGACIÓN
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * 🌳 OBTENER CATEGORÍAS RAÍZ
   *
   * Retorna todas las categorías de primer nivel (sin padre).
   */
  @Operation(
    summary = "Obtener categorías raíz",
    description = "Retorna categorías de primer nivel para construcción de menús jerárquicos"
  )
  @GetMapping("/raiz")
  @Cacheable("categorias-raiz")
  public ResponseEntity<ApiResponse<List<CategoriaDTO>>> getRootCategories(
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info("Consultando categorías raíz - TraceId: {}", traceId);

    try {
      List<CategoriaDTO> rootCategories = categoriaService.findRootCategories();

      ApiResponse<List<CategoriaDTO>> response = ApiResponse.success(
        rootCategories,
        "Categorías raíz obtenidas",
        "ROOT_CATEGORIES_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error al consultar categorías raíz - TraceId: {}", traceId, e);

      ApiResponse<List<CategoriaDTO>> errorResponse = ApiResponse.<
        List<CategoriaDTO>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 👶 OBTENER SUBCATEGORÍAS
   *
   * Retorna las subcategorías directas de una categoría padre.
   */
  @Operation(
    summary = "Obtener subcategorías",
    description = "Retorna subcategorías directas de una categoría padre específica"
  )
  @GetMapping("/{id}/subcategorias")
  @Cacheable(value = "subcategorias", key = "#id")
  public ResponseEntity<ApiResponse<List<CategoriaDTO>>> getSubcategories(
    @Parameter(
      description = "ID de la categoría padre",
      example = "1",
      required = true
    ) @PathVariable @Min(1) Long id,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Consultando subcategorías para categoría ID: {} - TraceId: {}",
      id,
      traceId
    );

    try {
      List<CategoriaDTO> subcategories =
        categoriaService.findSubcategoriesByParentId(id);

      ApiResponse<List<CategoriaDTO>> response = ApiResponse.success(
        subcategories,
        "Subcategorías obtenidas",
        "SUBCATEGORIES_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error(
        "Error al consultar subcategorías - ID: {}, TraceId: {}",
        id,
        traceId,
        e
      );

      ApiResponse<List<CategoriaDTO>> errorResponse = ApiResponse.<
        List<CategoriaDTO>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 🌳 OBTENER JERARQUÍA COMPLETA
   *
   * Retorna la jerarquía completa de categorías en estructura de árbol.
   */
  @Operation(
    summary = "Obtener jerarquía completa",
    description = "Retorna toda la estructura jerárquica de categorías en formato de árbol"
  )
  @GetMapping("/jerarquia")
  @Cacheable("categorias-jerarquia")
  public ResponseEntity<ApiResponse<List<CategoriaDTO>>> getFullHierarchy(
    @Parameter(
      description = "Solo categorías activas",
      example = "true"
    ) @RequestParam(defaultValue = "true") Boolean activo,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Consultando jerarquía completa - Activo: {}, TraceId: {}",
      activo,
      traceId
    );

    try {
      List<CategoriaDTO> hierarchy = activo
        ? categoriaService.findActiveCategoryHierarchy()
        : categoriaService.findFullCategoryHierarchy();

      ApiResponse<List<CategoriaDTO>> response = ApiResponse.success(
        hierarchy,
        "Jerarquía obtenida exitosamente",
        "HIERARCHY_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error al consultar jerarquía - TraceId: {}", traceId, e);

      ApiResponse<List<CategoriaDTO>> errorResponse = ApiResponse.<
        List<CategoriaDTO>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 🔍 OPERACIONES DE BÚSQUEDA Y FILTRADO
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * 🔎 BÚSQUEDA AVANZADA DE CATEGORÍAS
   *
   * Realiza búsqueda de texto completo en categorías con filtros adicionales.
   */
  @Operation(
    summary = "Búsqueda avanzada de categorías",
    description = "Búsqueda de texto completo con filtros adicionales y paginación"
  )
  @GetMapping("/buscar")
  public ResponseEntity<
    ApiResponse<PagedResponse<CategoriaDTO>>
  > searchCategories(
    @Parameter(
      description = "Término de búsqueda",
      example = "electrónicos",
      required = true
    ) @RequestParam String q,
    @Parameter(
      description = "Buscar en descripción también",
      example = "true"
    ) @RequestParam(defaultValue = "false") Boolean incluirDescripcion,
    @PageableDefault(size = 20, sort = "nombre") Pageable pageable,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info("Búsqueda de categorías - Término: '{}', TraceId: {}", q, traceId);

    try {
      Page<CategoriaDTO> results = categoriaService.searchByText(
        q,
        incluirDescripcion,
        pageable
      );

      String baseUrl = buildBaseUrl(request) + "/buscar";
      PagedResponse<CategoriaDTO> pagedResponse = PagedResponse.from(
        results,
        baseUrl
      );

      // Metadatos de búsqueda
      Map<String, Object> metadata = new HashMap<>();
      metadata.put("terminoBusqueda", q);
      metadata.put("incluirDescripcion", incluirDescripcion);
      metadata.put("tiempoBusqueda", System.currentTimeMillis());
      pagedResponse.withMetadata(metadata);

      ApiResponse<PagedResponse<CategoriaDTO>> response = ApiResponse.success(
        pagedResponse,
        "Búsqueda completada",
        "SEARCH_COMPLETED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error(
        "Error en búsqueda de categorías - Término: '{}', TraceId: {}",
        q,
        traceId,
        e
      );

      ApiResponse<PagedResponse<CategoriaDTO>> errorResponse = ApiResponse.<
        PagedResponse<CategoriaDTO>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  /**
   * 🏆 OBTENER CATEGORÍAS MÁS POPULARES
   *
   * Retorna las categorías ordenadas por popularidad.
   */
  @Operation(
    summary = "Obtener categorías populares",
    description = "Retorna categorías ordenadas por métricas de popularidad"
  )
  @GetMapping("/populares")
  @Cacheable("categorias-populares")
  public ResponseEntity<
    ApiResponse<List<CategoriaSummaryDto>>
  > getPopularCategories(
    @Parameter(
      description = "Límite de resultados",
      example = "10"
    ) @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer limit,
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info(
      "Consultando categorías populares - Límite: {}, TraceId: {}",
      limit,
      traceId
    );

    try {
      List<CategoriaSummaryDto> popularCategories =
        categoriaService.findMostPopular(limit);

      ApiResponse<List<CategoriaSummaryDto>> response = ApiResponse.success(
        popularCategories,
        "Categorías populares obtenidas",
        "POPULAR_CATEGORIES_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error(
        "Error al consultar categorías populares - TraceId: {}",
        traceId,
        e
      );

      ApiResponse<List<CategoriaSummaryDto>> errorResponse = ApiResponse.<
        List<CategoriaSummaryDto>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 📊 OPERACIONES DE ESTADÍSTICAS Y ANÁLISIS
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * 📈 OBTENER ESTADÍSTICAS DE CATEGORÍAS
   *
   * Retorna métricas y estadísticas del sistema de categorías.
   */
  @Operation(
    summary = "Obtener estadísticas de categorías",
    description = "Retorna métricas y estadísticas detalladas del sistema de categorías"
  )
  @GetMapping("/estadisticas")
  @Cacheable("categoria-estadisticas")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics(
    HttpServletRequest request
  ) {
    String traceId = generateTraceId();
    log.info("Consultando estadísticas de categorías - TraceId: {}", traceId);

    try {
      Map<String, Object> stats = new HashMap<>();
      stats.put("totalCategorias", categoriaService.countAll());
      stats.put("categoriasActivas", categoriaService.countAllActive());
      stats.put("categoriasInactivas", categoriaService.countAllInactive());
      stats.put("categoriasRaiz", categoriaService.countRootCategories());
      stats.put(
        "promedioSubcategorias",
        categoriaService.getAverageSubcategoriesPerCategory()
      );
      stats.put(
        "nivelMaximoJerarquia",
        categoriaService.getMaxHierarchyLevel()
      );
      stats.put(
        "categoriaSinProductos",
        categoriaService.countCategoriesWithoutProducts()
      );
      stats.put(
        "categoriaConMasProductos",
        categoriaService.getCategoryWithMostProducts()
      );

      ApiResponse<Map<String, Object>> response = ApiResponse.success(
        stats,
        "Estadísticas obtenidas exitosamente",
        "STATISTICS_RETRIEVED"
      )
        .withTrace(traceId)
        .withApiVersion(API_VERSION)
        .withServer(SERVER_INFO);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error al consultar estadísticas - TraceId: {}", traceId, e);

      ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<
        Map<String, Object>
      >error("Error interno del servidor", "INTERNAL_ERROR").withTrace(traceId);

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        errorResponse
      );
    }
  }

  // ═══════════════════════════════════════════════════════════════════════════════════════
  // 🛠️ MÉTODOS UTILITARIOS Y HELPERS
  // ═══════════════════════════════════════════════════════════════════════════════════════

  /**
   * Genera un ID único para seguimiento de requests
   */
  private String generateTraceId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Construye la URL base para enlaces HATEOAS
   */
  private String buildBaseUrl(HttpServletRequest request) {
    return (
      request.getRequestURL().toString().replace(request.getRequestURI(), "") +
      request.getContextPath() +
      "/api/v1/categorias"
    );
  }

  /**
   * Construye enlaces HATEOAS para una entidad específica
   */
  private Map<String, String> buildEntityLinks(
    CategoriaDTO categoria,
    HttpServletRequest request
  ) {
    String baseUrl = buildBaseUrl(request);
    Map<String, String> links = new HashMap<>();

    links.put("self", baseUrl + "/" + categoria.getId());
    links.put("update", baseUrl + "/" + categoria.getId());
    links.put("delete", baseUrl + "/" + categoria.getId());
    links.put(
      "subcategorias",
      baseUrl + "/" + categoria.getId() + "/subcategorias"
    );

    if (categoria.getCategoriaPadreId() != null) {
      links.put("padre", baseUrl + "/" + categoria.getCategoriaPadreId());
    }

    links.put("collection", baseUrl);
    links.put("jerarquia", baseUrl + "/jerarquia");

    return links;
  }
}
