# 📈 Diagramas de Arquitectura y Flujos

## 🏗️ Diagrama de Arquitectura Actual vs Propuesta

### 📊 **Arquitectura Actual**

```mermaid
graph TB
    subgraph "Frontend Actual"
        A[CategoriaModule] --> B[CategoriaRoutingModule]
        A --> C[CategoriaLayoutComponent]
        A --> D[ListCategoriaComponent]
        A --> E[AddCategoriaComponent]
        
        F[CategoriaService] --> G[HTTP Client]
        H[CategoriaPublicService] --> G
        
        I[Categoria Interface] --> J[PaginaCategoria Interface]
    end
    
    subgraph "Backend"
        K[API Gateway] --> L[CategoriaController]
        L --> M[CategoriaService Backend]
        M --> N[CategoriaRepository]
        N --> O[Database]
    end
    
    G --> K
    
    style A fill:#ffeb3b
    style F fill:#ff9800
    style L fill:#4caf50
```

### 🚀 **Arquitectura Propuesta** 

```mermaid
graph TB
    subgraph "Presentation Layer"
        A1[CategoriaModule] --> B1[Smart Containers]
        B1 --> C1[CategoriaDashboard]
        B1 --> D1[CategoriaManagement]
        B1 --> E1[CategoriaCatalog]
        
        F1[Presentational Components]
        F1 --> G1[CategoriaTree]
        F1 --> H1[CategoriaCard]
        F1 --> I1[CategoriaForm]
        F1 --> J1[CategoriaSearch]
    end
    
    subgraph "Domain Layer"
        K1[CategoriaFacade] --> L1[CategoriaBusinessService]
        K1 --> M1[CategoriaStateService]
        K1 --> N1[CategoriaValidationService]
        
        O1[CategoriaStoreService] --> P1[Reactive State]
    end
    
    subgraph "Infrastructure Layer"
        Q1[Repository Layer]
        Q1 --> R1[CategoriaPublicRepository]
        Q1 --> S1[CategoriaAdminRepository]
        
        T1[CategoriaCacheService] --> U1[Cache Strategy]
        V1[CategoriaApiClient] --> W1[HTTP Interceptors]
    end
    
    subgraph "Backend Services"
        X1[API Gateway] --> Y1[Public Controller]
        X1 --> Z1[Admin Controller]
        Y1 --> AA1[Categoria Service]
        Z1 --> AA1
    end
    
    B1 --> K1
    K1 --> Q1
    Q1 --> V1
    W1 --> X1
    
    style K1 fill:#2196f3,color:#fff
    style Q1 fill:#ff9800,color:#fff
    style AA1 fill:#4caf50,color:#fff
```

---

## 🔄 Flujo de Datos: Lectura de Categorías

### 🌐 **Flujo Público (Sin Autenticación)**

```mermaid
sequenceDiagram
    participant U as Usuario
    participant C as CatalogContainer
    participant F as CategoriaFacade
    participant CS as CacheService
    participant PR as PublicRepository
    participant API as Backend API
    participant DB as Database
    
    U->>C: Solicita categorías
    C->>F: loadPublicCategorias()
    F->>CS: getOrSet('public-categorias')
    
    alt Cache Hit
        CS-->>F: Datos en cache
        F-->>C: CategoriaDTO[]
    else Cache Miss
        CS->>PR: findAllActive()
        PR->>API: GET /api/public/categorias/list
        API->>DB: SELECT categorias WHERE activo=true
        DB-->>API: Resultados
        API-->>PR: ApiResponse<CategoriaDTO[]>
        PR-->>CS: CategoriaDTO[]
        CS-->>F: CategoriaDTO[] (cached)
        F-->>C: CategoriaDTO[]
    end
    
    C->>C: updateState(categorias)
    C-->>U: Renderiza lista
```

### 🔒 **Flujo Administrativo (Con Autenticación)**

```mermaid
sequenceDiagram
    participant A as Admin
    participant M as ManagementContainer
    participant F as CategoriaFacade
    participant BS as BusinessService
    parameter VS as ValidationService
    participant AR as AdminRepository
    participant SS as StateService
    participant API as Backend API
    
    A->>M: Crear categoría
    M->>F: createCategoria(data)
    F->>VS: validateCreation(data)
    
    alt Validación OK
        VS-->>F: ValidationResult.valid
        F->>BS: processCreation(validData)
        BS->>AR: create(categoria)
        AR->>API: POST /api/segura/categorias/create
        API-->>AR: ApiResponse<CategoriaDTO>
        AR-->>BS: CategoriaDTO
        BS->>SS: addCategoria(categoria)
        SS-->>F: State updated
        F->>F: invalidateCache()
        F-->>M: CategoriaDTO created
        M-->>A: Success message
    else Validación Error
        VS-->>F: ValidationResult.errors
        F-->>M: ValidationErrors
        M-->>A: Error display
    end
```

---

## 🌳 Flujo de Jerarquía de Categorías

### 📊 **Construcción de Árbol Jerárquico**

```mermaid
graph TD
    A[Categorías Planas] --> B{Procesar Jerarquía}
    
    B --> C[Identificar Raíces]
    C --> D[categoriaPadreId === null]
    
    B --> E[Agrupar por Padre]
    E --> F[Map<parentId, children[]>]
    
    D --> G[Para cada raíz]
    G --> H[Buscar hijos recursivamente]
    H --> I[Construir subcategorias[]]
    I --> J[Árbol completo]
    
    F --> H
    
    J --> K[CategoriaTree Component]
    K --> L[Renderizado jerárquico]
    
    style A fill:#ffeb3b
    style J fill:#4caf50
    style L fill:#2196f3,color:#fff
```

### 🔍 **Flujo de Expansión Lazy Loading**

```mermaid
sequenceDiagram
    participant U as Usuario
    participant TC as TreeComponent
    participant LS as LazyLoadingService
    participant R as Repository
    participant Cache as CacheService
    
    U->>TC: Click expandir nodo
    TC->>TC: checkIfLoaded(nodeId)
    
    alt Ya cargado
        TC-->>U: Mostrar subcategorías
    else No cargado
        TC->>LS: loadSubcategoriesIfNeeded(nodeId)
        LS->>Cache: get('subcategorias-' + nodeId)
        
        alt Cache hit
            Cache-->>LS: Subcategorías
        else Cache miss
            LS->>R: findByParent(nodeId)
            R-->>LS: Subcategorías
            LS->>Cache: set('subcategorias-' + nodeId)
        end
        
        LS->>TC: updateNode(subcategorías)
        TC-->>U: Mostrar subcategorías
    end
```

---

## 🔍 Flujo de Búsqueda Avanzada

### 📱 **Búsqueda con Debouncing y Filtros**

```mermaid
sequenceDiagram
    participant U as Usuario
    participant SC as SearchComponent
    participant F as CategoriaFacade
    participant R as Repository
    participant API as Backend
    
    U->>SC: Escribe en input
    SC->>SC: debounceTime(300ms)
    SC->>SC: distinctUntilChanged()
    SC->>SC: filter(length >= 2)
    
    Note over SC: RxJS operators chain
    
    SC->>F: searchCategorias(query, filters)
    F->>R: search(SearchCriteria)
    R->>API: GET /categorias/buscar?q=...&filters=...
    API-->>R: ApiResponse<CategoriaDTO[]>
    R-->>F: CategoriaDTO[]
    F-->>SC: Resultados
    SC-->>U: Mostrar resultados
    
    Note over U,SC: Real-time search experience
```

---

## 💾 Estrategia de Cache Multi-Nivel

### 🗂️ **Cache Hierarchy**

```mermaid
graph TB
    subgraph "Cache Levels"
        A[Component Level Cache] --> B[Service Level Cache]
        B --> C[HTTP Interceptor Cache]
        C --> D[Browser Cache]
    end
    
    subgraph "Cache Keys Strategy"
        E[Static Data: 'categorias-all']
        F[User-specific: 'user-{id}-favoritas']
        G[Search Results: 'search-{query}-{filters}']
        H[Hierarchy: 'hierarchy-{level}-{parentId}']
    end
    
    subgraph "Invalidation Strategy"
        I[Time-based TTL]
        J[Event-based Invalidation]
        K[Pattern-based Cleanup]
        L[Memory Pressure Cleanup]
    end
    
    A --> E
    B --> F
    C --> G
    D --> H
    
    E --> I
    F --> J
    G --> K
    H --> L
    
    style A fill:#2196f3,color:#fff
    style I fill:#ff9800,color:#fff
```

---

## 🔄 Estado de Aplicación con RxJS

### 📊 **State Flow Diagram**

```mermaid
stateDiagram-v2
    [*] --> Initial
    
    Initial --> Loading: loadCategorias()
    Loading --> Loaded: success
    Loading --> Error: failure
    
    Loaded --> Searching: searchCategorias()
    Searching --> SearchResults: results found
    Searching --> NoResults: no results
    
    SearchResults --> Loaded: clearSearch()
    NoResults --> Loaded: clearSearch()
    
    Loaded --> Creating: createCategoria()
    Creating --> Loaded: success
    Creating --> Error: failure
    
    Loaded --> Updating: updateCategoria()
    Updating --> Loaded: success  
    Updating --> Error: failure
    
    Error --> Loading: retry()
    Error --> Initial: reset()
    
    note right of Loading
        loading: true
        error: null
    end note
    
    note right of Loaded
        categorias: CategoriaDTO[]
        loading: false
        error: null
    end note
    
    note right of Error
        loading: false
        error: string
    end note
```

---

## 🔐 Flujo de Autorización

### 🚦 **Permission-based Access Control**

```mermaid
graph TD
    A[Usuario Accede] --> B{Verificar Ruta}
    
    B -->|Pública| C[Acceso Directo]
    B -->|Protegida| D[CanActivate Guard]
    
    D --> E{Token JWT Válido?}
    E -->|No| F[Redirect Login]
    E -->|Sí| G{Permisos Suficientes?}
    
    G -->|No| H[403 Forbidden]
    G -->|Sí| I[Verificar Operación]
    
    I --> J{Tipo de Operación}
    J -->|Lectura| K[Permitir]
    J -->|Escritura| L{Es Admin?}
    J -->|Eliminación| M{Es Super Admin?}
    
    L -->|Sí| K
    L -->|No| H
    M -->|Sí| K
    M -->|No| H
    
    C --> N[Componente Público]
    K --> O[Componente Autorizado]
    
    style D fill:#ff9800,color:#fff
    style H fill:#f44336,color:#fff
    style K fill:#4caf50,color:#fff
```

---

## 📱 Responsive Design Flow

### 🖥️ **Multi-Device Experience**

```mermaid
graph LR
    subgraph "Device Detection"
        A[ViewportSize] --> B{Breakpoint?}
        B -->|Mobile| C[< 768px]
        B -->|Tablet| D[768-1024px]  
        B -->|Desktop| E[> 1024px]
    end
    
    subgraph "Layout Adaptation"
        C --> F[Stack Layout]
        D --> G[Hybrid Layout]
        E --> H[Grid Layout]
    end
    
    subgraph "Component Behavior"
        F --> I[Drawer Navigation]
        F --> J[Simplified Forms]
        F --> K[Touch Gestures]
        
        G --> L[Tab Navigation]
        G --> M[Responsive Tables]
        
        H --> N[Sidebar Navigation]
        H --> O[Advanced Features]
        H --> P[Mouse Interactions]
    end
    
    style C fill:#ff9800,color:#fff
    style D fill:#2196f3,color:#fff
    style E fill:#4caf50,color:#fff
```

---

## 🎯 Performance Optimization Flow

### ⚡ **Optimization Strategy**

```mermaid
graph TB
    subgraph "Initial Load"
        A[Bundle Splitting] --> B[Lazy Loading]
        B --> C[Critical CSS]
        C --> D[Above Fold Content]
    end
    
    subgraph "Runtime Optimization"
        E[Virtual Scrolling] --> F[OnPush Detection]
        F --> G[Memoization]
        G --> H[Debouncing]
    end
    
    subgraph "Data Optimization"
        I[Cache Strategy] --> J[Prefetching]
        J --> K[Background Sync]
        K --> L[Delta Updates]
    end
    
    subgraph "Monitoring"
        M[Performance Metrics] --> N[User Experience]
        N --> O[Bundle Analysis]
        O --> P[Runtime Profiling]
    end
    
    D --> E
    H --> I
    L --> M
    
    style A fill:#2196f3,color:#fff
    style E fill:#ff9800,color:#fff
    style I fill:#4caf50,color:#fff
    style M fill:#9c27b0,color:#fff
```

---

**Próximo:** [Mejores Prácticas](./06-MEJORES-PRACTICAS.md)
