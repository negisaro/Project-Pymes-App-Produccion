import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Product, ProductPage } from '../interfaces/product-public.interface';

@Injectable({
  providedIn: 'root'
})
export class ProductPublicService {
  private baseUrl = `${environment.baseUrl}/api/public/productos`;

  constructor(private http: HttpClient) { }

  getProducts(page: number = 0, size: number = 10): Observable<ProductPage> {
    return this.http.get<ProductPage>(`${this.baseUrl}/list?page=${page}&size=${size}`);
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/list/${id}`);
  }

  searchProducts(query: string, page: number = 0, size: number = 10): Observable<ProductPage> {
    return this.http.get<ProductPage>(`${this.baseUrl}/search?q=${query}&page=${page}&size=${size}`);
  }

  getProductsByCategory(categoryId: number, page: number = 0, size: number = 10): Observable<ProductPage> {
    return this.http.get<ProductPage>(`${this.baseUrl}/categoria/${categoryId}?page=${page}&size=${size}`);
  }

  getFeaturedProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/destacados`);
  }
}
