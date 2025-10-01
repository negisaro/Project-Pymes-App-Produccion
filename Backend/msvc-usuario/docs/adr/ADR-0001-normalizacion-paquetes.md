# ADR-0001: Normalización de Paquetes Base

## Contexto
El código actual usa un namespace repetido: `com.nelson.project.msvc_usuario.msvc_usuario`. Esto genera rutas largas, ruido y dificulta la futura reorganización hacia un modelo hexagonal (domain / application / infrastructure). Mantener paquetes profundos y redundantes complica el refactor incremental.

## Decisión
Adoptar el paquete base: `com.nelson.project.usuario` para el microservicio, y reorganizar en sub-paquetes según responsabilidad.

Nueva estructura objetivo:
```
com.nelson.project.usuario
  api (controllers, dto request/response)
  application (servicios de orquestación / casos de uso)
  domain (entidades, value objects, domain services)
  infrastructure
     persistence (repositories JPA, mappers específicos infra)
     security
     config
     email
  shared (exceptions, utilidades, eventos)
```
Refactor gradual en ramas cortas:
1. Crear nuevos paquetes vacíos + mover una entidad y su mapper (POC). 
2. Mover servicios y actualizar imports (Usuario -> dominio y application).
3. Mover controladores al paquete `api`.
4. Ajustar `@ComponentScan` si fuera necesario (Spring Boot suele detectar por raíz).

## Alternativas Consideradas
- Dejar estructura actual y solo documentar: Rechazado (no reduce deuda futura).
- Adoptar prefijo `identity` en lugar de `usuario`: Posible futuro renombre si se agregan funcionalidades cross-cutting (MFA, sessions) pero no prioritario ahora.

## Consecuencias
Positivo:
- Claridad y menor fricción cognitiva.
- Facilita separación de capas / pruebas unitarias.
- Sienta base para arquitectura hexagonal.
Negativo:
- Refactor de imports amplio; riesgo de conflictos en ramas paralelas.

## Métricas de Aceptación
- Todos los paquetes compilando tras mover entidades centrales (Usuario, Rol) y servicios asociados.
- Tests (cuando existan) sin roturas.
- Checklist de elementos movidos actualizado.

## Estado
Propuesto (pendiente ejecución). 

## Próximo Paso
Implementar POC moviendo `Rol` y `Usuario` + `UsuarioRepository` y validar.
