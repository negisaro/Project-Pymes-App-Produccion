import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class IsNotAuthenticatedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(_route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    // Si NO está autenticado, permitir acceso a la ruta de auth (login / register)
    if (!this.authService.isAuthenticated()) return true;

    // Ya autenticado: redirigir según rol
    const user = this.authService.getCurrentUser();
    const roles = user?.roles?.map(r => r.name) || [];
    if (roles.some(r => /ADMIN$/.test(r))) {
      return this.router.createUrlTree(['/admin']);
    }
    if (roles.some(r => /(CLIENT|CLIENTE|USER)$/.test(r))) {
      return this.router.createUrlTree(['/cliente']);
    }
    // Fallback: home pública.
    return this.router.createUrlTree(['/']);
  }
}
