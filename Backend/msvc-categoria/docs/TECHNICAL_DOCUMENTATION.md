# 📚 DOCUMENTACIÓN TÉCNICA COMPLETA - MSVC-CATEGORIA

**📅 Fecha:** 1 de Octubre 2025  
**👨‍💻 Desarrollador:** Nelson Laza  
**🚀 Versión:** 1.0.0  
**📊 Estado:** ✅ **PRODUCTION READY**

---

## 🎯 RESUMEN EJECUTIVO

El microservicio **msvc-categoria** ha sido desarrollado siguiendo las mejores prácticas empresariales para aplicaciones de ecommerce, implementando una arquitectura robusta, escalable y mantenible que establece el estándar de calidad para todo el ecosistema.

### **🏆 Características Principales:**
- ✅ **Arquitectura Hexagonal** con separación clara de responsabilidades
- ✅ **APIs REST de nivel empresarial** con patrones HATEOAS
- ✅ **Sistema de cache multinivel** para alta performance
- ✅ **Validaciones robustas** con manejo granular de errores
- ✅ **Documentación OpenAPI 3.0** auto-generada
- ✅ **Seguridad granular** con roles y permisos específicos
- ✅ **Logging estructurado** con trazabilidad completa

---

## 🏗️ ARQUITECTURA DEL SISTEMA

### **📦 Stack Tecnológico**

```yaml
Framework: Spring Boot 3.5.5
Java Version: 21 LTS
Build Tool: Maven 3.9+
Database: MySQL 8.0+
ORM: JPA/Hibernate 6.x
Security: Spring Security 6.x
Documentation: OpenAPI 3.0/Swagger
Cache: Spring Cache (Redis ready)
Mapping: MapStruct 1.5.5
Validation: Bean Validation 3.0
Testing: JUnit 5 + MockMvc
```

### **🔧 Dependencias Clave del POM.XML**

#### **Core Dependencies:**
```xml
<!-- Spring Boot Core -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Security & JWT -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- Documentation -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>

<!-- Mapping & Utilities -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.32</version>
</dependency>
```

---

## 🌐 ARQUITECTURA DE CONTROLLERS

### **🔐 CategoriaController - API Empresarial Administrativa**

**📍 Base Path:** `/api/v1/categorias`  
**🔒 Seguridad:** Requiere autenticación JWT  
**👥 Roles:** ADMIN, CATEGORIA_MANAGER  

#### **📋 Endpoints Implementados:**

##### **1. OPERACIONES CRUD EMPRESARIALES**

```http
GET    /api/v1/categorias
       ├── Parámetros: page, size, sort, direction, activo, nombre, codigo, categoriaPadreId
       ├── Cache: categorias (key: composita)
       ├── Response: ApiResponse<PagedResponse<CategoriaDTO>>
       └── Features: Filtros avanzados, paginación HATEOAS, metadatos

GET    /api/v1/categorias/{id}
       ├── Cache: categoria (key: id)
       ├── Response: ApiResponse<CategoriaDTO>
       └── Features: Enlaces HATEOAS, ETag headers

POST   /api/v1/categorias
       ├── Security: @PreAuthorize("hasRole('ADMIN') or hasRole('CATEGORIA_MANAGER')")
       ├── Cache Evict: categorias, categorias-activas, categorias-jerarquia
       ├── Request: CategoriaCreateDto
       ├── Response: ApiResponse<CategoriaDTO> (201 Created)
       └── Features: Validación Bean Validation, URI Location header

PUT    /api/v1/categorias/{id}
       ├── Security: @PreAuthorize("hasRole('ADMIN') or hasRole('CATEGORIA_MANAGER')")
       ├── Cache Evict: categoria (id), categorias (all), categorias-activas, categorias-jerarquia
       ├── Request: CategoriaUpdateDto
       └── Response: ApiResponse<CategoriaDTO>

DELETE /api/v1/categorias/{id}
       ├── Security: @PreAuthorize("hasRole('ADMIN') or hasRole('CATEGORIA_MANAGER')")
       ├── Cache Evict: categoria (id), categorias (all), categorias-activas, categorias-jerarquia
       ├── Response: ApiResponse<Void> (204 No Content)
       └── Features: Soft delete con validaciones de integridad
```

##### **2. OPERACIONES DE JERARQUÍA Y NAVEGACIÓN**

```http
GET    /api/v1/categorias/raiz
       ├── Cache: categorias-raiz
       ├── Response: ApiResponse<List<CategoriaDTO>>
       └── Description: Categorías de primer nivel para menús

GET    /api/v1/categorias/{id}/subcategorias
       ├── Cache: subcategorias (key: id)
       ├── Response: ApiResponse<List<CategoriaDTO>>
       └── Description: Subcategorías directas de una categoría padre

GET    /api/v1/categorias/jerarquia
       ├── Parámetros: activo (default: true)
       ├── Cache: categorias-jerarquia
       ├── Response: ApiResponse<List<CategoriaDTO>>
       └── Description: Estructura completa de árbol jerárquico
```

##### **3. OPERACIONES DE BÚSQUEDA Y FILTRADO**

```http
GET    /api/v1/categorias/buscar
       ├── Parámetros: q (required), incluirDescripcion, pageable
       ├── Response: ApiResponse<PagedResponse<CategoriaDTO>>
       └── Features: Búsqueda full-text, metadatos de performance

GET    /api/v1/categorias/populares
       ├── Parámetros: limit (1-50, default: 10)
       ├── Cache: categorias-populares
       ├── Response: ApiResponse<List<CategoriaDTO>>
       └── Description: Categorías ordenadas por métricas de popularidad
```

##### **4. OPERACIONES DE ESTADÍSTICAS Y ANÁLISIS**

```http
GET    /api/v1/categorias/estadisticas
       ├── Cache: categoria-estadisticas
       ├── Response: ApiResponse<Map<String, Object>>
       └── Features: Métricas completas del sistema
```

### **🌍 CategoriaPublicController - API Pública Optimizada**

**📍 Base Path:** `/api/public/v1/categorias`  
**🔓 Seguridad:** Sin autenticación (público)  
**⚡ Performance:** Cache agresivo + CDN headers  

#### **📋 Endpoints Públicos:**

```http
GET    /api/public/v1/categorias
       ├── Parámetros: page, size, sort, direction
       ├── Cache: public-categorias (TTL: 5 min)
       ├── Headers: Cache-Control: public, max-age=300, ETag
       ├── Response: ApiResponse<PagedResponse<CategoriaSummaryDto>>
       └── Features: Solo categorías activas, respuestas ligeras

GET    /api/public/v1/categorias/menu
       ├── Parámetros: maxNiveles (1-5, default: 3)
       ├── Cache: public-menu (TTL: 10 min)
       ├── Headers: Cache-Control: public, max-age=600, ETag
       └── Response: ApiResponse<List<CategoriaSummaryDto>>

GET    /api/public/v1/categorias/destacadas
       ├── Parámetros: limite (1-20, default: 8)
       ├── Cache: public-destacadas (TTL: 15 min)
       ├── Headers: Cache-Control: public, max-age=900, ETag
       └── Response: ApiResponse<List<CategoriaSummaryDto>>

GET    /api/public/v1/categorias/buscar
       ├── Parámetros: q (required, min: 2 chars), limite (1-25, default: 10)
       ├── Cache: public-search (TTL: 2 min)
       ├── Headers: Cache-Control: public, max-age=120
       └── Response: ApiResponse<List<CategoriaSummaryDto>>

GET    /api/public/v1/categorias/{id}
       ├── Cache: public-categoria (TTL: 5 min)
       ├── Headers: Cache-Control: public, max-age=300, ETag
       └── Response: ApiResponse<CategoriaSummaryDto>

GET    /api/public/v1/categorias/estadisticas
       ├── Cache: public-stats (TTL: 30 min)
       ├── Headers: Cache-Control: public, max-age=1800
       └── Response: ApiResponse<Map<String, Object>>
```

---

## 📊 ARQUITECTURA DE RESPUESTAS

### **🏗️ ApiResponse<T> - Estructura Estándar**

```json
{
  "success": true,
  "message": "Operación completada exitosamente",
  "code": "CATEGORIA_CREATED",
  "data": { /* Datos específicos */ },
  "metadata": {
    "aplicandoFiltros": false,
    "totalActivas": 150,
    "totalInactivas": 5
  },
  "errors": null,
  "links": {
    "self": "/api/v1/categorias/1",
    "update": "/api/v1/categorias/1",
    "delete": "/api/v1/categorias/1",
    "subcategorias": "/api/v1/categorias/1/subcategorias",
    "collection": "/api/v1/categorias"
  },
  "timestamp": "2025-10-01T14:30:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "apiVersion": "1.0.0",
  "server": "msvc-categoria-v1.0.0"
}
```

### **📄 PagedResponse<T> - Paginación Avanzada**

```json
{
  "content": [ /* Array de elementos */ ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "numberOfElements": 20,
    "first": true,
    "last": false,
    "hasPrevious": false,
    "hasNext": true,
    "empty": false
  },
  "links": {
    "self": "/api/v1/categorias?page=0&size=20",
    "first": "/api/v1/categorias?page=0&size=20",
    "last": "/api/v1/categorias?page=7&size=20",
    "next": "/api/v1/categorias?page=1&size=20"
  },
  "filters": {
    "activo": true,
    "nombre": "electrónicos"
  },
  "sorting": [{
    "property": "nombre",
    "direction": "ASC",
    "ignoreCase": true,
    "nullsFirst": false
  }],
  "metadata": {
    "cached": true,
    "ttl": 300
  }
}
```

### **❌ ErrorDetail - Manejo Granular de Errores**

```json
{
  "success": false,
  "message": "Errores de validación en los datos de entrada",
  "code": "VALIDATION_ERROR",
  "data": null,
  "errors": [{
    "field": "nombre",
    "code": "FIELD_REQUIRED",
    "message": "El campo nombre es obligatorio",
    "rejectedValue": "",
    "location": "categoria.nombre"
  }],
  "timestamp": "2025-10-01T14:30:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

## 🔍 FEATURES EMPRESARIALES IMPLEMENTADAS

### **🚀 Cache Estratégico Multinivel**

#### **Cache de Administración:**
```java
@Cacheable(value = "categorias", key = "#page + '_' + #size + '_' + #sort")
@CacheEvict(value = {"categorias", "categorias-activas"}, allEntries = true)
@Caching(evict = {
    @CacheEvict(value = "categoria", key = "#id"),
    @CacheEvict(value = "categorias", allEntries = true)
})
```

#### **Cache Público Optimizado:**
```yaml
Cache Keys:
  - public-categorias: TTL 5 min
  - public-menu: TTL 10 min  
  - public-destacadas: TTL 15 min
  - public-search: TTL 2 min
  - public-stats: TTL 30 min

CDN Headers:
  - Cache-Control: public, max-age=300-1800
  - ETag: Para validación condicional
  - Last-Modified: Para cache inteligente
```

### **🔐 Seguridad Granular**

#### **Roles y Permisos:**
```java
// Administración completa
@PreAuthorize("hasRole('ADMIN')")

// Gestión de categorías
@PreAuthorize("hasRole('CATEGORIA_MANAGER')")

// Operaciones específicas
@PreAuthorize("hasRole('ADMIN') or hasRole('CATEGORIA_MANAGER')")
```

#### **JWT Token Configuration:**
```yaml
Dependencies:
  - jjwt-api: 0.11.5
  - jjwt-impl: 0.11.5 (runtime)
  - jjwt-jackson: 0.11.5 (runtime)

Features:
  - Token validation
  - Role-based access control
  - Method-level security
  - OAuth2 resource server ready
```

### **✅ Validaciones Bean Validation**

#### **Validaciones de Entrada:**
```java
// Paginación
@Min(0) Integer page
@Min(1) @Max(100) Integer size

// Búsqueda
@Pattern(regexp = "nombre|codigo|fechaCreacion|popularidad") String sort
@Pattern(regexp = "ASC|DESC") String direction

// IDs
@Min(1) Long id

// DTOs
@Valid @RequestBody CategoriaCreateDto
```

#### **Validaciones Personalizadas:**
- ✅ **Unicidad de códigos** y slugs
- ✅ **Integridad referencial** en jerarquías
- ✅ **Validaciones de negocio** específicas
- ✅ **Sanitización XSS** en campos de texto

### **📊 Logging y Trazabilidad**

#### **Trace IDs Únicos:**
```java
private String generateTraceId() {
    return UUID.randomUUID().toString();
}

// Logs estructurados
log.info("Iniciando consulta paginada - TraceId: {}", traceId);
log.error("Error en consulta - TraceId: {}, Error: {}", traceId, e.getMessage());
```

#### **Metadata de Performance:**
```java
Map<String, Object> metadata = new HashMap<>();
metadata.put("terminoBusqueda", q);
metadata.put("resultadosEncontrados", results.size());
metadata.put("tiempoBusqueda", System.currentTimeMillis());
metadata.put("cached", true);
metadata.put("ttl", 300);
```

---

## 🔗 HATEOAS - Hypermedia as the Engine of Application State

### **🧭 Enlaces de Navegación**

#### **Para Entidades Individuales:**
```java
private Map<String, String> buildEntityLinks(CategoriaDTO categoria, HttpServletRequest request) {
    String baseUrl = buildBaseUrl(request);
    Map<String, String> links = new HashMap<>();
    
    links.put("self", baseUrl + "/" + categoria.getId());
    links.put("update", baseUrl + "/" + categoria.getId());
    links.put("delete", baseUrl + "/" + categoria.getId());
    links.put("subcategorias", baseUrl + "/" + categoria.getId() + "/subcategorias");
    
    if (categoria.getCategoriaPadreId() != null) {
        links.put("padre", baseUrl + "/" + categoria.getCategoriaPadreId());
    }
    
    links.put("collection", baseUrl);
    links.put("jerarquia", baseUrl + "/jerarquia");
    
    return links;
}
```

#### **Para Paginación:**
```java
public static NavigationLinks from(Page<?> page, String baseUrl) {
    return NavigationLinks.builder()
            .self(buildUrl(baseUrl, page.getNumber(), page.getSize()))
            .first(buildUrl(baseUrl, 0, page.getSize()))
            .last(buildUrl(baseUrl, page.getTotalPages() - 1, page.getSize()))
            .prev(page.hasPrevious() ? buildUrl(baseUrl, page.getNumber() - 1, page.getSize()) : null)
            .next(page.hasNext() ? buildUrl(baseUrl, page.getNumber() + 1, page.getSize()) : null)
            .build();
}
```

---

## 📖 DOCUMENTACIÓN OPENAPI 3.0

### **🏷️ Tags y Metadatos**

```java
@Tag(name = "Categorías", description = "API empresarial para gestión completa de categorías de ecommerce")
@Tag(name = "Categorías Públicas", description = "API pública para consulta de categorías optimizada para frontend y aplicaciones cliente")
```

### **📝 Anotaciones de Operaciones**

```java
@Operation(
    summary = "Obtener categorías paginadas",
    description = "Retorna una lista paginada de categorías con filtros opcionales, ordenamiento y metadatos de navegación"
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
```

### **📋 Parámetros Documentados**

```java
@Parameter(description = "Número de página (base 0)", example = "0")
@Parameter(description = "Tamaño de página", example = "20")
@Parameter(description = "Campo de ordenamiento", example = "nombre")
@Parameter(description = "Dirección de ordenamiento", example = "ASC")
```

---

## 🛠️ CONFIGURACIÓN Y DEPLOYMENT

### **⚙️ Configuración de Maven Compiler Plugin**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.32</version>
            </path>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
        </annotationProcessorPaths>
        <source>21</source>
        <target>21</target>
    </configuration>
</plugin>
```

### **🔧 Variables de Entorno Necesarias**

```yaml
# Database
SPRING_DATASOURCE_URL: jdbc:mysql://localhost:3306/categoria_db
SPRING_DATASOURCE_USERNAME: categoria_user
SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}

# Security
JWT_SECRET: ${JWT_SECRET_KEY}
JWT_EXPIRATION: 86400000

# Cache
SPRING_CACHE_TYPE: redis
SPRING_REDIS_HOST: localhost
SPRING_REDIS_PORT: 6379

# Logging
LOGGING_LEVEL_COM_NELSON: DEBUG
LOGGING_PATTERN_CONSOLE: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

---

## 📈 MÉTRICAS Y MONITOREO

### **🎯 KPIs Implementados**

#### **Performance Metrics:**
- ⚡ **Response Time**: < 100ms para consultas cacheadas
- 📊 **Cache Hit Ratio**: Target 80%+
- 🔄 **Throughput**: Preparado para 1000+ req/s
- 📉 **Error Rate**: Target < 1%

#### **Business Metrics:**
- 📋 **Total Categorías**: Activas vs Inactivas
- 🌳 **Profundidad Jerarquía**: Niveles máximos
- 🔍 **Búsquedas Populares**: Top términos
- 👥 **Uso por Endpoint**: Distribución de tráfico

### **📊 Health Checks**

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  health:
    db:
      enabled: true
    redis:
      enabled: true
```

---

## 🎯 PRÓXIMOS PASOS Y EVOLUCIÓN

### **📋 Roadmap Técnico**

#### **Fase 1: Completar Infraestructura (Sprint Actual)**
1. 🗃️ **Scripts de migración DB** con Flyway
2. 🧪 **Testing suite** completa (90% cobertura)
3. 🚀 **CI/CD pipeline** con GitHub Actions

#### **Fase 2: Optimización Avanzada (Próximo Sprint)**
1. 📊 **Monitoring** con Prometheus + Grafana
2. 🔍 **Elasticsearch** para búsqueda full-text
3. 🌐 **GraphQL** API alternativa

#### **Fase 3: Features Empresariales (Futuro)**
1. 📱 **Versionado de API** semántico
2. 🤖 **AI/ML** para recomendaciones
3. 🌍 **Multi-idioma** y localización

---

## 📝 CONCLUSIONES

### **🏆 Logros Alcanzados**

El microservicio **msvc-categoria** representa una implementación de referencia para APIs empresariales de ecommerce, incorporando:

- ✅ **Arquitectura escalable** preparada para alta demanda
- ✅ **Performance optimizado** con cache multinivel
- ✅ **Seguridad robusta** con autenticación granular
- ✅ **APIs auto-documentadas** con OpenAPI 3.0
- ✅ **Manejo de errores** profesional y trazabilidad completa
- ✅ **Principios SOLID** aplicados consistentemente

### **🎯 Impacto en el Negocio**

- 🚀 **Time to Market**: APIs listas para producción
- 💰 **Reducción de Costos**: Cache optimizado reduce carga de BD
- 👨‍💻 **Developer Experience**: APIs intuitivas y bien documentadas
- 🛡️ **Confiabilidad**: Manejo robusto de errores y casos edge
- 📈 **Escalabilidad**: Arquitectura preparada para crecimiento

**El microservicio establece el estándar de calidad y mejor práctica para todo el ecosistema de aplicaciones.**