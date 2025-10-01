import { Injectable } from '@angular/core';
import { Route, PreloadingStrategy } from '@angular/router';
import { Observable, of } from 'rxjs';

/**
 * Estrategia de pre-carga selectiva: sólo precarga módulos que tengan data.preload = true.
 * Permite marcar dinámicamente en rutas clave (ej: search, auth) para mejorar navegación temprana.
 */
@Injectable({ providedIn: 'root' })
export class PreloadSelectedModulesStrategy implements PreloadingStrategy {
  preload(route: Route, load: () => Observable<any>): Observable<any> {
    if (route.data && route.data['preload']) {
      return load();
    }
    return of(null);
  }
}
