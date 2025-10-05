import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class IsAuthenticatedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    // Si ya está autenticado en memoria, permite acceso inmediato
    if (this.authService.isAuthenticated()) {
      return of(true);
    }
    // Verifica el token en localStorage y con el backend
    return this.authService.checkAuthStatus().pipe(
      map(isAuth => {
        if (isAuth) {
          return true;
        } else {
          return this.router.createUrlTree(['/auth/login']);
        }
      }),
      catchError(() => of(this.router.createUrlTree(['/auth/login'])))
    );
  }
}
