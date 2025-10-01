# (LEGACY) Plan de Implementación Frontend E-commerce

> NOTA: Este documento se conserva como referencia histórica. El estado vivo y priorización actual se gestionan ahora en:
> - `docs/FRONTEND_AUDIT_CHECKLIST.md`
> - `docs/FRONTEND_ARCHITECTURE.md`
> - ADRs en `docs/adr/`
>
> No añadir nuevas fases aquí; actualizar checklist y arquitectura.

---

## Contenido Histórico

(Se mantiene íntegro a partir del plan original)

# Plan de Implementación Frontend E-commerce

Estado actual: Arquitectura base, CoreModule, MainLayout, AdminLayout, módulos feature vacíos (catalog/product/cart), store de carrito (signals), design system tokens + theme inicial.

## Fases & Criterios de Aceptación

### 1. Fundaciones (COMPLETADO)
- CoreModule singleton
- Lazy Admin Dashboard
- Design System (tokens, theme, base components)
- Store carrito con persistencia
- MainLayout + routing público anidado

### 2. Layouts (EN PROGRESO)
- [x] MainLayout
- [x] AdminLayout (estructura base sidebar/topbar)
- [ ] AuthLayout (minimal, centrado, sin navbar principal)

Criterio: Rutas públicas, admin y auth encapsuladas en layouts distintos sin lógica duplicada.

### 3. Navbar & Shell (PENDIENTE / PARCIAL)
- [ ] Decomponer navbar monolítica en subcomponentes (brand, search, nav-links, user-menu, cart-indicator, theme-toggle)
- [ ] Estado responsive (mobile overlay)
- [ ] Cart indicator enlazado a store (contador reactivo) (store lista, falta binding visual)
- [ ] Theme toggle (en backlog de ThemeService)

### 4. Catálogo (MVP)
- ProductTileComponent (imagen, nombre, precio, CTA agregar)
- Grid responsivo (CSS clamp + auto-fit)
- Servicio mock (in-memory) para datos iniciales
- Skeleton loaders (3–6 placeholders)

### 5. Detalle de Producto
- Ruta /producto/:slug
- Galería básica + info + selector cantidad + botón agregar
- SEO tags (Title + meta description dinámico)

### 6. Carrito & Mini-Cart
- Flyout lateral (aria-modal pattern, focus trap)
- Listado items + subtotal + CTA checkout
- Vaciar / actualizar cantidades

### 7. Checkout (Inicial)
- Paso 1: Datos cliente / login inline
- Paso 2: Dirección
- Paso 3: Resumen + confirmar

### 8. Theming & Accesibilidad (BACKLOG)
- [ ] Modo oscuro refinado (tokens semánticos extendidos)
- [ ] Verificación contraste (AAA texto crítico)
- [ ] Skip link, roles landmark, focus visible
- [ ] High-contrast (fase posterior)

### 9. Refactor / Limpieza
- Eliminar clases Bootstrap redundantes donde ya hay utilidades
- Extraer componentes repetidos a shared/ui

### 10. Optimizaciones
- Preloading strategy selectiva
- image lazy + tamaños adaptativos
- Auditar bundle (source-map-explorer)

## Estructura de Componentes Clave
```
core/layout/
  main-layout
  admin-layout
  auth-layout (pendiente)
shared/ui/
  buttons/
  card/
  skeleton/
  modal/
navbar/
  navbar-shell.component
  navbar-brand.component
  navbar-search.component
  navbar-links.component
  navbar-user-menu.component
  navbar-cart-indicator.component
catalog/
  components/product-tile
  pages/catalog-page
product/
  pages/product-detail-page
cart/
  components/cart-flyout
checkout/
  pages/checkout-step-* (wizard)
```

## Estado de Store (Carrito)
- Signals: items, total, count
- Persistencia localStorage OK
- Pendiente: cupón, impuestos estimados, shipping

## Decisiones Técnicas
- Preferir signals > NgRx para MVP (simplicidad)
- CSS variables sobre theming; SCSS solo para tokens
- Layouts envuelven router-outlet (no contenedores duplicados)
- Accesibilidad integrada desde la fase 3 (navbar)

## Métricas de Éxito Iniciales
- CLS < 0.1 en catálogo (luego instrumentar)
- Tiempo primer render sin admin bundle (< 200ms adicional vs baseline)
- Peso ProductTile < 2KB TS + plantilla

## Riesgos y Mitigación
| Riesgo | Mitigación |
|--------|------------|
| Crecimiento store no tipado | Extender tipado y separar dominios en /state |
| Bloqueo por datos reales backend | Mock services + adaptar luego |
| Sobrecarga CSS | Auditar tokens y purgar utilidades huerfanas |

## Próximas Acciones Inmediatas (Histórico)
1. Crear AuthLayout (desbloquea flujo auth aislado)
2. Implementar ThemeService (persistencia preferencia + hook inicial)
3. Refactor Navbar modular consumiendo ThemeService y store carrito
4. ProductTile + catálogo mock (iniciar fase catálogo)

## Rollback Seguro
- Cambios modulares: revertir feature sin tocar núcleo
- Mantener commits atómicos por fase

---
## Sincronización Backend (Referencia Rápida)
| Área Backend | Estado |
|--------------|--------|
| DTOs Usuario/Rol | COMPLETADO |
| Mapper unificado UsuarioMapper + RolMapper | COMPLETADO |
| Actualización parcial segura (default method) | COMPLETADO |
| Tests mapper (Spring Boot, perfil test) | COMPLETADO |
| Config JWT (dev/test) + expiración externa | COMPLETADO |
| Secreto JWT unificado (GATEWAY_JWT_SECRET) | ACTIVO |

Última actualización histórica: 2025-09-30 (ajuste documentación secreto JWT)
