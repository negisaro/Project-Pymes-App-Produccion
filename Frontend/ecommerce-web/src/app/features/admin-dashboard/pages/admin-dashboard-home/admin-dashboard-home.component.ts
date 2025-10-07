import { Component, OnInit } from '@angular/core';
import { Observable, forkJoin, map } from 'rxjs';
import { ProductService } from '../../../products/services/product.service';
import { CategoryService } from '../../../categories/core/services';
import { UserService } from '../../../user-management/services/user.service';

@Component({
  selector: 'app-admin-dashboard-home',
  templateUrl: './admin-dashboard-home.component.html',
  styleUrls: ['./admin-dashboard-home.component.css']
})
export class AdminDashboardHomeComponent implements OnInit {
  // KPIs para el dashboard
  kpiData = [
    { label: 'Productos', value: 0, icon: 'fas fa-box', bgClass: 'bg-primary text-white' },
    { label: 'Categorías', value: 0, icon: 'fas fa-tags', bgClass: 'bg-success text-white' },
    { label: 'Usuarios', value: 0, icon: 'fas fa-users', bgClass: 'bg-info text-white' },
    { label: 'Pedidos', value: 0, icon: 'fas fa-shopping-cart', bgClass: 'bg-warning text-white' },
  ];

  // Datos mock para gráfico de ventas por mes
  ventasSeries = [{ name: 'Ventas', data: [120000, 90000, 150000, 110000, 170000, 130000, 160000, 140000, 180000, 125000, 155000, 175000] }];
  ventasChart = { type: 'line', height: 300 };
  ventasXaxis = { categories: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'] };

  // Datos mock para gráfico de pastel de categorías
  categoriasSeries = [40, 25, 20, 15];
  categoriasLabels = ['Electrónica', 'Ropa', 'Hogar', 'Otros'];
  categoriasColors = ['#0d6efd', '#198754', '#ffc107', '#6c757d'];
  totalProductos = 0;
  totalCategorias = 0;
  totalUsuarios = 0;
  totalPedidos = 0;
  pedidosRecientes: any[] = [];
  loading = true;
  errorMsg: string | null = null;

  // Datos de ejemplo escalables para pedidos recientes
  private mockPedidos(count: number = 5): any[] {
    const estados = ['COMPLETADO', 'PENDIENTE', 'CANCELADO'];
    return Array.from({ length: count }).map((_, i) => ({
      id: 1000 + i,
      cliente: { nombre: `Cliente ${i + 1}` },
      fecha: new Date(Date.now() - i * 86400000),
      total: Math.floor(Math.random() * 100000) + 10000,
      estado: estados[i % estados.length],
    }));
  }

  constructor(
    private productoService: ProductService,
    private categoriaService: CategoryService,
    private userService: UserService,
  // private pedidoService: PedidoService // TODO: Escalable, implementar servicio de pedidos
  ) {}

  ngOnInit(): void {
    // ✅ OPTIMIZACIÓN: Solo obtener conteos, no datos completos
    forkJoin({
      productosCount: this.productoService.getProducts().pipe(map(products => products.length)),
      categoriasCount: this.categoriaService.getPagedCategories({ page: 0, size: 1 }).pipe(map(response => response.page.totalElements || 0)),
      usuariosCount: this.userService.getPageable(0, 1).pipe(map(response => response.totalElements || 0)),
    }).subscribe({
      next: ({ productosCount, categoriasCount, usuariosCount }: {productosCount: number, categoriasCount: number, usuariosCount: number}) => {
        this.totalProductos = productosCount;
        this.totalCategorias = categoriasCount;
        this.totalUsuarios = usuariosCount;

        // Actualizar KPIs con datos reales optimizados
        this.kpiData = [
          { label: 'Productos', value: this.totalProductos, icon: 'fas fa-box', bgClass: 'bg-primary text-white' },
          { label: 'Categorías', value: this.totalCategorias, icon: 'fas fa-tags', bgClass: 'bg-success text-white' },
          { label: 'Usuarios', value: this.totalUsuarios, icon: 'fas fa-users', bgClass: 'bg-info text-white' },
          { label: 'Pedidos', value: this.totalPedidos, icon: 'fas fa-shopping-cart', bgClass: 'bg-warning text-white' },
        ];

        // Datos mock para pedidos recientes y totalPedidos
        this.pedidosRecientes = this.mockPedidos(5);
        this.totalPedidos = this.pedidosRecientes.length;
        this.loading = false;
        this.errorMsg = null;
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMsg = 'Error al cargar los datos del dashboard. Intenta recargar la página o contacta soporte.';
      }
    });
  }
}
