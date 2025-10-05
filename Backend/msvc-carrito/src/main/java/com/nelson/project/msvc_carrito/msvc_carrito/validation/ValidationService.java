package com.nelson.project.msvc_carrito.msvc_carrito.validation;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ItemCarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Servicio utilitario para aplicar validaciones contextuales usando Validation Groups.
 * Proporciona métodos de conveniencia para validar DTOs en contextos específicos.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-01-01
 */
@Component
public class ValidationService {

  private final Validator validator;

  public ValidationService(Validator validator) {
    this.validator = validator;
  }

  /**
   * Valida un CarritoDto para operaciones de creación
   */
  public Set<ConstraintViolation<CarritoDto>> validateForCreation(
    CarritoDto carrito
  ) {
    return validator.validate(carrito, ValidationGroups.OnCreate.class);
  }

  /**
   * Valida un CarritoDto para operaciones de actualización
   */
  public Set<ConstraintViolation<CarritoDto>> validateForUpdate(
    CarritoDto carrito
  ) {
    return validator.validate(carrito, ValidationGroups.OnUpdate.class);
  }

  /**
   * Valida un CarritoDto para proceso de pago
   */
  public Set<ConstraintViolation<CarritoDto>> validateForPayment(
    CarritoDto carrito
  ) {
    return validator.validate(carrito, ValidationGroups.OnPayment.class);
  }

  /**
   * Valida un CarritoDto para operaciones críticas del carrito
   */
  public Set<ConstraintViolation<CarritoDto>> validateForCartOperation(
    CarritoDto carrito
  ) {
    return validator.validate(carrito, ValidationGroups.OnCartOperation.class);
  }

  /**
   * Valida un ItemCarritoDto para operaciones de creación
   */
  public Set<ConstraintViolation<ItemCarritoDto>> validateItemForCreation(
    ItemCarritoDto item
  ) {
    return validator.validate(item, ValidationGroups.OnCreate.class);
  }

  /**
   * Valida un ItemCarritoDto para verificación de inventario
   */
  public Set<ConstraintViolation<ItemCarritoDto>> validateItemForInventory(
    ItemCarritoDto item
  ) {
    return validator.validate(item, ValidationGroups.OnInventoryCheck.class);
  }

  /**
   * Valida un ProductoDto para verificación de inventario
   */
  public Set<ConstraintViolation<ProductoDto>> validateProductForInventory(
    ProductoDto producto
  ) {
    return validator.validate(
      producto,
      ValidationGroups.OnInventoryCheck.class
    );
  }

  /**
   * Valida un ProductoDto para operaciones de creación
   */
  public Set<ConstraintViolation<ProductoDto>> validateProductForCreation(
    ProductoDto producto
  ) {
    return validator.validate(producto, ValidationGroups.OnCreate.class);
  }

  /**
   * Verifica si una validación tiene errores
   */
  public boolean hasErrors(Set<? extends ConstraintViolation<?>> violations) {
    return !violations.isEmpty();
  }

  /**
   * Obtiene el primer mensaje de error de las violaciones
   */
  public String getFirstErrorMessage(
    Set<? extends ConstraintViolation<?>> violations
  ) {
    return violations
      .stream()
      .findFirst()
      .map(ConstraintViolation::getMessage)
      .orElse("Validación exitosa");
  }

  /**
   * Combina múltiples mensajes de error en uno solo
   */
  public String getAllErrorMessages(
    Set<? extends ConstraintViolation<?>> violations
  ) {
    return violations
      .stream()
      .map(ConstraintViolation::getMessage)
      .reduce("", (acc, msg) -> acc.isEmpty() ? msg : acc + "; " + msg);
  }
}
