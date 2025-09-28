
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Proveedor } from '../../interfaces/proveedor';
import { ProveedorService } from '../../service/proveedor.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-proveedor',
  templateUrl: './list-proveedor.component.html',
  styleUrls: ['./list-proveedor.component.css']
})
export class ListProveedorComponent implements OnInit {
  proveedores: Proveedor[] = [];
  mensaje: string | null = null; // No se usará, solo para compatibilidad visual
  errores: any[] = []; // No se usará, solo para compatibilidad visual
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private proveedorService: ProveedorService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarProveedores();
  }

  cargarProveedores(): void {
    this.mensaje = null;
    this.errores = [];
    this.proveedorService.getProveedores(this.page, this.size).subscribe({
      next: (data: any) => {
        this.proveedores = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error al cargar proveedores' });
      }
    });
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarProveedores();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarProveedores();
    }
  }

  editarProveedor(proveedor: Proveedor): void {
    this.router.navigate(['edit', proveedor.id], { relativeTo: this.route });
  }

  eliminarProveedor(id?: number): void {
    if (!id) return;
    Swal.fire({
      title: '¿Seguro que deseas eliminar el proveedor?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.proveedorService.eliminarProveedor(id).subscribe({
          next: () => {
            this.showSwalToast('Proveedor eliminado correctamente', 'success');
            this.cargarProveedores();
          },
          error: () => {
            this.showSwalToast('Error al eliminar proveedor', 'error');
          }
        });
      }
    });
  }

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

  recargar(): void {
    this.cargarProveedores();
  }

  agregarProveedor() {
    this.router.navigate(['add'], { relativeTo: this.route });
  }
}
