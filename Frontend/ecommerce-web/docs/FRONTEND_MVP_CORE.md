# Frontend / Backend MVP Núcleo

> Estado base para la primera entrega funcional. Documento vivo: sólo refleja el alcance mínimo que debe permanecer estable. Expansiones futuras se documentan fuera de este archivo.
>
> Última actualización: 2025-09-29

---
## 1. Objetivo de la Entrega
Permitir a un cliente navegar productos, ver detalle, agregar al carrito, ejecutar un checkout simple y consultar pedidos básicos. Permitir a un administrador gestionar productos/categorías y visualizar métricas mínimas.

---
## 2. Alcance Funcional (Resumen)
| Actor | Acción | Incluido | Notas |
|-------|--------|----------|-------|
| Cliente | Ver listado productos | Sí | Paginado básico + filtro categoría/búsqueda simple |
| Cliente | Ver detalle producto | Sí | Descripción, precio, stock disponible |
| Cliente | Añadir al carrito / modificar cantidades | Sí | Persistencia local (localStorage) |
| Cliente | Checkout (confirmar pedido) | Sí | Formulario compacto: datos básicos + dirección |
| Cliente | Ver pedidos propios | Sí | Listado + ver detalle simple |
| Admin | Login admin | Sí | Autenticación rol ROLE_ADMIN |
| Admin | Crear/editar productos | Sí | Formulario CRUD |
| Admin | Crear/editar categorías | Sí | CRUD |
| Admin | Ver métricas básicas | Sí | Conteos (#productos, #pedidos, #usuarios) |
| Admin | Ver todos los pedidos | Sí | Listado simple |
| Sistema | Refresh token silencioso | Sí | Automático antes expiración |
| Sistema | Validación rol por guard | Sí | RoleGuard + IsAuthenticatedGuard |
| Sistema | Logout | Sí | Limpia token + usuario |

Fuera de alcance MVP: wishlist, reviews, cupones, favoritos, estados avanzados de pedido, múltiples direcciones, pasarelas de pago reales, notificaciones.

---
## 3. Frontend: Módulos MVP
| Módulo | Estado | Responsabilidad | Puntos Clave |
|--------|--------|-----------------|--------------|
| core | Implementado | Layouts, bootstrapping, routing raíz | `MainLayout`, `AuthLayout`, `AdminLayout` |
| shared | Implementado base | Componentes reutilizables y UI estructural | Navbar, sidebar admin, topbar |
| auth | Parcial | Login + guards + refresh programado | Interceptor global pendiente |
| producto | Pendiente | Listado y detalle productos | Grid + slug/detail |
| categoria | Pendiente | Filtro y gestión admin | Asociado a producto |
| search | Parcial | Búsqueda por texto simple | Reusa listado producto |
| cart (features/state) | Implementado parcial | Gestión local de items | Falta UI final unificada |
| checkout | Pendiente | Flujo compra mínima | 1 paso confirmación |
| order | Pendiente | Listado y detalle pedidos usuario/admin | Simple sin estados complejos |
| admin-dashboard | Parcial | Métricas mínimas | Totales agregados |
| cliente-dashboard | Pendiente | Perfil + pedidos usuario | Reutiliza order service |
| state (user) | Pendiente | Fuente de verdad usuario | Sustituye lecturas directas storage |

---
## 4. Backend: Contextos / Servicios MVP
| Contexto | Endpoints Clave | Notas |
|----------|-----------------|-------|
| Auth | /auth/login /auth/check-token /auth/refresh /auth/logout | Refresh rotativo, validación issuer/audience |
| Usuario | /me (GET/PUT) | Actualización básica perfil |
| Productos | /productos (GET paginado + filtros) /productos/{id|slug} /admin/productos (CRUD) | Stock decrece al crear pedido |
| Categorías | /categorias (GET) /admin/categorias (CRUD) | Asociadas a productos |
| Pedidos | POST /pedidos, GET /pedidos (user) GET /admin/pedidos (admin) GET /pedidos/{id} | Estado inicial CREATED |
| Métricas | /admin/metrics | Retorna conteos agregados |
| Inventario | (Embebido en pedido) | Validación + decremento stock |
| Auditoría | Logs internos | No endpoint público |

Fuera de alcance: pasarela pagos, tracking envío, eventos asíncronos, notificaciones.

---
## 5. Flujos Críticos
### 5.1 Navegación Cliente
1. Home → Catálogo → Detalle → Añadir carrito → Checkout → Pedido creado → Ver pedido.
2. Búsqueda: Navbar input → /buscar → Lista filtrada.

### 5.2 Autenticación
1. Login (usuario/contraseña) → guarda token + usuario → schedule refresh.
2. Refresh antes de expirar (temporal) → reemplaza token.
3. Logout → limpia estado.

### 5.3 Pedido
1. Cliente confirma checkout → Backend valida stock → Crea pedido → Devuelve ID → Limpia carrito.
2. Cliente lista pedidos (GET /pedidos) → Ver detalle puntual.

### 5.4 Admin Gestión Producto
1. Admin login → Accede a dashboard → Crea/edita producto → Aparece en catálogo público.

---
## 6. Criterios de Aceptación MVP
| Área | Criterio |
|------|----------|
| Catálogo | Paginado básico y filtro categoría funcionan sin recarga total |
| Producto Detalle | Precio, nombre, stock y descripción visibles |
| Carrito | Añadir, eliminar, modificar cantidad persiste en localStorage |
| Checkout | Pedido generado produce ID y reduce stock |
| Pedidos Usuario | Solo ve sus pedidos |
| Pedidos Admin | Ve todos y puede filtrar por usuario (futuro) |
| Seguridad | Acceso admin bloqueado sin rol |
| Refresh | No obliga re-login durante sesión normal |
| Métricas | Panel muestra conteos reales |

---
## 7. No Funcional (Scope MVP)
| Aspecto | Meta |
|---------|------|
| Performance | LCP inicial aceptable (< 3s local dev) antes de optimizar |
| Seguridad | JWT con issuer/audience + rotación claves |
| Mantenibilidad | Diagrama actualizado + checklist vivo |
| Observabilidad | Logging backend básico, sin métricas frontend aún |

---
## 8. Fuera de Alcance (Explícito)
- Wishlist / favoritos
- Reviews / calificaciones
- Cupones / descuentos
- Estados avanzados de pedido (PAID, SHIPPED, DELIVERED)
- Pasarela de pago real (se puede mockear confirmación)
- Multi-direcciones / facturación fiscal
- Notificaciones email / push
- Internacionalización avanzada
- Modo oscuro completo / theming semántico extendido

---
## 9. Riesgos & Mitigación
| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| Carrito sólo local | Pérdida entre dispositivos | Persistencia backend en fase 2 |
| Falta interceptor | Errores 401 dispersos | Implementar early (prioridad) |
| Crecimiento módulo auth | Peso bundle | Dividir si supera umbral después medición |
| Stock sin transacciones | Condiciones carrera (concurrencia) | Aislar service para futura transaccionalidad |

---
## 10. Orden Recomendado de Implementación
1. Redirección `/admin` + interceptor 401/refresh.
2. CRUD Producto/Categoría backend + listado / detalle frontend.
3. Carrito UI + adaptación checkout.
4. Endpoint crear pedido + estado CREATED + reducción stock.
5. Listado pedidos (user/admin) + métricas.
6. Store usuario + test integración routing.
7. Métricas bundle + variables SCSS base.

---
## 11. Mantenimiento del Documento
- Solo actualizar cuando cambie el alcance aprobado del MVP.
- Las expansiones post-MVP van a README de roadmap o nueva sección incremental.
- Si una capacidad se descarta antes de implementarla, mover a “Fuera de alcance” con breve razón.

---
## 12. Próximos Enlaces Relacionados
- `FRONTEND_ARCHITECTURE.md` (estructura técnica)
- `FRONTEND_FLOW_DIAGRAM.md` (diagramas actualizados)
- `FRONTEND_AUDIT_CHECKLIST.md` (estado granular y prioridades)

---
Si algo aquí deja de reflejar la realidad del código, este archivo tiene prioridad de corrección inmediata (fuente oficial del alcance MVP).
