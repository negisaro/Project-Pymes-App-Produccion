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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST empresarial para la gestión del carrito de compras.
 *
 * Implementa un API REST completo siguiendo las mejores prácticas de la industria:
 * - Documentación OpenAPI 3.0 exhaustiva
 * - Validaciones robustas con Jakarta Bean Validation
 * - Manejo de excepciones estructurado
 * - Soporte para paginación y filtros avanzados
 * - Seguridad integrada con Spring Security
 * - Logging y monitoreo comprehensivo
 * - Respuestas HTTP semánticamente correctas
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/v1/carrito")
@Tag(
  name = "Carrito de Compras",
  description = "API para gestión empresarial del carrito de compras"
)
@RequiredArgsConstructor
@Validated
@Slf4j
public class CarritoController {

  private final CarritoService carritoService;

  // ============================================
  // OPERACIONES BÁSICAS DEL CARRITO
  // ============================================

  @Operation(
    summary = "Obtener carrito de usuario",
    description = "Recupera el carrito activo de un usuario específico con todos sus items y cálculos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Carrito recuperado exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @GetMapping("/{usuarioId}")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> obtenerCarrito(
    @Parameter(
      description = "ID único del usuario",
      required = true,
      example = "1"
    ) @PathVariable @NotNull @Min(1) Long usuarioId
  ) {
    log.info("Obteniendo carrito para usuario: {}", usuarioId);
    CarritoDto carrito = carritoService.obtenerCarritoPorUsuario(usuarioId);
    log.info(
      "Carrito obtenido exitosamente para usuario: {} - Items: {}",
      usuarioId,
      carrito.getItems().size()
    );

    return ResponseEntity.ok(carrito);
  }

  @Operation(
    summary = "Crear nuevo carrito",
    description = "Crea un nuevo carrito para un usuario si no existe uno activo"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "201",
        description = "Carrito creado exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "409",
        description = "El usuario ya tiene un carrito activo",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PostMapping("/crear/{usuarioId}")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> crearCarrito(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId
  ) {
    log.info("Creando nuevo carrito para usuario: {}", usuarioId);
    CarritoDto nuevoCarrito = carritoService.crearCarrito(usuarioId);
    log.info("Carrito creado exitosamente para usuario: {}", usuarioId);

    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCarrito);
  }

  // ============================================
  // GESTIÓN DE ITEMS DEL CARRITO
  // ============================================

  @Operation(
    summary = "Agregar item al carrito",
    description = "Agrega un producto al carrito con validaciones de stock y límites"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Item agregado exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Stock insuficiente o límite excedido",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PostMapping("/{usuarioId}/items")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> agregarItem(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Datos del item a agregar",
      required = true
    ) @Valid @RequestBody ItemCarritoRequestDto itemRequest
  ) {
    log.info(
      "Agregando item al carrito - Usuario: {}, Producto: {}, Cantidad: {}",
      usuarioId,
      itemRequest.getProductoId(),
      itemRequest.getCantidad()
    );

    CarritoDto carritoActualizado = carritoService.agregarItem(
      usuarioId,
      itemRequest
    );

    log.info(
      "Item agregado exitosamente al carrito del usuario: {}",
      usuarioId
    );
    return ResponseEntity.ok(carritoActualizado);
  }

  @Operation(
    summary = "Actualizar cantidad de item",
    description = "Modifica la cantidad de un item específico en el carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Cantidad actualizada exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Item no encontrado en el carrito",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PutMapping("/{usuarioId}/items/{itemId}")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> actualizarCantidadItem(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "ID único del item",
      required = true
    ) @PathVariable @NotNull @Min(1) Long itemId,
    @Parameter(
      description = "Nueva cantidad",
      required = true,
      example = "3"
    ) @RequestParam @NotNull @Min(1) Integer nuevaCantidad
  ) {
    log.info(
      "Actualizando cantidad de item - Usuario: {}, Item: {}, Nueva cantidad: {}",
      usuarioId,
      itemId,
      nuevaCantidad
    );

    CarritoDto carritoActualizado = carritoService.actualizarCantidadItem(
      usuarioId,
      itemId,
      nuevaCantidad
    );

    log.info("Cantidad de item actualizada exitosamente");
    return ResponseEntity.ok(carritoActualizado);
  }

  @Operation(
    summary = "Eliminar item del carrito",
    description = "Remueve completamente un item específico del carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Item eliminado exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Item no encontrado en el carrito",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @DeleteMapping("/{usuarioId}/items/{itemId}")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> eliminarItem(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "ID único del item a eliminar",
      required = true
    ) @PathVariable @NotNull @Min(1) Long itemId
  ) {
    log.info(
      "Eliminando item del carrito - Usuario: {}, Item: {}",
      usuarioId,
      itemId
    );

    CarritoDto carritoActualizado = carritoService.eliminarItem(
      usuarioId,
      itemId
    );

    log.info("Item eliminado exitosamente del carrito");
    return ResponseEntity.ok(carritoActualizado);
  }

  @Operation(
    summary = "Vaciar carrito completo",
    description = "Remueve todos los items del carrito de un usuario"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "204",
        description = "Carrito vaciado exitosamente"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Carrito no encontrado",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @DeleteMapping("/{usuarioId}/vaciar")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<Void> vaciarCarrito(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId
  ) {
    log.info("Vaciando carrito completo para usuario: {}", usuarioId);

    carritoService.vaciarCarrito(usuarioId);

    log.info("Carrito vaciado exitosamente para usuario: {}", usuarioId);
    return ResponseEntity.noContent().build();
  }

  // ============================================
  // OPERACIONES MASIVAS Y AVANZADAS
  // ============================================

  @Operation(
    summary = "Agregar múltiples items",
    description = "Agrega varios items al carrito en una sola operación transaccional"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Items agregados exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PostMapping("/{usuarioId}/items/bulk")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> agregarItemsMasivo(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Lista de items a agregar",
      required = true
    ) @Valid @RequestBody List<ItemCarritoRequestDto> items
  ) {
    log.info(
      "Agregando {} items masivamente al carrito del usuario: {}",
      items.size(),
      usuarioId
    );

    CarritoDto carritoActualizado = carritoService.agregarItemsMasivo(
      usuarioId,
      items
    );

    log.info("Items agregados masivamente exitosamente");
    return ResponseEntity.ok(carritoActualizado);
  }

  @Operation(
    summary = "Transferir carrito",
    description = "Transfiere el contenido de un carrito temporal a un carrito de usuario registrado"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Carrito transferido exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Carrito origen o destino no encontrado",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PostMapping("/transferir")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<CarritoDto> transferirCarrito(
    @Parameter(
      description = "ID del carrito origen",
      required = true
    ) @RequestParam @NotNull @Min(1) Long carritoOrigenId,
    @Parameter(
      description = "ID del usuario destino",
      required = true
    ) @RequestParam @NotNull @Min(1) Long usuarioDestinoId
  ) {
    log.info(
      "Transfiriendo carrito {} al usuario: {}",
      carritoOrigenId,
      usuarioDestinoId
    );

    CarritoDto carritoFusionado = carritoService.transferirCarrito(
      carritoOrigenId,
      usuarioDestinoId
    );

    log.info("Carrito transferido exitosamente");
    return ResponseEntity.ok(carritoFusionado);
  }

  // ============================================
  // VALIDACIONES Y CÁLCULOS
  // ============================================

  @Operation(
    summary = "Validar carrito",
    description = "Ejecuta validaciones completas del carrito: stock, precios, descuentos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Validación completada",
        content = @Content(
          schema = @Schema(implementation = ValidacionCarritoDto.class)
        )
      ),
    }
  )
  @PostMapping("/{usuarioId}/validar")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<ValidacionCarritoDto> validarCarrito(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId
  ) {
    log.info("Validando carrito para usuario: {}", usuarioId);

    ValidacionCarritoDto validacion = carritoService.validarCarrito(usuarioId);

    log.info(
      "Validación de carrito completada - Estado: {}",
      validacion.isCarritoValido()
    );
    return ResponseEntity.ok(validacion);
  }

  @Operation(
    summary = "Calcular totales",
    description = "Recalcula todos los totales del carrito incluyendo impuestos y descuentos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Totales calculados exitosamente",
        content = @Content(
          schema = @Schema(implementation = CarritoResumenDto.class)
        )
      ),
    }
  )
  @PostMapping("/{usuarioId}/calcular-totales")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoResumenDto> calcularTotales(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId
  ) {
    log.info("Calculando totales para carrito del usuario: {}", usuarioId);

    CarritoResumenDto resumen = carritoService.calcularTotales(usuarioId);

    log.info(
      "Totales calculados exitosamente - Total: {}",
      resumen.getValorTotal()
    );
    return ResponseEntity.ok(resumen);
  }

  // ============================================
  // DESCUENTOS Y PROMOCIONES
  // ============================================

  @Operation(
    summary = "Aplicar código de descuento",
    description = "Aplica un código promocional o cupón de descuento al carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Descuento aplicado exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Código de descuento inválido o expirado",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @PostMapping("/{usuarioId}/descuentos")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> aplicarDescuento(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Código de descuento a aplicar",
      required = true,
      example = "SAVE20"
    ) @RequestParam String codigoDescuento
  ) {
    log.info(
      "Aplicando descuento {} al carrito del usuario: {}",
      codigoDescuento,
      usuarioId
    );

    CarritoDto carritoConDescuento = carritoService.aplicarDescuento(
      usuarioId,
      codigoDescuento
    );

    log.info("Descuento aplicado exitosamente");
    return ResponseEntity.ok(carritoConDescuento);
  }

  @Operation(
    summary = "Remover descuento",
    description = "Remueve un descuento específico aplicado al carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Descuento removido exitosamente",
        content = @Content(schema = @Schema(implementation = CarritoDto.class))
      ),
    }
  )
  @DeleteMapping("/{usuarioId}/descuentos/{descuentoId}")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<CarritoDto> removerDescuento(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "ID del descuento a remover",
      required = true
    ) @PathVariable @NotNull @Min(1) Long descuentoId
  ) {
    log.info(
      "Removiendo descuento {} del carrito del usuario: {}",
      descuentoId,
      usuarioId
    );

    CarritoDto carritoSinDescuento = carritoService.removerDescuento(usuarioId);

    log.info("Descuento removido exitosamente");
    return ResponseEntity.ok(carritoSinDescuento);
  }

  // ============================================
  // ANÁLISIS Y REPORTES
  // ============================================

  @Operation(
    summary = "Obtener historial de carritos",
    description = "Recupera el historial paginado de carritos de un usuario"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Historial recuperado exitosamente",
        content = @Content(schema = @Schema(implementation = Page.class))
      ),
    }
  )
  @GetMapping("/{usuarioId}/historial")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<Page<CarritoResumenDto>> obtenerHistorial(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Número de página (0-based)",
      example = "0"
    ) @RequestParam(defaultValue = "0") int page,
    @Parameter(description = "Tamaño de página", example = "10") @RequestParam(
      defaultValue = "10"
    ) int size,
    @Parameter(
      description = "Campo de ordenamiento",
      example = "fechaCreacion"
    ) @RequestParam(defaultValue = "fechaCreacion") String sortBy,
    @Parameter(
      description = "Dirección de ordenamiento",
      example = "desc"
    ) @RequestParam(defaultValue = "desc") String sortDir
  ) {
    log.info(
      "Obteniendo historial de carritos para usuario: {} - Página: {}, Tamaño: {}",
      usuarioId,
      page,
      size
    );

    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<CarritoResumenDto> historial = carritoService.obtenerHistorialCarritos(
      usuarioId,
      pageable
    );

    log.info(
      "Historial recuperado exitosamente - {} carritos encontrados",
      historial.getTotalElements()
    );
    return ResponseEntity.ok(historial);
  }

  @Operation(
    summary = "Obtener métricas del carrito",
    description = "Recupera métricas y estadísticas del comportamiento del carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Métricas recuperadas exitosamente",
        content = @Content(
          schema = @Schema(implementation = MetricasCarritoDto.class)
        )
      ),
    }
  )
  @GetMapping("/{usuarioId}/metricas")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<MetricasCarritoDto> obtenerMetricas(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Fecha de inicio para el cálculo de métricas"
    ) @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaInicio,
    @Parameter(
      description = "Fecha de fin para el cálculo de métricas"
    ) @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaFin
  ) {
    log.info(
      "Obteniendo métricas de carrito para usuario: {} - Período: {} a {}",
      usuarioId,
      fechaInicio,
      fechaFin
    );

    MetricasCarritoDto metricas = carritoService.obtenerMetricasCarrito(
      usuarioId,
      fechaInicio,
      fechaFin
    );

    log.info("Métricas recuperadas exitosamente");
    return ResponseEntity.ok(metricas);
  }

  @Operation(
    summary = "Obtener productos recomendados",
    description = "Recupera productos recomendados basados en el contenido actual del carrito"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Recomendaciones recuperadas exitosamente",
        content = @Content(
          schema = @Schema(implementation = ProductoRecomendadoDto.class)
        )
      ),
    }
  )
  @GetMapping("/{usuarioId}/recomendaciones")
  @PreAuthorize(
    "hasRole('USER') and (#usuarioId == authentication.principal.id or hasRole('ADMIN'))"
  )
  public ResponseEntity<List<ProductoRecomendadoDto>> obtenerRecomendaciones(
    @Parameter(
      description = "ID único del usuario",
      required = true
    ) @PathVariable @NotNull @Min(1) Long usuarioId,
    @Parameter(
      description = "Número máximo de recomendaciones",
      example = "5"
    ) @RequestParam(defaultValue = "5") int limite
  ) {
    log.info(
      "Obteniendo recomendaciones para usuario: {} - Límite: {}",
      usuarioId,
      limite
    );

    List<ProductoRecomendadoDto> recomendaciones =
      carritoService.obtenerRecomendaciones(usuarioId, limite);

    log.info(
      "{} recomendaciones recuperadas exitosamente",
      recomendaciones.size()
    );
    return ResponseEntity.ok(recomendaciones);
  }

  // ============================================
  // ENDPOINTS ADMINISTRATIVOS
  // ============================================

  @Operation(
    summary = "Obtener estadísticas globales",
    description = "Recupera estadísticas generales de carritos para administradores"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Estadísticas recuperadas exitosamente",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Acceso denegado - Requiere rol de administrador",
        content = @Content(
          schema = @Schema(implementation = ErrorResponseDto.class)
        )
      ),
    }
  )
  @GetMapping("/admin/estadisticas")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> obtenerEstadisticasGlobales(
    @Parameter(
      description = "Fecha de inicio para el cálculo de estadísticas"
    ) @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaInicio,
    @Parameter(
      description = "Fecha de fin para el cálculo de estadísticas"
    ) @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaFin
  ) {
    log.info(
      "Obteniendo estadísticas globales - Período: {} a {}",
      fechaInicio,
      fechaFin
    );

    Map<String, Object> estadisticas =
      carritoService.obtenerEstadisticasGlobales(fechaInicio, fechaFin);

    log.info("Estadísticas globales recuperadas exitosamente");
    return ResponseEntity.ok(estadisticas);
  }

  @Operation(
    summary = "Exportar datos de carrito",
    description = "Exporta datos de carritos en formato especificado para análisis"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Datos exportados exitosamente",
        content = @Content(mediaType = "application/octet-stream")
      ),
    }
  )
  @GetMapping("/admin/exportar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<byte[]> exportarDatos(
    @Parameter(
      description = "Formato de exportación",
      example = "CSV"
    ) @RequestParam(defaultValue = "CSV") String formato,
    @Parameter(
      description = "Fecha de inicio para la exportación"
    ) @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime fechaInicio,
    @Parameter(description = "Fecha de fin para la exportación") @RequestParam(
      required = false
    ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
  ) {
    log.info(
      "Exportando datos de carritos - Formato: {}, Período: {} a {}",
      formato,
      fechaInicio,
      fechaFin
    );

    byte[] datosExportados = carritoService.exportarDatos(
      formato,
      fechaInicio,
      fechaFin
    );

    String nombreArchivo = String.format(
      "carritos_export_%s.%s",
      LocalDateTime.now().toString().replaceAll(":", "-"),
      formato.toLowerCase()
    );

    log.info("Datos exportados exitosamente - Archivo: {}", nombreArchivo);

    return ResponseEntity.ok()
      .header("Content-Disposition", "attachment; filename=" + nombreArchivo)
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(datosExportados);
  }

  // ============================================
  // ENDPOINTS DE SALUD Y MONITOREO
  // ============================================

  @Operation(
    summary = "Verificar estado del servicio",
    description = "Endpoint de health check para monitoreo del servicio de carritos"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Servicio operativo",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping("/health")
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> health = Map.of(
      "status",
      "UP",
      "timestamp",
      LocalDateTime.now(),
      "service",
      "msvc-carrito",
      "version",
      "2.0.0"
    );

    return ResponseEntity.ok(health);
  }
}
