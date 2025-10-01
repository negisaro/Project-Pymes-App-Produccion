import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, filter, switchMap, take, tap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private storage = inject(StorageService);
  private auth = inject(AuthService);

  // Control de refresh single-flight
  private isRefreshing = false;
  private refreshSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

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
          return this.handle401(authReq, next);
        }
        if (error.status === 403) {
          // 403: acceso prohibido; opcionalmente forzar logout si el backend indica token inválido/cambiado
          return throwError(() => error);
        }
        return throwError(() => error);
      })
    );
  }

  private handle401(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const failedToken = this.storage.get<string>('token');
    if (!failedToken) {
      this.auth.logout();
      return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'No token available' }));
    }

    if (!this.isRefreshing) {
      this.isRefreshing = true;
  this.refreshSubject.next(null); // resetea para que suscriptores esperen

      // Llamar endpoint refresh (usa AuthService.refreshToken para coherencia)
      return this.auth.refreshToken(failedToken).pipe(
        tap(res => {
          if (res?.token) {
            this.auth.updateToken(res.token);
            this.refreshSubject.next(res.token);
          } else {
            // Si no viene token consideramos refresh fallido y forzamos logout
            this.auth.logout();
            this.refreshSubject.next(null);
          }
        }),
        switchMap(res => {
          if (!res?.token) {
            return throwError(() => new Error('No se recibió nuevo token'));
          }
          this.isRefreshing = false;
          // Reintenta request original con nuevo token
          const newReq = this.addAuthHeader(req, res.token);
          return next.handle(newReq);
        }),
        catchError(err => {
          this.isRefreshing = false;
          this.auth.logout();
          this.refreshSubject.next(null);
          return throwError(() => err);
        })
      );
    } else {
      // Ya hay un refresh en curso: esperar a que refreshSubject emita nuevo token
      return this.refreshSubject.pipe(
        filter(t => t !== null),
        take(1),
        switchMap((newToken) => {
          const retryReq = this.addAuthHeader(req, newToken as string);
          return next.handle(retryReq);
        })
      );
    }
  }

  private addAuthHeader(req: HttpRequest<any>, token: string): HttpRequest<any> {
    // Replicar lógica original para FormData vs JSON
    if (req.body instanceof FormData) {
      return req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/json',
          'X-Requested-With': 'XMLHttpRequest'
        }
      });
    } else {
      return req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': req.headers.get('Content-Type') || 'application/json',
          Accept: 'application/json',
          'X-Requested-With': 'XMLHttpRequest'
        }
      });
    }
  }
}
