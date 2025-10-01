# Auditoría Backend: msvc-usuario

Objetivo: Alinear el microservicio a un modelo profesional, funcional, moderno, escalable, robusto y conforme a Clean Code + principios SOLID.

## Leyenda
- [ ] Pendiente
- [~] En progreso / parcialmente aplicado
- [x] Completado (confirmado en código)
- (!) Riesgo / requiere decisión arquitectónica

---
## 1. Arquitectura & Organización
- [ ] 1.1. Consolidar estructura por capas (controller, service, repository, mapper, dto, security, config, exception) sin duplicaciones de paquetes profundos repetidos (`msvc_usuario.msvc_usuario`).
- [ ] 1.2. Revisar nombre de paquetes: reducir repetición (`com.nelson.project.msvc_usuario`).
- [ ] 1.3. Añadir diagrama de contexto y flujo de autenticación (README o docs/diagramas).
- [ ] 1.4. Introducir módulo de dominio (domain) si complejidad crece (agregar servicios de dominio puros sin dependencias infra).
- [ ] 1.5. Establecer convenciones para sufijos: *Controller, *Service, *Repository, *Mapper, *Config.
- [ ] 1.6. Crear carpeta `docs/` con arquitectura, decisiones (ADR) clave (JWT, recuperación contraseña, roles).

## 2. Dependencias y Build (pom.xml)
- [ ] 2.1. Validar versiones: Spring Boot 3.5.5 y Spring Cloud 2025.0.0 (ok) – documentar compatibilidad JDK 21.
- [ ] 2.2. Eliminar dependencias redundantes: `jakarta.servlet-api` (provided por Spring Boot), evaluar necesidad de `javax.annotation-api` (migrar a jakarta si aún usada).
- [ ] 2.3. Revisar uso de `angus-mail` + `jakarta.mail-api` (posible duplicidad; consolidar en Jakarta Mail oficial si es viable).
- [ ] 2.4. Centralizar versión de MapStruct en property `<mapstruct.version>` para futuras actualizaciones.
- [ ] 2.5. Añadir plugin de comprobación: `maven-enforcer-plugin` (reglas de versiones, Java, duplicados).
- [ ] 2.6. Añadir perfil `prod` para empaquetado con optimizaciones (remover dev-only props, activar layers Docker). 
- [ ] 2.7. Evaluar reemplazar librería JWT (jjwt) por `spring-security-oauth2-jose` si se requiere rotación de claves JWK en futuro.

## 3. Seguridad / JWT / Autenticación
- [x] 3.1. Externalizar expiración de token (`jwt.expiration-minutes`) (implementado: TokenJwtConfig @Value + definido en application-dev.yml y application-test.yml; application-prod.yml añadido con variable de entorno JWT_EXPIRATION_MINUTES).
- [x] 3.2. Asegurar rotación y longitud adecuada de clave (>=256 bits verificada; soporte múltiples secretos Base64 separados por coma para rotación básica; pendiente documentación README y pruebas específicas de verificación multi-clave).
- [~] 3.3. Añadir validaciones de audience / issuer configurables. (Implementado en generación y parseo; pendiente añadir pruebas unitarias específicas y documentación README.)
- [~] 3.4. Incluir refresh tokens robustos: expiración, revocación y rotación implementadas (entidad extendida con revoked/createdAt, servicio y endpoints actualizados). Pendiente: tests específicos (revocado, expirado, rotación doble) y limpieza programada.
- [x] 3.5. Mover `buildResponseBody` a AuthResponseDto + assembler (AuthResponseAssembler) eliminando métodos de construcción en JwtService y evitando uso de Map crudo en controladores / filtros.
- [ ] 3.6. Reducir logging de claims (nivel debug solo, no info en producción) y sanitizar datos sensibles.
- [ ] 3.7. Añadir pruebas unitarias para `JwtService` (validez, expiración, manipulación token).

## 4. Entidades / Modelo de Dominio
- [x] 4.1. Revisar herencia `AuditableEntity` (añadir @MappedSuperclass + campos createdAt/updatedAt con `@PrePersist/@PreUpdate`). (Implementado: clase base + @EnableJpaAuditing + listeners en entidades)
- [x] 4.2. Asegurar invariantes en métodos mutadores (por ej. `Usuario.updateEmail` validar formato centralizado). (Implementado validador dominio central + uso en métodos update*)
- [x] 4.3. Sustituir validaciones manuales dispersas por anotaciones Bean Validation en DTOs/entidades (`@NotBlank`, `@Email`, `@Size`). (Completado: DTOs alineados, @Valid en service, eliminación de validaciones redundantes.)
- [x] 4.4. Añadir índices DB (username, email únicos) mediante `@Table(indexes=...)` y constraints únicos. (Completado: anotaciones JPA + migración Flyway V1 con constraints e índices.)
- [x] 4.5. Revisar cascadas y fetch types explícitos en relaciones (EAGER vs LAZY) para evitar N+1. (Aplicado: @ManyToMany LAZY en Usuario y Rol)
- [x] 4.6. Introducir Value Objects (Email, Username) si se repiten validaciones. (Implementado Email & Username VO usados en métodos update*)

## 5. DTOs / Mappers
- [x] 5.1. Unificar patrón de nombres: UsuarioCreateDto / UsuarioDto / UsuarioUpdateDto coherentes (ya implementados).
- [x] 5.2. Añadir DTO específico para respuesta de autenticación (ver 3.5). (Integrado AuthResponseDto en login y check-token; pendiente limpieza de LoginResponseDto y mapper legacy.)
- [x] 5.3. MapStruct: `@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)` aplicado en UsuarioMapper, RolMapper y RefreshTokenMapper (eliminado `INSTANCE` estático). 
- [x] 5.4. Tests de mappers (añadidos casos: creación, toDto, actualización parcial, lista vacía, entrada nula). (Queda opcional agregar test de rolesIds vs roles gestionado en capa servicio.)

## 6. Servicios (Service Layer)
- [ ] 6.1. Eliminar duplicación de validaciones (extract `UsuarioValidator` / DomainService). 
- [ ] 6.2. Reemplazar múltiples `if` repetidos por guard clauses + Bean Validation en controlador.
- [ ] 6.3. Evitar `repository.findAll().stream().anyMatch(...)` para validar email único; usar query específica (exists / findByEmailAndIdNot).
- [ ] 6.4. `saveWithRoleUser`: renombrar a `registerClient` (intención clara) y mover lógica de rol default a configurador / domain policy.
- [ ] 6.5. Documentar transacciones: `@Transactional` en métodos públicos, no en privados; considerar modo readOnly consistente.
- [ ] 6.6. Añadir caching (Spring Cache) para lookups frecuentes (roles, usuario por username) si métricas lo justifican.
- [ ] 6.7. Introducir eventos de dominio (ApplicationEvent) para acciones post-registro (envío email, métricas).

## 7. Controladores (REST)
- [ ] 7.1. Asegurar consistencia en rutas (`/api/v1/users` plural / versión API).
- [ ] 7.2. Respuestas uniformes: envolver payload en objeto estándar (timestamp, data, errors, path).
- [ ] 7.3. Validar inputs con `@Valid` y eliminar validaciones manuales en servicio.
- [ ] 7.4. Manejo global de excepciones (ControllerAdvice) ya presente? (crear si falta: mapear CustomException a ResponseEntity).
- [ ] 7.5. Añadir rate limiting (Spring Cloud Gateway / bucket4j) si expuesto públicamente.
- [ ] 7.6. Generar documentación OpenAPI refinada (descripciones, schemas, ejemplos).

## 8. Excepciones / Errores
- [ ] 8.1. Reemplazar literales repetidos en mensajes por constantes o message codes i18n.
- [ ] 8.2. CustomException: revisar si conviene jerarquía (DomainException, ValidationException, NotFoundException).
- [ ] 8.3. Normalizar códigos ErrorCodes; documentar tabla en README.
- [ ] 8.4. Añadir correlación (traceId) en logs de errores (MDC + interceptor / filter).

## 9. Seguridad Adicional
- [ ] 9.1. Añadir Content Security Policy / headers duros (Spring Security config).
- [ ] 9.2. Forzar HTTPS (X-Forwarded-Proto) en prod detrás de proxy.
- [ ] 9.3. Revisar política CORS: restringir orígenes, métodos y headers.
- [ ] 9.4. Auditoría de passwords: políticas (longitud, complejidad) centralizadas.
- [ ] 9.5. Brute-force mitigation: contador intentos login + backoff.

## 10. Persistencia & Rendimiento
- [ ] 10.1. Añadir paginación consistente (no exponer findAll sin límites en controladores públicos).
- [ ] 10.2. Revisar necesidad de proyecciones para listas (evitar cargar relaciones pesadas).
- [ ] 10.3. Configurar métricas actuator + health para DB y readiness/liveness.
- [ ] 10.4. Añadir migraciones con Flyway/Liquibase (versionado de esquema).
- [ ] 10.5. Pool conexiones: verificar configuración Hikari (timeouts, tamaños, métricas).

## 11. Observabilidad / Logging
- [ ] 11.1. Añadir `spring-boot-starter-logging` config avanzada (patrones JSON opcionales para centralización).
- [ ] 11.2. MDC: incluir usuario autenticado y requestId en logs.
- [ ] 11.3. Exponer métricas personalizadas (usuarios activos, registros por hora) vía Micrometer.
- [ ] 11.4. Trazas distribuidas: evaluar OpenTelemetry exporter si hay más microservicios.

## 12. Calidad de Código / SOLID
- [ ] 12.1. SRP: extraer validaciones de UsuarioServiceImpl a clases dedicadas.
- [ ] 12.2. OCP: permitir nuevas reglas de negocio (roles, políticas) con estrategia en lugar de `if` directos.
- [ ] 12.3. LSP: revisar que interfaces no tengan métodos no usados por algunas implementaciones (actual OK).
- [ ] 12.4. ISP: separar interfaces si crecen demasiado (UsuarioService todavía aceptable pero vigilar).
- [ ] 12.5. DIP: inyectar interfaces externas (EmailService) a través de puertos (hexagonal) si se planea múltiples proveedores.

## 13. Testing
- [ ] 13.1. Añadir pruebas unitarias para servicios (UsuarioService, JwtService, PasswordRecoveryService).
- [ ] 13.2. Añadir pruebas de integración con H2 para repositorios (usuarioRepository, rolRepository) con datos de test.
- [ ] 13.3. Añadir test de seguridad: acceso denegado a endpoints sin token / con rol incorrecto.
- [ ] 13.4. Test de OpenAPI contract (springdoc) si se expone a terceros.
- [ ] 13.5. Cobertura mínima objetivo: 70% líneas / 80% critical paths.

## 14. Email / Notificaciones
- [ ] 14.1. Unificar proveedor: preferir Jakarta Mail estándar (eliminar duplicidades con angus-mail si innecesario).
- [ ] 14.2. Externalizar plantillas (freemarker / thymeleaf / plain mustache) y evitar strings incrustados.
- [ ] 14.3. Añadir cola asíncrona (RabbitMQ / Kafka / simple @Async) para envío de email no bloqueante.

## 15. Password Recovery / Tokens
- [ ] 15.1. Verificar expiración y uso único de PasswordResetToken.
- [ ] 15.2. Limitar intentos de solicitud de token por intervalo (throttling).
- [ ] 15.3. Invalidar tokens viejos del mismo usuario automáticamente.
- [ ] 15.4. Cifrar/Hash del token antes de guardar (evitar guardar token plano).

## 16. Refresh Tokens
- [ ] 16.1. Definir tiempo de expiración configurable (propiedad). 
- [ ] 16.2. Añadir columna revoked/expired y endpoint para revocación manual.
- [ ] 16.3. Limpiar tokens expirados con tarea programada (Spring Scheduling / Quartz).

## 17. Configuración / Profiles
- [ ] 17.1. Revisar duplicación de `application*.yml`; factorizar valores comunes en `application.yml`.
- [ ] 17.2. Incluir profile `test` con properties mínimas y DB H2.
- [ ] 17.3. Añadir verificación de configuración faltante al arrancar (Binder + @ConfigurationProperties + validation).

## 18. Docker / Despliegue
- [ ] 18.1. Optimizar Dockerfile (multi-stage, distroless / alpine, usar layers reproducibles).
- [ ] 18.2. Añadir variables de entorno para secretos (no en imagen) + documentación.
- [ ] 18.3. Healthcheck en Docker (curl actuator /health). 
- [ ] 18.4. Preparar K8s manifests / Helm chart si orquestación planeada.

## 19. Resiliencia / Comunicación Interservicios
- [ ] 19.1. Añadir timeouts y retries (resilience4j / Spring Retry) a Feign clients.
- [ ] 19.2. Circuit breaker y fallback para dependencias críticas.
- [ ] 19.3. Implementar idempotencia en operaciones sensibles (registro, password reset) si se repiten.

## 20. Performance / Hardening
- [ ] 20.1. Activar gzip / compression (Spring config) si aplica.
- [ ] 20.2. Limitar tamaño de payloads (multipart / JSON) con properties.
- [ ] 20.3. Revisión de logs excesivos (evitar info en loops). 
- [ ] 20.4. Activar `record` DTOs (Java 21) para inmutabilidad y menor boilerplate donde sea viable.
- [ ] 20.5. Revisar uso de `Optional` que lanzan excepción inmediatamente (simplificar a `orElseThrow`).

---
## Prioridad Recomendada (Primer Sprint Refactor)
1) 2.2 / 2.3 / 2.4 / 3.1 / 3.5 / 6.3 / 7.3 / 4.3 / 10.4
2) 15.x / 16.x (seguridad tokens) + 19.1 resiliencia Feign
3) 11.x observabilidad + 18.x despliegue optimizado
4) 12.x SOLID y 6.x limpieza lógica dominio
5) 13.x pruebas para consolidar confianza

## Métrica de Avance
- Total ítems: 100+
- Definir meta sprint: cerrar al menos 15 ítems críticos (prioridad lista arriba).

---
## Notas Iniciales
Hallazgos clave:
- Validaciones manuales duplicadas en UsuarioServiceImpl (riesgo de divergencia). 
- Mensajes de error repetidos; conviene internacionalización o centralización.
- Gestión de tokens (refresh/password reset) puede fortalecerse (revocación / hashing / expiración configurable).
- Dependencias de mail posiblemente redundantes.
- Falta de migraciones de esquema y constraints explícitos.
- Uso de `Optional` junto con `or(() -> { throw ... })` reduce claridad: preferir `orElseThrow`.

Mantener este checklist actualizado marcando avances y añadiendo decisiones (ADR) conforme se implementen cambios.
