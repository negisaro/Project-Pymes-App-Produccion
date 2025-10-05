# 🚀 Microservicio Categorías - msvc-categoria

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-yellow.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

## 📋 Descripción

Microservicio de gestión de categorías desarrollado con **Spring Boot 3.5.5** y **Java 21 LTS**. Forma parte de la arquitectura de microservicios para el sistema de gestión PYMES, proporcionando operaciones CRUD completas con auditoría, seguridad JWT y principios SOLID.

### ✨ Características Principales

- 🔐 **Seguridad JWT** completa con filtros de validación
- 📊 **Auditoría automática** de creación y modificación
- 🏗️ **Arquitectura SOLID** con separación clara de responsabilidades
- 🔄 **Mapeo automático** con MapStruct
- 📄 **Paginación y ordenamiento** integrados
- 🐋 **Containerización** con Docker
- 🔍 **Service Discovery** con Eureka
- 📡 **Comunicación inter-servicios** con OpenFeign
- ⚡ **Manejo de excepciones** centralizado y estructurado

## 🏗️ Arquitectura

```
msvc-categoria/
├── 🎮 controller/          # Capa de presentación
├── 🧠 service/            # Lógica de negocio
├── 📦 repository/         # Acceso a datos
├── 🔄 mapper/             # Transformación DTO-Entity
├── 🏷️ model/              # DTOs y Entidades
├── 🛡️ security/           # Configuración de seguridad
├── ⚠️ exception/          # Manejo de excepciones
└── ⚙️ config/             # Configuraciones
```

### 🎯 Principios SOLID Aplicados

- **[S]** Single Responsibility: Cada clase tiene una responsabilidad única
- **[O]** Open/Closed: Extensible mediante interfaces, cerrado para modificación
- **[L]** Liskov Substitution: Implementaciones intercambiables
- **[I]** Interface Segregation: Interfaces específicas y cohesivas
- **[D]** Dependency Inversion: Dependencias en abstracciones

## 🚀 Inicio Rápido

### 📋 Prerrequisitos

- ☕ **Java 21 LTS** o superior
- 📦 **Maven 3.9+**
- 🗄️ **MySQL 8.0+**
- 🐋 **Docker** (opcional)

### ⚙️ Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```env
# Base de datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=categoria_db
DB_USERNAME=categoria_user
DB_PASSWORD=categoria_pass

# JWT
JWT_SECRET=mi_clave_secreta_super_segura_de_256_bits_minimo
JWT_EXPIRATION=86400000

# Eureka
EUREKA_SERVER=http://localhost:8761/eureka

# Aplicación
APP_PORT=8086
PROFILE=dev
```

### 🏃‍♂️ Ejecución Local

#### Con Maven:
```bash
# 1. Clonar y navegar al directorio
cd msvc-categoria

# 2. Instalar dependencias
mvn clean install

# 3. Ejecutar la aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Con Docker:
```bash
# 1. Construir imagen
docker build -t msvc-categoria:latest .

# 2. Ejecutar contenedor
docker run -p 8086:8086 \
  -e PROFILE=dev \
  -e DB_HOST=host.docker.internal \
  msvc-categoria:latest
```

#### Con Docker Compose:
```yaml
version: '3.8'
services:
  categoria-service:
    build: .
    ports:
      - "8086:8086"
    environment:
      - PROFILE=dev
      - DB_HOST=categoria-db
    depends_on:
      - categoria-db
      
  categoria-db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: categoria_db
      MYSQL_USER: categoria_user
      MYSQL_PASSWORD: categoria_pass
      MYSQL_ROOT_PASSWORD: root_pass
    ports:
      - "3306:3306"
```

## 🔧 Configuración

### 📁 Perfiles de Configuración

#### Development (`application-dev.yml`)
```yaml
server:
  port: 8086

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:categoria_db}
    username: ${DB_USERNAME:categoria_user}
    password: ${DB_PASSWORD:categoria_pass}
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    
logging:
  level:
    com.nelson.project: DEBUG
```

#### Production (`application.yml`)
```yaml
server:
  port: ${APP_PORT:8086}

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT:3306}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    
logging:
  level:
    com.nelson.project: INFO
```

### 🗄️ Base de Datos

#### Crear esquema MySQL:
```sql
CREATE DATABASE categoria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'categoria_user'@'%' IDENTIFIED BY 'categoria_pass';
GRANT ALL PRIVILEGES ON categoria_db.* TO 'categoria_user'@'%';
FLUSH PRIVILEGES;
```

#### Esquema de tabla (auto-generado):
```sql
CREATE TABLE categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    creado_por VARCHAR(100),
    modificado_por VARCHAR(100),
    
    INDEX idx_nombre (nombre),
    INDEX idx_activo (activo),
    INDEX idx_fecha_creacion (fecha_creacion)
);
```

## 📚 API Endpoints

### 🔍 Información General
- **Base URL:** `http://localhost:8086/api/categorias`
- **Documentación:** `http://localhost:8086/swagger-ui.html`
- **Health Check:** `http://localhost:8086/actuator/health`

### 🎯 Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/categorias` | Listar categorías (paginado) |
| `GET` | `/api/categorias/{id}` | Obtener categoría por ID |
| `POST` | `/api/categorias` | Crear nueva categoría |
| `PUT` | `/api/categorias/{id}` | Actualizar categoría |
| `DELETE` | `/api/categorias/{id}` | Eliminar categoría |

### 📊 Ejemplo de Respuesta
```json
{
  "id": 1,
  "nombre": "Electrónicos",
  "descripcion": "Productos electrónicos y tecnológicos",
  "activo": true,
  "fechaCreacion": "2024-01-15T10:30:00Z",
  "fechaModificacion": "2024-01-15T10:30:00Z",
  "creadoPor": "admin",
  "modificadoPor": "admin"
}
```

> 📋 **Ver documentación completa:** [API_DOCUMENTATION.md](./docs/API_DOCUMENTATION.md)

## 🧪 Testing

### Ejecutar pruebas:
```bash
# Todas las pruebas
mvn test

# Pruebas específicas
mvn test -Dtest=CategoriaServiceImplTest

# Con coverage
mvn clean verify
```

### Pruebas de integración:
```bash
# Con perfil de test
mvn test -Dspring.profiles.active=test

# Con base de datos en memoria
mvn test -Dspring.datasource.url=jdbc:h2:mem:testdb
```

## 📊 Monitoreo y Observabilidad

### Spring Boot Actuator Endpoints:
- 🏥 **Health:** `/actuator/health`
- 📈 **Metrics:** `/actuator/metrics`
- ℹ️ **Info:** `/actuator/info`
- 🏷️ **Environment:** `/actuator/env`

### Logging:
```yaml
logging:
  level:
    com.nelson.project: DEBUG
  pattern:
    file: "%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/categoria-service.log
```

### Métricas personalizadas:
- Contadores de operaciones CRUD
- Tiempo de respuesta por endpoint
- Errores por tipo de excepción

## 🐋 Deployment

### Kubernetes:
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
        - containerPort: 8086
        env:
        - name: PROFILE
          value: "prod"
        - name: DB_HOST
          value: "mysql-service"
```

### Service:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: msvc-categoria-service
spec:
  selector:
    app: msvc-categoria
  ports:
  - port: 8086
    targetPort: 8086
  type: ClusterIP
```

## 🛠️ Desarrollo

### Estructura de branches:
- `main` - Código de producción
- `develop` - Desarrollo activo
- `feature/*` - Nuevas características
- `hotfix/*` - Correcciones urgentes

### Convención de commits:
```
feat: añadir validación de nombre único
fix: corregir problema de auditoría
docs: actualizar documentación API
test: añadir tests para CategoriaService
refactor: mejorar estructura de excepciones
```

### Pre-commit hooks:
```bash
# Instalar hooks
mvn git-code-format:install-hooks

# Verificar formato
mvn git-code-format:validate-code-format

# Aplicar formato
mvn git-code-format:format-code
```

## 🤝 Contribución

1. 🍴 Fork del repositorio
2. 🌿 Crear branch: `git checkout -b feature/nueva-funcionalidad`
3. 💻 Desarrollar y commitear cambios
4. 🧪 Ejecutar tests: `mvn test`
5. 📤 Push del branch: `git push origin feature/nueva-funcionalidad`
6. 🔄 Crear Pull Request

### Checklist para PR:
- [ ] Tests pasan completamente
- [ ] Documentación actualizada
- [ ] Código formateado correctamente
- [ ] Sin vulnerabilidades de seguridad
- [ ] Principios SOLID respetados

## 📈 Roadmap

### 🎯 Próximas funcionalidades:
- [ ] 🔍 Búsqueda avanzada por texto
- [ ] 📊 Filtros por estado y fecha
- [ ] 🔄 Soft delete con papelera
- [ ] 📸 Soporte para imágenes de categoría
- [ ] 🏷️ Sistema de etiquetas (tags)
- [ ] 📋 Importación/exportación CSV
- [ ] 🌐 Internacionalización (i18n)
- [ ] 📊 Dashboard de analytics

### 🔧 Mejoras técnicas:
- [ ] ⚡ Cache con Redis
- [ ] 📊 OpenTelemetry tracing
- [ ] 🔍 Elasticsearch integration
- [ ] 📋 GraphQL endpoint
- [ ] 🔄 Event Sourcing
- [ ] 🧪 Contract Testing

## 🐛 Resolución de Problemas

### Problemas comunes:

#### Error de conexión a BD:
```bash
# Verificar conectividad
telnet localhost 3306

# Verificar logs
docker logs msvc-categoria
```

#### Error JWT:
```bash
# Verificar variables de entorno
echo $JWT_SECRET

# Regenerar token
curl -X POST localhost:8080/auth/login
```

#### Error de Eureka:
```bash
# Verificar servidor Eureka
curl http://localhost:8761/eureka/apps

# Verificar configuración
grep eureka application.yml
```

## 📞 Soporte

- 📧 **Email:** soporte@pymes-app.com
- 📚 **Documentación:** [Wiki del proyecto](./docs)
- 🐛 **Issues:** [GitHub Issues](./issues)
- 💬 **Chat:** [Slack workspace](https://pymes-app.slack.com)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver [LICENSE.md](LICENSE.md) para detalles.

---

**Desarrollado con ❤️ para PYMES**

> 🚀 **¿Necesitas ayuda?** Consulta la [documentación completa](./docs) o abre un [issue](./issues)