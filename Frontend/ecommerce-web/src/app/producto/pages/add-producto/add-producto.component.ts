import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../../../environments/environments';
import Swal from 'sweetalert2';
import { Categoria } from '../../../categoria/interfaces/categoria';
import { CategoriaService } from '../../../categoria/service/categoria.service';
import { Proveedor } from '../../../proveedor/interfaces/proveedor';
import { ProveedorService } from '../../../proveedor/service/proveedor.service';
import { Producto } from '../../interfaces/producto';
import { ProductoService } from '../../service/producto.service';

@Component({
  selector: 'app-add-producto',
  templateUrl: './add-producto.component.html',
  styleUrls: ['./add-producto.component.css'],
})
export class AddProductoComponent implements OnInit {
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
  productoForm: FormGroup;
  mensaje: string | null = null;
  errores: any[] = [];
  categorias: Categoria[] = [];
  proveedores: Proveedor[] = [];


  imagenSeleccionada: File | null = null;
  imagenUrl: string | null = null;
  isUploading = false;

  modoEdicion = false;
  productoId: number | null = null;

  readonly maxSize = 2 * 1024 * 1024; // 2MB en bytes

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private proveedorService: ProveedorService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.productoForm = this.fb.group({
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
    this.cargarCategorias();
    this.proveedorService.getProveedores().subscribe((data: any) => {
      if (Array.isArray(data)) {
        this.proveedores = data;
      } else if (data && Array.isArray(data.content)) {
        this.proveedores = data.content;
      } else {
        this.proveedores = [];
      }
    });

    // Detectar modo edición
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.modoEdicion = true;
        this.productoId = +id;
        this.productoService.getProductoAdmin(this.productoId).subscribe({
          next: (producto: Producto) => {
            this.productoForm.patchValue(producto);
            this.imagenes.clear();
            if (producto.imagenes && Array.isArray(producto.imagenes)) {
              producto.imagenes.forEach(url => this.addImagen(url));
            }
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cargar el producto.',
              timer: 2000,
              showConfirmButton: false
            });
            this.router.navigate(['/dashboard/product/list']);
          }
        });
      }
    });
  }

  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (resp: any) => {
        this.categorias = resp.content;
      },
      error: (err) => {
        console.error('Error al cargar categorías', err);
      },
    });
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return 'https://via.placeholder.com/100x100?text=Sin+imagen';
    if (imagePath.startsWith('http')) return imagePath;
    return `${environment.baseUrl}${imagePath}`;
  }

  get imagenes(): FormArray {
    return this.productoForm.get('imagenes') as FormArray;
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

  subirImagen() {
    if (!this.imagenSeleccionada) {
      this.showSwalToast('Primero selecciona una imagen.', 'warning');
      return;
    }
    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', this.imagenSeleccionada);

    this.productoService.subirImagen(formData).subscribe({
      next: (resp: any) => {
        this.isUploading = false;
        if (resp && resp.url) {
          this.addImagen(resp.url);
          this.showSwalToast('La imagen se subió correctamente.', 'success');
        } else {
          this.showSwalError('No se recibió una URL válida del backend.');
        }
        this.imagenSeleccionada = null;
        // Limpia el input file (por si quieres subir otra imagen)
        const inputFile = document.querySelector('input[type="file"]') as HTMLInputElement;
        if (inputFile) inputFile.value = '';
      },
      error: (err) => {
        this.isUploading = false;
        let mensajeError = 'No se pudo subir la imagen.';
        if (err?.error?.error) mensajeError = err.error.error;
        this.showSwalError(mensajeError);
        this.imagenSeleccionada = null;
        const inputFile = document.querySelector('input[type="file"]') as HTMLInputElement;
        if (inputFile) inputFile.value = '';
      }
    });
  }

  onSubmit() {
    if (this.productoForm.invalid) return;

    // Validar que haya al menos una imagen válida
    if (this.imagenes.length === 0) {
      this.showSwalError('Debes subir al menos una imagen antes de guardar el producto.');
      return;
    }

    // ¡Clave! Asegurarse de enviar imágenes como array de string, no FormArray de controls
    const productoToSend: Producto = {
      ...this.productoForm.value,
      imagenes: this.imagenes.value // Esto es un array de strings (urls)
    };

    // LOG FRONTEND: Mostrar en consola el objeto que se enviará al backend
    console.log('🟢 Enviando producto al backend:', productoToSend);

    if (this.modoEdicion && this.productoId) {
      // Modo edición
      this.productoService.updateProducto(this.productoId, productoToSend).subscribe({
        next: () => {
          this.showSwalToast('Producto actualizado exitosamente', 'success');
          this.errores = [];
          setTimeout(() => {
            this.router.navigate(['/dashboard/product/list-product']);
          }, 2000);
        },
        error: (err) => {
          this.showSwalError(err.error?.errores?.[0]?.mensaje || 'Error al actualizar producto');
          this.mensaje = null;
          this.errores = err.error?.errores || [
            { campo: 'general', mensaje: 'Error al actualizar producto' },
          ];
        },
      });
    } else {
      // Modo agregar
      this.productoService.addProducto(productoToSend).subscribe({
        next: (resp: Producto) => {
          this.showSwalToast('Producto guardado exitosamente', 'success');
          this.errores = [];
          this.productoForm.reset();
          this.imagenes.clear();
          this.mensaje = null;
          setTimeout(() => {
            this.router.navigate(['/dashboard/product/list-product']);
          }, 2000);
        },
        error: (err) => {
          this.showSwalError(err.error?.errores?.[0]?.mensaje || 'Error al crear producto');
          this.mensaje = null;
          this.errores = err.error?.errores || [
            { campo: 'general', mensaje: 'Error al crear producto' },
          ];
        },
      });
    }
  }
}
