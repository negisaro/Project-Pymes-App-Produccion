# Frontend Architecture

## Capas / Módulos
```
shared/    -> Componentes reutilizables, páginas genéricas, utilidades UI
core/      -> Layouts, navegación, shell app, elementos estructurales
admin-dashboard/ (lazy) -> KPIs, gráficos, panel administración
cliente-dashboard/ (lazy) -> Panel usuario cliente
producto/  (lazy) -> Gestión de productos
categoria/ (lazy) -> Gestión de categorías
proveedor/ (lazy) -> Gestión de proveedores
search/    (lazy) -> Búsqueda
auth/      -> Formularios login/registro, guards, interceptors
state/     -> Stores globales (cart, usuario futuro)
features/  -> Funcionalidad vertical aislada (ej: carrito)
```

## Reglas de Dependencia
```
shared  : no depende de feature ni de core
core    : puede depender de shared
feature : puede depender de shared y state
state   : autónomo (no depende de features)
auth    : depende de core (layouts) y shared
```

## Layouts
| Layout | Rol | Contiene |
|--------|-----|----------|
| MainLayout | Público | Home, About, búsqueda, catálogo público*
| AuthLayout | Sesión | Login, Registro, Recuperación* |
| AdminLayout | Backoffice | Sidebar admin, topbar, dashboards |

(*) elementos planificados.

## Componentes Clave
- `AdminSidebarComponent` (menú navegación admin) – ahora en `SharedModule`.
- `AdminTopbarComponent` (acciones rápidas / usuario actual).
- `Navbar*` (dividido en piezas para composición flexible).

## Patrones Adoptados
- Módulos lazy para reducir bundle inicial.
- Nombres consistentes: `XxxModule`, `XxxComponent`.
- Evitar declarar componentes en más de un módulo.
- Reutilización via export de `SharedModule`.

## Estilos
- SCSS modular por layout + componentes.
- Posible futura extracción de variables a `styles/_theme.scss`.

## Errores Corregidos Recientes
| Problema | Solución |
|----------|----------|
| Duplicación `AdminLayoutComponent` | Neutralizado duplicado, consolidado en core |
| Sidebar desconocido en layout | Mover sidebar/topbar a Shared + importar en Core |
| Carga eager de admin-dashboard | Eliminado import en CoreModule |

## Próximas Mejoras Técnicas
1. Redirección `/admin` → `/admin/dashboard-admin`.
2. Breadcrumbs reutilizable.
3. Guards por rol encapsulados (wrapper para RoleGuard).
4. Consolidar estilos layout (mixins). 
5. Añadir pruebas de componentes críticos.

## Diagramas de Flujo
El flujo funcional y los diagramas de secuencia se mantienen en `FRONTEND_FLOW_DIAGRAM.md`.

Referencias rápidas:
- Rutas y layouts actuales.
- Secuencia de login/restauración + refresh.
- Guards de acceso.
- Roadmap incremental (tabla de módulos).

## Alcance MVP Núcleo
El alcance funcional mínimo aprobado está descrito en `FRONTEND_MVP_CORE.md` (fuente oficial de qué entra y qué queda fuera del primer release).

## Métricas (Pendiente)
- Tiempos de carga inicial.
- Tamaño bundle por segmento (main vs lazy).

## Observabilidad Frontend (Futuro)
- Interceptor para medir latencias y enviar a backend de métricas.
- Integración con analíticas de eventos (opt-in).
