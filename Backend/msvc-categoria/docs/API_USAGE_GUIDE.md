# 🚀 GUÍA DE DESARROLLO Y API - MSVC-CATEGORIA

**📅 Fecha:** 1 de Octubre 2025  
**👨‍💻 Desarrollador:** Nelson Laza  
**🎯 Audiencia:** Desarrolladores, DevOps, QA  
**📊 Estado:** ✅ **PRODUCTION READY**

---

## 🎯 INICIO RÁPIDO

### **🚀 Ejecución Local**

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd msvc-categoria

# 2. Configurar variables de entorno
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/categoria_db
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=password
export JWT_SECRET=mySecretKey

# 3. Ejecutar la aplicación
mvn spring-boot:run

# 4. Verificar funcionamiento
curl http://localhost:8080/actuator/health
```

### **📖 Documentación Interactiva**

Una vez ejecutando la aplicación, accede a:

- **🌐 Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **📋 OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **❤️ Health Check**: `http://localhost:8080/actuator/health`

---

## 📚 GUÍA DE APIS

### **🔐 Autenticación**

Todas las APIs administrativas requieren token JWT:

```bash
# 1. Obtener token (endpoint de auth del gateway)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'

# 2. Usar token en requests
curl -X GET http://localhost:8080/api/v1/categorias \
  -H "Authorization: Bearer <your-jwt-token>"
```

### **🌐 APIs Administrativas**

#### **📋 Listar Categorías con Filtros**

```bash
# Listar todas (paginado)
GET /api/v1/categorias?page=0&size=20&sort=nombre&direction=ASC

# Con filtros
GET /api/v1/categorias?activo=true&nombre=electrónicos&categoriaPadreId=1
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Categorías obtenidas exitosamente",
  "code": "CATEGORIAS_RETRIEVED",
  "data": {
    "content": [
      {
        "id": 1,
        "codigo": "ELEC001",
        "nombre": "Electrónicos",
        "descripcion": "Productos electrónicos",
        "slug": "electronicos",
        "activo": true,
        "categoria_padre_id": null
      }
    ],
    "page": {
      "number": 0,
      "size": 20,
      "totalElements": 1,
      "totalPages": 1
    },
    "links": {
      "self": "/api/v1/categorias?page=0&size=20",
      "first": "/api/v1/categorias?page=0&size=20",
      "last": "/api/v1/categorias?page=0&size=20"
    }
  },
  "timestamp": "2025-10-01T14:30:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### **🔍 Obtener Categoría por ID**

```bash
GET /api/v1/categorias/1
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Categoría encontrada",
  "code": "CATEGORIA_FOUND",
  "data": {
    "id": 1,
    "codigo": "ELEC001",
    "nombre": "Electrónicos",
    "descripcion": "Productos electrónicos y tecnología",
    "slug": "electronicos",
    "activo": true,
    "eliminado": false,
    "categoria_padre_id": null
  },
  "links": {
    "self": "/api/v1/categorias/1",
    "update": "/api/v1/categorias/1",
    "delete": "/api/v1/categorias/1",
    "subcategorias": "/api/v1/categorias/1/subcategorias",
    "collection": "/api/v1/categorias"
  }
}
```

#### **✨ Crear Nueva Categoría**

```bash
POST /api/v1/categorias
Content-Type: application/json
Authorization: Bearer <token>

{
  "codigo": "SMART001",
  "nombre": "Smartphones",
  "descripcion": "Teléfonos inteligentes",
  "categoria_padre_id": 1,
  "activo": true,
  "orden": 1
}
```

**Respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Categoría creada exitosamente",
  "code": "CATEGORIA_CREATED",
  "data": {
    "id": 2,
    "codigo": "SMART001",
    "nombre": "Smartphones",
    "descripcion": "Teléfonos inteligentes",
    "slug": "smartphones",
    "activo": true,
    "categoria_padre_id": 1,
    "fecha_creacion": "2025-10-01T14:30:00"
  }
}
```

#### **🔄 Actualizar Categoría**

```bash
PUT /api/v1/categorias/2
Content-Type: application/json
Authorization: Bearer <token>

{
  "nombre": "Smartphones Premium",
  "descripcion": "Teléfonos inteligentes de gama alta",
  "activo": true
}
```

#### **🗑️ Eliminar Categoría (Soft Delete)**

```bash
DELETE /api/v1/categorias/2
Authorization: Bearer <token>
```

**Respuesta (204 No Content):**
```json
{
  "success": true,
  "message": "Categoría eliminada exitosamente",
  "code": "CATEGORIA_DELETED"
}
```

### **🌳 APIs de Jerarquía**

#### **📋 Categorías Raíz**

```bash
GET /api/v1/categorias/raiz
```

#### **👶 Subcategorías**

```bash
GET /api/v1/categorias/1/subcategorias
```

#### **🌳 Jerarquía Completa**

```bash
GET /api/v1/categorias/jerarquia?activo=true
```

### **🔍 APIs de Búsqueda**

#### **🔎 Búsqueda de Texto**

```bash
GET /api/v1/categorias/buscar?q=electrónicos&incluirDescripcion=true&page=0&size=10
```

#### **🏆 Categorías Populares**

```bash
GET /api/v1/categorias/populares?limit=10
```

### **📊 APIs de Estadísticas**

```bash
GET /api/v1/categorias/estadisticas
```

**Respuesta:**
```json
{
  "success": true,
  "data": {
    "totalCategorias": 156,
    "categoriasActivas": 142,
    "categoriasInactivas": 14,
    "categoriasRaiz": 8,
    "promedioSubcategorias": 3.2,
    "nivelMaximoJerarquia": 4,
    "categoriaSinProductos": 12,
    "categoriaConMasProductos": {
      "id": 1,
      "nombre": "Electrónicos",
      "totalProductos": 245
    }
  }
}
```

---

## 🌍 APIs PÚBLICAS (Sin Autenticación)

### **📱 Optimizadas para Frontend**

#### **📋 Listar Categorías Activas**

```bash
GET /api/public/v1/categorias?page=0&size=20&sort=nombre
```

#### **🌳 Menú de Navegación**

```bash
GET /api/public/v1/categorias/menu?maxNiveles=3
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Menú de navegación obtenido",
  "data": [
    {
      "id": 1,
      "nombre": "Electrónicos",
      "slug": "electronicos",
      "subcategorias": [
        {
          "id": 2,
          "nombre": "Smartphones",
          "slug": "smartphones"
        }
      ]
    }
  ]
}
```

#### **🏆 Categorías Destacadas**

```bash
GET /api/public/v1/categorias/destacadas?limite=8
```

#### **🔎 Búsqueda Pública**

```bash
GET /api/public/v1/categorias/buscar?q=electrónicos&limite=10
```

#### **📊 Estadísticas Básicas**

```bash
GET /api/public/v1/categorias/estadisticas
```

---

## 🛠️ EJEMPLOS DE INTEGRACIÓN

### **🌐 JavaScript/Frontend**

```javascript
// Configuración base
const API_BASE = 'http://localhost:8080/api/public/v1/categorias';

// Obtener menú de navegación
async function getNavigationMenu() {
  try {
    const response = await fetch(`${API_BASE}/menu?maxNiveles=3`);
    const data = await response.json();
    
    if (data.success) {
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Error fetching menu:', error);
  }
}

// Búsqueda con autocompletado
async function searchCategories(query) {
  if (query.length < 2) return [];
  
  try {
    const response = await fetch(`${API_BASE}/buscar?q=${encodeURIComponent(query)}&limite=10`);
    const data = await response.json();
    
    return data.success ? data.data : [];
  } catch (error) {
    console.error('Error searching:', error);
    return [];
  }
}

// Obtener categorías destacadas
async function getFeaturedCategories() {
  try {
    const response = await fetch(`${API_BASE}/destacadas?limite=8`);
    const data = await response.json();
    
    return data.success ? data.data : [];
  } catch (error) {
    console.error('Error fetching featured:', error);
    return [];
  }
}
```

### **☕ Java/Spring Boot (Cliente)**

```java
@Service
public class CategoriaClientService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://msvc-categoria:8080/api/public/v1/categorias";
    
    public List<CategoriaSummaryDto> getNavigationMenu(int maxNiveles) {
        try {
            String url = baseUrl + "/menu?maxNiveles=" + maxNiveles;
            ApiResponse<List<CategoriaSummaryDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<List<CategoriaSummaryDto>>>() {}
            ).getBody();
            
            return response != null && response.getSuccess() ? response.getData() : Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error fetching navigation menu", e);
            return Collections.emptyList();
        }
    }
    
    public PagedResponse<CategoriaSummaryDto> getCategories(int page, int size) {
        try {
            String url = baseUrl + "?page=" + page + "&size=" + size;
            ApiResponse<PagedResponse<CategoriaSummaryDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PagedResponse<CategoriaSummaryDto>>>() {}
            ).getBody();
            
            return response != null && response.getSuccess() ? response.getData() : null;
            
        } catch (Exception e) {
            log.error("Error fetching categories", e);
            return null;
        }
    }
}
```

### **🐍 Python**

```python
import requests
from typing import List, Optional, Dict, Any

class CategoriaClient:
    def __init__(self, base_url: str = "http://localhost:8080/api/public/v1/categorias"):
        self.base_url = base_url
    
    def get_navigation_menu(self, max_niveles: int = 3) -> List[Dict[str, Any]]:
        """Obtiene el menú de navegación"""
        try:
            response = requests.get(f"{self.base_url}/menu", params={"maxNiveles": max_niveles})
            response.raise_for_status()
            
            data = response.json()
            return data.get("data", []) if data.get("success") else []
            
        except requests.RequestException as e:
            print(f"Error fetching navigation menu: {e}")
            return []
    
    def search_categories(self, query: str, limite: int = 10) -> List[Dict[str, Any]]:
        """Busca categorías por texto"""
        if len(query) < 2:
            return []
            
        try:
            response = requests.get(f"{self.base_url}/buscar", params={"q": query, "limite": limite})
            response.raise_for_status()
            
            data = response.json()
            return data.get("data", []) if data.get("success") else []
            
        except requests.RequestException as e:
            print(f"Error searching categories: {e}")
            return []
    
    def get_featured_categories(self, limite: int = 8) -> List[Dict[str, Any]]:
        """Obtiene categorías destacadas"""
        try:
            response = requests.get(f"{self.base_url}/destacadas", params={"limite": limite})
            response.raise_for_status()
            
            data = response.json()
            return data.get("data", []) if data.get("success") else []
            
        except requests.RequestException as e:
            print(f"Error fetching featured categories: {e}")
            return []

# Uso
client = CategoriaClient()
menu = client.get_navigation_menu(3)
featured = client.get_featured_categories(8)
search_results = client.search_categories("electrónicos")
```

---

## 🧪 TESTING

### **🔬 Tests Unitarios**

```bash
# Ejecutar todos los tests
mvn test

# Con cobertura
mvn test jacoco:report

# Solo tests de controllers
mvn test -Dtest="*ControllerTest"
```

### **🌐 Tests de Integración**

```bash
# Tests con base de datos H2
mvn test -Dspring.profiles.active=test

# Tests de APIs completas
mvn test -Dtest="*IntegrationTest"
```

### **📊 Postman Collection**

```json
{
  "info": {
    "name": "MSVC-Categoria APIs",
    "description": "Collection completa para testing de APIs"
  },
  "item": [
    {
      "name": "Public APIs",
      "item": [
        {
          "name": "Get Navigation Menu",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/public/v1/categorias/menu?maxNiveles=3",
              "host": ["{{base_url}}"],
              "path": ["api", "public", "v1", "categorias", "menu"],
              "query": [{"key": "maxNiveles", "value": "3"}]
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080"
    }
  ]
}
```

---

## 🐞 TROUBLESHOOTING

### **❗ Problemas Comunes**

#### **🔐 Error 401 - Unauthorized**
```bash
# Verificar token JWT
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'

# Verificar formato del header
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### **❌ Error 400 - Validation Failed**
```json
{
  "success": false,
  "message": "Errores de validación",
  "errors": [
    {
      "field": "nombre",
      "message": "El campo nombre es obligatorio",
      "rejectedValue": ""
    }
  ]
}
```

#### **⚡ Performance Issues**
```bash
# Verificar estado del cache
curl http://localhost:8080/actuator/caches

# Verificar métricas
curl http://localhost:8080/actuator/metrics/cache.gets

# Limpiar cache si es necesario
curl -X DELETE http://localhost:8080/actuator/caches/categorias
```

#### **🗃️ Database Connection Issues**
```yaml
# Verificar configuración en application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/categoria_db
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### **📊 Logs y Debugging**

#### **📋 Habilitar Logs Debug**
```yaml
logging:
  level:
    com.nelson.project.msvc_categoria: DEBUG
    org.springframework.cache: DEBUG
    org.springframework.security: DEBUG
```

#### **🔍 Trace ID para Seguimiento**
```bash
# Los trace IDs aparecen en todas las respuestas
curl -X GET http://localhost:8080/api/v1/categorias/1

# Buscar en logs por trace ID
grep "550e8400-e29b-41d4-a716-446655440000" application.log
```

---

## 🚀 DEPLOYMENT

### **🐳 Docker**

```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY target/msvc-categoria-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=production
ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

```bash
# Build
docker build -t msvc-categoria:latest .

# Run
docker run -d \
  --name msvc-categoria \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/categoria_db \
  -e JWT_SECRET=mySecretKey \
  msvc-categoria:latest
```

### **☸️ Kubernetes**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msvc-categoria
spec:
  replicas: 3
  selector:
    matchLabels:
      app: msvc-categoria
  template:
    metadata:
      labels:
        app: msvc-categoria
    spec:
      containers:
      - name: msvc-categoria
        image: msvc-categoria:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        resources:
          requests:
            memory: "256Mi"
            cpu: "200m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

---

## 📞 SOPORTE Y CONTACTO

### **🆘 Obtener Ayuda**

1. **📖 Documentación**: Revisar este documento y la documentación OpenAPI
2. **🐛 Issues**: Crear issue en el repositorio con logs y trace ID
3. **💬 Slack**: Canal #msvc-categoria-support
4. **📧 Email**: nelson.laza@company.com

### **🔧 Información del Sistema**

```bash
# Información de la aplicación
curl http://localhost:8080/actuator/info

# Estado de salud
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics
```

---

**✨ ¡APIs listas para potenciar tu aplicación de ecommerce! ✨**