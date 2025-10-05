import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged, switchMap, of, finalize } from 'rxjs';
import {
  CategoryService,
  CategoryDto,
  CategoryCreateDto,
  CategoryUpdateDto,
  CategorySummaryDto,
  CategoryType
} from '../../../core';
import { HeroSectionConfig, ActionVariant } from '../../../../../shared/components/data-management';
import { NotificationService } from '../../../../../shared/services';

/**
 * Category Form Page Component
 * Handles category creation and editing with comprehensive validation
 */
@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  // ===== FORM STATE =====
  categoryForm!: FormGroup;
  isEditMode = false;
  categoryId: number | null = null;
  loading = false;
  saving = false;

  // ===== DATA =====
  parentCategories: CategorySummaryDto[] = [];
  availableIcons: string[] = [
    'bi-diagram-3', 'bi-laptop', 'bi-phone', 'bi-tablet', 'bi-headphones',
    'bi-camera', 'bi-tv', 'bi-speaker', 'bi-smartwatch', 'bi-keyboard',
    'bi-mouse', 'bi-printer', 'bi-router', 'bi-cpu', 'bi-memory'
  ];

  categoryTypes = Object.values(CategoryType);

  // ===== VALIDATION STATE =====
  codeValidation = {
    checking: false,
    available: true,
    message: ''
  };

  // ===== UI STATE =====
  expandedSections = {
    basic: true,
    seo: false,
    business: false,
    visual: false
  };

  // ===== CONFIGURATION =====
  heroConfig: HeroSectionConfig = {
    title: 'Nueva Categoría',
    subtitle: 'Crear nueva categoría del sistema',
    description: 'Complete la información requerida para crear la categoría',
    icon: 'bi bi-plus-circle',
    actions: [
      {
        label: 'Volver al Listado',
        icon: 'bi bi-arrow-left',
        variant: ActionVariant.OUTLINE_SECONDARY,
        handler: () => this.navigateToList()
      }
    ],
    breadcrumbs: [
      { label: 'Inicio', route: '/admin', icon: 'bi bi-house' },
      { label: 'Categorías', route: '/admin/categories', icon: 'bi bi-diagram-3' },
      { label: 'Nueva', active: true, icon: 'bi bi-plus-circle' }
    ]
  };

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute,
    private notificationService: NotificationService
  ) {
    this.buildForm();
  }

  ngOnInit(): void {
    this.checkEditMode();
    this.loadParentCategories();
    this.setupValidators();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ===== FORM SETUP =====

  private buildForm(): void {
    this.categoryForm = this.fb.group({
      // Basic Information
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      codigo: ['', [Validators.required, Validators.pattern(/^[A-Z0-9_-]+$/)]],
      descripcion: ['', [Validators.maxLength(500)]],
      activo: [true],

      // Hierarchy
      categoriaPadreId: [null],

      // Visual
      colorHex: ['#6f42c1'],
      icono: ['bi-diagram-3'],
      imagenUrl: [''],
      ordenVisualizacion: [0, [Validators.min(0)]],

      // SEO
      metaTitulo: ['', [Validators.maxLength(60)]],
      metaDescripcion: ['', [Validators.maxLength(160)]],
      palabrasClave: ['', [Validators.maxLength(255)]],

      // Business
      permiteProductos: [true],
      requiereAprobacion: [false],
      comisionDefecto: [0, [Validators.min(0), Validators.max(100)]],
      tipo: [CategoryType.PRODUCTO]
    });
  }

  private setupValidators(): void {
    // Code availability validation
    this.categoryForm.get('codigo')?.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(code => {
        if (code && code.length >= 2) {
          this.checkCodeAvailability(code);
        }
      });

    // Auto-generate code from name
    this.categoryForm.get('nombre')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(nombre => {
        if (nombre && !this.categoryForm.get('codigo')?.value) {
          const generatedCode = this.generateCodeFromName(nombre);
          this.categoryForm.patchValue({ codigo: generatedCode }, { emitEvent: false });
        }
      });

    // Auto-generate meta title from name
    this.categoryForm.get('nombre')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(nombre => {
        if (nombre && !this.categoryForm.get('metaTitulo')?.value) {
          this.categoryForm.patchValue({ metaTitulo: nombre }, { emitEvent: false });
        }
      });
  }

  // ===== MODE DETECTION =====

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.categoryId = parseInt(id, 10);
      this.updateHeroForEditMode();
      this.loadCategoryForEdit();
    }
  }

  private updateHeroForEditMode(): void {
    this.heroConfig = {
      ...this.heroConfig,
      title: 'Editar Categoría',
      subtitle: 'Modificar información de la categoría',
      description: 'Actualice los datos de la categoría según sea necesario',
      icon: 'bi bi-pencil-square',
      breadcrumbs: [
        { label: 'Inicio', route: '/admin', icon: 'bi bi-house' },
        { label: 'Categorías', route: '/admin/categories', icon: 'bi bi-diagram-3' },
        { label: 'Editar', active: true, icon: 'bi bi-pencil-square' }
      ]
    };
  }

  // ===== DATA LOADING =====

  private loadParentCategories(): void {
    this.categoryService.getRootCategories({ page: 0, size: 100 })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.parentCategories = response.content;
        },
        error: (error) => {
          this.notificationService.error('No se pudieron cargar las categorías padre');
        }
      });
  }

  private loadCategoryForEdit(): void {
    if (!this.categoryId) return;

    this.loading = true;

    this.categoryService.getCategoryById(this.categoryId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (category) => {
          this.populateForm(category);
          this.heroConfig.title = `Editar: ${category.nombre}`;
        },
        error: (error) => {
          this.notificationService.error('No se pudo cargar la categoría');
          this.navigateToList();
        }
      });
  }

  private populateForm(category: CategoryDto): void {
    this.categoryForm.patchValue({
      nombre: category.nombre,
      codigo: category.codigo,
      descripcion: category.descripcion || '',
      activo: category.activo,
      categoriaPadreId: category.categoriaPadreId || null,
      colorHex: category.colorHex || '#6f42c1',
      icono: category.icono || 'bi-diagram-3',
      imagenUrl: category.imagenUrl || '',
      ordenVisualizacion: category.ordenVisualizacion || 0,
      metaTitulo: category.metaTitulo || '',
      metaDescripcion: category.metaDescripcion || '',
      palabrasClave: category.palabrasClave || '',
      permiteProductos: category.permiteProductos ?? true,
      requiereAprobacion: category.requiereAprobacion ?? false,
      comisionDefecto: category.comisionDefecto || 0,
      tipo: CategoryType.PRODUCTO // Default
    });
  }

  // ===== VALIDATION =====

  private checkCodeAvailability(code: string): void {
    if (this.isEditMode && code === this.categoryForm.get('codigo')?.value) {
      return; // Skip validation if it's the same code
    }

    this.codeValidation.checking = true;

    this.categoryService.validateCodeAvailable(code)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (available) => {
          this.codeValidation.available = available;
          this.codeValidation.message = available ? 'Código disponible' : 'Código ya existe';
          this.codeValidation.checking = false;
        },
        error: () => {
          this.codeValidation.checking = false;
          this.codeValidation.available = false;
          this.codeValidation.message = 'Error al validar código';
        }
      });
  }

  // ===== FORM OPERATIONS =====

  onSubmit(): void {
    if (this.categoryForm.invalid || !this.codeValidation.available) {
      this.markFormGroupTouched();
      this.notificationService.formValidationError();
      return;
    }

    this.saving = true;

    if (this.isEditMode) {
      this.updateCategory();
    } else {
      this.createCategory();
    }
  }

  private createCategory(): void {
    const formData = this.categoryForm.value;
    const createDto: CategoryCreateDto = this.mapFormToCreateDto(formData);

    this.categoryService.createCategory(createDto)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.saving = false)
      )
      .subscribe({
        next: (category) => {
          this.notificationService.saveSuccess('Categoría');
          this.navigateToList();
        },
        error: (error) => {
          this.notificationService.error(error.message);
        }
      });
  }

  private updateCategory(): void {
    if (!this.categoryId) return;

    const formData = this.categoryForm.value;
    const updateDto: CategoryUpdateDto = this.mapFormToUpdateDto(formData);

    this.categoryService.updateCategory(this.categoryId, updateDto)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.saving = false)
      )
      .subscribe({
        next: (category) => {
          this.notificationService.updateSuccess('Categoría');
          this.navigateToList();
        },
        error: (error) => {
          this.notificationService.error(error.message);
        }
      });
  }

  // ===== FORM MAPPING =====

  private mapFormToCreateDto(formData: any): CategoryCreateDto {
    return {
      nombre: formData.nombre,
      codigo: formData.codigo,
      descripcion: formData.descripcion || undefined,
      activo: formData.activo,
      categoriaPadreId: formData.categoriaPadreId || undefined,
      ordenVisualizacion: formData.ordenVisualizacion,
      colorHex: formData.colorHex,
      icono: formData.icono,
      imagenUrl: formData.imagenUrl || undefined,
      metaTitulo: formData.metaTitulo || undefined,
      metaDescripcion: formData.metaDescripcion || undefined,
      palabrasClave: formData.palabrasClave || undefined,
      permiteProductos: formData.permiteProductos,
      requiereAprobacion: formData.requiereAprobacion,
      comisionDefecto: formData.comisionDefecto,
      tipo: formData.tipo
    };
  }

  private mapFormToUpdateDto(formData: any): CategoryUpdateDto {
    return {
      id: this.categoryId!,
      ...this.mapFormToCreateDto(formData),
      motivoCambio: 'Actualización desde formulario web'
    };
  }

  // ===== UI HELPERS =====

  toggleSection(section: keyof typeof this.expandedSections): void {
    this.expandedSections[section] = !this.expandedSections[section];
  }

  private generateCodeFromName(nombre: string): string {
    return nombre
      .toUpperCase()
      .replace(/[^A-Z0-9\s]/g, '')
      .replace(/\s+/g, '_')
      .substring(0, 20);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.categoryForm.controls).forEach(key => {
      const control = this.categoryForm.get(key);
      control?.markAsTouched();
    });
  }

  // ===== NAVIGATION =====

  private navigateToList(): void {
    this.router.navigate(['/admin/categories']);
  }

  onCancel(): void {
    this.navigateToList();
  }

  // ===== GETTERS FOR TEMPLATE =====

  get f() { return this.categoryForm.controls; }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.categoryForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.categoryForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return 'Este campo es requerido';
      if (field.errors['minlength']) return `Mínimo ${field.errors['minlength'].requiredLength} caracteres`;
      if (field.errors['maxlength']) return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      if (field.errors['pattern']) return 'Formato inválido';
      if (field.errors['min']) return `Valor mínimo: ${field.errors['min'].min}`;
      if (field.errors['max']) return `Valor máximo: ${field.errors['max'].max}`;
    }
    return '';
  }
}
