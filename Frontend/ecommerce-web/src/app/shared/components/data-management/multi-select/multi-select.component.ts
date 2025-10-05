import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges, forwardRef, ElementRef, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MultiSelectConfig } from '../../../interfaces/data-management.interface';

/**
 * Componente de selección múltiple reutilizable
 * Compatible con Angular Forms y altamente configurable
 */
@Component({
  selector: 'app-multi-select',
  templateUrl: './multi-select.component.html',
  styleUrls: ['./multi-select.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MultiSelectComponent),
      multi: true
    }
  ]
})
export class MultiSelectComponent<T = any> implements OnInit, OnChanges, ControlValueAccessor {

  // ===== INPUTS =====
  @Input() config!: MultiSelectConfig<T>;
  @Input() placeholder: string = 'Seleccionar elementos...';
  @Input() disabled: boolean = false;
  @Input() required: boolean = false;
  @Input() multiple: boolean = true;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';

  // ===== OUTPUTS =====
  @Output() selectionChange = new EventEmitter<T[]>();
  @Output() itemAdd = new EventEmitter<T>();
  @Output() itemRemove = new EventEmitter<T>();
  @Output() dropdownOpen = new EventEmitter<void>();
  @Output() dropdownClose = new EventEmitter<void>();

  // ===== VIEWCHILD =====
  @ViewChild('searchInput') searchInput!: ElementRef<HTMLInputElement>;

  // ===== ESTADO INTERNO =====
  selectedItems: T[] = [];
  filteredItems: T[] = [];
  searchTerm: string = '';
  isOpen: boolean = false;
  focusedIndex: number = -1;
  groupedItems: Record<string, T[]> = {};
  hasGroups: boolean = false;

  // ===== CONTROL VALUE ACCESSOR =====
  private onChange = (value: T[]) => {};
  private onTouched = () => {};

  constructor() { }

  ngOnInit(): void {
    this.initializeComponent();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['config']) {
      this.initializeComponent();
    }
  }

  // ===== INICIALIZACIÓN =====
  private initializeComponent(): void {
    if (!this.config) {
      this.config = this.getDefaultConfig();
    }

    this.processItems();
    this.filterItems();
  }

  private getDefaultConfig(): MultiSelectConfig<T> {
    return {
      items: [],
      displayKey: 'name',
      valueKey: 'id',
      searchable: true,
      clearable: true,
      placeholder: this.placeholder,
      emptyMessage: 'No hay elementos disponibles',
      loading: false,
      disabled: false
    };
  }

  private processItems(): void {
    if (this.config.groupBy) {
      this.hasGroups = true;
      this.groupedItems = this.groupItemsByKey();
    } else {
      this.hasGroups = false;
      this.filteredItems = [...this.config.items];
    }
  }

  private groupItemsByKey(): Record<string, T[]> {
    const groups: Record<string, T[]> = {};

    this.config.items.forEach(item => {
      const groupValue = this.getItemValue(item, this.config.groupBy!);
      const groupKey = groupValue?.toString() || 'Sin grupo';

      if (!groups[groupKey]) {
        groups[groupKey] = [];
      }
      groups[groupKey].push(item);
    });

    return groups;
  }

  // ===== FILTRADO Y BÚSQUEDA =====
  private filterItems(): void {
    if (!this.config.searchable || !this.searchTerm.trim()) {
      if (this.hasGroups) {
        this.groupedItems = this.groupItemsByKey();
      } else {
        this.filteredItems = [...this.config.items];
      }
      return;
    }

    const searchLower = this.searchTerm.toLowerCase();

    if (this.hasGroups) {
      const filteredGroups: Record<string, T[]> = {};

      Object.keys(this.groupedItems).forEach(groupKey => {
        const filteredGroupItems = this.groupedItems[groupKey].filter(item =>
          this.itemMatchesSearch(item, searchLower)
        );

        if (filteredGroupItems.length > 0) {
          filteredGroups[groupKey] = filteredGroupItems;
        }
      });

      this.groupedItems = filteredGroups;
    } else {
      this.filteredItems = this.config.items.filter(item =>
        this.itemMatchesSearch(item, searchLower)
      );
    }
  }

  private itemMatchesSearch(item: T, searchTerm: string): boolean {
    const displayValue = this.getItemDisplay(item).toLowerCase();
    return displayValue.includes(searchTerm);
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm = input.value;
    this.filterItems();
    this.resetFocus();
  }

  onSearchClear(): void {
    this.searchTerm = '';
    this.filterItems();
    this.resetFocus();

    if (this.searchInput) {
      this.searchInput.nativeElement.focus();
    }
  }

  // ===== SELECCIÓN =====
  onItemClick(item: T): void {
    if (this.isItemDisabled(item)) {
      return;
    }

    if (this.isItemSelected(item)) {
      this.removeItem(item);
    } else {
      this.addItem(item);
    }
  }

  addItem(item: T): void {
    if (this.isItemSelected(item) || this.isItemDisabled(item)) {
      return;
    }

    if (!this.multiple) {
      this.selectedItems = [item];
    } else if (this.config.maxSelectedItems && this.selectedItems.length >= this.config.maxSelectedItems) {
      return; // Ya se alcanzó el máximo
    } else {
      this.selectedItems = [...this.selectedItems, item];
    }

    this.emitChanges();
    this.itemAdd.emit(item);

    if (!this.multiple) {
      this.closeDropdown();
    }
  }

  removeItem(item: T): void {
    this.selectedItems = this.selectedItems.filter(selected =>
      this.getItemValue(selected, this.config.valueKey) !== this.getItemValue(item, this.config.valueKey)
    );

    this.emitChanges();
    this.itemRemove.emit(item);
  }

  removeItemByIndex(index: number): void {
    if (index >= 0 && index < this.selectedItems.length) {
      const item = this.selectedItems[index];
      this.removeItem(item);
    }
  }

  clearAll(): void {
    if (this.selectedItems.length > 0) {
      this.selectedItems = [];
      this.emitChanges();
    }
  }

  // ===== DROPDOWN =====
  toggleDropdown(): void {
    if (this.disabled || this.config.disabled) {
      return;
    }

    if (this.isOpen) {
      this.closeDropdown();
    } else {
      this.openDropdown();
    }
  }

  openDropdown(): void {
    if (this.disabled || this.config.disabled) {
      return;
    }

    this.isOpen = true;
    this.filterItems();
    this.resetFocus();
    this.dropdownOpen.emit();

    // Focus en el input de búsqueda si está disponible
    setTimeout(() => {
      if (this.config.searchable && this.searchInput) {
        this.searchInput.nativeElement.focus();
      }
    }, 100);
  }

  closeDropdown(): void {
    this.isOpen = false;
    this.searchTerm = '';
    this.filterItems();
    this.resetFocus();
    this.dropdownClose.emit();
    this.onTouched();
  }

  // ===== NAVEGACIÓN POR TECLADO =====
  onKeyDown(event: KeyboardEvent): void {
    switch (event.key) {
      case 'ArrowDown':
        event.preventDefault();
        this.navigateDown();
        break;

      case 'ArrowUp':
        event.preventDefault();
        this.navigateUp();
        break;

      case 'Enter':
        event.preventDefault();
        this.selectFocusedItem();
        break;

      case 'Escape':
        this.closeDropdown();
        break;

      case 'Backspace':
        if (!this.searchTerm && this.selectedItems.length > 0) {
          this.removeItemByIndex(this.selectedItems.length - 1);
        }
        break;
    }
  }

  private navigateDown(): void {
    const availableItems = this.getAvailableItems();

    if (availableItems.length === 0) return;

    this.focusedIndex = Math.min(this.focusedIndex + 1, availableItems.length - 1);
  }

  private navigateUp(): void {
    if (this.focusedIndex <= 0) {
      this.focusedIndex = -1;
    } else {
      this.focusedIndex--;
    }
  }

  private selectFocusedItem(): void {
    const availableItems = this.getAvailableItems();

    if (this.focusedIndex >= 0 && this.focusedIndex < availableItems.length) {
      this.onItemClick(availableItems[this.focusedIndex]);
    }
  }

  private resetFocus(): void {
    this.focusedIndex = -1;
  }

  private getAvailableItems(): T[] {
    if (this.hasGroups) {
      return Object.values(this.groupedItems).flat();
    }
    return this.filteredItems;
  }

  // ===== UTILIDADES =====
  isItemSelected(item: T): boolean {
    return this.selectedItems.some(selected =>
      this.getItemValue(selected, this.config.valueKey) === this.getItemValue(item, this.config.valueKey)
    );
  }

  isItemDisabled(item: T): boolean {
    // Implementar lógica de deshabilitación si es necesario
    return false;
  }

  isFocusedItem(item: T): boolean {
    const availableItems = this.getAvailableItems();
    const itemIndex = availableItems.findIndex(availableItem =>
      this.getItemValue(availableItem, this.config.valueKey) === this.getItemValue(item, this.config.valueKey)
    );

    return itemIndex === this.focusedIndex;
  }

  getItemDisplay(item: T): string {
    return this.getItemValue(item, this.config.displayKey)?.toString() || '';
  }

  private getItemValue(item: T, key: keyof T | string): any {
    if (typeof key === 'string' && key.includes('.')) {
      // Soporte para claves anidadas como 'user.name'
      return key.split('.').reduce((obj, prop) => obj?.[prop], item as any);
    }

    return (item as any)[key];
  }

  private emitChanges(): void {
    this.onChange(this.selectedItems);
    this.selectionChange.emit(this.selectedItems);
  }

  // ===== CONTROL VALUE ACCESSOR =====
  writeValue(value: T[]): void {
    this.selectedItems = value || [];
  }

  registerOnChange(fn: (value: T[]) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  // ===== CLASES CSS DINÁMICAS =====
  getContainerClasses(): string {
    const classes = ['multi-select'];

    classes.push(`multi-select--${this.size}`);

    if (this.isOpen) {
      classes.push('multi-select--open');
    }

    if (this.disabled || this.config.disabled) {
      classes.push('multi-select--disabled');
    }

    if (this.config.loading) {
      classes.push('multi-select--loading');
    }

    return classes.join(' ');
  }

  getItemClasses(item: T): string {
    const classes = ['dropdown-item'];

    if (this.isItemSelected(item)) {
      classes.push('dropdown-item--selected');
    }

    if (this.isItemDisabled(item)) {
      classes.push('dropdown-item--disabled');
    }

    if (this.isFocusedItem(item)) {
      classes.push('dropdown-item--focused');
    }

    return classes.join(' ');
  }

  // ===== GETTERS PARA TEMPLATE =====
  get hasSelection(): boolean {
    return this.selectedItems.length > 0;
  }

  get canClear(): boolean {
    return (this.config.clearable ?? true) && this.hasSelection && !this.disabled && !(this.config.disabled ?? false);
  }

  get hasMaxSelection(): boolean {
    return this.config.maxSelectedItems ? this.selectedItems.length >= this.config.maxSelectedItems : false;
  }

  get placeholderText(): string {
    if (this.hasSelection) {
      return `${this.selectedItems.length} elemento(s) seleccionado(s)`;
    }
    return this.config.placeholder || this.placeholder;
  }

  get hasFilteredItems(): boolean {
    if (this.hasGroups) {
      return Object.keys(this.groupedItems).length > 0;
    }
    return this.filteredItems.length > 0;
  }

  get groupKeys(): string[] {
    return Object.keys(this.groupedItems);
  }
}
