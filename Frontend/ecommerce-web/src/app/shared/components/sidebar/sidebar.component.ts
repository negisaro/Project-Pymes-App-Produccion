import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'shared-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  collapsed = false;
  private authService = inject(AuthService);
  private router = inject(Router);
  user$: Observable<any> = this.authService.currentUser$;

  getRoleLabel(role?: string): string {
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrador';
      case 'ROLE_USER': return 'Usuario';
      case 'ROLE_CLIENT': return 'Cliente';
      default: return 'Usuario';
    }
  }

  isAdmin(u: any): boolean {
    return !!u && Array.isArray(u.roles) && u.roles.some((r: { name: string }) => r.name === 'ROLE_ADMIN');
  }

  isCliente(u: any): boolean {
    return !!u && Array.isArray(u.roles) && !u.roles.some((r: { name: string }) => r.name === 'ROLE_ADMIN');
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
