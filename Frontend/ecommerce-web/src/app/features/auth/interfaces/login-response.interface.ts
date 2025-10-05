import { User } from '../../../core/models/user.model';

export interface LoginResponse {
  user:  User;
  token: string;
}
