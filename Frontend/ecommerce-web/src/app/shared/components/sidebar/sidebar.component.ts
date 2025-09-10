import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';
@Component({
  selector: 'shared-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  collapsed = false;
  private authService = inject(AuthService);
  private router = inject(Router);
  user = computed(() => this.authService.currentUser());

  getRoleLabel(role?: string): string {
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrador';
      case 'ROLE_USER': return 'Usuario';
      case 'ROLE_SUPERVISOR': return 'Supervisor';
      case 'ROLE_EMPLEADO': return 'Empleado';
      default: return 'Usuario';
    }
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
