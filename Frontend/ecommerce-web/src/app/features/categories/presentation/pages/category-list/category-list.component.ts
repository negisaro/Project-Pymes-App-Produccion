import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Subject, takeUntil, finalize, debounceTime, distinctUntilChanged, filter } from 'rxjs';
import { CategoryService, CategoryDto, CategorySummaryDto, CategoryFilterDto } from '../../../core';
import {
  DataTableConfig,
  ColumnDefinition,
  ActionDefinition,
  ColumnType,
  ActionVariant,
  FilterConfig,
  FilterType,
  HeroSectionConfig,
  DataManagementState,
  DataManagementEvents
} from '../../../../../shared/components/data-management';
import { PaginationParams, SortCriteria } from '../../../../../shared/interfaces';
import { NotificationService } from '../../../../../shared/services';
import Swal from 'sweetalert2';

/**
 * Category List Page Component
 * Professional data management implementation using enterprise components
 */
@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  // ===== DATA MANAGEMENT STATE =====
  dataState: DataManagementState<CategoryDto> = {
    items: [],
    selectedItems: [],
    loading: false,
    error: null,
    filters: {},
    pagination: {
      number: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true,
      numberOfElements: 0
    },
    sorting: []
  };

  // ===== CONFIGURATION =====
  heroConfig: HeroSectionConfig = {
    title: 'Gestión de Categorías',
    subtitle: 'Administra las categorías del sistema',
    description: 'Crea, edita y organiza las categorías de productos de manera profesional',
    icon: 'bi bi-diagram-3',
    actions: [
      {
        label: 'Nueva Categoría',
        icon: 'bi bi-plus-lg',
        variant: ActionVariant.PRIMARY,
        handler: () => this.navigateToCreate()
      },
      {
        label: 'Importar',
        icon: 'bi bi-upload',
        variant: ActionVariant.OUTLINE_SECONDARY,
        handler: () => this.importCategories()
      },
      {
        label: 'Exportar',
        icon: 'bi bi-download',
        variant: ActionVariant.OUTLINE_INFO,
        handler: () => this.exportCategories()
      }
    ],
    breadcrumbs: [
      { label: 'Inicio', route: '/admin/dashboard-admin', icon: 'bi bi-house' },
      { label: 'Categorías', active: true, icon: 'bi bi-diagram-3' }
    ],
    stats: [
      { label: 'Total Categorías', value: '0', icon: 'bi bi-diagram-3', color: 'primary' },
      { label: 'Activas', value: '0', icon: 'bi bi-check-circle', color: 'success' },
      { label: 'Inactivas', value: '0', icon: 'bi bi-x-circle', color: 'warning' },
      { label: 'Productos', value: '0', icon: 'bi bi-box', color: 'info' }
    ]
  };

  tableConfig: DataTableConfig<CategoryDto> = {
    columns: this.buildColumns(),
    actions: this.buildActions(),
    selectable: true,
    sortable: true,
    searchable: true,
    exportable: true,
    responsive: true,
    density: 'comfortable',
    showRowNumbers: true,
    striped: true,
    hover: true
  };

  filterConfig: FilterConfig = {
    filters: [
      {
        key: 'texto',
        label: 'Buscar',
        type: FilterType.TEXT,
        placeholder: 'Buscar por nombre, código o descripción...'
      },
      {
        key: 'activo',
        label: 'Estado',
        type: FilterType.SELECT,
        options: [
          { value: '', label: 'Todos' },
          { value: true, label: 'Activos' },
          { value: false, label: 'Inactivos' }
        ]
      },
      {
        key: 'tipo',
        label: 'Tipo',
        type: FilterType.SELECT,
        options: [
          { value: '', label: 'Todos' },
          { value: 'PRODUCTO', label: 'Producto' },
          { value: 'SERVICIO', label: 'Servicio' },
          { value: 'DIGITAL', label: 'Digital' },
          { value: 'FISICA', label: 'Física' }
        ]
      },
      {
        key: 'categoriaPadreId',
        label: 'Categoría Padre',
        type: FilterType.SELECT,
        options: [],
        placeholder: 'Seleccionar categoría padre'
      }
    ],
    searchable: true,
    searchPlaceholder: 'Buscar categorías...',
    collapsible: true,
    position: 'top'
  };

  events: DataManagementEvents<CategoryDto> = {
    onItemSelect: (item) => this.onItemSelect(item),
    onItemsSelect: (items) => this.onItemsSelect(items),
    onItemAction: (action, item) => this.onItemAction(action, item),
    onBulkAction: (action, items) => this.onBulkAction(action, items),
    onFilter: (filters) => this.onFilter(filters),
    onSort: (sorting) => this.onSort(sorting),
    onPageChange: (page) => this.onPageChange(page),
    onPageSizeChange: (size) => this.onPageSizeChange(size),
    onSearch: (term) => this.onSearch(term),
    onExport: (format) => this.onExport(format),
    onRefresh: () => this.loadCategories()
  };

  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    // Asegurar que los arrays del dataState estén inicializados
    this.dataState.items = this.dataState.items || [];
    this.dataState.selectedItems = this.dataState.selectedItems || [];
    this.dataState.sorting = this.dataState.sorting || [];

    this.loadCategories();
    this.loadParentCategories();
    this.updateStats();

    // ✅ AUTO-REFRESH: Detectar cuando se regresa a esta página
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      filter((event: NavigationEnd) => event.url.includes('/admin/dashboard-admin/categoria')),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      console.log('[AUTO-REFRESH] Detectada navegación a categorías, recargando...');
      this.loadCategories();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ===== DATA OPERATIONS =====

  /**
   * Loads categories with current filters and pagination
   */
  private loadCategories(): void {
    this.dataState.loading = true;
    this.dataState.error = null;

    const params: PaginationParams = {
      page: this.dataState.pagination.number,
      size: this.dataState.pagination.size,
      sort: this.buildSortParams()
    };

    const filters: CategoryFilterDto = this.buildFilters();

    this.categoryService.getPagedCategories(params, filters)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.dataState.loading = false;
        })
      )
      .subscribe({
        next: (response) => {
          // Asegurar que siempre asignemos arrays válidos
          this.dataState.items = Array.isArray(response.content) ? response.content : [];
          this.dataState.pagination = response.page || this.dataState.pagination;
          this.updateStats();
        },
        error: (error) => {
          this.dataState.error = error.message;
          this.dataState.items = []; // Asegurar array vacío en caso de error
          this.notificationService.error('Error al cargar categorías: ' + error.message);
        }
      });
  }

  /**
   * Loads parent categories for filter
   */
  private loadParentCategories(): void {
    this.categoryService.getRootCategories({ page: 0, size: 100 })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          const parentFilter = this.filterConfig.filters.find(f => f.key === 'categoriaPadreId');
          if (parentFilter) {
            parentFilter.options = [
              { value: '', label: 'Todas' },
              ...response.content.map(cat => ({
                value: cat.id,
                label: cat.nombre
              }))
            ];
          }
        }
      });
  }

  /**
   * Updates statistics
   */
  private updateStats(): void {
    if (this.heroConfig.stats) {
      this.heroConfig.stats[0].value = this.dataState.pagination.totalElements.toString();

      // Count active/inactive categories
      const activeCount = this.dataState.items.filter(cat => cat.activo).length;
      const inactiveCount = this.dataState.items.length - activeCount;

      this.heroConfig.stats[1].value = activeCount.toString();
      this.heroConfig.stats[2].value = inactiveCount.toString();
    }
  }

  // ===== COLUMN DEFINITIONS =====

  private buildColumns(): ColumnDefinition<CategoryDto>[] {
    return [
      {
        key: 'id',
        label: 'ID',
        type: ColumnType.NUMBER,
        sortable: true,
        width: '80px',
        visible: false // Oculta la columna pero permite tracking
      },
      {
        key: 'codigo',
        label: 'Código',
        type: ColumnType.TEXT,
        sortable: true,
        searchable: true,
        width: '120px',
        formatter: (value) => value || 'N/A'
      },
      {
        key: 'nombre',
        label: 'Nombre',
        type: ColumnType.TEXT,
        sortable: true,
        searchable: true,
        minWidth: '200px'
      },
      {
        key: 'descripcion',
        label: 'Descripción',
        type: ColumnType.TEXT,
        width: '300px',
        formatter: (value) => value ? (value.length > 100 ? value.substring(0, 100) + '...' : value) : 'Sin descripción'
      },
      {
        key: 'activo',
        label: 'Estado',
        type: ColumnType.BADGE,
        width: '100px',
        align: 'center',
        formatter: (value: any) => value ? 'Activo' : 'Inactivo',
        cssClass: 'badge bg-success'
      },
      {
        key: 'nivel',
        label: 'Nivel',
        type: ColumnType.NUMBER,
        width: '80px',
        align: 'center',
        formatter: (value: any) => value?.toString() || '0'
      },
      {
        key: 'fechaCreacion',
        label: 'Creado',
        type: ColumnType.DATE,
        width: '120px',
        formatter: (value: any) => value ? new Date(value).toLocaleDateString() : 'N/A'
      }
    ];
  }

  // ===== ACTION DEFINITIONS =====

  private buildActions(): ActionDefinition<CategoryDto>[] {
    return [
      {
        key: 'view',
        label: 'Ver',
        icon: 'bi bi-eye',
        variant: ActionVariant.INFO,
        handler: (row) => this.viewCategory(row)
      },
      {
        key: 'edit',
        label: 'Editar',
        icon: 'bi bi-pencil',
        variant: ActionVariant.PRIMARY,
        handler: (row: any) => this.editCategory(row)
      },
      {
        key: 'toggle',
        label: 'Activar/Desactivar',
        icon: 'bi bi-toggle-on',
        variant: ActionVariant.SUCCESS,
        handler: (row: any) => this.toggleCategoryStatus(row)
      },
      {
        key: 'delete',
        label: 'Eliminar',
        icon: 'bi bi-trash',
        variant: ActionVariant.DANGER,
        confirmation: {
          title: '¿Eliminar categoría?',
          message: 'Esta acción no se puede deshacer',
          icon: 'warning',
          confirmText: 'Sí, eliminar',
          cancelText: 'Cancelar'
        },
        handler: (row: any) => this.deleteCategory(row)
      }
    ];
  }

  // ===== EVENT HANDLERS =====

  onItemSelect(item: CategoryDto): void {
    const index = this.dataState.selectedItems.findIndex(selected => selected.id === item.id);
    if (index > -1) {
      this.dataState.selectedItems.splice(index, 1);
    } else {
      this.dataState.selectedItems.push(item);
    }
  }

  onItemsSelect(items: CategoryDto[]): void {
    this.dataState.selectedItems = [...items];
  }

  onItemAction(action: string, item: CategoryDto): void {
    switch (action) {
      case 'view':
        this.viewCategory(item);
        break;
      case 'edit':
        this.editCategory(item);
        break;
      case 'toggle':
        this.toggleCategoryStatus(item);
        break;
      case 'delete':
        this.deleteCategory(item);
        break;
    }
  }

  onBulkAction(action: string, items: CategoryDto[]): void {
    switch (action) {
      case 'activate':
        this.bulkToggleStatus(items, true);
        break;
      case 'deactivate':
        this.bulkToggleStatus(items, false);
        break;
      case 'delete':
        this.bulkDelete(items);
        break;
      case 'export':
        this.exportSelected(items);
        break;
    }
  }

  onFilter(filters: Record<string, any>): void {
    this.dataState.filters = filters;
    this.dataState.pagination.number = 0; // Reset to first page
    this.loadCategories();
  }

  onSort(sorting: SortCriteria[]): void {
    this.dataState.sorting = Array.isArray(sorting) ? sorting : [];
    this.loadCategories();
  }

  onPageChange(page: number): void {
    this.dataState.pagination.number = page;
    this.loadCategories();
  }

  onPageSizeChange(size: number): void {
    this.dataState.pagination.size = size;
    this.dataState.pagination.number = 0; // Reset to first page
    this.loadCategories();
  }

  onSearch(term: string): void {
    this.dataState.filters = { ...this.dataState.filters, texto: term };
    this.dataState.pagination.number = 0; // Reset to first page
    this.loadCategories();
  }

  onExport(format: 'excel' | 'pdf' | 'csv'): void {
    this.notificationService.info(`Generando archivo ${format.toUpperCase()}`);
    // TODO: Implement export functionality
  }

  // ===== CATEGORY OPERATIONS =====

  private viewCategory(category: CategoryDto): void {
    this.router.navigate(['/admin/dashboard-admin/categoria/view', category.id]);
  }

  private editCategory(category: CategoryDto): void {
    this.router.navigate(['/admin/dashboard-admin/categoria/edit', category.id]);
  }

  private deleteCategory(category: CategoryDto): void {
    this.categoryService.deleteCategory(category.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.notificationService.deleteSuccess('Categoría');
          this.loadCategories();
        },
        error: (error) => {
          this.notificationService.error(error.message);
        }
      });
  }

  private toggleCategoryStatus(category: CategoryDto): void {
    const newStatus = !category.activo;
    const action = newStatus ? 'activar' : 'desactivar';

    this.categoryService.changeCategoryStatus(category.id, newStatus, `Usuario ${action} categoría`)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.notificationService.success(`Categoría ${action}da correctamente`);
          this.loadCategories();
        },
        error: (error) => {
          this.notificationService.error(error.message);
        }
      });
  }

  private bulkToggleStatus(categories: CategoryDto[], status: boolean): void {
    const action = status ? 'activar' : 'desactivar';

    Swal.fire({
      title: `¿${action.charAt(0).toUpperCase() + action.slice(1)} categorías?`,
      text: `Se ${action}án ${categories.length} categorías seleccionadas`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: `Sí, ${action}`,
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // TODO: Implement bulk status change
        this.notificationService.success(`Categorías ${action}das correctamente`);
        this.loadCategories();
      }
    });
  }

  private bulkDelete(categories: CategoryDto[]): void {
    Swal.fire({
      title: '¿Eliminar categorías?',
      text: `Se eliminarán ${categories.length} categorías seleccionadas`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#dc3545'
    }).then((result) => {
      if (result.isConfirmed) {
        // TODO: Implement bulk delete
        this.notificationService.deleteSuccess('Categorías');
        this.loadCategories();
      }
    });
  }

  private exportSelected(categories: CategoryDto[]): void {
    this.notificationService.info(`Exportando ${categories.length} categorías seleccionadas`);
    // TODO: Implement export selected functionality
  }

  // ===== NAVIGATION =====

  private navigateToCreate(): void {
    this.router.navigate(['/admin/dashboard-admin/categoria/create']);
  }

  private importCategories(): void {
    this.router.navigate(['/admin/dashboard-admin/categoria/import']);
  }

  private exportCategories(): void {
    this.onExport('excel');
  }

  // ===== UTILITY METHODS =====

  private buildSortParams(): string[] {
    if (!Array.isArray(this.dataState.sorting)) {
      return [];
    }

    return this.dataState.sorting.map(sort => `${sort.field},${sort.direction}`);
  }

  private buildFilters(): CategoryFilterDto {
    const filters: CategoryFilterDto = {};

    Object.keys(this.dataState.filters).forEach(key => {
      const value = this.dataState.filters[key];
      if (value !== undefined && value !== null && value !== '') {
        (filters as any)[key] = value;
      }
    });

    return filters;
  }
}
