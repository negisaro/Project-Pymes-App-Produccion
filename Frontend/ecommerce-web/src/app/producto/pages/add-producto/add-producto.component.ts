import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
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
  productoForm: FormGroup;
  mensaje: string | null = null;
  errores: any[] = [];
  categorias: Categoria[] = [];
  proveedores: Proveedor[] = [];

  imagenSeleccionada: File | null = null;
  imagenUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private proveedorService: ProveedorService,
    private router: Router
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
  }

  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (resp: any) => {
        console.log('Categorías recibidas:', resp);
        this.categorias = resp.content;
      },
      error: (err) => {
        console.error('Error al cargar categorías', err);
      },
    });
  }

  get imagenes(): FormArray {
    return this.productoForm.get('imagenes') as FormArray;
  }
  get caracteristicas(): FormArray {
    return this.productoForm.get('caracteristicas') as FormArray;
  }
  get imagenesFormControls(): FormControl[] {
    return this.imagenes.controls as FormControl[];
  }

  addImagen(url: string) {
    this.imagenes.push(this.fb.control(url));
  }
  removeImagen(index: number) {
    this.imagenes.removeAt(index);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.imagenSeleccionada = input.files[0];
    }
  }

  subirImagen() {
    if (!this.imagenSeleccionada) return;
    const formData = new FormData();
    formData.append('file', this.imagenSeleccionada);

    this.productoService.subirImagen(formData).subscribe({
      next: (resp: any) => {
        this.imagenUrl = resp.url;
        if (typeof this.imagenUrl === 'string' && this.imagenUrl) {
          this.addImagen(this.imagenUrl);
        }
        this.imagenSeleccionada = null;
      },
      error: (err) => {
        console.error('Error al subir imagen', err);
      },
    });
  }

  onSubmit() {
    if (this.productoForm.invalid) return;
    this.productoService.addProducto(this.productoForm.value).subscribe({
      next: (resp: Producto) => {
        Swal.fire({
          icon: 'success',
          title: 'Producto creado',
          text: 'El producto fue guardado exitosamente',
          timer: 2000,
          showConfirmButton: false,
        });
        this.errores = [];
        this.productoForm.reset();
        this.imagenes.clear();
        this.caracteristicas.clear();
        setTimeout(() => {
          this.router.navigate(['/producto/list']);
        }, 2000);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: err.error?.errores?.[0]?.mensaje || 'Error al crear producto',
          timer: 2500,
          showConfirmButton: false,
        });
        this.mensaje = null;
        this.errores = err.error?.errores || [
          { campo: 'general', mensaje: 'Error al crear producto' },
        ];
      },
    });
  }
}
