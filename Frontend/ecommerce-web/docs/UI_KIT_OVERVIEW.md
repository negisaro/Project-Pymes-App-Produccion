# UI Kit Overview

## Objetivo
Proveer una capa de componentes atómicos reutilizables (botones, badges, etc.) que permita:
- Consistencia visual.
- Evolución progresiva hacia un sistema de diseño.
- Reducción de duplicación de estilos.

## Componentes iniciales
1. `ui-button`
   - Variants: `primary`, `secondary`, `outline`, `danger`, `link`.
   - Sizes: `sm`, `md`, `lg`.
   - Props: `loading`, `disabled`, `type`, `ariaLabel`.
   - Accesibilidad: focus visible, aria-label opcional si no hay texto.

2. `ui-badge`
   - Variants: `neutral`, `success`, `info`, `warning`, `danger`.
   - Sizes: `sm`, `md`.
   - Props: `pill` para border-radius extendido.

## Uso rápido
En cualquier módulo que ya importe `SharedModule` (el cual reexporta `UiKitModule`):
```html
<ui-button variant="primary" (pressed)="onSave()">Guardar</ui-button>
<ui-badge variant="success" pill>Activo</ui-badge>
```

## Estilos
- Variables SCSS locales (colores) que migrarán a un archivo de design tokens (relacionado con checklist 12.1).
- Clases BEM-like: `.ui-btn--{variant}`, `.ui-btn--{size}`.

## Roadmap sugerido
- Inputs y Selects (`ui-input`, `ui-select`).
- Tokens de tipografía y espaciados globales.
- Theming (dark mode) apoyado en CSS variables.
- Documentación Storybook (si se adopta) o MDX.

## Buenas prácticas
- Mantener componentes puros (presentacionales) sin lógica de negocio.
- Evitar dependencia directa con librerías externas dentro de componentes base.
- Añadir pruebas unitarias a medida que crezca el set.

---
Actualizado: 2025-09-30
