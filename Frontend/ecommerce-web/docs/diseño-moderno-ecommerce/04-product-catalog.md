# 🛍️ Product Catalog - Catálogo de Productos

## 📋 Descripción

Diseño del sistema de catálogo de productos con navegación por categorías, filtros avanzados, búsqueda y listados optimizados para descubrimiento y conversión.

## 🎯 **Desktop Layout - Product Listing**

### **📂 Category Navigation**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│ Header + Breadcrumbs                                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ Inicio > Electrónicos > Smartphones                                        │ ← Breadcrumbs
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                              SMARTPHONES                                   │ ← Category Header
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  📱 Encuentra el smartphone perfecto para tu empresa                       │
│                                                                             │
│  Categorías relacionadas:                                                  │
│  [ iPhone ] [ Samsung ] [ Google Pixel ] [ Xiaomi ] [ OnePlus ]           │ ← Sub-categories
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Category Header: Large title + description
Sub-categories: Horizontal scrollable tags
Colors: Primary brand colors for active states
Typography: Large heading (text-3xl), description (text-lg)
```

### **🔍 Search & Filters Section**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            BÚSQUEDA Y FILTROS                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ ┌─ FILTROS ──────────────┐  ┌─ BÚSQUEDA Y RESULTADOS ─────────────────────┐ │
│ │                        │  │                                             │ │
│ │ 🔍 Buscar en categoría │  │ 🔍 [ Buscar smartphones...          ] 🔍   │ │
│ │ ┌────────────────────┐ │  │                                             │ │
│ │ │ iPhone 15...       │ │  │ 📊 Mostrando 24 de 156 productos          │ │
│ │ └────────────────────┘ │  │                                             │ │
│ │                        │  │ Ordenar por: [ Más Relevantes ▼ ]         │ │
│ │ 💰 Rango de Precio     │  │ Vista: [ 🔲 ] [ ▤ ] [ ☰ ]                 │ │ ← View toggle
│ │ ┌──────┐ ┌──────┐     │  │                                             │ │
│ │ │ $100 │ │$2000 │     │ │  │ Filtros activos:                           │ │
│ │ └──────┘ └──────┘     │ │  │ [ iPhone ✕ ] [ $500-$1500 ✕ ] [ 5G ✕ ]  │ │
│ │ ████████████████▒▒▒   │ │  │                                             │ │
│ │                        │  │                                             │ │
│ │ 🏷️ Marca               │  └─────────────────────────────────────────────┘ │
│ │ ☑️ Apple (45)          │                                                  │
│ │ ☑️ Samsung (32)        │                                                  │
│ │ ☐ Google (18)          │                                                  │
│ │ ☐ Xiaomi (12)          │                                                  │
│ │                        │                                                  │
│ │ 📱 Características     │                                                  │
│ │ ☑️ 5G (89)             │                                                  │
│ │ ☑️ Dual SIM (67)       │                                                  │
│ │ ☐ Wireless Charge (45) │                                                  │
│ │                        │                                                  │
│ │ ⭐ Calificación         │                                                  │
│ │ ☑️ 4+ estrellas (98)   │                                                  │
│ │ ☐ 3+ estrellas (124)   │                                                  │
│ │                        │                                                  │
│ │ 📦 Disponibilidad      │                                                  │
│ │ ☑️ En stock (142)      │                                                  │
│ │ ☐ Próximamente (14)    │                                                  │
│ │                        │                                                  │
│ │ [ Limpiar Filtros ]    │                                                  │
│ │                        │                                                  │
│ └────────────────────────┘                                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Layout: Sidebar (300px) + Main content (flex-1)
Filters: Collapsible sections with checkboxes
Price Range: Dual slider component
Active Filters: Removable tags above results
Responsive: Sidebar collapses to overlay on mobile
```

### **📦 Product Grid Layout**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              PRODUCTOS                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│ ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ ┌──────────── │
│ │ 🏷️ OFERTA       │ │                 │ │ 🆕 NUEVO        │ │ 🔥 POPULAR │ ← Badges
│ │                 │ │                 │ │                 │ │             │
│ │  [iPhone Image] │ │ [Samsung Image] │ │ [Pixel Image]   │ │ [OnePlus Im │
│ │                 │ │                 │ │                 │ │             │
│ │ iPhone 15 Pro   │ │ Galaxy S24      │ │ Pixel 8 Pro     │ │ OnePlus 12  │
│ │ 256GB Titanio   │ │ Ultra 512GB     │ │ 128GB Obsidian  │ │ 256GB Green │
│ │                 │ │                 │ │                 │ │             │
│ │ ⭐⭐⭐⭐⭐ (4.8)   │ │ ⭐⭐⭐⭐⭐ (4.7)   │ │ ⭐⭐⭐⭐⭐ (4.6)   │ │ ⭐⭐⭐⭐⭐ (4 │
│ │ 127 reseñas     │ │ 89 reseñas      │ │ 45 reseñas      │ │ 78 reseñas  │
│ │                 │ │                 │ │                 │ │             │
│ │ $1,099.99       │ │ $1,299.99       │ │ $899.99         │ │ $799.99     │
│ │ $1,299.99 ✂️15% │ │ 💳 $108/mes     │ │ 💳 $75/mes      │ │ 💳 $67/mes  │
│ │                 │ │                 │ │                 │ │             │
│ │ 📦 Stock: 15    │ │ 📦 Stock: 8     │ │ 📦 Stock: 23    │ │ 📦 Stock: 5 │
│ │ 🚚 Envío gratis │ │ 🚚 Envío gratis │ │ 🚚 Envío gratis │ │ 🚚 Envío gr │
│ │                 │ │                 │ │                 │ │             │
│ │ [ 🛒 Agregar ]  │ │ [ 🛒 Agregar ]  │ │ [ 🛒 Agregar ]  │ │ [ 🛒 Agreg │
│ │ [ 👁️ Ver Más ]   │ │ [ 👁️ Ver Más ]   │ │ [ 👁️ Ver Más ]   │ │ [ 👁️ Ver M │
│ │ [ ❤️ Guardar ]   │ │ [ ❤️ Guardar ]   │ │ [ ❤️ Guardar ]   │ │ [ ❤️ Guarda │
│ │                 │ │                 │ │                 │ │             │
│ └─────────────────┘ └─────────────────┘ └─────────────────┘ └──────────── │
│                                                                             │
│ ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ ┌──────────── │
│ │  [More cards]   │ │  [More cards]   │ │  [More cards]   │ │  [More card │
│ │                 │ │                 │ │                 │ │             │
│ │ [Second row]    │ │ [Second row]    │ │ [Second row]    │ │ [Second row │
│ │                 │ │                 │ │                 │ │             │
│ └─────────────────┘ └─────────────────┘ └─────────────────┘ └──────────── │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Grid: 4 columns (Desktop), 2 columns (Tablet), 1 column (Mobile)
Card Size: 280px width (Desktop), full width (Mobile)
Hover Effects: Scale, shadow increase, quick view option
Quick Actions: Add to cart, view details, save to wishlist
```

### **📖 Pagination & Load More**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                               PAGINACIÓN                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Mostrando 1-24 de 156 productos                                          │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │        [ ← Anterior ] [ 1 ] [ 2 ] [ 3 ] ... [ 7 ] [ Siguiente → ]  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│                    [ 📦 Cargar más productos ]                            │ ← Alternative
│                                                                             │
│                    Scroll infinito disponible                              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Options: Traditional pagination, load more button, infinite scroll
Current Page: Highlighted in primary color
Navigation: Previous/Next with icons
Info: Shows current range and total count
```

## 📱 **Mobile Layout - Product Catalog**

### **📱 Mobile Category Navigation**
```
┌─────────────────────────────────┐
│ ☰ TiendaPYME           🔍 🛒   │ ← Header
├─────────────────────────────────┤
│ Inicio > Electrónicos > Móviles │ ← Breadcrumbs
├─────────────────────────────────┤
│                                 │
│        📱 SMARTPHONES           │ ← Category title
│                                 │
│ ← → [ iPhone Samsung Pixel ]    │ ← Horizontal scroll
│                                 │
├─────────────────────────────────┤
│          FILTROS Y ORDEN        │
├─────────────────────────────────┤
│                                 │
│ 🔍 [ Buscar en categoría... ]   │
│                                 │
│ [ 🎛️ Filtros (3) ] [ 📊 Ordenar ] │ ← Filter & Sort buttons
│                                 │
│ Activos: [ iPhone ✕ ] [ 5G ✕ ] │ ← Active filters
│                                 │
├─────────────────────────────────┤
│            PRODUCTOS            │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │ 🏷️ OFERTA     [ ❤️ ]        │ │
│ │                             │ │
│ │      [iPhone Image]         │ │
│ │                             │ │
│ │ iPhone 15 Pro 256GB         │ │
│ │ ⭐⭐⭐⭐⭐ (4.8) · 127       │ │
│ │                             │ │
│ │ $1,099.99 ✂️ $1,299.99      │ │
│ │ 💳 $92/mes · 📦 Stock: 15   │ │
│ │                             │ │
│ │ [ 🛒 Agregar al Carrito ]   │ │
│ │ [ 👁️ Ver Detalles ]         │ │
│ │                             │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │      [Next Product]         │ │
│ └─────────────────────────────┘ │
│                                 │
│        [ Cargar más ]           │
│                                 │
└─────────────────────────────────┘

Layout: Single column cards
Filters: Bottom sheet overlay
Sort: Dropdown from header
Cards: Full width with horizontal layout option
```

### **🎛️ Mobile Filter Overlay**
```
┌─────────────────────────────────┐
│           FILTROS              ✕│ ← Bottom sheet overlay
├─────────────────────────────────┤
│                                 │
│ 💰 Precio                      │
│ ┌─────┐ a ┌─────┐              │
│ │ $100│   │$2000│              │
│ └─────┘   └─────┘              │
│ ████████████████▒▒▒▒▒          │
│                                 │
│ 🏷️ Marca                       │
│ ☑️ Apple (45)                  │
│ ☑️ Samsung (32)                │
│ ☐ Google (18)                  │
│ ☐ Xiaomi (12)                  │
│ [ Ver más marcas ▼ ]           │
│                                 │
│ 📱 Características              │
│ ☑️ 5G                          │
│ ☑️ Dual SIM                    │
│ ☐ Carga inalámbrica            │
│                                 │
│ ⭐ Calificación                 │
│ ☑️ 4+ estrellas                │
│ ☐ 3+ estrellas                 │
│                                 │
│ ┌─────────────────────────────┐ │
│ │  [ Limpiar ] [ Aplicar (3) ]│ │ ← Action buttons
│ └─────────────────────────────┘ │
│                                 │
└─────────────────────────────────┘

Overlay: Slides up from bottom
Sections: Collapsible with counts
Actions: Clear all filters, apply with count
Backdrop: Dismiss on tap outside
```

## 🎯 **Product Quick View Modal**

### **👁️ Quick View Overlay**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            VISTA RÁPIDA                                ✕   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────┐  │
│  │                             │  │ iPhone 15 Pro                       │  │
│  │      [Product Gallery]      │  │ 256GB - Titanio Natural             │  │
│  │                             │  │                                     │  │
│  │  ┌─┐ ┌─┐ ┌─┐ ┌─┐ ┌─┐       │  │ ⭐⭐⭐⭐⭐ 4.8 (127 reseñas)         │  │
│  │  │1│ │2│ │3│ │4│ │5│       │  │                                     │  │
│  │  └─┘ └─┘ └─┘ └─┘ └─┘       │  │ $1,299.99                           │  │
│  │                             │  │ 💳 Desde $109/mes                   │  │
│  │  [🔍] [📷] [❤️]             │  │                                     │  │
│  │                             │  │ Color: [ Titanio ] [ Azul ] [ Negro]│  │
│  │                             │  │ Almacenamiento: [ 128GB ] [256GB]   │  │ ← Options
│  │                             │  │                                     │  │
│  └─────────────────────────────┘  │ 📦 Stock disponible (15 unidades)  │  │
│                                   │ 🚚 Envío gratis en 1-3 días        │  │
│                                   │ 🔒 Garantía Apple 1 año            │  │
│                                   │                                     │  │
│                                   │ Características principales:        │  │
│                                   │ • Chip A17 Pro                      │  │
│                                   │ • Cámara Pro 48MP                   │  │
│                                   │ • Pantalla Super Retina XDR         │  │
│                                   │ • Titanio grado aeroespacial        │  │
│                                   │                                     │  │
│                                   │ [ 🛒 Agregar al Carrito ]          │  │
│                                   │ [ 👁️ Ver Detalles Completos ]      │  │
│                                   │                                     │  │
│                                   └─────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Layout: Two columns (50% image, 50% details)
Gallery: Main image + thumbnails below
Options: Color and storage variants
Actions: Add to cart (primary), view full details
Responsive: Single column on mobile
```

## 🔍 **Advanced Search Results**

### **🎯 Search Results Layout**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        RESULTADOS PARA "IPHONE 15"                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Encontrados 23 productos · Búsqueda realizada en 0.15s                   │
│                                                                             │
│  ¿Quisiste decir: [ iPhone 14 ] [ iPhone 13 ] [ Samsung S24 ]             │ ← Suggestions
│                                                                             │
│  ┌─ PRODUCTOS ──────────────────┐  ┌─ FILTRAR RESULTADOS ──────────────┐   │
│  │                              │  │                                   │   │
│  │ [ Standard Product Grid ]    │  │ 🏷️ Por marca:                     │   │
│  │                              │  │ Apple (18) Samsung (3) Google (2) │   │
│  │ [iPhone 15 Pro cards]        │  │                                   │   │
│  │ [iPhone 15 cards]            │  │ 💰 Por precio:                    │   │
│  │ [iPhone 15 Plus cards]       │  │ $500-$800 (5)                    │   │
│  │                              │  │ $800-$1200 (12)                  │   │
│  │                              │  │ $1200+ (6)                       │   │
│  │                              │  │                                   │   │
│  └──────────────────────────────┘  │ 📱 Por modelo:                    │   │
│                                    │ iPhone 15 Pro (8)                │   │
│                                    │ iPhone 15 (10)                   │   │
│                                    │ iPhone 15 Plus (5)               │   │
│                                    │                                   │   │
│                                    └───────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Search Info: Number of results + search time
Suggestions: Related terms and alternatives
Filters: Specific to search results
Layout: Same grid as category pages
```

## 🎨 **Visual States & Interactions**

### **⚡ Loading States**
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              ESTADOS DE CARGA                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─ SKELETON LOADING ─────────────────────────────────────────────────────┐ │
│  │                                                                        │ │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│ │ ← Product cards
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓   ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│ │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓   ▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│ │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓│ │
│  │                                                                        │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
│  ┌─ FILTER LOADING ───────────────────────────────────────────────────────┐ │
│  │                                                                        │ │
│  │ 🔄 Aplicando filtros...                                               │ │
│  │                                                                        │ │
│  │ ████████████████████████████████████████████████████████████ 85%      │ │ ← Progress bar
│  │                                                                        │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

Skeleton: Animated gray blocks matching content layout
Progress: Shows filter application progress
Spinner: For quick actions like add to cart
Fade: Smooth transitions between states
```

### **🎯 Hover & Focus States**
```
Product Card Hover:
- Scale: transform scale-105
- Shadow: shadow-lg
- Border: border-primary-200
- CTA: Show quick actions (add to cart, wishlist)

Filter Hover:
- Background: bg-primary-50
- Text: text-primary-700
- Icon: Subtle animation

Button Focus:
- Ring: ring-2 ring-primary-500
- Offset: ring-offset-2
- No outline: outline-none
```

## 📊 **Performance Optimizations**

### **⚡ Rendering Strategy**
```
Image Loading:     Lazy loading with blur placeholder
Virtual Scrolling: For large product lists (500+ items)
Pagination:        Server-side with prefetch next page
Filters:          Client-side with debounced search
Cache Strategy:    Cache filtered results for 5 minutes
```

### **📱 Mobile Optimizations**
```
Touch Targets:     Minimum 44px for all interactive elements
Scroll Performance: Hardware acceleration for smooth scrolling
Image Compression: WebP format with fallbacks
Bundle Size:       Code splitting by route
Offline Support:   Cache critical product data
```

---

*🛍️ Product Catalog v1.0 - Modern E-Commerce Design*
