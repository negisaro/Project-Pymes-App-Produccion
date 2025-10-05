import { Injectable } from '@angular/core';
import { User, RoleName } from '../../../core/models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthorizationService {
  /** Verifica si el usuario tiene al menos uno de los roles indicados */
  hasRole(user: User | null, roles: string | string[]): boolean {
    if (!user || !user.roles) return false;
    
    // Convertir roles a array si es string
    const rolesArray = Array.isArray(roles) ? roles : [roles];
    
    // Verificar si el usuario tiene alguno de los roles solicitados
    return rolesArray.some((role: string) => 
      user.roles.some((roleObj: any) => roleObj.name === role));
  }
}
