# Frontend Audit Checklist (Actualizado: 2025-09-29)

Leyenda: [ ] pendiente | [~] parcial | [x] completo | (!) decisión

## 1. Estructura & Arquitectura
- [x] 1.1. Unificar layout admin (el duplicado ya neutralizado)
- [x] 1.2. Sidebar / Topbar en SharedModule
- [x] 1.3. Redirección `/admin` → `/admin/dashboard-admin`
- [x] 1.4. Extraer estilos comunes layouts (`foundation.scss` centraliza layout + aliases)
- [x] 1.5. Módulo `ui-kit` (atoms/badges/buttons iniciales: button + badge)
- [x] 1.6. Lazy + preload selectivo (auth/search con data.preload + estrategia personalizada)

## 2. Rendimiento
- [ ] 2.1. Medir bundle inicial (stats)
- [ ] 2.2. Prefetch módulos tras idle
- [ ] 2.3. Imágenes lazy + formatos modernos
- [ ] 2.4. Evaluar split vendor si > umbral

## 3. Seguridad Frontend
- [ ] 3.1. Sanitizar bindings dinámicos
- [x] 3.2. Interceptor global 401/403 + refresh (single-flight refresh, cola espera, preserva FormData)
- [ ] 3.3. Documentar ausencia CSRF (JWT stateless)
- [ ] 3.4. Propuesta CSP (borrador)

## 4. Estado & Gestión Datos
- [ ] 4.1. Store usuario autenticado
- [ ] 4.2. Cache ligera catálogos
- [ ] 4.3. Normalizar interfaces dominio

## 5. Navegación & UX
- [ ] 5.1. Breadcrumbs admin
- [ ] 5.2. Loading states (skeleton/spinner service)
- [ ] 5.3. Errores visual unificados (toast + fallback)
- [ ] 5.4. Scroll restoration

## 6. Accesibilidad (A11y)
- [ ] 6.1. Roles / labels ARIA navbar & sidebar
- [ ] 6.2. Contraste mínimo WCAG (admin)
- [ ] 6.3. Focus visible / navegación teclado

## 7. Tests
- [ ] 7.1. Unit sidebar/topbar/navbar
- [ ] 7.2. Integración routing principal
- [ ] 7.3. Guards roles (admin vs cliente)
- [ ] 7.4. Snapshot componentes estáticos

## 8. Observabilidad
- [ ] 8.1. Interceptor métricas
- [ ] 8.2. Global error handler
- [ ] 8.3. Page view tracking (opt-in)

## 9. Internacionalización (i18n)
- [ ] 9.1. Externalizar strings clave
- [ ] 9.2. Estructura base mensajes

## 10. Documentación
- [x] 10.1. System Overview
- [x] 10.2. Arquitectura Frontend
- [ ] 10.3. ADR catálogo (cuando separe microservicio)
- [ ] 10.4. Guía contribución (naming, commits, ramas)
- [x] 10.5. Documento alcance MVP (`FRONTEND_MVP_CORE.md`)

## 11. Limpieza Código
- [ ] 11.1. Eliminar legacy definitivo
- [ ] 11.2. Consistencia sufijos (`*-page`)
- [ ] 11.3. Revisar imports profundos / barrels

## 12. Estilos / Theming
- [ ] 12.1. Variables SCSS base (colores, spacing)
- [ ] 12.2. Dark mode admin
- [ ] 12.3. Purgar CSS muerto

## 13. Acciones Inmediatas Prioridad (Top 8)
1) 1.3 Redirección /admin
2) 3.2 Interceptor 401/403 + refresh
3) 4.1 Store usuario autenticado
4) 5.2 Loading states (skeleton base)
5) 7.2 Test integración routing
6) 11.1 Limpieza legacy definitiva
7) 2.1 Medir bundle inicial
8) 12.1 Variables SCSS base

---
Actualizar este checklist conforme se completen tareas.
