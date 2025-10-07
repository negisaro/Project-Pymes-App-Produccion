import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../../../../environments/environments';
import Swal from 'sweetalert2';
import { CategoryDto, CategorySummaryDto } from '../../../categories/core/models';
import { CategoryPublicService } from '../../../../shared/services';
import { Supplier } from '../../../suppliers/interfaces/supplier.interface';
import { SupplierService } from '../../../suppliers/services/supplier.service';
import { Product } from '../../interfaces/product.interface';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css'],
})
export class ProductFormComponent implements OnInit {
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

  productForm: FormGroup;
  mensaje: string | null = null;
  errores: any[] = [];
  categorias: CategorySummaryDto[] = [];
  suppliers: Supplier[] = [];

  imagenSeleccionada: File | null = null;
  imagenUrl: string | null = null;
  isUploading = false;

  isEditMode = false;
  productId: number | null = null;

  readonly maxSize = 2 * 1024 * 1024; // 2MB en bytes

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryPublicService: CategoryPublicService,
    private supplierService: SupplierService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.productForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      precio: [0, [Validators.required, Validators.min(0)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      categoriaId: [null, Validators.required],
      proveedorId: [null, Validators.required],
      imagenes: this.fb.array([]),
      estado: [true, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadCategorias();
    this.loadSuppliers();

    // Detectar modo edición
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.productId = +id;
        this.loadProduct(this.productId);
      }
    });
  }

  loadCategorias(): void {
    console.log('🔄 Cargando categorías...');
    this.categoryPublicService.getActiveCategories().subscribe({
      next: (categorias: CategorySummaryDto[]) => {
        console.log('✅ Categorías cargadas:', categorias);
        this.categorias = categorias || [];
        if (this.categorias.length === 0) {
          console.warn('⚠️ No se encontraron categorías activas');
        }
      },
      error: (err: any) => {
        console.error('❌ Error al cargar categorías:', err);
        console.error('Response status:', err.status);
        console.error('Response message:', err.message);
        this.showSwalError('Error al cargar las categorías disponibles. Verifique su conexión.');
      },
    });
  }

  loadSuppliers(): void {
    console.log('🔄 Cargando proveedores...');
    this.supplierService.getActiveSuppliers().subscribe({
      next: (suppliers: Supplier[]) => {
        console.log('✅ Proveedores cargados:', suppliers);
        this.suppliers = suppliers || [];
        if (this.suppliers.length === 0) {
          console.warn('⚠️ No se encontraron proveedores activos');
        }
      },
      error: (err: any) => {
        console.error('❌ Error al cargar proveedores:', err);
        console.error('Response status:', err.status);
        console.error('Response message:', err.message);
        this.showSwalError('Error al cargar los proveedores disponibles. Verifique su conexión y permisos.');
      }
    });
  }

  loadProduct(id: number): void {
    this.productService.getProductAdmin(id).subscribe({
      next: (product: Product) => {
        this.productForm.patchValue(product);
        this.imagenes.clear();
        if (product.imagenes && Array.isArray(product.imagenes)) {
          product.imagenes.forEach(url => this.addImagen(url));
        }
      },
      error: () => {
        this.showSwalError('No se pudo cargar el producto.');
        this.router.navigate(['/admin/dashboard-admin/product']);
      }
    });
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return 'https://via.placeholder.com/100x100?text=Sin+imagen';
    if (imagePath.startsWith('http')) return imagePath;
    return `${environment.baseUrl}${imagePath}`;
  }

  get imagenes(): FormArray {
    return this.productForm.get('imagenes') as FormArray;
  }

  get imagenesFormControls(): FormControl[] {
    return this.imagenes.controls as FormControl[];
  }

  addImagen(url: string) {
    // Evitar imágenes duplicadas
    if (this.imagenes.value.includes(url)) {
      this.showSwalToast('Esta imagen ya ha sido agregada.', 'warning');
      return;
    }
    this.imagenes.push(this.fb.control(url));
  }

  removeImagen(index: number) {
    this.imagenes.removeAt(index);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const archivo = input.files[0];
      if (archivo.size > this.maxSize) {
        this.showSwalError('El archivo excede el tamaño máximo permitido (2MB).');
        this.imagenSeleccionada = null;
        input.value = '';
        return;
      }
      this.imagenSeleccionada = archivo;
    }
  }

  uploadImage() {
    if (!this.imagenSeleccionada) {
      this.showSwalToast('Primero selecciona una imagen.', 'warning');
      return;
    }
    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', this.imagenSeleccionada);

    this.productService.uploadImage(formData).subscribe({
      next: (resp: any) => {
        this.isUploading = false;
        if (resp && resp.url) {
          this.addImagen(resp.url);
          this.showSwalToast('La imagen se subió correctamente.', 'success');
        } else {
          this.showSwalError('No se recibió una URL válida del backend.');
        }
        this.clearFileInput();
      },
      error: (err) => {
        this.isUploading = false;
        let mensajeError = 'No se pudo subir la imagen.';
        if (err?.error?.error) mensajeError = err.error.error;
        this.showSwalError(mensajeError);
        this.clearFileInput();
      }
    });
  }

  private clearFileInput() {
    this.imagenSeleccionada = null;
    const inputFile = document.querySelector('input[type="file"]') as HTMLInputElement;
    if (inputFile) inputFile.value = '';
  }

  onSubmit() {
    if (this.productForm.invalid) return;

    // Validar que haya al menos una imagen válida
    if (this.imagenes.length === 0) {
      this.showSwalError('Debes subir al menos una imagen antes de guardar el producto.');
      return;
    }

    // Preparar producto para envío
    const productToSend: Product = {
      ...this.productForm.value,
      imagenes: this.imagenes.value // Array de strings (urls)
    };

    console.log('🟢 Enviando producto al backend:', productToSend);

    if (this.isEditMode && this.productId) {
      // Modo edición
      this.productService.updateProduct(this.productId, productToSend).subscribe({
        next: () => {
          this.showSwalToast('Producto actualizado exitosamente', 'success');
          this.errores = [];
          setTimeout(() => {
            this.navigateToList();
          }, 2000);
        },
        error: (err) => {
          this.handleError(err, 'Error al actualizar producto');
        },
      });
    } else {
      // Modo agregar
      this.productService.addProduct(productToSend).subscribe({
        next: () => {
          this.showSwalToast('Producto guardado exitosamente', 'success');
          this.errores = [];
          this.resetForm();
          setTimeout(() => {
            this.navigateToList();
          }, 2000);
        },
        error: (err) => {
          this.handleError(err, 'Error al crear producto');
        },
      });
    }
  }

  private handleError(err: any, defaultMessage: string) {
    this.showSwalError(err.error?.errores?.[0]?.mensaje || defaultMessage);
    this.mensaje = null;
    this.errores = err.error?.errores || [
      { campo: 'general', mensaje: defaultMessage },
    ];
  }

  private resetForm() {
    this.productForm.reset();
    this.imagenes.clear();
    this.mensaje = null;
    this.errores = [];
  }

  private navigateToList() {
    // Navegar correctamente a la lista de productos
    this.router.navigate(['/admin/dashboard-admin/product']);
  }

  cancel() {
    this.navigateToList();
  }
}
