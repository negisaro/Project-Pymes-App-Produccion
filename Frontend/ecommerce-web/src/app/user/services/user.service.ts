
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../../environments/environments';
import { User } from '../interfaces/user.interface';

// DTO para crear/actualizar usuario
export interface UserCreateDto {
  name: string;
  lastname: string;
  username: string;
  password?: string;
  email: string;
  rolesIds: number[];
}

export interface Paginator {
  content: User[];
  number: number;
  totalPages: number;
  size: number;
  totalElements: number;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) {}

  getPageable(
    page: number,
    size: number = 5,
    sort: string = 'id'
  ): Observable<Paginator> {
    return this.http.get<Paginator>(
      `${this.baseUrl}/api/segura/usuarios/list?page=${page}&size=${size}&sort=${sort}`
    );
  }

  addUser(user: UserCreateDto): Observable<User> {
    return this.http.post<User>(
      `${this.baseUrl}/api/segura/usuarios/create`,
      user
    );
  }

  updateUser(id: number, user: UserCreateDto): Observable<User> {
    return this.http.put<User>(
      `${this.baseUrl}/api/segura/usuarios/update/${id}`,
      user
    );
  }

  deleteUserById(id: number): Observable<boolean> {
    return this.http
      .delete(`${this.baseUrl}/api/segura/usuarios/delete/${id}`)
      .pipe(
        catchError((err) => of(false)),
        map((resp) => true)
      );
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/api/segura/usuarios/list/${id}`);
  }
}
