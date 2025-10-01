# 🏗️ Diagramas de Arquitectura - Microservicio Carrito de Compras

## 📊 Diagrama de Arquitectura General

```mermaid
graph TB
    %% Cliente y Gateway
    Client[🖥️ Cliente Web/Mobile]
    Gateway[🌐 API Gateway<br/>Puerto 8080]
    
    %% Microservicios
    CarritoMS[🛒 Microservicio Carrito<br/>Puerto 8082]
    UsuarioMS[👤 Microservicio Usuario<br/>Puerto 8081]
    ProductoMS[📦 Microservicio Producto<br/>Puerto 8085]
    
    %% Bases de Datos
    CarritoDB[(🗄️ MySQL<br/>Carrito DB)]
    Cache[(⚡ Redis<br/>Cache)]
    
    %% Service Discovery
    Eureka[🔍 Eureka Server<br/>Puerto 8761]
    
    %% Flujo de datos
    Client --> Gateway
    Gateway --> CarritoMS
    CarritoMS --> CarritoDB
    CarritoMS --> Cache
    CarritoMS --> UsuarioMS
    CarritoMS --> ProductoMS
    
    %% Service Discovery
    CarritoMS -.-> Eureka
    UsuarioMS -.-> Eureka
    ProductoMS -.-> Eureka
    
    %% Estilos
    classDef microservice fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef database fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef client fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef infrastructure fill:#fff3e0,stroke:#ef6c00,stroke-width:2px
    
    class CarritoMS,UsuarioMS,ProductoMS microservice
    class CarritoDB,Cache database
    class Client client
    class Gateway,Eureka infrastructure
```

## 🎯 Diagrama de Capas del Microservicio

```mermaid
graph TD
    %% Capa de Presentación
    Controller[🌐 Controller Layer<br/>- CarritoController<br/>- Validation<br/>- Security]
    
    %% Capa de Servicio
    Service[🔧 Service Layer<br/>- CarritoService<br/>- ItemCarritoService<br/>- ValidationService<br/>- CalculationService]
    
    %% Capa de Repositorio
    Repository[🗃️ Repository Layer<br/>- CarritoRepository<br/>- ItemCarritoRepository<br/>- Custom Queries]
    
    %% Capa de Integración
    Integration[🔗 Integration Layer<br/>- ProductoClient<br/>- UsuarioClient<br/>- DescuentoClient]
    
    %% Capa de Datos
    Database[(🗄️ MySQL Database<br/>- carritos<br/>- items_carrito)]
    CacheLayer[(⚡ Redis Cache<br/>- carrito:user:{id}<br/>- producto:{id})]
    
    %% Configuración y Seguridad
    Config[⚙️ Configuration<br/>- SecurityConfig<br/>- CacheConfig<br/>- FeignConfig]
    
    %% Flujo de capas
    Controller --> Service
    Service --> Repository
    Service --> Integration
    Repository --> Database
    Service --> CacheLayer
    Config -.-> Controller
    Config -.-> Service
    Config -.-> Repository
    
    %% Estilos
    classDef layer fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    classDef data fill:#f1f8e9,stroke:#33691e,stroke-width:2px
    classDef config fill:#fce4ec,stroke:#ad1457,stroke-width:2px
    
    class Controller,Service,Repository,Integration layer
    class Database,CacheLayer data
    class Config config
```

## 🔄 Diagrama de Flujo - Agregar Item al Carrito

```mermaid
sequenceDiagram
    participant C as 📱 Cliente
    participant GW as 🌐 Gateway
    participant CC as 🛒 CarritoController
    participant CS as 🔧 CarritoService
    participant VL as ✅ ValidationService
    participant CR as 🗃️ CarritoRepository
    participant PC as 📦 ProductoClient
    participant UC as 👤 UsuarioClient
    participant Cache as ⚡ Redis
    participant DB as 🗄️ MySQL

    Note over C,DB: Flujo: Agregar Item al Carrito

    C->>GW: POST /api/carrito/{userId}/items
    Note over C,GW: {productoId: 123, cantidad: 2}
    
    GW->>CC: Validar JWT & Forward
    CC->>VL: Validar request
    VL-->>CC: ✅ Request válido
    
    CC->>CS: agregarItem(userId, productoId, cantidad)
    
    %% Validar usuario
    CS->>UC: obtenerUsuario(userId)
    UC-->>CS: UsuarioDto
    
    %% Buscar carrito (con cache)
    CS->>Cache: get("carrito:user:" + userId)
    alt Cache Hit
        Cache-->>CS: CarritoDto
    else Cache Miss
        CS->>CR: findByUsuarioId(userId)
        CR->>DB: SELECT carrito WHERE usuario_id = ?
        DB-->>CR: Carrito entity
        CR-->>CS: Carrito
        CS->>Cache: set("carrito:user:" + userId, carrito)
    end
    
    %% Validar producto y stock
    CS->>PC: obtenerProducto(productoId)
    PC-->>CS: ProductoDto
    CS->>PC: verificarStock(productoId, cantidad)
    PC-->>CS: ✅ Stock disponible
    
    %% Agregar item
    CS->>CS: calcularPrecios()
    CS->>CR: save(carritoActualizado)
    CR->>DB: INSERT/UPDATE items_carrito
    DB-->>CR: ✅ Guardado
    CR-->>CS: Carrito actualizado
    
    %% Invalidar cache
    CS->>Cache: delete("carrito:user:" + userId)
    
    CS-->>CC: CarritoDto actualizado
    CC-->>GW: 200 OK + CarritoDto
    GW-->>C: Carrito actualizado
    
    Note over C,DB: ✅ Item agregado exitosamente
```

## 🏢 Diagrama de Componentes Detallado

```mermaid
graph TB
    subgraph "🛒 Microservicio Carrito"
        %% Controllers
        subgraph "🌐 Controllers"
            CarritoCtrl[CarritoController]
            ErrorHandler[GlobalExceptionHandler]
        end
        
        %% Services
        subgraph "🔧 Services"
            CarritoSvc[CarritoService]
            ItemSvc[ItemCarritoService]
            ValidSvc[ValidationService]
            CalcSvc[CalculationService]
            NotifSvc[NotificationService]
        end
        
        %% Repositories
        subgraph "🗃️ Repositories"
            CarritoRepo[CarritoRepository]
            ItemRepo[ItemCarritoRepository]
        end
        
        %% Feign Clients
        subgraph "🔗 Feign Clients"
            ProdClient[ProductoClient]
            UserClient[UsuarioClient]
            DiscClient[DescuentoClient]
        end
        
        %% Configuration
        subgraph "⚙️ Configuration"
            SecurityConf[SecurityConfig]
            CacheConf[CacheConfig]
            FeignConf[FeignConfig]
        end
        
        %% Entities & DTOs
        subgraph "📊 Model"
            Entities[Carrito<br/>ItemCarrito]
            DTOs[CarritoDto<br/>ItemCarritoDto<br/>RequestDTOs]
            Mappers[CarritoMapper<br/>ItemMapper]
        end
    end
    
    %% External Services
    subgraph "🌐 External Services"
        ProdMS[📦 Producto Service]
        UserMS[👤 Usuario Service]
        DiscMS[🎯 Descuento Service]
    end
    
    %% Databases
    subgraph "🗄️ Data Layer"
        MySQL[(MySQL<br/>Primary DB)]
        Redis[(Redis<br/>Cache)]
    end
    
    %% Connections
    CarritoCtrl --> CarritoSvc
    CarritoCtrl --> ErrorHandler
    CarritoSvc --> ItemSvc
    CarritoSvc --> ValidSvc
    CarritoSvc --> CalcSvc
    CarritoSvc --> NotifSvc
    CarritoSvc --> CarritoRepo
    ItemSvc --> ItemRepo
    CarritoSvc --> ProdClient
    CarritoSvc --> UserClient
    CarritoSvc --> DiscClient
    
    ProdClient --> ProdMS
    UserClient --> UserMS
    DiscClient --> DiscMS
    
    CarritoRepo --> MySQL
    ItemRepo --> MySQL
    CarritoSvc --> Redis
    
    Mappers --> Entities
    Mappers --> DTOs
    
    %% Estilos
    classDef controller fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef service fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    classDef repository fill:#fff3e0,stroke:#ef6c00,stroke-width:2px
    classDef client fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef config fill:#fce4ec,stroke:#ad1457,stroke-width:2px
    classDef model fill:#e0f2f1,stroke:#00695c,stroke-width:2px
    classDef external fill:#f9fbe7,stroke:#827717,stroke-width:2px
    classDef database fill:#e8eaf6,stroke:#283593,stroke-width:2px
    
    class CarritoCtrl,ErrorHandler controller
    class CarritoSvc,ItemSvc,ValidSvc,CalcSvc,NotifSvc service
    class CarritoRepo,ItemRepo repository
    class ProdClient,UserClient,DiscClient client
    class SecurityConf,CacheConf,FeignConf config
    class Entities,DTOs,Mappers model
    class ProdMS,UserMS,DiscMS external
    class MySQL,Redis database
```

## 🔐 Diagrama de Seguridad

```mermaid
graph TD
    %% Entrada del cliente
    Client[📱 Cliente]
    
    %% Gateway con autenticación
    Gateway[🌐 API Gateway<br/>JWT Validation]
    
    %% Filtros de seguridad
    subgraph "🛡️ Security Filters"
        JWTFilter[JWT Authentication Filter]
        AuthFilter[Authorization Filter]
        RateLimit[Rate Limiting Filter]
    end
    
    %% Controlador con validaciones
    Controller[🛒 Carrito Controller<br/>- Request Validation<br/>- User Authorization]
    
    %% Validaciones de negocio
    subgraph "✅ Business Validations"
        UserValid[User Ownership<br/>Validation]
        StockValid[Stock Availability<br/>Validation]
        LimitValid[Quantity Limits<br/>Validation]
    end
    
    %% Datos seguros
    Database[(🔒 Encrypted Database)]
    
    %% Flujo de seguridad
    Client --> Gateway
    Gateway --> JWTFilter
    JWTFilter --> AuthFilter
    AuthFilter --> RateLimit
    RateLimit --> Controller
    Controller --> UserValid
    Controller --> StockValid
    Controller --> LimitValid
    UserValid --> Database
    StockValid --> Database
    LimitValid --> Database
    
    %% Estilos
    classDef security fill:#ffebee,stroke:#c62828,stroke-width:2px
    classDef validation fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef data fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    
    class Gateway,JWTFilter,AuthFilter,RateLimit security
    class Controller,UserValid,StockValid,LimitValid validation
    class Database data
```

## 📊 Diagrama de Base de Datos

```mermaid
erDiagram
    CARRITOS {
        bigint id PK "AUTO_INCREMENT"
        bigint usuario_id FK "NOT NULL, INDEX"
        datetime creado_en "NOT NULL, DEFAULT CURRENT_TIMESTAMP"
        datetime actualizado_en "ON UPDATE CURRENT_TIMESTAMP"
        decimal subtotal "DECIMAL(10,2), DEFAULT 0.00"
        decimal descuento "DECIMAL(10,2), DEFAULT 0.00"
        decimal total "DECIMAL(10,2), DEFAULT 0.00"
        enum estado "ACTIVO, ABANDONADO, PROCESADO"
        varchar creado_por "VARCHAR(100)"
        varchar actualizado_por "VARCHAR(100)"
    }
    
    ITEMS_CARRITO {
        bigint id PK "AUTO_INCREMENT"
        bigint carrito_id FK "NOT NULL"
        bigint producto_id "NOT NULL, INDEX"
        varchar nombre_producto "VARCHAR(255), DENORMALIZED"
        decimal precio_unitario "DECIMAL(10,2), DENORMALIZED"
        int cantidad "NOT NULL, CHECK > 0"
        decimal subtotal "DECIMAL(10,2), COMPUTED"
        datetime agregado_en "DEFAULT CURRENT_TIMESTAMP"
    }
    
    CARRITO_HISTORIAL {
        bigint id PK "AUTO_INCREMENT"
        bigint carrito_id FK "NOT NULL"
        varchar operacion "INSERT, UPDATE, DELETE"
        json datos_anteriores "JSON"
        json datos_nuevos "JSON"
        datetime fecha_operacion "DEFAULT CURRENT_TIMESTAMP"
        varchar usuario_operacion "VARCHAR(100)"
    }
    
    DESCUENTOS_APLICADOS {
        bigint id PK "AUTO_INCREMENT"
        bigint carrito_id FK "NOT NULL"
        varchar codigo_descuento "VARCHAR(50)"
        decimal monto_descuento "DECIMAL(10,2)"
        decimal porcentaje_descuento "DECIMAL(5,2)"
        datetime aplicado_en "DEFAULT CURRENT_TIMESTAMP"
    }
    
    %% Relaciones
    CARRITOS ||--o{ ITEMS_CARRITO : contiene
    CARRITOS ||--o{ CARRITO_HISTORIAL : tiene_historial
    CARRITOS ||--o{ DESCUENTOS_APLICADOS : tiene_descuentos
```

## ⚡ Diagrama de Cache Strategy

```mermaid
graph TD
    %% Request flow
    Request[📱 Cliente Request]
    Service[🔧 Carrito Service]
    
    %% Cache decision
    CacheCheck{🔍 Check Cache}
    
    %% Cache scenarios
    CacheHit[⚡ Cache HIT<br/>Return from Redis]
    CacheMiss[❌ Cache MISS<br/>Query Database]
    
    %% Database and Cache update
    Database[(🗄️ MySQL)]
    CacheUpdate[📝 Update Cache<br/>TTL: 30 min]
    
    %% Cache invalidation
    WriteOp{✏️ Write Operation?}
    InvalidateCache[🗑️ Invalidate Cache<br/>Related Keys]
    
    %% Flow
    Request --> Service
    Service --> CacheCheck
    
    CacheCheck -->|Found| CacheHit
    CacheCheck -->|Not Found| CacheMiss
    
    CacheMiss --> Database
    Database --> CacheUpdate
    CacheUpdate --> Service
    CacheHit --> Service
    
    Service --> WriteOp
    WriteOp -->|Yes| InvalidateCache
    WriteOp -->|No| Request
    InvalidateCache --> Request
    
    %% Cache Keys Strategy
    subgraph "🔑 Cache Keys"
        UserCarrito["carrito:user:{userId}"]
        ProductInfo["producto:{productoId}"]
        UserSession["session:user:{userId}"]
    end
    
    %% Estilos
    classDef cache fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef database fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    classDef operation fill:#fff3e0,stroke:#ef6c00,stroke-width:2px
    
    class CacheHit,CacheUpdate,InvalidateCache,UserCarrito,ProductInfo,UserSession cache
    class Database,CacheMiss database
    class Request,Service,WriteOp operation
```

## 🔄 Diagrama de Circuit Breaker

```mermaid
stateDiagram-v2
    [*] --> Closed : Initial State
    
    Closed --> Open : Failure Threshold<br/>Reached (50%)
    Open --> HalfOpen : Timeout Period<br/>Elapsed (60s)
    HalfOpen --> Closed : Success Threshold<br/>Met (3 requests)
    HalfOpen --> Open : Failure Detected
    
    state Closed {
        [*] --> MonitoringCalls
        MonitoringCalls --> CountingFailures
        CountingFailures --> [*]
    }
    
    state Open {
        [*] --> RejectingCalls
        RejectingCalls --> FallbackResponse
        FallbackResponse --> [*]
    }
    
    state HalfOpen {
        [*] --> TestingService
        TestingService --> EvaluatingResults
        EvaluatingResults --> [*]
    }
    
    note right of Closed
        Normal operation
        Calls flow through
        Monitor failure rate
    end note
    
    note right of Open
        Service unavailable
        Fast-fail responses
        Use cached data
    end note
    
    note right of HalfOpen
        Limited test calls
        Evaluate service health
        Decide next state
    end note
```

---

## 📈 Métricas y Monitoring

### 🎯 Dashboard de Métricas

```mermaid
graph TB
    subgraph "📊 Business Metrics"
        ActiveCarts[🛒 Carritos Activos<br/>Real-time count]
        AvgItems[📦 Items Promedio<br/>Por carrito]
        Conversion[💰 Tasa Conversión<br/>Carrito → Compra]
        Revenue[💵 Valor Promedio<br/>Por carrito]
    end
    
    subgraph "⚡ Technical Metrics"
        Latency[⏱️ Latencia P95<br/>< 200ms]
        Throughput[🔄 Throughput<br/>req/sec]
        ErrorRate[❌ Error Rate<br/>< 0.1%]
        CacheHit[⚡ Cache Hit Ratio<br/>> 80%]
    end
    
    subgraph "🔍 Observability"
        Logs[📝 Structured Logs<br/>JSON format]
        Traces[🔍 Distributed Tracing<br/>Request correlation]
        Alerts[🚨 Smart Alerts<br/>Anomaly detection]
        Health[💚 Health Checks<br/>Dependencies]
    end
    
    %% Connections
    ActiveCarts -.-> Logs
    Latency -.-> Traces
    ErrorRate -.-> Alerts
    CacheHit -.-> Health
```

---

*Diagramas generados para el microservicio de carrito de compras*  
*Proyecto: PYMES E-commerce*  
*Última actualización: 30 de septiembre de 2025*