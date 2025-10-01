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
    const fallback: string = route.data['fallback'] || '/';

    // Si no hay restricción de roles, permitir el acceso directo
    if (!expectedRoles.length) return of(true);

    // Esperar a que el estado de auth esté resuelto (el IsAuthenticatedGuard debería haber corrido antes, pero mantenemos robustez)
    return this.authService.authStatus$.pipe(
      take(1),
      switchMap(status => {
        if (status !== 'authenticated') {
          return of(this.router.createUrlTree(['/auth/login']));
        }
        return this.authService.currentUser$.pipe(
          take(1),
            map(user => {
              if (!user) return this.router.createUrlTree(['/auth/login']);
              const userRoles = (user.roles || []).map(r => r.name);
              if (this.matchesAnyRole(expectedRoles, userRoles)) {
                return true;
              }
              return this.router.createUrlTree([fallback]);
            })
        );
      })
    );
  }

  /**
   * Compara roles de forma tolerante (acepta ROLE_ADMIN / ADMIN, case-insensitive)
   */
  private matchesAnyRole(expected: string[], userRoles: string[]): boolean {
    const norm = (r: string) => r.replace(/^ROLE_/i, '').toUpperCase();
    const expectedSet = new Set(expected.map(norm));
    return userRoles.some(r => expectedSet.has(norm(r)));
  }
}
