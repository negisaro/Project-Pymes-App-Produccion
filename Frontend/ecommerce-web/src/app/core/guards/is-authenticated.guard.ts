import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';
import { AuthStatus } from '../../features/auth/interfaces';
import { Observable, of, map, catchError, filter, take, timeout } from 'rxjs';

export const isAuthenticatedGuard: CanActivateFn = (): Observable<boolean> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Obtener el estado actual usando el computed signal
  const currentStatus = authService.authStatus();

  switch(currentStatus) {
    case AuthStatus.authenticated:
      // Ya autenticado, permitir acceso
      return of(true);

    case AuthStatus.notAuthenticated:
      // No autenticado, redirigir a login
      router.navigateByUrl('/auth/login');
      return of(false);

    case AuthStatus.checking:
      // Esperando verificación, usar el observable para suscribirse a cambios
      return authService.authStatus$.pipe(
        filter(status => status !== AuthStatus.checking),
        take(1),
        timeout(10000), // Timeout de 10 segundos
        map(status => {
          const isAuth = status === AuthStatus.authenticated;
          if (!isAuth) {
            router.navigateByUrl('/auth/login');
          }
          return isAuth;
        }),
        catchError((error) => {
          // En caso de timeout o error, asumir no autenticado
          console.warn('[IsAuthenticatedGuard] Error esperando verificación de auth:', error);
          router.navigateByUrl('/auth/login');
          return of(false);
        })
      );

    default:
      // Estado desconocido, redirigir a login por seguridad
      router.navigateByUrl('/auth/login');
      return of(false);
  }
};
