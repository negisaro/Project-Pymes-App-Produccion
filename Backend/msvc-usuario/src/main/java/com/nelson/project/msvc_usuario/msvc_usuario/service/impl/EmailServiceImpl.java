package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Value("${spring.mail.host}")
  private String mailHost;

  @Value("${spring.mail.port}")
  private String mailPort;

  @Value("${spring.mail.username}")
  private String mailUsername;

  // No loggear nunca el password; sólo se inyecta en el JavaMailSender
  @Value("${spring.mail.password:}")
  private String mailPassword; // NO USAR EN LOGS

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendPasswordResetToken(
    String to,
    String token,
    int expiryMinutes
  ) {
    if (log.isDebugEnabled()) {
      log.debug(
        "[EmailService] SMTP configurado host={}, port={}, username={}",
        mailHost,
        mailPort,
        mailUsername
      );
    }
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject("Recuperación de contraseña");

      // Cambia aquí el path para incluir /auth/
      String appUrl = frontendUrl + "/auth/reset-password?token=" + token;

      // Email solo con el link (opcional: puedes dejar el token aparte si deseas)
      message.setText(
        "Hola,\n\n" +
        "Hemos recibido una solicitud para restablecer tu contraseña.\n" +
        "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" +
        appUrl +
        "\n\n" +
        "Este enlace expirará en " + expiryMinutes + " minutos.\n\n" +
        "Si tú no solicitaste este cambio, ignora este mensaje.\n\n" +
        "Saludos,\nEquipo de soporte."
      );
      log.info("[EmailService] Enviando email de recuperación a {}", to);
      mailSender.send(message);
      log.info("[EmailService] Email de recuperación enviado a {}", to);
    } catch (org.springframework.mail.MailAuthenticationException authEx) {
      log.error(
        "[EmailService] Falló autenticación SMTP al enviar a {}: {}",
        to,
        authEx.getMessage()
      );
      throw new CustomException(
        "No se pudo autenticar contra el servidor de correo. Verifica credenciales SMTP/App Password.",
        500,
        "EMAIL_AUTH_ERROR",
        authEx.getMessage()
      );
    } catch (Exception e) {
      log.error(
        "[EmailService] Error genérico enviando email de recuperación a {}",
        to,
        e
      );
      throw new CustomException(
        "No se pudo enviar el email de recuperación. Intenta más tarde.",
        500,
        "EMAIL_SEND_ERROR",
        e.getMessage()
      );
    }
  }
}