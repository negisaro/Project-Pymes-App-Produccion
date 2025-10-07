````markdown
# 🔗 Integración con Backend - Microservicio Carrito

## 🏗️ Arquitectura del Microservicio

### 📡 **Endpoints Disponibles**

#### **Carrito de Usuario** (`/api/segura/carrito`)
```java
@RestController
@RequestMapping("/api/segura/carrito")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('CLIENT')")
public class CarritoController {

    // ✅ Obtener carrito actual del usuario
    @GetMapping
    public ResponseEntity<ApiResponse<CarritoDto>> obtenerCarrito(Authentication auth)
    
    // ✅ Agregar producto al carrito
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CarritoDto>> agregarItem(
        @RequestBody AgregarItemRequest request, Authentication auth)
    
    // ✅ Actualizar cantidad de item
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CarritoDto>> actualizarCantidad(
        @PathVariable Long itemId, @RequestBody ActualizarCantidadRequest request)
    
    // ✅ Eliminar item del carrito
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CarritoDto>> eliminarItem(@PathVariable Long itemId)
    
    // ✅ Aplicar cupón de descuento
    @PostMapping("/cupones")
    public ResponseEntity<ApiResponse<CarritoDto>> aplicarCupon(
        @RequestBody AplicarCuponRequest request)
    
    // ✅ Limpiar carrito completo
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> limpiarCarrito(Authentication auth)
    
    // 🔄 Migrar carrito guest
    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<CarritoDto>> migrarCarritoGuest(
        @RequestBody MigrarCarritoRequest request, Authentication auth)
}
```

#### **Validaciones Públicas** (`/api/public/carrito`)
```java
@RestController
@RequestMapping("/api/public/carrito")
public class CarritoPublicController {

    // ✅ Validar disponibilidad de producto
    @PostMapping("/validar-producto")
    public ResponseEntity<ApiResponse<ProductoValidacionDto>> validarProducto(
        @RequestBody ValidarProductoRequest request)
    
    // ✅ Calcular estimación de envío
    @PostMapping("/calcular-envio")
    public ResponseEntity<ApiResponse<CalculoEnvioDto>> calcularEnvio(
        @RequestBody CalcularEnvioRequest request)
    
    // ✅ Obtener promociones activas
    @GetMapping("/promociones")
    public ResponseEntity<ApiResponse<List<PromocionDto>>> obtenerPromociones()
}
```

---

## 📋 Modelos de Datos (DTOs)

### **CarritoDto**
```java
public class CarritoDto {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal descuentos;
    private BigDecimal impuestos;
    private BigDecimal envio;
    private BigDecimal total;
    private List<CuponAplicadoDto> cupones;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    private String estado; // ACTIVO, ABANDONADO, CONVERTIDO
    
    // Campos calculados
    private Integer totalItems;
    private Boolean tieneDescuentos;
    private Boolean requiereEnvio;
}
```

### **CarritoItemDto**
```java
public class CarritoItemDto {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private String imagenProducto;
    private BigDecimal precioUnitario;
    private BigDecimal precioOriginal;
    private Integer cantidad;
    private BigDecimal subtotal;
    private BigDecimal descuentoItem;
    private String varianteId;
    private Map<String, String> atributosVariante;
    private Boolean disponible;
    private Integer stockDisponible;
    private LocalDateTime agregadoEn;
}
```

### **Request Models**
```java
// Agregar item
public class AgregarItemRequest {
    @NotNull
    private Long productoId;
    
    @Min(1) @Max(999)
    private Integer cantidad;
    
    private String varianteId;
    private Map<String, String> opciones;
}

// Actualizar cantidad
public class ActualizarCantidadRequest {
    @Min(0) @Max(999)
    private Integer cantidad;
}

// Aplicar cupón
public class AplicarCuponRequest {
    @NotBlank
    private String codigoCupon;
}

// Migrar carrito guest
public class MigrarCarritoRequest {
    private List<ItemGuestDto> itemsGuest;
    
    public static class ItemGuestDto {
        private Long productoId;
        private Integer cantidad;
        private String varianteId;
    }
}
```

---

## 🔄 Servicios Frontend Integrados

### **CartApiService**
```typescript
@Injectable({ providedIn: 'root' })
export class CartApiService {
  
  private readonly baseUrl = `${environment.baseUrl}/api/segura/carrito`;
  
  constructor(
    private http: HttpClient,
    private errorHandler: ErrorHandlerService
  ) {}
  
  // Obtener carrito actual
  getCart(): Observable<CarritoDto> {
    return this.http.get<ApiResponse<CarritoDto>>(this.baseUrl).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Agregar producto al carrito
  addItem(request: AgregarItemRequest): Observable<CarritoDto> {
    return this.http.post<ApiResponse<CarritoDto>>(`${this.baseUrl}/items`, request).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Actualizar cantidad
  updateQuantity(itemId: number, quantity: number): Observable<CarritoDto> {
    return this.http.put<ApiResponse<CarritoDto>>(`${this.baseUrl}/items/${itemId}`, {
      cantidad: quantity
    }).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Eliminar item
  removeItem(itemId: number): Observable<CarritoDto> {
    return this.http.delete<ApiResponse<CarritoDto>>(`${this.baseUrl}/items/${itemId}`).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Aplicar cupón
  applyCoupon(couponCode: string): Observable<CarritoDto> {
    return this.http.post<ApiResponse<CarritoDto>>(`${this.baseUrl}/cupones`, {
      codigoCupon: couponCode
    }).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Migrar carrito guest
  migrateGuestCart(guestItems: GuestCartItem[]): Observable<CarritoDto> {
    const request = {
      itemsGuest: guestItems.map(item => ({
        productoId: item.productId,
        cantidad: item.quantity,
        varianteId: item.variantId
      }))
    };
    
    return this.http.post<ApiResponse<CarritoDto>>(`${this.baseUrl}/merge`, request).pipe(
      map(response => response.data),
      catchError(this.errorHandler.handle)
    );
  }
  
  // Limpiar carrito
  clearCart(): Observable<void> {
    return this.http.delete<ApiResponse<void>>(this.baseUrl).pipe(
      map(() => void 0),
      catchError(this.errorHandler.handle)
    );
  }
}
```

### **CartPublicApiService**
```typescript
@Injectable({ providedIn: 'root' })
export class CartPublicApiService {
  
  private readonly baseUrl = `${environment.baseUrl}/api/public/carrito`;
  
  constructor(private http: HttpClient) {}
  
  // Validar disponibilidad de producto
  validateProduct(productId: number, quantity: number): Observable<ProductValidationDto> {
    return this.http.post<ApiResponse<ProductValidationDto>>(`${this.baseUrl}/validar-producto`, {
      productoId: productId,
      cantidad: quantity
    }).pipe(map(response => response.data));
  }
  
  // Calcular costo de envío
  calculateShipping(items: CartItem[], shippingAddress: Address): Observable<ShippingCalculationDto> {
    return this.http.post<ApiResponse<ShippingCalculationDto>>(`${this.baseUrl}/calcular-envio`, {
      items: items.map(item => ({
        productoId: item.productId,
        cantidad: item.quantity,
        peso: item.weight,
        dimensiones: item.dimensions
      })),
      direccionEnvio: shippingAddress
    }).pipe(map(response => response.data));
  }
  
  // Obtener promociones activas
  getActivePromotions(): Observable<PromocionDto[]> {
    return this.http.get<ApiResponse<PromocionDto[]>>(`${this.baseUrl}/promociones`).pipe(
      map(response => response.data)
    );
  }
}
```

---

## 🔄 Mappers Frontend-Backend

### **CartMapper**
```typescript
@Injectable({ providedIn: 'root' })
export class CartMapper {
  
  // Backend DTO -> Frontend Model
  fromDto(dto: CarritoDto): Cart {
    return {
      id: dto.id.toString(),
      userId: dto.usuarioId,
      items: dto.items.map(item => this.mapCartItem(item)),
      subtotal: dto.subtotal,
      discounts: dto.descuentos,
      tax: dto.impuestos,
      shipping: dto.envio,
      total: dto.total,
      coupons: dto.cupones?.map(coupon => this.mapCoupon(coupon)) || [],
      createdAt: new Date(dto.creadoEn),
      updatedAt: new Date(dto.actualizadoEn),
      status: dto.estado as CartStatus,
      
      // Campos calculados
      itemCount: dto.totalItems,
      hasDiscounts: dto.tieneDescuentos,
      requiresShipping: dto.requiereEnvio
    };
  }
  
  private mapCartItem(dto: CarritoItemDto): CartItem {
    return {
      id: dto.id.toString(),
      productId: dto.productoId,
      name: dto.nombreProducto,
      description: dto.descripcionProducto,
      image: dto.imagenProducto,
      unitPrice: dto.precioUnitario,
      originalPrice: dto.precioOriginal,
      quantity: dto.cantidad,
      subtotal: dto.subtotal,
      discount: dto.descuentoItem,
      variantId: dto.varianteId,
      variantAttributes: dto.atributosVariante,
      available: dto.disponible,
      stockAvailable: dto.stockDisponible,
      addedAt: new Date(dto.agregadoEn)
    };
  }
  
  // Frontend Model -> Backend Request
  toAddItemRequest(product: Product, quantity: number, options?: ProductOptions): AgregarItemRequest {
    return {
      productoId: product.id,
      cantidad: quantity,
      varianteId: product.selectedVariant?.id,
      opciones: options
    };
  }
  
  // Guest Cart -> Migration Request
  toMigrationRequest(guestCart: GuestCart): MigrarCarritoRequest {
    return {
      itemsGuest: guestCart.items.map(item => ({
        productoId: item.productId,
        cantidad: item.quantity,
        varianteId: item.variantId
      }))
    };
  }
}
```

---

## 🔍 Validaciones y Reglas de Negocio

### **CartValidationService**
```typescript
@Injectable({ providedIn: 'root' })
export class CartValidationService {
  
  constructor(
    private publicApi: CartPublicApiService,
    private productService: ProductService
  ) {}
  
  // Validar antes de agregar al carrito
  validateAddition(product: Product, quantity: number): Observable<ValidationResult> {
    return combineLatest([
      this.validateProductExists(product.id),
      this.validateQuantityLimits(quantity),
      this.validateStock(product.id, quantity),
      this.validateProductActive(product)
    ]).pipe(
      map(([productExists, quantityValid, stockValid, productActive]) => ({
        valid: productExists && quantityValid && stockValid && productActive,
        errors: [
          ...(!productExists ? ['Producto no encontrado'] : []),
          ...(!quantityValid ? ['Cantidad inválida'] : []),
          ...(!stockValid ? ['Stock insuficiente'] : []),
          ...(!productActive ? ['Producto no disponible'] : [])
        ]
      }))
    );
  }
  
  private validateProductExists(productId: number): Observable<boolean> {
    return this.productService.getProduct(productId).pipe(
      map(product => !!product),
      catchError(() => of(false))
    );
  }
  
  private validateQuantityLimits(quantity: number): Observable<boolean> {
    return of(quantity > 0 && quantity <= 999);
  }
  
  private validateStock(productId: number, quantity: number): Observable<boolean> {
    return this.publicApi.validateProduct(productId, quantity).pipe(
      map(validation => validation.disponible && validation.stockSuficiente)
    );
  }
  
  private validateProductActive(product: Product): Observable<boolean> {
    return of(product.active && !product.discontinued);
  }
}
```

---

## 🚨 Manejo de Errores

### **CartErrorHandlerService**
```typescript
@Injectable({ providedIn: 'root' })
export class CartErrorHandlerService {
  
  constructor(
    private toast: ToastrService,
    private logger: LoggerService
  ) {}
  
  handle(error: HttpErrorResponse): Observable<never> {
    const errorInfo = this.parseError(error);
    
    switch (errorInfo.type) {
      case 'STOCK_INSUFFICIENT':
        this.handleStockError(errorInfo);
        break;
        
      case 'PRODUCT_NOT_FOUND':
        this.handleProductNotFound(errorInfo);
        break;
        
      case 'CART_LIMIT_EXCEEDED':
        this.handleCartLimitError(errorInfo);
        break;
        
      case 'INVALID_COUPON':
        this.handleCouponError(errorInfo);
        break;
        
      default:
        this.handleGenericError(errorInfo);
    }
    
    this.logger.error('Cart operation failed', errorInfo);
    return throwError(() => errorInfo);
  }
  
  private parseError(error: HttpErrorResponse): CartError {
    if (error.error?.error) {
      return {
        type: error.error.error.tipo || 'UNKNOWN_ERROR',
        message: error.error.error.mensaje || 'Error desconocido',
        details: error.error.error.detalles || {},
        statusCode: error.status
      };
    }
    
    return {
      type: 'NETWORK_ERROR',
      message: 'Error de conexión',
      details: { originalError: error.message },
      statusCode: error.status
    };
  }
  
  private handleStockError(error: CartError): void {
    this.toast.warning(
      `Stock insuficiente. Disponible: ${error.details.stockDisponible}`,
      'Stock Limitado'
    );
  }
  
  private handleProductNotFound(error: CartError): void {
    this.toast.error('El producto ya no está disponible', 'Producto No Disponible');
  }
  
  private handleCartLimitError(error: CartError): void {
    this.toast.warning(
      `Límite de carrito alcanzado (${error.details.limite} items)`,
      'Límite Alcanzado'
    );
  }
  
  private handleCouponError(error: CartError): void {
    this.toast.error(error.message, 'Cupón Inválido');
  }
  
  private handleGenericError(error: CartError): void {
    this.toast.error('Error procesando la solicitud', 'Error');
  }
}
```

---

## 📊 Configuración de Environment

### **Environment Variables**
```typescript
export const environment = {
  production: false,
  baseUrl: 'http://localhost:8080',
  
  cart: {
    endpoints: {
      secure: '/api/segura/carrito',
      public: '/api/public/carrito'
    },
    
    limits: {
      maxItems: 999,
      maxQuantityPerItem: 99,
      guestCartExpiration: 7 * 24 * 60 * 60 * 1000, // 7 días
      syncInterval: 30000 // 30 segundos
    },
    
    storage: {
      guestCartKey: 'pymes-guest-cart',
      cartPreferencesKey: 'pymes-cart-preferences'
    },
    
    features: {
      enableGuestCart: true,
      enableAutoSync: true,
      enablePromotions: true,
      enableShippingCalculation: true
    }
  }
};
```

---

## 🔄 Sincronización y Cache

### **CartSyncService**
```typescript
@Injectable({ providedIn: 'root' })
export class CartSyncService {
  
  private syncInterval$ = interval(environment.cart.limits.syncInterval);
  
  constructor(
    private cartApi: CartApiService,
    private cartStore: CartStoreService,
    private auth: AuthService,
    private networkStatus: NetworkStatusService
  ) {
    this.setupAutoSync();
  }
  
  private setupAutoSync(): void {
    this.syncInterval$.pipe(
      filter(() => environment.cart.features.enableAutoSync),
      filter(() => this.auth.isAuthenticated()),
      filter(() => this.networkStatus.isOnline()),
      switchMap(() => this.syncWithBackend()),
      takeUntil(this.destroy$)
    ).subscribe();
  }
  
  syncWithBackend(): Observable<Cart> {
    return this.cartApi.getCart().pipe(
      tap(cart => this.cartStore.setCart(cart)),
      catchError(error => {
        console.warn('Cart sync failed:', error);
        return this.cartStore.cart$;
      })
    );
  }
  
  forceSyncIfNeeded(): Observable<Cart> {
    const lastSync = this.cartStore.getLastSyncTime();
    const shouldSync = Date.now() - lastSync > environment.cart.limits.syncInterval;
    
    return shouldSync ? 
      this.syncWithBackend() : 
      this.cartStore.cart$;
  }
}
```

---

**Próximo:** [Diseño Moderno](./04-DISEÑO-MODERNO.md)

````
