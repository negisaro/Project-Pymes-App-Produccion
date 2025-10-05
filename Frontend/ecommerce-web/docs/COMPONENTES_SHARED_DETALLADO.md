# Componentes para Extracción a Shared - Análisis Detallado

## 📋 **Resumen Ejecutivo**

Este documento especifica exactamente qué componentes extraer del módulo `categoria/` hacia `shared/` para crear una arquitectura reutilizable y profesional. Cada componente está identificado con ubicación exacta, código específico y propósito.

---

## 🎯 **Componentes Identificados para Extracción**

### **1. 📊 Sistema de Paginación**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 30-35 (variables) + 530-580 (métodos)

**🔧 Código a extraer:**
```typescript
// Variables de paginación
page = 0;
size = 8;
totalPages = 0;
totalElements = 0;

// Métodos de paginación
cambiarTamañoPagina(): void {
  if (this.loading) return;
  this.page = 0;
  this.cargarCategorias();
}

irAPagina(pagina: number): void {
  if (this.loading || pagina < 0 || pagina >= this.totalPages) return;
  this.page = pagina;
  this.cargarCategorias();
}

siguientePagina(): void {
  if (this.loading || this.page >= this.totalPages - 1) return;
  this.page++;
  this.cargarCategorias();
}

paginaAnterior(): void {
  if (this.loading || this.page <= 0) return;
  this.page--;
  this.cargarCategorias();
}

getPaginasVisibles(): number[] {
  const rango = 5;
  const inicio = Math.max(0, this.page - Math.floor(rango / 2));
  const fin = Math.min(this.totalPages, inicio + rango);
  return Array.from({ length: fin - inicio }, (_, i) => inicio + i);
}
```

**🎯 Destino:** `shared/components/pagination/pagination.component.ts`

**📋 Interface genérica:**
```typescript
export interface PaginationConfig {
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
  loading?: boolean;
}
```

---

### **2. 🔍 Sistema de Filtros**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 40-55 (variables) + métodos dispersos

**🔧 Código a extraer:**
```typescript
// Variables de filtros
filtros = {
  busqueda: '',
  estado: '',
  tipo: '',
  nivel: '',
  activo: ''
};
filtersExpanded = false;

// Métodos de filtros
onBuscar(): void {
  // Búsqueda con debounce automático
  if (this.filtros.busqueda.length >= 2 || this.filtros.busqueda.length === 0) {
    this.page = 0;
    this.aplicarFiltros();
  }
}

aplicarFiltros(): void {
  this.page = 0;
  this.cargarCategorias();
}

limpiarFiltros(): void {
  this.filtros = { busqueda: '', estado: '', tipo: '', nivel: '', activo: '' };
  this.page = 0;
  this.cargarCategorias();
}

toggleFilters(): void {
  this.filtersExpanded = !this.filtersExpanded;
}
```

**🎯 Destino:** `shared/components/filters-panel/filters-panel.component.ts`

**📋 Interface genérica:**
```typescript
export interface FilterConfig {
  [key: string]: string | number | boolean;
}

export interface FilterField {
  key: string;
  label: string;
  type: 'text' | 'select' | 'checkbox';
  options?: { value: any; label: string }[];
}
```

---

### **3. ⏳ Loading Manager**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 25-29 (variables) + 90-120 (métodos timeout)

**🔧 Código a extraer:**
```typescript
// Variables loading
loading = false;
private loadingTimeout: any = null;

// Métodos de timeout de seguridad
private iniciarTimeoutSeguridad(): void {
  this.limpiarTimeoutSeguridad();
  this.loadingTimeout = setTimeout(() => {
    if (this.loading) {
      console.warn('[LOADING] ⚠️ Timeout de seguridad activado');
      this.loading = false;
      this.showSwalError('Tiempo de espera agotado. Intenta nuevamente.');
    }
  }, 15000);
}

private limpiarTimeoutSeguridad(): void {
  if (this.loadingTimeout) {
    clearTimeout(this.loadingTimeout);
    this.loadingTimeout = null;
  }
}

forzarLimpiarLoading(): void {
  console.warn('[COMPONENT] 🚨 MÉTODO DE EMERGENCIA: Forzando limpieza de loading');
  this.limpiarTimeoutSeguridad();
  this.loading = false;
  this.showSwalToast('Estado de carga limpiado manualmente', 'info');
}
```

**🎯 Destino:** `shared/services/loading-manager.service.ts`

---

### **4. 🔔 Notification Service (SweetAlert2)**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: Métodos dispersos (590-627)

**🔧 Código a extraer:**
```typescript
// Helpers SweetAlert2 para feedback uniforme
private showSwalToast(message: string, icon: 'success' | 'error' | 'info' | 'warning'): void {
  Swal.fire({
    icon: icon,
    title: message,
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true
  });
}

private showSwalError(message: string): void {
  Swal.fire({
    icon: 'error',
    title: 'Error',
    text: message,
    confirmButtonColor: '#d33'
  });
}

private showSwalSuccess(message: string): void {
  Swal.fire({
    icon: 'success',
    title: 'Éxito',
    text: message,
    timer: 2000,
    showConfirmButton: false
  });
}

// Confirmación de eliminación
private async mostrarConfirmacionEliminacion(cantidad: number = 1): Promise<boolean> {
  const text = cantidad === 1 
    ? 'Esta acción no se puede deshacer.'
    : `Se eliminarán ${cantidad} elementos. Esta acción no se puede deshacer.`;
  
  const result = await Swal.fire({
    title: '¿Confirmar eliminación?',
    text: text,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#dc3545',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  });
  
  return result.isConfirmed;
}
```

**🎯 Destino:** `shared/services/notification.service.ts`

---

### **5. ✅ Multi-Select System**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 25-30 (variables) + 340-420 (métodos)

**🔧 Código a extraer:**
```typescript
// Variables de selección múltiple
categoriasSeleccionadas: CategoriaConUI[] = [];
todoSeleccionado = false;

// Interface para elementos seleccionables
interface ElementoSeleccionable {
  id: number;
  seleccionado?: boolean;
}

// Métodos de selección múltiple
alternarSeleccionTodo(): void {
  this.todoSeleccionado = !this.todoSeleccionado;
  this.categorias.forEach(categoria => {
    categoria.seleccionado = this.todoSeleccionado;
  });
  this.actualizarSeleccionadas();
}

seleccionar(categoria: CategoriaConUI): void {
  categoria.seleccionado = !categoria.seleccionado;
  this.actualizarSeleccionadas();
}

private actualizarSeleccionadas(): void {
  this.categoriasSeleccionadas = this.categorias.filter(c => c.seleccionado);
  this.todoSeleccionado = this.categorias.length > 0 && 
    this.categorias.every(c => c.seleccionado);
}

// Acciones múltiples
activarSeleccionadas(): void {
  if (this.categoriasSeleccionadas.length === 0) {
    this.showSwalError('No hay categorías seleccionadas para activar.');
    return;
  }
  const ids = this.categoriasSeleccionadas.map(c => c.id);
  this.cambiarEstadoMultiple(ids, true);
}

desactivarSeleccionadas(): void {
  if (this.categoriasSeleccionadas.length === 0) {
    this.showSwalError('No hay categorías seleccionadas para desactivar.');
    return;
  }
  const ids = this.categoriasSeleccionadas.map(c => c.id);
  this.cambiarEstadoMultiple(ids, false);
}

eliminarSeleccionadas(): void {
  if (this.categoriasSeleccionadas.length === 0) return;
  
  const categoriasEliminables = this.categoriasSeleccionadas.filter(c => c.puedeEliminar);
  
  if (categoriasEliminables.length === 0) {
    this.showSwalError('Ninguna de las categorías seleccionadas se puede eliminar.');
    return;
  }

  // Usar confirmación y proceder con eliminación múltiple
}
```

**🎯 Destino:** `shared/components/multi-select/multi-select.component.ts`

**📋 Interface genérica:**
```typescript
export interface Selectable {
  id: number;
  seleccionado?: boolean;
}

export interface MultiSelectConfig<T extends Selectable> {
  items: T[];
  selectedItems: T[];
  allSelected: boolean;
}
```

---

### **6. 🎨 View Switcher**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 35-40 (variables) + método simple

**🔧 Código a extraer:**
```typescript
// Variables de vista
vistaActual: 'cards' | 'tabla' | 'jerarquia' = 'cards';

// Método cambio de vista
cambiarVista(vista: 'cards' | 'tabla' | 'jerarquia'): void {
  this.vistaActual = vista;
  // Opcional: guardar preferencia en localStorage
  localStorage.setItem('vista-preferida', vista);
}
```

**🎯 Destino:** `shared/components/view-switcher/view-switcher.component.ts`

**📋 Interface genérica:**
```typescript
export type ViewType = 'cards' | 'table' | 'list' | 'grid';

export interface ViewConfig {
  type: ViewType;
  icon: string;
  label: string;
  enabled: boolean;
}
```

---

### **7. 📊 API Response Handler**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 180-280 (método procesarRespuestaCategorias)

**🔧 Código a extraer:**
```typescript
procesarRespuestaCategorias(data: any): void {
  console.log('[COMPONENT] 🚀 Iniciando procesarRespuestaCategorias()');
  console.log('[COMPONENT] 📦 Data recibida:', data);
  
  try {
    let categorias: any[] = [];
    let totalPages = 1;
    let totalElements = 0;

    if (data) {
      // Caso 1: Respuesta con wrapper success + data + content (formato actual del backend)
      if (data.success && data.data && data.data.content && Array.isArray(data.data.content)) {
        categorias = data.data.content;
        totalPages = data.data.page?.totalPages || 1;
        totalElements = data.data.page?.totalElements || categorias.length;
        console.log('[COMPONENT] 📦 Datos extraídos del wrapper success + data + content');
      }
      // Caso 2: Respuesta directa con content (Spring Page estándar)
      else if (data.content && Array.isArray(data.content)) {
        categorias = data.content;
        totalPages = data.totalPages || 1;
        totalElements = data.totalElements || categorias.length;
        console.log('[COMPONENT] 📦 Datos extraídos del wrapper Spring Page directo');
      }
      // Caso 3: Respuesta con wrapper data + content (sin success)
      else if (data.data && data.data.content && Array.isArray(data.data.content)) {
        categorias = data.data.content;
        totalPages = data.data.totalPages || 1;
        totalElements = data.data.totalElements || categorias.length;
        console.log('[COMPONENT] 📦 Datos extraídos del wrapper con data');
      }
      // Caso 4: Array directo
      else if (Array.isArray(data)) {
        categorias = data;
        totalPages = 1;
        totalElements = data.length;
        console.log('[COMPONENT] 📦 Datos como array directo');
      }
      // Caso 5: Respuesta con success pero sin data wrapper
      else if (data.success && Array.isArray(data.content)) {
        categorias = data.content;
        totalPages = data.totalPages || 1;
        totalElements = data.totalElements || categorias.length;
        console.log('[COMPONENT] 📦 Datos extraídos de success + content directo');
      }
    }

    // Procesar los datos extraídos
    this.categorias = this.procesarCategorias(categorias);
    this.totalPages = totalPages;
    this.totalElements = totalElements;

    console.log('[COMPONENT] ✅ Categorías procesadas:', this.categorias.length);
    
  } catch (processError) {
    console.error('[COMPONENT] ❌ Error procesando categorías:', processError);
    this.showSwalError('Error procesando los datos de categorías');
  } finally {
    this.loading = false;
  }
}
```

**🎯 Destino:** `shared/services/api-response-handler.service.ts`

**📋 Interface genérica:**
```typescript
export interface ApiResponse<T> {
  success?: boolean;
  data?: any;
  content?: T[];
  totalPages?: number;
  totalElements?: number;
  page?: any;
}

export interface PagedResult<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
}
```

---

### **8. 📤 Export Service**

**📍 Ubicación actual:**
- Archivo: `categoria/presentation/pages/list-categoria/list-categoria-modern.component.ts`
- Líneas: 570-590

**🔧 Código a extraer:**
```typescript
// Métodos de exportación
exportarExcel(): void {
  this.showSwalToast('Exportando a Excel...', 'info');
  // Implementar exportación a Excel
  console.log('Exportar Excel - por implementar');
}

exportarPDF(): void {
  this.showSwalToast('Exportando a PDF...', 'info');
  // Implementar exportación a PDF
  console.log('Exportar PDF - por implementar');
}
```

**🎯 Destino:** `shared/services/export.service.ts`

---

## 🏗️ **Estructura de Destino en Shared**

```
shared/
├── components/
│   ├── data-management/
│   │   ├── pagination/
│   │   │   ├── pagination.component.ts
│   │   │   ├── pagination.component.html
│   │   │   └── pagination.component.scss
│   │   ├── filters-panel/
│   │   │   ├── filters-panel.component.ts
│   │   │   ├── filters-panel.component.html
│   │   │   └── filters-panel.component.scss
│   │   ├── multi-select/
│   │   │   ├── multi-select.component.ts
│   │   │   ├── multi-select.component.html
│   │   │   └── multi-select.component.scss
│   │   ├── view-switcher/
│   │   │   ├── view-switcher.component.ts
│   │   │   ├── view-switcher.component.html
│   │   │   └── view-switcher.component.scss
│   │   └── data-management.module.ts
├── services/
│   ├── api-response-handler.service.ts
│   ├── loading-manager.service.ts
│   ├── notification.service.ts
│   └── export.service.ts
└── interfaces/
    ├── data-management.interface.ts
    ├── pagination.interface.ts
    ├── filters.interface.ts
    └── api-response.interface.ts
```

---

## ⚡ **Próximos Pasos de Implementación**

1. **📋 Crear estructura base** en `shared/`
2. **🔧 Extraer componentes** uno por uno
3. **🧪 Crear tests** para cada componente
4. **📖 Documentar interfaces** y uso
5. **🔄 Refactorizar categoria/** para usar shared
6. **🚀 Migrar otros módulos** gradualmente

---

## 📝 **Notas de Implementación**

- **Mantener convención:** Nombres en inglés, propiedades en español
- **Interfaces genéricas:** Usar `<T>` para reutilización
- **Testing:** Cada componente debe tener tests unitarios
- **Documentación:** JSDoc en todos los métodos públicos
- **Performance:** Lazy loading para componentes pesados

---

*Documento actualizado: 4 de octubre de 2025*
*Versión: 1.0*
*Estado: Listo para implementación*