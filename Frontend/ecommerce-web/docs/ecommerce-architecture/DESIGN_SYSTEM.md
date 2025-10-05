# 🎨 Design System - Sistema de Diseño

## 📋 Descripción

Sistema de diseño unificado para el E-Commerce con tokens, componentes y patrones visuales consistentes.

## 🎯 Design Tokens

### 🎨 **Color Palette**

```scss
// Primary Colors - Colores Primarios
:root {
  --color-primary-50: #f0f9ff;
  --color-primary-100: #e0f2fe;
  --color-primary-200: #bae6fd;
  --color-primary-300: #7dd3fc;
  --color-primary-400: #38bdf8;
  --color-primary-500: #0ea5e9; // Main brand color
  --color-primary-600: #0284c7;
  --color-primary-700: #0369a1;
  --color-primary-800: #075985;
  --color-primary-900: #0c4a6e;
}

// Secondary Colors - Colores Secundarios
:root {
  --color-secondary-50: #fafaf9;
  --color-secondary-100: #f5f5f4;
  --color-secondary-200: #e7e5e4;
  --color-secondary-300: #d6d3d1;
  --color-secondary-400: #a8a29e;
  --color-secondary-500: #78716c;
  --color-secondary-600: #57534e;
  --color-secondary-700: #44403c;
  --color-secondary-800: #292524;
  --color-secondary-900: #1c1917;
}

// Semantic Colors - Colores Semánticos
:root {
  --color-success-50: #f0fdf4;
  --color-success-500: #22c55e;
  --color-success-700: #15803d;
  
  --color-warning-50: #fffbeb;
  --color-warning-500: #f59e0b;
  --color-warning-700: #b45309;
  
  --color-error-50: #fef2f2;
  --color-error-500: #ef4444;
  --color-error-700: #b91c1c;
  
  --color-info-50: #f0f9ff;
  --color-info-500: #3b82f6;
  --color-info-700: #1d4ed8;
}

// Neutral Colors - Colores Neutrales
:root {
  --color-white: #ffffff;
  --color-gray-50: #f9fafb;
  --color-gray-100: #f3f4f6;
  --color-gray-200: #e5e7eb;
  --color-gray-300: #d1d5db;
  --color-gray-400: #9ca3af;
  --color-gray-500: #6b7280;
  --color-gray-600: #4b5563;
  --color-gray-700: #374151;
  --color-gray-800: #1f2937;
  --color-gray-900: #111827;
  --color-black: #000000;
}
```

### 📏 **Spacing Scale**

```scss
:root {
  --space-0: 0;
  --space-px: 1px;
  --space-0_5: 0.125rem; // 2px
  --space-1: 0.25rem;    // 4px
  --space-1_5: 0.375rem; // 6px
  --space-2: 0.5rem;     // 8px
  --space-2_5: 0.625rem; // 10px
  --space-3: 0.75rem;    // 12px
  --space-3_5: 0.875rem; // 14px
  --space-4: 1rem;       // 16px
  --space-5: 1.25rem;    // 20px
  --space-6: 1.5rem;     // 24px
  --space-7: 1.75rem;    // 28px
  --space-8: 2rem;       // 32px
  --space-9: 2.25rem;    // 36px
  --space-10: 2.5rem;    // 40px
  --space-11: 2.75rem;   // 44px
  --space-12: 3rem;      // 48px
  --space-14: 3.5rem;    // 56px
  --space-16: 4rem;      // 64px
  --space-20: 5rem;      // 80px
  --space-24: 6rem;      // 96px
  --space-28: 7rem;      // 112px
  --space-32: 8rem;      // 128px
}
```

### 🔤 **Typography Scale**

```scss
:root {
  // Font Families
  --font-sans: 'Inter', 'system-ui', 'sans-serif';
  --font-serif: 'Georgia', 'serif';
  --font-mono: 'Fira Code', 'monospace';
  
  // Font Sizes
  --text-xs: 0.75rem;     // 12px
  --text-sm: 0.875rem;    // 14px
  --text-base: 1rem;      // 16px
  --text-lg: 1.125rem;    // 18px
  --text-xl: 1.25rem;     // 20px
  --text-2xl: 1.5rem;     // 24px
  --text-3xl: 1.875rem;   // 30px
  --text-4xl: 2.25rem;    // 36px
  --text-5xl: 3rem;       // 48px
  --text-6xl: 3.75rem;    // 60px
  
  // Line Heights
  --leading-3: 0.75rem;
  --leading-4: 1rem;
  --leading-5: 1.25rem;
  --leading-6: 1.5rem;
  --leading-7: 1.75rem;
  --leading-8: 2rem;
  --leading-9: 2.25rem;
  --leading-10: 2.5rem;
  
  // Font Weights
  --font-thin: 100;
  --font-light: 300;
  --font-normal: 400;
  --font-medium: 500;
  --font-semibold: 600;
  --font-bold: 700;
  --font-extrabold: 800;
  --font-black: 900;
}
```

### 🔲 **Border Radius**

```scss
:root {
  --radius-none: 0;
  --radius-sm: 0.125rem;  // 2px
  --radius-md: 0.375rem;  // 6px
  --radius-lg: 0.5rem;    // 8px
  --radius-xl: 0.75rem;   // 12px
  --radius-2xl: 1rem;     // 16px
  --radius-3xl: 1.5rem;   // 24px
  --radius-full: 9999px;
}
```

### 🌫️ **Shadows**

```scss
:root {
  --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
  --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  --shadow-xl: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
  --shadow-2xl: 0 25px 50px -12px rgb(0 0 0 / 0.25);
  --shadow-inner: inset 0 2px 4px 0 rgb(0 0 0 / 0.05);
}
```

### 📐 **Z-Index Scale**

```scss
:root {
  --z-auto: auto;
  --z-0: 0;
  --z-10: 10;
  --z-20: 20;
  --z-30: 30;
  --z-40: 40;
  --z-50: 50;
  
  // Semantic Z-Index
  --z-dropdown: 1000;
  --z-sticky: 1020;
  --z-fixed: 1030;
  --z-modal-backdrop: 1040;
  --z-modal: 1050;
  --z-popover: 1060;
  --z-tooltip: 1070;
  --z-toast: 1080;
}
```

## 🧩 **Component Library**

### 🔘 **Button Component**

```typescript
@Component({
  selector: 'app-button',
  template: `
    <button 
      [type]="type"
      [disabled]="disabled || loading"
      [class]="buttonClasses"
      (click)="onClick($event)">
      
      <app-icon 
        *ngIf="loading" 
        name="spinner" 
        class="animate-spin mr-2">
      </app-icon>
      
      <app-icon 
        *ngIf="icon && !loading" 
        [name]="icon" 
        [class]="iconClasses">
      </app-icon>
      
      <span *ngIf="!iconOnly">
        <ng-content></ng-content>
      </span>
    </button>
  `,
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() variant: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger' = 'primary';
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() disabled = false;
  @Input() loading = false;
  @Input() icon?: string;
  @Input() iconOnly = false;
  @Input() fullWidth = false;
  
  @Output() clicked = new EventEmitter<MouseEvent>();

  get buttonClasses(): string {
    return [
      'btn',
      `btn--${this.variant}`,
      `btn--${this.size}`,
      { 'btn--full-width': this.fullWidth },
      { 'btn--icon-only': this.iconOnly },
      { 'btn--loading': this.loading }
    ].filter(Boolean).join(' ');
  }

  get iconClasses(): string {
    const position = this.iconOnly ? '' : 'mr-2';
    return `btn__icon ${position}`;
  }

  onClick(event: MouseEvent): void {
    if (!this.disabled && !this.loading) {
      this.clicked.emit(event);
    }
  }
}
```

```scss
// button.component.scss
.btn {
  @apply inline-flex items-center justify-center font-medium transition-all duration-200;
  @apply focus:outline-none focus:ring-2 focus:ring-offset-2;
  @apply disabled:opacity-50 disabled:cursor-not-allowed;
  
  &--primary {
    @apply bg-primary-500 text-white;
    @apply hover:bg-primary-600 focus:ring-primary-500;
  }
  
  &--secondary {
    @apply bg-gray-200 text-gray-900;
    @apply hover:bg-gray-300 focus:ring-gray-500;
  }
  
  &--outline {
    @apply border border-gray-300 bg-white text-gray-700;
    @apply hover:bg-gray-50 focus:ring-gray-500;
  }
  
  &--ghost {
    @apply text-gray-700;
    @apply hover:bg-gray-100 focus:ring-gray-500;
  }
  
  &--danger {
    @apply bg-error-500 text-white;
    @apply hover:bg-error-600 focus:ring-error-500;
  }
  
  &--sm {
    @apply px-3 py-1.5 text-sm rounded-md;
  }
  
  &--md {
    @apply px-4 py-2 text-base rounded-lg;
  }
  
  &--lg {
    @apply px-6 py-3 text-lg rounded-xl;
  }
  
  &--full-width {
    @apply w-full;
  }
  
  &--icon-only {
    @apply aspect-square p-2;
  }
  
  &--loading {
    @apply cursor-wait;
  }
}
```

### 📋 **Input Component**

```typescript
@Component({
  selector: 'app-input',
  template: `
    <div class="input-group">
      <label 
        *ngIf="label" 
        [for]="inputId" 
        class="input-label"
        [class.required]="required">
        {{ label }}
      </label>
      
      <div class="input-wrapper" [class]="wrapperClasses">
        <app-icon 
          *ngIf="prefixIcon" 
          [name]="prefixIcon" 
          class="input-icon input-icon--prefix">
        </app-icon>
        
        <input 
          [id]="inputId"
          [type]="type"
          [placeholder]="placeholder"
          [disabled]="disabled"
          [readonly]="readonly"
          [value]="value"
          [class]="inputClasses"
          (input)="onInput($event)"
          (blur)="onBlur()"
          (focus)="onFocus()">
        
        <app-icon 
          *ngIf="suffixIcon" 
          [name]="suffixIcon" 
          class="input-icon input-icon--suffix">
        </app-icon>
      </div>
      
      <div *ngIf="hint || error" class="input-feedback">
        <span *ngIf="error" class="input-error">{{ error }}</span>
        <span *ngIf="hint && !error" class="input-hint">{{ hint }}</span>
      </div>
    </div>
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true
    }
  ]
})
export class InputComponent implements ControlValueAccessor {
  @Input() label?: string;
  @Input() type: 'text' | 'email' | 'password' | 'number' | 'tel' = 'text';
  @Input() placeholder?: string;
  @Input() hint?: string;
  @Input() error?: string;
  @Input() disabled = false;
  @Input() readonly = false;
  @Input() required = false;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() prefixIcon?: string;
  @Input() suffixIcon?: string;
  
  inputId = `input-${Math.random().toString(36).substr(2, 9)}`;
  value = '';
  focused = false;
  
  private onChange = (value: string) => {};
  private onTouched = () => {};

  get wrapperClasses(): string {
    return [
      'input-wrapper',
      { 'input-wrapper--focused': this.focused },
      { 'input-wrapper--error': this.error },
      { 'input-wrapper--disabled': this.disabled }
    ].filter(Boolean).join(' ');
  }

  get inputClasses(): string {
    return [
      'input',
      `input--${this.size}`,
      { 'input--prefix': this.prefixIcon },
      { 'input--suffix': this.suffixIcon }
    ].filter(Boolean).join(' ');
  }

  onInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.value = target.value;
    this.onChange(this.value);
  }

  onFocus(): void {
    this.focused = true;
  }

  onBlur(): void {
    this.focused = false;
    this.onTouched();
  }

  // ControlValueAccessor implementation
  writeValue(value: string): void {
    this.value = value || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
```

### 🎴 **Card Component**

```typescript
@Component({
  selector: 'app-card',
  template: `
    <div [class]="cardClasses">
      <ng-content></ng-content>
    </div>
  `
})
export class CardComponent {
  @Input() variant: 'default' | 'elevated' | 'outlined' = 'default';
  @Input() padding: 'none' | 'sm' | 'md' | 'lg' = 'md';
  @Input() interactive = false;

  get cardClasses(): string {
    return [
      'card',
      `card--${this.variant}`,
      `card--padding-${this.padding}`,
      { 'card--interactive': this.interactive }
    ].filter(Boolean).join(' ');
  }
}
```

## 🎯 **Layout System**

### 📱 **Container Component**

```typescript
@Component({
  selector: 'app-container',
  template: `
    <div [class]="containerClasses">
      <ng-content></ng-content>
    </div>
  `
})
export class ContainerComponent {
  @Input() size: 'sm' | 'md' | 'lg' | 'xl' | 'full' = 'lg';
  @Input() centered = true;

  get containerClasses(): string {
    return [
      'container',
      `container--${this.size}`,
      { 'container--centered': this.centered }
    ].filter(Boolean).join(' ');
  }
}
```

```scss
.container {
  width: 100%;
  
  &--centered {
    margin-left: auto;
    margin-right: auto;
  }
  
  &--sm {
    max-width: 640px;
  }
  
  &--md {
    max-width: 768px;
  }
  
  &--lg {
    max-width: 1024px;
  }
  
  &--xl {
    max-width: 1280px;
  }
  
  &--full {
    max-width: none;
  }
}
```

### 🔲 **Grid System**

```typescript
@Component({
  selector: 'app-grid',
  template: `
    <div [class]="gridClasses">
      <ng-content></ng-content>
    </div>
  `
})
export class GridComponent {
  @Input() cols: 1 | 2 | 3 | 4 | 6 | 12 = 1;
  @Input() gap: 'sm' | 'md' | 'lg' = 'md';
  @Input() responsive = true;

  get gridClasses(): string {
    return [
      'grid',
      `grid-cols-${this.cols}`,
      `gap-${this.gap}`,
      { 'grid--responsive': this.responsive }
    ].filter(Boolean).join(' ');
  }
}

@Component({
  selector: 'app-grid-item',
  template: `
    <div [class]="itemClasses">
      <ng-content></ng-content>
    </div>
  `
})
export class GridItemComponent {
  @Input() span: 1 | 2 | 3 | 4 | 6 | 12 = 1;
  @Input() order?: number;

  get itemClasses(): string {
    return [
      'grid-item',
      `col-span-${this.span}`,
      this.order ? `order-${this.order}` : ''
    ].filter(Boolean).join(' ');
  }
}
```

## 🎨 **Theme System**

### 🌙 **Dark Mode Support**

```scss
// Theme Variables
:root {
  --bg-primary: var(--color-white);
  --bg-secondary: var(--color-gray-50);
  --bg-tertiary: var(--color-gray-100);
  
  --text-primary: var(--color-gray-900);
  --text-secondary: var(--color-gray-600);
  --text-tertiary: var(--color-gray-500);
  
  --border-primary: var(--color-gray-200);
  --border-secondary: var(--color-gray-300);
}

[data-theme="dark"] {
  --bg-primary: var(--color-gray-900);
  --bg-secondary: var(--color-gray-800);
  --bg-tertiary: var(--color-gray-700);
  
  --text-primary: var(--color-white);
  --text-secondary: var(--color-gray-300);
  --text-tertiary: var(--color-gray-400);
  
  --border-primary: var(--color-gray-700);
  --border-secondary: var(--color-gray-600);
}
```

### 🎨 **Theme Service**

```typescript
@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly THEME_KEY = 'app-theme';
  private currentTheme$ = new BehaviorSubject<'light' | 'dark'>('light');

  theme$ = this.currentTheme$.asObservable();

  constructor() {
    this.initializeTheme();
  }

  private initializeTheme(): void {
    const savedTheme = localStorage.getItem(this.THEME_KEY) as 'light' | 'dark';
    const systemTheme = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    
    const theme = savedTheme || systemTheme;
    this.setTheme(theme);
  }

  setTheme(theme: 'light' | 'dark'): void {
    this.currentTheme$.next(theme);
    localStorage.setItem(this.THEME_KEY, theme);
    document.documentElement.setAttribute('data-theme', theme);
  }

  toggleTheme(): void {
    const current = this.currentTheme$.value;
    this.setTheme(current === 'light' ? 'dark' : 'light');
  }
}
```

Este sistema de diseño asegura consistencia visual y experiencia de usuario cohesiva. 🎨
