/**
 * Interfaces para el sistema de gestión de datos
 * Proporciona contratos para componentes reutilizables de CRUD
 */

import { SortCriteria, PaginationInfo } from './api-response.interface';

export interface DataTableConfig<T = any> {
  columns: ColumnDefinition<T>[];
  actions?: ActionDefinition<T>[];
  selectable?: boolean;
  sortable?: boolean;
  searchable?: boolean;
  exportable?: boolean;
  responsive?: boolean;
  density?: 'compact' | 'comfortable' | 'spacious';
  showRowNumbers?: boolean;
  striped?: boolean;
  bordered?: boolean;
  hover?: boolean;
}

export interface ColumnDefinition<T = any> {
  key: keyof T | string;
  label: string;
  type?: ColumnType;
  sortable?: boolean;
  searchable?: boolean;
  width?: string;
  minWidth?: string;
  align?: 'left' | 'center' | 'right';
  headerAlign?: 'left' | 'center' | 'right';
  formatter?: (value: any, row: T) => string;
  cellTemplate?: string; // Para templates personalizados
  headerTemplate?: string;
  cssClass?: string;
  headerCssClass?: string;
  visible?: boolean;
  resizable?: boolean;
  pinned?: 'left' | 'right' | false;
}

export enum ColumnType {
  TEXT = 'text',
  NUMBER = 'number',
  DATE = 'date',
  DATETIME = 'datetime',
  BOOLEAN = 'boolean',
  CURRENCY = 'currency',
  PERCENTAGE = 'percentage',
  IMAGE = 'image',
  BADGE = 'badge',
  LINK = 'link',
  ACTIONS = 'actions',
  CUSTOM = 'custom'
}

export interface ActionDefinition<T = any> {
  key: string;
  label: string;
  icon?: string;
  tooltip?: string;
  type?: ActionType;
  variant?: ActionVariant;
  size?: ActionSize;
  visible?: boolean | ((row: T) => boolean);
  disabled?: boolean | ((row: T) => boolean);
  confirmation?: ConfirmationConfig;
  handler: (row: T, action: ActionDefinition<T>) => void;
}

export enum ActionType {
  BUTTON = 'button',
  DROPDOWN = 'dropdown',
  ICON = 'icon',
  LINK = 'link'
}

export enum ActionVariant {
  PRIMARY = 'primary',
  SECONDARY = 'secondary',
  SUCCESS = 'success',
  DANGER = 'danger',
  WARNING = 'warning',
  INFO = 'info',
  LIGHT = 'light',
  DARK = 'dark',
  OUTLINE_PRIMARY = 'outline-primary',
  OUTLINE_SECONDARY = 'outline-secondary',
  OUTLINE_SUCCESS = 'outline-success',
  OUTLINE_DANGER = 'outline-danger',
  OUTLINE_WARNING = 'outline-warning',
  OUTLINE_INFO = 'outline-info'
}

export enum ActionSize {
  SMALL = 'sm',
  MEDIUM = 'md',
  LARGE = 'lg'
}

export interface ConfirmationConfig {
  title: string;
  message: string;
  icon?: 'warning' | 'question' | 'info' | 'error';
  confirmText?: string;
  cancelText?: string;
  type?: 'confirm' | 'input';
  inputPlaceholder?: string;
  inputType?: string;
}

export interface FilterConfig {
  filters: FilterDefinition[];
  searchable?: boolean;
  searchPlaceholder?: string;
  collapsible?: boolean;
  position?: 'top' | 'side';
  variant?: 'card' | 'inline';
  showActionButtons?: boolean;
}

export interface FilterDefinition {
  key: string;
  label: string;
  type: FilterType;
  options?: FilterOption[];
  placeholder?: string;
  multiple?: boolean;
  searchable?: boolean;
  clearable?: boolean;
  dataSource?: string; // Para cargar opciones dinámicamente
  dependsOn?: string[]; // Filtros que dependen de otros
  validator?: (value: any) => boolean;
  formatter?: (value: any) => string;
}

export enum FilterType {
  TEXT = 'text',
  NUMBER = 'number',
  SELECT = 'select',
  MULTISELECT = 'multiselect',
  DATE = 'date',
  DATE_RANGE = 'daterange',
  BOOLEAN = 'boolean',
  AUTOCOMPLETE = 'autocomplete',
  RANGE = 'range'
}

export interface FilterOption {
  value: any;
  label: string;
  disabled?: boolean;
  group?: string;
  icon?: string;
  color?: string;
}

export interface HeroSectionConfig {
  title: string;
  subtitle?: string;
  description?: string;
  icon?: string;
  image?: string;
  gradient?: string;
  actions?: HeroAction[];
  breadcrumbs?: Breadcrumb[];
  stats?: StatCard[];
}

export interface HeroAction {
  label: string;
  icon?: string;
  variant?: ActionVariant;
  size?: ActionSize;
  handler: () => void;
  visible?: boolean;
  disabled?: boolean;
}

export interface Breadcrumb {
  label: string;
  route?: string;
  icon?: string;
  active?: boolean;
}

export interface StatCard {
  label: string;
  value: string | number;
  icon?: string;
  color?: string;
  trend?: {
    value: number;
    direction: 'up' | 'down';
    label?: string;
  };
}

export interface MultiSelectConfig<T = any> {
  items: T[];
  displayKey: keyof T | string;
  valueKey: keyof T | string;
  searchable?: boolean;
  clearable?: boolean;
  placeholder?: string;
  emptyMessage?: string;
  maxSelectedItems?: number;
  groupBy?: keyof T | string;
  virtualScroll?: boolean;
  loading?: boolean;
  disabled?: boolean;
}

export interface DataManagementState<T = any> {
  items: T[];
  selectedItems: T[];
  loading: boolean;
  error: string | null;
  filters: Record<string, any>;
  pagination: PaginationInfo;
  sorting: SortCriteria[];
}

export interface DataManagementEvents<T = any> {
  onItemSelect?: (item: T) => void;
  onItemsSelect?: (items: T[]) => void;
  onItemAction?: (action: string, item: T) => void;
  onBulkAction?: (action: string, items: T[]) => void;
  onFilter?: (filters: Record<string, any>) => void;
  onSort?: (sorting: SortCriteria[]) => void;
  onPageChange?: (page: number) => void;
  onPageSizeChange?: (size: number) => void;
  onSearch?: (term: string) => void;
  onExport?: (format: 'excel' | 'pdf' | 'csv') => void;
  onRefresh?: () => void;
}
