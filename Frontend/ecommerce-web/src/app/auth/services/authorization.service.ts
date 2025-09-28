import { Injectable } from '@angular/core';
import { User, RoleName } from '../../user/interfaces/user.interface';

@Injectable({ providedIn: 'root' })
export class AuthorizationService {
  /** Verifica si el usuario tiene al menos uno de los roles indicados */
  hasRole(user: User | null, roles: string | string[]): boolean {
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
}
