import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environments';
import { Supplier } from '../interfaces/supplier.interface';

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  getSuppliers(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/api/segura/proveedores/list?page=${page}&size=${size}`
    );
  }

  getSupplier(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(
      `${this.baseUrl}/api/segura/proveedores/list/${id}`
    );
  }

  getActiveSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(
      `${this.baseUrl}/api/segura/proveedores/activos`
    );
  }

  searchByName(nombre: string): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(
      `${
        this.baseUrl
      }/api/segura/proveedores/buscar?nombre=${encodeURIComponent(nombre)}`
    );
  }

  getSuppliersByProduct(productId: number): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(
      `${this.baseUrl}/api/segura/proveedores/por-producto/${productId}`
    );
  }

  createSupplier(supplier: Partial<Supplier>): Observable<Supplier> {
    return this.http.post<Supplier>(
      `${this.baseUrl}/api/segura/proveedores/create`,
      supplier
    );
  }

  updateSupplier(
    id: number,
    supplier: Partial<Supplier>
  ): Observable<Supplier> {
    return this.http.put<Supplier>(
      `${this.baseUrl}/api/segura/proveedores/update/${id}`,
      supplier
    );
  }

  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/api/segura/proveedores/delete/${id}`
    );
  }
}