# 🛒 Shopping Cart - Carrito de Compras

## 📋 Descripción

Diseño del sistema de carrito de compras con gestión de productos, cálculos dinámicos, opciones de envío y experiencia optimizada para conversión.

## 🎯 **Desktop Layout - Shopping Cart**

### **🛒 Cart Header & Summary**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│ Header Navigation                                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ Inicio > Carrito de Compras                                                │ ← Breadcrumbs
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                            🛒 MI CARRITO                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  3 productos en tu carrito                                                 │ ← Item count
│                                                                             │
│  [ ← Continuar comprando ]                              [ 🗑️ Vaciar carrito ] │ ← Actions
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Title: Large heading with cart icon
Count: Number of items with plural handling
Actions: Continue shopping + clear cart options
Navigation: Clear breadcrumbs for context
```

### **📦 Cart Items List**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                               PRODUCTOS                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ ┌─ ITEM 1 ────────────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ ┌─────────┐  iPhone 15 Pro                                      [ ✕ ]  │ │ ← Remove button
│ │ │         │  256GB - Titanio Natural                                   │ │
│ │ │ [Image] │                                                            │ │
│ │ │ 150x150 │  ⭐⭐⭐⭐⭐ 4.8 (127 reseñas)                            │ │
│ │ │         │                                                            │ │
│ │ └─────────┘  🏷️ SKU: IPH15P-256-TIT                                   │ │
│ │              📦 En stock (15 disponibles)                              │ │
│ │              🚚 Envío gratis                                           │ │
│ │                                                                         │ │
│ │              Cantidad: [ ➖ ] [ 1 ] [ ➕ ]                             │ │ ← Quantity controls
│ │                                                                         │ │
│ │              Precio: $1,299.99  Total: $1,299.99                      │ │
│ │              💳 Financiamiento: $109/mes x 12 cuotas                   │ │
│ │                                                                         │ │
│ │              [ ❤️ Guardar para después ] [ 🔄 Actualizar ]             │ │ ← Secondary actions
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ ITEM 2 ────────────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ ┌─────────┐  MacBook Air M2                                     [ ✕ ]  │ │
│ │ │         │  13" 512GB Midnight                                        │ │
│ │ │ [Image] │                                                            │ │
│ │ │ 150x150 │  ⭐⭐⭐⭐⭐ 4.9 (89 reseñas)                             │ │
│ │ │         │                                                            │ │
│ │ └─────────┘  🏷️ SKU: MBA13-512-MID                                    │ │
│ │              📦 En stock (8 disponibles)                               │ │
│ │              🚚 Envío gratis                                           │ │
│ │                                                                         │ │
│ │              Cantidad: [ ➖ ] [ 1 ] [ ➕ ]                             │ │
│ │                                                                         │ │
│ │              Precio: $1,199.99  Total: $1,199.99                      │ │
│ │              💳 Financiamiento: $100/mes x 12 cuotas                   │ │
│ │                                                                         │ │
│ │              [ ❤️ Guardar para después ] [ 🔄 Actualizar ]             │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ ITEM 3 ────────────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ ┌─────────┐  Impresora Canon                                    [ ✕ ]  │ │
│ │ │         │  PIXMA G3260 Multifuncional                                │ │
│ │ │ [Image] │                                                            │ │
│ │ │ 150x150 │  ⭐⭐⭐⭐⭐ 4.7 (156 reseñas)                            │ │
│ │ │         │                                                            │ │
│ │ └─────────┘  🏷️ SKU: CAN-G3260-BLK                                    │ │
│ │              📦 En stock (23 disponibles)                              │ │
│ │              🚚 Envío gratis                                           │ │
│ │                                                                         │ │
│ │              Cantidad: [ ➖ ] [ 2 ] [ ➕ ]                             │ │
│ │                                                                         │ │
│ │              Precio: $299.99  Total: $599.98                          │ │
│ │              💳 Financiamiento: $50/mes x 12 cuotas                    │ │
│ │                                                                         │ │
│ │              [ ❤️ Guardar para después ] [ 🔄 Actualizar ]             │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Layout: Stacked item cards with consistent spacing
Image: Square thumbnail (150x150px)
Info: Product name, variant, rating, SKU, stock
Controls: Quantity selectors with validation
Pricing: Unit price + line total + financing
Actions: Remove, save for later, update quantity
```

### **💳 Order Summary Sidebar**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              RESUMEN DEL PEDIDO                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ ┌─ TOTALES ───────────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ Subtotal (4 productos):                               $3,099.96        │ │
│ │ Descuentos aplicados:                                  -$0.00          │ │
│ │ ────────────────────────────────────────────────────────────────────── │ │
│ │ Envío:                                         Gratis ✅ (Ahorro $45)  │ │
│ │ Impuestos (IVA 21%):                                   $650.99         │ │
│ │ ────────────────────────────────────────────────────────────────────── │ │
│ │ TOTAL:                                                 $3,750.95       │ │
│ │                                                                         │ │
│ │ 💳 Financiamiento disponible:                                          │ │
│ │ 12 cuotas de $312.58/mes sin interés                                   │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ CUPONES Y DESCUENTOS ──────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ 🎟️ Código de descuento                                                 │ │
│ │ ┌─────────────────────────────────────────────┐                       │ │
│ │ │ Ingresa tu código promocional...            │                       │ │
│ │ └─────────────────────────────────────────────┘ [ Aplicar ]           │ │
│ │                                                                         │ │
│ │ Descuentos disponibles:                                                 │ │
│ │ • 🎯 PYME15: 15% descuento en tu primera compra                        │ │
│ │ • 🚚 ENVIO50: Envío gratis en compras +$500                           │ │ ← Auto-applied
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ OPCIONES DE ENVÍO ─────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ ⚫ Envío estándar gratis (1-3 días)                            Gratis   │ │ ← Selected
│ │ ⚪ Envío express (24 horas)                                    $25.99   │ │
│ │ ⚪ Retiro en tienda (Disponible hoy)                           Gratis   │ │
│ │                                                                         │ │
│ │ 📍 Envío a: Buenos Aires, CABA (1234)               [ Cambiar ]        │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ ACCIONES ──────────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │            [ 💳 PROCEDER AL PAGO ]                                     │ │ ← Primary CTA
│ │            [ 💾 Guardar Carrito ]                                      │ │
│ │            [ 📧 Enviar por Email ]                                     │ │
│ │                                                                         │ │
│ │ 🔒 Compra 100% segura                                                  │ │
│ │ 🛡️ Garantía de devolución 30 días                                     │ │
│ │ 📞 Soporte 24/7                                                        │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Layout: Sticky sidebar (Desktop), bottom section (Mobile)
Calculations: Real-time updates with taxes
Coupons: Input field with suggestions
Shipping: Radio buttons with pricing
Trust: Security badges and guarantees
CTA: Large, prominent checkout button
```

## 📱 **Mobile Layout - Shopping Cart**

### **📱 Mobile Cart View**
```
┌─────────────────────────────────┐
│ ☰ TiendaPYME           🔍 🛒   │ ← Header
├─────────────────────────────────┤
│ Inicio > Carrito                │ ← Breadcrumbs
├─────────────────────────────────┤
│                                 │
│         🛒 MI CARRITO           │
│                                 │
│        3 productos              │
│                                 │
├─────────────────────────────────┤
│              PRODUCTOS          │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │ ┌─────┐ iPhone 15 Pro    ✕ │ │
│ │ │     │ 256GB Titanio       │ │
│ │ │ Img │ ⭐⭐⭐⭐⭐ (4.8)      │ │
│ │ │     │                     │ │
│ │ └─────┘ $1,299.99           │ │
│ │         💳 $109/mes         │ │
│ │                             │ │
│ │ [ ➖ ] [ 1 ] [ ➕ ]         │ │ ← Quantity
│ │                             │ │
│ │ [ ❤️ ] [ 🔄 ]                │ │ ← Actions
│ │                             │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ ┌─────┐ MacBook Air      ✕ │ │
│ │ │     │ M2 13" 512GB        │ │
│ │ │ Img │ ⭐⭐⭐⭐⭐ (4.9)      │ │
│ │ │     │                     │ │
│ │ └─────┘ $1,199.99           │ │
│ │         💳 $100/mes         │ │
│ │                             │ │
│ │ [ ➖ ] [ 1 ] [ ➕ ]         │ │
│ │                             │ │
│ │ [ ❤️ ] [ 🔄 ]                │ │
│ │                             │ │
│ └─────────────────────────────┘ │
│                                 │
├─────────────────────────────────┤
│             RESUMEN             │
├─────────────────────────────────┤
│                                 │
│ Subtotal:              $3,099.96│
│ Envío:                    Gratis│
│ Impuestos:               $650.99│ 
│ ─────────────────────────────── │
│ TOTAL:                 $3,750.95│
│                                 │
│ 💳 12 cuotas de $312.58/mes     │
│                                 │
│ 🎟️ [ Código descuento... ]      │
│                                 │
│ 🚚 Envío: [ Estándar gratis ▼ ] │
│                                 │
│ ┌─────────────────────────────┐ │
│ │    💳 PROCEDER AL PAGO      │ │ ← Large CTA
│ └─────────────────────────────┘ │
│                                 │
│ [ ← Continuar comprando ]       │
│                                 │
└─────────────────────────────────┘

Layout: Single column, stacked cards
Items: Horizontal layout with thumbnail
Summary: Collapsed by default, expandable
CTA: Full-width button for easy tap
Actions: Prominent and touch-friendly
```

### **📱 Mobile Quantity Selector**
```
┌─────────────────────────────────┐
│         CANTIDAD                │ ← Modal overlay
├─────────────────────────────────┤
│                                 │
│        iPhone 15 Pro            │
│                                 │
│    Selecciona la cantidad:      │
│                                 │
│  ┌─────┐ ┌─────┐ ┌─────┐       │
│  │  1  │ │  2  │ │  3  │       │ ← Number grid
│  └─────┘ └─────┘ └─────┘       │
│                                 │
│  ┌─────┐ ┌─────┐ ┌─────┐       │
│  │  4  │ │  5  │ │  6  │       │
│  └─────┘ └─────┘ └─────┘       │
│                                 │
│  ┌─────┐ ┌─────┐ ┌─────┐       │
│  │  7  │ │  8  │ │  9  │       │
│  └─────┘ └─────┘ └─────┘       │
│                                 │
│     [ Cantidad personalizada ]  │
│                                 │
│ ┌─────────────────────────────┐ │
│ │         CONFIRMAR           │ │
│ └─────────────────────────────┘ │
│                                 │
│           [ Cancelar ]          │
│                                 │
└─────────────────────────────────┘

Overlay: Bottom sheet for quantity selection
Grid: Large touch targets for numbers
Custom: Input for quantities over 9
Actions: Confirm and cancel buttons
```

## 🛒 **Empty Cart State**

### **📭 Empty Cart Design**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                               CARRITO VACÍO                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                                                                             │
│                                🛒                                          │ ← Large cart icon
│                                                                             │
│                    Tu carrito está vacío                                   │
│                                                                             │
│              Agrega productos para comenzar tu compra                      │
│                                                                             │
│                     [ 🛍️ Explorar Productos ]                             │ ← Primary CTA
│                                                                             │
│                                                                             │
│  ┌─ PRODUCTOS RECOMENDADOS ────────────────────────────────────────────────┐ │
│  │                                                                         │ │
│  │ Basado en tus búsquedas recientes:                                      │ │
│  │                                                                         │ │
│  │ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐ │ │
│  │ │ [iPhone]    │ │ [MacBook]   │ │ [Impresora] │ │ [Más productos]     │ │ │
│  │ │ desde $999  │ │ desde $1199 │ │ desde $299  │ │                     │ │ │
│  │ └─────────────┘ └─────────────┘ └─────────────┘ └─────────────────────┘ │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│  ┌─ ÚLTIMOS PRODUCTOS VISTOS ──────────────────────────────────────────────┐ │
│  │                                                                         │ │
│  │ Productos que viste recientemente:                                      │ │
│  │                                                                         │ │
│  │ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                       │ │
│  │ │ [Galaxy S24]│ │ [iPad Pro]  │ │ [AirPods]   │                       │ │
│  │ │ $1,299      │ │ $1,099      │ │ $249        │                       │ │
│  │ │ [+ Agregar] │ │ [+ Agregar] │ │ [+ Agregar] │                       │ │
│  │ └─────────────┘ └─────────────┘ └─────────────┘                       │ │
│  │                                                                         │ │
│  └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Icon: Large, friendly cart illustration
Message: Clear, helpful text
CTA: Prominent explore products button
Recommendations: Based on browsing history
Recently Viewed: Quick add to cart options
```

## 🎯 **Cart Interactions & Features**

### **⚡ Real-time Updates**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ACTUALIZACIONES EN TIEMPO REAL                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ Quantity Change:                                                            │
│ [ ➖ ] [ 2 ] [ ➕ ] → [ ➖ ] [ 3 ] [ ➕ ]                                   │ ← Instant update
│                                                                             │
│ Price Update (animated):                                                    │
│ $599.98 → 🔄 → $899.97                                                     │ ← Number animation
│                                                                             │
│ Total Recalculation:                                                        │
│ Subtotal: $3,099.96 → $3,399.95                                           │
│ Taxes: $650.99 → $713.99                                                  │
│ TOTAL: $3,750.95 → $4,113.94                                              │
│                                                                             │
│ Stock Validation:                                                           │
│ ⚠️ Solo quedan 2 unidades disponibles                                      │ ← Warning message
│ [ Actualizar cantidad ] [ Eliminar producto ]                              │
│                                                                             │
│ Success Feedback:                                                           │
│ ✅ Carrito actualizado correctamente                                        │ ← Toast notification
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Updates: Instant price recalculations
Animation: Smooth number transitions
Validation: Real-time stock checking
Feedback: Success/error notifications
Persistence: Auto-save cart state
```

### **💾 Save for Later**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              GUARDADOS PARA DESPUÉS                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ ┌─ ITEM GUARDADO ─────────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ ┌─────────┐  iPad Pro 11"                                               │ │
│ │ │         │  256GB WiFi Space Gray                                      │ │
│ │ │ [Image] │                                                             │ │
│ │ │         │  ⭐⭐⭐⭐⭐ 4.9 (234 reseñas)                               │ │
│ │ └─────────┘                                                             │ │
│ │              $899.99 · 📦 En stock                                      │ │
│ │                                                                         │ │
│ │              Guardado el 15/10/2025                                     │ │ ← Saved date
│ │                                                                         │ │
│ │              [ 🛒 Mover al Carrito ] [ ❌ Eliminar ]                    │ │ ← Actions
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│ ┌─ PRECIO MEJORADO ───────────────────────────────────────────────────────┐ │
│ │                                                                         │ │
│ │ 🔔 ¡Precio rebajado!                                                   │ │
│ │                                                                         │ │
│ │ AirPods Pro (2da Gen)                                                   │ │
│ │ Precio anterior: $249.99                                                │ │
│ │ Precio actual: $199.99 🔥 ¡Ahorra $50!                                 │ │ ← Price alert
│ │                                                                         │ │
│ │ [ 🛒 Agregar al Carrito ] [ 📧 Recordarme ]                            │ │
│ │                                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Features: Save items for later consideration
Notifications: Price drop alerts
Actions: Move back to cart or remove
Date: When item was saved
Status: Current stock and price
```

### **🔔 Cart Notifications**
```
Cart Abandonment (Email):
"¡No olvides tus productos! 
Tu carrito te está esperando con $3,750 en productos increíbles.
[Completar Compra] [Ver Carrito]"

Stock Alert:
"⚠️ Uno de tus productos tiene pocas unidades disponibles.
iPhone 15 Pro - Solo quedan 3 unidades"

Price Change:
"📉 Buenas noticias! El precio de MacBook Air M2 bajó de $1,199 a $1,099"

Back in Stock:
"📦 ¡Ya está disponible! AirPods Pro volvió a estar en stock"
```

## 🎨 **Visual Design Elements**

### **🎨 Color Coding**
```
Success Actions:    Green (#10b981) - Add to cart, proceed
Warning States:     Amber (#f59e0b) - Low stock, price alerts
Error States:       Red (#ef4444) - Out of stock, remove
Info Elements:      Blue (#3b82f6) - Shipping, financing
Neutral Actions:    Gray (#6b7280) - Secondary actions
```

### **📐 Spacing & Layout**
```
Cart Item Padding:    24px (Desktop), 16px (Mobile)
Item Spacing:         16px between items
Section Spacing:      32px between sections
Button Spacing:       12px between actions
Sidebar Width:        360px (Desktop)
Mobile Breakpoint:    768px
```

### **⚡ Animations**
```
Quantity Change:      200ms ease-out
Price Update:         300ms ease-in-out with number counting
Add to Cart:          Scale + fade animation
Remove Item:          Slide out + fade (400ms)
Loading States:       Skeleton loading during updates
Success Feedback:     Bounce animation + color change
```

---

*🛒 Shopping Cart v1.0 - Modern E-Commerce Design*
