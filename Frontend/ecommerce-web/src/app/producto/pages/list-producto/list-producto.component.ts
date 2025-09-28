import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';
import { Categoria } from '../../../categoria/interfaces/categoria';
import { CategoriaService } from '../../../categoria/service/categoria.service';
import { environment } from '../../../../environments/environments';
import { Producto } from '../../interfaces/producto';
import { PaginaProducto } from '../../interfaces/pagina-producto';
import { ProductoService } from '../../service/producto.service';
import { ProveedorService } from '../../../proveedor/service/proveedor.service';
import { Proveedor } from '../../../proveedor/interfaces/proveedor';

@Component({
  selector: 'app-list-producto',
  templateUrl: './list-producto.component.html',
  styleUrls: ['./list-producto.component.css'],
})
export class ListProductoComponent implements OnInit {
  // Helpers SweetAlert2 para feedback uniforme
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

  private showSwalError(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#d33',
      timer: 2500
    });
  }
  productos: Producto[] = [];
  categorias: Categoria[] = [];
  proveedor: Proveedor[] = [];
  loading = false;
  error = false;
  errorMsg = '';
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  public puedeAgregarProducto(): boolean {
    return this.authService.hasRole(['ROLE_ADMIN']);
  }

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private proveedorService: ProveedorService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
  }

  cargarProductos(): void {
    this.loading = true;
    this.error = false;
    this.errorMsg = '';
  this.productoService.getProductosPaginados(this.page, this.size).subscribe({
      next: (resp: PaginaProducto) => {
        this.productos = resp.content;
        this.totalPages = resp.totalPages;
        this.totalElements = resp.totalElements;
        this.loading = false;
        // Mensaje de éxito opcional
        // Swal.fire({ icon: 'success', title: 'Productos cargados', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
      },
      error: (err) => {
        this.error = true;
        this.loading = false;
        if (err.status === 401) {
          this.showSwalToast('Sesión expirada. Redirigiendo al login...', 'warning');
          setTimeout(() => this.router.navigate(['/auth/login']), 2000);
        } else if (err.status === 403) {
          this.showSwalError('No tienes permisos para ver los productos.');
        } else {
          this.showSwalError('Ocurrió un error al cargar los productos.');
        }
      },
    });
  }

  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (resp: Categoria[]) => {
        this.categorias = resp;
        // Mensaje de éxito opcional
        // Swal.fire({ icon: 'success', title: 'Categorías cargadas', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
      },
      error: (err) => {
        this.showSwalError('Ocurrió un error al cargar las categorías.');
      },
    });
  }

  cargarProveedor(): void {
    this.proveedorService.getProveedores().subscribe({
      next: (resp: Proveedor[]) => {
        this.proveedor = resp;
        // Mensaje de éxito opcional
        // Swal.fire({ icon: 'success', title: 'Categorías cargadas', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
      },
      error: (err) => {
        this.showSwalError('Ocurrió un error al cargar los proveedores.');
      },
    });
  }

  editarProducto(producto: Producto): void {
    this.router.navigate(['edit', producto.id], { relativeTo: this.route });
  }

  eliminarProducto(id: number): void {
    Swal.fire({
      title: '¿Seguro que deseas eliminar el producto?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.productoService.deleteProducto(id).subscribe({
          next: () => {
            this.showSwalToast('Producto eliminado correctamente', 'success');
            this.cargarProductos();
          },
          error: () => {
            this.showSwalError('Error al eliminar producto');
          }
        });
      }
    });
  }

  volverInicio(): void {
    this.showSwalToast('Volviendo al inicio', 'info');
    this.router.navigate(['/']);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina < 0 || nuevaPagina >= this.totalPages) return;
    this.page = nuevaPagina;
    this.cargarProductos();
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return 'https://via.placeholder.com/120x120?text=Sin+imagen';
    if (imagePath.startsWith('http')) return imagePath;
    return `${environment.baseUrl}${imagePath}`;
  }

  agregarProducto(): void {
    this.router.navigate(['add'], { relativeTo: this.route });
  }
}
