import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../features/auth/services/auth.service';
import Swal from 'sweetalert2';

interface AdminNavItem { icon?: string; label: string; path: string; }

@Component({
  selector: 'app-admin-sidebar',
  templateUrl: './admin-sidebar.component.html',
  styleUrls: ['./admin-sidebar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminSidebarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  sidebarOpen = true;
  items: AdminNavItem[] = [
    // Ajustado: las rutas reales están bajo /admin/dashboard-admin/*
    { label: 'Panel', path: '/admin/dashboard-admin' },
    { label: 'Usuarios', path: '/admin/dashboard-admin/user' },
    { label: 'Productos', path: '/admin/dashboard-admin/product' },
    { label: 'Categorías', path: '/admin/dashboard-admin/categoria' }, // ✅ Apunta a la nueva ruta moderna
  { label: 'Suppliers', path: '/admin/dashboard-admin/suppliers' },
    { label: 'Pedidos', path: '/admin/dashboard-admin/pedido' },
  ];

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  async logout() {
    const result = await Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión de administrador?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      backdrop: true
    });

    if (result.isConfirmed) {
      try {
        this.authService.logout();

        // Mostrar mensaje de éxito
        await Swal.fire({
          title: 'Sesión cerrada',
          text: 'Has cerrado sesión correctamente',
          icon: 'success',
          timer: 1500,
          showConfirmButton: false
        });

        // Redirigir al home
        this.router.navigate(['/']);
      } catch (error) {
        Swal.fire({
          title: 'Error',
          text: 'Hubo un problema al cerrar la sesión',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    }
  }
}
