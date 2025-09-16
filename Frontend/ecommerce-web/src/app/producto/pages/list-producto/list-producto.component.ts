import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';
import { Categoria } from '../../../categoria/interfaces/categoria';
import { CategoriaService } from '../../../categoria/service/categoria.service';
import { Producto } from '../../interfaces/producto';
import {
  PaginaProducto,
  ProductoService,
} from '../../service/producto.service';
import { ProveedorService } from '../../../proveedor/service/proveedor.service';
import { Proveedor } from '../../../proveedor/interfaces/proveedor';

@Component({
  selector: 'app-list-producto',
  templateUrl: './list-producto.component.html',
  styleUrls: ['./list-producto.component.css'],
})
export class ListProductoComponent implements OnInit {
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
    return this.authService.hasRole(['ROLE_ADMIN', 'ROLE_SUPERVISOR']);
  }

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private proveedorService: ProveedorService,
    private router: Router,
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
          Swal.fire({
            icon: 'warning',
            title: 'Sesión expirada',
            text: 'Redirigiendo al login...',
            timer: 2000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
          setTimeout(() => this.router.navigate(['/auth/login']), 2000);
        } else if (err.status === 403) {
          Swal.fire({
            icon: 'error',
            title: 'Sin permisos',
            text: 'No tienes permisos para ver los productos.',
            timer: 2000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al cargar los productos.',
            timer: 2000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
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
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Ocurrió un error al cargar las categorías.',
          timer: 2000,
          showConfirmButton: false,
          toast: true,
          position: 'top-end'
        });
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
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Ocurrió un error al cargar las proveedores.',
          timer: 2000,
          showConfirmButton: false,
          toast: true,
          position: 'top-end'
        });
      },
    });
  }

  editarProducto(producto: Producto): void {
  this.router.navigate(['/dashboard/product/edit-product', producto.id]);
  }

  eliminarProducto(id: number): void {
    Swal.fire({
      title: '¿Eliminar producto?',
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e74c3c',
      cancelButtonColor: '#2980b9',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.productoService.deleteProducto(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Producto eliminado',
              text: 'El producto ha sido eliminado correctamente.',
              timer: 1800,
              showConfirmButton: false,
              toast: true,
              position: 'top-end'
            });
            setTimeout(() => {
              this.router.navigate(['/dashboard/product/list-product']);
            }, 1000);
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el producto.',
              timer: 2000,
              showConfirmButton: false,
              toast: true,
              position: 'top-end'
            });
          }
        });
      }
    });
  }

  volverInicio(): void {
    Swal.fire({
      icon: 'info',
      title: 'Volviendo al inicio',
      timer: 1200,
      showConfirmButton: false,
      toast: true,
      position: 'top-end'
    });
    this.router.navigate(['/']);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina < 0 || nuevaPagina >= this.totalPages) return;
    this.page = nuevaPagina;
    this.cargarProductos();
  }

  agregarProducto(): void {
    this.router.navigate(['/dashboard/product/add-product']);
  }
}
