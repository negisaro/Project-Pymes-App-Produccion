import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Proveedor } from '../interfaces/proveedor';

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  getProveedores(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/api/segura/proveedores/list?page=${page}&size=${size}`
    );
  }

  getProveedor(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(
      `${this.baseUrl}/api/segura/proveedores/list/${id}`
    );
  }

  getProveedoresActivos(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(
      `${this.baseUrl}/api/segura/proveedores/activos`
    );
  }

  buscarPorNombre(nombre: string): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(
      `${
        this.baseUrl
      }/api/segura/proveedores/buscar?nombre=${encodeURIComponent(nombre)}`
    );
  }

  getProveedoresPorProducto(productoId: number): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(
      `${this.baseUrl}/api/segura/proveedores/por-producto/${productoId}`
    );
  }

  crearProveedor(proveedor: Partial<Proveedor>): Observable<Proveedor> {
    return this.http.post<Proveedor>(
      `${this.baseUrl}/api/segura/proveedores/create`,
      proveedor
    );
  }

  actualizarProveedor(
    id: number,
    proveedor: Partial<Proveedor>
  ): Observable<Proveedor> {
    return this.http.put<Proveedor>(
      `${this.baseUrl}/api/segura/proveedores/update/${id}`,
      proveedor
    );
  }

  eliminarProveedor(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/api/segura/proveedores/delete/${id}`
    );
  }
}
