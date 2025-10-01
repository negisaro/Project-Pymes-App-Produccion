package com.nelson.project.msvc_carrito.msvc_carrito.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para la solicitud de aplicar un cupón de descuento
 * REFACTORIZACIÓN PENDIENTE: Candidato ideal para Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * Cambios aplicados: Validaciones mejoradas, documentación expandida
 */
@Schema(description = "Solicitud para aplicar un cupón de descuento al carrito")
public class AplicarCuponRequest {

  @NotBlank(message = "El código del cupón es obligatorio")
  @Size(
    min = 3,
    max = 50,
    message = "El código del cupón debe tener entre 3 y 50 caracteres"
  )
  @Pattern(
    regexp = "^[A-Z0-9_-]+$",
    message = "El código del cupón solo puede contener letras mayúsculas, números, guiones y guiones bajos"
  )
  @Schema(
    description = "Código del cupón a aplicar",
    example = "DESCUENTO10",
    required = true,
    pattern = "^[A-Z0-9_-]+$"
  )
  private String codigoCupon;

  @Size(
    max = 200,
    message = "Las observaciones no pueden exceder 200 caracteres"
  )
  @Schema(
    description = "Observaciones adicionales del cliente sobre el cupón",
    example = "Cupón recibido por email promocional"
  )
  private String observaciones;

  @Schema(
    description = "Indica si aplicar el cupón automáticamente en el checkout",
    example = "true"
  )
  private Boolean aplicarAutomaticamente = true;

  @Schema(
    description = "Canal por el cual se obtuvo el cupón",
    example = "EMAIL",
    allowableValues = {
      "EMAIL", "SMS", "WEB", "MOBILE_APP", "STORE", "SOCIAL", "OTHER",
    }
  )
  private String canalOrigen;

  // Constructores
  public AplicarCuponRequest() {}

  public AplicarCuponRequest(String codigoCupon) {
    this.codigoCupon = codigoCupon;
    this.aplicarAutomaticamente = true;
  }

  public AplicarCuponRequest(String codigoCupon, String observaciones) {
    this.codigoCupon = codigoCupon;
    this.observaciones = observaciones;
    this.aplicarAutomaticamente = true;
  }

  public AplicarCuponRequest(
    String codigoCupon,
    String observaciones,
    String canalOrigen
  ) {
    this.codigoCupon = codigoCupon;
    this.observaciones = observaciones;
    this.canalOrigen = canalOrigen;
    this.aplicarAutomaticamente = true;
  }

  // Getters y Setters
  public String getCodigoCupon() {
    return codigoCupon;
  }

  public void setCodigoCupon(String codigoCupon) {
    this.codigoCupon = codigoCupon;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Boolean getAplicarAutomaticamente() {
    return aplicarAutomaticamente;
  }

  public void setAplicarAutomaticamente(Boolean aplicarAutomaticamente) {
    this.aplicarAutomaticamente = aplicarAutomaticamente;
  }

  public String getCanalOrigen() {
    return canalOrigen;
  }

  public void setCanalOrigen(String canalOrigen) {
    this.canalOrigen = canalOrigen;
  }

  // Métodos de utilidad
  public boolean tieneObservaciones() {
    return observaciones != null && !observaciones.trim().isEmpty();
  }

  public boolean debeAplicarAutomaticamente() {
    return aplicarAutomaticamente != null && aplicarAutomaticamente;
  }

  public String getCodigoCuponNormalizado() {
    return codigoCupon != null ? codigoCupon.toUpperCase().trim() : null;
  }

  public boolean tieneCanalOrigen() {
    return canalOrigen != null && !canalOrigen.trim().isEmpty();
  }

  @Override
  public String toString() {
    return (
      "AplicarCuponRequest{" +
      "codigoCupon='" +
      codigoCupon +
      '\'' +
      ", observaciones='" +
      observaciones +
      '\'' +
      ", aplicarAutomaticamente=" +
      aplicarAutomaticamente +
      ", canalOrigen='" +
      canalOrigen +
      '\'' +
      '}'
    );
  }
}
