package com.nelson.project.msvc_usuario.msvc_usuario.service.password;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import org.springframework.stereotype.Component;

@Component
public class BasicPasswordPolicy implements PasswordPolicy {

  @Override
  public void validate(String rawPassword) {
    if (rawPassword == null || rawPassword.trim().length() < 8) {
      throw new CustomException(
        "La contraseña no cumple con los requisitos mínimos (>= 8 caracteres)",
        422,
        ErrorCodes.PASSWORD_POLICY_VIOLATION
      );
    }
  }
}
