package com.nelson.project.msvc_usuario.msvc_usuario.model.valueobject;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para encapsular reglas de Username.
 */
public final class Username implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Pattern USERNAME_REGEX = Pattern.compile(
    "^[a-zA-Z0-9_.-]{4,20}$"
  );

  private final String value;

  private Username(String value) {
    this.value = value;
  }

  public static Username of(String raw) {
    if (raw == null) {
      throw new IllegalArgumentException("Username no puede ser null");
    }
    String cleaned = raw.trim();
    if (cleaned.isEmpty()) {
      throw new IllegalArgumentException("Username no puede ser vacío");
    }
    if (!USERNAME_REGEX.matcher(cleaned).matches()) {
      throw new IllegalArgumentException(
        "Username inválido (4-20 alfanumérico y . _ -)"
      );
    }
    return new Username(cleaned);
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Username)) return false;
    Username that = (Username) o;
    return value.equalsIgnoreCase(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value.toLowerCase(Locale.ROOT));
  }

  @Override
  public String toString() {
    return value;
  }
}
