# 🎨 Design Tokens - Sistema de Diseño

## 📋 Descripción

Tokens de diseño fundamentales para la aplicación E-Commerce, definiendo la base visual consistente.

## 🌈 **Paleta de Colores**

### **🎯 Colores Primarios**
```
┌─────────────────────────────────────┐
│ Primary Brand                       │
├─────────────────────────────────────┤
│ #0ea5e9  █████  Sky Blue 500       │ ← Main Brand
│ #0284c7  █████  Sky Blue 600       │ ← Hover State
│ #0369a1  █████  Sky Blue 700       │ ← Active State
│ #bae6fd  █████  Sky Blue 200       │ ← Light Variant
│ #f0f9ff  █████  Sky Blue 50        │ ← Background
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Secondary Accent                    │
├─────────────────────────────────────┤
│ #6366f1  █████  Indigo 500         │ ← Secondary Action
│ #4f46e5  █████  Indigo 600         │ ← Hover State
│ #4338ca  █████  Indigo 700         │ ← Active State
│ #c7d2fe  █████  Indigo 200         │ ← Light Variant
│ #f1f5f9  █████  Indigo 50          │ ← Background
└─────────────────────────────────────┘
```

### **🚦 Colores Semánticos**
```
┌─────────────────────────────────────┐
│ Success States                      │
├─────────────────────────────────────┤
│ #10b981  █████  Emerald 500        │ ← Success
│ #059669  █████  Emerald 600        │ ← Success Hover
│ #d1fae5  █████  Emerald 100        │ ← Success Background
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Warning States                      │
├─────────────────────────────────────┤
│ #f59e0b  █████  Amber 500          │ ← Warning
│ #d97706  █████  Amber 600          │ ← Warning Hover
│ #fef3c7  █████  Amber 100          │ ← Warning Background
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Error States                        │
├─────────────────────────────────────┤
│ #ef4444  █████  Red 500            │ ← Error
│ #dc2626  █████  Red 600            │ ← Error Hover
│ #fee2e2  █████  Red 100            │ ← Error Background
└─────────────────────────────────────┘
```

### **⚫ Colores Neutrales**
```
┌─────────────────────────────────────┐
│ Text & UI Colors                    │
├─────────────────────────────────────┤
│ #0f172a  █████  Slate 900          │ ← Primary Text
│ #334155  █████  Slate 700          │ ← Secondary Text
│ #64748b  █████  Slate 500          │ ← Tertiary Text
│ #94a3b8  █████  Slate 400          │ ← Disabled Text
│ #e2e8f0  █████  Slate 200          │ ← Borders
│ #f1f5f9  █████  Slate 100          │ ← Backgrounds
│ #f8fafc  █████  Slate 50           │ ← Light Backgrounds
│ #ffffff  █████  White              │ ← Pure White
└─────────────────────────────────────┘
```

## 🔤 **Typography System**

### **📝 Font Family**
```
Primary:   'Inter', system-ui, -apple-system, sans-serif
Secondary: 'Poppins', sans-serif (for headings)
Mono:      'Fira Code', 'Menlo', monospace
```

### **📏 Font Scale**
```
┌─────────────────────────────────────┐
│ Text Sizes                          │
├─────────────────────────────────────┤
│ 12px / 16px  xs   Caption, Labels   │
│ 14px / 20px  sm   Body Small        │
│ 16px / 24px  md   Body Regular      │ ← Base Size
│ 18px / 28px  lg   Body Large        │
│ 20px / 28px  xl   Subheading        │
│ 24px / 32px  2xl  Heading 3         │
│ 30px / 36px  3xl  Heading 2         │
│ 36px / 40px  4xl  Heading 1         │
│ 48px / 48px  5xl  Display Large     │
│ 60px / 60px  6xl  Hero Title        │
└─────────────────────────────────────┘
```

### **⚖️ Font Weights**
```
Light:     300  (Minimal use)
Regular:   400  (Body text)
Medium:    500  (Emphasized text)
Semibold:  600  (Headings, buttons)
Bold:      700  (Strong emphasis)
```

## 📐 **Spacing System**

### **📏 Spacing Scale**
```
┌─────────────────────────────────────┐
│ Spacing Values                      │
├─────────────────────────────────────┤
│ 4px    xs    Tight spacing          │
│ 8px    sm    Component padding      │
│ 12px   md-   Form elements          │
│ 16px   md    Default spacing        │ ← Base Unit
│ 20px   md+   Comfortable spacing    │
│ 24px   lg    Section spacing        │
│ 32px   xl    Card padding           │
│ 40px   2xl   Component gaps         │
│ 48px   3xl   Section margins        │
│ 64px   4xl   Page sections          │
│ 80px   5xl   Large sections         │
│ 96px   6xl   Hero spacing           │
└─────────────────────────────────────┘
```

### **🎯 Usage Examples**
```
Button Padding:     12px 24px (md- xl)
Card Padding:       24px (lg)
Input Padding:      12px 16px (md- md)
Section Margin:     48px (3xl)
Component Gap:      16px (md)
```

## 🔲 **Border & Radius**

### **📐 Border Radius**
```
┌─────────────────────────────────────┐
│ Radius Values                       │
├─────────────────────────────────────┤
│ 0px     none    No radius           │
│ 2px     xs      Small elements      │
│ 4px     sm      Buttons, inputs     │
│ 8px     md      Cards, containers   │ ← Default
│ 12px    lg      Large cards         │
│ 16px    xl      Modal, panels       │
│ 24px    2xl     Hero sections       │
│ 9999px  full    Pills, avatars      │
└─────────────────────────────────────┘
```

### **🔲 Border Widths**
```
1px     Default borders
2px     Emphasized borders
4px     Thick accents
```

## 🌫️ **Shadow System**

### **📦 Box Shadows**
```
┌─────────────────────────────────────┐
│ Shadow Levels                       │
├─────────────────────────────────────┤
│ xs   0 1px 2px rgba(0,0,0,0.05)     │ ← Subtle depth
│ sm   0 1px 3px rgba(0,0,0,0.1)      │ ← Light cards
│ md   0 4px 6px rgba(0,0,0,0.07)     │ ← Default cards
│ lg   0 10px 15px rgba(0,0,0,0.1)    │ ← Elevated cards
│ xl   0 20px 25px rgba(0,0,0,0.1)    │ ← Floating elements
│ 2xl  0 25px 50px rgba(0,0,0,0.25)   │ ← Modal, drawer
└─────────────────────────────────────┘
```

### **🎯 Shadow Usage**
```
Button Hover:    shadow-sm
Cards:          shadow-md
Dropdown:       shadow-lg
Modal:          shadow-2xl
```

## 📱 **Responsive Breakpoints**

### **📐 Screen Sizes**
```
┌─────────────────────────────────────┐
│ Breakpoints                         │
├─────────────────────────────────────┤
│ sm   640px+   Mobile Large          │
│ md   768px+   Tablet Portrait       │
│ lg   1024px+  Tablet Landscape      │
│ xl   1280px+  Desktop Small         │
│ 2xl  1536px+  Desktop Large         │
└─────────────────────────────────────┘
```

### **🎯 Container Widths**
```
sm    640px   max-width
md    768px   max-width
lg    1024px  max-width
xl    1280px  max-width
2xl   1536px  max-width
```

## ⚡ **Z-Index Scale**

### **📚 Layer System**
```
┌─────────────────────────────────────┐
│ Z-Index Values                      │
├─────────────────────────────────────┤
│ 0      auto      Normal flow        │
│ 10     elevated  Dropdowns          │
│ 20     sticky    Sticky headers     │
│ 30     fixed     Fixed navigation   │
│ 40     overlay   Modal backdrop     │
│ 50     modal     Modal content      │
│ 60     popover   Popovers           │
│ 70     tooltip   Tooltips           │
│ 80     toast     Notifications      │
│ 90     loading   Loading overlays   │
└─────────────────────────────────────┘
```

## 🎭 **Animation Tokens**

### **⏱️ Duration**
```
fast    150ms   Micro-interactions
base    250ms   Default transitions
slow    350ms   Complex animations
```

### **📈 Easing**
```
linear     linear               No acceleration
ease-out   cubic-bezier(0,0,.2,1)   Smooth deceleration
ease-in    cubic-bezier(.4,0,1,1)   Smooth acceleration  
bounce     cubic-bezier(.68,-.55,.265,1.55)  Playful bounce
```

## 🎯 **Usage Guidelines**

### **✅ Do's**
- Usar spacing múltiples de 4px
- Mantener consistencia en radius
- Aplicar shadows apropiados según contexto
- Respetar la jerarquía tipográfica
- Usar colores semánticos correctamente

### **❌ Don'ts**
- No crear spacing custom arbitrarios
- No mezclar diferentes radius en mismo componente
- No usar shadows muy pesados en mobile
- No exceder 3 pesos tipográficos por vista
- No usar colores fuera del sistema

---

*🎨 Design Tokens v1.0 - Project-Pymes-App-Produccion*
