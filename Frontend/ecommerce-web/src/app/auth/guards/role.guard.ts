import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable, of } from 'rxjs';
import { map, switchMap, take } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    const expectedRoles: string[] = route.data['roles'] || [];
    // Esperar a que el usuario esté restaurado y autenticado
    return this.authService.authStatus$.pipe(
      take(1),
      switchMap(status => {
        if (status !== 'authenticated') {
          // Si no está autenticado, redirigir a login
          return of(this.router.createUrlTree(['/auth/login']));
        }
        // Usuario restaurado, validar roles
        return this.authService.currentUser$.pipe(
          take(1),
          map(user => {
            if (user && this.authService.hasRole(expectedRoles)) {
              return true;
            }
            // Si no tiene rol, redirigir a unauthorized
            return this.router.createUrlTree(['/unauthorized']);
          })
        );
      })
    );
  }
}
