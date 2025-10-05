# Mejoras Pendientes - Configuración JPA y Hibernate

**Fecha:** 1 de octubre de 2025  
**Estado:** 📋 PENDIENTE  
**Prioridad:** MEDIA  
**Estimación:** 2-4 horas  

## 🎯 Objetivo

Implementar optimizaciones de configuración JPA/Hibernate para mejorar performance, consistencia y mantenibilidad del microservicio carrito.

## 📋 Mejoras Pendientes

### 1. **Configuración de Naming Strategy** 
**Prioridad:** ALTA  
**Archivo:** `application.yml`

```yaml
# PENDIENTE: Agregar configuración de naming strategy
spring:
  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
```

**Beneficios:**
- ✅ Consistencia automática en nombres de tablas y columnas
- ✅ Reduce errores de mapping manual
- ✅ Seguimiento de convenciones estándar

### 2. **Optimización de Configuración Hibernate**
**Prioridad:** ALTA  
**Archivo:** `application.yml`

```yaml
# PENDIENTE: Configuración optimizada de Hibernate
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        jdbc:
          batch_size: 25
          fetch_size: 25
        order_inserts: true
        order_updates: true
        generate_statistics: false
        use_sql_comments: false # Solo en desarrollo
        format_sql: false # Solo en desarrollo
```

**Beneficios:**
- ✅ Mejora performance con batch operations
- ✅ Optimiza queries con fetch size apropiado
- ✅ Reduce overhead con estadísticas deshabilitadas

### 3. **Configuración de Cache de Segundo Nivel**
**Prioridad:** MEDIA  
**Archivo:** `application.yml`

```yaml
# PENDIENTE: Configuración de cache L2
spring:
  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true
          region.factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
```

**Archivos a modificar:**
- `Carrito.java`: Agregar `@Cacheable`
- `EstadoCarrito.java`: Agregar `@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)`

### 4. **Configuración de Pool de Conexiones**
**Prioridad:** ALTA  
**Archivo:** `application.yml`

```yaml
# PENDIENTE: Optimizar pool de conexiones
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
```

### 5. **Configuración de Auditoría JPA**
**Prioridad:** ALTA  
**Archivo:** `JpaAuditingConfiguration.java` (NUEVO)

```java
// PENDIENTE: Crear configuración de auditoría
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}

// PENDIENTE: Implementar AuditorAware
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Implementar lógica para obtener usuario actual
        return Optional.of("SYSTEM"); // Placeholder
    }
}
```

### 6. **Configuración de Validación**
**Prioridad:** MEDIA  
**Archivo:** `application.yml`

```yaml
# PENDIENTE: Configurar validación JPA
spring:
  jpa:
    properties:
      javax.persistence.validation.mode: AUTO
      hibernate.check_nullability: true
```

## 🏗️ Cambios en Entidades

### 1. **BaseEntityCorrected.java**
```java
// PENDIENTE: Agregar configuración de cache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class BaseEntityCorrected {
    // Contenido existente...
}
```

### 2. **Carrito.java**
```java
// PENDIENTE: Optimizar relaciones
@OneToMany(
    mappedBy = "carrito",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
@BatchSize(size = 25) // PENDIENTE: Agregar
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) // PENDIENTE
private List<ItemCarrito> items = new ArrayList<>();
```

### 3. **ItemCarrito.java**
```java
// PENDIENTE: Optimizar foreign key
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "carrito_id", 
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_item_carrito") // PENDIENTE
)
private Carrito carrito;
```

## 📊 Configuración de Monitoreo

### 1. **Métricas de Hibernate**
```yaml
# PENDIENTE: Agregar métricas
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,hibernate
  metrics:
    export:
      prometheus:
        enabled: true
```

### 2. **Logging de Performance**
```yaml
# PENDIENTE: Configurar logging específico
logging:
  level:
    org.hibernate.SQL: WARN # DEBUG solo en desarrollo
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
    org.hibernate.stat: INFO
```

## 🔧 Configuración de Profiles

### 1. **application-dev.yml**
```yaml
# PENDIENTE: Configuración para desarrollo
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        generate_statistics: true
```

### 2. **application-prod.yml**
```yaml
# PENDIENTE: Configuración para producción
spring:
  jpa:
    show-sql: false
    properties:
      hibernate:
        format_sql: false
        use_sql_comments: false
        generate_statistics: false
```

## 📈 Beneficios Esperados

### Performance
- ⬆️ **+30%** en throughput con batch operations
- ⬇️ **-40%** en uso de memoria con cache L2
- ⬇️ **-25%** en latencia de queries frecuentes

### Mantenibilidad
- ✅ Configuración centralizada y consistente
- ✅ Profiles específicos por ambiente
- ✅ Monitoring y debugging mejorado

### Escalabilidad
- ✅ Pool de conexiones optimizado
- ✅ Cache strategy preparado para múltiples nodos
- ✅ Configuración production-ready

## 📋 Checklist de Implementación

- [ ] **1. Configurar naming strategy en application.yml**
- [ ] **2. Optimizar configuración de Hibernate**
- [ ] **3. Implementar cache de segundo nivel**
- [ ] **4. Configurar pool de conexiones HikariCP**
- [ ] **5. Crear configuración de auditoría JPA**
- [ ] **6. Agregar anotaciones de cache en entidades**
- [ ] **7. Optimizar foreign keys y batch operations**
- [ ] **8. Configurar métricas y monitoring**
- [ ] **9. Separar configuración por profiles**
- [ ] **10. Realizar pruebas de performance**

## 🎯 Criterios de Aceptación

- ✅ Todas las configuraciones deben estar en archivos YAML
- ✅ Debe mantener compatibilidad con código existente
- ✅ Profiles dev/prod deben estar claramente separados
- ✅ Métricas de performance deben estar disponibles
- ✅ No debe haber breaking changes en la API

---

**Próximo paso:** Implementar mejoras en orden de prioridad.  
**Dependencias:** Ninguna - puede implementarse incrementalmente.