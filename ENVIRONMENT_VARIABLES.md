# Environment Variables – Project PYMES

> Estado: ACTIVO (base unificada). Este documento describe las variables de entorno usadas por los microservicios y el frontend. Mantenerlo sincronizado ante cambios.

## Tabla Resumen

| Variable | Obligatoria | Default (si aplica) | Usada en | Tipo | Rotación | Notas |
|----------|------------|----------------------|----------|------|----------|-------|
| SPRING_PROFILES_ACTIVE | Sí | dev | Todos (Spring) | Config | No | Define perfil de configuración. |
| EUREKA_SERVER_URL | Sí | — | Todos (clientes Eureka) | Infra | No | URL del servidor de descubrimiento. |
| EUREKA_DASHBOARD_PASSWORD | Opcional | — | eureka-server | Credencial | Recomendada | Proteger dashboard. |
| GATEWAY_JWT_SECRET | Sí | — | Gateway + microservicios | Secreto | 90 días | Base64 >= 32 bytes decodificados. |
| JWT_EXPIRATION_MINUTES | Opcional | 60 | Servicios JWT | Config | Según política | Ajusta duración access token. |
| JWT_ISSUER | Opcional | project-pymes | Servicios JWT | Config | Cambia solo ante refactor dominio | Coincide con claim `iss`. |
| JWT_AUDIENCE | Opcional | project-pymes-clients | Servicios JWT | Config | Cambia solo si cambia cliente | Claim `aud`. |
| REDIS_HOST | Sí (si Redis usado) | redis | Servicios que cachean | Infra | No | Nombre de servicio interno docker. |
| REDIS_PORT | Sí | 6379 | Servicios Redis | Infra | No | Puerto estándar. |
| MYSQL_ROOT_PASSWORD | Sí (en contenedor DB) | — | db | Secreto | 180 días | Rotar y NO reutilizar en otros entornos. |
| MYSQL_DATABASE | Sí | — | Todos (datasource) | Config | No | Nombre BD principal compartida. |
| MYSQL_USER | Sí | — | Todos | Credencial | 180 días | Usuario app. Separar usuarios por servicio en futuro. |
| MYSQL_PASSWORD | Sí | — | Todos | Secreto | 180 días | Rotar con root. |
| MYSQL_HOST | Sí | db | Microservicios | Infra | No | Overriden en compose -> `db`. |
| MYSQL_PORT | Opcional | 3306 | Microservicios | Infra | No | Enlace interno. Externo mapeado a 3307. |
| MAIL_HOST | Sí (si email) | smtp.gmail.com | Servicios que envían correo | Config | No | Ajustar según proveedor. |
| MAIL_PORT | Sí | 587 | Email | Config | No | STARTTLS. |
| MAIL_USERNAME | Sí | — | Email | Credencial | 180 días | Cuenta/app user. |
| MAIL_PASSWORD | Sí | — | Email | Secreto | 180 días | App password. No commitear real en prod. |
| MAIL_DEBUG | Opcional | false | Email | Config | No | Activar solo en dev. |
| PASSWORD_RESET_EXPIRATION_MINUTES | Opcional | 30 | Usuario | Config | No | Vida de token de reset. |
| PASSWORD_RESET_MAX_ACTIVE_TOKENS | Opcional | 3 | Usuario | Config | No | Limita abuso. |
| FRONTEND_URL | Opcional | http://localhost:4200 | CORS / Links | Config | No | Cambiar en despliegue prod. |
| LOG_LEVEL (propuesto) | Opcional | INFO | Todos | Config | No | Añadir en futuro para tuning. |
| TRACING_ENABLED (propuesto) | Opcional | false | Observabilidad | Config | No | Para APM / OpenTelemetry. |
| DB_POOL_SIZE (propuesto) | Opcional | 10 | Datasource | Config | No | Ajustar según carga. |

## Política de Rotación
- GATEWAY_JWT_SECRET: cada 90 días o incidente. Proceso: generar nueva -> desplegar en paralelo (multi-clave si se habilita) -> revocar anterior.
- Credenciales DB (root y app): cada 180 días. A futuro: usuarios dedicados por microservicio (principio de privilegio mínimo).
- MAIL_PASSWORD: cada 180 días o al perderse control de la cuenta.

## Buenas Prácticas
1. Separar `.env` (desarrollo) de variables de producción (usar gestor de secretos: Vault, AWS Secrets Manager, Azure Key Vault, etc.).
2. No reutilizar la misma clave JWT entre entornos. Prefijo de tagging en gestor: `pymes-{env}-jwt-YYYYMMDD`.
3. Evitar añadir nuevas variables sin actualizar este documento.
4. Para valores opcionales con fallback en YAML no declarar si se usa el default (limpia repositorio). Documentar aquí igualmente.
5. En producción usar configuración inmutable (docker secrets, envs cifrados o inyección CI/CD). No montar `.env` plano.

## Ejemplo de Rotación de JWT
```bash
# Generar nueva clave segura (Linux/macOS)
openssl rand -base64 48 > new_jwt.txt
# Export temporal (shell) y probar en entorno staging antes de activar en prod
export GATEWAY_JWT_SECRET=$(cat new_jwt.txt)
```

## Futuras Mejoras
- Introducir soporte multi-secreto (clave activa + clave previa) para rotaciones sin invalidar sesiones inmediatamente.
- Añadir variables de tracing (OTEL_EXPORTER_OTLP_ENDPOINT, SERVICE_NAMESPACE, etc.) cuando se integre observabilidad.
- Definir `SPRING_PROFILES_ACTIVE` por servicio si se introducen perfiles especializados (ej. `msvc-usuario=dev,mail-debug`).

---
Última actualización: 2025-09-30
Responsable: (añadir responsable de plataforma / DevOps)
