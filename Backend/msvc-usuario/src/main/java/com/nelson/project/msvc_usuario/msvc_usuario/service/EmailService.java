package com.nelson.project.msvc_usuario.msvc_usuario.service;

public interface EmailService {
  void sendPasswordResetToken(String to, String token, int expiryMinutes);
}
