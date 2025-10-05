import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import {
  FilterConfig,
  FilterDefinition,
  FilterType,
  FilterOption
} from '../../../interfaces/data-management.interface';

/**
 * Componente de panel de filtros dinámico
 * Proporciona sistema de filtrado reutilizable y configurable
 * Aplicando principios SOLID:
 * - Single Responsibility: Solo maneja filtros
 * - Open/Closed: Extensible para nuevos tipos de filtros
 * - Interface Segregation: Interfaces específicas
 * - Dependency Inversion: Depende de abstracciones
 */
@Component({
  selector: 'app-filters-panel',
  templateUrl: './filters-panel.component.html',
  styleUrls: ['./filters-panel.component.css']
})
export class FiltersPanelComponent implements OnInit, OnChanges {

  // ===== INPUTS =====
  @Input() config!: FilterConfig;
  @Input() initialFilters: Record<string, any> = {};
  @Input() loading: boolean = false;
  @Input() collapsed: boolean = false;

  // ===== OUTPUTS =====
  @Output() filtersChange = new EventEmitter<Record<string, any>>();
  @Output() filtersApply = new EventEmitter<Record<string, any>>();
  @Output() filtersClear = new EventEmitter<void>();
  @Output() collapseToggle = new EventEmitter<boolean>();

  // ===== FORM STATE =====
  filtersForm!: FormGroup;

  // ===== UI STATE =====
  hasActiveFilters = false;

  // ===== ENUMS FOR TEMPLATE =====
  FilterType = FilterType;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initializeForm();
    this.setupFormSubscription();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['config'] && this.config) {
      this.initializeForm();
    }
    if (changes['initialFilters'] && this.initialFilters) {
      this.loadInitialFilters();
    }
  }

  // ===== FORM INITIALIZATION =====

  private initializeForm(): void {
    if (!this.config?.filters) return;

    const formControls: Record<string, any> = {};

    this.config.filters.forEach((filter: FilterDefinition) => {
      const initialValue = this.getInitialValue(filter);
      formControls[filter.key] = [initialValue];

      // Add additional controls for date ranges
      if (filter.type === FilterType.DATE_RANGE) {
        formControls[`${filter.key}_from`] = [null];
        formControls[`${filter.key}_to`] = [null];
      }
    });

    this.filtersForm = this.fb.group(formControls);
    this.setupFormSubscription();
  }

  private getInitialValue(filter: FilterDefinition): any {
    const initialValue = this.initialFilters[filter.key];

    if (initialValue !== undefined) {
      return initialValue;
    }

    switch (filter.type) {
      case FilterType.MULTISELECT:
        return [];
      case FilterType.BOOLEAN:
        return null;
      case FilterType.NUMBER:
        return null;
      default:
        return '';
    }
  }

  private loadInitialFilters(): void {
    if (!this.filtersForm || !this.initialFilters) return;

    Object.keys(this.initialFilters).forEach(key => {
      const control = this.filtersForm.get(key);
      if (control) {
        control.setValue(this.initialFilters[key], { emitEvent: false });
      }
    });

    this.updateActiveFiltersState();
  }

  private setupFormSubscription(): void {
    if (!this.filtersForm) return;

    this.filtersForm.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(values => {
        this.updateActiveFiltersState();
        this.filtersChange.emit(this.getCleanFilters(values));
      });
  }

  // ===== FORM UTILITIES =====

  private updateActiveFiltersState(): void {
    if (!this.filtersForm) return;

    const values = this.filtersForm.value;
    this.hasActiveFilters = Object.keys(values).some(key => {
      const value = values[key];
      if (Array.isArray(value)) {
        return value.length > 0;
      }
      return value !== null && value !== undefined && value !== '';
    });
  }

  private getCleanFilters(values: Record<string, any>): Record<string, any> {
    const cleanFilters: Record<string, any> = {};

    Object.keys(values).forEach(key => {
      const value = values[key];
      if (this.isValidFilterValue(value)) {
        cleanFilters[key] = value;
      }
    });

    return cleanFilters;
  }

  private isValidFilterValue(value: any): boolean {
    if (value === null || value === undefined || value === '') {
      return false;
    }
    if (Array.isArray(value)) {
      return value.length > 0;
    }
    return true;
  }

  // ===== FIELD CLASS UTILITIES =====

  getFieldClasses(filter: FilterDefinition): string {
    const baseClass = 'col-12';

    // Responsive classes based on filter type
    switch (filter.type) {
      case FilterType.TEXT:
      case FilterType.AUTOCOMPLETE:
        return `${baseClass} col-md-6`;
      case FilterType.SELECT:
        return `${baseClass} col-md-4`;
      case FilterType.DATE:
      case FilterType.NUMBER:
        return `${baseClass} col-md-3`;
      case FilterType.BOOLEAN:
        return `${baseClass} col-md-2`;
      case FilterType.DATE_RANGE:
      case FilterType.MULTISELECT:
        return `${baseClass} col-md-6`;
      default:
        return `${baseClass} col-md-4`;
    }
  }

  // ===== MULTI-SELECT UTILITIES =====

  getSelectedOptions(filterKey: string): any[] {
    const value = this.filtersForm?.get(filterKey)?.value;
    return Array.isArray(value) ? value : [];
  }

  getLabelForValue(filter: FilterDefinition, value: any): string {
    if (!filter.options) return value?.toString() || '';

    const option = filter.options.find((opt: FilterOption) => opt.value === value);
    return option?.label || value?.toString() || '';
  }

  getAvailableOptions(filter: FilterDefinition): FilterOption[] {
    if (!filter.options) return [];

    const selectedValues = this.getSelectedOptions(filter.key);
    return filter.options.filter((option: FilterOption) =>
      !selectedValues.includes(option.value)
    );
  }

  addMultiSelectValue(filterKey: string, value: any): void {
    const control = this.filtersForm?.get(filterKey);
    if (!control) return;

    const currentValues = this.getSelectedOptions(filterKey);
    if (!currentValues.includes(value)) {
      control.setValue([...currentValues, value]);
    }
  }

  removeMultiSelectValue(filterKey: string, value: any): void {
    const control = this.filtersForm?.get(filterKey);
    if (!control) return;

    const currentValues = this.getSelectedOptions(filterKey);
    const newValues = currentValues.filter(val => val !== value);
    control.setValue(newValues);
  }

  // ===== ACTION METHODS =====

  applyFilters(): void {
    if (!this.filtersForm) return;

    const filters = this.getCleanFilters(this.filtersForm.value);
    this.filtersApply.emit(filters);
  }

  clearFilters(): void {
    if (!this.filtersForm || !this.config?.filters) return;

    this.config.filters.forEach((filter: FilterDefinition) => {
      const control = this.filtersForm.get(filter.key);
      if (control) {
        const defaultValue = this.getInitialValue(filter);
        control.setValue(defaultValue, { emitEvent: false });
      }

      // Clear date range controls
      if (filter.type === FilterType.DATE_RANGE) {
        this.filtersForm.get(`${filter.key}_from`)?.setValue(null, { emitEvent: false });
        this.filtersForm.get(`${filter.key}_to`)?.setValue(null, { emitEvent: false });
      }
    });

    this.updateActiveFiltersState();
    this.filtersClear.emit();
    this.filtersChange.emit({});
  }

  toggleCollapse(): void {
    this.collapsed = !this.collapsed;
    this.collapseToggle.emit(this.collapsed);
  }

  // ===== SUMMARY UTILITIES =====

  getActiveFiltersSummary(): string[] {
    if (!this.filtersForm || !this.config?.filters) return [];

    const summary: string[] = [];
    const values = this.filtersForm.value;

    this.config.filters.forEach((filter: FilterDefinition) => {
      const value = values[filter.key];

      if (this.isValidFilterValue(value)) {
        let summaryText = `${filter.label}: `;

        if (Array.isArray(value)) {
          summaryText += `${value.length} seleccionado(s)`;
        } else if (filter.type === FilterType.BOOLEAN) {
          summaryText += value ? 'Sí' : 'No';
        } else if (filter.options) {
          const option = filter.options.find((opt: FilterOption) => opt.value === value);
          summaryText += option?.label || value.toString();
        } else {
          summaryText += value.toString();
        }

        summary.push(summaryText);
      }
    });

    return summary;
  }
}
