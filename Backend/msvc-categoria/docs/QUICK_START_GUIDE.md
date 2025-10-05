# 🏃‍♂️ GUÍA DE INICIO RÁPIDO - MSVC-CATEGORIA

**Microservicio de Categorías Empresarial**  
**Versión:** 1.0.0  
**Estado:** ✅ **PRODUCCIÓN READY**  
**Fecha:** 1 de octubre de 2025

---

## 🚀 INICIO RÁPIDO

### **📋 REQUISITOS PREVIOS**
```bash
✅ Java 21 LTS
✅ Maven 3.8+
✅ Base de datos (PostgreSQL/MySQL)
✅ IDE (IntelliJ IDEA/VS Code)
```

### **⚡ INSTALACIÓN EN 3 PASOS**

**1. 📥 Clonar el repositorio**
```bash
git clone https://github.com/negisaro/Project-Pymes-App-Produccion.git
cd Backend/msvc-categoria
```

**2. 🔧 Configurar base de datos**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/categoria_db
    username: tu_usuario
    password: tu_password
```

**3. 🚀 Ejecutar el microservicio**
```bash
mvn spring-boot:run
```

**🎯 ¡Listo! API disponible en:** `http://localhost:8080`

---

## 📊 DASHBOARD DE ESTADO

### **🟢 COMPONENTES OPERATIVOS**
| Componente | Estado | URL | Descripción |
|------------|--------|-----|-------------|
| **🎮 API REST** | ✅ ACTIVA | `/api/v1/categorias` | CRUD Administrativo |
| **🌐 API Pública** | ✅ ACTIVA | `/api/v1/public/categorias` | Acceso Público |
| **📚 Swagger UI** | ✅ ACTIVA | `/swagger-ui.html` | Documentación Interactiva |
| **🔍 Health Check** | ✅ ACTIVA | `/actuator/health` | Monitoreo de Salud |
| **📊 Métricas** | ✅ ACTIVA | `/actuator/metrics` | Métricas de Performance |

### **🎯 ENDPOINTS PRINCIPALES**

#### **🎮 API ADMINISTRATIVA** (`/api/v1/categorias`)
```http
GET    /                     # Listar categorías con filtros
POST   /                     # Crear nueva categoría
GET    /{id}                 # Obtener por ID
PUT    /{id}                 # Actualizar categoría
DELETE /{id}                 # Eliminar (soft delete)
GET    /populares           # Categorías más populares
GET    /jerarquia           # Árbol jerárquico
GET    /buscar              # Búsqueda avanzada
GET    /estadisticas        # Analytics empresariales
```

#### **🌐 API PÚBLICA** (`/api/v1/public/categorias`)
```http
GET    /                     # Listado público paginado
GET    /menu-navegacion     # Menú jerárquico optimizado
GET    /destacadas          # Categorías promocionales
GET    /buscar              # Búsqueda pública
GET    /{id}                # Vista pública individual
GET    /estadisticas        # Métricas públicas
```

---

## 🧪 TESTING RÁPIDO

### **🔧 CURL COMMANDS BÁSICOS**

**📋 Listar categorías:**
```bash
curl -X GET "http://localhost:8080/api/v1/categorias" \
  -H "Content-Type: application/json"
```

**➕ Crear categoría:**
```bash
curl -X POST "http://localhost:8080/api/v1/categorias" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Electrónicos",
    "descripcion": "Productos electrónicos y tecnología",
    "activo": true,
    "destacado": true
  }'
```

**🔍 Buscar categorías:**
```bash
curl -X GET "http://localhost:8080/api/v1/categorias/buscar?q=electro" \
  -H "Content-Type: application/json"
```

### **🌐 TESTING API PÚBLICA**

**📋 Menú de navegación:**
```bash
curl -X GET "http://localhost:8080/api/v1/public/categorias/menu-navegacion" \
  -H "Content-Type: application/json"
```

**⭐ Categorías destacadas:**
```bash
curl -X GET "http://localhost:8080/api/v1/public/categorias/destacadas" \
  -H "Content-Type: application/json"
```

---

## 📚 EJEMPLOS DE USO

### **🎯 CASO DE USO 1: E-COMMERCE**
```javascript
// Frontend: Obtener categorías para menú
fetch('/api/v1/public/categorias/menu-navegacion')
  .then(response => response.json())
  .then(menu => {
    // Renderizar menú jerárquico
    console.log('Menú de categorías:', menu);
  });

// Frontend: Buscar productos por categoría
fetch('/api/v1/public/categorias/buscar?q=ropa&limite=10')
  .then(response => response.json())
  .then(categorias => {
    // Mostrar resultados de búsqueda
    console.log('Categorías encontradas:', categorias);
  });
```

### **🎯 CASO DE USO 2: ADMINISTRACIÓN**
```javascript
// Admin: Crear nueva categoría
fetch('/api/v1/categorias', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer token_jwt'
  },
  body: JSON.stringify({
    nombre: 'Nueva Categoría',
    descripcion: 'Descripción detallada',
    activo: true,
    destacado: false,
    permiteProductos: true
  })
})
.then(response => response.json())
.then(categoria => {
  console.log('Categoría creada:', categoria);
});

// Admin: Obtener estadísticas
fetch('/api/v1/categorias/estadisticas')
  .then(response => response.json())
  .then(stats => {
    console.log('Estadísticas:', stats);
  });
```

---

## 🔧 CONFIGURACIÓN AVANZADA

### **⚙️ VARIABLES DE ENTORNO**
```bash
# Configuración de base de datos
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/categoria_db
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=password

# Configuración de cache
SPRING_CACHE_TYPE=redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Configuración de logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_NELSON=DEBUG
```

### **📊 PROFILES DISPONIBLES**
```yaml
# application-dev.yml (Desarrollo)
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
logging:
  level:
    com.nelson: DEBUG

# application-prod.yml (Producción)
spring:
  jpa:
    show-sql: false
logging:
  level:
    root: WARN
    com.nelson: INFO
```

---

## 🏗️ ARQUITECTURA RÁPIDA

### **📦 ESTRUCTURA DEL PROYECTO**
```
msvc-categoria/
├── 🎮 controller/          # APIs REST
├── ⚙️ service/             # Lógica de negocio
├── 🗄️ repository/          # Acceso a datos
├── 📊 model/               # Entidades y DTOs
├── 🗺️ mapper/              # Conversiones MapStruct
├── 🔧 config/              # Configuraciones
├── 🚨 exception/           # Manejo de errores
└── 📚 docs/                # Documentación
```

### **🔄 FLUJO DE DATOS**
```
🌐 Cliente → 🎮 Controller → ⚙️ Service → 🗄️ Repository → 💾 Database
                     ↓
              🗺️ Mapper ← 📊 Entity/DTO
```

---

## 📊 MONITOREO Y OBSERVABILIDAD

### **🔍 HEALTH CHECKS**
```bash
# Verificar estado general
curl http://localhost:8080/actuator/health

# Métricas de JVM
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Información de la aplicación
curl http://localhost:8080/actuator/info
```

### **📈 MÉTRICAS CLAVE**
- **🔢 Total de categorías**: `categoria_total`
- **⚡ Requests por segundo**: `http_requests_per_second`
- **⏱️ Tiempo de respuesta**: `response_time_avg`
- **💾 Uso de memoria**: `jvm_memory_usage`
- **🔄 Cache hit ratio**: `cache_hit_ratio`

---

## 🛠️ TROUBLESHOOTING

### **❌ PROBLEMAS COMUNES**

**🔴 Error de conexión a BD:**
```bash
# Verificar que la BD esté ejecutándose
docker ps | grep postgres

# Verificar configuración
curl http://localhost:8080/actuator/health/db
```

**🔴 Puerto ya en uso:**
```bash
# Cambiar puerto en application.yml
server:
  port: 8081
```

**🔴 Problemas de cache:**
```bash
# Limpiar cache
curl -X POST http://localhost:8080/actuator/caches
```

### **🔧 COMANDOS DE DIAGNÓSTICO**
```bash
# Ver logs en tiempo real
tail -f logs/application.log

# Verificar configuración activa
curl http://localhost:8080/actuator/configprops

# Ver beans registrados
curl http://localhost:8080/actuator/beans
```

---

## 🎓 RECURSOS ADICIONALES

### **📚 DOCUMENTACIÓN COMPLETA**
- 📋 [Documentación Técnica](./TECHNICAL_DOCUMENTATION.md)
- 🎯 [Guía de API](./API_USAGE_GUIDE.md)
- 🧠 [Lógica de Negocio](./BUSINESS_LOGIC_DOCUMENTATION.md)
- 🔧 [Arquitectura](./ARCHITECTURE.md)

### **🤝 SOPORTE**
- 📧 **Email**: desarrollo@empresa.com
- 💬 **Slack**: #microservicios-categoria
- 📱 **Teams**: Equipo Backend
- 🎫 **Issues**: GitHub Issues

---

## 🎉 ¡ÉXITO!

**🏆 ¡Tu microservicio msvc-categoria está funcionando correctamente!**

### **✅ VERIFICACIÓN FINAL**
1. ✅ API respondiendo en puerto 8080
2. ✅ Swagger UI accesible
3. ✅ Endpoints principales funcionando
4. ✅ Base de datos conectada
5. ✅ Logs estructurados generándose

### **🚀 PRÓXIMOS PASOS**
1. 🎨 Personalizar configuración para tu entorno
2. 🧪 Ejecutar tests de integración
3. 📊 Configurar monitoreo de producción
4. 🔒 Implementar seguridad adicional
5. 🚀 Desplegar en ambiente target

---

**✨ ¡Bienvenido al microservicio más robusto de categorías! ✨**

*Guía de inicio rápido - Actualizada el 1 de octubre de 2025*