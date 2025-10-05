# 🧠 DOCUMENTACIÓN DE LÓGICA DE NEGOCIO - CATEGORÍAS

**Fecha:** 1 de Octubre 2025  
**Microservicio:** msvc-categoria  
**Estado:** ✅ IMPLEMENTADO  

---

## 🎯 RESUMEN EJECUTIVO

El microservicio de categorías **NO es un simple CRUD**. Implementa **15+ reglas de negocio empresariales** que garantizan integridad de datos, automatización de procesos y experiencia de usuario optimizada.

---

## 📋 CATÁLOGO DE REGLAS DE NEGOCIO IMPLEMENTADAS

### **1. 🔐 VALIDACIONES DE INTEGRIDAD DE DATOS**

#### **Métodos de Validación Básica:**
- `validateId(Long id)` - **IDs positivos y no nulos**
- `validateCreateDto(CategoriaCreateDto dto)` - **Datos obligatorios para creación**  
- `validateUpdateDto(CategoriaUpdateDto dto)` - **Datos válidos para actualización**
- `validateLimit(int limit)` - **Límites de consulta entre 1-1000**

#### **Reglas Aplicadas:**
- ✅ **ID válido**: Debe ser positivo y no nulo
- ✅ **Nombre obligatorio**: Entre 2-100 caracteres
- ✅ **Descripción opcional**: Máximo 2000 caracteres
- ✅ **Límites de consulta**: Entre 1 y 1000 resultados

---

### **2. 🏢 VALIDACIONES DE UNICIDAD EMPRESARIAL**

#### **Métodos de Unicidad:**
- `validateBusinessRulesForCreate(CategoriaCreateDto dto)` - **Unicidad en creación**
- `validateBusinessRulesForUpdate(CategoriaCreateDto dto, Long excludeId)` - **Unicidad en actualización**
- `validateBusinessRulesForSpecificUpdate(CategoriaUpdateDto dto, Long excludeId)` - **Unicidad en actualización específica**

#### **Reglas Aplicadas:**
- ✅ **Código único**: "Ya existe una categoría con el código: {código}"
- ✅ **Slug único**: "Ya existe una categoría con el slug: {slug}"
- ✅ **Nombre único**: "Ya existe una categoría con el nombre: {nombre}"

---

### **3. 🛡️ VALIDACIONES DE SEGURIDAD**

#### **Métodos de Seguridad:**
- `validateBasicBusinessRules(String nombre, String descripcion)` - **Prevención XSS y validaciones básicas**

#### **Reglas Aplicadas:**
- ✅ **Caracteres peligrosos**: Bloquea `<>\"'&` en nombres
- ✅ **Longitud segura**: Descripción máximo 2000 caracteres
- ✅ **Prevención XSS**: Sanitización de caracteres especiales

---

### **4. 🔄 REGLAS AUTOMÁTICAS DE NEGOCIO**

#### **Métodos de Automatización:**
- `applyBusinessRules(Categoria categoria)` - **Aplicación automática de reglas**

#### **Reglas Automáticas Implementadas:**
- ✅ **Generación de slug**: "Mi Categoría" → "mi-categoria"
- ✅ **Ruta jerárquica**: "Electrónicos > Smartphones > Android"
- ✅ **Cálculo de popularidad**: (productos×0.3) + (ventas×0.5) + (vistas×0.2)
- ✅ **Valores por defecto**: activo=true, eliminado=false

---

### **5. 🗑️ VALIDACIONES DE ELIMINACIÓN**

#### **Métodos de Eliminación:**
- `validateCanDelete(Categoria categoria)` - **Validaciones para soft delete**
- `validateCanHardDelete(Categoria categoria)` - **Validaciones para hard delete**

#### **Reglas de Eliminación:**
- ✅ **Protección jerarquía**: No eliminar si tiene subcategorías
- ✅ **Protección productos**: No eliminar si tiene productos asociados
- ✅ **Hard delete controlado**: Solo después de soft delete
- ✅ **Auditoría obligatoria**: Registrar usuario y motivo de eliminación

---

### **6. 🎯 LÓGICA EMPRESARIAL ESPECÍFICA**

#### **Métodos en la Entidad Categoria:**
- `generarSlug()` - **Genera slug SEO automáticamente**
- `generarRutaCompleta()` - **Calcula ruta jerárquica completa**
- `calcularPopularidad()` - **Calcula popularidad basada en métricas**
- `incrementarVistas()` - **Incrementa contador de vistas**
- `esRaiz()` - **Determina si es categoría raíz**
- `tieneSubcategorias()` - **Verifica si tiene hijos**
- `estaDisponible()` - **Verifica si está activa y no eliminada**

#### **Reglas Específicas:**
- ✅ **SEO automático**: Generación de slugs únicos para URLs amigables
- ✅ **Jerarquía inteligente**: Cálculo automático de rutas completas
- ✅ **Métricas empresariales**: Popularidad basada en múltiples factores
- ✅ **Estado consistente**: Validaciones de disponibilidad

---

## 🚀 CASOS DE USO EMPRESARIALES

### **Caso 1: Creación de Categoría**
**Flujo:** `save(CategoriaCreateDto dto)`
1. **Validar** datos básicos obligatorios
2. **Verificar** unicidad de código/slug/nombre  
3. **Aplicar** reglas automáticas (slug, popularidad, defaults)
4. **Persistir** con auditoría completa

### **Caso 2: Actualización de Categoría**
**Flujo:** `update(Long id, CategoriaUpdateDto dto)`
1. **Validar** ID y existencia
2. **Verificar** unicidad excluyendo categoría actual
3. **Aplicar** reglas automáticas actualizadas
4. **Persistir** cambios con auditoría

### **Caso 3: Eliminación Segura**
**Flujo:** `deleteById(Long id)`
1. **Verificar** inexistencia de subcategorías
2. **Verificar** inexistencia de productos asociados
3. **Aplicar** soft delete con auditoría
4. **Registrar** usuario y motivo

### **Caso 4: Consultas Inteligentes**
**Flujo:** Múltiples métodos especializados
1. **Filtrar** automáticamente por activo=true y eliminado=false
2. **Ordenar** por criterios empresariales (popularidad, orden)
3. **Paginar** con límites seguros
4. **Optimizar** para casos de uso específicos

---

## 📊 MÉTRICAS DE NEGOCIO AUTOMATIZADAS

### **Cálculos Automáticos:**
- **Popularidad**: Combinación ponderada de productos, ventas y vistas
- **Nivel jerárquico**: Cálculo automático basado en categoría padre
- **Ruta completa**: Construcción de breadcrumb para navegación
- **Estado disponible**: Evaluación activo AND NOT eliminado

### **Contadores Empresariales:**
- **Total productos**: Cantidad de productos en la categoría
- **Total ventas**: Número de transacciones realizadas
- **Vistas totales**: Contador de visualizaciones
- **Ingresos totales**: Suma de ingresos generados

---

## 🛡️ PROTECCIONES IMPLEMENTADAS

### **Integridad Referencial:**
- ✅ **Jerarquías consistentes**: No eliminar padres con hijos
- ✅ **Productos protegidos**: No eliminar categorías con productos
- ✅ **Unicidad garantizada**: Códigos y slugs únicos en el sistema

### **Seguridad de Datos:**
- ✅ **Prevención XSS**: Bloqueo de caracteres peligrosos
- ✅ **Validación de entrada**: Sanitización de todos los inputs
- ✅ **Límites seguros**: Protección contra consultas masivas

### **Auditoría Completa:**
- ✅ **Trazabilidad**: Registro de creador y modificador
- ✅ **Soft delete**: Preservación de datos con motivos
- ✅ **Versionado**: Control de cambios concurrentes

---

## 🎯 BENEFICIOS EMPRESARIALES

### **1. 🔒 Integridad de Datos**
- **Previene** inconsistencias en el catálogo
- **Garantiza** unicidad de identificadores
- **Protege** relaciones críticas

### **2. 🚀 Automatización**
- **Genera** URLs SEO automáticamente
- **Calcula** métricas de popularidad
- **Mantiene** jerarquías actualizadas

### **3. 📊 Inteligencia de Negocio**
- **Proporciona** métricas calculadas en tiempo real
- **Facilita** análisis de rendimiento de categorías
- **Optimiza** navegación del catálogo

### **4. 🛡️ Experiencia de Usuario**
- **Previene** errores con validaciones claras
- **Genera** mensajes de error descriptivos
- **Mantiene** consistencia en la interfaz

---

## 📝 NOTAS TÉCNICAS

### **Patrones Aplicados:**
- ✅ **Template Method**: Validaciones consistentes
- ✅ **Strategy Pattern**: Diferentes validaciones por contexto
- ✅ **Business Rules Pattern**: Lógica centralizada en service
- ✅ **Domain Logic Pattern**: Reglas en la entidad

### **Principios Seguidos:**
- ✅ **Single Responsibility**: Cada método una validación específica
- ✅ **Fail Fast**: Validaciones tempranas con excepciones claras
- ✅ **DRY**: Reutilización de validaciones básicas
- ✅ **Defensive Programming**: Validación exhaustiva de entradas

---

**🎯 CONCLUSIÓN: Categorías implementa un sistema robusto de lógica de negocio que va más allá del CRUD básico, proporcionando automatización, validaciones empresariales y protección de integridad de datos.**