package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.EstadoCarrito;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para la solicitud de actualizar el estado del carrito
 * REFACTORIZACIÓN PENDIENTE: Candidato para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * Cambios aplicados: Documentación mejorada, validaciones consistentes
 */
@Schema(description = "Solicitud para actualizar el estado del carrito")
public class ActualizarEstadoCarritoRequest {

  @NotNull(message = "El nuevo estado es obligatorio")
  @Schema(
    description = "Nuevo estado del carrito",
    example = "ABANDONADO",
    required = true,
    allowableValues = {
      "ACTIVO", "ABANDONADO", "PROCESADO", "EXPIRADO", "BLOQUEADO",
    }
  )
  private EstadoCarrito nuevoEstado;

  @Size(max = 300, message = "El motivo no puede exceder 300 caracteres")
  @Schema(
    description = "Motivo del cambio de estado",
    example = "Carrito abandonado por inactividad del usuario"
  )
  private String motivo;

  @Schema(
    description = "Indica si se debe notificar al usuario sobre el cambio",
    example = "true"
  )
  private Boolean notificarUsuario = false;

  @Schema(
    description = "Canal por el cual notificar al usuario",
    example = "EMAIL",
    allowableValues = { "EMAIL", "SMS", "PUSH", "NONE" }
  )
  private String canalNotificacion;

  // Constructores
  public ActualizarEstadoCarritoRequest() {}

  public ActualizarEstadoCarritoRequest(EstadoCarrito nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
    this.notificarUsuario = false;
  }

  public ActualizarEstadoCarritoRequest(
    EstadoCarrito nuevoEstado,
    String motivo
  ) {
    this.nuevoEstado = nuevoEstado;
    this.motivo = motivo;
    this.notificarUsuario = false;
  }

  public ActualizarEstadoCarritoRequest(
    EstadoCarrito nuevoEstado,
    String motivo,
    Boolean notificarUsuario
  ) {
    this.nuevoEstado = nuevoEstado;
    this.motivo = motivo;
    this.notificarUsuario = notificarUsuario != null ? notificarUsuario : false;
  }

  // Getters y Setters
  public EstadoCarrito getNuevoEstado() {
    return nuevoEstado;
  }

  public void setNuevoEstado(EstadoCarrito nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public Boolean getNotificarUsuario() {
    return notificarUsuario;
  }

  public void setNotificarUsuario(Boolean notificarUsuario) {
    this.notificarUsuario = notificarUsuario;
  }

  public String getCanalNotificacion() {
    return canalNotificacion;
  }

  public void setCanalNotificacion(String canalNotificacion) {
    this.canalNotificacion = canalNotificacion;
  }

  // Métodos de utilidad
  public boolean debeNotificar() {
    return notificarUsuario != null && notificarUsuario;
  }

  public boolean tieneMotivo() {
    return motivo != null && !motivo.trim().isEmpty();
  }

  public boolean esCambioAEstadoFinal() {
    return nuevoEstado != null && nuevoEstado.esFinal();
  }

  @Override
  public String toString() {
    return (
      "ActualizarEstadoCarritoRequest{" +
      "nuevoEstado=" +
      nuevoEstado +
      ", motivo='" +
      motivo +
      '\'' +
      ", notificarUsuario=" +
      notificarUsuario +
      ", canalNotificacion='" +
      canalNotificacion +
      '\'' +
      '}'
    );
  }
}
