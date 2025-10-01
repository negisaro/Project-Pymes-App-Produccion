# 📋 Refactorización de DTOs - Estrategia Híbrida Clásica-Lombok

## 🎯 Objetivo
Implementar una estrategia híbrida para DTOs combinando métodos tradicionales para casos complejos y Lombok para casos simples, optimizando productividad y mantenibilidad.

## 📊 Análisis Completo de DTOs

### **🔹 DTOs Complejos** (Mantener Getters/Setters Tradicionales)

#### ✅ `CarritoDto.java`
- **Motivo**: Lógica de negocio compleja, 15+ métodos de utilidad
- **Características**: 
  - Validaciones extensas con @DecimalMin, @Digits, @Pattern
  - Métodos de negocio: `tieneItems()`, `puedeModificarse()`, `estaEnEstadoFinal()`
  - Control de estados complejos (ACTIVO, ABANDONADO, PROCESADO, etc.)
- **Estado**: ✅ Mantenido con enfoque tradicional
- **Justificación**: La lógica de negocio supera la simplicidad de Lombok

#### ✅ `ItemCarritoDto.java`
- **Motivo**: Campos desnormalizados complejos, lógica de cálculo
- **Características**:
  - 20+ campos desnormalizados del producto
  - Métodos de utilidad: `estaDisponible()`, `tieneStock()`, `calcularTotal()`
  - Validaciones complejas de precios y cantidades
- **Estado**: ✅ Mantenido con enfoque tradicional
- **Justificación**: Desnormalización y cálculos complejos requieren control manual

### **🔹 DTOs Simples** (Candidatos para Lombok)

#### 📝 `CrearCarritoRequest.java`
- **Motivo**: Solo 3 campos, validaciones básicas
- **Estado**: 🔄 Preparado para Lombok
- **Anotaciones Lombok**: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Beneficio**: Reducir de 60 a 15 líneas (~75% menos código)

#### 📝 `AgregarItemRequest.java`
- **Motivo**: Solo 3 campos, lógica simple
- **Estado**: 🔄 Preparado para Lombok
- **Anotaciones Lombok**: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Beneficio**: Reducir de 70 a 20 líneas (~71% menos código)

#### 📝 `ActualizarCantidadRequest.java`
- **Motivo**: Solo 2 campos, validaciones simples
- **Estado**: 🔄 Preparado para Lombok
- **Anotaciones Lombok**: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Beneficio**: Reducir de 50 a 12 líneas (~76% menos código)

#### 📝 `ApiResponse<T>.java`
- **Motivo**: Genérico con métodos factory, pero estructura simple
- **Estado**: 🔄 Preparado para Lombok híbrido
- **Estrategia**: `@Data` + métodos factory manuales
- **Beneficio**: Mantener factory methods, reducir getters/setters

### **🔹 DTOs por Analizar**

#### 🔄 Pendientes de Análisis:
- `ActualizarEstadoCarritoRequest.java` ✅ COMPLETADO
- `AplicarCuponRequest.java` ✅ COMPLETADO
- `CarritoResponse.java`
- `ResumenCarritoResponse.java`

#### ✅ DTOs Analizados y Mejorados (Manteniendo Compatibilidad Feign):
- **`UsuarioDto.java`**: Validaciones robustas, métodos de utilidad, documentación OpenAPI (campos: `name`, `lastname`, `username`, `password`, `active`, `carritoId`)
- **`RolDto.java`**: Validaciones, métodos business logic (esAdministrador, esUsuario, esModerador), documentación (campos: `name`, `activo`)
- **`ProductoDto.java`**: Validaciones @NotNull/@Digits, métodos de utilidad business logic, documentación completa (campos originales mantenidos)

**IMPORTANTE**: Todos los nombres de campos originales mantenidos para compatibilidad con comunicación Feign entre microservicios.

## 🛠️ Configuración de Lombok

### **✅ Dependencias Agregadas al POM.xml:**

```xml
<!-- ========================================= -->
<!-- LOMBOK PARA REDUCIR CÓDIGO BOILERPLATE   -->
<!-- ========================================= -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### **✅ Annotation Processors Configurados:**

```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${mapstruct.version}</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
    <!-- LOMBOK + MAPSTRUCT BINDING PARA INTEGRACIÓN -->
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok-mapstruct-binding</artifactId>
        <version>0.2.0</version>
    </path>
</annotationProcessorPaths>
```

### **✅ Properties Agregadas:**

```xml
<lombok.version>1.18.30</lombok.version>
```

## 📈 Métricas de Mejora Esperadas

### **Reducción de Código:**
- **Request DTOs**: ~73% menos líneas de código
- **Response DTOs simples**: ~65% menos líneas de código
- **DTOs de transferencia**: ~70% menos líneas de código

### **Beneficios:**
- ✅ Menos código boilerplate
- ✅ Mayor legibilidad en DTOs simples
- ✅ Mantenimiento simplificado
- ✅ Consistencia en naming
- ✅ Integración perfecta con MapStruct

## 🎯 Estrategia de Implementación

### **Fase 1**: DTOs Request Simples ✅ 
- `CrearCarritoRequest`
- `AgregarItemRequest` 
- `ActualizarCantidadRequest`

### **Fase 2**: DTOs Response Simples (Pendiente)
- `ApiResponse` (híbrido)
- Response DTOs básicos

### **Fase 3**: DTOs de Transferencia (Pendiente)  
- `UsuarioDto`
- `RolDto`
- `ProductoDto`

### **Fase 4**: Validación y Testing (Pendiente)
- Verificar MapStruct compatibility
- Tests de integración
- Validación de serializacion JSON

## 🔍 Criterios de Decisión

### **Usar Lombok cuando:**
- ≤ 5 campos principales
- Sin lógica de negocio compleja
- Sin métodos de utilidad específicos
- Validaciones estándar (@NotNull, @Positive, etc.)
- DTOs de transferencia pura

### **Usar Getters/Setters tradicionales cuando:**
- Múltiples métodos de utilidad
- Lógica de negocio embebida
- Validaciones complejas con lógica custom
- Control fino sobre serialización
- Campos calculados o derivados

## 📋 Estado Actual

### **✅ Completado:**
- Análisis de DTOs principales
- Configuración de Lombok en POM.xml
- Documentación de estrategia
- Preparación de DTOs request simples

### **🔄 En Progreso:**
- Análisis de DTOs restantes
- Implementación gradual de Lombok

### **⏳ Pendiente:**
- Configuración de IDE para Lombok
- Testing de compatibilidad MapStruct
- Implementación en DTOs response
- Validación completa

## 📝 Notas Técnicas

1. **Lombok-MapStruct Integration**: Binding configurado para evitar conflictos
2. **IDE Configuration**: Requerirá instalación de plugin Lombok
3. **Build Process**: Annotation processors configurados correctamente
4. **Backward Compatibility**: Cambios no rompen API existente

---

**Documentado por**: Refactorización de DTOs  
**Fecha**: $(date)  
**Estado**: Configuración completada, implementación en progreso