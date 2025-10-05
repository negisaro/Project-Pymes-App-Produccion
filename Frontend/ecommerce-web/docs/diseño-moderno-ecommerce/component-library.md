# 🧩 Component Library - Biblioteca de Componentes

## 📋 Descripción

Biblioteca visual de componentes para la aplicación E-Commerce, mostrando diseños modernos y estados interactivos.

## 🔘 **Buttons - Botones**

### **🎯 Primary Buttons**
```
┌─────────────────────────────────────┐
│ Primary Action Buttons              │
├─────────────────────────────────────┤
│                                     │
│  [ Comprar Ahora ]  ← Primary       │
│  [ Comprar Ahora ]  ← Hover         │ (Darker)
│  [ Comprar Ahora ]  ← Active        │ (Pressed)
│  [ Comprar Ahora ]  ← Disabled      │ (Grayed)
│                                     │
└─────────────────────────────────────┘

Colors: bg-primary-500, hover:bg-primary-600
Text: text-white, font-medium
Padding: py-3 px-6 (12px 24px)
Radius: rounded-lg (8px)
Shadow: shadow-sm, hover:shadow-md
```

### **🎯 Secondary Buttons**
```
┌─────────────────────────────────────┐
│ Secondary Action Buttons            │
├─────────────────────────────────────┤
│                                     │
│  [ Ver Detalles ]   ← Secondary     │
│  [ Ver Detalles ]   ← Hover         │ (Light fill)
│  [ Ver Detalles ]   ← Active        │ (Pressed)
│  [ Ver Detalles ]   ← Disabled      │ (Grayed)
│                                     │
└─────────────────────────────────────┘

Colors: bg-white border-slate-300, hover:bg-slate-50
Text: text-slate-700, font-medium
Border: border-2
```

### **🎯 Button Sizes**
```
┌─────────────────────────────────────┐
│ Button Size Variations              │
├─────────────────────────────────────┤
│                                     │
│  [ Small ]      ← sm (py-2 px-4)    │
│  [ Medium ]     ← md (py-3 px-6)    │ Default
│  [ Large ]      ← lg (py-4 px-8)    │
│                                     │
└─────────────────────────────────────┘
```

## 📝 **Forms - Formularios**

### **📋 Input Fields**
```
┌─────────────────────────────────────┐
│ Text Input States                   │
├─────────────────────────────────────┤
│                                     │
│ Email Address                       │
│ ┌─────────────────────────────────┐ │
│ │ usuario@ejemplo.com             │ │ ← Default
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ usuario@ejemplo.com       │ ◄── │ │ ← Focus (Blue border)
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ email inválido              ⚠️  │ │ ← Error (Red border)
│ └─────────────────────────────────┘ │
│ Este campo es requerido             │
│                                     │
└─────────────────────────────────────┘

Border: border-slate-300, focus:border-primary-500
Padding: py-3 px-4 (12px 16px)
Font: text-base text-slate-900
Radius: rounded-lg (8px)
```

### **🔽 Select Dropdowns**
```
┌─────────────────────────────────────┐
│ Dropdown Select                     │
├─────────────────────────────────────┤
│                                     │
│ Categoría                           │
│ ┌─────────────────────────────────┐ │
│ │ Seleccionar categoría       ▼  │ │
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─────────────────────────────────┐ │ ← Opened
│ │ Electrónicos               ▲   │ │
│ ├─────────────────────────────────┤ │
│ │ ✓ Ropa y Accesorios            │ │ ← Selected
│ │   Hogar y Jardín                │ │
│ │   Deportes                      │ │
│ │   Libros                        │ │
│ └─────────────────────────────────┘ │
│                                     │
└─────────────────────────────────────┘
```

### **☑️ Checkboxes & Radio**
```
┌─────────────────────────────────────┐
│ Selection Controls                  │
├─────────────────────────────────────┤
│                                     │
│ ☑️ Acepto términos y condiciones    │ ← Checked
│ ☐ Suscribirse al newsletter        │ ← Unchecked
│                                     │
│ Método de pago:                     │
│ ⚫ Tarjeta de crédito              │ ← Selected
│ ⚪ PayPal                          │ ← Unselected  
│ ⚪ Transferencia bancaria          │ ← Unselected
│                                     │
└─────────────────────────────────────┘
```

## 🎴 **Cards - Tarjetas**

### **🛍️ Product Cards**
```
┌─────────────────────────────────────┐
│ Product Card Design                 │
├─────────────────────────────────────┤
│ ┌─────────────────────────────────┐ │
│ │                                 │ │
│ │    [Product Image 400x300]      │ │
│ │                                 │ │
│ └─────────────────────────────────┘ │
│                                     │
│ iPhone 15 Pro Max                   │ ← text-lg font-semibold
│ Smartphone Apple 256GB              │ ← text-sm text-slate-600
│                                     │
│ ⭐⭐⭐⭐⭐ (4.8) · 127 reseñas      │ ← Reviews
│                                     │
│ $1,299.99  💳 $109/mes              │ ← Pricing
│                                     │
│ [ 🛒 Agregar al Carrito ]          │ ← Primary CTA
│                                     │
└─────────────────────────────────────┘

Background: bg-white
Border: border border-slate-200
Radius: rounded-xl (12px)
Padding: p-6 (24px)
Shadow: shadow-sm hover:shadow-lg
Transition: transition-all duration-300
```

### **📋 Info Cards**
```
┌─────────────────────────────────────┐
│ Information Card                    │
├─────────────────────────────────────┤
│                                     │
│ 📦  Envío Gratis                    │ ← Icon + Title
│                                     │
│ En compras mayores a $500           │ ← Description
│ Entrega en 1-3 días hábiles        │
│                                     │
│ [ Ver Detalles → ]                 │ ← Action Link
│                                     │
└─────────────────────────────────────┘
```

## 🗂️ **Navigation - Navegación**

### **🔝 Header Navigation**
```
┌─────────────────────────────────────┐
│ Main Header                         │
├─────────────────────────────────────┤
│                                     │
│ 🏪 TiendaPYME  🔍[Buscar...]  👤🛒  │
│                                     │
│ 🏠 Inicio  📂 Categorías  💼 Ofertas │ ← Main Nav
│                                     │
└─────────────────────────────────────┘

Height: h-16 (64px)
Background: bg-white border-b border-slate-200
Padding: px-4 lg:px-6
```

### **📱 Mobile Navigation**
```
┌─────────────────────────────────────┐
│ Mobile Header                       │
├─────────────────────────────────────┤
│                                     │
│ ☰  TiendaPYME              🔍 🛒    │
│                                     │
└─────────────────────────────────────┘
│                                     │ ← Hamburger Menu
│ 🏠 Inicio                          │
│ 📂 Categorías                      │
│ 💼 Ofertas                         │
│ 👤 Mi Cuenta                       │
│ 📋 Mis Pedidos                     │
│ ⚙️ Configuración                   │
│ 🚪 Cerrar Sesión                   │
│                                     │
└─────────────────────────────────────┘
```

### **🍞 Breadcrumbs**
```
┌─────────────────────────────────────┐
│ Breadcrumb Navigation               │
├─────────────────────────────────────┤
│                                     │
│ Inicio > Electrónicos > Smartphones │
│        >  iPhone 15 Pro             │
│                                     │
└─────────────────────────────────────┘

Links: text-primary-600 hover:text-primary-800
Current: text-slate-900 font-medium
Separator: text-slate-400
```

## 🔔 **Feedback - Retroalimentación**

### **🚨 Alerts & Notifications**
```
┌─────────────────────────────────────┐
│ Success Alert                       │
├─────────────────────────────────────┤
│                                     │
│ ✅ Producto agregado al carrito     │ ← Success
│                                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Warning Alert                       │
├─────────────────────────────────────┤
│                                     │
│ ⚠️ Pocas unidades disponibles       │ ← Warning
│                                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Error Alert                         │
├─────────────────────────────────────┤
│                                     │
│ ❌ Error al procesar el pago        │ ← Error
│                                     │
└─────────────────────────────────────┘
```

### **💬 Toast Notifications**
```
┌─────────────────────────────────────┐
│ Toast Notification (Top Right)      │
├─────────────────────────────────────┤
│                                     │
│  ✅ Guardado exitosamente      ✖️   │
│                                     │
└─────────────────────────────────────┘

Position: fixed top-4 right-4
Background: bg-white shadow-lg
Border: border-l-4 border-success-500
Animation: slide-in from right
Auto-dismiss: 4 seconds
```

### **⏳ Loading States**
```
┌─────────────────────────────────────┐
│ Loading Spinner                     │
├─────────────────────────────────────┤
│                                     │
│        ⟳ Cargando productos...     │
│                                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Skeleton Loading                    │
├─────────────────────────────────────┤
│ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  │ ← Image
│ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓                  │ ← Title
│ ▓▓▓▓▓▓▓▓▓▓▓▓                       │ ← Description
│ ▓▓▓▓▓▓▓                            │ ← Price
└─────────────────────────────────────┘
```

## 🪟 **Modals & Overlays**

### **📱 Modal Dialog**
```
┌─────────────────────────────────────┐
│ Modal Backdrop (Semi-transparent)   │
├─────────────────────────────────────┤
│                                     │
│    ┌─────────────────────────────┐  │
│    │ Confirmar Eliminación    ✖️ │  │ ← Modal Header
│    ├─────────────────────────────┤  │
│    │                             │  │
│    │ ¿Estás seguro de que        │  │ ← Modal Content
│    │ quieres eliminar este       │  │
│    │ producto del carrito?       │  │
│    │                             │  │
│    ├─────────────────────────────┤  │
│    │        [ Cancelar ]         │  │ ← Modal Actions
│    │        [ Eliminar ]         │  │
│    └─────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘

Backdrop: bg-black bg-opacity-50
Modal: bg-white rounded-xl shadow-2xl
Max Width: max-w-md mx-auto
```

### **🎯 Dropdown Menu**
```
┌─────────────────────────────────────┐
│ Dropdown Menu                       │
├─────────────────────────────────────┤
│                                     │
│ 👤 Mi Cuenta ▼                     │
│ ┌─────────────────────────────────┐ │
│ │ 📊 Dashboard                    │ │
│ │ 📋 Mis Pedidos                  │ │
│ │ ⚙️ Configuración                │ │
│ │ ────────────────────             │ │ ← Separator
│ │ 🚪 Cerrar Sesión                │ │
│ └─────────────────────────────────┘ │
│                                     │
└─────────────────────────────────────┘

Background: bg-white shadow-lg
Border: border border-slate-200
Radius: rounded-lg
Animation: fade-in scale-95 to scale-100
```

## 📊 **Data Display**

### **📋 Tables**
```
┌─────────────────────────────────────┐
│ Data Table                          │
├─────────────────────────────────────┤
│                                     │
│ Producto          Precio    Stock   │ ← Header
│ ─────────────────────────────────── │
│ iPhone 15         $1,299    12      │ ← Row
│ Samsung Galaxy    $899      8       │
│ Google Pixel      $699      15      │
│                                     │
│ Mostrando 1-10 de 156 productos    │ ← Pagination Info
│ [ ← ] [ 1 ] [ 2 ] [ 3 ] [ → ]      │ ← Pagination
│                                     │
└─────────────────────────────────────┘
```

### **📈 Progress Indicators**
```
┌─────────────────────────────────────┐
│ Progress Bars                       │
├─────────────────────────────────────┤
│                                     │
│ Progreso del pedido: 75%            │
│ ████████████████████▒▒▒▒▒▒▒▒        │
│                                     │
│ Carga del archivo: 45%              │
│ ████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒        │
│                                     │
└─────────────────────────────────────┘
```

## 🎯 **Interactive Elements**

### **⭐ Rating Component**
```
┌─────────────────────────────────────┐
│ Product Rating                      │
├─────────────────────────────────────┤
│                                     │
│ ⭐⭐⭐⭐⭐ 4.8 de 5                  │ ← Stars + Score
│ Basado en 127 reseñas              │ ← Review Count
│                                     │
│ 5★ ████████████████████ 78%        │ ← Rating Breakdown
│ 4★ ████████████▒▒▒▒▒▒▒▒ 15%        │
│ 3★ ████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 5%         │
│ 2★ ██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 1%         │
│ 1★ ██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 1%         │
│                                     │
└─────────────────────────────────────┘
```

### **🏷️ Tags & Badges**
```
┌─────────────────────────────────────┐
│ Tags and Badges                     │
├─────────────────────────────────────┤
│                                     │
│ [ Nuevo ] [ En Oferta ] [ Popular ] │ ← Product Tags
│                                     │
│ Stock: [ 15 ] Vendidos: [ 1.2K ]   │ ← Status Badges
│                                     │
│ Categorías:                         │
│ [ Electrónicos ] [ Smartphones ]    │ ← Category Tags
│ [ Apple ] [ 256GB ]                 │
│                                     │
└─────────────────────────────────────┘

Border: border border-current
Radius: rounded-full
Padding: px-3 py-1
Font: text-xs font-medium
```

---

*🧩 Component Library v1.0 - Modern E-Commerce Design*
