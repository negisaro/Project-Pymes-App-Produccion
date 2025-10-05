import { Component } from '@angular/core';

@Component({
  selector: 'app-cliente-sidebar',
  templateUrl: './cliente-sidebar.component.html',
  styleUrls: ['./cliente-sidebar.component.css']
})
export class ClienteSidebarComponent {
  // Mock de menú escalable
  menu = [
    { icon: 'bi-house-door', label: 'Inicio', route: '/dashboard-cliente' },
    { icon: 'bi-bag', label: 'Mis pedidos', route: '/dashboard-cliente/pedidos' },
    { icon: 'bi-heart', label: 'Favoritos', route: '/dashboard-cliente/favoritos' },
    { icon: 'bi-cart', label: 'Carrito', route: '/dashboard-cliente/carrito' },
    { icon: 'bi-person', label: 'Perfil', route: '/dashboard-cliente/perfil' },
    { icon: 'bi-gear', label: 'Configuración', route: '/dashboard-cliente/configuracion' }
  ];
}
