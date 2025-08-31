  import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { User } from '../../interfaces/user.interface';
import { UserService } from '../../services/user.service';

interface Paginator {
  content: User[];
  number: number;
  totalPages: number;
  size: number;
  totalElements: number;
}

@Component({
  selector: 'user-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css'],
})
export class ListUserComponent implements OnInit {
  users: User[] = [];
  paginator: Paginator = {
    content: [],
    number: 0,
    totalPages: 0,
    size: 10,
    totalElements: 0
  };
  loading = false;
  errorMsg = '';

  getRoleLabel(role: string): string {
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrador';
      case 'ROLE_USER': return 'Usuario';
      case 'ROLE_SUPERVISOR': return 'Supervisor';
      case 'ROLE_EMPLEADO': return 'Empleado';
      default: return role;
    }
  }

  get dynamicPages(): number[] {
    const total = this.paginator.totalPages;
    const current = this.paginator.number;
    const delta = 2; // páginas a mostrar a cada lado
    let start = Math.max(0, current - delta);
    let end = Math.min(total - 1, current + delta);
    if (current <= delta) {
      end = Math.min(total - 1, 2 * delta);
    }
    if (current + delta >= total) {
      start = Math.max(0, total - 1 - 2 * delta);
    }
    const pages: number[] = [];
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  constructor(
    private userService: UserService,
    public router: Router,
    private route: ActivatedRoute
  ) {}

  // Método para mostrar el botón solo a admin
  public puedeAgregarUsuario(): boolean {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      console.log('No hay usuario en localStorage');
      return false;
    }
    try {
      const user = JSON.parse(userStr);
      console.log('Usuario en localStorage:', user);
      console.log('Roles:', user.roles);
      const esAdmin = user.roles?.some((r: any) => r.name === 'ROLE_ADMIN');
      console.log('¿Es admin?', esAdmin);
      return esAdmin;
    } catch (e) {
      console.log('Error parseando usuario:', e);
      return false;
    }
  }

  public irAgregarUsuario(): void {
    this.router.navigate(['/dashboard/user/add-user']);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const page = +(params.get('page') || '0');
      this.getUsers(page);
    });
  }

  getUsers(page: number): void {
    this.loading = true;
    this.errorMsg = '';
    this.userService.getPageable(page).subscribe({
      next: (res: Paginator) => {
        this.users = res.content;
        this.paginator = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = 'No se pudo cargar la lista de usuarios.';
        Swal.fire('Error', this.errorMsg, 'error');
      }
    });
  }

  onDeleteUser(id: number): void {
    Swal.fire({
      title: '¿Seguro que quiere eliminar?',
      text: 'Cuidado, el usuario será eliminado del sistema.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then(result => {
      if (result.isConfirmed) {
        this.userService.deleteUserById(id).subscribe({
          next: () => {
            Swal.fire({
              title: 'Eliminado',
              text: 'Usuario eliminado con éxito.',
              icon: 'success',
              timer: 1200,
              showConfirmButton: false,
            });
            this.getUsers(this.paginator.number);
          },
          error: () => {
            Swal.fire('Error', 'No se pudo eliminar el usuario.', 'error');
          }
        });
      }
    });
  }

  goToEditUser(id: number): void {
    this.router.navigate(['/dashboard/user/edit-user', id]);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.paginator.totalPages && page !== this.paginator.number) {
      this.router.navigate(['/dashboard/user/list-user', { page }]);
    }
  }
}
