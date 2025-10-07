# 📊 Diagramas: Arquitectura del Carrito de Compras

> **Estado:** 🔄 **PENDIENTE** - Diagramas completos para visualizar arquitectura

## 🏗️ Diagrama de Arquitectura General

### 🧱 **Clean Architecture Overview**
```mermaid
graph TB
    subgraph "🎨 Presentation Layer"
        PC[Page Containers]
        UI[UI Components]
        F[Facades]
    end
    
    subgraph "🧠 Core Layer (Domain)"
        E[Entities]
        VO[Value Objects]
        UC[Use Cases]
        RI[Repository Interfaces]
    end
    
    subgraph "🔧 Infrastructure Layer"
        R[Repository Implementations]
        API[API Services]
        S[State Management]
        C[Cache Services]
    end
    
    subgraph "🤝 Shared Layer"
        I[Interfaces]
        U[Utils]
        CONST[Constants]
    end
    
    %% Dependencies (hacia el centro)
    PC --> F
    UI --> F
    F --> UC
    UC --> E
    UC --> VO
    UC --> RI
    R --> RI
    API --> R
    S --> R
    C --> R
    
    %% Shared usado por todas las capas
    PC -.-> I
    UC -.-> I
    R -.-> I
    
    classDef domain fill:#e1f5fe
    classDef presentation fill:#f3e5f5
    classDef infrastructure fill:#e8f5e8
    classDef shared fill:#fff3e0
    
    class E,VO,UC,RI domain
    class PC,UI,F presentation
    class R,API,S,C infrastructure
    class I,U,CONST shared
```

---

## 🔄 Diagramas de Flujo de Datos

### 📈 **Flujo: Agregar Item al Carrito**
```mermaid
sequenceDiagram
    participant U as User
    participant C as Component
    participant F as CartFacade
    participant UC as AddItemUseCase
    participant CR as CartRepository
    participant PR as ProductRepository
    participant S as StateService
    participant API as Backend API
    
    U->>C: Clicks "Add to Cart"
    C->>F: addItem(product, quantity)
    F->>S: setLoading(true)
    F->>UC: execute(command)
    
    UC->>CR: findByUserId(userId)
    CR->>API: GET /api/segura/carrito
    API-->>CR: CarritoDto
    CR-->>UC: Cart
    
    UC->>PR: findById(productId)
    PR->>API: GET /api/productos/{id}
    API-->>PR: ProductDto
    PR-->>UC: Product
    
    UC->>UC: cart.addItem(product, quantity)
    UC->>CR: save(updatedCart)
    CR->>API: PUT /api/segura/carrito
    API-->>CR: CarritoDto
    CR-->>UC: Cart
    
    UC-->>F: Result<Cart>
    F->>S: setCart(cart)
    F->>S: setLoading(false)
    S->>C: cart$ emits
    C->>U: Shows success feedback
```

### 🔢 **Flujo: Actualizar Cantidad**
```mermaid
flowchart TD
    A[User changes quantity] --> B{Valid quantity?}
    B -->|No| C[Show validation error]
    B -->|Yes| D[Start optimistic update]
    
    D --> E[Update UI immediately]
    D --> F[Send API request]
    
    F --> G{API Success?}
    G -->|Yes| H[Confirm optimistic update]
    G -->|No| I[Revert optimistic update]
    
    H --> J[Update state with real data]
    I --> K[Show error message]
    I --> L[Restore previous state]
    
    J --> M[Show success feedback]
    K --> M
    L --> M
    M --> N[End]
    
    classDef success fill:#e8f5e8
    classDef error fill:#ffebee
    classDef process fill:#e3f2fd
    
    class H,J,M success
    class C,I,K,L error
    class D,E,F process
```

---

## 🏛️ Diagrama de Componentes

### 🧩 **Jerarquía de Componentes**
```mermaid
graph TD
    subgraph "🏠 Cart Module"
        CP[CartPageContainer]
        CF[CartFlyoutComponent]
    end
    
    subgraph "📦 Cart Components"
        CI[CartItemComponent]
        CIL[CartItemsListComponent]
        CS[CartSummaryComponent]
        CA[CartActionsComponent]
        EC[EmptyCartComponent]
    end
    
    subgraph "🔧 Shared Components"
        QS[QuantitySelectorComponent]
        PI[ProductImageComponent]
        PR[PriceComponent]
        L[LoaderComponent]
    end
    
    subgraph "🎭 Services/Facades"
        CartF[CartFacade]
        CSS[CartStateService]
        CCS[CartCacheService]
    end
    
    %% Component relationships
    CP --> CIL
    CP --> CS
    CP --> CA
    CP --> EC
    
    CF --> CI
    CF --> CS
    
    CIL --> CI
    CI --> QS
    CI --> PI
    CI --> PR
    CI --> L
    
    %% Service dependencies
    CP --> CartF
    CF --> CartF
    CartF --> CSS
    CartF --> CCS
    
    classDef container fill:#e1f5fe
    classDef component fill:#f3e5f5
    classDef shared fill:#fff3e0
    classDef service fill:#e8f5e8
    
    class CP,CF container
    class CI,CIL,CS,CA,EC component
    class QS,PI,PR,L shared
    class CartF,CSS,CCS service
```

---

## 📊 Diagrama de Estado

### 🔄 **Cart State Machine**
```mermaid
stateDiagram-v2
    [*] --> Uninitialized
    
    Uninitialized --> Loading : Initialize Cart
    Loading --> Empty : No items found
    Loading --> Populated : Items found
    Loading --> Error : Load failed
    
    Empty --> Loading : Add first item
    Empty --> Error : Add item failed
    
    Populated --> Loading : Update item
    Populated --> Empty : Remove last item
    Populated --> Populated : Modify items
    Populated --> Error : Operation failed
    
    Error --> Loading : Retry operation
    Error --> Empty : Clear error (empty cart)
    Error --> Populated : Clear error (has items)
    
    state Populated {
        [*] --> Synced
        Synced --> Syncing : User action
        Synced --> OptimisticUpdate : Optimistic action
        
        OptimisticUpdate --> Synced : Confirm update
        OptimisticUpdate --> Conflict : Server conflict
        
        Syncing --> Synced : Success
        Syncing --> Error : Failure
        
        Conflict --> Synced : Resolve conflict
    }
    
    state Error {
        [*] --> Temporary
        [*] --> Permanent
        
        Temporary --> [*] : Auto retry
        Permanent --> [*] : User action
    }
```

---

## 🗂️ Diagrama de Capas de Datos

### 📚 **Data Layer Architecture**
```mermaid
graph LR
    subgraph "🎨 Presentation"
        C[Components]
        F[Facades]
    end
    
    subgraph "🧠 Domain"
        E[Cart Entity]
        UC[Use Cases]
        RI[Repository Interface]
    end
    
    subgraph "🔧 Infrastructure"
        subgraph "State Management"
            SS[StateService]
            CS[CacheService]
        end
        
        subgraph "Data Sources"
            HR[HttpRepository]
            LS[LocalStorageRepository]
        end
        
        subgraph "External"
            API[REST API]
            DB[(Database)]
        end
    end
    
    %% Data flow
    C --> F
    F --> UC
    UC --> RI
    HR -.-> RI
    LS -.-> RI
    
    HR --> API
    API --> DB
    
    UC --> SS
    SS --> CS
    
    %% Cache flow
    HR --> CS
    CS --> LS
    
    classDef presentation fill:#e1f5fe
    classDef domain fill:#fff3e0
    classDef infrastructure fill:#e8f5e8
    classDef external fill:#ffebee
    
    class C,F presentation
    class E,UC,RI domain
    class SS,CS,HR,LS infrastructure
    class API,DB external
```

---

## 🚀 Diagrama de Performance

### ⚡ **Optimization Strategy**
```mermaid
flowchart TD
    subgraph "🎯 Performance Layers"
        subgraph "UI Layer"
            A[OnPush Strategy]
            B[Virtual Scrolling]
            C[Lazy Loading]
        end
        
        subgraph "State Layer"
            D[Optimistic Updates]
            E[Memoization]
            F[Debouncing]
        end
        
        subgraph "Network Layer"
            G[HTTP Caching]
            H[Request Deduplication]
            I[Background Sync]
        end
        
        subgraph "Storage Layer"
            J[Memory Cache]
            K[Local Storage]
            L[IndexedDB]
        end
    end
    
    A --> D
    B --> E
    C --> F
    
    D --> G
    E --> H
    F --> I
    
    G --> J
    H --> K
    I --> L
    
    classDef ui fill:#e1f5fe
    classDef state fill:#f3e5f5
    classDef network fill:#e8f5e8
    classDef storage fill:#fff3e0
    
    class A,B,C ui
    class D,E,F state
    class G,H,I network
    class J,K,L storage
```

---

## 🔐 Diagrama de Seguridad

### 🛡️ **Security Flow**
```mermaid
sequenceDiagram
    participant U as User
    participant C as Component
    participant G as Auth Guard
    participant I as Auth Interceptor
    participant API as Backend
    participant S as Security Service
    
    U->>C: Interact with cart
    C->>G: Check authentication
    G->>S: Validate token
    S-->>G: Token valid
    G-->>C: Allow access
    
    C->>I: HTTP request
    I->>S: Get auth token
    S-->>I: Valid token
    I->>API: Request + Auth header
    
    alt Token valid
        API-->>I: Success response
        I-->>C: Data returned
    else Token invalid/expired
        API-->>I: 401 Unauthorized
        I->>S: Handle auth error
        S->>U: Redirect to login
    end
    
    Note over U,S: All cart operations require authentication
    Note over I,API: HTTPS + JWT tokens
    Note over S: Automatic token refresh
```

---

## 📱 Diagrama de Responsive Design

### 🖥️ **Breakpoint Strategy**
```mermaid
graph TD
    subgraph "📱 Mobile (320px-767px)"
        M1[Stack Layout]
        M2[Touch Interactions]
        M3[Simplified UI]
    end
    
    subgraph "📟 Tablet (768px-1023px)"
        T1[Grid Layout]
        T2[Expanded Controls]
        T3[Side Panel]
    end
    
    subgraph "🖥️ Desktop (1024px+)"
        D1[Multi-column]
        D2[Hover States]
        D3[Keyboard Shortcuts]
    end
    
    subgraph "🎨 Shared Design Tokens"
        ST1[Colors]
        ST2[Typography]
        ST3[Spacing]
        ST4[Shadows]
    end
    
    M1 --> ST1
    M2 --> ST2
    M3 --> ST3
    
    T1 --> ST1
    T2 --> ST2
    T3 --> ST4
    
    D1 --> ST1
    D2 --> ST3
    D3 --> ST4
    
    classDef mobile fill:#e8f5e8
    classDef tablet fill:#e1f5fe
    classDef desktop fill:#f3e5f5
    classDef tokens fill:#fff3e0
    
    class M1,M2,M3 mobile
    class T1,T2,T3 tablet
    class D1,D2,D3 desktop
    class ST1,ST2,ST3,ST4 tokens
```

---

## 🧪 Diagrama de Testing

### 🔬 **Testing Pyramid**
```mermaid
graph TD
    subgraph "🧪 Testing Strategy"
        subgraph "E2E Tests (10%)"
            E1[User Workflows]
            E2[Cross-browser]
            E3[Mobile Testing]
        end
        
        subgraph "Integration Tests (20%)"
            I1[API Integration]
            I2[Component Integration]
            I3[State Management]
        end
        
        subgraph "Unit Tests (70%)"
            U1[Domain Logic]
            U2[Use Cases]
            U3[Pure Functions]
            U4[Components]
        end
    end
    
    U1 --> I1
    U2 --> I2
    U3 --> I3
    U4 --> I1
    
    I1 --> E1
    I2 --> E2
    I3 --> E3
    
    classDef unit fill:#e8f5e8
    classDef integration fill:#e1f5fe
    classDef e2e fill:#f3e5f5
    
    class U1,U2,U3,U4 unit
    class I1,I2,I3 integration
    class E1,E2,E3 e2e
```

---

## 🔄 Diagrama de CI/CD

### 🚀 **Deployment Pipeline**
```mermaid
flowchart LR
    subgraph "Development"
        A[Code Commit]
        B[PR Created]
        C[Code Review]
    end
    
    subgraph "CI Pipeline"
        D[Build]
        E[Unit Tests]
        F[Integration Tests]
        G[E2E Tests]
        H[Security Scan]
        I[Performance Audit]
    end
    
    subgraph "CD Pipeline"
        J[Deploy Staging]
        K[Staging Tests]
        L[Deploy Production]
        M[Health Check]
        N[Monitoring]
    end
    
    A --> B
    B --> C
    C --> D
    
    D --> E
    E --> F
    F --> G
    G --> H
    H --> I
    
    I --> J
    J --> K
    K --> L
    L --> M
    M --> N
    
    %% Failure paths
    E -.->|Fail| O[Notify Team]
    F -.->|Fail| O
    G -.->|Fail| O
    K -.->|Fail| P[Rollback]
    M -.->|Fail| P
    
    classDef dev fill:#e8f5e8
    classDef ci fill:#e1f5fe
    classDef cd fill:#f3e5f5
    classDef error fill:#ffebee
    
    class A,B,C dev
    class D,E,F,G,H,I ci
    class J,K,L,M,N cd
    class O,P error
```

---

## 📈 Diagrama de Monitoreo

### 📊 **Observability Stack**
```mermaid
graph TB
    subgraph "🔍 Frontend Monitoring"
        A[Error Tracking]
        B[Performance Metrics]
        C[User Analytics]
    end
    
    subgraph "📊 Metrics Collection"
        D[Page Load Times]
        E[API Response Times]
        F[User Interactions]
        G[Error Rates]
    end
    
    subgraph "🚨 Alerting"
        H[Error Threshold]
        I[Performance Degradation]
        J[User Experience Issues]
    end
    
    subgraph "📈 Dashboards"
        K[Real-time Metrics]
        L[Historical Trends]
        M[Business KPIs]
    end
    
    A --> D
    B --> E
    C --> F
    
    D --> H
    E --> I
    F --> J
    G --> H
    
    H --> K
    I --> L
    J --> M
    
    classDef monitoring fill:#e1f5fe
    classDef metrics fill:#e8f5e8
    classDef alerting fill:#ffebee
    classDef dashboard fill:#f3e5f5
    
    class A,B,C monitoring
    class D,E,F,G metrics
    class H,I,J alerting
    class K,L,M dashboard
```

---

**Fin de la documentación del Carrito de Compras**

✅ **Documentación Completada:**
- [x] README.md - Visión general
- [x] 01-ESTRUCTURA-ACTUAL.md - Análisis del estado actual
- [x] 02-ARQUITECTURA-DUAL.md - Clean Architecture propuesta
- [x] 03-INTEGRACION-BACKEND.md - Integración con APIs
- [x] 04-DISEÑO-MODERNO.md - Implementación detallada
- [x] 05-CHECKLIST-IMPLEMENTACION.md - Guía de implementación
- [x] 06-MEJORES-PRACTICAS.md - Estándares y prácticas
- [x] 07-DIAGRAMAS.md - Visualización de arquitectura

🎯 **¿Dónde empezamos?** ¡La documentación está lista para guiar la implementación!
