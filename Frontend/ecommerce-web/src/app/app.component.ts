import { Component, inject, OnInit, OnDestroy, computed, effect } from '@angular/core';
import { Router } from '@angular/router';
import { UiLoaderService } from './shared/services/ui-loader.service';
import { AuthService } from './features/auth/services/auth.service';
import { AuthStatus } from './features/auth/interfaces';
import { BehaviorSubject, Subscription, timer, combineLatest } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  uiLoader = inject(UiLoaderService);
  private authService = inject(AuthService);
  private router = inject(Router);

  private loadingSub?: Subscription;
  private safeLoading$ = new BehaviorSubject<boolean>(true);
  loading$ = this.safeLoading$.asObservable();

  // Computed para verificar si la autenticación ha terminado de cargar
  public finishedAuthCheck = computed<boolean>(() => {
    const status = this.authService.authStatus();
    // Solo está "checking" durante la inicialización
    return status !== AuthStatus.checking;
  });

  // Effect para manejar cambios en el estado de autenticación
  public authStatusChangedEffect = effect(() => {
    const status = this.authService.authStatus();

    switch(status) {
      case AuthStatus.checking:
        // Mostrar loader o mantener estado actual
        console.log('[AppComponent] Verificando autenticación...');
        break;

      case AuthStatus.authenticated:
        // Usuario autenticado, redirigir si está en auth
        console.log('[AppComponent] Usuario autenticado');
        if (this.router.url.includes('/auth')) {
          this.router.navigateByUrl('/admin');
        }
        break;

      case AuthStatus.notAuthenticated:
        // Usuario no autenticado, permitir navegación libre (no redirigir a login)
        console.log('[AppComponent] Usuario no autenticado');
        break;
    }
  });

  ngOnInit() {
    // Combina el loading real con un timeout de seguridad (8s)
    this.loadingSub = combineLatest([
      this.uiLoader.loading$,
      timer(0, 1000)
    ]).subscribe(([realLoading, t]) => {
      if (!realLoading) {
        this.safeLoading$.next(false);
      } else if (t > 8) { // 8 segundos máximo
        this.safeLoading$.next(false);
      } else {
        this.safeLoading$.next(true);
      }
    });
  }

  ngOnDestroy() {
    this.loadingSub?.unsubscribe();
  }
}
