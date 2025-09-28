export interface LoginDto {
  username: string;
  password: string;
}

export interface LoginResponseDto {
  usuario: any; // Puedes importar User si lo prefieres
  token: string;
}

export interface ForgotPasswordRequestDto {
  email: string;
}

export interface ResetPasswordRequestDto {
  token: string;
  newPassword: string;
}
