import { User } from '../../user/interfaces/user.interface';

export interface LoginResponse {
  user:  User;
  token: string;
}
