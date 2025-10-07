import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Subject, takeUntil, filter } from 'rxjs';
import { Supplier } from '../../interfaces/supplier.interface';
import { SupplierService } from '../../services/supplier.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-suppliers-list',
  templateUrl: './suppliers-list.component.html',
  styleUrls: ['./suppliers-list.component.css']
})
export class SuppliersListComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  suppliers: Supplier[] = [];
  message: string | null = null;
  errors: any[] = [];
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private supplierService: SupplierService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();

    // ✅ AUTO-REFRESH: Detectar cuando se regresa a esta página
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      filter((event: NavigationEnd) => event.url.includes('/admin/dashboard-admin/suppliers')),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      console.log('[AUTO-REFRESH] Detectada navegación a proveedores, recargando...');
      this.loadSuppliers();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadSuppliers(): void {
    this.message = null;
    this.errors = [];
    this.supplierService.getSuppliers(this.page, this.size).subscribe({
      next: (data: any) => {
        this.suppliers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error al cargar los proveedores' });
      }
    });
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadSuppliers();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadSuppliers();
    }
  }

  editSupplier(supplier: Supplier): void {
    this.router.navigate(['edit', supplier.id], { relativeTo: this.route });
  }

  deleteSupplier(id?: number): void {
    if (!id) return;
    Swal.fire({
      title: '¿Estás seguro de que quieres eliminar este proveedor?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.supplierService.deleteSupplier(id).subscribe({
          next: () => {
            this.showSwalToast('Proveedor eliminado exitosamente', 'success');
            this.loadSuppliers();
          },
          error: () => {
            this.showSwalToast('Error al eliminar el proveedor', 'error');
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

  reload(): void {
    this.loadSuppliers();
  }

  addSupplier() {
    this.router.navigate(['add'], { relativeTo: this.route });
  }
}
