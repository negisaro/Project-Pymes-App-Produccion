package com.nelson.project.msvc_usuario.msvc_usuario.service;

public interface PasswordRecoveryService {
  void requestReset(String email);
  void validateTokenOrThrow(String token);
  void resetPassword(String token, String newPassword);
}
