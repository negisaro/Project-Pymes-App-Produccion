import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Categoria } from '../interfaces/categoria';
import { PaginaCategoria } from '../interfaces/pagina-categoria';

@Injectable({ providedIn: 'root' })
export class CategoriaPublicService {
  private readonly baseUrl = environment.baseUrl;
  private http = inject(HttpClient);

  // Listado simple
  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.baseUrl}/api/public/categorias/list`);
  }


  // Listado paginado
  getCategoriasPaginadas(
    page: number,
    size: number
  ): Observable<PaginaCategoria> {
    return this.http.get<PaginaCategoria>(
      `${this.baseUrl}/api/public/categorias/list?page=${page}&size=${size}`
    );
  }
}


