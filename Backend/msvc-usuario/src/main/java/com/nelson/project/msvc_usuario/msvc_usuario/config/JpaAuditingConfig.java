package com.nelson.project.msvc_usuario.msvc_usuario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración central para habilitar auditoría JPA (@CreatedDate, @LastModifiedDate).
 * Checklist 4.1: consolida soporte de auditoría en una sola clase.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {}
