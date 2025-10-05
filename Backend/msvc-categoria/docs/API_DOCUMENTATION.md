# 📋 API Documentation - Microservicio Categorías Empresarial

## 🌟 Información General

**Versión:** 2.0.0 (Entidad Robusta)  
**Puerto por defecto:** 8086  
**Base URL:** `http://localhost:8086/categorias`  
**OpenAPI:** `http://localhost:8086/swagger-ui.html`

### 🎯 Características Empresariales
- **Entidad Robusta**: 65+ campos especializados en 10 grupos funcionales
- **DTOs Especializados**: 5 DTOs optimizados para diferentes casos de uso
- **Jerarquías Multinivel**: Soporte completo para categorías padre-hijo (hasta 10 niveles)
- **SEO Integrado**: Campos especializados para optimización web
- **Métricas Automáticas**: Sistema de analíticas y popularidad en tiempo real
- **Auditoría Completa**: Seguimiento detallado de cambios con contexto de usuario
- **Soft Delete**: Eliminación lógica con auditoría completa

## 🔐 Autenticación

Todas las operaciones protegidas requieren autenticación JWT mediante el header:
```
Authorization: Bearer <jwt-token>
```

### Endpoints Públicos (sin autenticación)
- `GET /api/categorias/public/activas` - Consulta categorías activas
- `GET /api/categorias/public/jerarquia` - Estructura jerárquica pública
- `GET /api/categorias/public/populares` - Categorías más populares

## 📝 Recursos API

### 🏷️ Categoria Resource Completa

#### **GET** `/api/categorias`
Obtiene todas las categorías con paginación y filtros avanzados.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento (ej: "popularidad,desc", "nombre,asc")
- `filtro` (opcional): Filtros complejos usando CategoriaFilterDto
- `nivel` (opcional): Filtrar por nivel jerárquico (0-10)
- `tipo` (opcional): Filtrar por tipo (`PRODUCTO`, `SERVICIO`, `DIGITAL`, etc.)
- `estado` (opcional): Filtrar por estado (`APROBADA`, `PENDIENTE_APROBACION`, etc.)
- `activo` (opcional): Filtrar por estado activo (true/false)
- `destacada` (opcional): Solo categorías destacadas (true/false)
- `parentId` (opcional): Filtrar por categoría padre

**Respuesta exitosa (200) - CategoriaDTO completo:**
```json
{
  "content": [
    {
      "id": 1,
      "nombre": "Electrónicos y Tecnología",
      "codigo": "ELECTRO_TECH",
      "descripcion": "Categoría principal para productos electrónicos, dispositivos móviles, computadoras y accesorios tecnológicos de última generación",
      "activo": true,
      
      // JERARQUÍA
      "categoriaPadreId": null,
      "categoriaPadreNombre": null,
      "subcategorias": [
        {
          "id": 2,
          "nombre": "Smartphones",
          "nivel": 1
        }
      ],
      "nivel": 0,
      "rutaCompleta": "Electrónicos y Tecnología",
      
      // VISUALIZACIÓN
      "ordenVisualizacion": 1,
      "colorHex": "#2196F3",
      "icono": "fa-mobile-alt",
      "imagenUrl": "https://cdn.example.com/categorias/electronicos.jpg",
      "imagenThumbnailUrl": "https://cdn.example.com/categorias/electronicos-thumb.jpg",
      
      // SEO Y MARKETING
      "slug": "electronicos-tecnologia",
      "metaTitulo": "Electrónicos y Tecnología - Mejores Precios",
      "metaDescripcion": "Encuentra los mejores productos electrónicos y tecnológicos con garantía y envío gratis",
      "palabrasClave": "electrónicos,tecnología,móviles,computadoras",
      
      // CONFIGURACIÓN DE NEGOCIO
      "permiteProductos": true,
      "requiereAprobacion": false,
      "visibleEnMenu": true,
      "destacada": true,
      "comisionPorcentaje": 5.50,
      
      // CONFIGURACIÓN DE PRODUCTOS
      "precioMinimo": 10.00,
      "precioMaximo": 50000.00,
      "requiereInventario": true,
      "permiteVariantes": true,
      
      // MÉTRICAS Y ANALÍTICAS
      "totalProductos": 1250,
      "totalVentas": 8945,
      "ingresosTotales": 2850000.75,
      "vistasTotal": 156789,
      "popularidad": 92.5,
      
      // CONFIGURACIÓN AVANZADA
      "configuracionJson": "{\"autoTags\": true, \"requiereEspecificaciones\": true}",
      "plantillaProducto": "template_electronicos",
      "departamento": "Tecnología",
      "tipo": "PRODUCTO",
      "estadoAprobacion": "APROBADA",
      
      // AUDITORÍA
      "fechaCreacion": "2024-01-15T10:30:00Z",
      "fechaModificacion": "2024-10-01T15:45:00Z",
      "creadoPor": "admin@empresa.com",
      "modificadoPor": "manager@empresa.com",
      "versionRegistro": 5,
      "motivoUltimoCambio": "Actualización de métricas y configuración SEO",
      
      // SOFT DELETE
      "eliminado": false,
      "eliminadoPor": null,
      "motivoEliminacion": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 125,
  "totalPages": 7,
  "numberOfElements": 20,
  "first": true,
  "last": false
}
```

#### **GET** `/api/categorias/{id}`
Obtiene una categoría específica por ID.

**Parámetros de ruta:**
- `id` (Long): ID de la categoría

#### **GET** `/api/categorias/{id}`
Obtiene una categoría específica por su ID.

**Parámetros de ruta:**
- `id`: ID único de la categoría (Long)

**Respuesta exitosa (200) - CategoriaDTO completo:**
```json
{
  "id": 1,
  "nombre": "Smartphones Premium",
  "codigo": "SMART_PREMIUM",
  "descripcion": "Smartphones de gama alta con tecnología avanzada y características premium",
  "activo": true,
  
  // JERARQUÍA COMPLETA
  "categoriaPadreId": 1,
  "categoriaPadreNombre": "Electrónicos y Tecnología",
  "subcategorias": [
    {
      "id": 15,
      "nombre": "iPhone",
      "nivel": 2
    },
    {
      "id": 16,
      "nombre": "Samsung Galaxy",
      "nivel": 2
    }
  ],
  "nivel": 1,
  "rutaCompleta": "Electrónicos y Tecnología > Smartphones Premium",
  
  // CONFIGURACIÓN VISUAL
  "ordenVisualizacion": 5,
  "colorHex": "#FF5722",
  "icono": "fa-mobile-screen",
  "imagenUrl": "https://cdn.example.com/categorias/smartphones-premium.jpg",
  "imagenThumbnailUrl": "https://cdn.example.com/categorias/smartphones-premium-thumb.jpg",
  
  // SEO OPTIMIZADO
  "slug": "smartphones-premium",
  "metaTitulo": "Smartphones Premium - Última Tecnología",
  "metaDescripcion": "Descubre los smartphones más avanzados con tecnología de punta, cámaras profesionales y rendimiento excepcional",
  "palabrasClave": "smartphones,premium,tecnología,móviles,gama alta",
  
  // TODAS LAS MÉTRICAS
  "totalProductos": 89,
  "totalVentas": 2156,
  "ingresosTotales": 485750.25,
  "vistasTotal": 45623,
  "popularidad": 87.3,
  
  // CONFIGURACIÓN EMPRESARIAL
  "permiteProductos": true,
  "requiereAprobacion": true,
  "visibleEnMenu": true,
  "destacada": true,
  "comisionPorcentaje": 8.50,
  "precioMinimo": 200.00,
  "precioMaximo": 2500.00,
  "requiereInventario": true,
  "permiteVariantes": true,
  
  // CONFIGURACIÓN AVANZADA
  "configuracionJson": "{\"requiereImei\": true, \"garantiaExtendida\": true, \"certificaciones\": [\"CE\", \"FCC\"]}",
  "plantillaProducto": "template_smartphones",
  "departamento": "Tecnología Móvil",
  "tipo": "PRODUCTO",
  "estadoAprobacion": "APROBADA",
  
  // AUDITORÍA COMPLETA
  "fechaCreacion": "2024-01-15T10:30:00Z",
  "fechaModificacion": "2024-10-01T15:45:00Z",
  "creadoPor": "categoria.manager@empresa.com",
  "modificadoPor": "seo.specialist@empresa.com",
  "versionRegistro": 12,
  "motivoUltimoCambio": "Optimización SEO y actualización de precios por campaña Black Friday",
  
  // ESTADO ACTUAL
  "eliminado": false,
  "eliminadoPor": null,
  "motivoEliminacion": null
}
```

**Respuesta de error (404):**
```json
{
  "timestamp": "2024-10-01T15:45:00Z",
  "status": 404,
  "error": "Categoria Not Found",
  "message": "Categoría no encontrada con ID: 999",
  "path": "/api/categorias/999",
  "code": "CATEGORIA_NOT_FOUND"
}
```

#### **POST** `/api/categorias`
Crea una nueva categoría usando CategoriaCreateDto con validaciones empresariales.

**Cuerpo de la solicitud (CategoriaCreateDto):**
```json
{
  // CAMPOS BÁSICOS OBLIGATORIOS
  "nombre": "Laptops Gaming",
  "codigo": "LAPTOP_GAMING",
  "descripcion": "Laptops especializadas para gaming con alto rendimiento gráfico y procesadores de última generación",
  "activo": true,
  
  // JERARQUÍA (OPCIONAL)
  "categoriaPadreId": 1,
  "nivel": 1,
  "ordenVisualizacion": 10,
  
  // CONFIGURACIÓN VISUAL (OPCIONAL)
  "colorHex": "#9C27B0",
  "icono": "fa-laptop-code",
  "imagenUrl": "https://cdn.example.com/categorias/laptops-gaming.jpg",
  
  // SEO BÁSICO (OPCIONAL)
  "slug": "laptops-gaming",
  "metaTitulo": "Laptops Gaming - Alto Rendimiento",
  "metaDescripcion": "Las mejores laptops gaming con tarjetas gráficas dedicadas y procesadores de alto rendimiento",
  "palabrasClave": "laptops,gaming,alto rendimiento,gráficos",
  
  // CONFIGURACIÓN DE NEGOCIO (OPCIONAL)
  "permiteProductos": true,
  "requiereAprobacion": false,
  "visibleEnMenu": true,
  "destacada": false,
  "comisionPorcentaje": 6.00,
  
  // CONFIGURACIÓN DE PRODUCTOS (OPCIONAL)
  "precioMinimo": 800.00,
  "precioMaximo": 5000.00,
  "requiereInventario": true,
  "permiteVariantes": true,
  
  // CONFIGURACIÓN AVANZADA (OPCIONAL)
  "configuracionJson": "{\"requiereEspecificacionesGpu\": true, \"categoriaEspecializada\": true}",
  "plantillaProducto": "template_laptops_gaming",
  "departamento": "Gaming y Entretenimiento",
  "tipo": "PRODUCTO",
  "estadoAprobacion": "PENDIENTE_APROBACION"
}
```

**Respuesta exitosa (201) - CategoriaDTO completo:**
```json
{
  "id": 25,
  "nombre": "Laptops Gaming",
  "codigo": "LAPTOP_GAMING",
  "descripcion": "Laptops especializadas para gaming con alto rendimiento gráfico y procesadores de última generación",
  "activo": true,
  
  // JERARQUÍA AUTOMÁTICA
  "categoriaPadreId": 1,
  "categoriaPadreNombre": "Electrónicos y Tecnología",
  "subcategorias": [],
  "nivel": 1,
  "rutaCompleta": "Electrónicos y Tecnología > Laptops Gaming",
  
  // VALORES GENERADOS AUTOMÁTICAMENTE
  "slug": "laptops-gaming",
  "totalProductos": 0,
  "totalVentas": 0,
  "ingresosTotales": 0.00,
  "vistasTotal": 0,
  "popularidad": 0.0,
  
  // AUDITORÍA DE CREACIÓN
  "fechaCreacion": "2024-10-01T16:20:00Z",
  "fechaModificacion": "2024-10-01T16:20:00Z",
  "creadoPor": "categoria.admin@empresa.com",
  "modificadoPor": "categoria.admin@empresa.com",
  "versionRegistro": 1,
  "motivoUltimoCambio": "Creación inicial de categoría",
  
  "eliminado": false,
  "eliminadoPor": null,
  "motivoEliminacion": null,
  
  // ... (resto de campos según configuración enviada)
}
```

**Respuesta de error de validación (400):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe una categoría con el nombre: Nueva Categoría",
  "path": "/api/categorias"
}
```

#### **PUT** `/api/categorias/{id}`
Actualiza una categoría existente.

**Parámetros de ruta:**
- `id` (Long): ID de la categoría a actualizar

**Cuerpo de la solicitud:**
```json
{
  "nombre": "Categoría Actualizada",
  "descripcion": "Descripción actualizada",
  "activo": false
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "nombre": "Categoría Actualizada",
  "descripcion": "Descripción actualizada",
  "activo": false,
  "fechaCreacion": "2024-01-15T10:30:00Z",
  "fechaModificacion": "2024-01-15T11:30:00Z",
  "creadoPor": "admin",
  "modificadoPor": "admin"
}
```

#### **DELETE** `/api/categorias/{id}`
Elimina una categoría.

**Parámetros de ruta:**
- `id` (Long): ID de la categoría a eliminar

**Respuesta exitosa (204):** No Content

**Respuesta de error (404):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Categoría no encontrada con ID: 1",
  "path": "/api/categorias/1"
}
```

## 📊 Modelos de Datos

### CategoriaDTO
```json
{
  "id": "Long - ID único de la categoría",
  "nombre": "String - Nombre de la categoría (requerido, max 255 caracteres)",
  "descripcion": "String - Descripción de la categoría (opcional, max 1000 caracteres)",
  "activo": "Boolean - Estado activo/inactivo (default: true)",
  "fechaCreacion": "LocalDateTime - Fecha de creación (automático)",
  "fechaModificacion": "LocalDateTime - Fecha de última modificación (automático)",
  "creadoPor": "String - Usuario que creó el registro",
  "modificadoPor": "String - Usuario que modificó por última vez"
}
```

### CategoriaCreateDto
```json
{
  "nombre": "String - Nombre de la categoría (requerido, max 255 caracteres)",
  "descripcion": "String - Descripción de la categoría (opcional, max 1000 caracteres)",
  "activo": "Boolean - Estado activo/inactivo (default: true)"
}
```

## ⚠️ Códigos de Estado HTTP

| Código | Significado | Descripción |
|--------|-------------|-------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado exitosamente |
| 204 | No Content | Eliminación exitosa |
| 400 | Bad Request | Error de validación o reglas de negocio |
| 401 | Unauthorized | Token JWT inválido o ausente |
| 403 | Forbidden | Sin permisos para realizar la operación |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Conflicto con el estado actual del recurso |
| 500 | Internal Server Error | Error interno del servidor |

## 🔧 Reglas de Negocio

### Validaciones para Categorías:
1. **Nombre único**: No pueden existir dos categorías con el mismo nombre
2. **Caracteres especiales**: El nombre no puede contener `<`, `>`, `"`, `'`, `&`
3. **Longitud**: 
   - Nombre: máximo 255 caracteres
   - Descripción: máximo 1000 caracteres
4. **Campos requeridos**: Solo el nombre es obligatorio
5. **Estado por defecto**: Las categorías se crean activas por defecto

### Auditoría:
- **fechaCreacion**: Se establece automáticamente al crear
- **fechaModificacion**: Se actualiza automáticamente en cada modificación
- **creadoPor/modificadoPor**: Se obtienen del contexto de seguridad JWT

## 🧪 Ejemplos de Uso

### Crear categoría con curl:
```bash
curl -X POST http://localhost:8086/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "nombre": "Ropa",
    "descripcion": "Artículos de vestir",
    "activo": true
  }'
```

### Obtener categorías paginadas:
```bash
curl -X GET "http://localhost:8086/api/categorias?page=0&size=10&sort=nombre,asc" \
  -H "Authorization: Bearer <jwt-token>"
```

### Actualizar categoría:
```bash
curl -X PUT http://localhost:8086/api/categorias/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "nombre": "Ropa Actualizada",
    "descripcion": "Artículos de vestir para todas las edades",
    "activo": true
  }'
```

## 🔍 Filtros y Búsquedas

### Paginación:
- **page**: Número de página (comenzando desde 0)
- **size**: Elementos por página (máximo recomendado: 100)
- **sort**: Campo de ordenamiento seguido de dirección (asc/desc)

### Ejemplos de ordenamiento:
- `sort=nombre,asc` - Ordenar por nombre ascendente
- `sort=fechaCreacion,desc` - Ordenar por fecha de creación descendente
- `sort=id,asc&sort=nombre,desc` - Ordenamiento múltiple

## 🚨 Manejo de Errores

### Estructura de respuesta de error:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción detallada del error",
  "path": "/api/categorias",
  "details": {
    "campo": "error específico del campo"
  }
}
```

### Errores comunes:
- **Categoría duplicada**: Al intentar crear una categoría con nombre existente
- **Categoría no encontrada**: Al buscar/actualizar/eliminar con ID inexistente
- **Validación de campos**: Cuando los datos no cumplen las reglas de negocio
- **Token JWT inválido**: Cuando la autenticación falla

## 📈 Monitoreo y Observabilidad

### Health Check:
```
GET /actuator/health
```

### Métricas:
```
GET /actuator/metrics
```

### Información de la aplicación:
```
GET /actuator/info
```

---

**Notas importantes:**
- Todos los endpoints requieren autenticación JWT válida
- Las fechas se manejan en formato ISO-8601 (UTC)
- Los campos de auditoría se gestionan automáticamente
- Se recomienda validar los datos en el frontend antes de enviar solicitudes