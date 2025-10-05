import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environments';
import { CategorySummaryDto } from '../../features/categories/core/models';
import { ApiResponse, PagedResponse, PaginationParams } from '../interfaces';

/**
 * Public Category Service
 * Handles public category operations that don't require authentication
 * Centralized in shared/services for public access across the application
 */
@Injectable({
  providedIn: 'root'
})
export class CategoryPublicService {
  private readonly baseUrl = `${environment.baseUrl}/api/public/categorias`;

  constructor(private http: HttpClient) {}

  /**
   * Gets public categories with pagination
   * No authentication required
   */
  getPublicCategories(
    page: number = 0,
    size: number = 10
  ): Observable<PagedResponse<CategorySummaryDto>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/list`,
      { params }
    ).pipe(
      map(response => {
        if (!response.success) {
          throw new Error(response.message || 'Error obteniendo categorías públicas');
        }
        return response.data;
      })
    );
  }

  /**
   * Gets all active public categories (simplified)
   * Returns only basic category information for public use
   */
  getActiveCategories(): Observable<CategorySummaryDto[]> {
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/list?activo=true&size=100`
    ).pipe(
      map(response => {
        if (!response.success) {
          throw new Error(response.message || 'Error obteniendo categorías activas');
        }
        return response.data.content || [];
      })
    );
  }

  /**
   * Gets categories for navigation/menu purposes
   * Returns hierarchical structure suitable for public navigation
   */
  getCategoriesForNavigation(): Observable<CategorySummaryDto[]> {
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/list?activo=true&nivel=0&size=50`
    ).pipe(
      map(response => {
        if (!response.success) {
          throw new Error(response.message || 'Error obteniendo categorías de navegación');
        }
        return response.data.content || [];
      })
    );
  }

  /**
   * Search categories by name (public search)
   */
  searchCategories(
    query: string,
    page: number = 0,
    size: number = 10
  ): Observable<PagedResponse<CategorySummaryDto>> {
    const params = new HttpParams()
      .set('texto', query)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('activo', 'true');

    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/list`,
      { params }
    ).pipe(
      map(response => {
        if (!response.success) {
          throw new Error(response.message || 'Error buscando categorías');
        }
        return response.data;
      })
    );
  }

  /**
   * Gets category statistics for public display
   * Useful for showing category counts in UI
   */
  getCategoryStats(): Observable<{ totalCategories: number; activeCategories: number }> {
    return this.http.get<ApiResponse<PagedResponse<CategorySummaryDto>>>(
      `${this.baseUrl}/list?size=1`
    ).pipe(
      map(response => {
        if (!response.success) {
          throw new Error(response.message || 'Error obteniendo estadísticas de categorías');
        }
        return {
          totalCategories: response.data.page.totalElements || 0,
          activeCategories: response.data.page.totalElements || 0 // Este endpoint solo devuelve activas por defecto
        };
      })
    );
  }
}
