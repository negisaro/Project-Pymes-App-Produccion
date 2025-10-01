# System Overview

## Visión General
Plataforma e-commerce modular orientada a PYMES compuesta por:
- Microservicio `msvc-usuario`: autenticación, usuarios, roles, emisión y rotación de tokens JWT + refresh tokens.
- Frontend Angular `ecommerce-web`: tienda pública, panel administrativo y dashboards.

## Objetivos Arquitectónicos
- Seguridad sólida (JWT con rotación, issuer/audience, refresh robusto)
- Modularidad y escalabilidad progresiva (separación futura de más microservicios: catálogo, pedidos, pagos)
- Clean Code + SOLID + separación de capas
- Experiencia mantenible (documentación viva, ADRs, checklist de auditoría)

## Dominios Iniciales
| Dominio | Responsabilidad | Estado |
|---------|-----------------|--------|
| Identidad & Seguridad | Usuarios, roles, login, refresh | Activo (msvc-usuario) |
| Catálogo | Productos, categorías, proveedores | Parcial (frontend stub) |
| Clientes | Panel cliente, pedidos futuros | Parcial |
| Métricas | KPI ventas, gráficos | En progreso (dashboard admin) |

## Flujos Críticos
### Autenticación
```
Login -> AuthenticationManager -> JwtService (access) + RefreshTokenService -> AuthResponseAssembler -> Frontend
Refresh -> Validate refresh -> Rotate -> New access + refresh
```

### Navegación Frontend
```
AppComponent
  ├─ MainLayout (público)
  ├─ AuthLayout (login/registro)
  └─ AdminLayout (sidebar + topbar)
         ├─ AdminDashboardModule (lazy)
         └─ ClienteDashboardModule (lazy)
```

## Estrategia de Evolución
1. Consolidar calidad y seguridad en usuario (completar tests pendientes)
2. Separar futuro `msvc-catalogo` (productos/categorías) cuando reglas crezcan
3. Añadir observabilidad (métricas, tracing) y endurecer cabeceras
4. Formalizar contratos (OpenAPI + generación de clientes)

## Principios Adoptados
- Única responsabilidad por módulo (core, shared, feature)
- Evitar dependencias circulares (shared nunca importa feature)
- DTOs explícitos, sin exponer entidades en capas externas
- Claves JWT rotables y validadas por longitud

## Glosario
- **Assembler**: Componente que construye DTOs compuestos (ej: AuthResponseAssembler)
- **ADR**: Architectural Decision Record
- **Checklist**: Lista viva de tareas de refactor / hardening
