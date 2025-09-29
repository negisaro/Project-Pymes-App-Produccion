package com.nelson.project.msvc_usuario.msvc_usuario.config.configmail;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${spring.mail.host:}")
  private String mailHost;

  @Value("${spring.mail.port:}")
  private String mailPort;

  @Value("${spring.mail.username:}")
  private String mailUser;

  @Value("${spring.mail.password:}")
  private String mailPassword;

  @Value("${app.mail.debug:false}")
  private boolean mailDebug;

  @Bean
  public JavaMailSender javaMailSender() {
    // Validación básica de configuración
    if (mailHost == null || mailHost.isBlank()) {
      throw new CustomException(
        "Error de configuración de email: spring.mail.host no está configurado",
        500,
        "MAIL_CONFIG_ERROR",
        "Falta la propiedad spring.mail.host en el entorno o archivo de configuración"
      );
    }
    if (mailPort == null || mailPort.isBlank()) {
      throw new CustomException(
        "Error de configuración de email: spring.mail.port no está configurado",
        500,
        "MAIL_CONFIG_ERROR",
        "Falta la propiedad spring.mail.port en el entorno o archivo de configuración"
      );
    }
    if (mailUser == null || mailUser.isBlank()) {
      throw new CustomException(
        "Error de configuración de email: spring.mail.username no está configurado",
        500,
        "MAIL_CONFIG_ERROR",
        "Falta la propiedad spring.mail.username en el entorno o archivo de configuración"
      );
    }
    if (mailPassword == null || mailPassword.isBlank()) {
      throw new CustomException(
        "Error de configuración de email: spring.mail.password no está configurado",
        500,
        "MAIL_CONFIG_ERROR",
        "Falta la propiedad spring.mail.password en el entorno o archivo de configuración"
      );
    }
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(Integer.parseInt(mailPort));
    mailSender.setUsername(mailUser);
    mailSender.setPassword(mailPassword);
    mailSender.setDefaultEncoding("UTF-8");
    // Properties avanzadas SMTP
    java.util.Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.starttls.required", "true");
    props.put("mail.smtp.ssl.trust", mailHost);
    props.put("mail.transport.protocol", "smtp");
    // Timeouts (ms)
    props.put("mail.smtp.connectiontimeout", "5000");
    props.put("mail.smtp.timeout", "5000");
    props.put("mail.smtp.writetimeout", "5000");
    if (mailDebug) {
      props.put("mail.debug", "true");
    }
    return mailSender;
  }
}
