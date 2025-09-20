import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, catchError, map, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Role, RoleName, User } from '../../user/interfaces/user.interface';
import { AuthStatus } from '../interfaces';

// Convierte string[] a Role[]
function toRoleArray(roles: string[]): Role[] {
  return roles
    .map(role => {
      switch (role) {
        case 'ROLE_ADMIN': return { name: RoleName.ADMIN };
        case 'ROLE_USER': return { name: RoleName.USER };
        case 'ROLE_CLIENT': return { name: RoleName.CLIENTE };
        
        default: return undefined;
      }
    })
    .filter((r): r is Role => r !== undefined);
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = environment.baseUrl;
  private http = inject(HttpClient);

  private _currentUser = signal<User | null>(null);
  private _authStatus = signal<AuthStatus>(AuthStatus.checking);

  public currentUser = computed(() => this._currentUser());
  public authStatus = computed(() => this._authStatus());

  constructor() {
    // Verifica el estado de autenticación al iniciar el servicio
    this.checkAuthStatus().subscribe();
  }

  /** Guarda usuario y token en memoria y localStorage */
  private setAuthentication(user: User, token: string): boolean {
    this._currentUser.set(user);
    this._authStatus.set(AuthStatus.authenticated);
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    return true;
  }

  /** Inicia sesión y guarda usuario/token si es exitoso */
  login(username: string, password: string): Observable<boolean> {
    const url = `${this.baseUrl}/api/auth/login`;
    const body = { username, password };
    return this.http.post<any>(url, body).pipe(
      map((res) => {
        // LOG: Mostrar la respuesta cruda del backend
        console.log('[AuthService] Respuesta cruda del backend (login):', res);
        // Permite ambos formatos: {usuario, token} o DTO plano
        const usuario = res.usuario || res;
        const token = res.token || res.token;
        console.log('[AuthService] usuario extraído:', usuario);
        console.log('[AuthService] token extraído:', token);
        if (!usuario || !token) {
          console.warn('[AuthService] usuario o token vacío:', usuario, token);
          return false;
        }
        // Mapeo seguro del usuario (acepta objetos y strings)
        const roles: Role[] = Array.isArray(usuario.roles)
          ? usuario.roles.map((r: any) => {
              if (typeof r === 'string') {
                // Si es string, usar función toRoleArray
                const arr = toRoleArray([r]);
                return arr.length > 0 ? arr[0] : undefined;
              } else {
                // Si es objeto, mapear normalmente
                return {
                  id: Number(r.id),
                  name: r.name,
                  active: r.active ?? true
                };
              }
            }).filter((r: unknown): r is Role => r !== undefined)
          : [];
        const user: User = {
          id: Number(usuario.id),
          name: usuario.name,
          lastname: usuario.lastname,
          email: usuario.email || '',
          username: usuario.username,
          password: '',
          active: usuario.active ?? true,
          roles
        };
        console.log('[AuthService] user mapeado:', user);
        return this.setAuthentication(user, token);
      }),
      catchError((err) => {
        this.logout();
        return throwError(() => new Error(err?.error?.message || 'Error de autenticación'));
      })
    );
  }

  /** Verifica el estado de autenticación usando el token almacenado */
  checkAuthStatus(): Observable<boolean> {
    const url = `${this.baseUrl}/api/auth/check-token`;
    const token = localStorage.getItem('token');
    console.log('[AuthService] checkAuthStatus: token en localStorage:', token);
    if (!token) {
      console.warn('[AuthService] No hay token en localStorage, cerrando sesión');
      this.logout();
      return of(false);
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(url, { headers }).pipe(
      map((res) => {
        console.log('[AuthService] Respuesta de /auth/check-token:', res);
        const usuario = res.usuario;
        const newToken = res.token || token;
        if (!usuario || !newToken) {
          console.warn('[AuthService] Usuario o token inválido en respuesta:', usuario, newToken);
          this.logout();
          this._authStatus.set(AuthStatus.notAuthenticated);
          return false;
        }
        const roles: Role[] = Array.isArray(usuario.roles)
          ? usuario.roles.map((r: any) => {
              if (typeof r === 'string') {
                const arr = toRoleArray([r]);
                return arr.length > 0 ? arr[0] : undefined;
              } else {
                return {
                  id: Number(r.id),
                  name: r.name,
                  active: r.active ?? true
                };
              }
            }).filter((r: unknown): r is Role => r !== undefined)
          : [];
        const user: User = {
          id: Number(usuario.id),
          name: usuario.name,
          lastname: usuario.lastname,
          email: usuario.email || '',
          username: usuario.username,
          password: '',
          active: usuario.active ?? true,
          roles
        };
        console.log('[AuthService] Usuario restaurado tras recarga:', user);
        return this.setAuthentication(user, newToken);
      }),
      catchError((err) => {
        console.error('[AuthService] Error en /auth/check-token:', err);
        this._authStatus.set(AuthStatus.notAuthenticated);
        this.logout();
        return of(false);
      })
    );
  }

  /** Envía email para recuperación de contraseña */
  sendResetPasswordEmail(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/auth/forgot-password`, { email }).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.mensaje || err?.error?.message || 'Error enviando email de recuperación')))
    );
  }

  /** Registra un nuevo usuario */
  register(data: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/api/usuarios/register`, data).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.message || 'Error de registro')))
    );
  }

  /** Restablece la contraseña usando token */
  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/auth/reset-password`, { token, newPassword }).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.mensaje || err?.error?.message || 'Error al restablecer contraseña')))
    );
  }

  /** Cierra sesión limpiando usuario y token */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this._currentUser.set(null);
    this._authStatus.set(AuthStatus.notAuthenticated);
  }

  /** Devuelve el usuario actual (sin signals) */
  getCurrentUser(): User | null {
    const user = this._currentUser();
    if (user) return user;
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
    return null;
  }

  /** Devuelve true si el usuario tiene al menos uno de los roles indicados */
  hasRole(roles: string | string[]): boolean {
    const user = this.getCurrentUser();
    if (!user || !user.roles) return false;
    const toRoleName = (role: string): RoleName | undefined => {
      switch (role) {
        case 'ROLE_ADMIN': return RoleName.ADMIN;
        case 'ROLE_USER': return RoleName.USER;
        case 'ROLE_CLIENT': return RoleName.CLIENTE;
        default: return undefined;
      }
    };
    const rolesArr = Array.isArray(roles) ? roles.map(toRoleName) : [toRoleName(roles)];
    return rolesArr.some(r => r !== undefined && user.roles.some(roleObj => roleObj.name === r));
  }

  /** Devuelve si el usuario está autenticado */
  isAuthenticated(): boolean {
    return this._authStatus() === AuthStatus.authenticated && !!this._currentUser();
  }

  /** Devuelve el token actual */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /** Obtiene los roles disponibles desde el backend */
  getRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/api/roles`).pipe(
      catchError(() => of(['USER', 'ADMIN', 'CLIENTE']))
    );
  }
}
