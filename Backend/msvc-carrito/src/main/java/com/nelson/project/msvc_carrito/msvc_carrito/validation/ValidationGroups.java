package com.nelson.project.msvc_carrito.msvc_carrito.validation;

/**
 * Grupos de validación para contextos específicos de operaciones CRUD y de negocio.
 * Permite aplicar validaciones diferentes según el contexto de uso de los DTOs.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-01-01
 */
public final class ValidationGroups {

  /**
   * Constructor privado para prevenir instanciación de clase utilitaria
   */
  private ValidationGroups() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Grupo de validación para operaciones de creación.
   * Se aplica cuando se crea una nueva entidad.
   *
   * Validaciones típicas:
   * - Campos obligatorios para creación
   * - ID debe ser null
   * - Valores por defecto válidos
   */
  public interface OnCreate {}

  /**
   * Grupo de validación para operaciones de actualización.
   * Se aplica cuando se modifica una entidad existente.
   *
   * Validaciones típicas:
   * - ID debe estar presente
   * - Campos modificables
   * - Validaciones de integridad
   */
  public interface OnUpdate {}

  /**
   * Grupo de validación para operaciones de eliminación.
   * Se aplica antes de eliminar una entidad.
   *
   * Validaciones típicas:
   * - ID debe estar presente
   * - Validar dependencias
   * - Estado válido para eliminación
   */
  public interface OnDelete {}

  /**
   * Grupo de validación para operaciones de pago/checkout.
   * Se aplica en el proceso de pago del carrito.
   *
   * Validaciones típicas:
   * - Carrito debe tener items
   * - Stock disponible
   * - Precios actualizados
   * - Información de pago válida
   */
  public interface OnPayment {}

  /**
   * Grupo de validación para operaciones de búsqueda.
   * Se aplica en filtros y criterios de búsqueda.
   *
   * Validaciones típicas:
   * - Parámetros de búsqueda válidos
   * - Rangos de fechas coherentes
   * - Límites de paginación
   */
  public interface OnSearch {}

  /**
   * Grupo de validación para operaciones de administración.
   * Se aplica en operaciones administrativas que requieren privilegios especiales.
   *
   * Validaciones típicas:
   * - Permisos administrativos
   * - Operaciones masivas
   * - Configuraciones del sistema
   */
  public interface OnAdmin {}

  /**
   * Grupo de validación para operaciones críticas del carrito.
   * Se aplica en operaciones que afectan el estado crítico del carrito.
   *
   * Validaciones típicas:
   * - Cambios de estado del carrito
   * - Aplicación de descuentos
   * - Modificaciones de cantidad
   */
  public interface OnCartOperation {}

  /**
   * Grupo de validación para operaciones de inventario.
   * Se aplica cuando se verifica disponibilidad de productos.
   *
   * Validaciones típicas:
   * - Stock disponible
   * - Producto activo
   * - Precios válidos
   */
  public interface OnInventoryCheck {}
}
