export enum RoleName {
  ADMIN = 'ROLE_ADMIN',
  USER = 'ROLE_USER',
  CLIENTE = 'ROLE_CLIENT',
}

export interface Role {
  id: number;
  name: RoleName;
  active: boolean;
}

export interface User {
  id: number;
  name: string;
  lastname: string;
  username: string;
  password: string;
  email: string;
  roles: Role[]; // Ahora es un array de objetos Role para reflejar la respuesta del backend
  active: boolean;
}

/**
 * Utilidad para verificar si un usuario tiene un rol específico
 */
export function hasRole(user: User, role: RoleName): boolean {
  return user.roles.some((r) => r.name === role);
}
