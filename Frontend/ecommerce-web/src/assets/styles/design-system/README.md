# Design System (Estado Intermedio)

Este directorio contiene la capa de tokens SCSS y las variables semánticas iniciales.

## Capas actuales
1. **Tokens SCSS (raw)**
   - `tokens/_colors.scss`
   - `tokens/_spacing.scss`
   - `tokens/_radius.scss`
   - `tokens/_shadows.scss`
   - `tokens/_typography.scss`
   Se importan vía `theme/_index.scss` pero hoy casi no son consumidos directamente en componentes.

2. **Semantic layer**
   - `theme/_semantic.scss`: define un set base de CSS custom properties (root + `.theme-dark`).
   - Overridden posteriormente por `foundation.scss` (dark/light con `data-theme`).

3. **Foundation (fuera de esta carpeta)**
   - `assets/styles/foundation.scss` establece tema claro/oscuro dinámico, componentes base (`.ui-btn`, `.ui-card`), y refuerza variables.

## Lo que se eliminó
- `base.scss` (duplicaba reglas de botones y tarjetas). Mantener un único punto de entrada: `foundation.scss`.

## Decisiones actuales (Opción C)
- Conservamos los tokens SCSS para futura generación automática (maps, loops) aunque no se usen aún.
- No fusionamos todavía `semantic` dentro de `foundation` para mantener separación conceptual.
- Se planifica exponer tokens como CSS vars si se requieren en tiempo de ejecución.

## Próximos pasos sugeridos
1. Exponer tokens de spacing/radius/shadow como CSS vars directamente en `foundation.scss`.
2. Crear utilidades tipográficas usando el mapa `$font-scale` (`h1..h5`).
3. Revisar redundancias entre `_semantic.scss` y bloques de tema de `foundation.scss` para evitar overlap.
4. Añadir test visual (Storybook o equivalente) antes de refactors profundos.

## Convención de uso
- En componentes preferir `var(--color-*)` y no `$blue-600` para permitir theming dinámico.
- Si se necesita una escala consistente de espacio, migrar `$space-*` a custom properties (`--space-*`).

---
Última actualización realizada automáticamente por el asistente (Opción C).
