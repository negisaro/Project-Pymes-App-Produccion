# 📊 Configuración y Deployment - Microservicio Categorías

## 🌍 Guía de Configuración por Ambiente

### 📁 Estructura de Configuración

```
src/main/resources/
├── application.yml                 # Configuración base
├── application-dev.yml            # Desarrollo local
├── application-test.yml           # Testing
├── application-staging.yml        # Pre-producción
└── application-prod.yml           # Producción
```

---

## 🔧 **CONFIGURACIÓN DESARROLLO**

### 📝 application-dev.yml
```yaml
server:
  port: 8086

spring:
  application:
    name: msvc-categoria
  
  profiles:
    active: dev
  
  # Base de Datos
  datasource:
    url: jdbc:mysql://localhost:3306/categoria_dev_db?useSSL=false&serverTimezone=UTC
    username: categoria_dev_user
    password: categoria_dev_pass
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  # JPA/Hibernate
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        
  # Eureka Client
  cloud:
    discovery:
      enabled: true
      
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${server.port}

# JWT Configuration
jwt:
  secret: dev_secret_key_256_bits_minimum_length_required_for_security
  expiration: 86400000  # 24 horas

# Logging
logging:
  level:
    com.nelson.project: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    
# Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env
  endpoint:
    health:
      show-details: always
```

### 🗄️ Setup Base de Datos Development
```bash
# Crear base de datos y usuario
mysql -u root -p

CREATE DATABASE categoria_dev_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'categoria_dev_user'@'localhost' IDENTIFIED BY 'categoria_dev_pass';
GRANT ALL PRIVILEGES ON categoria_dev_db.* TO 'categoria_dev_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 🚀 Ejecutar en Desarrollo
```bash
# Con Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Con JAR
java -jar -Dspring.profiles.active=dev target/msvc-categoria-0.0.1-SNAPSHOT.jar

# Con variables de entorno
export SPRING_PROFILES_ACTIVE=dev
export DB_PASSWORD=categoria_dev_pass
mvn spring-boot:run
```

---

## 🧪 **CONFIGURACIÓN TESTING**

### 📝 application-test.yml
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
    driver-class-name: org.h2.Driver
    
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    
  h2:
    console:
      enabled: true
      
eureka:
  client:
    enabled: false

jwt:
  secret: test_secret_key_256_bits_minimum_length_required_for_testing
  expiration: 3600000  # 1 hora

logging:
  level:
    com.nelson.project: INFO
    org.springframework.test: DEBUG
```

### 🧪 Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=CategoriaServiceImplTest

# Tests con perfil específico
mvn test -Dspring.profiles.active=test

# Tests con coverage
mvn clean verify jacoco:report
```

---

## 🚀 **CONFIGURACIÓN PRODUCCIÓN**

### 📝 application-prod.yml
```yaml
server:
  port: ${PORT:8086}
  shutdown: graceful
  
spring:
  application:
    name: msvc-categoria
    
  profiles:
    active: prod
    
  # Base de Datos Producción
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT:3306}/${DB_NAME}?useSSL=true&requireSSL=true&verifyServerCertificate=false
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    # Connection Pool
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 600000
      connection-timeout: 20000
      
  # JPA Optimizado para Producción
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
        
  # Eureka para Producción
  cloud:
    discovery:
      enabled: true

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER:http://eureka-server:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90

# JWT Seguro para Producción
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:3600000}

# Logging Producción
logging:
  level:
    com.nelson.project: INFO
    org.springframework: WARN
    org.hibernate: WARN
  pattern:
    file: "%d{ISO8601} [%thread] %-5level [%X{traceId},%X{spanId}] %logger{36} - %msg%n"
  file:
    name: /var/log/app/categoria-service.log
    max-size: 100MB
    max-history: 30

# Actuator para Producción
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
  metrics:
    export:
      prometheus:
        enabled: true
```

### 🔒 Variables de Entorno Producción
```bash
# Archivo .env para producción
DB_HOST=prod-mysql-server.com
DB_PORT=3306
DB_NAME=categoria_prod_db
DB_USERNAME=categoria_prod_user
DB_PASSWORD=super_secure_password_here

JWT_SECRET=ultra_secure_jwt_secret_key_minimum_256_bits_for_production_environment
JWT_EXPIRATION=3600000

EUREKA_SERVER=http://eureka-cluster:8761/eureka/

PORT=8086
SPRING_PROFILES_ACTIVE=prod
```

---

## 🐋 **DOCKER DEPLOYMENT**

### 📦 Dockerfile Optimizado
```dockerfile
# Multi-stage build para optimizar imagen
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Construir aplicación
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Imagen runtime optimizada
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copiar JAR desde stage builder
COPY --from=builder /app/target/msvc-categoria-*.jar app.jar

# Cambiar propietario
RUN chown -R appuser:appgroup /app

# Crear directorio de logs
RUN mkdir -p /var/log/app && \
    chown -R appuser:appgroup /var/log/app

USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8086/actuator/health || exit 1

EXPOSE 8086

ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=${PROFILE:prod}", \
    "-Xmx512m", \
    "-Xms256m", \
    "-jar", "app.jar"]
```

### 🚀 Docker Compose Completo
```yaml
version: '3.8'

services:
  # Servicio Categoría
  categoria-service:
    build: 
      context: .
      dockerfile: Dockerfile
    image: msvc-categoria:latest
    container_name: categoria-service
    ports:
      - "8086:8086"
    environment:
      - PROFILE=prod
      - DB_HOST=categoria-db
      - DB_NAME=categoria_db
      - DB_USERNAME=categoria_user
      - DB_PASSWORD=categoria_password
      - JWT_SECRET=my_super_secret_jwt_key_for_production_256_bits_minimum
      - EUREKA_SERVER=http://eureka-server:8761/eureka/
    depends_on:
      categoria-db:
        condition: service_healthy
      eureka-server:
        condition: service_healthy
    networks:
      - pymes-network
    volumes:
      - ./logs:/var/log/app
    restart: unless-stopped
    deploy:
      resources:
        limits:
          memory: 512M
          cpus: '0.5'
        reservations:
          memory: 256M
          cpus: '0.25'

  # Base de Datos MySQL
  categoria-db:
    image: mysql:8.0
    container_name: categoria-mysql
    environment:
      MYSQL_DATABASE: categoria_db
      MYSQL_USER: categoria_user
      MYSQL_PASSWORD: categoria_password
      MYSQL_ROOT_PASSWORD: root_password
    ports:
      - "3306:3306"
    volumes:
      - categoria_db_data:/var/lib/mysql
      - ./init-scripts:/docker-entrypoint-initdb.d
    networks:
      - pymes-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  # Eureka Server
  eureka-server:
    image: steeltoeoss/eureka-server:latest
    container_name: eureka-server
    ports:
      - "8761:8761"
    networks:
      - pymes-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Redis para Cache (opcional)
  redis:
    image: redis:7-alpine
    container_name: categoria-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - pymes-network
    restart: unless-stopped
    command: redis-server --appendonly yes

volumes:
  categoria_db_data:
  redis_data:

networks:
  pymes-network:
    driver: bridge
```

### 🚀 Comandos Docker
```bash
# Construir imagen
docker build -t msvc-categoria:latest .

# Ejecutar con Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f categoria-service

# Escalar servicios
docker-compose up -d --scale categoria-service=3

# Detener servicios
docker-compose down

# Limpiar volúmenes
docker-compose down -v
```

---

## ☸️ **KUBERNETES DEPLOYMENT**

### 📋 ConfigMap
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: categoria-config
  namespace: pymes
data:
  application-k8s.yml: |
    spring:
      datasource:
        url: jdbc:mysql://mysql-service:3306/categoria_db
        username: categoria_user
      jpa:
        hibernate:
          ddl-auto: validate
    eureka:
      client:
        service-url:
          defaultZone: http://eureka-service:8761/eureka/
    management:
      endpoints:
        web:
          exposure:
            include: health,info,metrics,prometheus
```

### 🔐 Secret
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: categoria-secrets
  namespace: pymes
type: Opaque
data:
  db-password: Y2F0ZWdvcmlhX3Bhc3N3b3Jk  # base64 encoded
  jwt-secret: bXlfc3VwZXJfc2VjcmV0X2p3dF9rZXk=  # base64 encoded
```

### 🚀 Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msvc-categoria
  namespace: pymes
  labels:
    app: msvc-categoria
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
      - name: categoria-service
        image: msvc-categoria:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8086
        env:
        - name: PROFILE
          value: "k8s"
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: categoria-secrets
              key: db-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: categoria-secrets
              key: jwt-secret
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8086
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8086
          initialDelaySeconds: 30
          periodSeconds: 10
        volumeMounts:
        - name: config
          mountPath: /app/config
      volumes:
      - name: config
        configMap:
          name: categoria-config
```

### 🌐 Service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: categoria-service
  namespace: pymes
  labels:
    app: msvc-categoria
spec:
  selector:
    app: msvc-categoria
  ports:
  - port: 8086
    targetPort: 8086
    name: http
  type: ClusterIP
```

### 🔄 HorizontalPodAutoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: categoria-hpa
  namespace: pymes
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: msvc-categoria
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

### 🚀 Comandos Kubernetes
```bash
# Crear namespace
kubectl create namespace pymes

# Aplicar configuraciones
kubectl apply -f k8s/

# Ver pods
kubectl get pods -n pymes

# Ver logs
kubectl logs -f deployment/msvc-categoria -n pymes

# Escalar manualmente
kubectl scale deployment msvc-categoria --replicas=5 -n pymes

# Port forward para testing
kubectl port-forward service/categoria-service 8086:8086 -n pymes
```

---

## 📊 **MONITOREO Y OBSERVABILIDAD**

### 📈 Prometheus Metrics
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'categoria-service'
    static_configs:
      - targets: ['categoria-service:8086']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 30s
```

### 📊 Grafana Dashboard
```json
{
  "dashboard": {
    "title": "Microservicio Categorías",
    "panels": [
      {
        "title": "Request Rate",
        "targets": [
          {
            "expr": "rate(http_requests_total{job=\"categoria-service\"}[5m])"
          }
        ]
      },
      {
        "title": "Response Time",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(http_request_duration_seconds_bucket{job=\"categoria-service\"}[5m]))"
          }
        ]
      }
    ]
  }
}
```

### 🔍 Logging con ELK Stack
```yaml
# filebeat.yml
filebeat.inputs:
- type: log
  enabled: true
  paths:
    - /var/log/app/categoria-service.log
  fields:
    service: categoria-service
    environment: production

output.elasticsearch:
  hosts: ["elasticsearch:9200"]
```

---

## 🛠️ **SCRIPTS DE DEPLOYMENT**

### 🚀 deploy.sh
```bash
#!/bin/bash

set -e

ENVIRONMENT=${1:-dev}
VERSION=${2:-latest}

echo "🚀 Deploying msvc-categoria to $ENVIRONMENT..."

case $ENVIRONMENT in
  dev)
    echo "📦 Building for development..."
    mvn clean package -DskipTests
    docker build -t msvc-categoria:$VERSION .
    docker-compose -f docker-compose.dev.yml up -d
    ;;
  staging)
    echo "📦 Building for staging..."
    mvn clean package
    docker build -t msvc-categoria:$VERSION .
    kubectl apply -f k8s/staging/
    ;;
  prod)
    echo "📦 Deploying to production..."
    kubectl set image deployment/msvc-categoria categoria-service=msvc-categoria:$VERSION -n pymes
    kubectl rollout status deployment/msvc-categoria -n pymes
    ;;
  *)
    echo "❌ Environment not supported: $ENVIRONMENT"
    exit 1
    ;;
esac

echo "✅ Deployment completed!"
```

### 🔄 rollback.sh
```bash
#!/bin/bash

ENVIRONMENT=${1:-prod}

echo "🔄 Rolling back msvc-categoria in $ENVIRONMENT..."

case $ENVIRONMENT in
  prod)
    kubectl rollout undo deployment/msvc-categoria -n pymes
    kubectl rollout status deployment/msvc-categoria -n pymes
    ;;
  staging)
    kubectl rollout undo deployment/msvc-categoria -n staging
    ;;
  *)
    echo "❌ Environment not supported: $ENVIRONMENT"
    exit 1
    ;;
esac

echo "✅ Rollback completed!"
```

---

**🎯 Con esta configuración tienes un deployment completo y profesional para todos los ambientes de tu microservicio de categorías.**