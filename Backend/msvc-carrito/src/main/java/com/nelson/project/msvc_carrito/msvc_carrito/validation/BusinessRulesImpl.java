package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoCoreService;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementación de reglas de negocio para el carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Service
@RequiredArgsConstructor
public class BusinessRulesImpl implements BusinessRules {

  private static final Logger log = LoggerFactory.getLogger(
    BusinessRulesImpl.class
  );

  // Constantes de negocio
  private static final int MAX_ITEMS_POR_CARRITO = 50;
  private static final int MAX_CANTIDAD_POR_ITEM = 999;
  private static final int MIN_CANTIDAD_POR_ITEM = 1;
  private static final BigDecimal PRECIO_MINIMO_ITEM = new BigDecimal("0.01");
  private static final BigDecimal VALOR_MAXIMO_CARRITO = new BigDecimal(
    "10000.00"
  );

  private final CarritoCoreService carritoCoreService;

  @Override
  public ValidationResult validarCarrito(CarritoDto carrito) {
    log.debug("Validando carrito completo: {}", carrito.getId());

    ValidationResult result = ValidationResult.success()
      .context("validar_carrito");

    if (carrito == null) {
      return result.addError("carrito", "Carrito es requerido", "CARRITO_NULL");
    }

    // Validar límites del carrito
    result.combine(validarLimitesCarrito(carrito));

    // Validar cada item
    if (carrito.getItems() != null) {
      for (ItemCarritoDto item : carrito.getItems()) {
        result.combine(validarItem(item));
      }
    }

    // Validar totales
    if (
      carrito.getTotal() != null &&
      carrito.getTotal().compareTo(VALOR_MAXIMO_CARRITO) > 0
    ) {
      result
        .addWarning("total", "El valor del carrito es muy alto", "VALOR_ALTO")
        .addWarning(
          "total",
          "Considere dividir la compra",
          "RECOMENDACION_DIVISION"
        );
    }

    return result;
  }

  @Override
  public ValidationResult validarCreacionCarrito(
    Long usuarioId,
    String ipCliente
  ) {
    log.debug("Validando creación de carrito para usuario: {}", usuarioId);

    ValidationResult result = ValidationResult.success()
      .context("crear_carrito");

    if (usuarioId == null || usuarioId <= 0) {
      result.addError(
        "usuarioId",
        "ID de usuario inválido",
        "USUARIO_ID_INVALIDO"
      );
    }

    if (ipCliente == null || ipCliente.trim().isEmpty()) {
      result.addWarning(
        "ipCliente",
        "IP de cliente no proporcionada",
        "IP_FALTANTE"
      );
    }

    return result;
  }

  @Override
  public ValidationResult validarAgregarItem(
    Long carritoId,
    Long productoId,
    Integer cantidad,
    Long usuarioId
  ) {
    log.debug(
      "Validando agregar item: carritoId={}, productoId={}, cantidad={}",
      carritoId,
      productoId,
      cantidad
    );

    ValidationResult result = ValidationResult.success()
      .context("agregar_item");

    // Validaciones básicas
    if (carritoId == null || carritoId <= 0) {
      result.addError(
        "carritoId",
        "ID de carrito inválido",
        "CARRITO_ID_INVALIDO"
      );
    }

    if (productoId == null || productoId <= 0) {
      result.addError(
        "productoId",
        "ID de producto inválido",
        "PRODUCTO_ID_INVALIDO"
      );
    }

    if (cantidad == null || cantidad < MIN_CANTIDAD_POR_ITEM) {
      result.addError(
        "cantidad",
        "Cantidad debe ser mayor a cero",
        "CANTIDAD_INVALIDA"
      );
    } else if (cantidad > MAX_CANTIDAD_POR_ITEM) {
      result.addError(
        "cantidad",
        "Cantidad excede el máximo permitido",
        "CANTIDAD_MAXIMA_EXCEDIDA"
      );
    }

    if (usuarioId == null || usuarioId <= 0) {
      result.addError(
        "usuarioId",
        "ID de usuario inválido",
        "USUARIO_ID_INVALIDO"
      );
    }

    // Validar permisos si las validaciones básicas pasaron
    if (result.getIsValid() && carritoId != null && usuarioId != null) {
      result.combine(validarPermisos(carritoId, usuarioId));
    }

    return result;
  }

  @Override
  public ValidationResult validarActualizarCantidad(
    Long carritoId,
    Long itemId,
    Integer nuevaCantidad,
    Long usuarioId
  ) {
    log.debug(
      "Validando actualizar cantidad: carritoId={}, itemId={}, cantidad={}",
      carritoId,
      itemId,
      nuevaCantidad
    );

    ValidationResult result = ValidationResult.success()
      .context("actualizar_cantidad");

    if (itemId == null || itemId <= 0) {
      result.addError("itemId", "ID de item inválido", "ITEM_ID_INVALIDO");
    }

    if (nuevaCantidad == null || nuevaCantidad < 0) {
      result.addError(
        "nuevaCantidad",
        "Cantidad inválida",
        "CANTIDAD_INVALIDA"
      );
    } else if (nuevaCantidad > MAX_CANTIDAD_POR_ITEM) {
      result.addError(
        "nuevaCantidad",
        "Cantidad excede el máximo permitido",
        "CANTIDAD_MAXIMA_EXCEDIDA"
      );
    }

    // Validar permisos
    if (carritoId != null && usuarioId != null) {
      result.combine(validarPermisos(carritoId, usuarioId));
    }

    return result;
  }

  @Override
  public ValidationResult validarAplicarDescuento(
    Long carritoId,
    String codigoDescuento,
    Long usuarioId
  ) {
    log.debug(
      "Validando aplicar descuento: carritoId={}, codigo={}",
      carritoId,
      codigoDescuento
    );

    ValidationResult result = ValidationResult.success()
      .context("aplicar_descuento");

    if (codigoDescuento == null || codigoDescuento.trim().isEmpty()) {
      result.addError(
        "codigoDescuento",
        "Código de descuento es requerido",
        "CODIGO_REQUERIDO"
      );
    }

    if (carritoId != null && usuarioId != null) {
      result.combine(validarPermisos(carritoId, usuarioId));
    }

    return result;
  }

  @Override
  public ValidationResult validarLimitesCarrito(CarritoDto carrito) {
    ValidationResult result = ValidationResult.success()
      .context("validar_limites");

    if (
      carrito.getItems() != null &&
      carrito.getItems().size() > MAX_ITEMS_POR_CARRITO
    ) {
      result.addError(
        "items",
        "Carrito excede el máximo de items permitidos",
        "LIMITE_ITEMS_EXCEDIDO"
      );
    }

    return result;
  }

  @Override
  public ValidationResult validarStock(
    Long productoId,
    Integer cantidadSolicitada
  ) {
    log.debug(
      "Validando stock: productoId={}, cantidad={}",
      productoId,
      cantidadSolicitada
    );

    ValidationResult result = ValidationResult.success()
      .context("validar_stock");

    // TODO: Implementar validación real de stock con ProductoFeignClient
    // Por ahora simulamos la validación
    if (cantidadSolicitada != null && cantidadSolicitada > 100) {
      result
        .addWarning("stock", "Cantidad solicitada es muy alta", "CANTIDAD_ALTA")
        .addWarning(
          "stock",
          "Verificar disponibilidad con proveedor",
          "VERIFICAR_PROVEEDOR"
        );
    }

    return result;
  }

  @Override
  public ValidationResult validarPermisos(Long carritoId, Long usuarioId) {
    log.debug(
      "Validando permisos: carritoId={}, usuarioId={}",
      carritoId,
      usuarioId
    );

    ValidationResult result = ValidationResult.success()
      .context("validar_permisos");

    try {
      Optional<CarritoDto> carritoOpt = carritoCoreService.obtenerCarritoPorId(
        carritoId,
        usuarioId
      );
      if (carritoOpt.isEmpty()) {
        result.addError(
          "permisos",
          "No tiene permisos para acceder a este carrito",
          "ACCESO_DENEGADO"
        );
      }
    } catch (Exception e) {
      log.warn("Error validando permisos: {}", e.getMessage());
      result.addError(
        "permisos",
        "Error validando permisos",
        "ERROR_VALIDACION_PERMISOS"
      );
    }

    return result;
  }

  @Override
  public ValidationResult validarItem(ItemCarritoDto item) {
    ValidationResult result = ValidationResult.success()
      .context("validar_item");

    if (item == null) {
      return result.addError("item", "Item es requerido", "ITEM_NULL");
    }

    if (item.getProductoId() == null || item.getProductoId() <= 0) {
      result.addError(
        "item.productoId",
        "ID de producto inválido",
        "PRODUCTO_ID_INVALIDO"
      );
    }

    if (
      item.getCantidad() == null || item.getCantidad() < MIN_CANTIDAD_POR_ITEM
    ) {
      result.addError(
        "item.cantidad",
        "Cantidad inválida",
        "CANTIDAD_INVALIDA"
      );
    }

    if (
      item.getPrecioUnitario() == null ||
      item.getPrecioUnitario().compareTo(PRECIO_MINIMO_ITEM) < 0
    ) {
      result.addError(
        "item.precioUnitario",
        "Precio unitario inválido",
        "PRECIO_INVALIDO"
      );
    }

    return result;
  }

  @Override
  public ValidationResult validarReglasNegocio(
    CarritoDto carrito,
    String operacion
  ) {
    log.debug("Validando reglas de negocio para operación: {}", operacion);

    ValidationResult result = ValidationResult.success()
      .context("reglas_negocio");

    // Reglas específicas por operación
    switch (operacion.toUpperCase()) {
      case "CHECKOUT":
        // Validaciones específicas para checkout
        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
          result.addError(
            "checkout",
            "No se puede procesar un carrito vacío",
            "CARRITO_VACIO"
          );
        }
        break;
      case "APLICAR_DESCUENTO":
        // Validaciones para aplicar descuentos
        if (
          carrito.getTotal() != null &&
          carrito.getTotal().compareTo(BigDecimal.TEN) < 0
        ) {
          result.addWarning(
            "descuento",
            "El monto es muy bajo para descuentos",
            "MONTO_BAJO_DESCUENTO"
          );
        }
        break;
      default:
        log.debug("No hay reglas específicas para la operación: {}", operacion);
    }

    return result;
  }
}
