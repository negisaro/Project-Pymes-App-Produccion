import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environments';
import { Product } from '../interfaces/product.interface';
import { ProductPage } from '../interfaces/product-page.interface';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  // Listado paginado administrativo (ruta segura)
  getProductsPaginated(
    page: number,
    size: number
  ): Observable<ProductPage> {
    return this.http.get<ProductPage>(
      `${this.baseUrl}/api/segura/productos/list?page=${page}&size=${size}`
    );
  }

  // Listado simple administrativo (ruta segura)
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/api/segura/productos/list`);
  }

  /**
   * Crea un nuevo producto (requiere autenticación)
   */
  addProduct(producto: Product): Observable<Product> {
    return this.http.post<Product>(
      `${this.baseUrl}/api/segura/productos/create`,
      producto
    );
  }

  /**
   * Actualiza un producto existente (requiere autenticación)
   */
  updateProduct(id: number, producto: Product): Observable<Product> {
    return this.http.put<Product>(
      `${this.baseUrl}/api/segura/productos/update/${id}`,
      producto
    );
  }

  /**
   * Elimina un producto por ID (requiere autenticación)
   */
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/segura/productos/delete/${id}`);
  }

  /**
   * Subir imagen (requiere autenticación)
   */
  uploadImage(formData: FormData) {
    return this.http.post<{ url: string }>(
      `${this.baseUrl}/api/segura/productos/upload`,
      formData
    );
  }

  /**
   * Obtiene un producto por ID para administración
   */
  getProductAdmin(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/api/segura/productos/list/${id}`);
  }
}