import { Component, computed, inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'shared-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  userSignal = computed(() => this.authService.currentUser());
  user$ = toObservable(this.userSignal);
  notificationsCount = 0; // Simulación, puedes conectar a un servicio real

  getRoleLabel(role?: string): string {
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrador';
      case 'ROLE_USER': return 'Usuario';
      case 'ROLE_SUPERVISOR': return 'Supervisor';
      case 'ROLE_EMPLEADO': return 'Empleado';
      default: return 'Usuario';
    }
  }

  isAdmin(user?: any): boolean {
    const u = user ?? this.userSignal();
    return !!u?.roles?.some((r: any) => r.name === 'ROLE_ADMIN');
  }
  isSupervisor(user?: any): boolean {
    const u = user ?? this.userSignal();
    return !!u?.roles?.some((r: any) => r.name === 'ROLE_SUPERVISOR');
  }
  isEmpleado(user?: any): boolean {
    const u = user ?? this.userSignal();
    return !!u?.roles?.some((r: any) => r.name === 'ROLE_EMPLEADO');
  }

  async logout() {
    const result = await Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#1976d2',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
    });
    if (result.isConfirmed) {
      this.authService.logout();
      this.router.navigate(['/auth/login']);
    }
  }
}
