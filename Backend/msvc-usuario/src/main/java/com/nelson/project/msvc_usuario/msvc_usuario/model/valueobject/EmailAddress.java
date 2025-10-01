package com.nelson.project.msvc_usuario.msvc_usuario.model.valueobject;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar un Email válido dentro del dominio.
 * Renombrado a EmailAddress para evitar colisión con la anotación @Email de Bean Validation.
 */
public final class EmailAddress implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Pattern EMAIL_REGEX = Pattern.compile(
    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
  );

  private final String value;

  private EmailAddress(String value) {
    this.value = value;
  }

  public static EmailAddress of(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new IllegalArgumentException("Email no puede ser vacío");
    }
    if (!EMAIL_REGEX.matcher(raw).matches()) {
      throw new IllegalArgumentException("Formato de email inválido");
    }
    return new EmailAddress(raw.trim());
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EmailAddress)) return false;
    EmailAddress email = (EmailAddress) o;
    return value.equalsIgnoreCase(email.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value.toLowerCase());
  }

  @Override
  public String toString() {
    return value;
  }
}
