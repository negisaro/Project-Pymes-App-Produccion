# Mejoras Pendientes - Validaciones Adicionales

**Fecha:** 1 de octubre de 2025  
**Estado:** 📋 PENDIENTE  
**Prioridad:** MEDIA  
**Estimación:** 3-5 horas  

## 🎯 Objetivo

Implementar validaciones adicionales en las entidades para mejorar la integridad de datos, seguridad y robustez del sistema de carrito.

## 🛡️ Validaciones de Seguridad

### 1. **Validaciones en Carrito.java**

#### **Email y Datos de Usuario**
```java
// PENDIENTE: Agregar validación de email
@Email(message = "Email debe tener formato válido")
@Size(max = 255, message = "Email no puede exceder 255 caracteres")
@Column(name = "email_usuario", length = 255)
private String emailUsuario;

// PENDIENTE: Validar IP del cliente
@Pattern(
    regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$",
    message = "IP debe tener formato válido IPv4 o IPv6"
)
@Column(name = "ip_cliente", length = 45)
private String ipCliente;
```

#### **Validaciones de Negocio Adicionales**
```java
// PENDIENTE: Validar límites de carrito
@Min(value = 1, message = "Carrito debe tener al menos 1 item para procesar")
@Max(value = 100, message = "Carrito no puede tener más de 100 items")
@Transient
private Integer limitesValidation;

// PENDIENTE: Validar montos máximos
@DecimalMax(value = "999999.99", message = "Total no puede exceder $999,999.99")
@Column(name = "total", precision = 12, scale = 2, nullable = false)
private BigDecimal total = BigDecimal.ZERO;

// PENDIENTE: Validar código de descuento
@Pattern(
    regexp = "^[A-Z0-9]{3,20}$",
    message = "Código de descuento debe contener solo letras mayúsculas y números, entre 3-20 caracteres"
)
@Column(name = "codigo_descuento", length = 50)
private String codigoDescuento;
```

### 2. **Validaciones en ItemCarrito.java**

#### **SKU y Código de Producto**
```java
// PENDIENTE: Validar SKU del producto
@NotBlank(message = "SKU del producto es obligatorio")
@Pattern(
    regexp = "^[A-Z0-9-]{3,30}$",
    message = "SKU debe contener solo letras mayúsculas, números y guiones, entre 3-30 caracteres"
)
@Column(name = "sku_producto", nullable = false, length = 30)
private String skuProducto;

// PENDIENTE: Validar código de barras
@Pattern(
    regexp = "^[0-9]{8,14}$",
    message = "Código de barras debe contener entre 8-14 dígitos"
)
@Column(name = "codigo_barras", length = 14)
private String codigoBarras;
```

#### **Validaciones de Stock y Disponibilidad**
```java
// PENDIENTE: Validar stock mínimo
@Positive(message = "Stock debe ser positivo")
@Column(name = "stock_disponible")
private Integer stockDisponible;

// PENDIENTE: Validar peso del producto
@DecimalMin(value = "0.001", message = "Peso debe ser mayor a 0")
@DecimalMax(value = "999.999", message = "Peso no puede exceder 999.999 kg")
@Digits(integer = 3, fraction = 3, message = "Peso debe tener máximo 3 enteros y 3 decimales")
@Column(name = "peso", precision = 8, scale = 3)
private BigDecimal peso;

// PENDIENTE: Validar dimensiones
@DecimalMin(value = "0.1", message = "Dimensiones deben ser positivas")
@Column(name = "largo_cm", precision = 6, scale = 2)
private BigDecimal largoCm;

@DecimalMin(value = "0.1", message = "Dimensiones deben ser positivas")
@Column(name = "ancho_cm", precision = 6, scale = 2)
private BigDecimal anchoCm;

@DecimalMin(value = "0.1", message = "Dimensiones deben ser positivas")
@Column(name = "alto_cm", precision = 6, scale = 2)
private BigDecimal altoCm;
```

#### **Validaciones de URL y Recursos**
```java
// PENDIENTE: Validar URL de imagen
@URL(message = "URL de imagen debe ser válida")
@Size(max = 500, message = "URL no puede exceder 500 caracteres")
@Pattern(
    regexp = ".*\\.(jpg|jpeg|png|gif|webp)$",
    message = "URL debe apuntar a una imagen válida (jpg, jpeg, png, gif, webp)",
    flags = Pattern.Flag.CASE_INSENSITIVE
)
@Column(name = "imagen_url", length = 500)
private String imagenUrl;
```

## 🔍 Validaciones Custom

### 1. **Validador de Coherencia de Precios**
```java
// PENDIENTE: Crear validador personalizado
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrecioCoherenciaValidator.class)
public @interface ValidPrecioCoherencia {
    String message() default "Precio unitario debe ser coherente con subtotal y cantidad";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// PENDIENTE: Implementar validador
public class PrecioCoherenciaValidator implements ConstraintValidator<ValidPrecioCoherencia, ItemCarrito> {
    @Override
    public boolean isValid(ItemCarrito item, ConstraintValidatorContext context) {
        if (item.getPrecioUnitario() == null || item.getCantidad() == null) {
            return true; // Otros validadores manejan nulls
        }
        
        BigDecimal subtotalCalculado = item.getPrecioUnitario()
            .multiply(new BigDecimal(item.getCantidad()));
        
        return item.getSubtotal().compareTo(subtotalCalculado) == 0;
    }
}

// PENDIENTE: Aplicar en ItemCarrito
@ValidPrecioCoherencia
public class ItemCarrito extends BaseEntityCorrected {
    // Contenido existente...
}
```

### 2. **Validador de Estado de Carrito**
```java
// PENDIENTE: Validador de transiciones de estado
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EstadoTransicionValidator.class)
public @interface ValidEstadoTransicion {
    String message() default "Transición de estado no válida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// PENDIENTE: Implementar validador
public class EstadoTransicionValidator implements ConstraintValidator<ValidEstadoTransicion, Carrito> {
    @Override
    public boolean isValid(Carrito carrito, ConstraintValidatorContext context) {
        // Implementar lógica de validación de transiciones de estado
        return true; // Placeholder
    }
}
```

### 3. **Validador de Capacidad de Carrito**
```java
// PENDIENTE: Validador de capacidad máxima
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CapacidadCarritoValidator.class)
public @interface ValidCapacidadCarrito {
    String message() default "Carrito excede la capacidad máxima permitida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int maxItems() default 100;
    String maxPeso() default "50.0"; // kg
    String maxTotal() default "999999.99";
}
```

## 📧 Validaciones de Datos de Contacto

### 1. **DescuentoAplicado.java**
```java
// PENDIENTE: Validar formato de códigos promocionales
@Pattern(
    regexp = "^[A-Z0-9]{3,20}$",
    message = "Código de descuento debe ser alfanumérico en mayúsculas, 3-20 caracteres"
)
@NotBlank(message = "Código de descuento es obligatorio")
@Column(name = "codigo_descuento", nullable = false, length = 100)
private String codigoDescuento;

// PENDIENTE: Validar rangos de porcentajes
@DecimalMin(value = "0.01", message = "Porcentaje debe ser mayor a 0.01%")
@DecimalMax(value = "99.99", message = "Porcentaje no puede exceder 99.99%")
@Digits(integer = 2, fraction = 2, message = "Porcentaje debe tener máximo 2 enteros y 2 decimales")
@Column(name = "porcentaje_descuento", precision = 5, scale = 2)
private BigDecimal porcentajeDescuento;

// PENDIENTE: Validar ID de campaña
@Pattern(
    regexp = "^CAMP-[0-9]{4}-[A-Z0-9]{8}$",
    message = "ID de campaña debe seguir formato CAMP-YYYY-XXXXXXXX"
)
@Column(name = "campana_id", length = 50)
private String campanaId;
```

### 2. **CarritoHistorial.java**
```java
// PENDIENTE: Validar User Agent
@Size(max = 200, message = "User Agent no puede exceder 200 caracteres")
@Pattern(
    regexp = "^[\\x20-\\x7E]*$",
    message = "User Agent debe contener solo caracteres ASCII imprimibles"
)
@Column(name = "user_agent", length = 200)
private String userAgent;

// PENDIENTE: Validar JSON de datos de operación
@Valid
@JsonValid(message = "Datos de operación deben ser JSON válido")
@Column(name = "datos_operacion")
private String datosOperacion;
```

## 🔐 Validaciones de Seguridad Avanzadas

### 1. **Sanitización de Inputs**
```java
// PENDIENTE: Validador anti-XSS
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AntiXSSValidator.class)
public @interface SafeText {
    String message() default "Texto contiene caracteres peligrosos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// PENDIENTE: Aplicar en campos de texto libre
@SafeText
@Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
@Column(name = "notas", length = 500)
private String notas;
```

### 2. **Validaciones de Rate Limiting**
```java
// PENDIENTE: Validador de frecuencia de operaciones
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RateLimitValidator.class)
public @interface RateLimit {
    String message() default "Exceso de operaciones en período de tiempo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int maxOperations() default 10;
    int timeWindowMinutes() default 1;
}
```

## ⚡ Validaciones de Performance

### 1. **Validaciones Asíncronas para Datos Externos**
```java
// PENDIENTE: Validador asíncrono de stock
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StockDisponibleValidator.class)
public @interface ValidStockDisponible {
    String message() default "Stock insuficiente para la cantidad solicitada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    boolean async() default true;
}

// PENDIENTE: Aplicar en ItemCarrito
@ValidStockDisponible(groups = ValidationGroups.External.class)
@Column(name = "cantidad", nullable = false)
private Integer cantidad;
```

### 2. **Grupos de Validación**
```java
// PENDIENTE: Crear grupos de validación
public interface ValidationGroups {
    interface Basic extends Default {}
    interface Business {}
    interface External {}
    interface Security {}
    interface Performance {}
}

// PENDIENTE: Aplicar en controladores
@Validated({ValidationGroups.Basic.class, ValidationGroups.Business.class})
@RestController
public class CarritoController {
    // Métodos del controlador...
}
```

## 📊 Validaciones de Integridad Referencial

### 1. **Validaciones Cross-Entity**
```java
// PENDIENTE: Validador de consistencia carrito-items
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CarritoItemsConsistencyValidator.class)
public @interface ValidCarritoItemsConsistency {
    String message() default "Inconsistencia entre carrito y sus items";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// PENDIENTE: Aplicar en Carrito
@ValidCarritoItemsConsistency
public class Carrito extends BaseEntityCorrected {
    // Contenido existente...
}
```

### 2. **Validaciones de Sumatorias**
```java
// PENDIENTE: Validador de totales
public class TotalesValidator implements ConstraintValidator<ValidTotales, Carrito> {
    @Override
    public boolean isValid(Carrito carrito, ConstraintValidatorContext context) {
        if (carrito.getItems().isEmpty()) {
            return carrito.getTotal().compareTo(BigDecimal.ZERO) == 0;
        }
        
        BigDecimal subtotalCalculado = carrito.getItems().stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalCalculado = subtotalCalculado.subtract(carrito.getDescuento());
        
        return carrito.getTotal().compareTo(totalCalculado) == 0;
    }
}
```

## 📋 Checklist de Implementación

### Validaciones Básicas
- [ ] **1. Agregar validaciones de email y formato de datos**
- [ ] **2. Implementar validaciones de rangos monetarios**
- [ ] **3. Validar códigos y SKUs con patrones**
- [ ] **4. Agregar validaciones de URLs y recursos**
- [ ] **5. Implementar sanitización anti-XSS**

### Validaciones de Negocio
- [ ] **6. Crear validador de coherencia de precios**
- [ ] **7. Implementar validador de transiciones de estado**
- [ ] **8. Validar capacidad máxima de carrito**
- [ ] **9. Crear validaciones cross-entity**
- [ ] **10. Implementar validador de totales**

### Validaciones Avanzadas
- [ ] **11. Implementar rate limiting validation**
- [ ] **12. Crear validaciones asíncronas de stock**
- [ ] **13. Configurar grupos de validación**
- [ ] **14. Implementar validaciones de seguridad**
- [ ] **15. Crear tests para todas las validaciones**

## 📈 Beneficios Esperados

### Integridad de Datos
- ✅ **+95%** de precisión en datos de carrito
- ✅ **0 errores** de inconsistencia en totales
- ✅ **100% validación** de formatos críticos

### Seguridad
- ✅ **Prevención** de ataques XSS en inputs
- ✅ **Rate limiting** automático en operaciones
- ✅ **Validación** de IPs y user agents

### Performance
- ✅ **Validación temprana** antes de lógica de negocio
- ✅ **Validaciones asíncronas** para datos externos
- ✅ **Grupos de validación** para optimizar checks

---

**Próximo paso:** Implementar validaciones en orden de criticidad.  
**Dependencias:** Testing framework debe estar configurado para validar.