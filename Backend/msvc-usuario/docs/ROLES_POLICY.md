# Política de Roles y Registro de Usuarios

Estado: ACTIVO  
Última actualización: 2025-09-30

## 1. Objetivo
Definir claramente los roles base del sistema y el comportamiento del método de registro público (`saveWithRoleUser`) sin alterar la implementación existente.

## 2. Roles Base
Los roles iniciales se crean automáticamente en el bootstrap (`InitBootstrap`):

| Rol | Propósito | Creación Automática | Comentarios |
|-----|-----------|---------------------|-------------|
| ROLE_ADMIN | Administración global (gestión de usuarios, roles, configuraciones). | Sí | Se crea usuario admin por defecto si no existe. |
| ROLE_USER | Rol genérico de autenticación (reservado para futuras ampliaciones). | Sí | Actualmente no asignado en registro público. |
| ROLE_CLIENT | Rol asignado a usuarios finales (clientes tienda / consumidores API). | Sí | Asignado en registro público. |

## 3. Registro Público
El método `UsuarioServiceImpl#saveWithRoleUser` asigna actualmente el rol `ROLE_CLIENT` (naming legacy).  
Decisión: NO refactorizar el nombre del método para preservar compatibilidad y simplicidad en esta fase.  

### Comportamiento actual
1. Valida unicidad de `username`.
2. Construye entidad `Usuario` con datos básicos.
3. Recupera o crea `ROLE_CLIENT`.
4. Asigna `ROLE_CLIENT` como único rol.
5. Persiste y retorna DTO.

### Justificación de mantener nombre
- Posibles integraciones externas ya apuntan a la operación.
- Refactor implicaría documentar breaking change en contratos internos.
- Bajo impacto práctico mantener alias semántico (USER=CLIENT en contexto público).

## 4. Bootstrap de Roles y Usuario Admin
Archivo: `InitBootstrap`.

Acciones:
- Crea roles: `ROLE_ADMIN`, `ROLE_USER`, `ROLE_CLIENT` si faltan.
- Crea usuario `admin` (contraseña inicial: `admin123` – debe cambiarse en entornos reales) si no hay usuarios con `ROLE_ADMIN`.

## 5. Convenciones Decididas
| Área | Convención | Estado |
|------|------------|--------|
| Naming método registro | Mantener `saveWithRoleUser` | DECIDIDO |
| Rol público asignado | `ROLE_CLIENT` | ACTIVO |
| Rol genérico `ROLE_USER` | Reservado para expansion (ej. features comunes) | PENDIENTE USO |
| Constantes de roles | Literales inline (sin clase de constantes) | A MEJORAR FUTURO |

## 6. Riesgos y Mitigaciones
| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| Confusión por naming `saveWithRoleUser` vs asignar `ROLE_CLIENT` | Medio (lectura de código) | Documentado aquí; aclarar en onboarding. |
| Uso accidental de `ROLE_USER` no gestionado | Bajo | Definir proceso de activación cuando se use. |
| Contraseña admin por defecto en entornos de prueba se filtra a prod | Alto | Exigir override vía variable de entorno / script de despliegue. |

## 7. Próximas Mejoras (Opcional)
1. Introducir `Roles` constants class o enum.
2. Exponer endpoint para listar roles activos y su asignación.
3. Añadir política de escalamiento: convertir usuarios `CLIENT` a otros roles bajo aprobación admin.
4. Registrar auditoría de creación de usuarios (tabla/log) si compliance lo requiere.

## 8. Checklist de Integridad
- [x] Roles iniciales creados automáticamente.
- [x] Registro público crea sólo `ROLE_CLIENT`.
- [x] Usuario admin se autogenera si no existe uno.
- [x] Documentación de naming legado.

---
Responsable de mantenimiento: Equipo Backend / Seguridad.
