import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoryRepository } from './category.repository';
import { CategoryDto, CategorySummaryDto, CategoryCreateDto, CategoryUpdateDto, CategoryFilterDto } from '../models';
import { ApiResponse, PagedResponse, PaginationParams } from '../../../../shared/interfaces';
import { environment } from '../../../../../environments/environments';

/**
 * Category HTTP Repository Implementation
 * Communicates with msvc-categoria microservice through HTTP
 * Implements all endpoints defined in the backend
 */
@Injectable({
  providedIn: 'root'
})
export class CategoryHttpRepository extends CategoryRepository {

  private readonly baseUrl = `${environment.baseUrl}/api/segura/categorias`;
  private readonly publicUrl = `${environment.baseUrl}/api/public/categorias`;

  constructor(private http: HttpClient) {
    super();
  }

  // ===== CORE CRUD OPERATIONS =====

  getPagedCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategoryDto>>> {
    let httpParams = this.buildPaginationParams(params);
    httpParams = this.addFilters(httpParams, filters);

    return this.http.get<ApiResponse<PagedResponse<CategoryDto>>>(
      `${this.baseUrl}/list`,
      { params: httpParams }
    );
  }

  getPublicCategories(
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    let httpParams = this.buildPaginationParams(params);
    httpParams = this.addFilters(httpParams, filters);

    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.publicUrl}/list`,
      { params: httpParams }
    );
  }

  getCategoryById(id: number): Observable<ApiResponse<CategoryDto>> {
    return this.http.get<ApiResponse<CategoryDto>>(`${this.baseUrl}/list/${id}`);
  }

  createCategory(category: CategoryCreateDto): Observable<ApiResponse<CategoryDto>> {
    return this.http.post<ApiResponse<CategoryDto>>(`${this.baseUrl}/create`, category);
  }

  updateCategory(id: number, category: CategoryUpdateDto): Observable<ApiResponse<CategoryDto>> {
    return this.http.put<ApiResponse<CategoryDto>>(`${this.baseUrl}/update/${id}`, category);
  }

  deleteCategory(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/delete/${id}`);
  }

  // ===== SPECIFIC OPERATIONS =====

  getCategoryByCode(code: string): Observable<ApiResponse<CategoryDto>> {
    return this.http.get<ApiResponse<CategoryDto>>(`${this.baseUrl}/codigo/${code}`);
  }

  getSubcategories(
    categoryId: number,
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/${categoryId}/subcategorias`,
      { params: httpParams }
    );
  }

  getCategoryHierarchy(id: number): Observable<ApiResponse<CategoryDto[]>> {
    return this.http.get<ApiResponse<CategoryDto[]>>(`${this.baseUrl}/${id}/jerarquia`);
  }

  getRootCategories(
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/raiz`,
      { params: httpParams }
    );
  }

  // ===== SEARCH AND FILTER OPERATIONS =====

  searchCategories(
    text: string,
    params?: PaginationParams,
    filters?: CategoryFilterDto
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    let httpParams = this.buildPaginationParams(params);
    httpParams = httpParams.set('texto', text);
    httpParams = this.addFilters(httpParams, filters);

    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/buscar`,
      { params: httpParams }
    );
  }

  getCategoriesByType(
    type: string,
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/tipo/${type}`,
      { params: httpParams }
    );
  }

  getActiveCategories(
    params?: PaginationParams
  ): Observable<ApiResponse<PagedResponse<CategorySummaryDto>>> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/activas`,
      { params: httpParams }
    );
  }

  // ===== VALIDATION OPERATIONS =====

  validateCodeAvailable(code: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.baseUrl}/validar-codigo/${code}`);
  }

  validateCanDelete(id: number): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.baseUrl}/${id}/puede-eliminar`);
  }

  // ===== STATISTICS OPERATIONS =====

  getCategoryStatistics(id: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/${id}/estadisticas`);
  }

  countProductsInCategory(id: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/${id}/contar-productos`);
  }

  // ===== ADMINISTRATION OPERATIONS =====

  changeCategoryStatus(
    id: number,
    active: boolean,
    reason?: string
  ): Observable<ApiResponse<CategoryDto>> {
    const body = { activo: active, motivo: reason };
    return this.http.patch<ApiResponse<CategoryDto>>(`${this.baseUrl}/${id}/estado`, body);
  }

  reorderCategories(
    reorderings: { id: number; nuevoOrden: number }[]
  ): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(`${this.baseUrl}/reordenar`, reorderings);
  }

  moveCategory(
    id: number,
    newParentId?: number
  ): Observable<ApiResponse<CategoryDto>> {
    const body = { nuevoPadreId: newParentId };
    return this.http.patch<ApiResponse<CategoryDto>>(`${this.baseUrl}/${id}/mover`, body);
  }

  // ===== PRIVATE UTILITY METHODS =====

  /**
   * Builds HTTP params for pagination
   */
  private buildPaginationParams(params?: PaginationParams): HttpParams {
    let httpParams = new HttpParams();

    if (params) {
      if (params.page !== undefined) {
        httpParams = httpParams.set('page', params.page.toString());
      }
      if (params.size !== undefined) {
        httpParams = httpParams.set('size', params.size.toString());
      }
      if (params.sort && params.sort.length > 0) {
        params.sort.forEach(sortParam => {
          httpParams = httpParams.append('sort', sortParam);
        });
      }
    }

    return httpParams;
  }

  /**
   * Adds filters to HTTP params
   */
  private addFilters(httpParams: HttpParams, filters?: CategoryFilterDto): HttpParams {
    if (!filters) return httpParams;

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        httpParams = httpParams.set(key, value.toString());
      }
    });

    return httpParams;
  }
}
