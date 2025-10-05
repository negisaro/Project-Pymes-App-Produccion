# Plan de Implementación - Shared Components

## 🎯 **Orden de Implementación Recomendado**

### **FASE 1A: Servicios Base (Semana 1 - Días 1-2)**
```
Priority: CRÍTICO
Dependencias: Ninguna
Impacto: Alto - Todos los componentes dependen de estos
```

1. **📱 Notification Service** → `shared/services/notification.service.ts`
2. **⏳ Loading Manager Service** → `shared/services/loading-manager.service.ts`
3. **📊 API Response Handler** → `shared/services/api-response-handler.service.ts`

### **FASE 1B: Interfaces (Semana 1 - Día 3)**
```
Priority: ALTO
Dependencias: Servicios base
Impacto: Medio - Define contratos para componentes
```

4. **📋 Data Management Interfaces** → `shared/interfaces/`
5. **🔧 Generic Types** → Para reutilización

### **FASE 1C: Componentes UI (Semana 1 - Días 4-5)**
```
Priority: MEDIO
Dependencias: Servicios + Interfaces
Impacto: Alto - Componentes visuales reutilizables
```

6. **📊 Pagination Component** → Más complejo, prioridad alta
7. **🔍 Filters Panel** → Segundo más complejo
8. **✅ Multi-Select** → Funcionalidad crítica
9. **🎨 View Switcher** → Simple, último

---

## 🏗️ **Estructura Técnica de Implementación**

### **1. Crear Base Module**
```typescript
// shared/components/data-management/data-management.module.ts
@NgModule({
  declarations: [
    PaginationComponent,
    FiltersPanelComponent,
    MultiSelectComponent,
    ViewSwitcherComponent
  ],
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  exports: [
    PaginationComponent,
    FiltersPanelComponent,
    MultiSelectComponent,
    ViewSwitcherComponent
  ],
  providers: [
    NotificationService,
    LoadingManagerService,
    ApiResponseHandlerService,
    ExportService
  ]
})
export class DataManagementModule { }
```

### **2. Abstract Base Component**
```typescript
// shared/components/data-management/base-data-management.component.ts
export abstract class BaseDataManagementComponent<T extends ManageableEntity> 
  implements OnInit, OnDestroy {
  
  // Properties comunes
  items: T[] = [];
  selectedItems: T[] = [];
  loading = false;
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;
  
  // Servicios inyectados
  protected notificationService = inject(NotificationService);
  protected loadingManager = inject(LoadingManagerService);
  protected apiHandler = inject(ApiResponseHandlerService);
  
  // Métodos abstractos que cada módulo debe implementar
  abstract loadData(): void;
  abstract deleteItem(id: number): Observable<any>;
  abstract updateItem(id: number, item: Partial<T>): Observable<any>;
  
  // Métodos comunes implementados
  onPageChange(page: number): void { /* ... */ }
  onFilterChange(filters: any): void { /* ... */ }
  onSelect(item: T): void { /* ... */ }
  // etc...
}
```

---

## 📋 **Checklist de Implementación**

### **✅ Preparación**
- [ ] Crear estructura de directorios en `shared/`
- [ ] Configurar imports en `shared.module.ts`
- [ ] Preparar dependencias (SweetAlert2, etc.)

### **📱 Servicios Base**
- [ ] **NotificationService**
  - [ ] Métodos showSuccess, showError, showWarning, showInfo
  - [ ] Configuración de SweetAlert2
  - [ ] Toast notifications
  - [ ] Confirmation dialogs
  - [ ] Tests unitarios

- [ ] **LoadingManagerService**
  - [ ] Estado global de loading
  - [ ] Timeout de seguridad
  - [ ] Loading overlay
  - [ ] Tests unitarios

- [ ] **ApiResponseHandlerService**
  - [ ] Manejo de múltiples formatos de respuesta
  - [ ] Extracción de datos paginados
  - [ ] Error handling
  - [ ] Tests unitarios

### **📋 Interfaces**
- [ ] **ManageableEntity** (id, nombre, activo, fechas)
- [ ] **Pageable<T>** (content, totalPages, etc.)
- [ ] **Filterable** (filtros dinámicos)
- [ ] **Selectable** (selección múltiple)
- [ ] **ApiResponse<T>** (respuestas del backend)

### **🎨 Componentes UI**
- [ ] **PaginationComponent**
  - [ ] Input: página actual, total páginas, tamaño
  - [ ] Output: eventos de cambio de página
  - [ ] Navegación con números visibles
  - [ ] Responsive design
  - [ ] Tests + Storybook

- [ ] **FiltersPanelComponent**
  - [ ] Input: configuración de filtros
  - [ ] Output: eventos de cambio de filtros
  - [ ] Filtros colapsables
  - [ ] Búsqueda con debounce
  - [ ] Tests + Storybook

- [ ] **MultiSelectComponent**
  - [ ] Input: lista de items
  - [ ] Output: items seleccionados
  - [ ] Select all/none
  - [ ] Acciones múltiples
  - [ ] Tests + Storybook

- [ ] **ViewSwitcherComponent**
  - [ ] Input: vistas disponibles
  - [ ] Output: vista seleccionada
  - [ ] Persistencia en localStorage
  - [ ] Tests + Storybook

### **🔧 Integración**
- [ ] **BaseDataManagementComponent**
  - [ ] Clase abstracta con funcionalidad común
  - [ ] Inyección de servicios shared
  - [ ] Lifecycle hooks
  - [ ] Error handling

- [ ] **DataManagementModule**
  - [ ] Exports de todos los componentes
  - [ ] Providers de servicios
  - [ ] Configuración completa

### **🧪 Testing**
- [ ] Tests unitarios para cada servicio
- [ ] Tests de componentes con TestBed
- [ ] Tests de integración
- [ ] E2E tests para flujos completos

### **📖 Documentación**
- [ ] JSDoc en todos los métodos públicos
- [ ] README de cada componente
- [ ] Ejemplos de uso
- [ ] Guía de migración

---

## 🚀 **Pasos Inmediatos para Comenzar**

### **1. Crear estructura inicial (15 min)**
```bash
mkdir -p shared/components/data-management
mkdir -p shared/services
mkdir -p shared/interfaces
mkdir -p shared/utils
```

### **2. Instalar dependencias adicionales (5 min)**
```bash
npm install sweetalert2
npm install @types/sweetalert2 --save-dev
```

### **3. Comenzar con NotificationService (30 min)**
- Extraer código de SweetAlert2 del componente categoria
- Crear servicio injectable
- Configurar en DataManagementModule

### **4. Crear primera interface (15 min)**
- ManageableEntity base
- Definir propiedades comunes (id, nombre, activo, fechas)

### **5. Test inmediato (10 min)**
- Importar DataManagementModule en categoria
- Inyectar NotificationService
- Verificar funcionamiento

---

## 📊 **Timeline Estimado**

| Componente | Tiempo Estimado | Prioridad |
|-----------|----------------|-----------|
| NotificationService | 2 horas | 🔴 Crítico |
| LoadingManagerService | 1.5 horas | 🔴 Crítico |
| ApiResponseHandlerService | 3 horas | 🔴 Crítico |
| Interfaces base | 1 hora | 🟡 Alto |
| PaginationComponent | 4 horas | 🟡 Alto |
| FiltersPanelComponent | 3 horas | 🟡 Alto |
| MultiSelectComponent | 2.5 horas | 🟡 Alto |
| ViewSwitcherComponent | 1 hora | 🟢 Medio |
| BaseDataManagementComponent | 2 horas | 🟡 Alto |
| Tests + Documentación | 4 horas | 🟡 Alto |

**Total estimado: 24 horas = 3 días de trabajo**

---

*Documento de implementación listo para ejecución*
*Fecha: 4 de octubre de 2025*
*Estado: READY TO CODE*