# ✅ Diseño Moderno: Clean Architecture para Carrito de Compras
# ✅ Diseño Moderno: Clean Architecture para Carrito de Compras

> **Estado:** 🔄 **PENDIENTE** - Diseño completo a implementar

## 🏗️ Arquitectura Clean Architecture

### 📐 **Principios de Diseño**

```typescript
// 🎯 Principios aplicados:
// 1. Separación de responsabilidades por capas
// 2. Dependencias dirigidas hacia el dominio  
// 3. Independencia de frameworks externos
// 4. Testabilidad completa
// 5. Mantenibilidad y escalabilidad
// 6. Estado inmutable y reactivo
```

### 🏛️ **Estructura de Capas**

```
cart/
├── 🧠 core/                         # DOMINIO (Business Logic)
│   ├── models/                      # Entidades y Value Objects
│   ├── services/                    # Servicios de dominio
│   ├── repositories/                # Interfaces de repositorio
│   └── use-cases/                   # Casos de uso del negocio
├── 🔧 infrastructure/               # INFRAESTRUCTURA
│   ├── api/                         # Implementaciones HTTP
│   ├── storage/                     # LocalStorage/SessionStorage
│   ├── cache/                       # Estrategias de cache
│   └── state/                       # State management
├── 🎨 presentation/                 # PRESENTACIÓN
│   ├── components/                  # Componentes UI puros
│   ├── containers/                  # Contenedores inteligentes
│   ├── pages/                       # Páginas del carrito
│   └── layouts/                     # Layouts específicos
└── 🤝 shared/                       # COMPARTIDO
    ├── interfaces/                  # Contratos compartidos
    ├── utils/                       # Utilidades helper
    └── constants/                   # Constantes del dominio
```

---

## 🧠 Capa de Dominio (Core)

### 🎭 **Entidades de Dominio**

#### `Cart` (Entidad Principal)
```typescript
export class Cart {
  constructor(
    public readonly id: CartId,
    public readonly userId: UserId | null,
    private _items: CartItem[],
    private _status: CartStatus,
    private _createdAt: Date,
    private _updatedAt: Date,
    public readonly metadata: CartMetadata
  ) {}
  
  // ✅ Métodos de dominio inmutables
  addItem(product: Product, quantity: Quantity, options?: ProductOptions): Result<Cart, CartError> {
    const validation = this.validateAddition(product, quantity);
    if (validation.isFailure()) {
      return Result.fail(validation.error);
    }
    
    const existingItem = this.findItem(product.id, options);
    if (existingItem) {
      return this.updateItemQuantity(existingItem.id, existingItem.quantity.add(quantity));
    }
    
    const newItem = CartItem.create(product, quantity, options);
    if (newItem.isFailure()) {
      return Result.fail(newItem.error);
    }
    
    return Result.ok(new Cart(
      this.id,
      this.userId,
      [...this._items, newItem.value],
      this._status,
      this._createdAt,
      new Date(),
      this.metadata
    ));
  }
  
  updateItemQuantity(itemId: CartItemId, newQuantity: Quantity): Result<Cart, CartError> {
    if (newQuantity.isZero()) {
      return this.removeItem(itemId);
    }
    
    const itemIndex = this._items.findIndex(item => item.id.equals(itemId));
    if (itemIndex === -1) {
      return Result.fail(new CartError('ITEM_NOT_FOUND', 'Item no encontrado en el carrito'));
    }
    
    const updatedItems = [...this._items];
    const updatedItem = updatedItems[itemIndex].updateQuantity(newQuantity);
    if (updatedItem.isFailure()) {
      return Result.fail(updatedItem.error);
    }
    
    updatedItems[itemIndex] = updatedItem.value;
    
    return Result.ok(new Cart(
      this.id,
      this.userId,
      updatedItems,
      this._status,
      this._createdAt,
      new Date(),
      this.metadata
    ));
  }
  
  // ✅ Getters calculados
  get items(): readonly CartItem[] {
    return Object.freeze([...this._items]);
  }
  
  get itemCount(): number {
    return this._items.reduce((total, item) => total + item.quantity.value, 0);
  }
  
  get subtotal(): Money {
    return this._items.reduce(
      (total, item) => total.add(item.subtotal),
      Money.zero(this.currency)
    );
  }
  
  get total(): Money {
    return this.subtotal
      .add(this.calculateTax())
      .add(this.calculateShipping())
      .subtract(this.calculateDiscounts());
  }
  
  // ✅ Validaciones de negocio
  private validateAddition(product: Product, quantity: Quantity): Result<void, CartError> {
    if (!product.isAvailable()) {
      return Result.fail(new CartError('PRODUCT_UNAVAILABLE', 'Producto no disponible'));
    }
    
    if (this.wouldExceedLimits(quantity)) {
      return Result.fail(new CartError('CART_LIMIT_EXCEEDED', 'Límite del carrito excedido'));
    }
    
    return Result.ok();
  }
  
  private wouldExceedLimits(additionalQuantity: Quantity): boolean {
    return this.itemCount + additionalQuantity.value > CartLimits.MAX_ITEMS;
  }
}
```

#### `CartItem` (Value Object)
```typescript
export class CartItem {
  private constructor(
    public readonly id: CartItemId,
    public readonly product: Product,
    public readonly quantity: Quantity,
    public readonly unitPrice: Money,
    public readonly options: ProductOptions,
    public readonly addedAt: Date
  ) {}
  
  static create(product: Product, quantity: Quantity, options?: ProductOptions): Result<CartItem, CartError> {
    const validation = CartItem.validate(product, quantity);
    if (validation.isFailure()) {
      return Result.fail(validation.error);
    }
    
    return Result.ok(new CartItem(
      CartItemId.generate(),
      product,
      quantity,
      product.currentPrice,
      options || ProductOptions.empty(),
      new Date()
    ));
  }
  
  updateQuantity(newQuantity: Quantity): Result<CartItem, CartError> {
    if (newQuantity.isZero()) {
      return Result.fail(new CartError('INVALID_QUANTITY', 'La cantidad debe ser mayor a cero'));
    }
    
    return Result.ok(new CartItem(
      this.id,
      this.product,
      newQuantity,
      this.unitPrice,
      this.options,
      this.addedAt
    ));
  }
  
  get subtotal(): Money {
    return this.unitPrice.multiply(this.quantity.value);
  }
  
  get isAvailable(): boolean {
    return this.product.isAvailable() && this.product.hasStock(this.quantity);
  }
  
  private static validate(product: Product, quantity: Quantity): Result<void, CartError> {
    if (!product) {
      return Result.fail(new CartError('INVALID_PRODUCT', 'Producto inválido'));
    }
    
    if (quantity.isZero() || quantity.isNegative()) {
      return Result.fail(new CartError('INVALID_QUANTITY', 'Cantidad inválida'));
    }
    
    return Result.ok();
  }
}
```

### 🎯 **Casos de Uso**

#### `AddItemToCartUseCase`
```typescript
@Injectable({ providedIn: 'root' })
export class AddItemToCartUseCase {
  
  constructor(
    private cartRepository: CartRepository,
    private productRepository: ProductRepository,
    private cartValidationService: CartValidationService,
    private eventBus: EventBus
  ) {}
  
  async execute(command: AddItemToCartCommand): Promise<Result<Cart, CartError>> {
    try {
      // 1. Obtener carrito actual
      const cart = await this.cartRepository.findByUserId(command.userId);
      
      // 2. Obtener y validar producto
      const product = await this.productRepository.findById(command.productId);
      if (!product) {
        return Result.fail(new CartError('PRODUCT_NOT_FOUND', 'Producto no encontrado'));
      }
      
      // 3. Validar disponibilidad y stock
      const validation = await this.cartValidationService.validateAddition(
        cart, product, command.quantity, command.options
      );
      if (validation.isFailure()) {
        return Result.fail(validation.error);
      }
      
      // 4. Aplicar lógica de dominio
      const quantity = Quantity.create(command.quantity);
      if (quantity.isFailure()) {
        return Result.fail(quantity.error);
      }
      
      const updatedCart = cart.addItem(product, quantity.value, command.options);
      if (updatedCart.isFailure()) {
        return Result.fail(updatedCart.error);
      }
      
      // 5. Persistir cambios
      const savedCart = await this.cartRepository.save(updatedCart.value);
      
      // 6. Publicar evento
      await this.eventBus.publish(new ItemAddedToCartEvent({
        cartId: savedCart.id,
        userId: command.userId,
        productId: command.productId,
        quantity: command.quantity,
        timestamp: new Date()
      }));
      
      return Result.ok(savedCart);
      
    } catch (error) {
      return Result.fail(new CartError('OPERATION_FAILED', 'Error agregando item al carrito', error));
    }
  }
}

export interface AddItemToCartCommand {
  userId: UserId;
  productId: ProductId;
  quantity: number;
  options?: ProductOptions;
}
```

#### `CalculateCartTotalsUseCase`
```typescript
@Injectable({ providedIn: 'root' })
export class CalculateCartTotalsUseCase {
  
  constructor(
    private taxCalculationService: TaxCalculationService,
    private shippingCalculationService: ShippingCalculationService,
    private promotionService: PromotionService
  ) {}
  
  async execute(cart: Cart, shippingAddress?: Address): Promise<CartTotals> {
    // 1. Calcular subtotal
    const subtotal = cart.subtotal;
    
    // 2. Aplicar promociones
    const promotions = await this.promotionService.getEligiblePromotions(cart);
    const discounts = this.calculateDiscounts(cart, promotions);
    
    // 3. Calcular impuestos
    const taxableAmount = subtotal.subtract(discounts);
    const tax = await this.taxCalculationService.calculate(taxableAmount, shippingAddress);
    
    // 4. Calcular envío
    const shipping = shippingAddress ? 
      await this.shippingCalculationService.calculate(cart, shippingAddress) :
      Money.zero(cart.currency);
    
    // 5. Total final
    const total = subtotal.add(tax).add(shipping).subtract(discounts);
    
    return new CartTotals({
      subtotal,
      discounts,
      tax,
      shipping,
      total,
      appliedPromotions: promotions
    });
  }
  
  private calculateDiscounts(cart: Cart, promotions: Promotion[]): Money {
    return promotions.reduce(
      (total, promotion) => total.add(promotion.calculateDiscount(cart)),
      Money.zero(cart.currency)
    );
  }
}
```

---

## 🔧 Capa de Infraestructura

### 📡 **Implementación de Repositorios**

#### `HttpCartRepository`
```typescript
@Injectable({ providedIn: 'root' })
export class HttpCartRepository implements CartRepository {
  
  constructor(
    private http: HttpClient,
    private cartMapper: CartMapper,
    private errorHandler: ErrorHandlerService
  ) {}
  
  async findByUserId(userId: UserId): Promise<Cart> {
    try {
      const response = await this.http.get<ApiResponse<CarritoDto>>(
        `/api/segura/carrito`
      ).toPromise();
      
      return this.cartMapper.fromDto(response.data);
    } catch (error) {
      if (error.status === 404) {
        return Cart.createEmpty(userId);
      }
      throw this.errorHandler.transform(error);
    }
  }
  
  async save(cart: Cart): Promise<Cart> {
    try {
      const dto = this.cartMapper.toDto(cart);
      const response = await this.http.put<ApiResponse<CarritoDto>>(
        `/api/segura/carrito`,
        dto
      ).toPromise();
      
      return this.cartMapper.fromDto(response.data);
    } catch (error) {
      throw this.errorHandler.transform(error);
    }
  }
  
  async addItem(cartId: CartId, item: CartItem): Promise<Cart> {
    try {
      const request = this.cartMapper.toAddItemRequest(item);
      const response = await this.http.post<ApiResponse<CarritoDto>>(
        `/api/segura/carrito/items`,
        request
      ).toPromise();
      
      return this.cartMapper.fromDto(response.data);
    } catch (error) {
      throw this.errorHandler.transform(error);
    }
  }
  
  async updateItem(cartId: CartId, itemId: CartItemId, quantity: Quantity): Promise<Cart> {
    try {
      const response = await this.http.put<ApiResponse<CarritoDto>>(
        `/api/segura/carrito/items/${itemId.value}`,
        { cantidad: quantity.value }
      ).toPromise();
      
      return this.cartMapper.fromDto(response.data);
    } catch (error) {
      throw this.errorHandler.transform(error);
    }
  }
  
  async removeItem(cartId: CartId, itemId: CartItemId): Promise<Cart> {
    try {
      const response = await this.http.delete<ApiResponse<CarritoDto>>(
        `/api/segura/carrito/items/${itemId.value}`
      ).toPromise();
      
      return this.cartMapper.fromDto(response.data);
    } catch (error) {
      throw this.errorHandler.transform(error);
    }
  }
}
```

### 💾 **State Management**

#### `CartStateService` (NgRx-inspired)
```typescript
export interface CartState {
  readonly cart: Cart | null;
  readonly loading: boolean;
  readonly error: CartError | null;
  readonly lastSyncTime: Date | null;
  readonly optimisticUpdates: CartOperation[];
}

@Injectable({ providedIn: 'root' })
export class CartStateService {
  
  private readonly _state = new BehaviorSubject<CartState>(initialCartState);
  
  // Selectores
  readonly state$ = this._state.asObservable();
  readonly cart$ = this.state$.pipe(map(state => state.cart));
  readonly loading$ = this.state$.pipe(map(state => state.loading));
  readonly error$ = this.state$.pipe(map(state => state.error));
  readonly itemCount$ = this.cart$.pipe(map(cart => cart?.itemCount || 0));
  readonly total$ = this.cart$.pipe(map(cart => cart?.total || Money.zero()));
  
  // Acciones
  setLoading(loading: boolean): void {
    this.updateState(state => ({ ...state, loading, error: null }));
  }
  
  setCart(cart: Cart): void {
    this.updateState(state => ({
      ...state,
      cart,
      loading: false,
      error: null,
      lastSyncTime: new Date(),
      optimisticUpdates: []
    }));
  }
  
  setError(error: CartError): void {
    this.updateState(state => ({ ...state, error, loading: false }));
  }
  
  addOptimisticUpdate(operation: CartOperation): void {
    this.updateState(state => ({
      ...state,
      optimisticUpdates: [...state.optimisticUpdates, operation]
    }));
  }
  
  private updateState(updater: (state: CartState) => CartState): void {
    this._state.next(updater(this._state.value));
  }
  
  getCurrentState(): CartState {
    return this._state.value;
  }
}
```

#### `CartCacheService`
```typescript
@Injectable({ providedIn: 'root' })
export class CartCacheService {
  
  private readonly cache = new Map<string, CacheEntry<any>>();
  private readonly TTL = 5 * 60 * 1000; // 5 minutos
  
  set<T>(key: string, value: T, ttl: number = this.TTL): void {
    this.cache.set(key, {
      value,
      expiresAt: Date.now() + ttl,
      createdAt: Date.now()
    });
  }
  
  get<T>(key: string): T | null {
    const entry = this.cache.get(key);
    if (!entry) return null;
    
    if (Date.now() > entry.expiresAt) {
      this.cache.delete(key);
      return null;
    }
    
    return entry.value;
  }
  
  invalidate(pattern: string): void {
    const regex = new RegExp(pattern);
    Array.from(this.cache.keys())
      .filter(key => regex.test(key))
      .forEach(key => this.cache.delete(key));
  }
  
  clear(): void {
    this.cache.clear();
  }
  
  // Cache específico para carritos
  cacheCart(userId: UserId, cart: Cart): void {
    this.set(`cart:${userId.value}`, cart, 10 * 60 * 1000); // 10 minutos
  }
  
  getCachedCart(userId: UserId): Cart | null {
    return this.get(`cart:${userId.value}`);
  }
  
  invalidateUserCart(userId: UserId): void {
    this.invalidate(`cart:${userId.value}`);
  }
}
```

---

## 🎨 Capa de Presentación

### 🎭 **Facade Service**

#### `CartFacade`
```typescript
@Injectable({ providedIn: 'root' })
export class CartFacade {
  
  // Observables públicos
  readonly cart$ = this.cartState.cart$;
  readonly loading$ = this.cartState.loading$;
  readonly error$ = this.cartState.error$;
  readonly itemCount$ = this.cartState.itemCount$;
  readonly total$ = this.cartState.total$;
  
  constructor(
    private addItemUseCase: AddItemToCartUseCase,
    private updateQuantityUseCase: UpdateCartItemQuantityUseCase,
    private removeItemUseCase: RemoveCartItemUseCase,
    private calculateTotalsUseCase: CalculateCartTotalsUseCase,
    private cartState: CartStateService,
    private auth: AuthService
  ) {}
  
  // API simplificada para componentes
  async addItem(product: Product, quantity: number, options?: ProductOptions): Promise<void> {
    this.cartState.setLoading(true);
    
    try {
      const userId = await this.auth.getCurrentUserId();
      const result = await this.addItemUseCase.execute({
        userId,
        productId: product.id,
        quantity,
        options
      });
      
      if (result.isSuccess()) {
        this.cartState.setCart(result.value);
      } else {
        this.cartState.setError(result.error);
      }
    } catch (error) {
      this.cartState.setError(new CartError('OPERATION_FAILED', 'Error agregando producto'));
    }
  }
  
  async updateQuantity(itemId: string, quantity: number): Promise<void> {
    this.cartState.setLoading(true);
    
    try {
      const result = await this.updateQuantityUseCase.execute({
        itemId: CartItemId.fromString(itemId),
        quantity: Quantity.create(quantity).value
      });
      
      if (result.isSuccess()) {
        this.cartState.setCart(result.value);
      } else {
        this.cartState.setError(result.error);
      }
    } catch (error) {
      this.cartState.setError(new CartError('OPERATION_FAILED', 'Error actualizando cantidad'));
    }
  }
  
  async removeItem(itemId: string): Promise<void> {
    this.cartState.setLoading(true);
    
    try {
      const result = await this.removeItemUseCase.execute({
        itemId: CartItemId.fromString(itemId)
      });
      
      if (result.isSuccess()) {
        this.cartState.setCart(result.value);
      } else {
        this.cartState.setError(result.error);
      }
    } catch (error) {
      this.cartState.setError(new CartError('OPERATION_FAILED', 'Error eliminando producto'));
    }
  }
  
  // Helpers para componentes
  isItemInCart(productId: number): Observable<boolean> {
    return this.cart$.pipe(
      map(cart => cart?.items.some(item => item.product.id.value === productId) || false)
    );
  }
  
  getItemQuantity(productId: number): Observable<number> {
    return this.cart$.pipe(
      map(cart => {
        const item = cart?.items.find(item => item.product.id.value === productId);
        return item?.quantity.value || 0;
      })
    );
  }
}
```

### 🧩 **Componentes Inteligentes**

#### `CartPageContainer`
```typescript
@Component({
  selector: 'app-cart-page',
  template: `
    <div class="cart-page">
      <app-cart-header 
        [itemCount]="itemCount$ | async"
        [total]="total$ | async">
      </app-cart-header>
      
      <div class="cart-content" *ngIf="cart$ | async as cart; else emptyCart">
        <app-cart-items-list
          [items]="cart.items"
          [loading]="loading$ | async"
          (quantityChange)="onQuantityChange($event)"
          (removeItem)="onRemoveItem($event)">
        </app-cart-items-list>
        
        <app-cart-summary
          [cart]="cart"
          [loading]="loading$ | async"
          (applyCoupon)="onApplyCoupon($event)"
          (removeCoupon)="onRemoveCoupon($event)">
        </app-cart-summary>
      </div>
      
      <ng-template #emptyCart>
        <app-empty-cart></app-empty-cart>
      </ng-template>
      
      <app-cart-actions
        [cart]="cart$ | async"
        [loading]="loading$ | async"
        (checkout)="onCheckout()"
        (continueShopping)="onContinueShopping()">
      </app-cart-actions>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartPageContainer {
  
  readonly cart$ = this.cartFacade.cart$;
  readonly loading$ = this.cartFacade.loading$;
  readonly error$ = this.cartFacade.error$;
  readonly itemCount$ = this.cartFacade.itemCount$;
  readonly total$ = this.cartFacade.total$;
  
  constructor(
    private cartFacade: CartFacade,
    private router: Router,
    private notificationService: NotificationService
  ) {}
  
  onQuantityChange(event: { itemId: string; quantity: number }): void {
    this.cartFacade.updateQuantity(event.itemId, event.quantity);
  }
  
  onRemoveItem(itemId: string): void {
    this.cartFacade.removeItem(itemId);
  }
  
  onApplyCoupon(couponCode: string): void {
    // TODO: Implementar aplicación de cupón
  }
  
  onRemoveCoupon(couponId: string): void {
    // TODO: Implementar eliminación de cupón
  }
  
  onCheckout(): void {
    this.router.navigate(['/checkout']);
  }
  
  onContinueShopping(): void {
    this.router.navigate(['/productos']);
  }
}
```

### 🎨 **Componentes de Presentación**

#### `CartItemComponent`
```typescript
@Component({
  selector: 'app-cart-item',
  template: `
    <div class="cart-item" [class.unavailable]="!item.isAvailable">
      <div class="item-image">
        <img [src]="item.product.image" [alt]="item.product.name" loading="lazy">
      </div>
      
      <div class="item-details">
        <h3 class="item-name">{{ item.product.name }}</h3>
        <p class="item-description">{{ item.product.description }}</p>
        
        <div class="item-options" *ngIf="item.options.hasOptions()">
          <span *ngFor="let option of item.options.entries()" class="option-tag">
            {{ option.name }}: {{ option.value }}
          </span>
        </div>
      </div>
      
      <div class="item-quantity">
        <app-quantity-selector
          [value]="item.quantity.value"
          [min]="1"
          [max]="item.product.maxOrderQuantity"
          [disabled]="disabled"
          (change)="onQuantityChange($event)">
        </app-quantity-selector>
      </div>
      
      <div class="item-pricing">
        <div class="unit-price">{{ item.unitPrice | currency }}</div>
        <div class="subtotal">{{ item.subtotal | currency }}</div>
      </div>
      
      <div class="item-actions">
        <button 
          type="button" 
          class="btn btn-remove"
          [disabled]="disabled"
          (click)="onRemove()">
          <i class="icon-trash"></i>
          Eliminar
        </button>
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartItemComponent {
  @Input() item!: CartItem;
  @Input() disabled = false;
  
  @Output() quantityChange = new EventEmitter<number>();
  @Output() remove = new EventEmitter<void>();
  
  onQuantityChange(quantity: number): void {
    if (quantity !== this.item.quantity.value) {
      this.quantityChange.emit(quantity);
    }
  }
  
  onRemove(): void {
    this.remove.emit();
  }
}
```

---

## ⚡ Optimizaciones de Performance

### 🚀 **Lazy Loading Strategy**
```typescript
// Lazy loading de componentes pesados
const routes: Routes = [
  {
    path: 'carrito',
    loadChildren: () => import('./cart/cart.module').then(m => m.CartModule)
  },
  {
    path: 'checkout',
    loadChildren: () => import('./checkout/checkout.module').then(m => m.CheckoutModule)
  }
];
```

### 💨 **Virtual Scrolling para Carritos Grandes**
```typescript
@Component({
  selector: 'app-cart-items-virtual-list',
  template: `
    <cdk-virtual-scroll-viewport 
      itemSize="120" 
      class="cart-items-viewport">
      
      <app-cart-item
        *cdkVirtualFor="let item of items; trackBy: trackByFn"
        [item]="item"
        [disabled]="disabled"
        (quantityChange)="onQuantityChange(item.id, $event)"
        (remove)="onRemoveItem(item.id)">
      </app-cart-item>
      
    </cdk-virtual-scroll-viewport>
  `
})
export class CartItemsVirtualListComponent {
  @Input() items: CartItem[] = [];
  @Input() disabled = false;
  
  @Output() quantityChange = new EventEmitter<{itemId: string, quantity: number}>();
  @Output() removeItem = new EventEmitter<string>();
  
  trackByFn(index: number, item: CartItem): string {
    return item.id.value;
  }
  
  onQuantityChange(itemId: CartItemId, quantity: number): void {
    this.quantityChange.emit({ itemId: itemId.value, quantity });
  }
  
  onRemoveItem(itemId: CartItemId): void {
    this.removeItem.emit(itemId.value);
  }
}
```

### 🔄 **Optimistic Updates**
```typescript
@Injectable({ providedIn: 'root' })
export class OptimisticCartService {
  
  constructor(
    private cartState: CartStateService,
    private cartFacade: CartFacade
  ) {}
  
  optimisticAddItem(product: Product, quantity: number): void {
    const currentCart = this.cartState.getCurrentState().cart;
    if (!currentCart) return;
    
    // Aplicar cambio optimista localmente
    const optimisticCart = this.applyOptimisticAdd(currentCart, product, quantity);
    this.cartState.setCart(optimisticCart);
    
    // Ejecutar operación real en background
    this.cartFacade.addItem(product, quantity).catch(error => {
      // Revertir cambio optimista en caso de error
      this.cartState.setCart(currentCart);
      this.cartState.setError(error);
    });
  }
  
  private applyOptimisticAdd(cart: Cart, product: Product, quantity: number): Cart {
    const quantityObj = Quantity.create(quantity);
    if (quantityObj.isFailure()) return cart;
    
    const result = cart.addItem(product, quantityObj.value);
    return result.isSuccess() ? result.value : cart;
  }
}
```

---

**Próximo:** [Checklist Implementación](./05-CHECKLIST-IMPLEMENTACION.md)
