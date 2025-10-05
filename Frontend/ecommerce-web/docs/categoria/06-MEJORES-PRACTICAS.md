# 🏆 Mejores Prácticas y Estándares

## 🎯 Principios Fundamentales

### 🧠 **Clean Architecture Principles**

#### 1. **Separation of Concerns**
```typescript
// ✅ CORRECTO: Responsabilidades bien definidas
@Injectable({ providedIn: 'root' })
export class CategoriaFacade {
  // Solo orchestración, no lógica de negocio
  constructor(
    private businessService: CategoriaBusinessService,
    private repository: CategoriaRepository,
    private stateService: CategoriaStateService
  ) {}
}

// ❌ INCORRECTO: Múltiples responsabilidades mezcladas
@Component({...})
export class CategoriaComponent {
  // NO mezclar lógica de negocio, estado y presentación
  processBusinessLogic() { /* ... */ }
  makeHttpCalls() { /* ... */ }
  manipulateDOM() { /* ... */ }
}
```

#### 2. **Dependency Inversion**
```typescript
// ✅ CORRECTO: Depender de abstracciones
export abstract class CategoriaRepository {
  abstract findAll(): Observable<CategoriaDTO[]>;
  abstract create(categoria: CreateCategoriaDto): Observable<CategoriaDTO>;
}

@Injectable()
export class CategoriaHttpRepository extends CategoriaRepository {
  // Implementación específica
}

// ❌ INCORRECTO: Depender de implementaciones concretas
export class CategoriaService {
  constructor(private httpClient: HttpClient) {} // Acoplamiento directo
}
```

#### 3. **Single Responsibility**
```typescript
// ✅ CORRECTO: Una responsabilidad por clase
@Injectable()
export class CategoriaValidationService {
  validateCreation(data: CreateCategoriaDto): ValidationResult {
    // Solo validaciones
  }
}

@Injectable()
export class CategoriaBusinessService {
  processHierarchy(categorias: CategoriaDTO[]): CategoriaDTO[] {
    // Solo lógica de negocio
  }
}
```

---

## 🔄 Reactive Programming Best Practices

### 📡 **RxJS Patterns**

#### 1. **Unsubscription Strategy**
```typescript
// ✅ CORRECTO: takeUntil pattern
@Component({...})
export class CategoriaListComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  ngOnInit(): void {
    this.facade.categorias$
      .pipe(takeUntil(this.destroy$))
      .subscribe(categorias => this.categorias = categorias);
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// ❌ INCORRECTO: Memory leaks
@Component({...})
export class BadComponent {
  ngOnInit(): void {
    this.facade.categorias$.subscribe(/* ... */); // Sin unsubscribe
  }
}
```

#### 2. **Error Handling**
```typescript
// ✅ CORRECTO: Error handling centralizado
@Injectable()
export class CategoriaRepository {
  findAll(): Observable<CategoriaDTO[]> {
    return this.http.get<ApiResponse<CategoriaDTO[]>>('/categorias').pipe(
      map(response => response.data),
      retry({ count: 3, delay: 1000 }),
      catchError(this.handleError),
      timeout(10000)
    );
  }
  
  private handleError = (error: HttpErrorResponse): Observable<never> => {
    this.logger.error('API Error:', error);
    this.notificationService.showError('Error al cargar categorías');
    return throwError(() => new Error(this.getErrorMessage(error)));
  }
}
```

#### 3. **Caching with shareReplay**
```typescript
// ✅ CORRECTO: Cache automático
@Injectable()
export class CategoriaCacheService {
  private cache = new Map<string, Observable<any>>();
  
  getOrSet<T>(key: string, factory: () => Observable<T>): Observable<T> {
    if (!this.cache.has(key)) {
      const stream$ = factory().pipe(
        shareReplay({ bufferSize: 1, refCount: true }),
        tap(() => this.cache.delete(key)) // Auto-cleanup en error
      );
      this.cache.set(key, stream$);
    }
    return this.cache.get(key)!;
  }
}
```

---

## 🎭 State Management Patterns

### 🗂️ **Immutable State Updates**

```typescript
// ✅ CORRECTO: Estado inmutable
interface CategoriaState {
  readonly categorias: readonly CategoriaDTO[];
  readonly loading: boolean;
  readonly error: string | null;
  readonly selectedId: number | null;
}

@Injectable()
export class CategoriaStoreService {
  private readonly _state = new BehaviorSubject<CategoriaState>(initialState);
  
  addCategoria(categoria: CategoriaDTO): void {
    this.updateState(state => ({
      ...state,
      categorias: [...state.categorias, categoria]
    }));
  }
  
  updateCategoria(id: number, updates: Partial<CategoriaDTO>): void {
    this.updateState(state => ({
      ...state,
      categorias: state.categorias.map(cat => 
        cat.id === id ? { ...cat, ...updates } : cat
      )
    }));
  }
  
  private updateState(updater: (state: CategoriaState) => CategoriaState): void {
    this._state.next(updater(this._state.value));
  }
}
```

### 🎯 **Selectors Pattern**

```typescript
// ✅ CORRECTO: Selectors memoizados
@Injectable()
export class CategoriaSelectors {
  
  // Selectores básicos
  private categorias$ = this.store.select(state => state.categorias);
  private filters$ = this.store.select(state => state.filters);
  
  // Selectores derivados con memoización
  readonly filteredCategorias$ = combineLatest([
    this.categorias$,
    this.filters$
  ]).pipe(
    map(([categorias, filters]) => this.applyFilters(categorias, filters)),
    shareReplay(1)
  );
  
  readonly categoriaTree$ = this.categorias$.pipe(
    map(categorias => this.buildTree(categorias)),
    shareReplay(1)
  );
  
  readonly categoryStats$ = this.categorias$.pipe(
    map(categorias => ({
      total: categorias.length,
      active: categorias.filter(c => c.activo).length,
      destacadas: categorias.filter(c => c.destacada).length
    })),
    shareReplay(1)
  );
}
```

---

## 🎨 Component Design Patterns

### 🏗️ **Smart vs Dumb Components**

#### 📱 **Smart Components (Containers)**
```typescript
// ✅ CORRECTO: Container component
@Component({
  selector: 'app-categoria-management-container',
  template: `
    <app-categoria-list 
      [categorias]="categorias$ | async"
      [loading]="loading$ | async"
      [error]="error$ | async"
      (create)="onCreateCategoria($event)"
      (update)="onUpdateCategoria($event)"
      (delete)="onDeleteCategoria($event)">
    </app-categoria-list>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoriaManagementContainer {
  readonly categorias$ = this.facade.categorias$;
  readonly loading$ = this.facade.loading$;
  readonly error$ = this.facade.error$;
  
  constructor(private facade: CategoriaFacade) {}
  
  onCreateCategoria(data: CreateCategoriaDto): void {
    this.facade.createCategoria(data);
  }
  
  onUpdateCategoria(event: { id: number; data: UpdateCategoriaDto }): void {
    this.facade.updateCategoria(event.id, event.data);
  }
  
  onDeleteCategoria(id: number): void {
    this.facade.deleteCategoria(id);
  }
}
```

#### 🎨 **Dumb Components (Presentational)**
```typescript
// ✅ CORRECTO: Presentational component
@Component({
  selector: 'app-categoria-list',
  template: `
    <div class="categoria-list">
      <app-loading-spinner *ngIf="loading"></app-loading-spinner>
      <app-error-message *ngIf="error" [message]="error"></app-error-message>
      
      <div class="categoria-grid" *ngIf="!loading && !error">
        <app-categoria-card 
          *ngFor="let categoria of categorias; trackBy: trackByFn"
          [categoria]="categoria"
          (edit)="onEdit($event)"
          (delete)="onDelete($event)">
        </app-categoria-card>
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoriaListComponent {
  @Input() categorias: CategoriaDTO[] = [];
  @Input() loading = false;
  @Input() error: string | null = null;
  
  @Output() create = new EventEmitter<CreateCategoriaDto>();
  @Output() update = new EventEmitter<{id: number; data: UpdateCategoriaDto}>();
  @Output() delete = new EventEmitter<number>();
  
  trackByFn = (index: number, item: CategoriaDTO): number => item.id;
  
  onEdit(categoria: CategoriaDTO): void {
    // Lógica de presentación únicamente
    this.update.emit({ id: categoria.id, data: this.mapToUpdateDto(categoria) });
  }
  
  onDelete(categoria: CategoriaDTO): void {
    this.delete.emit(categoria.id);
  }
}
```

### 🔄 **OnPush Change Detection**

```typescript
// ✅ CORRECTO: Optimización con OnPush
@Component({
  selector: 'app-categoria-card',
  template: `
    <div class="card" [class.highlighted]="highlighted">
      <h3>{{ categoria.nombre }}</h3>
      <p>{{ categoria.descripcion }}</p>
      <div class="actions">
        <button (click)="onEdit()" [disabled]="readonly">Editar</button>
        <button (click)="onDelete()" [disabled]="readonly">Eliminar</button>
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoriaCardComponent {
  @Input() categoria!: CategoriaDTO;
  @Input() readonly = false;
  @Input() highlighted = false;
  
  @Output() edit = new EventEmitter<CategoriaDTO>();
  @Output() delete = new EventEmitter<CategoriaDTO>();
  
  constructor(private cdr: ChangeDetectorRef) {}
  
  onEdit(): void {
    this.edit.emit(this.categoria);
  }
  
  onDelete(): void {
    this.delete.emit(this.categoria);
  }
}
```

---

## 🔒 Security Best Practices

### 🛡️ **Input Validation**

```typescript
// ✅ CORRECTO: Validación robusta
@Injectable()
export class CategoriaValidationService {
  
  validateCreation(data: CreateCategoriaDto): ValidationResult {
    const errors: ValidationError[] = [];
    
    // Validación de nombre
    if (!data.nombre?.trim()) {
      errors.push({ field: 'nombre', message: 'Nombre es requerido' });
    } else if (data.nombre.length > 100) {
      errors.push({ field: 'nombre', message: 'Nombre muy largo (máx 100 caracteres)' });
    } else if (!/^[a-zA-ZÀ-ÿ\s\-]+$/.test(data.nombre)) {
      errors.push({ field: 'nombre', message: 'Nombre contiene caracteres inválidos' });
    }
    
    // Validación de código
    if (data.codigo && !/^[A-Z0-9_-]+$/.test(data.codigo)) {
      errors.push({ field: 'codigo', message: 'Código debe ser alfanumérico mayúscula' });
    }
    
    // Validación de jerarquía
    if (data.categoriaPadreId && data.categoriaPadreId < 1) {
      errors.push({ field: 'categoriaPadreId', message: 'ID de padre inválido' });
    }
    
    return {
      valid: errors.length === 0,
      errors
    };
  }
  
  sanitizeInput(input: string): string {
    return input
      .trim()
      .replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '') // XSS protection
      .replace(/[<>'"]/g, ''); // HTML injection protection
  }
}
```

### 🔑 **Authorization Guards**

```typescript
// ✅ CORRECTO: Guard granular
@Injectable()
export class CategoriaPermissionGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private permissionService: PermissionService,
    private router: Router
  ) {}
  
  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    const operation = route.data['operation'] as CrudOperation;
    const resource = 'categoria';
    
    return this.authService.currentUser$.pipe(
      switchMap(user => {
        if (!user) {
          this.router.navigate(['/login']);
          return of(false);
        }
        
        return this.permissionService.hasPermission(user, resource, operation);
      }),
      tap(hasPermission => {
        if (!hasPermission) {
          this.router.navigate(['/unauthorized']);
        }
      })
    );
  }
}

// Uso en routing
const routes: Routes = [
  {
    path: 'admin/categorias',
    canActivate: [CategoriaPermissionGuard],
    data: { operation: 'READ' },
    component: CategoriaManagementContainer
  },
  {
    path: 'admin/categorias/create',
    canActivate: [CategoriaPermissionGuard],
    data: { operation: 'CREATE' },
    component: CategoriaFormContainer
  }
];
```

---

## ⚡ Performance Best Practices

### 🚀 **Lazy Loading Strategies**

```typescript
// ✅ CORRECTO: Lazy loading inteligente
@Injectable()
export class CategoriaLazyService {
  private loadedChunks = new Set<string>();
  
  loadCategoriesChunk(parentId: number, offset: number, limit: number): Observable<CategoriaDTO[]> {
    const chunkKey = `${parentId}-${offset}-${limit}`;
    
    if (this.loadedChunks.has(chunkKey)) {
      return this.getCachedChunk(chunkKey);
    }
    
    return this.repository.findByParent(parentId, { offset, limit }).pipe(
      tap(() => this.loadedChunks.add(chunkKey)),
      tap(chunk => this.cacheChunk(chunkKey, chunk))
    );
  }
  
  preloadNextChunk(currentChunk: { parentId: number; offset: number; limit: number }): void {
    const nextOffset = currentChunk.offset + currentChunk.limit;
    
    // Preload en background sin bloquear UI
    this.loadCategoriesChunk(currentChunk.parentId, nextOffset, currentChunk.limit)
      .pipe(
        takeUntil(this.destroy$),
        catchError(() => EMPTY) // Ignore preload errors
      )
      .subscribe();
  }
}
```

### 💾 **Memoization Techniques**

```typescript
// ✅ CORRECTO: Memoización de cálculos pesados
@Injectable()
export class CategoriaComputationService {
  private memoCache = new Map<string, any>();
  
  @Memoize() // Decorator personalizado
  buildCategoryTree(categories: CategoriaDTO[]): CategoriaTreeNode[] {
    const cacheKey = this.generateCacheKey(categories);
    
    if (this.memoCache.has(cacheKey)) {
      return this.memoCache.get(cacheKey);
    }
    
    const tree = this.computeTree(categories);
    this.memoCache.set(cacheKey, tree);
    
    return tree;
  }
  
  @Memoize()
  calculateCategoryStats(categories: CategoriaDTO[]): CategoriaStats {
    return {
      total: categories.length,
      byLevel: this.groupByLevel(categories),
      byStatus: this.groupByStatus(categories),
      popularityDistribution: this.calculatePopularityDistribution(categories)
    };
  }
  
  private generateCacheKey(categories: CategoriaDTO[]): string {
    // Hash based on IDs and modification timestamps
    return categories
      .map(c => `${c.id}-${c.fechaActualizacion}`)
      .join('|');
  }
}

// Decorator de memoización personalizado
function Memoize() {
  return function(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const cache = new Map();
    const originalMethod = descriptor.value;
    
    descriptor.value = function(...args: any[]) {
      const key = JSON.stringify(args);
      
      if (cache.has(key)) {
        return cache.get(key);
      }
      
      const result = originalMethod.apply(this, args);
      cache.set(key, result);
      
      return result;
    };
    
    return descriptor;
  };
}
```

---

## 🧪 Testing Best Practices

### 🎭 **Mock Strategies**

```typescript
// ✅ CORRECTO: Mocks reutilizables y tipados
export class MockCategoriaRepository extends CategoriaRepository {
  private mockData: CategoriaDTO[] = [
    createMockCategoria({ id: 1, nombre: 'Electrónicos' }),
    createMockCategoria({ id: 2, nombre: 'Ropa', categoriaPadreId: 1 })
  ];
  
  findAll(): Observable<CategoriaDTO[]> {
    return of([...this.mockData]); // Inmutabilidad
  }
  
  findById(id: number): Observable<CategoriaDTO> {
    const categoria = this.mockData.find(c => c.id === id);
    return categoria ? of(categoria) : throwError(() => new Error('Not found'));
  }
  
  create(data: CreateCategoriaDto): Observable<CategoriaDTO> {
    const newCategoria = createMockCategoria({
      ...data,
      id: Math.max(...this.mockData.map(c => c.id)) + 1
    });
    
    this.mockData.push(newCategoria);
    return of(newCategoria);
  }
}

// Factory para crear mocks consistentes
export function createMockCategoria(overrides: Partial<CategoriaDTO> = {}): CategoriaDTO {
  return {
    id: 1,
    codigo: 'TEST001',
    nombre: 'Test Categoria',
    descripcion: 'Descripción de prueba',
    destacada: false,
    visibleEnMenu: true,
    ordenVisualizacion: 1,
    estadoAprobacion: 'APROBADA',
    nivel: 1,
    rutaCompleta: '/test-categoria',
    popularidad: 0,
    totalProductos: 0,
    activo: true,
    eliminado: false,
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
    ...overrides
  };
}
```

### 🔬 **Component Testing Patterns**

```typescript
// ✅ CORRECTO: Test completo de componente
describe('CategoriaListComponent', () => {
  let component: CategoriaListComponent;
  let fixture: ComponentFixture<CategoriaListComponent>;
  let mockFacade: jasmine.SpyObj<CategoriaFacade>;
  
  beforeEach(async () => {
    const facadeSpy = jasmine.createSpyObj('CategoriaFacade', [
      'loadCategorias',
      'deleteCategoria'
    ]);
    
    await TestBed.configureTestingModule({
      declarations: [CategoriaListComponent],
      providers: [
        { provide: CategoriaFacade, useValue: facadeSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Para componentes hijo
    }).compileComponents();
    
    fixture = TestBed.createComponent(CategoriaListComponent);
    component = fixture.componentInstance;
    mockFacade = TestBed.inject(CategoriaFacade) as jasmine.SpyObj<CategoriaFacade>;
  });
  
  describe('Categorias Display', () => {
    it('should display categorias when loaded', () => {
      const mockCategorias = [
        createMockCategoria({ id: 1, nombre: 'Test 1' }),
        createMockCategoria({ id: 2, nombre: 'Test 2' })
      ];
      
      component.categorias = mockCategorias;
      fixture.detectChanges();
      
      const categoryElements = fixture.debugElement.queryAll(
        By.css('[data-testid="categoria-item"]')
      );
      
      expect(categoryElements.length).toBe(2);
      expect(categoryElements[0].nativeElement.textContent).toContain('Test 1');
    });
    
    it('should show loading state', () => {
      component.loading = true;
      fixture.detectChanges();
      
      const loadingElement = fixture.debugElement.query(
        By.css('[data-testid="loading-spinner"]')
      );
      
      expect(loadingElement).toBeTruthy();
    });
  });
  
  describe('User Interactions', () => {
    it('should emit delete event when delete button clicked', () => {
      spyOn(component.delete, 'emit');
      const mockCategoria = createMockCategoria({ id: 1 });
      
      component.onDelete(mockCategoria);
      
      expect(component.delete.emit).toHaveBeenCalledWith(1);
    });
  });
  
  describe('Accessibility', () => {
    it('should have proper ARIA labels', () => {
      component.categorias = [createMockCategoria()];
      fixture.detectChanges();
      
      const listElement = fixture.debugElement.query(By.css('[role="list"]'));
      expect(listElement).toBeTruthy();
      expect(listElement.nativeElement.getAttribute('aria-label')).toBe('Lista de categorías');
    });
  });
});
```

---

## 📏 Code Quality Standards

### 🎯 **ESLint Configuration**

```json
// .eslintrc.json - Configuración estricta
{
  "extends": [
    "@angular-eslint/recommended",
    "@typescript-eslint/recommended",
    "@typescript-eslint/recommended-requiring-type-checking"
  ],
  "rules": {
    // TypeScript strict rules
    "@typescript-eslint/no-explicit-any": "error",
    "@typescript-eslint/no-unused-vars": "error",
    "@typescript-eslint/explicit-function-return-type": "warn",
    
    // Angular specific
    "@angular-eslint/prefer-on-push-component-change-detection": "warn",
    "@angular-eslint/use-injectable-provided-in": "error",
    
    // Code quality
    "complexity": ["error", { "max": 10 }],
    "max-lines-per-function": ["error", { "max": 50 }],
    "max-depth": ["error", { "max": 4 }],
    
    // Naming conventions
    "@typescript-eslint/naming-convention": [
      "error",
      {
        "selector": "interface",
        "format": ["PascalCase"],
        "suffix": ["DTO", "Model", "Interface"]
      },
      {
        "selector": "class",
        "format": ["PascalCase"]
      },
      {
        "selector": "method",
        "format": ["camelCase"]
      }
    ]
  }
}
```

### 📊 **SonarQube Quality Gates**

```yaml
# sonar-project.properties
sonar.projectKey=ecommerce-categorias
sonar.sources=src/app/categoria
sonar.exclusions=**/*.spec.ts,**/mock/**
sonar.tests=src/app/categoria
sonar.test.inclusions=**/*.spec.ts

# Quality Gates
sonar.qualitygate.wait=true
sonar.coverage.exclusions=**/*.spec.ts,**/testing/**
sonar.typescript.lcov.reportPaths=coverage/lcov.info

# Thresholds
sonar.coverage.minimum=80%
sonar.duplicated_lines_density.maximum=3%
sonar.cognitive_complexity.threshold=15
```

---

**Final:** Esta documentación representa las mejores prácticas para implementar un módulo de categorías de nivel empresarial, escalable y mantenible.
