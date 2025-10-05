/**
 * Interfaces para respuestas estándar de la API
 * Garantiza consistencia en todas las comunicaciones con el backend
 */

export interface ApiResponse<T = any> {
  data: T;
  message: string;
  success: boolean;
  errors?: ValidationError[];
  timestamp?: string;
  path?: string;
}

export interface PagedResponse<T = any> {
  content: T[];
  page: PaginationInfo;
}

export interface PaginationInfo {
  number: number;        // Página actual (0-indexed)
  size: number;          // Tamaño de página
  totalElements: number; // Total de elementos
  totalPages: number;    // Total de páginas
  first: boolean;        // Es primera página
  last: boolean;         // Es última página
  numberOfElements: number; // Elementos en página actual
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string[];
}

export interface ValidationError {
  field: string;
  message: string;
  rejectedValue?: any;
}

export interface FilterCriteria {
  field: string;
  operation: FilterOperation;
  value: any;
  values?: any[]; // Para operaciones como IN, BETWEEN
}

export enum FilterOperation {
  EQUALS = 'eq',
  NOT_EQUALS = 'ne',
  GREATER_THAN = 'gt',
  GREATER_THAN_OR_EQUAL = 'gte',
  LESS_THAN = 'lt',
  LESS_THAN_OR_EQUAL = 'lte',
  LIKE = 'like',
  ILIKE = 'ilike',
  IN = 'in',
  NOT_IN = 'nin',
  BETWEEN = 'between',
  IS_NULL = 'null',
  IS_NOT_NULL = 'nnull'
}

export interface SortCriteria {
  field: string;
  direction: 'asc' | 'desc';
}
