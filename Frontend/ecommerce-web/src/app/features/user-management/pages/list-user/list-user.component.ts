
import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Observable, BehaviorSubject, Subject, takeUntil, filter } from 'rxjs';
import { User, RoleName, hasRole } from '../../../../core/models/user.model';
import { UserService, Paginator } from '../../services/user.service';
import { AuthService } from '../../../auth/services/auth.service';
import { AuthStatus } from '../../../auth/interfaces';
import Swal from 'sweetalert2';


@Component({
  selector: 'user-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css'],
})

export class ListUserComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private authService = inject(AuthService);

  users$: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);
  paginator$: BehaviorSubject<Paginator | null> = new BehaviorSubject<Paginator | null>(null);
  loading = false;
  errorMsg = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
  // toastr eliminado, solo Swal
  ) {}

  ngOnInit(): void {
    // Esperar a que la autenticación esté lista antes de cargar datos
    this.authService.authStatus$.pipe(
      filter(status => status !== AuthStatus.checking),
      takeUntil(this.destroy$),
      filter(() => this.authService.isAuthenticated()) // Solo continuar si está autenticado
    ).subscribe(() => {
      this.route.paramMap.subscribe((params) => {
        const page = +(params.get('page') || '0');
        this.loadUsers(page);
      });
    });

    // ✅ AUTO-REFRESH: Detectar cuando se regresa a esta página
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      filter((event: NavigationEnd) => event.url.includes('/admin/dashboard-admin/user')),
      filter(() => this.authService.isAuthenticated()), // Solo si está autenticado
      takeUntil(this.destroy$)
    ).subscribe(() => {
      console.log('[AUTO-REFRESH] Detectada navegación a usuarios, recargando...');
      this.loadUsers(0);
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadUsers(page: number): void {
    this.loading = true;
    this.errorMsg = '';
    this.userService.getPageable(page, 5, 'id').subscribe({
      next: (res: Paginator) => {
        this.users$.next(res.content);
        this.paginator$.next(res);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMsg = 'No se pudo cargar la lista de usuarios.';
      },
    });
  }

  puedeAgregarUsuario(): boolean {
    const user = this.authService.getCurrentUser();
    return !!user && hasRole(user, RoleName.ADMIN);
  }


  irAgregarUsuario(): void {
    this.router.navigate(['add-user'], { relativeTo: this.route });
  }

  onDeleteUser(id: number): void {
    Swal.fire({
      title: '¿Seguro que quiere eliminar este usuario?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.deleteUserById(id).subscribe({
          next: (ok) => {
            this.loadUsers(this.paginator$.value?.number || 0);
            if (ok) {
              this.showSwalToast('Usuario eliminado con éxito', 'success');
            } else {
              this.showSwalToast('No se pudo eliminar el usuario.', 'error');
            }
          },
          error: () => {
            this.errorMsg = 'No se pudo eliminar el usuario.';
            this.showSwalToast('No se pudo eliminar el usuario.', 'error');
          },
        });
      }
    });
  }

  private showSwalToast(message: string, icon: 'success' | 'error' | 'info' | 'warning') {
    Swal.fire({
      toast: true,
      position: 'top-end',
      icon,
      title: message,
      showConfirmButton: false,
      timer: 2000,
      timerProgressBar: true
    });
  }


  goToEditUser(id: number): void {
    this.router.navigate(['edit-user', id], { relativeTo: this.route });
  }


  goToPage(page: number): void {
    const paginator = this.paginator$.value;
    if (!paginator) return;
    if (page >= 0 && page < paginator.totalPages && page !== paginator.number) {
      this.router.navigate(['list-user', { page }], { relativeTo: this.route });
    }
  }

  getRoleLabel(role: string): string {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'Administrador';
      case 'ROLE_USER':
        return 'Usuario';
      case 'ROLE_CLIENT':
        return 'Cliente';
      default:
        return 'Usuario';
    }
  }

  get dynamicPages(): number[] {
    const paginator = this.paginator$.value;
    if (!paginator) return [];
    const total = paginator.totalPages;
    const current = paginator.number;
    const delta = 2;
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
}
