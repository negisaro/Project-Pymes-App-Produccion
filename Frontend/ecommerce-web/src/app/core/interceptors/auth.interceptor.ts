import { Injectable, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private authService = inject(AuthService);
  private router = inject(Router);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Este interceptor se enfoca en la navegación automática cuando hay errores de auth
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el token es inválido (401), hacer logout automático y redirigir
        if (error.status === 401) {
          const token = this.authService.getToken();
          if (token && !req.url.includes('/auth/login') && !req.url.includes('/auth/refresh')) {
            console.warn('[AuthInterceptor] Token inválido detectado, cerrando sesión');
            this.authService.logout();
            this.router.navigateByUrl('/auth/login');
          }
        }

        // Si es 403 (Forbidden), mostrar mensaje pero no hacer logout automático
        if (error.status === 403) {
          console.warn('[AuthInterceptor] Acceso prohibido - rol insuficiente');
        }

        return throwError(() => error);
      })
    );
  }
}
