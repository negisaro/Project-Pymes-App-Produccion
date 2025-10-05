# Mejoras Pendientes - Optimizaciones de Performance

**Fecha:** 1 de octubre de 2025  
**Estado:** 📋 PENDIENTE  
**Prioridad:** MEDIA-ALTA  
**Estimación:** 4-6 horas  

## 🎯 Objetivo

Implementar optimizaciones específicas de performance en las entidades para mejorar throughput, reducir latencia y optimizar el uso de recursos.

## 🚀 Optimizaciones de Cache

### 1. **Cache de Segundo Nivel en Entidades**
**Prioridad:** ALTA  

#### **Carrito.java**
```java
// PENDIENTE: Agregar cache strategy
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "carrito-cache")
@Table(name = "carritos")
public class Carrito extends BaseEntityCorrected {
    
    // PENDIENTE: Cache para colección de items
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, 
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "carrito-items-cache")
    @BatchSize(size = 25) // PENDIENTE: Optimizar carga por lotes
    private List<ItemCarrito> items = new ArrayList<>();
}
```

#### **EstadoCarrito.java**
```java
// PENDIENTE: Cache inmutable para enum
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "estado-cache")
public enum EstadoCarrito {
    // Contenido existente...
}
```

### 2. **Query Cache para Consultas Frecuentes**
```java
// PENDIENTE: Agregar en repositorio
@QueryHints({
    @QueryHint(name = "org.hibernate.cacheable", value = "true"),
    @QueryHint(name = "org.hibernate.cacheRegion", value = "query-cache")
})
List<Carrito> findByUsuarioIdAndEstado(Long usuarioId, EstadoCarrito estado);
```

## 📊 Optimizaciones de Queries

### 1. **Fetch Strategies Optimizadas**

#### **ItemCarrito.java**
```java
// PENDIENTE: Optimizar relación con carrito
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "carrito_id", nullable = false)
@Fetch(FetchMode.SELECT) // PENDIENTE: Evitar N+1 queries
private Carrito carrito;
```

### 2. **Proyecciones para Consultas de Solo Lectura**
```java
// PENDIENTE: Crear interfaces de proyección
public interface CarritoSummaryProjection {
    Long getId();
    BigDecimal getTotal();
    Integer getTotalItems();
    EstadoCarrito getEstado();
}

public interface ItemCarritoProjection {
    Long getProductoId();
    String getNombreProducto();
    Integer getCantidad();
    BigDecimal getSubtotal();
}
```

### 3. **Índices Compuestos Adicionales**
```sql
-- PENDIENTE: Agregar en migration script
CREATE INDEX idx_carrito_usuario_estado_fecha 
ON carritos(usuario_id, estado, creado_en);

CREATE INDEX idx_item_carrito_producto_fecha 
ON items_carrito(carrito_id, producto_id, agregado_en);

CREATE INDEX idx_historial_carrito_operacion_fecha 
ON carrito_historial(carrito_id, tipo_operacion, fecha_operacion);
```

## ⚡ Optimizaciones de Batch Operations

### 1. **Batch Inserts Optimizados**
```yaml
# PENDIENTE: application.yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 25
          batch_versioned_data: true
        order_inserts: true
        order_updates: true
        batch_fetch_style: PADDED
```

### 2. **Batch Processing en Servicios**
```java
// PENDIENTE: Implementar en servicio
@Transactional
public void procesarCarritosLote(List<Long> carritoIds) {
    List<Carrito> carritos = carritoRepository.findAllById(carritoIds);
    
    // Procesar en lotes de 25
    Lists.partition(carritos, 25).forEach(lote -> {
        lote.forEach(this::procesarCarrito);
        entityManager.flush();
        entityManager.clear();
    });
}
```

## 🔍 Lazy Loading Optimizations

### 1. **Carrito.java - Optimizaciones**
```java
// PENDIENTE: Mejorar lazy loading
public class Carrito extends BaseEntityCorrected {
    
    // PENDIENTE: Optimizar carga de items
    @OneToMany(mappedBy = "carrito", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA) // Para count() sin cargar colección
    @BatchSize(size = 25)
    private List<ItemCarrito> items = new ArrayList<>();
    
    // PENDIENTE: Método optimizado para conteo
    @Formula("(SELECT COUNT(*) FROM items_carrito ic WHERE ic.carrito_id = id)")
    private Integer itemCount;
    
    public int getTotalItems() {
        // Usar formula si está disponible, sino calcular
        return itemCount != null ? itemCount : 
               items.stream().mapToInt(ItemCarrito::getCantidad).sum();
    }
}
```

### 2. **Fetch Graphs para Casos Específicos**
```java
// PENDIENTE: Agregar entity graphs
@NamedEntityGraph(
    name = "carrito-with-items",
    attributeNodes = {
        @NamedAttributeNode("items")
    }
)

@NamedEntityGraph(
    name = "carrito-summary",
    attributeNodes = {
        @NamedAttributeNode("estado"),
        @NamedAttributeNode("total")
    }
)
public class Carrito extends BaseEntityCorrected {
    // Contenido existente...
}
```

## 📈 Connection Pool Optimizations

### 1. **HikariCP Configuration**
```yaml
# PENDIENTE: Optimizar pool de conexiones
spring:
  datasource:
    hikari:
      maximum-pool-size: 25
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 900000
      leak-detection-threshold: 30000
      connection-test-query: SELECT 1
      pool-name: CarritoHikariPool
```

### 2. **Read/Write Splitting** (Futuro)
```yaml
# PENDIENTE: Para escalabilidad futura
spring:
  datasource:
    write:
      url: jdbc:mysql://master-db:3306/carrito_db
    read:
      url: jdbc:mysql://slave-db:3306/carrito_db
```

## 🏗️ Optimizaciones de Serialización

### 1. **JSON Serialization**
```java
// PENDIENTE: Agregar en entidades principales
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Carrito extends BaseEntityCorrected {
    
    // PENDIENTE: Serialización lazy de items
    @JsonManagedReference
    @JsonSerialize(using = LazyItemListSerializer.class)
    private List<ItemCarrito> items = new ArrayList<>();
}
```

### 2. **Custom Serializers**
```java
// PENDIENTE: Crear serializer personalizado
public class LazyItemListSerializer extends JsonSerializer<List<ItemCarrito>> {
    @Override
    public void serialize(List<ItemCarrito> items, JsonGenerator gen, 
                         SerializerProvider provider) throws IOException {
        if (Hibernate.isInitialized(items)) {
            gen.writeObject(items);
        } else {
            gen.writeStartArray();
            gen.writeEndArray(); // Empty array for uninitialized collections
        }
    }
}
```

## 🗂️ Partitioning Strategy (Futuro)

### 1. **Partición por Usuario**
```sql
-- PENDIENTE: Para gran escala
CREATE TABLE carritos_partitioned (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    -- otros campos...
    PRIMARY KEY (id, usuario_id)
) PARTITION BY HASH(usuario_id) PARTITIONS 16;
```

### 2. **Archiving Strategy**
```java
// PENDIENTE: Estrategia de archivado
@Entity
@Table(name = "carritos_archived")
public class CarritoArchived {
    // Misma estructura que Carrito para carritos antiguos
}
```

## 📊 Monitoring y Métricas

### 1. **Performance Metrics**
```java
// PENDIENTE: Agregar métricas personalizadas
@Component
public class CarritoPerformanceMetrics {
    
    private final Counter carritoCreationCounter;
    private final Timer itemAdditionTimer;
    private final Gauge activeCarritosGauge;
    
    // Implementar métricas de performance
}
```

### 2. **Query Performance Tracking**
```yaml
# PENDIENTE: Configurar logging de queries lentas
logging:
  level:
    org.hibernate.SQL_SLOW: WARN
spring:
  jpa:
    properties:
      hibernate:
        session.events.log.LOG_QUERIES_SLOWER_THAN_MS: 100
```

## 🎯 Optimizaciones de Memory

### 1. **Object Pool para Entidades Frecuentes**
```java
// PENDIENTE: Pool para objetos temporales
@Component
public class CarritoObjectPool {
    private final ObjectPool<CarritoCalculation> calculationPool;
    
    public CarritoCalculation borrowCalculation() {
        return calculationPool.borrowObject();
    }
    
    public void returnCalculation(CarritoCalculation calc) {
        calc.reset();
        calculationPool.returnObject(calc);
    }
}
```

### 2. **Weak References para Cache**
```java
// PENDIENTE: Cache con weak references
@Component
public class CarritoCache {
    private final Map<Long, WeakReference<Carrito>> cache = 
        new ConcurrentHashMap<>();
    
    public Optional<Carrito> get(Long id) {
        WeakReference<Carrito> ref = cache.get(id);
        return ref != null ? Optional.ofNullable(ref.get()) : Optional.empty();
    }
}
```

## 📋 Checklist de Implementación

### Performance Crítico
- [ ] **1. Implementar cache de segundo nivel**
- [ ] **2. Optimizar batch operations**
- [ ] **3. Agregar índices compuestos necesarios**
- [ ] **4. Configurar HikariCP optimalmente**
- [ ] **5. Implementar lazy loading optimizado**

### Performance Intermedio
- [ ] **6. Crear proyecciones para queries de solo lectura**
- [ ] **7. Implementar entity graphs**
- [ ] **8. Optimizar serialización JSON**
- [ ] **9. Configurar query cache**
- [ ] **10. Implementar métricas de performance**

### Performance Avanzado
- [ ] **11. Evaluar estrategias de partitioning**
- [ ] **12. Implementar read/write splitting**
- [ ] **13. Crear object pools para objetos frecuentes**
- [ ] **14. Optimizar memory management**
- [ ] **15. Implementar archiving strategy**

## 📈 Beneficios Esperados

### Throughput
- ⬆️ **+40%** en operaciones de lectura con cache L2
- ⬆️ **+25%** en operaciones de escritura con batch processing
- ⬆️ **+30%** en queries complejas con índices optimizados

### Latencia
- ⬇️ **-50%** en tiempo de respuesta para carritos frecuentes
- ⬇️ **-35%** en carga de items con batch size optimizado
- ⬇️ **-40%** en queries de búsqueda con proyecciones

### Recursos
- ⬇️ **-30%** en uso de memoria con lazy loading optimizado
- ⬇️ **-25%** en conexiones de BD con pool optimizado
- ⬇️ **-20%** en CPU con cache hits mejorados

---

**Próximo paso:** Priorizar implementación según impacto vs esfuerzo.  
**Dependencias:** Configuración JPA base debe estar implementada.