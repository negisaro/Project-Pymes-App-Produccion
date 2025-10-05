# 🧩 Shared Components - Componentes Compartidos

## 📋 Descripción

Librería de componentes reutilizables, utilidades y servicios compartidos en toda la aplicación.

## 🎯 Componentes UI Compartidos

### 🎨 Design System
- **Color Palette** - Paleta de colores del sistema
- **Typography** - Sistema tipográfico
- **Spacing System** - Sistema de espaciado
- **Border Radius** - Bordes y radiados
- **Shadows & Elevation** - Sombras y elevación

### 🧱 Basic Components
- **Button Components** - Botones (primary, secondary, etc.)
- **Form Controls** - Controles de formulario
- **Input Components** - Componentes de entrada
- **Card Components** - Componentes de tarjeta
- **Modal Components** - Modales y popups

### 📱 Layout Components
- **Header Component** - Header global
- **Footer Component** - Footer global
- **Sidebar Component** - Barra lateral
- **Navigation Menu** - Menú de navegación
- **Breadcrumb** - Navegación breadcrumb

### 📊 Data Display
- **Table Components** - Tablas de datos
- **List Components** - Componentes de lista
- **Chart Components** - Gráficos y charts
- **Progress Indicators** - Indicadores de progreso
- **Status Badges** - Badges de estado

### 🔔 Feedback Components
- **Loading Spinners** - Spinners de carga
- **Toast Notifications** - Notificaciones toast
- **Alert Components** - Componentes de alerta
- **Error Messages** - Mensajes de error
- **Empty States** - Estados vacíos

## 📁 Estructura de Archivos

```
src/app/shared/
├── components/
│   ├── ui/
│   │   ├── button/
│   │   ├── input/
│   │   ├── card/
│   │   ├── modal/
│   │   └── badge/
│   ├── layout/
│   │   ├── header/
│   │   ├── footer/
│   │   ├── sidebar/
│   │   └── navigation/
│   ├── data-display/
│   │   ├── table/
│   │   ├── list/
│   │   ├── chart/
│   │   └── progress/
│   └── feedback/
│       ├── loading/
│       ├── toast/
│       ├── alert/
│       └── empty-state/
├── services/
│   ├── http.service.ts
│   ├── storage.service.ts
│   ├── notification.service.ts
│   └── utility.service.ts
├── pipes/
│   ├── currency.pipe.ts
│   ├── date.pipe.ts
│   ├── filter.pipe.ts
│   └── search.pipe.ts
├── directives/
│   ├── highlight.directive.ts
│   ├── click-outside.directive.ts
│   └── lazy-load.directive.ts
├── validators/
│   ├── email.validator.ts
│   ├── phone.validator.ts
│   └── custom.validators.ts
├── models/
│   ├── common.interfaces.ts
│   ├── api.models.ts
│   └── form.models.ts
└── utils/
    ├── date.utils.ts
    ├── string.utils.ts
    ├── validation.utils.ts
    └── storage.utils.ts
```

## 🚀 Estado Actual vs Objetivo

### ✅ Implementado
- [x] Estructura básica de shared
- [x] Algunos servicios básicos
- [x] Interceptores HTTP

### 🔄 En Desarrollo
- [ ] UI Kit básico
- [ ] Design system tokens

### 📋 Pendiente
- [ ] **Design System**
  - [ ] Color tokens
  - [ ] Typography scale
  - [ ] Spacing system
  - [ ] Component variants
- [ ] **UI Components**
  - [ ] Button library
  - [ ] Form controls
  - [ ] Card components
  - [ ] Modal system
- [ ] **Utilities**
  - [ ] Pipe library
  - [ ] Directive collection
  - [ ] Validator library
  - [ ] Utility functions
- [ ] **Services**
  - [ ] HTTP service
  - [ ] Storage service
  - [ ] Notification service
  - [ ] Theme service

## 🎨 Design Tokens

### Colors
```scss
// Primary Colors
$primary-50: #e3f2fd;
$primary-100: #bbdefb;
$primary-500: #2196f3;
$primary-900: #0d47a1;

// Secondary Colors
$secondary-50: #fce4ec;
$secondary-500: #e91e63;
$secondary-900: #880e4f;

// Neutral Colors
$neutral-50: #fafafa;
$neutral-100: #f5f5f5;
$neutral-500: #9e9e9e;
$neutral-900: #212121;
```

### Typography
```scss
// Font Families
$font-primary: 'Inter', sans-serif;
$font-secondary: 'Roboto', sans-serif;

// Font Sizes
$text-xs: 0.75rem;   // 12px
$text-sm: 0.875rem;  // 14px
$text-base: 1rem;    // 16px
$text-lg: 1.125rem;  // 18px
$text-xl: 1.25rem;   // 20px
```

### Spacing
```scss
// Spacing Scale
$space-1: 0.25rem;   // 4px
$space-2: 0.5rem;    // 8px
$space-3: 0.75rem;   // 12px
$space-4: 1rem;      // 16px
$space-6: 1.5rem;    // 24px
$space-8: 2rem;      // 32px
```

## 🔧 Servicios Compartidos

### HttpService
```typescript
@Injectable()
export class HttpService {
  get<T>(url: string, options?: HttpOptions): Observable<T>
  post<T>(url: string, data: any, options?: HttpOptions): Observable<T>
  put<T>(url: string, data: any, options?: HttpOptions): Observable<T>
  delete<T>(url: string, options?: HttpOptions): Observable<T>
}
```

### NotificationService
```typescript
@Injectable()
export class NotificationService {
  success(message: string, options?: ToastOptions): void
  error(message: string, options?: ToastOptions): void
  warning(message: string, options?: ToastOptions): void
  info(message: string, options?: ToastOptions): void
}
```

### StorageService
```typescript
@Injectable()
export class StorageService {
  setItem(key: string, value: any): void
  getItem<T>(key: string): T | null
  removeItem(key: string): void
  clear(): void
}
```

## 📖 Guías de Uso

- [Design System Guide](./design-system-guide.md)
- [Component Usage](./component-usage.md)
- [Service Implementation](./service-implementation.md)
- [Utilities Guide](./utilities-guide.md)

## 🎯 Roadmap de Desarrollo

### Fase 1 - Fundamentos
- [ ] Design tokens
- [ ] Basic UI components
- [ ] Core services
- [ ] Utility functions

### Fase 2 - Expansion
- [ ] Advanced components
- [ ] Complex layouts
- [ ] Animation system
- [ ] Accessibility features

### Fase 3 - Optimization
- [ ] Performance optimization
- [ ] Bundle size optimization
- [ ] Tree shaking
- [ ] Lazy loading

## 📏 Principios de Diseño

1. **Consistencia** - Componentes consistentes en toda la app
2. **Reutilización** - Máxima reutilización de componentes
3. **Accesibilidad** - Cumplir estándares WCAG 2.1
4. **Performance** - Optimizados para rendimiento
5. **Mantenibilidad** - Fácil mantenimiento y extensión

## 🧪 Testing Strategy

- **Unit Tests** - Tests unitarios para todos los componentes
- **Integration Tests** - Tests de integración
- **Visual Regression** - Tests de regresión visual
- **Accessibility Tests** - Tests de accesibilidad
- **Performance Tests** - Tests de rendimiento
