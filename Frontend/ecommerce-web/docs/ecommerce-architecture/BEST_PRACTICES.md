# ⭐ Best Practices - Mejores Prácticas

## 📋 Descripción

Mejores prácticas y convenciones para el desarrollo del sistema E-Commerce.

## 🏗️ **Arquitectura y Estructura**

### 📁 **Organización de Archivos**

```
src/app/
├── core/                    # Singleton services, guards, interceptors
│   ├── services/
│   ├── guards/
│   ├── interceptors/
│   └── models/
├── shared/                  # Shared components, pipes, directives
│   ├── components/
│   ├── pipes/
│   ├── directives/
│   └── utils/
├── features/               # Feature modules
│   ├── auth/
│   ├── product/
│   └── cart/
└── ui-kit/                # Design system components
    ├── components/
    ├── tokens/
    └── themes/
```

### 🎯 **Naming Conventions**

```typescript
// ✅ CORRECTO - Descriptivo y consistente
export class ProductListComponent { }
export class ProductService { }
export interface ProductSearchParams { }
export enum ProductStatus { }

// ❌ INCORRECTO - Vago o inconsistente
export class List { }
export class ProdSvc { }
export interface Params { }
```

### 📦 **Module Structure**

```typescript
// ✅ CORRECTO - Feature Module Structure
@NgModule({
  declarations: [
    ProductListComponent,
    ProductCardComponent,
    ProductDetailComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ProductRoutingModule
  ],
  providers: [
    ProductService,
    ProductResolver
  ]
})
export class ProductModule { }
```

## 🔧 **Component Best Practices**

### 🎯 **Smart vs Dumb Components**

```typescript
// ✅ CORRECTO - Smart Component (Container)
@Component({
  selector: 'app-product-list-container',
  template: `
    <app-product-list
      [products]="products$ | async"
      [loading]="loading$ | async"
      (productClick)="onProductClick($event)">
    </app-product-list>
  `
})
export class ProductListContainerComponent {
  products$ = this.store.select(selectProducts);
  loading$ = this.store.select(selectLoading);

  constructor(private store: Store) {}

  onProductClick(product: Product): void {
    // Handle business logic
  }
}

// ✅ CORRECTO - Dumb Component (Presentational)
@Component({
  selector: 'app-product-list',
  template: `
    <div class="product-grid">
      <app-product-card
        *ngFor="let product of products; trackBy: trackByProductId"
        [product]="product"
        (click)="productClick.emit(product)">
      </app-product-card>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductListComponent {
  @Input() products: Product[] = [];
  @Input() loading = false;
  @Output() productClick = new EventEmitter<Product>();

  trackByProductId(index: number, product: Product): string {
    return product.id;
  }
}
```

### 🔄 **Lifecycle Management**

```typescript
// ✅ CORRECTO - Proper lifecycle management
@Component({
  template: `...`
})
export class ProductComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.productService.getProducts()
      .pipe(takeUntil(this.destroy$))
      .subscribe(products => {
        // Handle products
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// ❌ INCORRECTO - Memory leaks
@Component({
  template: `...`
})
export class ProductComponent implements OnInit {
  ngOnInit(): void {
    this.productService.getProducts()
      .subscribe(products => {
        // This will cause memory leaks!
      });
  }
}
```

### 🎨 **Template Best Practices**

```html
<!-- ✅ CORRECTO - Clear, readable templates -->
<div class="product-card" 
     [class.featured]="product.featured"
     *ngIf="product; else emptyState">
  
  <img [src]="product.imageUrl" 
       [alt]="product.name"
       loading="lazy">
  
  <h3>{{ product.name }}</h3>
  <p>{{ product.price | currency:'USD':'symbol':'1.2-2' }}</p>
  
  <button (click)="addToCart(product)"
          [disabled]="!product.inStock"
          class="btn btn--primary">
    {{ product.inStock ? 'Add to Cart' : 'Out of Stock' }}
  </button>
</div>

<ng-template #emptyState>
  <app-empty-state 
    icon="shopping-bag"
    title="No products found"
    description="Try adjusting your search filters">
  </app-empty-state>
</ng-template>

<!-- ❌ INCORRECTO - Complex logic in templates -->
<div *ngIf="products && products.length > 0 && !loading && !error">
  <div *ngFor="let product of products">
    {{ product.discountedPrice ? 
        (product.price - product.discountedPrice) : 
        product.price }}
  </div>
</div>
```

## 🔧 **Service Best Practices**

### 🏪 **Service Design**

```typescript
// ✅ CORRECTO - Clean service with proper error handling
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly apiUrl = '/api/products';

  constructor(
    private http: HttpClient,
    private errorHandler: ErrorHandlerService
  ) {}

  getProducts(params?: ProductSearchParams): Observable<PaginatedResponse<Product>> {
    const httpParams = this.buildHttpParams(params);
    
    return this.http.get<PaginatedResponse<Product>>(this.apiUrl, { params: httpParams })
      .pipe(
        map(response => this.transformResponse(response)),
        catchError(error => this.errorHandler.handleError(error)),
        shareReplay(1)
      );
  }

  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`)
      .pipe(
        map(product => this.enrichProduct(product)),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  private buildHttpParams(params?: ProductSearchParams): HttpParams {
    let httpParams = new HttpParams();
    
    if (params?.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params?.category) {
      httpParams = httpParams.set('category', params.category);
    }
    if (params?.page) {
      httpParams = httpParams.set('page', params.page.toString());
    }
    
    return httpParams;
  }

  private transformResponse(response: any): PaginatedResponse<Product> {
    return {
      data: response.data?.map((item: any) => this.mapToProduct(item)) || [],
      totalItems: response.totalItems || 0,
      currentPage: response.currentPage || 1,
      totalPages: response.totalPages || 1
    };
  }

  private enrichProduct(product: Product): Product {
    return {
      ...product,
      displayPrice: this.formatPrice(product.price),
      isOnSale: product.salePrice > 0,
      discountPercentage: this.calculateDiscount(product.price, product.salePrice)
    };
  }
}
```

### 🔄 **State Management**

```typescript
// ✅ CORRECTO - Clean NgRx implementation
export const ProductActions = createActionGroup({
  source: 'Product',
  events: {
    'Load Products': props<{ params?: ProductSearchParams }>(),
    'Load Products Success': props<{ response: PaginatedResponse<Product> }>(),
    'Load Products Failure': props<{ error: string }>(),
    'Clear Products': emptyProps()
  }
});

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
  }))
);

@Injectable()
export class ProductEffects {
  loadProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.loadProducts),
      switchMap(({ params }) =>
        this.productService.getProducts(params).pipe(
          map(response => ProductActions.loadProductsSuccess({ response })),
          catchError(error => of(ProductActions.loadProductsFailure({ 
            error: error.message 
          })))
        )
      )
    )
  );
}
```

## 🎯 **Performance Best Practices**

### ⚡ **Change Detection Optimization**

```typescript
// ✅ CORRECTO - OnPush strategy
@Component({
  selector: 'app-product-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `...`
})
export class ProductCardComponent {
  @Input() product!: Product;
  
  constructor(private cdr: ChangeDetectorRef) {}
}

// ✅ CORRECTO - TrackBy functions
@Component({
  template: `
    <div *ngFor="let item of items; trackBy: trackByItemId">
      {{ item.name }}
    </div>
  `
})
export class ListComponent {
  trackByItemId(index: number, item: any): string {
    return item.id;
  }
}
```

### 🚀 **Lazy Loading**

```typescript
// ✅ CORRECTO - Feature module lazy loading
const routes: Routes = [
  {
    path: 'products',
    loadChildren: () => import('./features/product/product.module')
      .then(m => m.ProductModule)
  },
  {
    path: 'cart',
    loadChildren: () => import('./features/cart/cart.module')
      .then(m => m.CartModule)
  }
];

// ✅ CORRECTO - Component lazy loading
@Component({
  template: `
    <app-heavy-component 
      *ngIf="showHeavyComponent"
      [data]="data">
    </app-heavy-component>
  `
})
export class ParentComponent {
  showHeavyComponent = false;
}
```

## 🧪 **Testing Best Practices**

### 🎯 **Unit Testing**

```typescript
// ✅ CORRECTO - Comprehensive unit tests
describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('getProducts', () => {
    it('should return products successfully', () => {
      // Arrange
      const mockResponse = {
        data: [{ id: '1', name: 'Test Product' }],
        totalItems: 1,
        currentPage: 1,
        totalPages: 1
      };

      // Act
      service.getProducts().subscribe(response => {
        // Assert
        expect(response.data).toHaveLength(1);
        expect(response.data[0].name).toBe('Test Product');
      });

      // Assert HTTP call
      const req = httpMock.expectOne('/api/products');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle errors gracefully', () => {
      // Act & Assert
      service.getProducts().subscribe({
        error: (error) => {
          expect(error).toBeTruthy();
        }
      });

      const req = httpMock.expectOne('/api/products');
      req.error(new ErrorEvent('Network error'));
    });
  });
});
```

### 🔍 **Component Testing**

```typescript
// ✅ CORRECTO - Component testing with mocks
describe('ProductCardComponent', () => {
  let component: ProductCardComponent;
  let fixture: ComponentFixture<ProductCardComponent>;
  
  const mockProduct: Product = {
    id: '1',
    name: 'Test Product',
    price: 99.99,
    imageUrl: 'test.jpg'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductCardComponent],
      imports: [SharedModule]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductCardComponent);
    component = fixture.componentInstance;
  });

  it('should display product information', () => {
    // Arrange
    component.product = mockProduct;
    
    // Act
    fixture.detectChanges();
    
    // Assert
    const nameElement = fixture.debugElement.query(By.css('.product-name'));
    expect(nameElement.nativeElement.textContent).toBe('Test Product');
  });

  it('should emit add to cart event', () => {
    // Arrange
    spyOn(component.addToCartClick, 'emit');
    component.product = mockProduct;
    fixture.detectChanges();
    
    // Act
    const button = fixture.debugElement.query(By.css('.add-to-cart-btn'));
    button.triggerEventHandler('click', null);
    
    // Assert
    expect(component.addToCartClick.emit).toHaveBeenCalledWith(mockProduct);
  });
});
```

## 🔒 **Security Best Practices**

### 🛡️ **Input Validation**

```typescript
// ✅ CORRECTO - Input sanitization
@Component({
  template: `
    <div [innerHTML]="sanitizedContent"></div>
  `
})
export class ContentComponent {
  @Input() set content(value: string) {
    this.sanitizedContent = this.sanitizer.sanitize(SecurityContext.HTML, value);
  }
  
  sanitizedContent: string = '';
  
  constructor(private sanitizer: DomSanitizer) {}
}

// ✅ CORRECTO - Form validation
export class ProductFormComponent {
  productForm = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    price: ['', [Validators.required, Validators.min(0)]],
    description: ['', [Validators.maxLength(500)]]
  });

  constructor(private fb: FormBuilder) {}

  onSubmit(): void {
    if (this.productForm.valid) {
      const formValue = this.productForm.value;
      // Submit form
    }
  }
}
```

### 🔐 **Authentication & Authorization**

```typescript
// ✅ CORRECTO - JWT handling
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    
    if (token && !this.isPublicRoute(req.url)) {
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(authReq);
    }
    
    return next.handle(req);
  }

  private isPublicRoute(url: string): boolean {
    const publicRoutes = ['/auth/login', '/auth/register', '/public'];
    return publicRoutes.some(route => url.includes(route));
  }
}
```

## 📱 **Accessibility Best Practices**

### ♿ **ARIA and Semantic HTML**

```html
<!-- ✅ CORRECTO - Accessible components -->
<button 
  class="product-card"
  [attr.aria-label]="'View details for ' + product.name"
  [attr.aria-describedby]="'product-description-' + product.id"
  (click)="viewProduct(product)">
  
  <img [src]="product.imageUrl" 
       [alt]="product.name"
       loading="lazy">
  
  <h3 [id]="'product-name-' + product.id">{{ product.name }}</h3>
  <p [id]="'product-description-' + product.id">{{ product.description }}</p>
  
  <span role="text" 
        [attr.aria-label]="'Price: ' + (product.price | currency)">
    {{ product.price | currency }}
  </span>
</button>

<!-- ✅ CORRECTO - Form accessibility -->
<form [formGroup]="searchForm" (ngSubmit)="onSearch()">
  <label for="search-input" class="sr-only">Search products</label>
  <input 
    id="search-input"
    type="search"
    formControlName="query"
    placeholder="Search products..."
    [attr.aria-describedby]="searchForm.get('query')?.errors ? 'search-error' : null">
  
  <div id="search-error" 
       *ngIf="searchForm.get('query')?.errors"
       role="alert"
       class="error-message">
    Please enter a valid search term
  </div>
  
  <button type="submit" 
          [disabled]="searchForm.invalid"
          aria-label="Search products">
    Search
  </button>
</form>
```

## 🌍 **Internationalization (i18n)**

```typescript
// ✅ CORRECTO - i18n implementation
@Component({
  template: `
    <h1 i18n="@@product.list.title">Products</h1>
    <p i18n="@@product.list.description">
      Browse our collection of products
    </p>
    
    <span i18n="@@product.price.label">
      Price: {{ product.price | currency:currentCurrency }}
    </span>
  `
})
export class ProductListComponent {
  currentCurrency = 'USD';
}

// ✅ CORRECTO - Locale-aware services
@Injectable()
export class CurrencyService {
  constructor(@Inject(LOCALE_ID) private locale: string) {}

  formatPrice(price: number): string {
    return new Intl.NumberFormat(this.locale, {
      style: 'currency',
      currency: this.getCurrency()
    }).format(price);
  }

  private getCurrency(): string {
    const currencyMap: Record<string, string> = {
      'en-US': 'USD',
      'es-ES': 'EUR',
      'es-MX': 'MXN'
    };
    
    return currencyMap[this.locale] || 'USD';
  }
}
```

Siguiendo estas mejores prácticas garantizamos código de alta calidad, mantenible y escalable. 🚀
