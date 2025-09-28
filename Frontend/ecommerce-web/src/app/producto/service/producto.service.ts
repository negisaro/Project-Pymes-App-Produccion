import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Producto } from '../interfaces/producto';
import { PaginaProducto } from '../interfaces/pagina-producto';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  // Listado paginado administrativo (ruta segura)
  getProductosPaginados(
    page: number,
    size: number
  ): Observable<PaginaProducto> {
    return this.http.get<PaginaProducto>(
      `${this.baseUrl}/api/segura/productos/list?page=${page}&size=${size}`
    );
  }

  // Listado simple administrativo (ruta segura)
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.baseUrl}/api/segura/productos/list`);
  }

  /**
   * Crea un nuevo producto (requiere autenticación)
   */
  addProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(
      `${this.baseUrl}/api/segura/productos/create`,
      producto
    );
  }

  /**
   * Actualiza un producto existente (requiere autenticación)
   */
  updateProducto(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(
      `${this.baseUrl}/api/segura/productos/update/${id}`,
      producto
    );
  }

  /**
   * Elimina un producto por ID (requiere autenticación)
   */
  deleteProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/segura/productos/delete/${id}`);
  }

  /**
   * Subir imagen (requiere autenticación)
   */
  subirImagen(formData: FormData) {
    return this.http.post<{ url: string }>(
      `${this.baseUrl}/api/segura/productos/upload`,
      formData
    );
  }

  /**
   * Obtiene un producto por ID para administración
   */
  getProductoAdmin(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.baseUrl}/api/segura/productos/list/${id}`);
  }
}
