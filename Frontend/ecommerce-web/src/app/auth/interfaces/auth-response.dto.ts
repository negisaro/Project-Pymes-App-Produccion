export interface AuthResponseDTO {
  token: string;
  id: string;
  name: string;
  lastname: string;
  email: string;
  username: string;
  active: boolean;
  roles: string[];
  admin: boolean;
  message: string;
}
