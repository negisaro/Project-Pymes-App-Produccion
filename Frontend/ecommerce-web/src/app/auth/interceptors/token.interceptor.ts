import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    let authReq = req;
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
          // Aquí podrías redirigir o limpiar localStorage si lo deseas
          // Por ejemplo: localStorage.removeItem('token');
        }
        return throwError(() => error);
      })
    );
  }
}

