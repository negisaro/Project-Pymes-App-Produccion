import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import {
  DataTableConfig,
  ColumnDefinition,
  ActionDefinition,
  ColumnType,
  ActionType,
  DataManagementEvents
} from '../../../interfaces/data-management.interface';
import { SortCriteria } from '../../../interfaces/api-response.interface';

/**
 * Componente de tabla genérica para gestión de datos
 * Proporciona funcionalidad CRUD completa y reutilizable
 */
@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent<T = any> implements OnInit, OnChanges {

  // ===== INPUTS =====
  @Input() data: T[] = [];
  @Input() config!: DataTableConfig<T>;
  @Input() loading: boolean = false;
  @Input() error: string | null = null;
  @Input() selectedItems: T[] = [];
  @Input() totalItems: number = 0;
  @Input() currentPage: number = 0;
  @Input() pageSize: number = 10;
  @Input() searchTerm: string = '';
  @Input() sortCriteria: SortCriteria[] = [];

  // ===== OUTPUTS =====
  @Output() itemSelect = new EventEmitter<T>();
  @Output() itemsSelect = new EventEmitter<T[]>();
  @Output() itemAction = new EventEmitter<{action: string, item: T}>();
  @Output() bulkAction = new EventEmitter<{action: string, items: T[]}>();
  @Output() sort = new EventEmitter<SortCriteria[]>();
  @Output() search = new EventEmitter<string>();
  @Output() export = new EventEmitter<'excel' | 'pdf' | 'csv'>();
  @Output() refresh = new EventEmitter<void>();

  // ===== ESTADO INTERNO =====
  allSelected: boolean = false;
  indeterminate: boolean = false;
  visibleColumns: ColumnDefinition<T>[] = [];
  searchTimeout: any;

  // ===== REFERENCIAS A ENUMS PARA TEMPLATE =====
  ColumnType = ColumnType;
  ActionType = ActionType;

  constructor() { }

  ngOnInit(): void {
    this.initializeComponent();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['config']) {
      this.initializeComponent();
    }

    if (changes['selectedItems'] || changes['data']) {
      this.updateSelectionState();
    }
  }

  // ===== INICIALIZACIÓN =====
  private initializeComponent(): void {
    if (!this.config) {
      this.config = this.getDefaultConfig();
    }

    this.setupVisibleColumns();
    this.updateSelectionState();
  }

  private getDefaultConfig(): DataTableConfig<T> {
    return {
      columns: [],
      actions: [],
      selectable: false,
      sortable: true,
      searchable: true,
      exportable: true,
      responsive: true,
      density: 'comfortable',
      showRowNumbers: false,
      striped: true,
      bordered: false,
      hover: true
    };
  }

  private setupVisibleColumns(): void {
    this.visibleColumns = this.config.columns.filter(col => col.visible !== false);
  }

  // ===== SELECCIÓN =====
  onSelectAll(): void {
    if (this.allSelected) {
      // Deseleccionar todos
      this.selectedItems = [];
      this.allSelected = false;
    } else {
      // Seleccionar todos los visibles
      this.selectedItems = [...this.data];
      this.allSelected = true;
    }

    this.indeterminate = false;
    this.itemsSelect.emit(this.selectedItems);
  }

  onSelectItem(item: T, event: Event): void {
    const checkbox = event.target as HTMLInputElement;

    if (checkbox.checked) {
      // Agregar item si no está seleccionado
      if (!this.isItemSelected(item)) {
        this.selectedItems = [...this.selectedItems, item];
      }
    } else {
      // Remover item
      this.selectedItems = this.selectedItems.filter(selected =>
        this.getItemId(selected) !== this.getItemId(item)
      );
    }

    this.updateSelectionState();
    this.itemsSelect.emit(this.selectedItems);
  }

  isItemSelected(item: T): boolean {
    return this.selectedItems.some(selected =>
      this.getItemId(selected) === this.getItemId(item)
    );
  }

  private updateSelectionState(): void {
    const selectedCount = this.selectedItems.length;
    const totalCount = this.data.length;

    this.allSelected = selectedCount > 0 && selectedCount === totalCount;
    this.indeterminate = selectedCount > 0 && selectedCount < totalCount;
  }

  private getItemId(item: T): any {
    // Buscar column que podría ser ID
    const idColumn = this.config.columns.find(col =>
      col.key === 'id' || col.key === 'ID' ||
      col.key.toString().toLowerCase().includes('id')
    );

    if (idColumn) {
      return (item as any)[idColumn.key];
    }

    // Fallback: usar el primer campo
    if (this.config.columns.length > 0) {
      return (item as any)[this.config.columns[0].key];
    }

    return item;
  }

  // ===== ORDENAMIENTO =====
  onSort(column: ColumnDefinition<T>): void {
    if (!column.sortable || !this.config.sortable) {
      return;
    }

    const existingSort = this.sortCriteria.find(sort => sort.field === column.key.toString());
    let newSortCriteria: SortCriteria[];

    if (existingSort) {
      // Cambiar dirección o remover
      if (existingSort.direction === 'asc') {
        existingSort.direction = 'desc';
        newSortCriteria = [...this.sortCriteria];
      } else {
        // Remover ordenamiento
        newSortCriteria = this.sortCriteria.filter(sort => sort.field !== column.key.toString());
      }
    } else {
      // Agregar nuevo ordenamiento
      const newSort: SortCriteria = {
        field: column.key.toString(),
        direction: 'asc'
      };
      newSortCriteria = [newSort]; // Solo un ordenamiento por ahora
    }

    this.sortCriteria = newSortCriteria;
    this.sort.emit(newSortCriteria);
  }

  getSortIcon(column: ColumnDefinition<T>): string {
    const sort = this.sortCriteria.find(s => s.field === column.key.toString());

    if (!sort) {
      return 'bi bi-arrow-down-up text-muted';
    }

    return sort.direction === 'asc'
      ? 'bi bi-arrow-up text-primary'
      : 'bi bi-arrow-down text-primary';
  }

  // ===== BÚSQUEDA =====
  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    // Debounce para evitar múltiples llamadas
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }

    this.searchTimeout = setTimeout(() => {
      this.search.emit(value);
    }, 300);
  }

  // ===== ACCIONES =====
  onActionClick(action: ActionDefinition<T>, item: T): void {
    if (this.isActionDisabled(action, item) || !this.isActionVisible(action, item)) {
      return;
    }

    // Si tiene confirmación, mostrarla
    if (action.confirmation) {
      this.showActionConfirmation(action, item);
    } else {
      this.executeAction(action, item);
    }
  }

  private async showActionConfirmation(action: ActionDefinition<T>, item: T): Promise<void> {
    // TODO: Integrar con NotificationService
    const confirmed = confirm(action.confirmation!.message);

    if (confirmed) {
      this.executeAction(action, item);
    }
  }

  private executeAction(action: ActionDefinition<T>, item: T): void {
    // Ejecutar handler del componente padre
    if (action.handler) {
      action.handler(item, action);
    }

    // Emitir evento
    this.itemAction.emit({ action: action.key, item });
  }

  isActionVisible(action: ActionDefinition<T>, item: T): boolean {
    if (typeof action.visible === 'function') {
      return action.visible(item);
    }
    return action.visible !== false;
  }

  isActionDisabled(action: ActionDefinition<T>, item: T): boolean {
    if (typeof action.disabled === 'function') {
      return action.disabled(item);
    }
    return action.disabled === true;
  }

  // ===== FORMATEO DE CELDAS =====
  formatCellValue(value: any, column: ColumnDefinition<T>, row: T): string {
    // Usar formatter personalizado si existe
    if (column.formatter) {
      return column.formatter(value, row);
    }

    // Formateo por tipo
    switch (column.type) {
      case ColumnType.DATE:
        return this.formatDate(value);

      case ColumnType.DATETIME:
        return this.formatDateTime(value);

      case ColumnType.CURRENCY:
        return this.formatCurrency(value);

      case ColumnType.PERCENTAGE:
        return this.formatPercentage(value);

      case ColumnType.BOOLEAN:
        return this.formatBoolean(value);

      default:
        return this.formatDefault(value);
    }
  }

  private formatDate(value: any): string {
    if (!value) return '';

    try {
      const date = new Date(value);
      return date.toLocaleDateString('es-ES');
    } catch {
      return value?.toString() || '';
    }
  }

  private formatDateTime(value: any): string {
    if (!value) return '';

    try {
      const date = new Date(value);
      return date.toLocaleString('es-ES');
    } catch {
      return value?.toString() || '';
    }
  }

  private formatCurrency(value: any): string {
    if (value === null || value === undefined) return '';

    try {
      const num = parseFloat(value);
      return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR'
      }).format(num);
    } catch {
      return value?.toString() || '';
    }
  }

  private formatPercentage(value: any): string {
    if (value === null || value === undefined) return '';

    try {
      const num = parseFloat(value);
      return `${num}%`;
    } catch {
      return value?.toString() || '';
    }
  }

  private formatBoolean(value: any): string {
    if (value === true) return 'Sí';
    if (value === false) return 'No';
    return value?.toString() || '';
  }

  private formatDefault(value: any): string {
    if (value === null || value === undefined) return '';
    return value.toString();
  }

  // ===== UTILIDADES =====
  getTableClasses(): string {
    const classes = ['table'];

    if (this.config.striped) classes.push('table-striped');
    if (this.config.bordered) classes.push('table-bordered');
    if (this.config.hover) classes.push('table-hover');

    // Densidad
    if (this.config.density === 'compact') classes.push('table-sm');

    return classes.join(' ');
  }

  getColumnClasses(column: ColumnDefinition<T>): string {
    const classes = [];

    if (column.cssClass) classes.push(column.cssClass);
    if (column.align) classes.push(`text-${column.align}`);

    return classes.join(' ');
  }

  getHeaderClasses(column: ColumnDefinition<T>): string {
    const classes = [];

    if (column.headerCssClass) classes.push(column.headerCssClass);
    if (column.headerAlign) classes.push(`text-${column.headerAlign}`);
    if (column.sortable && this.config.sortable) classes.push('sortable');

    return classes.join(' ');
  }

  // ===== EXPORTACIÓN =====
  onExport(format: 'excel' | 'pdf' | 'csv'): void {
    this.export.emit(format);
  }

  onRefresh(): void {
    this.refresh.emit();
  }

  // ===== TRACKING =====
  trackByFn(index: number, item: ColumnDefinition<T>): any {
    return item.key || index;
  }

  trackByItemFn(index: number, item: T): any {
    return this.getItemId(item) || index;
  }

  // ===== UTILITIES =====
  getColspan(): number {
    let colspan = this.visibleColumns.length;
    if (this.config.selectable) colspan++;
    if (this.config.actions && this.config.actions.length > 0) colspan++;
    return colspan;
  }

  // Helper para acceso seguro a propiedades
  getItemValue(item: T, key: string | keyof T): any {
    return (item as any)[key];
  }

  // ===== GETTER PARA TEMPLATE =====
  get hasDropdownActions(): boolean {
    return this.config.actions?.some(a => a.type === ActionType.DROPDOWN) || false;
  }

  // ===== ACCIONES =====
  onItemAction(actionKey: string, item: T): void {
    this.itemAction.emit({ action: actionKey, item });
  }
}
