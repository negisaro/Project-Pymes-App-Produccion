// Convierte un array de strings a un array de objetos Role
import { Role, RoleName } from '../../user/interfaces/user.interface';

export function toRoleArray(roles: string[]): Role[] {
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
