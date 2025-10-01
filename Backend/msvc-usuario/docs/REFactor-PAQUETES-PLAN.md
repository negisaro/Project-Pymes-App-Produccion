# Plan de Refactor de Paquetes

## Objetivo
Eliminar duplicación `msvc_usuario.msvc_usuario` y preparar la transición a una arquitectura por capas claras (api, application, domain, infrastructure, shared).

## Estrategia
Refactor incremental y seguro en 6 pasos cortos para minimizar riesgo.

## Pasos
1. Preparación
   - [ ] Crear paquete raíz alterno `com.nelson.project.usuario.domain` y mover `Rol`, `Usuario`.
   - [ ] Ajustar imports en repositorios y servicios que referencian estas entidades.
2. Repositorios
   - [ ] Mover `UsuarioRepository`, `RolRepository` a `infrastructure.persistence`.
   - [ ] Verificar `@EnableJpaRepositories` no es necesario (Spring Boot autoconfig). Si se usa, apuntar base package nuevo.
3. Servicios
   - [ ] Crear `application` package; mover `UsuarioService` + impl (renombrar a `UsuarioApplicationService`).
   - [ ] Extraer validaciones a `domain` (e.g. `UsuarioValidator`).
4. Controladores
   - [ ] Mover controladores a `api` y renombrar endpoints a `/api/v1/...` si aplica.
5. Seguridad / Infraestructura
   - [ ] Mover `JwtService`, filtros, configs a `infrastructure.security`.
   - [ ] Mover mappers a `infrastructure.persistence.mapper` o mantener `shared.mapper` si son puros.
6. Limpieza Final
   - [ ] Eliminar antiguos paquetes vacíos.
   - [ ] Actualizar documentación (ARCHITECTURE.md + ADR-0001 -> estado "Aceptado").

## Validación Post-Refactor
- Compilación verde.
- Endpoints responden (smoke test login / registro / list users).
- Sin duplicación de clases en dos rutas.

## Riesgos y Mitigaciones
| Riesgo | Mitigación |
|--------|-----------|
| Conflicto en merges | Crear rama dedicada y fusionar rápido |
| Imports rotos | Uso de IDE refactor + build continuo |
| Pérdida de cobertura (futura) | Añadir tests antes de mover piezas clave |

## Criterio de Hecho
- Estructura nueva adoptada con al menos entidades y servicios principales migrados.
- Documentación actualizada.
- Checklist de auditoría marca 1.1 y 1.2 como completadas.
