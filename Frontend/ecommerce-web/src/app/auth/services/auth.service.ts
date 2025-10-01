import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, throwError, Subscription, BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Role, RoleName, User } from '../../user/interfaces/user.interface';
import { AuthStatus } from '../interfaces';
import { LoginDto, LoginResponseDto, ForgotPasswordRequestDto, ResetPasswordRequestDto } from '../dto/auth.dto';
import { StorageService } from './storage.service';
import { AuthorizationService } from './authorization.service';
import { toRoleArray } from '../util/role.util';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = environment.baseUrl;
  private http = inject(HttpClient);
  private storage = inject(StorageService);
  private authorization = inject(AuthorizationService);

  private _currentUser$ = new BehaviorSubject<User | null>(null);
  private _authStatus$ = new BehaviorSubject<AuthStatus>(AuthStatus.checking);

  /** Observable del usuario actual */
  public currentUser$ = this._currentUser$.asObservable();
  /** Observable del estado de autenticación */
  public authStatus$ = this._authStatus$.asObservable();

  private refreshTimeout: any = null;
  private refreshSubscription: Subscription | null = null;

  constructor() {
    // Restaurar usuario desde StorageService
    const user = this.storage.get<User>('user');
    const token = this.storage.get<string>('token');
    if (user && token) {
      this._currentUser$.next(user);
      this._authStatus$.next(AuthStatus.authenticated);
      this.scheduleTokenRefresh(token);
    } else {
      this._currentUser$.next(null);
      this._authStatus$.next(AuthStatus.notAuthenticated);
    }
    // Validar con backend (mantiene seguridad)
    this.checkAuthStatus().subscribe();
  }

  /** Decodifica un JWT y retorna el payload como objeto */
  /** Decodifica un JWT y retorna el payload como objeto */
  private decodeJwt(token: string): any {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }

  /** Valida si el token está expirado */
  private isTokenExpired(token: string): boolean {
    const payload = this.decodeJwt(token);
    if (!payload || !payload.exp) return true;
    const exp = payload.exp * 1000;
    return Date.now() > exp;
  }

  /** Programa la renovación automática del token antes de que expire */
  private scheduleTokenRefresh(token: string) {
    if (this.refreshTimeout) {
      clearTimeout(this.refreshTimeout);
    }
    const payload = this.decodeJwt(token);
    if (!payload || !payload.exp) return;
    const exp = payload.exp * 1000; // JWT exp en segundos, JS en ms
    const now = Date.now();
    // Refrescar 1 minuto antes de expirar (o a los 10s si ya está por expirar)
    const msToRefresh = Math.max(exp - now - 60000, 10000);
    this.refreshTimeout = setTimeout(() => {
      this.refreshToken(token).subscribe({
        next: (res) => {
          if (res && res.token) {
            this.storage.set('token', res.token);
            this.scheduleTokenRefresh(res.token);
          }
        },
        error: () => {
          this.logout();
        }
      });
    }, msToRefresh);
  }

  /** Guarda usuario y token en memoria y localStorage */
  /**
   * Guarda usuario y token en memoria y almacenamiento local
   * @param user Usuario autenticado
   * @param token JWT
   */
  private setAuthentication(user: User, token: string): boolean {
    this._currentUser$.next(user);
    this._authStatus$.next(AuthStatus.authenticated);
    this.storage.set('token', token);
    this.storage.set('user', user);
    this.scheduleTokenRefresh(token);
    return true;
  }

  /** Inicia sesión y guarda usuario/token si es exitoso */
  /**
   * Inicia sesión y guarda usuario/token si es exitoso
   * @param username
   * @param password
   */
  login(username: string, password: string): Observable<boolean> {
    const url = `${this.baseUrl}/api/public/auth/login`;
    const body = { username, password };
    return this.http.post<any>(url, body, { observe: 'response' }).pipe(
      map((httpRes) => {
        const res = httpRes.body || {};
        console.debug('[AUTH][LOGIN][HTTP_STATUS]', httpRes.status);
        console.debug('[AUTH][LOGIN][HEADERS]', this.headersToObject(httpRes.headers));
        console.debug('[AUTH][LOGIN][RAW_BODY]', res);

        const headerAuth = httpRes.headers.get('Authorization');
        let tokenFromHeader = headerAuth && /Bearer\s+/i.test(headerAuth) ? headerAuth.replace(/Bearer\s+/i,'').trim() : null;
        // Nuevo formato: accessToken en body
        const token = res?.accessToken || res?.token || tokenFromHeader || null;
        if (!token) {
          console.warn('[AUTH][LOGIN] No token presente ni en body ni en header Authorization');
        } else {
          const payload = this.decodeJwt(token);
          console.debug('[AUTH][LOGIN][JWT_PAYLOAD]', payload);
          console.debug('[AUTH][LOGIN][JWT_PAYLOAD.roles]', payload?.roles || payload?.authorities);
        }
        // Nuevo formato: roles y username top-level
        const baseUser = res?.usuario || res?.user || {
          username: res?.username,
          roles: res?.roles,
          email: res?.email,
          name: res?.name,
          lastname: res?.lastname,
          active: res?.active,
          id: res?.id
        };
        console.debug('[AUTH][LOGIN][USUARIO_RESUELTO]', baseUser);
        console.debug('[AUTH][LOGIN][USUARIO_RESUELTO.roles]', baseUser?.roles);
        if (!baseUser || !token) {
          return false;
        }

        const rawRoles = baseUser.roles || baseUser.authorities || [];
        const roles: Role[] = Array.isArray(rawRoles)
          ? rawRoles.map((r: any) => {
              if (typeof r === 'string') {
                const arr = toRoleArray([r]);
                return arr.length > 0 ? arr[0] : undefined;
              } else {
                return {
                  id: Number(r.id) || 0,
                  name: r.name || r.authority || '',
                  active: (r.active ?? true)
                } as Role;
              }
            }).filter((r: unknown): r is Role => r !== undefined)
          : [];
        const user: User = {
          id: Number(baseUser.id) || 0,
          name: baseUser.name || '',
          lastname: baseUser.lastname || '',
          email: baseUser.email || '',
            username: baseUser.username || username,
          password: '',
          active: baseUser.active ?? true,
          roles
        };
        return this.setAuthentication(user, token);
      }),
      catchError((err) => this.handleError(err, 'Error de autenticación'))
    );
  }

  /** Convierte HttpHeaders a objeto plano para log */
  private headersToObject(headers: HttpHeaders): Record<string,string> {
    const obj: Record<string,string> = {};
    headers.keys().forEach(k => { obj[k] = headers.get(k)!; });
    return obj;
  }

  /** Verifica el estado de autenticación usando el token almacenado */
  /**
   * Verifica el estado de autenticación usando el token almacenado
   */
  checkAuthStatus(): Observable<boolean> {
    const url = `${this.baseUrl}/api/public/auth/check-token`;
    const token = this.storage.get<string>('token');
    if (!token || this.isTokenExpired(token)) {
      this.logout();
      return of(false);
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(url, { headers }).pipe(
      map((res) => {
        console.debug('[AUTH][CHECK_AUTH][RAW_RESPONSE]', res);
        const newToken = res.accessToken || res.token || token;
        const usuario = res.usuario || res.user || {
          username: res?.username,
          roles: res?.roles,
          email: res?.email,
          name: res?.name,
          lastname: res?.lastname,
          active: res?.active,
          id: res?.id
        };
        console.debug('[AUTH][CHECK_AUTH][USUARIO_RESUELTO]', usuario);
        console.debug('[AUTH][CHECK_AUTH][USUARIO_RESUELTO.roles]', usuario?.roles);
        if (newToken) {
          const payload = this.decodeJwt(newToken);
          console.debug('[AUTH][CHECK_AUTH][JWT_PAYLOAD]', payload);
          console.debug('[AUTH][CHECK_AUTH][JWT_PAYLOAD.roles]', payload?.roles || payload?.authorities);
        }
        if (!usuario || !newToken) {
          this.logout();
          this._authStatus$.next(AuthStatus.notAuthenticated);
          return false;
        }
        const rawRoles = usuario.roles || usuario.authorities || [];
        const roles: Role[] = Array.isArray(rawRoles)
          ? rawRoles.map((r: any) => {
              if (typeof r === 'string') {
                const arr = toRoleArray([r]);
                return arr.length > 0 ? arr[0] : undefined;
              } else {
                return {
                  id: Number(r.id) || 0,
                  name: r.name || r.authority || '',
                  active: r.active ?? true
                } as Role;
              }
            }).filter((r: unknown): r is Role => r !== undefined)
          : [];
        const user: User = {
          id: Number(usuario.id) || 0,
          name: usuario.name || '',
          lastname: usuario.lastname || '',
          email: usuario.email || '',
          username: usuario.username,
          password: '',
          active: usuario.active ?? true,
          roles
        };
        return this.setAuthentication(user, newToken);
      }),
      catchError((err) => this.handleError(err, 'Error de autenticación', false))
    );
  }

  /** Envía email para recuperación de contraseña */
  /**
   * Envía email para recuperación de contraseña
   */
  sendResetPasswordEmail(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/public/auth/forgot-password`, { email }).pipe(
      catchError((err) => this.handleError(err, 'Error enviando email de recuperación'))
    );
  }

  /** Registra un nuevo usuario */
  /**
   * Registra un nuevo usuario
   */
  register(data: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/api/public/auth/register`, data).pipe(
      catchError((err) => this.handleError(err, 'Error de registro'))
    );
  }

  /** Restablece la contraseña usando token */
  /**
   * Restablece la contraseña usando token
   */
  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/public/auth/reset-password`, { token, newPassword }).pipe(
      catchError((err) => this.handleError(err, 'Error al restablecer contraseña'))
    );
  }

  /** Cierra sesión limpiando usuario y token */
  /**
   * Cierra sesión limpiando usuario y token
   */
  logout(): void {
    this.storage.remove('token');
    this.storage.remove('user');
    this._currentUser$.next(null);
    this._authStatus$.next(AuthStatus.notAuthenticated);
    if (this.refreshTimeout) {
      clearTimeout(this.refreshTimeout);
      this.refreshTimeout = null;
    }
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
      this.refreshSubscription = null;
    }
  }

  /** Devuelve el usuario actual (sin signals) */
  /**
   * Devuelve el usuario actual (sin signals)
   */
  getCurrentUser(): User | null {
    return this._currentUser$.getValue();
  }

  /** Devuelve true si el usuario tiene al menos uno de los roles indicados */
  /**
   * Devuelve true si el usuario tiene al menos uno de los roles indicados
   */
  hasRole(roles: string | string[]): boolean {
    return this.authorization.hasRole(this.getCurrentUser(), roles);
  }

  /** Devuelve si el usuario está autenticado */
  /**
   * Devuelve si el usuario está autenticado
   */
  isAuthenticated(): boolean {
    return this._authStatus$.getValue() === AuthStatus.authenticated && !!this._currentUser$.getValue();
  }

  /** Devuelve el token actual */
  /**
   * Devuelve el token actual
   */
  getToken(): string | null {
    return this.storage.get<string>('token');
  }

  /** Actualiza el token en memoria + storage y reprograma el refresh (usado por interceptor) */
  updateToken(newToken: string): void {
    if (!newToken) return;
    this.storage.set('token', newToken);
    this.scheduleTokenRefresh(newToken);
  }

  /** Obtiene los roles disponibles desde el backend */
  /**
   * Obtiene los roles disponibles desde el backend
   */
  getRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/api/segura/roles`).pipe(
      catchError(() => of(['USER', 'ADMIN', 'CLIENTE']))
    );
  }

   /** Refresca el token JWT */
  /**
   * Refresca el token JWT
   */
  refreshToken(token: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      `${this.baseUrl}/api/public/auth/refresh`,
      { token }
    ).pipe(
      catchError((err) => this.handleError(err, 'Error al refrescar token'))
    );
  }

  /** Llama al endpoint de logout del backend (opcional, para trazabilidad) */
  /**
   * Llama al endpoint de logout del backend (opcional, para trazabilidad)
   */
  logoutBackend(): Observable<any> {
    const token = this.getToken();
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;
    return this.http.post(`${this.baseUrl}/api/public/auth/logout`, {}, { headers }).pipe(
      catchError(() => of({ message: 'Sesión cerrada localmente.' }))
    );
  }

  /**
   * Centraliza el manejo de errores para todos los métodos
   */
  private handleError(err: any, defaultMsg: string, logoutOnError = true) {
    if (logoutOnError) this.logout();
    const msg = err?.error?.mensaje || err?.error?.message || err?.error?.error || err?.message || defaultMsg;
    return throwError(() => new Error(msg));
  }

}
