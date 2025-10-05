import { Observable } from 'rxjs';
import { CategoryDto, CategorySummaryDto, CategoryCreateDto, CategoryUpdateDto, CategoryFilterDto } from '../models';
import { ApiResponse, PagedResponse, PaginationParams } from '../../../../shared/interfaces';

/**
 * Category Repository Abstract Interface
 * Defines the contract for category data access operations
 * Aligned with backend microservice msvc-categoria endpoints
 */
export abstract class CategoryRepository {

  // ===== CORE CRUD OPERATIONS =====

  /**
   * Gets paginated categories with security (Secure endpoint)
   * GET /categorias/list
   */
  abstract getPagedCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategoryDto>>>;

  /**
   * Gets public paginated categories
   * GET /public/categorias
   */
  abstract getPublicCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  /**
   * Gets category by ID
   * GET /categorias/{id}
   */
  abstract getCategoryById(id: number): Observable<ApiResponse<CategoryDto>>;

  /**
   * Creates new category
   * POST /categorias
   */
  abstract createCategory(category: CategoryCreateDto): Observable<ApiResponse<CategoryDto>>;

  /**
   * Updates existing category
   * PUT /categorias/{id}
   */
  abstract updateCategory(id: number, category: CategoryUpdateDto): Observable<ApiResponse<CategoryDto>>;

  /**
   * Deletes category (soft delete)
   * DELETE /categorias/{id}
   */
  abstract deleteCategory(id: number): Observable<ApiResponse<void>>;

  // ===== SPECIFIC OPERATIONS =====

  /**
   * Gets category by code
   * GET /categorias/codigo/{codigo}
   */
  abstract getCategoryByCode(code: string): Observable<ApiResponse<CategoryDto>>;

  /**
   * Gets subcategories of a parent category
   * GET /categorias/{id}/subcategorias
   */
  abstract getSubcategories(
    categoryId: number,
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  /**
   * Gets complete hierarchy of a category
   * GET /categorias/{id}/jerarquia
   */
  abstract getCategoryHierarchy(id: number): Observable<ApiResponse<CategoryDto[]>>;

  /**
   * Gets root categories (without parent)
   * GET /categorias/raiz
   */
  abstract getRootCategories(
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  // ===== SEARCH AND FILTER OPERATIONS =====

  /**
   * Searches categories by text
   * GET /categorias/buscar
   */
  abstract searchCategories(
    text: string,
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  /**
   * Gets categories by type
   * GET /categorias/tipo/{tipo}
   */
  abstract getCategoriesByType(
    type: string,
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  /**
   * Gets active categories
   * GET /categorias/activas
   */
  abstract getActiveCategories(
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>>;

  // ===== VALIDATION OPERATIONS =====

  /**
   * Validates if category code is available
   * GET /categorias/validar-codigo/{codigo}
   */
  abstract validateCodeAvailable(code: string): Observable<ApiResponse<boolean>>;

  /**
   * Validates if category can be deleted
   * GET /categorias/{id}/puede-eliminar
   */
  abstract validateCanDelete(id: number): Observable<ApiResponse<boolean>>;

  // ===== STATISTICS OPERATIONS =====

  /**
   * Gets category statistics
   * GET /categorias/{id}/estadisticas
   */
  abstract getCategoryStatistics(id: number): Observable<ApiResponse<any>>;

  /**
   * Counts products in category
   * GET /categorias/{id}/contar-productos
   */
  abstract countProductsInCategory(id: number): Observable<ApiResponse<number>>;

  // ===== ADMINISTRATION OPERATIONS =====

  /**
   * Changes category status
   * PATCH /categorias/{id}/estado
   */
  abstract changeCategoryStatus(
    id: number,
    active: boolean,
    reason?: string
  ): Observable<ApiResponse<CategoryDto>>;

  /**
   * Reorders categories
   * PATCH /categorias/reordenar
   */
  abstract reorderCategories(
    reorderings: { id: number; nuevoOrden: number }[]
  ): Observable<ApiResponse<void>>;

  /**
   * Moves category to new parent
   * PATCH /categorias/{id}/mover
   */
  abstract moveCategory(
    id: number,
    newParentId?: number
  ): Observable<ApiResponse<CategoryDto>>;
}
