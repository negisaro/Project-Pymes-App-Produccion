package com.nelson.project.msvc_usuario.msvc_usuario.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@TestConfiguration
public class TestMailConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername("alexandervinokuro3@gmail.com");
    mailSender.setPassword("AlexanderVinokuro/1970");
    // No necesitas properties avanzadas en test, pero puedes agregar si mockeas un servidor SMTP local
    return mailSender;
  }
}