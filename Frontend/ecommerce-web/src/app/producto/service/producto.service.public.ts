import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Producto } from '../interfaces/producto';
import { PaginaProducto } from '../interfaces/pagina-producto';

@Injectable({ providedIn: 'root' })
export class ProductoPublicService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  // Listado paginado público
  getProductosPaginados(
    page: number,
    size: number
  ): Observable<PaginaProducto> {
    return this.http.get<PaginaProducto>(
      `${this.baseUrl}/api/public/productos/list?page=${page}&size=${size}`
    );
  }

  // Listado simple público
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(
      `${this.baseUrl}/api/public/productos/list`
    );
  }

  // Obtener producto por id público
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(
      `${this.baseUrl}/api/public/productos/list/${id}`
    );
  }
}
