import { Component } from '@angular/core';

@Component({
  selector: 'app-cliente-dashboard',
  templateUrl: './cliente-dashboard.component.html',
  styleUrls: ['./cliente-dashboard.component.css']
})
export class ClienteDashboardComponent {
  // Mock data para escalar a futuro
  user = {
    nombre: 'Juan Pérez',
    avatar: 'assets/images/user.png',
    puntos: 120,
    nivel: 'Oro',
    cupones: 2
  };
  pedidosRecientes = [
    { id: 'PED-001', fecha: '2025-09-20', estado: 'Enviado', total: 120000 },
    { id: 'PED-002', fecha: '2025-08-15', estado: 'Entregado', total: 85000 },
    { id: 'PED-003', fecha: '2025-07-30', estado: 'Cancelado', total: 45000 }
  ];
  carrito = [
    { nombre: 'Camiseta Premium', cantidad: 2, precio: 35000 },
    { nombre: 'Gorra Glow', cantidad: 1, precio: 25000 }
  ];
  favoritos = [
    { nombre: 'Sudadera Glow', precio: 90000 },
    { nombre: 'Mug Glow Dreams', precio: 18000 }
  ];
  recomendaciones = [
    { nombre: 'Bolso Glow', precio: 65000, img: 'assets/images/bolso.jpg' },
    { nombre: 'Termo Glow', precio: 32000, img: 'assets/images/termo.jpg' },
    { nombre: 'Llavero Glow', precio: 9000, img: 'assets/images/llavero.jpg' }
  ];
  promociones = [
    { titulo: 'Envío gratis en compras mayores a $100.000', descripcion: 'Aprovecha esta promoción por tiempo limitado.' },
    { titulo: '2x1 en mugs seleccionados', descripcion: 'Solo por hoy.' }
  ];
}
