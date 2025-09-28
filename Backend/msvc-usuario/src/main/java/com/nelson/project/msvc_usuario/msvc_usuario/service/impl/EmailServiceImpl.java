package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendPasswordResetToken(
    String to,
    String token,
    int expiryMinutes
  ) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject("Recuperación de contraseña");
      message.setText(
        "Para restablecer tu contraseña, usa este token: " +
        token +
        "\nEste token expirará en " +
        expiryMinutes +
        " minutos."
      );
      mailSender.send(message);
      log.info("[EmailService] Email de recuperación enviado a {}", to);
    } catch (Exception e) {
      log.error(
        "[EmailService] Error enviando email de recuperación a {}: {}",
        to,
        e.getMessage(),
        e
      );
      throw new RuntimeException(
        "No se pudo enviar el email de recuperación. Intenta más tarde."
      );
    }
  }
}
