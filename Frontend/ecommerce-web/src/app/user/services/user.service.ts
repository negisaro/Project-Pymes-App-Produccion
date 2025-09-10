import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../../environments/environments';
import { User } from '../interfaces/user.interface';

export interface Paginator {
  content: User[];
  number: number;
  totalPages: number;
  size: number;
  totalElements: number;
}


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) { }

  getUser(): Observable<User[]>{
    return this.http.get<User[]>(`${this.baseUrl}/api/segura/usuarios/list`)
  }

  getPageable(page: number): Observable<Paginator> {
    return this.http.get<Paginator>(`${this.baseUrl}/api/segura/usuarios/page/${page}`);
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/api/segura/usuarios/create`, user);
  }

  updateUser(user: User): Observable<User> {
    if (!user.id) throw Error('Usuario requerido');
    return this.http.put<User>(`${this.baseUrl}/api/segura/usuarios/update/${user.id}`, user);
  }

  deleteUserById(id: number): Observable<boolean> {
    return this.http.delete(`${this.baseUrl}/api/segura/usuarios/delete/${id}`).pipe(
      catchError((err) => of(false)),
      map((resp) => true)
    );
  }
}
