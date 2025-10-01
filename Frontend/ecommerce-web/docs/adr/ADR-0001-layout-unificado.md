# ADR-0001: Unificación de Layout Admin

## Contexto
Existía duplicación de `AdminLayoutComponent`: uno en `core/layout/admin-layout` y otro dentro de `admin-dashboard/layouts/admin-layout`. Esto causaba:
- Conflictos de selector (`app-admin-layout`)
- Problemas de detección de componentes hijos (`app-admin-sidebar`)
- Riesgo de dependencias circulares y carga ansiosa del módulo admin

## Decisión
Centralizar el layout admin en `core/layout/admin-layout` y neutralizar el duplicado histórico:
- Renombrar componente duplicado a `AdminDashboardLegacyLayoutComponent` con selector `app-admin-dashboard-legacy-layout` y marcarlo obsoleto.
- Mover `AdminSidebarComponent` y `AdminTopbarComponent` a `SharedModule`.
- Eliminar import de `AdminDashboardModule` en `CoreModule` para mantener lazy loading.

## Consecuencias
### Positivas
- Menor confusión y errores de template
- Sidebar disponible sin romper lazy loading
- Ruta `/admin` mantiene un único punto de composición

### Negativas / Riesgos
- Código legacy debe eliminarse físicamente en commit futuro (ya neutralizado)
- Documentación debe reflejar cambio (resuelto con este ADR)

## Alternativas Consideradas
1. Mantener ambos layouts y diferenciar con selectors distintos → descartado (duplica mantenimiento).
2. Convertir todo a standalone inmediato → pospuesto para una fase posterior.

## Estado
Aceptado.

## Próximos pasos
- Añadir redirección `/admin` → `/admin/dashboard-admin`
- Documentar guidelines de creación de nuevos layouts en `FRONTEND_ARCHITECTURE.md`
