package com.nelson.project.msvc_carrito.msvc_carrito.event;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Evento base para todas las operaciones del carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Getter
public abstract class CarritoEvent extends ApplicationEvent {

  private final Long carritoId;
  private final Long usuarioId;
  private final String tipoEvento;
  private final LocalDateTime eventTimestamp;
  private final String metadata;

  public CarritoEvent(
    Object source,
    Long carritoId,
    Long usuarioId,
    String tipoEvento,
    String metadata
  ) {
    super(source);
    this.carritoId = carritoId;
    this.usuarioId = usuarioId;
    this.tipoEvento = tipoEvento;
    this.metadata = metadata;
    this.eventTimestamp = LocalDateTime.now();
  }

  public CarritoEvent(
    Object source,
    Long carritoId,
    Long usuarioId,
    String tipoEvento
  ) {
    this(source, carritoId, usuarioId, tipoEvento, null);
  }
}
