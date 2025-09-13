import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Proveedor } from '../interfaces/proveedor';

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  getProveedores(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.baseUrl}/api/segura/proveedores/list`);
  }

  getProveedor(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(`${this.baseUrl}/api/segura/proveedores/list/${id}`);
  }
}
