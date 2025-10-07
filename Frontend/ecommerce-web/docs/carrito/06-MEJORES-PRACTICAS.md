# 🏆 Mejores Prácticas: Carrito de Compras

> **Estado:** 🔄 **PENDIENTE** - Guía de mejores prácticas a seguir

## 🧠 Principios de Clean Architecture

### 🎯 **Separación de Responsabilidades**

#### ✅ **DO - Correcto**
```typescript
// ✅ Dominio puro - Sin dependencias externas
export class Cart {
  addItem(product: Product, quantity: Quantity): Result<Cart, CartError> {
    // Solo lógica de negocio
    const validation = this.validateAddition(product, quantity);
    if (validation.isFailure()) return validation;
    
    // Inmutabilidad
    return Result.ok(new Cart(
      this.id,
      this.userId,
      [...this._items, newItem],
      this._status,
      this._createdAt,
      new Date(),
      this.metadata
    ));
  }
}

// ✅ Caso de uso - Orquestación sin estado
@Injectable({ providedIn: 'root' })
export class AddItemToCartUseCase {
  async execute(command: AddItemToCartCommand): Promise<Result<Cart, CartError>> {
    // 1. Obtener datos
    const cart = await this.cartRepository.findByUserId(command.userId);
    const product = await this.productRepository.findById(command.productId);
    
    // 2. Aplicar lógica de dominio
    const result = cart.addItem(product, Quantity.create(command.quantity));
    
    // 3. Persistir y notificar
    if (result.isSuccess()) {
      await this.cartRepository.save(result.value);
      await this.eventBus.publish(new ItemAddedEvent(...));
    }
    
    return result;
  }
}
```

#### ❌ **DON'T - Incorrecto**
```typescript
// ❌ Dominio acoplado a infraestructura
export class Cart {
  constructor(private http: HttpClient) {} // ❌ Dependencia externa
  
  async addItem(product: Product): Promise<void> { // ❌ Side effects
    this._items.push(product); // ❌ Mutabilidad directa
    await this.http.post('/api/cart', this._items); // ❌ HTTP en dominio
  }
}

// ❌ Caso de uso con estado
@Injectable({ providedIn: 'root' })
export class AddItemToCartUseCase {
  private currentCart: Cart; // ❌ Estado en caso de uso
  
  execute(productId: number): void { // ❌ Sin validación de types
    this.currentCart.addItem(productId); // ❌ Sin manejo de errores
  }
}
```

---

## 🛡️ Gestión de Errores

### 🎯 **Result Pattern**

#### ✅ **Implementación Robusta**
```typescript
// ✅ Result Type para manejo de errores funcional
export class Result<T, E = Error> {
  private constructor(
    private _isSuccess: boolean,
    private _value?: T,
    private _error?: E
  ) {}
  
  static ok<T, E>(value: T): Result<T, E> {
    return new Result<T, E>(true, value, undefined);
  }
  
  static fail<T, E>(error: E): Result<T, E> {
    return new Result<T, E>(false, undefined, error);
  }
  
  isSuccess(): boolean { return this._isSuccess; }
  isFailure(): boolean { return !this._isSuccess; }
  
  get value(): T {
    if (!this._isSuccess) throw new Error('Cannot get value from failed result');
    return this._value!;
  }
  
  get error(): E {
    if (this._isSuccess) throw new Error('Cannot get error from successful result');
    return this._error!;
  }
  
  // Functional programming helpers
  map<U>(fn: (value: T) => U): Result<U, E> {
    return this._isSuccess 
      ? Result.ok(fn(this._value!))
      : Result.fail(this._error!);
  }
  
  flatMap<U>(fn: (value: T) => Result<U, E>): Result<U, E> {
    return this._isSuccess 
      ? fn(this._value!)
      : Result.fail(this._error!);
  }
}

// ✅ Errores tipados del dominio
export class CartError extends Error {
  constructor(
    public readonly code: CartErrorCode,
    message: string,
    public readonly cause?: Error
  ) {
    super(message);
    this.name = 'CartError';
  }
}

export type CartErrorCode = 
  | 'PRODUCT_NOT_FOUND'
  | 'INSUFFICIENT_STOCK' 
  | 'CART_LIMIT_EXCEEDED'
  | 'INVALID_QUANTITY'
  | 'PRODUCT_UNAVAILABLE'
  | 'OPERATION_FAILED';
```

#### ✅ **Uso en Componentes**
```typescript
@Component({...})
export class CartPageContainer {
  
  async onAddItem(product: Product, quantity: number): Promise<void> {
    this.setLoading(true);
    
    try {
      const result = await this.cartFacade.addItem(product, quantity);
      
      // ✅ Pattern matching con Result
      if (result.isSuccess()) {
        this.notificationService.success('Producto agregado al carrito');
      } else {
        this.handleCartError(result.error);
      }
    } catch (error) {
      this.handleUnexpectedError(error);
    } finally {
      this.setLoading(false);
    }
  }
  
  private handleCartError(error: CartError): void {
    const messages: Record<CartErrorCode, string> = {
      'PRODUCT_NOT_FOUND': 'Producto no encontrado',
      'INSUFFICIENT_STOCK': 'Stock insuficiente',
      'CART_LIMIT_EXCEEDED': 'Límite del carrito excedido',
      'INVALID_QUANTITY': 'Cantidad inválida',
      'PRODUCT_UNAVAILABLE': 'Producto no disponible',
      'OPERATION_FAILED': 'Error en la operación'
    };
    
    this.notificationService.error(messages[error.code] || error.message);
  }
}
```

---

## ⚡ Performance & Optimización

### 🚀 **OnPush Strategy & Signals**

#### ✅ **Componentes Optimizados**
```typescript
@Component({
  selector: 'app-cart-item',
  changeDetection: ChangeDetectionStrategy.OnPush, // ✅ OnPush
  template: `
    <div class="cart-item">
      <!-- ✅ Computed properties para derivations -->
      <div class="subtotal">{{ subtotal() | currency }}</div>
      <div class="available">{{ isAvailable() ? 'Disponible' : 'Agotado' }}</div>
      
      <!-- ✅ Signals para estado reactive -->
      <app-quantity-selector
        [value]="item().quantity.value"
        [disabled]="loading()"
        (change)="onQuantityChange($event)">
      </app-quantity-selector>
    </div>
  `
})
export class CartItemComponent {
  // ✅ Input signals para mejor performance
  item = input.required<CartItem>();
  loading = input<boolean>(false);
  
  // ✅ Computed properties para derivaciones
  subtotal = computed(() => this.item().subtotal);
  isAvailable = computed(() => this.item().isAvailable);
  
  // ✅ Output signals para eventos
  quantityChange = output<number>();
  remove = output<void>();
  
  onQuantityChange(quantity: number): void {
    if (quantity !== this.item().quantity.value) {
      this.quantityChange.emit(quantity);
    }
  }
}
```

### 🎯 **Optimistic Updates**
```typescript
@Injectable({ providedIn: 'root' })
export class OptimisticCartService {
  
  private optimisticOperations = new Map<string, CartOperation>();
  
  async optimisticAddItem(product: Product, quantity: number): Promise<void> {
    const operationId = this.generateOperationId();
    
    try {
      // 1. ✅ Aplicar cambio optimista inmediatamente
      const currentCart = this.cartState.currentCart();
      const optimisticCart = this.applyOptimisticAdd(currentCart, product, quantity);
      
      this.cartState.setCart(optimisticCart);
      this.optimisticOperations.set(operationId, { type: 'add', product, quantity });
      
      // 2. ✅ Ejecutar operación real en paralelo
      const realResult = await this.cartRepository.addItem(currentCart.id, product, quantity);
      
      // 3. ✅ Reconciliar con resultado real
      this.reconcileOptimisticOperation(operationId, realResult);
      
    } catch (error) {
      // 4. ✅ Revertir en caso de error
      this.revertOptimisticOperation(operationId);
      throw error;
    }
  }
  
  private reconcileOptimisticOperation(operationId: string, realCart: Cart): void {
    // Comparar estado optimista vs real y ajustar si es necesario
    this.cartState.setCart(realCart);
    this.optimisticOperations.delete(operationId);
  }
  
  private revertOptimisticOperation(operationId: string): void {
    const operation = this.optimisticOperations.get(operationId);
    if (operation) {
      // Revertir al estado anterior
      const previousCart = this.cartState.previousCart();
      this.cartState.setCart(previousCart);
      this.optimisticOperations.delete(operationId);
    }
  }
}
```

### 💨 **Virtual Scrolling & Lazy Loading**
```typescript
@Component({
  template: `
    <!-- ✅ Virtual scrolling para carritos grandes -->
    <cdk-virtual-scroll-viewport 
      itemSize="120" 
      class="cart-items-viewport"
      *ngIf="items().length > 10; else normalList">
      
      <app-cart-item
        *cdkVirtualFor="let item of items(); trackBy: trackByItemId"
        [item]="item"
        [loading]="isUpdating(item.id)"
        (quantityChange)="onQuantityChange(item.id, $event)"
        (remove)="onRemoveItem(item.id)">
      </app-cart-item>
    </cdk-virtual-scroll-viewport>
    
    <!-- ✅ Lista normal para carritos pequeños -->
    <ng-template #normalList>
      <app-cart-item
        *ngFor="let item of items(); trackBy: trackByItemId"
        [item]="item"
        [loading]="isUpdating(item.id)"
        (quantityChange)="onQuantityChange(item.id, $event)"
        (remove)="onRemoveItem(item.id)">
      </app-cart-item>
    </ng-template>
  `
})
export class CartItemsListComponent {
  items = input.required<CartItem[]>();
  
  // ✅ TrackBy function para performance de ngFor
  trackByItemId = (index: number, item: CartItem): string => item.id.value;
  
  // ✅ Signal para operaciones pendientes
  private pendingOperations = signal<Set<string>>(new Set());
  
  isUpdating = computed(() => (itemId: string) => 
    this.pendingOperations().has(itemId)
  );
}
```

---

## 🔒 Seguridad & Validación

### 🛡️ **Input Validation**

#### ✅ **Validación en Capas**
```typescript
// ✅ 1. Validación de dominio (siempre)
export class Quantity {
  private constructor(private _value: number) {}
  
  static create(value: number): Result<Quantity, CartError> {
    // Validaciones de negocio
    if (!Number.isInteger(value)) {
      return Result.fail(new CartError('INVALID_QUANTITY', 'La cantidad debe ser un número entero'));
    }
    
    if (value < 1) {
      return Result.fail(new CartError('INVALID_QUANTITY', 'La cantidad debe ser mayor a 0'));
    }
    
    if (value > 999) {
      return Result.fail(new CartError('INVALID_QUANTITY', 'La cantidad no puede exceder 999'));
    }
    
    return Result.ok(new Quantity(value));
  }
  
  get value(): number { return this._value; }
  
  add(other: Quantity): Quantity {
    return Quantity.create(this._value + other._value).value;
  }
  
  isZero(): boolean { return this._value === 0; }
  isNegative(): boolean { return this._value < 0; }
}

// ✅ 2. Validación en casos de uso
@Injectable({ providedIn: 'root' })
export class AddItemToCartUseCase {
  async execute(command: AddItemToCartCommand): Promise<Result<Cart, CartError>> {
    // Validar comando
    const validation = this.validateCommand(command);
    if (validation.isFailure()) return validation;
    
    // Validar business rules
    const businessValidation = await this.validateBusinessRules(command);
    if (businessValidation.isFailure()) return businessValidation;
    
    // Procesar...
  }
  
  private validateCommand(command: AddItemToCartCommand): Result<void, CartError> {
    if (!command.userId) {
      return Result.fail(new CartError('INVALID_COMMAND', 'Usuario requerido'));
    }
    
    if (!command.productId) {
      return Result.fail(new CartError('INVALID_COMMAND', 'Producto requerido'));
    }
    
    return Quantity.create(command.quantity).map(() => void 0);
  }
}

// ✅ 3. Validación en UI
@Component({
  template: `
    <form [formGroup]="quantityForm" (ngSubmit)="onSubmit()">
      <input 
        type="number"
        formControlName="quantity"
        [min]="1"
        [max]="maxQuantity"
        class="quantity-input"
        [class.error]="quantityControl.invalid && quantityControl.touched">
      
      <div class="error-message" *ngIf="quantityControl.invalid && quantityControl.touched">
        {{ getQuantityErrorMessage() }}
      </div>
      
      <button 
        type="submit" 
        [disabled]="quantityForm.invalid || loading()">
        Agregar al Carrito
      </button>
    </form>
  `
})
export class AddToCartComponent {
  quantityForm = this.fb.group({
    quantity: [1, [
      Validators.required,
      Validators.min(1),
      Validators.max(this.maxQuantity),
      Validators.pattern(/^\d+$/) // Solo enteros
    ]]
  });
  
  get quantityControl() { return this.quantityForm.get('quantity')!; }
  
  getQuantityErrorMessage(): string {
    const control = this.quantityControl;
    if (control.hasError('required')) return 'Cantidad requerida';
    if (control.hasError('min')) return 'Cantidad mínima: 1';
    if (control.hasError('max')) return `Cantidad máxima: ${this.maxQuantity}`;
    if (control.hasError('pattern')) return 'Solo números enteros';
    return '';
  }
}
```

### 🔐 **Sanitización de Datos**
```typescript
@Injectable({ providedIn: 'root' })
export class CartSanitizerService {
  
  sanitizeAddItemRequest(request: any): AddItemToCartCommand {
    return {
      userId: this.sanitizeUserId(request.userId),
      productId: this.sanitizeProductId(request.productId),
      quantity: this.sanitizeQuantity(request.quantity),
      options: this.sanitizeOptions(request.options)
    };
  }
  
  private sanitizeUserId(userId: any): UserId {
    if (typeof userId !== 'number' || userId <= 0) {
      throw new Error('Invalid user ID');
    }
    return UserId.create(userId);
  }
  
  private sanitizeProductId(productId: any): ProductId {
    if (typeof productId !== 'number' || productId <= 0) {
      throw new Error('Invalid product ID');
    }
    return ProductId.create(productId);
  }
  
  private sanitizeQuantity(quantity: any): number {
    const num = Number(quantity);
    if (!Number.isInteger(num) || num < 1 || num > 999) {
      throw new Error('Invalid quantity');
    }
    return num;
  }
}
```

---

## 🧪 Testing Estratégico

### 🎯 **Testing Pyramid**

#### ✅ **Unit Tests (70%)**
```typescript
// ✅ Tests de entidades de dominio
describe('Cart Entity', () => {
  let cart: Cart;
  let product: Product;
  
  beforeEach(() => {
    cart = Cart.createEmpty(UserId.create(1));
    product = ProductTestBuilder
      .aProduct()
      .withId(ProductId.create(1))
      .withPrice(Money.create(100, 'USD'))
      .withStock(10)
      .build();
  });
  
  describe('addItem', () => {
    it('should add item successfully when valid', () => {
      // Arrange
      const quantity = Quantity.create(2).value;
      
      // Act
      const result = cart.addItem(product, quantity);
      
      // Assert
      expect(result.isSuccess()).toBe(true);
      expect(result.value.itemCount).toBe(2);
      expect(result.value.items).toHaveLength(1);
    });
    
    it('should fail when product is unavailable', () => {
      // Arrange
      const unavailableProduct = ProductTestBuilder
        .aProduct()
        .unavailable()
        .build();
      
      // Act
      const result = cart.addItem(unavailableProduct, Quantity.create(1).value);
      
      // Assert
      expect(result.isFailure()).toBe(true);
      expect(result.error.code).toBe('PRODUCT_UNAVAILABLE');
    });
    
    it('should merge quantities when adding existing item', () => {
      // Arrange
      cart = cart.addItem(product, Quantity.create(1).value).value;
      
      // Act
      const result = cart.addItem(product, Quantity.create(2).value);
      
      // Assert
      expect(result.isSuccess()).toBe(true);
      expect(result.value.items).toHaveLength(1);
      expect(result.value.items[0].quantity.value).toBe(3);
    });
  });
});

// ✅ Tests de casos de uso
describe('AddItemToCartUseCase', () => {
  let useCase: AddItemToCartUseCase;
  let cartRepository: jest.Mocked<CartRepository>;
  let productRepository: jest.Mocked<ProductRepository>;
  
  beforeEach(() => {
    cartRepository = createMockCartRepository();
    productRepository = createMockProductRepository();
    useCase = new AddItemToCartUseCase(cartRepository, productRepository);
  });
  
  it('should add item successfully', async () => {
    // Arrange
    const command = { userId: UserId.create(1), productId: ProductId.create(1), quantity: 2 };
    const cart = Cart.createEmpty(command.userId);
    const product = ProductTestBuilder.aProduct().build();
    
    cartRepository.findByUserId.mockResolvedValue(cart);
    productRepository.findById.mockResolvedValue(product);
    cartRepository.save.mockResolvedValue(cart);
    
    // Act
    const result = await useCase.execute(command);
    
    // Assert
    expect(result.isSuccess()).toBe(true);
    expect(cartRepository.save).toHaveBeenCalledTimes(1);
  });
});
```

#### ✅ **Integration Tests (20%)**
```typescript
// ✅ Tests de integración de repositorios
describe('HttpCartRepository Integration', () => {
  let repository: HttpCartRepository;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HttpCartRepository]
    });
    
    repository = TestBed.inject(HttpCartRepository);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  it('should retrieve cart from API', async () => {
    // Arrange
    const userId = UserId.create(1);
    const mockResponse = { data: createMockCarritoDto() };
    
    // Act
    const cartPromise = repository.findByUserId(userId);
    
    // Assert
    const req = httpMock.expectOne('/api/segura/carrito');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
    
    const cart = await cartPromise;
    expect(cart).toBeInstanceOf(Cart);
    expect(cart.userId).toEqual(userId);
  });
  
  it('should handle 404 by returning empty cart', async () => {
    // Arrange
    const userId = UserId.create(1);
    
    // Act
    const cartPromise = repository.findByUserId(userId);
    
    // Assert
    const req = httpMock.expectOne('/api/segura/carrito');
    req.flush({ message: 'Not found' }, { status: 404, statusText: 'Not Found' });
    
    const cart = await cartPromise;
    expect(cart.isEmpty).toBe(true);
  });
});
```

#### ✅ **E2E Tests (10%)**
```typescript
// ✅ Tests E2E críticos
describe('Cart Workflow E2E', () => {
  it('should complete add to cart flow', () => {
    // Arrange
    cy.login('test@example.com', 'password');
    cy.visit('/productos');
    
    // Act & Assert
    cy.get('[data-testid="product-1"]').within(() => {
      cy.get('[data-testid="add-to-cart"]').click();
    });
    
    cy.get('[data-testid="cart-notification"]')
      .should('contain', 'Producto agregado al carrito');
    
    cy.get('[data-testid="cart-flyout-trigger"]').click();
    cy.get('[data-testid="cart-flyout"]').should('be.visible');
    cy.get('[data-testid="cart-item-count"]').should('contain', '1');
    
    cy.get('[data-testid="view-cart"]').click();
    cy.url().should('include', '/carrito');
    
    cy.get('[data-testid="cart-item"]').should('have.length', 1);
    cy.get('[data-testid="cart-total"]').should('not.be.empty');
  });
});
```

---

## 📱 UX/UI Best Practices

### 🎨 **Design System Integration**
```typescript
// ✅ Componentes reutilizables del design system
@Component({
  selector: 'app-cart-item',
  template: `
    <div class="cart-item" [class.cart-item--unavailable]="!item().isAvailable">
      <!-- ✅ Usar componentes del design system -->
      <ui-card class="cart-item__card">
        <ui-card-header>
          <ui-product-image 
            [src]="item().product.image"
            [alt]="item().product.name"
            size="sm">
          </ui-product-image>
          
          <div class="cart-item__details">
            <ui-text variant="subtitle1">{{ item().product.name }}</ui-text>
            <ui-text variant="body2" color="muted">{{ item().product.description }}</ui-text>
          </div>
        </ui-card-header>
        
        <ui-card-content>
          <div class="cart-item__quantity">
            <ui-quantity-selector
              [value]="item().quantity.value"
              [min]="1"
              [max]="item().product.maxOrderQuantity"
              [disabled]="loading()"
              (valueChange)="onQuantityChange($event)">
            </ui-quantity-selector>
          </div>
          
          <div class="cart-item__pricing">
            <ui-price 
              [amount]="item().unitPrice"
              variant="body2">
            </ui-price>
            <ui-price 
              [amount]="item().subtotal"
              variant="subtitle1"
              emphasized>
            </ui-price>
          </div>
        </ui-card-content>
        
        <ui-card-actions>
          <ui-button
            variant="text"
            color="danger"
            [disabled]="loading()"
            (click)="onRemove()">
            <ui-icon name="trash"></ui-icon>
            Eliminar
          </ui-button>
        </ui-card-actions>
      </ui-card>
    </div>
  `,
  styleUrls: ['./cart-item.component.scss']
})
export class CartItemComponent {
  // Implementation...
}
```

### ♿ **Accessibility Standards**
```typescript
@Component({
  template: `
    <div 
      class="cart-flyout"
      role="dialog"
      aria-labelledby="cart-title"
      aria-describedby="cart-description"
      [attr.aria-expanded]="isOpen()"
      [attr.aria-hidden]="!isOpen()">
      
      <header class="cart-flyout__header">
        <h2 id="cart-title" class="cart-flyout__title">
          Carrito de Compras
        </h2>
        <p id="cart-description" class="sr-only">
          {{ itemCount() }} productos en tu carrito por un total de {{ total() | currency }}
        </p>
        
        <button 
          type="button"
          class="cart-flyout__close"
          aria-label="Cerrar carrito"
          (click)="onClose()">
          <ui-icon name="close" aria-hidden="true"></ui-icon>
        </button>
      </header>
      
      <div class="cart-flyout__content">
        <ul 
          class="cart-items" 
          role="list"
          aria-label="Productos en el carrito">
          
          <li 
            *ngFor="let item of items(); trackBy: trackByItemId"
            role="listitem"
            class="cart-item">
            
            <app-cart-item
              [item]="item"
              [attr.aria-describedby]="'item-description-' + item.id.value">
            </app-cart-item>
            
            <!-- Descripción para screen readers -->
            <div 
              [id]="'item-description-' + item.id.value"
              class="sr-only">
              {{ item.product.name }}, 
              cantidad {{ item.quantity.value }}, 
              precio {{ item.subtotal | currency }}
            </div>
          </li>
        </ul>
      </div>
      
      <footer class="cart-flyout__footer">
        <div class="cart-total" aria-live="polite">
          <span class="cart-total__label">Total:</span>
          <span class="cart-total__amount">{{ total() | currency }}</span>
        </div>
        
        <div class="cart-actions">
          <button 
            type="button"
            class="btn btn-secondary"
            [routerLink]="['/carrito']"
            (click)="onClose()">
            Ver Carrito Completo
          </button>
          
          <button 
            type="button"
            class="btn btn-primary"
            [disabled]="itemCount() === 0"
            [routerLink]="['/checkout']"
            (click)="onClose()">
            Proceder al Checkout
          </button>
        </div>
      </footer>
    </div>
  `
})
export class CartFlyoutComponent {
  // ✅ Gestión de foco para accessibility
  @ViewChild('closeButton') closeButton!: ElementRef<HTMLButtonElement>;
  
  ngAfterViewInit(): void {
    if (this.isOpen()) {
      // Enfocar el botón de cerrar cuando se abre
      this.closeButton.nativeElement.focus();
    }
  }
  
  @HostListener('keydown.escape')
  onEscapeKey(): void {
    this.onClose();
  }
}
```

### 📱 **Mobile-First Design**
```scss
// ✅ SCSS con mobile-first approach
.cart-item {
  // Mobile base styles
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  border-bottom: 1px solid var(--color-border);
  
  &__image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: var(--border-radius-sm);
  }
  
  &__details {
    flex: 1;
    min-width: 0; // Para text truncation
  }
  
  &__quantity {
    align-self: flex-start;
  }
  
  &__pricing {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 0.25rem;
  }
  
  // ✅ Tablet breakpoint
  @media (min-width: 768px) {
    flex-direction: row;
    align-items: center;
    
    &__image {
      width: 100px;
      height: 100px;
    }
    
    &__details {
      flex: 2;
    }
    
    &__quantity {
      flex: 0 0 120px;
    }
    
    &__pricing {
      flex: 0 0 140px;
    }
  }
  
  // ✅ Desktop breakpoint
  @media (min-width: 1024px) {
    &__image {
      width: 120px;
      height: 120px;
    }
    
    &__details {
      flex: 3;
    }
    
    &__quantity {
      flex: 0 0 150px;
    }
    
    &__pricing {
      flex: 0 0 180px;
    }
  }
}

// ✅ States y interactions
.cart-item {
  &--loading {
    opacity: 0.6;
    pointer-events: none;
  }
  
  &--unavailable {
    opacity: 0.7;
    
    .cart-item__image {
      filter: grayscale(100%);
    }
  }
  
  &:hover {
    background-color: var(--color-surface-hover);
  }
  
  // ✅ Focus management
  &:focus-within {
    outline: 2px solid var(--color-primary);
    outline-offset: 2px;
  }
}

// ✅ Dark mode support
@media (prefers-color-scheme: dark) {
  .cart-item {
    border-bottom-color: var(--color-border-dark);
    
    &:hover {
      background-color: var(--color-surface-hover-dark);
    }
  }
}

// ✅ Reduced motion support
@media (prefers-reduced-motion: reduce) {
  .cart-item {
    transition: none;
  }
}

// ✅ High contrast support
@media (prefers-contrast: high) {
  .cart-item {
    border: 2px solid var(--color-border);
    
    &:focus-within {
      outline-width: 3px;
    }
  }
}
```

---

**Próximo:** [Diagramas](./07-DIAGRAMAS.md)
