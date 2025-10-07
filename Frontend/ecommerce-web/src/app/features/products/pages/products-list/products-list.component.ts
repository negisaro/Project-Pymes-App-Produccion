import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Subject, takeUntil, filter } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';
import { CategoryDto } from '../../../categories/core/models';
import { CategoryService } from '../../../categories/core/services';
import { environment } from '../../../../../environments/environments';
import { Product } from '../../interfaces/product.interface';
import { ProductPage } from '../../interfaces/product-page.interface';
import { ProductService } from '../../services/product.service';
import { SupplierService } from '../../../suppliers/services/supplier.service';
import { Supplier } from '../../../suppliers/interfaces/supplier.interface';

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.css'],
})
export class ProductsListComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
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

  products: Product[] = [];
  categorias: CategoryDto[] = [];
  suppliers: Supplier[] = [];
  loading = false;
  error = false;
  errorMsg = '';
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  public canAddProduct(): boolean {
    return this.authService.hasRole(['ROLE_ADMIN']);
  }

  constructor(
    private productService: ProductService,
    private categoriaService: CategoryService,
    private supplierService: SupplierService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategorias();
    this.loadSuppliers();

    // ✅ AUTO-REFRESH: Detectar cuando se regresa a esta página
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      filter((event: NavigationEnd) => event.url.includes('/admin/dashboard-admin/product')),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      console.log('[AUTO-REFRESH] Detectada navegación a productos, recargando...');
      this.loadProducts();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadProducts(): void {
    this.loading = true;
    this.error = false;
    this.errorMsg = '';
    this.productService.getProductsPaginated(this.page, this.size).subscribe({
      next: (resp: ProductPage) => {
        this.products = resp.content;
        this.totalPages = resp.totalPages;
        this.totalElements = resp.totalElements;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.errorMsg = 'Error loading products';
        this.loading = false;
        this.showSwalError('Error loading products');
      }
    });
  }

  loadCategorias(): void {
    this.categoriaService.getPagedCategories().subscribe({
      next: (resp: any) => {
        this.categorias = resp.content || [];
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getSuppliers().subscribe({
      next: (resp: Supplier[]) => {
        // Asegurar que siempre sea un array válido
        this.suppliers = Array.isArray(resp) ? resp : [];
      },
      error: (error) => {
        console.error('Error loading suppliers:', error);
        // En caso de error, asegurar que sea un array vacío
        this.suppliers = [];
      }
    });
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadProducts();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadProducts();
    }
  }

  editProduct(product: Product): void {
    this.router.navigate(['edit', product.id], { relativeTo: this.route });
  }

  deleteProduct(id?: number): void {
    if (!id) return;
    Swal.fire({
      title: 'Are you sure you want to delete this product?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete',
      cancelButtonText: 'Cancel'
    }).then(result => {
      if (result.isConfirmed) {
        this.productService.deleteProduct(id).subscribe({
          next: () => {
            this.showSwalToast('Product deleted successfully', 'success');
            this.loadProducts();
          },
          error: () => {
            this.showSwalError('Error deleting product');
          }
        });
      }
    });
  }

  getCategoryName(categoriaId: number): string {
    // Validar que categorias sea un array antes de usar find()
    if (!Array.isArray(this.categorias)) {
      return 'N/A';
    }

    const categoria = this.categorias.find(c => c.id === categoriaId);
    return categoria ? categoria.nombre : 'N/A';
  }

  getSupplierName(proveedorId: number): string {
    // Validar que suppliers sea un array antes de usar find()
    if (!Array.isArray(this.suppliers)) {
      return 'N/A';
    }

    const supplier = this.suppliers.find(s => s.id === proveedorId);
    return supplier ? supplier.nombre : 'N/A';
  }

  reload(): void {
    this.loadProducts();
  }

  addProduct() {
    this.router.navigate(['add'], { relativeTo: this.route });
  }

  formatPrice(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP'
    }).format(precio);
  }

  getImageUrl(imagenes?: string[]): string {
    if (imagenes && imagenes.length > 0) {
      return `${environment.baseUrl}${imagenes[0]}`;
    }
    return '/assets/images/no-image.png';
  }
}
