package com.nelson.project.msvc_usuario.msvc_usuario.model.domain;

import java.util.regex.Pattern;

/**
 * Validador de reglas de dominio para Usuario.
 * Centraliza invariantes para evitar duplicación en servicios.
 * Checklist 4.2 (invariantes en métodos mutadores) & prepara futura introducción de Value Objects.
 */
public final class UsuarioDomainValidator {

  private static final Pattern EMAIL_REGEX = Pattern.compile(
    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
  );
  private static final Pattern USERNAME_REGEX = Pattern.compile(
    "^[a-zA-Z0-9_.-]{4,20}$"
  );

  private UsuarioDomainValidator() {}

  public static void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email no puede ser vacío");
    }
    if (!EMAIL_REGEX.matcher(email).matches()) {
      throw new IllegalArgumentException("Formato de email inválido");
    }
  }

  public static void validateUsername(String username) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username no puede ser vacío");
    }
    if (!USERNAME_REGEX.matcher(username).matches()) {
      throw new IllegalArgumentException(
        "Username inválido (4-20 chars alfanumérico . _ -)"
      );
    }
  }

  public static void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Nombre no puede ser vacío");
    }
  }

  public static void validateLastname(String lastname) {
    if (lastname == null || lastname.isBlank()) {
      throw new IllegalArgumentException("Apellido no puede ser vacío");
    }
  }

  public static void validatePassword(String password) {
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password no puede ser vacío");
    }
    if (password.length() < 8) {
      throw new IllegalArgumentException("Password mínimo 8 caracteres");
    }
  }
}
