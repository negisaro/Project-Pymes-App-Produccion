import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private storage = inject(StorageService);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.storage.get<string>('token');
    let authReq = req;

    // Detectar si es FormData (upload de archivo)
    if (req.body instanceof FormData) {
      if (token) {
        authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
            Accept: 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });
      } else {
        authReq = req.clone({
          setHeaders: {
            Accept: 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });
      }
    } else {
      if (token) {
        authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
            'Content-Type': req.headers.get('Content-Type') || 'application/json',
            Accept: 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });
      } else {
        authReq = req.clone({
          setHeaders: {
            'Content-Type': req.headers.get('Content-Type') || 'application/json',
            Accept: 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });
      }
    }

    // Logger para depuración: muestra la request enviada al backend
    console.log('Interceptor - Request:', {
      url: authReq.url,
      method: authReq.method,
      headers: authReq.headers,
      body: authReq.body
    });

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Manejo de 401 si es necesario
        }
        return throwError(() => error);
      })
    );
  }
}