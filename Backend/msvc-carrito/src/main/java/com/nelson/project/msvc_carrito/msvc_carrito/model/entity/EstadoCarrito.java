package com.nelson.project.msvc_carrito.msvc_carrito.model.entity;

/**
 * Enum que define los estados posibles de un carrito de compras.
 *
 * Estados del carrito:
 * - ACTIVO: Carrito en uso, puede ser modificado
 * - ABANDONADO: Carrito sin actividad por tiempo prolongado
 * - PROCESADO: Carrito convertido en pedido
 * - EXPIRADO: Carrito que ha superado el tiempo límite
 * - BLOQUEADO: Carrito bloqueado por problemas de seguridad
 */
public enum EstadoCarrito {
  /**
   * Carrito activo y editable por el usuario
   */
  ACTIVO("Activo", "Carrito disponible para modificaciones"),

  /**
   * Carrito sin actividad reciente
   */
  ABANDONADO("Abandonado", "Carrito sin actividad por tiempo prolongado"),

  /**
   * Carrito procesado como pedido
   */
  PROCESADO("Procesado", "Carrito convertido en pedido exitosamente"),

  /**
   * Carrito que ha expirado
   */
  EXPIRADO("Expirado", "Carrito que superó el tiempo límite de vida"),

  /**
   * Carrito bloqueado por seguridad
   */
  BLOQUEADO("Bloqueado", "Carrito bloqueado por motivos de seguridad");

  private final String nombre;
  private final String descripcion;

  EstadoCarrito(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  /**
   * Verifica si el carrito puede ser modificado
   */
  public boolean esModificable() {
    return this == ACTIVO;
  }

  /**
   * Verifica si el carrito está en un estado final
   */
  public boolean esFinal() {
    return this == PROCESADO || this == EXPIRADO;
  }

  /**
   * Obtiene el siguiente estado lógico
   */
  public EstadoCarrito siguienteEstado() {
    return switch (this) {
      case ACTIVO -> ABANDONADO;
      case ABANDONADO -> EXPIRADO;
      case PROCESADO, EXPIRADO, BLOQUEADO -> this; // Estados finales
    };
  }
}
