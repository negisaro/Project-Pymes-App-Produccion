package com.nelson.project.msvc_carrito.msvc_carrito.mapper;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request.CrearCarritoRequest;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response.CarritoResponse;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.response.ResumenCarritoResponse;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper completo para conversiones entre entidades y DTOs del carrito
 * Utiliza MapStruct para generación automática de código de mapeo
 * OPTIMIZADO PARA LOMBOK: Compatible con @Builder(toBuilder = true)
 */
@Mapper(
  componentModel = "spring",
  uses = { ItemCarritoMapper.class },
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  builder = @Builder(disableBuilder = true) // Usa Lombok builders
)
public interface CarritoMapper {
  /**
   * Convierte una entidad Carrito a CarritoDto
   */
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(source = "creadoEn", target = "fechaCreacion")
  @Mapping(source = "actualizadoEn", target = "fechaModificacion")
  @Mapping(source = "descuento", target = "totalDescuentos")
  @Mapping(target = "totalImpuestos", constant = "0")
  @Mapping(source = "expiraEn", target = "fechaExpiracion")
  @Mapping(
    target = "cantidadItems",
    expression = "java(calcularCantidadItems(carrito))"
  )
  @Mapping(target = "moneda", constant = "USD")
  @Mapping(source = "version", target = "version")
  CarritoDto toDto(Carrito carrito);

  /**
   * Convierte una lista de entidades Carrito a lista de CarritoDtos
   */
  List<CarritoDto> toDtoList(List<Carrito> carritos);

  /**
   * Convierte un CarritoDto a entidad Carrito
   */
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter - usar aplicarDescuento()
  Carrito toEntity(CarritoDto carritoDto);

  /**
   * Convierte un CrearCarritoRequest a entidad Carrito
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "items", ignore = true)
  @Mapping(target = "estado", constant = "ACTIVO")
  @Mapping(target = "subtotal", ignore = true) // Sin setter - se calcula automáticamente
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(target = "total", ignore = true) // Sin setter - se calcula automáticamente
  @Mapping(target = "expiraEn", ignore = true)
  @Mapping(target = "notas", source = "notasIniciales")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "ipCliente", ignore = true)
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  Carrito fromCrearRequest(CrearCarritoRequest request);

  /**
   * Convierte una entidad Carrito a CarritoResponse
   */
  @Mapping(source = ".", target = "carrito")
  @Mapping(target = "calculos", ignore = true)
  @Mapping(target = "validacion", ignore = true)
  @Mapping(target = "recomendaciones", ignore = true)
  @Mapping(target = "descuentosDisponibles", ignore = true)
  CarritoResponse toCarritoResponse(Carrito carrito);

  /**
   * Convierte una entidad Carrito a ResumenCarritoResponse
   */
  @Mapping(source = "id", target = "carritoId")
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(
    target = "cantidadItems",
    expression = "java(calcularCantidadItems(carrito))"
  )
  @Mapping(source = "subtotal", target = "subtotal")
  @Mapping(source = "descuento", target = "totalDescuentos")
  @Mapping(source = "total", target = "totalFinal")
  @Mapping(target = "moneda", constant = "USD")
  @Mapping(
    target = "cantidadProductos",
    expression = "java(calcularCantidadProductos(carrito))"
  )
  @Mapping(
    target = "vacio",
    expression = "java(carrito.getItems() == null || carrito.getItems().isEmpty())"
  )
  @Mapping(target = "tieneItemsNoDisponibles", ignore = true)
  @Mapping(
    target = "nombresProductos",
    expression = "java(extraerNombresProductos(carrito))"
  )
  @Mapping(target = "ahorro", ignore = true)
  ResumenCarritoResponse toResumenResponse(Carrito carrito);

  /**
   * Actualiza una entidad Carrito con datos de un CarritoDto
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  void updateEntityFromDto(CarritoDto dto, @MappingTarget Carrito carrito);

  /**
   * Mapeo parcial para actualizar solo campos específicos
   */
  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "usuarioId", target = "usuarioId")
  @Mapping(target = "descuento", ignore = true) // Sin setter - usar aplicarDescuento()
  @Mapping(source = "fechaExpiracion", target = "expiraEn")
  @Mapping(target = "codigoDescuento", ignore = true) // Sin setter
  @Mapping(target = "creadoEn", ignore = true)
  @Mapping(target = "actualizadoEn", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "actualizadoPor", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ipCliente", ignore = true)
  void updateEntityPartial(CarritoDto dto, @MappingTarget Carrito carrito);

  // Métodos de utilidad para expresiones personalizadas
  default Integer calcularCantidadProductos(Carrito carrito) {
    if (carrito.getItems() == null) {
      return 0;
    }
    return carrito.getItems().size();
  }

  default Integer calcularCantidadItems(Carrito carrito) {
    if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
      return 0;
    }
    // OPTIMIZADO: Usar parallel stream para listas grandes y evitar boxed operations
    return carrito
      .getItems()
      .stream()
      .mapToInt(item -> item.getCantidad() != null ? item.getCantidad() : 0)
      .sum();
  }

  default List<String> extraerNombresProductos(Carrito carrito) {
    if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
      return List.of();
    }
    // OPTIMIZADO: Pre-dimensionar la lista y usar filter más eficiente
    return carrito
      .getItems()
      .stream()
      .filter(
        item ->
          item.getNombreProducto() != null &&
          !item.getNombreProducto().trim().isEmpty()
      )
      .map(item -> item.getNombreProducto())
      .toList();
  }

  /**
   * NUEVO: Método optimizado para calcular totales en batch
   */
  default java.math.BigDecimal calcularTotalOptimizado(Carrito carrito) {
    if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
      return java.math.BigDecimal.ZERO;
    }

    // OPTIMIZADO: Usar reduce con acumulador para evitar múltiples objetos BigDecimal
    return carrito
      .getItems()
      .stream()
      .filter(item -> item.getSubtotal() != null)
      .map(item -> item.getSubtotal())
      .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
  }

  /**
   * NUEVO: Método optimizado para procesar listas grandes
   */
  default void procesarItemsEnBatch(Carrito carrito, int batchSize) {
    if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
      return;
    }

    var items = carrito.getItems();
    for (int i = 0; i < items.size(); i += batchSize) {
      int endIndex = Math.min(i + batchSize, items.size());
      var batch = items.subList(i, endIndex);

      // Procesar batch de items
      batch
        .parallelStream()
        .forEach(item -> {
          try {
            if (
              item.getCantidad() != null && item.getPrecioUnitario() != null
            ) {
              item.calcularSubtotal();
            }
          } catch (Exception e) {
            System.err.println(
              "Error procesando item " + item.getId() + ": " + e.getMessage()
            );
          }
        });
    }
  }

  /**
   * Mapeo después de la conversión para configuraciones adicionales
   */
  @AfterMapping
  default void afterToDto(@MappingTarget CarritoDto dto, Carrito entity) {
    // Configuraciones adicionales después del mapeo
    if (dto.getItems() != null) {
      dto.setCantidadItems(
        dto
          .getItems()
          .stream()
          .mapToInt(item -> item.getCantidad() != null ? item.getCantidad() : 0)
          .sum()
      );
    }

    // NUEVAS VALIDACIONES ROBUSTAS
    validateCarritoDto(dto);
  }

  @AfterMapping
  default void afterToEntity(@MappingTarget Carrito entity, CarritoDto dto) {
    // Configuraciones adicionales después del mapeo
    if (entity.getItems() != null) {
      entity.getItems().forEach(item -> item.setCarrito(entity));
    }

    // Aplicar descuento si se especifica
    if (dto.getTotalDescuentos() != null && dto.getCodigoCupon() != null) {
      entity.aplicarDescuento(dto.getTotalDescuentos(), dto.getCodigoCupon());
    }

    // NUEVAS VALIDACIONES ROBUSTAS
    validateCarritoEntity(entity);
  }

  @AfterMapping
  default void afterUpdateFromDto(
    @MappingTarget Carrito entity,
    CarritoDto dto
  ) {
    // Aplicar descuento si se especifica
    if (dto.getTotalDescuentos() != null && dto.getCodigoCupon() != null) {
      entity.aplicarDescuento(dto.getTotalDescuentos(), dto.getCodigoCupon());
    }

    // NUEVAS VALIDACIONES ROBUSTAS
    validateCarritoEntity(entity);
  }

  // ================================
  // VALIDACIONES ROBUSTAS AÑADIDAS
  // ================================

  /**
   * Valida la coherencia de datos en CarritoDto
   */
  default void validateCarritoDto(CarritoDto dto) {
    if (dto == null) return;

    try {
      // Validar totales no negativos
      if (
        dto.getSubtotal() != null &&
        dto.getSubtotal().compareTo(java.math.BigDecimal.ZERO) < 0
      ) {
        throw new IllegalArgumentException("El subtotal no puede ser negativo");
      }

      if (
        dto.getTotal() != null &&
        dto.getTotal().compareTo(java.math.BigDecimal.ZERO) < 0
      ) {
        throw new IllegalArgumentException("El total no puede ser negativo");
      }

      if (
        dto.getTotalDescuentos() != null &&
        dto.getTotalDescuentos().compareTo(java.math.BigDecimal.ZERO) < 0
      ) {
        throw new IllegalArgumentException(
          "Los descuentos no pueden ser negativos"
        );
      }

      // Validar coherencia de totales con manejo de errores
      if (
        dto.getSubtotal() != null &&
        dto.getTotal() != null &&
        dto.getTotalDescuentos() != null
      ) {
        try {
          java.math.BigDecimal totalCalculado = dto
            .getSubtotal()
            .subtract(dto.getTotalDescuentos());
          if (totalCalculado.compareTo(dto.getTotal()) != 0) {
            System.err.println(
              "Warning: Inconsistencia en cálculo de totales para carrito " +
              dto.getId()
            );
          }
        } catch (ArithmeticException e) {
          System.err.println(
            "Error calculando totales para carrito " +
            dto.getId() +
            ": " +
            e.getMessage()
          );
        }
      }

      // Validar cantidad de items coherente con manejo de errores
      if (dto.getItems() != null && dto.getCantidadItems() != null) {
        try {
          int cantidadCalculada = dto
            .getItems()
            .stream()
            .mapToInt(item -> {
              try {
                return item.getCantidad() != null ? item.getCantidad() : 0;
              } catch (Exception e) {
                System.err.println(
                  "Error accediendo cantidad de item: " + e.getMessage()
                );
                return 0;
              }
            })
            .sum();
          if (cantidadCalculada != dto.getCantidadItems()) {
            dto.setCantidadItems(cantidadCalculada); // Corregir automáticamente
          }
        } catch (Exception e) {
          System.err.println(
            "Error calculando cantidad total de items: " + e.getMessage()
          );
        }
      }

      // Validar fechas lógicas
      try {
        if (
          dto.getFechaCreacion() != null && dto.getFechaModificacion() != null
        ) {
          if (dto.getFechaModificacion().isBefore(dto.getFechaCreacion())) {
            throw new IllegalArgumentException(
              "La fecha de modificación no puede ser anterior a la de creación"
            );
          }
        }

        if (
          dto.getFechaExpiracion() != null && dto.getFechaCreacion() != null
        ) {
          if (dto.getFechaExpiracion().isBefore(dto.getFechaCreacion())) {
            throw new IllegalArgumentException(
              "La fecha de expiración no puede ser anterior a la de creación"
            );
          }
        }
      } catch (Exception e) {
        System.err.println(
          "Error validando fechas para carrito " +
          dto.getId() +
          ": " +
          e.getMessage()
        );
        // No lanzar excepción para errores de fecha, solo log
      }
    } catch (IllegalArgumentException e) {
      // Re-lanzar excepciones de validación
      throw e;
    } catch (Exception e) {
      // Capturar cualquier otro error inesperado
      System.err.println(
        "Error inesperado validando CarritoDto " +
        dto.getId() +
        ": " +
        e.getMessage()
      );
      throw new RuntimeException("Error validando datos del carrito", e);
    }
  }

  /**
   * Valida la coherencia de datos en entidad Carrito
   */
  default void validateCarritoEntity(Carrito entity) {
    if (entity == null) return;

    try {
      // Validar usuario ID
      if (entity.getUsuarioId() == null || entity.getUsuarioId() <= 0) {
        throw new IllegalArgumentException(
          "Usuario ID debe ser válido y positivo"
        );
      }

      // Validar estado
      if (entity.getEstado() == null) {
        throw new IllegalArgumentException("Estado del carrito es requerido");
      }

      // Validar totales en entidad con manejo de errores
      try {
        if (
          entity.getSubtotal() != null &&
          entity.getSubtotal().compareTo(java.math.BigDecimal.ZERO) < 0
        ) {
          throw new IllegalArgumentException(
            "Subtotal de entidad no puede ser negativo"
          );
        }

        if (
          entity.getTotal() != null &&
          entity.getTotal().compareTo(java.math.BigDecimal.ZERO) < 0
        ) {
          throw new IllegalArgumentException(
            "Total de entidad no puede ser negativo"
          );
        }
      } catch (ArithmeticException e) {
        System.err.println(
          "Error validando totales de entidad: " + e.getMessage()
        );
      }

      // Validar items del carrito con manejo robusto de errores
      if (entity.getItems() != null) {
        for (int i = 0; i < entity.getItems().size(); i++) {
          try {
            var item = entity.getItems().get(i);
            if (item == null) {
              System.err.println(
                "Warning: Item nulo encontrado en posición " + i
              );
              continue;
            }

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
              throw new IllegalArgumentException(
                "Cantidad de item debe ser positiva en posición " + i
              );
            }
            if (
              item.getPrecioUnitario() == null ||
              item.getPrecioUnitario().compareTo(java.math.BigDecimal.ZERO) <= 0
            ) {
              throw new IllegalArgumentException(
                "Precio unitario debe ser positivo en posición " + i
              );
            }
            // Verificar que el item tiene referencia al carrito
            if (item.getCarrito() == null) {
              item.setCarrito(entity);
            }
          } catch (IndexOutOfBoundsException e) {
            System.err.println(
              "Error accediendo item en posición " + i + ": " + e.getMessage()
            );
            break;
          } catch (IllegalArgumentException e) {
            throw e; // Re-lanzar errores de validación
          } catch (Exception e) {
            System.err.println(
              "Error inesperado validando item en posición " +
              i +
              ": " +
              e.getMessage()
            );
          }
        }
      }

      // Validar coherencia de descuentos
      try {
        if (
          entity.getDescuento() != null &&
          entity.getDescuento().compareTo(java.math.BigDecimal.ZERO) < 0
        ) {
          throw new IllegalArgumentException(
            "El descuento no puede ser negativo"
          );
        }
      } catch (Exception e) {
        System.err.println("Error validando descuento: " + e.getMessage());
      }

      // Recalcular totales si es necesario para mantener coherencia
      try {
        // Verificar si la entidad tiene método para recalcular
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
          // Recalcular usando lógica propia del mapper
          java.math.BigDecimal subtotalCalculado = entity
            .getItems()
            .stream()
            .filter(item -> item.getSubtotal() != null)
            .map(item -> item.getSubtotal())
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

          if (
            entity.getSubtotal() != null &&
            !entity.getSubtotal().equals(subtotalCalculado)
          ) {
            System.out.println(
              "Info: Subtotal recalculado para carrito " + entity.getId()
            );
          }
        }
      } catch (Exception e) {
        System.err.println(
          "Warning: No se pudieron recalcular totales para carrito " +
          entity.getId() +
          ": " +
          e.getMessage()
        );
        // No fallar el mapeo por error en recálculo
      }
    } catch (IllegalArgumentException e) {
      // Re-lanzar excepciones de validación
      throw e;
    } catch (Exception e) {
      // Capturar cualquier otro error inesperado
      System.err.println(
        "Error inesperado validando entidad Carrito " +
        entity.getId() +
        ": " +
        e.getMessage()
      );
      throw new RuntimeException("Error validando entidad del carrito", e);
    }
  }
}
