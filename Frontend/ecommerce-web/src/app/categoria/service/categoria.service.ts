import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

import { Categoria } from '../interfaces/categoria';
import { PaginaCategoria } from '../interfaces/pagina-categoria';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  // Listado paginado
  getCategoriasPaginadas(
    page: number,
    size: number
  ): Observable<PaginaCategoria> {
    return this.http.get<PaginaCategoria>(
      `${this.baseUrl}/api/segura/categorias/list?page=${page}&size=${size}`
    );
  }

  // Listado simple
  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.baseUrl}/api/segura/categorias/list`);
  }

  // Detalle por id (mejor REST)
  getCategoria(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.baseUrl}/api/segura/categorias/list/${id}`);
  }

  // Crear
  addCategoria(categoria: Categoria): Observable<Categoria> {
    return this.http.post<Categoria>(
      `${this.baseUrl}/api/segura/categorias/create`,
      categoria
    );
  }

  // Actualizar
  updateCategoria(id: number, categoria: Categoria): Observable<Categoria> {
    return this.http.put<Categoria>(
      `${this.baseUrl}/api/segura/categorias/update/${id}`,
      categoria
    );
  }

  // Eliminar
  deleteCategoria(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/api/segura/categorias/delete/${id}`
    );
  }
}
