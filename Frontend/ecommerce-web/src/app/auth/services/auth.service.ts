import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, catchError, map, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Role, RoleName, User } from '../../user/interfaces/user.interface';
import { AuthStatus } from '../interfaces';
import { AuthResponseDTO } from '../interfaces/auth-response.dto';
// Convierte string[] a Role[]
function toRoleArray(roles: string[]): Role[] {
  return roles
    .map(role => {
      switch (role) {
        case 'ROLE_ADMIN': return { name: RoleName.ADMIN };
        case 'ROLE_USER': return { name: RoleName.USER };
        case 'ROLE_SUPERVISOR': return { name: RoleName.SUPERVISOR };
        case 'ROLE_EMPLEADO': return { name: RoleName.EMPLEADO };
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

  /**
   * Computed: usuario autenticado actual
   */
  public currentUser = computed(() => this._currentUser());
  /**
   * Computed: estado de autenticación
   */
  public authStatus = computed(() => this._authStatus());

  constructor() {
    // Verifica el estado de autenticación al iniciar el servicio
    this.checkAuthStatus().subscribe();
  }

  /**
   * Guarda usuario y token en memoria y localStorage
   */
  private setAuthentication(user: User, token: string): boolean {
    console.log('[AuthService] setAuthentication()');
    console.log('Usuario recibido del backend:', user);
    if (!user || !user.name || !user.lastname || !user.roles) {
      console.warn('[AuthService] El usuario recibido está incompleto:', user);
    }
    this._currentUser.set(user);
    this._authStatus.set(AuthStatus.authenticated);
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    console.log('Usuario guardado en localStorage:', JSON.parse(localStorage.getItem('user') || 'null'));
    console.log('AuthStatus actual:', this._authStatus());
    return true;
  }

  /**
   * Inicia sesión y guarda usuario/token si es exitoso
   */
  login(username: string, password: string): Observable<boolean> {
    const url = `${this.baseUrl}/api/auth/login`;
    const body = { username, password };
    console.log('[AuthService] login()');
    console.log('URL:', url);
    console.log('Body:', body);
    return this.http.post<AuthResponseDTO>(url, body).pipe(
      map((res) => {
        console.log('[AuthService] Respuesta DTO del backend (login):', res);
        // El backend envía un objeto AuthResponseDTO
        let roles: Role[] = [];
        if (Array.isArray(res.roles) && res.roles.length > 0 && typeof res.roles[0] === 'object') {
          // El backend envía array de objetos Role
          roles = res.roles.map((r: any) => ({
            id: Number(r.id),
            name: r.name,
            active: r.active ?? true
          }));
        } else if (Array.isArray(res.roles)) {
          // El backend envía array de strings
          roles = toRoleArray(res.roles);
        }
          const user: User = {
            id: Number(res.id),
            name: res.name,
            lastname: res.lastname,
            email: res.email || '',
            username: res.username,
            password: '', // Nunca guardar el password
            active: res.active ?? true,
            roles
          };
        let token = res.token || '';
        if (token && token.startsWith('Bearer ')) token = token.replace('Bearer ', '');
        console.log('Token extraído:', token);
        return this.setAuthentication(user, token);
      }),
      catchError((err) => {
        console.error('[AuthService] Error en login:', err);
        const msg = err?.error?.message || 'Error de autenticación';
        return throwError(() => new Error(msg));
      })
    );
  }

  /**
   * Verifica el estado de autenticación usando el token almacenado
   */
  checkAuthStatus(): Observable<boolean> {
    const url = `${this.baseUrl}/auth/check-token`;
    const token = localStorage.getItem('token');
    console.log('[AuthService] checkAuthStatus()');
    console.log('URL:', url);
    console.log('Token en localStorage:', token);
    if (!token) {
      console.log('No hay token, cerrando sesión.');
      this.logout();
      return of(false);
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(url, { headers, observe: 'response' }).pipe(
      map((response) => {
        const users = response.body;
        const user = Array.isArray(users) && users.length > 0 ? users[0] : null;
        let newToken = response.headers.get('Authorization') || (user && user.token) || token;
        if (newToken && newToken.startsWith('Bearer ')) newToken = newToken.replace('Bearer ', '');
        console.log('Respuesta de check-token:', users);
        console.log('Usuario extraído:', user);
        console.log('Nuevo token:', newToken);
        return this.setAuthentication(user, newToken);
      }),
      catchError((err) => {
        console.error('[AuthService] Error en checkAuthStatus:', err);
        this._authStatus.set(AuthStatus.notAuthenticated);
        localStorage.removeItem('token');
        return of(false);
      })
    );
  }

  /**
   * Envía email para recuperación de contraseña
   */
  sendResetPasswordEmail(username: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/forgot-password`, { username }).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.message || 'Error enviando email de recuperación')))
    );
  }

  /**
   * Registra un nuevo usuario
   */
  register(data: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/api/usuarios/create`, data).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.message || 'Error de registro')))
    );
  }

  /**
   * Restablece la contraseña usando token
   */
  resetPassword(token: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/reset-password`, { token, password }).pipe(
      catchError((err) => throwError(() => new Error(err?.error?.message || 'Error al restablecer contraseña')))
    );
  }

  /**
   * Cierra sesión limpiando usuario y token
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this._currentUser.set(null);
    this._authStatus.set(AuthStatus.notAuthenticated);
  }
  /**
   * Devuelve el usuario actual (sin signals)
   */
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

  /**
   * Devuelve true si el usuario tiene al menos uno de los roles indicados
   */
  hasRole(roles: string | string[]): boolean {
    const user = this.getCurrentUser();
    if (!user || !user.roles) return false;
    const toRoleName = (role: string): RoleName | undefined => {
      switch (role) {
        case 'ROLE_ADMIN': return RoleName.ADMIN;
        case 'ROLE_USER': return RoleName.USER;
        case 'ROLE_SUPERVISOR': return RoleName.SUPERVISOR;
        case 'ROLE_EMPLEADO': return RoleName.EMPLEADO;
        default: return undefined;
      }
    };
    const rolesArr = Array.isArray(roles) ? roles.map(toRoleName) : [toRoleName(roles)];
    return rolesArr.some(r => r !== undefined && user.roles.some(roleObj => roleObj.name === r));
  }

  /**
   * Devuelve si el usuario está autenticado
   */
  isAuthenticated(): boolean {
    return this._authStatus() === AuthStatus.authenticated && !!this._currentUser();
  }

  /**
   * Devuelve el token actual
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Obtiene los roles disponibles desde el backend
   */
  getRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/api/roles`).pipe(
      catchError(() => of(['USER', 'ADMIN', 'SUPERVISOR', 'EMPLEADO']))
    );
  }
}


