import { ChangeDetectionStrategy, Component } from '@angular/core';

interface AdminNavItem { icon?: string; label: string; path: string; }

@Component({
  selector: 'app-admin-sidebar',
  templateUrl: './admin-sidebar.component.html',
  styleUrls: ['./admin-sidebar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminSidebarComponent {
  sidebarOpen = true;
  items: AdminNavItem[] = [
    // Ajustado: las rutas reales están bajo /admin/dashboard-admin/*
    { label: 'Panel', path: '/admin/dashboard-admin' },
    { label: 'Usuarios', path: '/admin/dashboard-admin/user' },
    { label: 'Productos', path: '/admin/dashboard-admin/product' },
    { label: 'Categorías', path: '/admin/dashboard-admin/categoria' },
  { label: 'Proveedores', path: '/admin/dashboard-admin/proveedor' },
    { label: 'Pedidos', path: '/admin/dashboard-admin/pedido' },
  ];
  toggleSidebar() { this.sidebarOpen = !this.sidebarOpen; }
}
