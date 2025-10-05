# 🏗️ Development Patterns - Patrones de Desarrollo

## 📋 Descripción

Patrones y mejores prácticas para el desarrollo consistente del sistema E-Commerce.

## 🎯 Arquitectura de Componentes

### 📦 **Patrón Container/Presentational**

```typescript
// CONTAINER COMPONENT (Smart Component)
@Component({
  selector: 'app-product-list-container',
  template: `
    <app-product-list
      [products]="products$ | async"
      [loading]="loading$ | async"
      [error]="error$ | async"
      (productSelect)="onProductSelect($event)"
      (loadMore)="onLoadMore()">
    </app-product-list>
  `
})
export class ProductListContainerComponent {
  products$ = this.store.select(selectProducts);
  loading$ = this.store.select(selectLoading);
  error$ = this.store.select(selectError);

  constructor(private store: Store) {}

  onProductSelect(product: Product): void {
    this.router.navigate(['/products', product.id]);
  }

  onLoadMore(): void {
    this.store.dispatch(loadMoreProducts());
  }
}

// PRESENTATIONAL COMPONENT (Dumb Component)
@Component({
  selector: 'app-product-list',
  template: `
    <div class="product-grid">
      <app-product-card
        *ngFor="let product of products"
        [product]="product"
        (click)="productSelect.emit(product)">
      </app-product-card>
    </div>
    
    <app-loading-spinner *ngIf="loading"></app-loading-spinner>
    <app-error-message *ngIf="error" [message]="error"></app-error-message>
    
    <button 
      *ngIf="!loading && !error"
      (click)="loadMore.emit()"
      class="load-more-btn">
      Cargar más
    </button>
  `
})
export class ProductListComponent {
  @Input() products: Product[] = [];
  @Input() loading = false;
  @Input() error: string | null = null;
  @Output() productSelect = new EventEmitter<Product>();
  @Output() loadMore = new EventEmitter<void>();
}
```

### 🔄 **Patrón Service/Repository**

```typescript
// DOMAIN INTERFACE
export interface ProductRepository {
  getAll(params?: ProductSearchParams): Observable<PaginatedResponse<Product>>;
  getById(id: string): Observable<Product>;
  create(product: CreateProductDto): Observable<Product>;
  update(id: string, product: UpdateProductDto): Observable<Product>;
  delete(id: string): Observable<void>;
}

// HTTP IMPLEMENTATION
@Injectable()
export class HttpProductRepository implements ProductRepository {
  constructor(private http: HttpClient) {}

  getAll(params?: ProductSearchParams): Observable<PaginatedResponse<Product>> {
    const queryParams = this.buildQueryParams(params);
    return this.http.get<PaginatedResponse<Product>>(
      `${this.apiUrl}/products`,
      { params: queryParams }
    ).pipe(
      catchError(this.handleError)
    );
  }

  private buildQueryParams(params?: ProductSearchParams): HttpParams {
    let queryParams = new HttpParams();
    if (params?.page) queryParams = queryParams.set('page', params.page.toString());
    if (params?.limit) queryParams = queryParams.set('limit', params.limit.toString());
    if (params?.search) queryParams = queryParams.set('search', params.search);
    return queryParams;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    // Error handling logic
    return throwError(() => error);
  }
}

// SERVICE LAYER
@Injectable()
export class ProductService {
  constructor(private repository: ProductRepository) {}

  getProducts(params?: ProductSearchParams): Observable<PaginatedResponse<Product>> {
    return this.repository.getAll(params).pipe(
      map(response => ({
        ...response,
        data: response.data.map(product => this.enrichProduct(product))
      }))
    );
  }

  private enrichProduct(product: Product): Product {
    return {
      ...product,
      displayPrice: this.formatPrice(product.price),
      isOnSale: this.checkIfOnSale(product)
    };
  }
}
```

## 🏪 **State Management Patterns**

### 📊 **NgRx Feature Store**

```typescript
// STATE INTERFACE
export interface ProductState {
  products: Product[];
  selectedProduct: Product | null;
  loading: boolean;
  error: string | null;
  pagination: {
    currentPage: number;
    totalPages: number;
    totalItems: number;
  };
}

// ACTIONS
export const ProductActions = createActionGroup({
  source: 'Product',
  events: {
    'Load Products': props<{ params?: ProductSearchParams }>(),
    'Load Products Success': props<{ response: PaginatedResponse<Product> }>(),
    'Load Products Failure': props<{ error: string }>(),
    
    'Select Product': props<{ productId: string }>(),
    'Clear Selected Product': emptyProps(),
    
    'Add Product': props<{ product: CreateProductDto }>(),
    'Add Product Success': props<{ product: Product }>(),
    'Add Product Failure': props<{ error: string }>()
  }
});

// REDUCERS
export const productReducer = createReducer(
  initialState,
  on(ProductActions.loadProducts, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(ProductActions.loadProductsSuccess, (state, { response }) => ({
    ...state,
    products: response.data,
    pagination: {
      currentPage: response.currentPage,
      totalPages: response.totalPages,
      totalItems: response.totalItems
    },
    loading: false,
    error: null
  })),
  on(ProductActions.loadProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);

// EFFECTS
@Injectable()
export class ProductEffects {
  loadProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.loadProducts),
      switchMap(({ params }) =>
        this.productService.getProducts(params).pipe(
          map(response => ProductActions.loadProductsSuccess({ response })),
          catchError(error => 
            of(ProductActions.loadProductsFailure({ 
              error: error.message || 'Error loading products' 
            }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private productService: ProductService
  ) {}
}

// SELECTORS
export const selectProductState = createFeatureSelector<ProductState>('product');

export const selectProducts = createSelector(
  selectProductState,
  (state) => state.products
);

export const selectProductsLoading = createSelector(
  selectProductState,
  (state) => state.loading
);

export const selectSelectedProduct = createSelector(
  selectProductState,
  (state) => state.selectedProduct
);
```

## 🎨 **UI Component Patterns**

### 🧩 **Compound Components**

```typescript
// COMPOUND COMPONENT PATTERN
@Component({
  selector: 'app-card',
  template: `
    <div class="card" [ngClass]="variant">
      <ng-content></ng-content>
    </div>
  `
})
export class CardComponent {
  @Input() variant: 'default' | 'elevated' | 'outlined' = 'default';
}

@Component({
  selector: 'app-card-header',
  template: `
    <div class="card-header">
      <ng-content></ng-content>
    </div>
  `
})
export class CardHeaderComponent {}

@Component({
  selector: 'app-card-content',
  template: `
    <div class="card-content">
      <ng-content></ng-content>
    </div>
  `
})
export class CardContentComponent {}

@Component({
  selector: 'app-card-actions',
  template: `
    <div class="card-actions">
      <ng-content></ng-content>
    </div>
  `
})
export class CardActionsComponent {}

// USAGE
@Component({
  template: `
    <app-card variant="elevated">
      <app-card-header>
        <h3>{{ product.name }}</h3>
      </app-card-header>
      <app-card-content>
        <img [src]="product.imageUrl" [alt]="product.name">
        <p>{{ product.description }}</p>
        <span class="price">{{ product.displayPrice }}</span>
      </app-card-content>
      <app-card-actions>
        <app-button (click)="addToCart()">Agregar al carrito</app-button>
        <app-button variant="outline" (click)="viewDetails()">Ver detalles</app-button>
      </app-card-actions>
    </app-card>
  `
})
export class ProductCardComponent {}
```

### 🔄 **Custom Control Value Accessor**

```typescript
@Component({
  selector: 'app-rating',
  template: `
    <div class="rating">
      <button
        *ngFor="let star of stars; index as i"
        type="button"
        class="star"
        [class.filled]="i < value"
        (click)="setValue(i + 1)"
        (mouseover)="highlightStars(i + 1)"
        (mouseleave)="highlightStars(value)"
        [disabled]="disabled">
        ⭐
      </button>
    </div>
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RatingComponent),
      multi: true
    }
  ]
})
export class RatingComponent implements ControlValueAccessor, OnInit {
  @Input() max = 5;
  @Input() disabled = false;
  
  stars: number[] = [];
  value = 0;
  highlighted = 0;

  private onChange = (value: number) => {};
  private onTouched = () => {};

  ngOnInit(): void {
    this.stars = Array(this.max).fill(0).map((_, i) => i);
  }

  setValue(value: number): void {
    if (this.disabled) return;
    
    this.value = value;
    this.onChange(value);
    this.onTouched();
  }

  highlightStars(count: number): void {
    this.highlighted = count;
  }

  // ControlValueAccessor implementation
  writeValue(value: number): void {
    this.value = value || 0;
  }

  registerOnChange(fn: (value: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
```

## 🛡️ **Error Handling Patterns**

### 🚨 **Global Error Handler**

```typescript
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private notification: NotificationService,
    private logger: LoggerService
  ) {}

  handleError(error: Error): void {
    // Log error
    this.logger.error('Global Error:', error);

    // Show user-friendly message
    if (error instanceof HttpErrorResponse) {
      this.handleHttpError(error);
    } else {
      this.notification.error('Ha ocurrido un error inesperado');
    }
  }

  private handleHttpError(error: HttpErrorResponse): void {
    switch (error.status) {
      case 400:
        this.notification.error('Datos inválidos');
        break;
      case 401:
        this.notification.error('Sesión expirada');
        // Redirect to login
        break;
      case 403:
        this.notification.error('No tienes permisos');
        break;
      case 404:
        this.notification.error('Recurso no encontrado');
        break;
      case 500:
        this.notification.error('Error del servidor');
        break;
      default:
        this.notification.error('Error de conexión');
    }
  }
}

// HTTP ERROR INTERCEPTOR
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private errorHandler: GlobalErrorHandler) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      retry(2), // Retry failed requests
      catchError((error: HttpErrorResponse) => {
        this.errorHandler.handleError(error);
        return throwError(() => error);
      })
    );
  }
}
```

## 🔐 **Security Patterns**

### 🛡️ **Authentication Guard**

```typescript
@Injectable()
export class AuthGuard implements CanActivate {
  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | boolean {
    
    return this.auth.isAuthenticated$.pipe(
      map(isAuthenticated => {
        if (isAuthenticated) {
          return true;
        }
        
        // Store intended URL
        this.auth.redirectUrl = state.url;
        
        // Redirect to login
        this.router.navigate(['/auth/login']);
        return false;
      }),
      catchError(() => {
        this.router.navigate(['/auth/login']);
        return of(false);
      })
    );
  }
}

// ROLE-BASED GUARD
@Injectable()
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    const requiredRoles = route.data['roles'] as string[];
    
    return this.auth.user$.pipe(
      map(user => {
        if (!user || !requiredRoles) return false;
        
        return requiredRoles.some(role => user.roles.includes(role));
      })
    );
  }
}
```

## 📱 **Responsive Patterns**

### 📐 **Breakpoint Service**

```typescript
@Injectable()
export class BreakpointService {
  private breakpointObserver = inject(BreakpointObserver);

  readonly isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset);
  readonly isTablet$ = this.breakpointObserver.observe(Breakpoints.Tablet);
  readonly isDesktop$ = this.breakpointObserver.observe([
    Breakpoints.Desktop,
    Breakpoints.Large,
    Breakpoints.XLarge
  ]);

  readonly currentBreakpoint$ = combineLatest([
    this.isHandset$,
    this.isTablet$,
    this.isDesktop$
  ]).pipe(
    map(([handset, tablet, desktop]) => {
      if (handset.matches) return 'handset';
      if (tablet.matches) return 'tablet';
      if (desktop.matches) return 'desktop';
      return 'unknown';
    })
  );
}

// USAGE IN COMPONENT
@Component({
  template: `
    <div [ngSwitch]="breakpoint$ | async">
      <!-- Mobile Layout -->
      <div *ngSwitchCase="'handset'">
        <app-mobile-product-grid [products]="products"></app-mobile-product-grid>
      </div>
      
      <!-- Tablet Layout -->
      <div *ngSwitchCase="'tablet'">
        <app-tablet-product-grid [products]="products"></app-tablet-product-grid>
      </div>
      
      <!-- Desktop Layout -->
      <div *ngSwitchDefault>
        <app-desktop-product-grid [products]="products"></app-desktop-product-grid>
      </div>
    </div>
  `
})
export class ProductGridComponent {
  breakpoint$ = this.breakpointService.currentBreakpoint$;
  
  constructor(private breakpointService: BreakpointService) {}
}
```

## 🧪 **Testing Patterns**

### 🎯 **Component Testing**

```typescript
describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductListComponent],
      imports: [SharedModule],
      providers: [
        { provide: ProductService, useValue: mockProductService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
  });

  it('should display products', () => {
    // Arrange
    const mockProducts = [
      { id: '1', name: 'Product 1', price: 100 },
      { id: '2', name: 'Product 2', price: 200 }
    ];
    component.products = mockProducts;

    // Act
    fixture.detectChanges();

    // Assert
    const productElements = fixture.debugElement.queryAll(
      By.css('.product-card')
    );
    expect(productElements.length).toBe(2);
  });

  it('should emit productSelect when product is clicked', () => {
    // Arrange
    spyOn(component.productSelect, 'emit');
    const mockProduct = { id: '1', name: 'Product 1', price: 100 };
    component.products = [mockProduct];
    fixture.detectChanges();

    // Act
    const productElement = fixture.debugElement.query(By.css('.product-card'));
    productElement.triggerEventHandler('click', null);

    // Assert
    expect(component.productSelect.emit).toHaveBeenCalledWith(mockProduct);
  });
});
```

## 🎯 **Performance Patterns**

### ⚡ **OnPush Strategy**

```typescript
@Component({
  selector: 'app-product-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="product-card">
      <img [src]="product.imageUrl" [alt]="product.name">
      <h3>{{ product.name }}</h3>
      <p>{{ product.price | currency }}</p>
      <button (click)="addToCart()">Add to Cart</button>
    </div>
  `
})
export class ProductCardComponent {
  @Input() product!: Product;
  @Output() addToCartClick = new EventEmitter<Product>();

  constructor(private cdr: ChangeDetectorRef) {}

  addToCart(): void {
    this.addToCartClick.emit(this.product);
  }

  // Manually trigger change detection when needed
  forceUpdate(): void {
    this.cdr.markForCheck();
  }
}
```

Estos patrones aseguran código consistente, mantenible y escalable. 🚀
