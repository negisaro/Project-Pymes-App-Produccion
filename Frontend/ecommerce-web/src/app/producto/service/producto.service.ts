import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Producto } from '../interfaces/producto';

// Interfaz para la respuesta paginada
export interface PaginaProducto {
  content: Producto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  // Listado paginado
  getProductosPaginados(
    page: number,
    size: number
  ): Observable<PaginaProducto> {
    return this.http.get<PaginaProducto>(
      `${this.baseUrl}/api/segura/productos/list?page=${page}&size=${size}`
    );
  }

  /**
   * Obtiene un producto por ID
   */
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.baseUrl}/api/segura/productos/list/${id}`);
  }

  /**
   * Crea un nuevo producto
   */
  addProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(
      `${this.baseUrl}/api/segura/productos/create`,
      producto
    );
  }

  /**
   * Actualiza un producto existente
   */
  updateProducto(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(
      `${this.baseUrl}/api/segura/productos/update/${id}`,
      producto
    );
  }

  /**
   * Elimina un producto por ID
   */
  deleteProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/segura/productos/delete/${id}`);
  }

  /**
   * Subir imagen
   */
  subirImagen(formData: FormData) {
    return this.http.post<{ url: string }>(
      `${this.baseUrl}/api/segura/productos/upload`,
      formData
    );
  }
}
