import { Injectable, Inject } from '@angular/core';
import { Observable, map, catchError, throwError, switchMap } from 'rxjs';
import {
  CategoryDto,
  CategorySummaryDto,
  CategoryCreateDto,
  CategoryUpdateDto,
  CategoryFilterDto,
  Category,
  CategoryHierarchy
} from '../models';
import { CategoryRepository } from '../repositories';
import { ApiResponse, PagedResponse, PaginationParams } from '../../../../shared/interfaces';

/**
 * Injection token for category repository
 */
export const CATEGORY_REPOSITORY_TOKEN = 'CategoryRepository';

/**
 * Category Domain Service
 * Implements business logic and orchestrates repository operations
 * Follows Clean Architecture principles
 */
@Injectable({ providedIn: 'root' })
export class CategoryService {

  constructor(
    @Inject(CATEGORY_REPOSITORY_TOKEN)
    private repository: CategoryRepository
  ) {}

  // ===== CORE CRUD OPERATIONS =====

  /**
   * Gets paginated categories (secure endpoint)
   */
  getPagedCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<PagedResponse<CategoryDto>> {
    return this.repository.getPagedCategories(params, filters)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets public categories (no authentication required)
   */
  getPublicCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<PagedResponse<CategorySummaryDto>> {
    return this.repository.getPublicCategories(params, filters)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets category by ID
   */
  getCategoryById(id: number): Observable<CategoryDto> {
    return this.repository.getCategoryById(id)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Creates new category
   */
  createCategory(categoryData: CategoryCreateDto): Observable<CategoryDto> {
    // Apply business rules before creation
    this.validateCategoryData(categoryData);

    return this.repository.createCategory(categoryData)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Updates existing category
   */
  updateCategory(id: number, categoryData: CategoryUpdateDto): Observable<CategoryDto> {
    // Apply business rules before update
    this.validateCategoryUpdateData(categoryData);

    return this.repository.updateCategory(id, categoryData)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Deletes category (soft delete)
   */
  deleteCategory(id: number): Observable<void> {
    return this.validateCanDelete(id).pipe(
      switchMap(canDelete => {
        if (!canDelete) {
          throw new Error('No se puede eliminar la categoría porque tiene productos asociados');
        }
        return this.repository.deleteCategory(id);
      }),
      map(response => this.extractDataFromApiResponse(response)),
      catchError(this.handleError)
    );
  }

  // ===== SPECIFIC OPERATIONS =====

  /**
   * Gets category by unique code
   */
  getCategoryByCode(code: string): Observable<CategoryDto> {
    return this.repository.getCategoryByCode(code)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets subcategories of parent category
   */
  getSubcategories(
    categoryId: number,
    params?: PaginationParams
  ): Observable<PagedResponse<CategorySummaryDto>> {
    return this.repository.getSubcategories(categoryId, params)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets complete hierarchy of a category
   */
  getCategoryHierarchy(id: number): Observable<CategoryDto[]> {
    return this.repository.getCategoryHierarchy(id)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets root categories (without parent)
   */
  getRootCategories(
    params?: PaginationParams
  ): Observable<PagedResponse<CategorySummaryDto>> {
    return this.repository.getRootCategories(params)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  // ===== SEARCH AND FILTER OPERATIONS =====

  /**
   * Searches categories by text
   */
  searchCategories(
    text: string,
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<PagedResponse<CategorySummaryDto>> {
    if (!text || text.trim().length < 2) {
      throw new Error('El texto de búsqueda debe tener al menos 2 caracteres');
    }

    return this.repository.searchCategories(text, params, filters)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets categories by type
   */
  getCategoriesByType(
    type: string,
    params?: PaginationParams
  ): Observable<PagedResponse<CategorySummaryDto>> {
    return this.repository.getCategoriesByType(type, params)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Gets active categories
   */
  getActiveCategories(
    params?: PaginationParams
  ): Observable<PagedResponse<CategorySummaryDto>> {
    return this.repository.getActiveCategories(params)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  // ===== VALIDATION OPERATIONS =====

  /**
   * Validates if category code is available
   */
  validateCodeAvailable(code: string): Observable<boolean> {
    if (!code || code.trim().length === 0) {
      return throwError(() => new Error('El código es requerido'));
    }

    return this.repository.validateCodeAvailable(code)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Validates if category can be deleted
   */
  validateCanDelete(id: number): Observable<boolean> {
    return this.repository.validateCanDelete(id)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  // ===== STATISTICS OPERATIONS =====

  /**
   * Gets category statistics
   */
  getCategoryStatistics(id: number): Observable<any> {
    return this.repository.getCategoryStatistics(id)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Counts products in category
   */
  countProductsInCategory(id: number): Observable<number> {
    return this.repository.countProductsInCategory(id)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  // ===== ADMINISTRATION OPERATIONS =====

  /**
   * Changes category status (activate/deactivate)
   */
  changeCategoryStatus(
    id: number,
    active: boolean,
    reason?: string
  ): Observable<CategoryDto> {
    return this.repository.changeCategoryStatus(id, active, reason)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Reorders categories
   */
  reorderCategories(
    reorderings: { id: number; nuevoOrden: number }[]
  ): Observable<void> {
    if (!reorderings || reorderings.length === 0) {
      throw new Error('Se requiere al menos una categoría para reordenar');
    }

    return this.repository.reorderCategories(reorderings)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  /**
   * Moves category to new parent
   */
  moveCategory(
    id: number,
    newParentId?: number
  ): Observable<CategoryDto> {
    return this.repository.moveCategory(id, newParentId)
      .pipe(
        map(response => this.extractDataFromApiResponse(response)),
        catchError(this.handleError)
      );
  }

  // ===== DOMAIN LOGIC METHODS =====

  /**
   * Converts DTO to Domain Entity
   */
  toDomainEntity(dto: CategoryDto): Category {
    return Category.fromDto(dto);
  }

  /**
   * Builds category hierarchy from DTOs
   */
  buildHierarchy(categories: CategoryDto[]): CategoryHierarchy[] {
    const hierarchy: CategoryHierarchy[] = [];

    categories.forEach((dto, index) => {
      const category = this.toDomainEntity(dto);
      const level = dto.nivel || 0;
      const path = this.buildCategoryPath(dto, categories);

      hierarchy.push(new CategoryHierarchy(category, level, path));
    });

    return hierarchy;
  }

  /**
   * Validates category creation data
   */
  private validateCategoryData(data: CategoryCreateDto): void {
    if (!data.nombre || data.nombre.trim().length < 2) {
      throw new Error('El nombre debe tener al menos 2 caracteres');
    }

    if (data.codigo && !/^[A-Z0-9_-]+$/.test(data.codigo)) {
      throw new Error('El código solo puede contener letras mayúsculas, números, guiones y guiones bajos');
    }

    if (data.comisionDefecto !== undefined && (data.comisionDefecto < 0 || data.comisionDefecto > 100)) {
      throw new Error('La comisión debe estar entre 0 y 100');
    }
  }

  /**
   * Validates category update data
   */
  private validateCategoryUpdateData(data: CategoryUpdateDto): void {
    if (data.nombre && data.nombre.trim().length < 2) {
      throw new Error('El nombre debe tener al menos 2 caracteres');
    }

    if (data.codigo && !/^[A-Z0-9_-]+$/.test(data.codigo)) {
      throw new Error('El código solo puede contener letras mayúsculas, números, guiones y guiones bajos');
    }

    if (data.comisionDefecto !== undefined && (data.comisionDefecto < 0 || data.comisionDefecto > 100)) {
      throw new Error('La comisión debe estar entre 0 y 100');
    }
  }

  /**
   * Builds category path for hierarchy
   */
  private buildCategoryPath(category: CategoryDto, allCategories: CategoryDto[]): string[] {
    const path: string[] = [];
    let currentCategory: CategoryDto | undefined = category;

    while (currentCategory) {
      path.unshift(currentCategory.nombre);
      currentCategory = allCategories.find(c => c.id === currentCategory?.categoriaPadreId);
    }

    return path;
  }

  /**
   * Extracts data from API response
   */
  private extractDataFromApiResponse<T>(response: ApiResponse<T>): T {
    if (!response.success) {
      throw new Error(response.message || 'Error en la operación');
    }
    return response.data;
  }

  /**
   * Handles errors from repository operations
   */
  private handleError = (error: any): Observable<never> => {
    console.error('CategoryService Error:', error);

    let errorMessage = 'Error inesperado en el servicio de categorías';

    if (error?.error?.message) {
      errorMessage = error.error.message;
    } else if (error?.message) {
      errorMessage = error.message;
    } else if (typeof error === 'string') {
      errorMessage = error;
    }

    return throwError(() => new Error(errorMessage));
  };
}
