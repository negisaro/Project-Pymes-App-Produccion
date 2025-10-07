import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Role } from '../../core/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private readonly baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene la lista de roles disponibles desde el endpoint público
   * @returns Observable<Role[]> Lista de roles
   */
  getRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.baseUrl}/api/public/roles/list`).pipe(
      catchError((error) => {
        console.error('[RoleService] Error al obtener roles:', error);
        // Fallback con roles por defecto en caso de error
        return of([
          { id: 1, name: 'ROLE_ADMIN' as any, active: true },
          { id: 2, name: 'ROLE_USER' as any, active: true },
          { id: 3, name: 'ROLE_CLIENT' as any, active: true }
        ]);
      })
    );
  }
}