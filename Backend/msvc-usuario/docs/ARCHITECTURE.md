# Arquitectura msvc-usuario

## 1. Resumen
Microservicio responsable de la gestión de usuarios, roles, autenticación JWT, refresh tokens y recuperación de contraseña.
Se construye con Spring Boot 3.x (Java 21), Spring Security, Spring Data JPA, OpenFeign, Eureka Client.

## 2. Objetivos Arquitectónicos
- Alineación a principios SOLID y Clean Code.
- Facilidad de evolución (nuevos flujos de autenticación, MFA futuro).
- Observabilidad y resiliencia en un entorno de microservicios.
- Seguridad fuerte para operaciones de identidad.

## 3. Capas (Vista Lógica)
```
Controller (entrada HTTP / DTO)  -->  Service (casos de uso / orquestación)  -->  Domain (Reglas de negocio puras)  -->  Repository (Persistencia)  
                                              |                                    
                                       Integrations (Feign, Email)                 
```
Estado actual: falta separar explícitamente una capa `domain`/`validator` para encapsular reglas (hoy se concentran en `UsuarioServiceImpl`).

## 4. Paquetes (Estado Actual vs Objetivo)
Actual:
```
com.nelson.project.msvc_usuario.msvc_usuario.
  controller
  service (impl, password)
  security (filter, service)
  model (entity, dto)
  mapper
  repository
  exception
  util
  config
```
Problema: Duplicación `msvc_usuario.msvc_usuario` en el namespace.
Objetivo:
```
com.nelson.project.usuario
  api        (controllers + dto request/response)
  domain     (entidades, value objects, servicios dominio puros)
  application (casos de uso / orquestación: UserApplicationService)
  infrastructure
     persistence (repositories JPA + mappers infra)
     security
     config
     email
  shared (exceptions base, eventos, utilidades transversales)
```
Refactor gradual (ver Plan de Refactor de Paquetes).

## 5. Flujo de Autenticación (Simplificado)
1. Usuario envía credenciales a endpoint de login.
2. Spring Security autentica (UserDetailsService -> DB).
3. `JwtService` genera token (subject = username, claims roles/email).
4. Se retorna body JSON (token + datos usuario + roles).
5. Para refresh: se valida refresh token almacenado y se genera nuevo JWT.

## 6. Recuperación de Contraseña
1. Usuario solicita reset -> se genera `PasswordResetToken` (persistido) y se envía correo con enlace.
2. Usuario invoca endpoint con token y nueva contraseña.
3. Servicio valida token (expiración / uso único) y actualiza password (hash con PasswordEncoder).
4. Token se marca consumido / se elimina (pendiente reforzar hashing token y revocación previa).

## 7. Decisiones Arquitectónicas Relevantes (ADR)
- ADR-0001 (pendiente): Normalizar paquete base eliminando duplicación.
- ADR-0002 (futuro): Estrategia de tokens refresh (revocación y rotación).
- ADR-0003 (futuro): Introducción de migraciones de esquema (Flyway) + constraints.

## 8. Seguridad JWT
- Clave extraída desde `TokenJwtConfig` (revisar robustez / longitud).
- Expiración hardcodeada (60 min) -> mover a `application.yml` (`jwt.expiration-minutes`).
- Falta issuer / audience configurable.
- Respuesta actual: Map (migrar a DTO tipado `AuthResponseDto`).

## 9. Observabilidad
- Actuator presente.
- Pendiente: métricas personalizadas, trazas distribuidas (OpenTelemetry), MDC con `traceId` y `user`.

## 10. Resiliencia
- Falta configuración de timeouts, retries y circuit breakers para Feign.
- Sin políticas de rate limiting / brute force login.

## 11. Persistencia
- Falta esquema versionado (Flyway/Liquibase).
- Verificar índices únicos email/username en DB física.
- Oportunidad: proyecciones o DTO query para listados grandes.

## 12. Evolución Propuesta (Hoja de Ruta Alta)
Sprint 1:
- Externalizar expiración JWT + AuthResponseDto + Validación Bean Validation DTOs.
- Refactor email / username uniqueness (queries específicas) + remover validaciones manuales triviales.
- Añadir migraciones iniciales (Flyway) + constraints.

Sprint 2:
- Introducir RefreshToken robusto (revoked / expiración) + hashing PasswordResetToken.
- Timeouts + retries Feign + circuit breaker.

Sprint 3:
- Observabilidad (MDC, métricas, tracing) + documentación ADR actualizada.

Sprint 4:
- Refactor empaquetado a arquitectura hexagonal (application/domain/infrastructure) gradualmente.

## 13. Riesgos Identificados
| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| Validaciones dispersas | Inconsistencias | Centralizar en validators + Bean Validation |
| Tokens sin revocación | Sesiones prolongadas | Implementar tabla / campo revoked + expiración configurable |
| Dependencias mail duplicadas | Complejidad / conflictos | Consolidar en Jakarta Mail estándar |
| Falta de migraciones | Drift en entornos | Adoptar Flyway y pipeline CI para aplicar scripts |
| Logging sensible claims | Exposición datos | Reducir a DEBUG y sanitizar |

## 14. Métricas de Calidad Iniciales (Targets)
- Cobertura >70% servicios críticos (auth, usuario) en 2 sprints.
- P99 login < 300ms (tras habilitar métricas).
- 0 code smells críticos (Sonar) tras Sprint 2.

## 15. Glosario
- DTO: Data Transfer Object.
- ADR: Architecture Decision Record.
- Domain Service: Servicio con lógica de negocio pura sin dependencias infra.

---
Actualizar este documento a medida que se vayan marcando ítems del checklist principal.
