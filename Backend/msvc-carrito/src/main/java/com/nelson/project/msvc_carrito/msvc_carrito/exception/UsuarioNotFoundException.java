package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción específica cuando un usuario no existe en el sistema.
 * Parte del manejo empresarial de errores.
 *
 * @author Sistema Automatizado
 * @version 2.0.0
 * @since 2025-09-30
 */
public class UsuarioNotFoundException extends BusinessException {

  private static final long serialVersionUID = 1L;
  private final Long usuarioId;

  public UsuarioNotFoundException(Long usuarioId) {
    super("Usuario no encontrado con ID: " + usuarioId, "USUARIO_NOT_FOUND");
    this.usuarioId = usuarioId;
  }

  public UsuarioNotFoundException(String mensaje) {
    super(mensaje, "USUARIO_NOT_FOUND");
    this.usuarioId = null;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }
}
