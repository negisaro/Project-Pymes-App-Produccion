import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { Categoria } from '../../interfaces/categoria';
import { CategoriaService } from '../../service/categoria.service';

@Component({
  selector: 'app-add-categoria',
  templateUrl: './add-categoria.component.html',
  styleUrls: ['./add-categoria.component.css']
})
export class AddCategoriaComponent implements OnInit {
  categoriaForm: FormGroup;
  mensaje: string | null = null;
  errores: any[] = [];
  debugResponse: any = null;
  debugError: any = null;


  modoEdicion: boolean = false;
  categoriaId: number | null = null;


  constructor(
    private fb: FormBuilder,
    private categoriaService: CategoriaService,
    public router: Router,
    private route: ActivatedRoute
  ) {
    this.categoriaForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      estado: [true, Validators.required]
    });
  }

  onClear(): void {
    this.categoriaForm.reset({ estado: true });
    this.showSwalToast('El formulario ha sido limpiado.', 'info');
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

  private showSwalError(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#d33',
      timer: 2500
    });
  }


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.modoEdicion = true;
        this.categoriaId = +idParam;
        this.categoriaService.getCategoria(this.categoriaId).subscribe({
          next: (categoria) => {
            this.categoriaForm.patchValue({
              nombre: categoria.nombre,
              descripcion: categoria.descripcion,
              estado: categoria.estado
            });
            this.showSwalToast('Categoría cargada para edición', 'info');
          },
          error: () => {
            this.showSwalError('No se pudo cargar la categoría para editar.');
            this.router.navigate(['../list'], { relativeTo: this.route });
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.categoriaForm.invalid) {
      this.showSwalError('Por favor, completa todos los campos obligatorios y verifica los datos.');
      this.categoriaForm.markAllAsTouched();
      return;
    }
    const categoria: Categoria = this.categoriaForm.value;
    this.debugResponse = null;
    this.debugError = null;
    if (this.modoEdicion && this.categoriaId !== null) {
      // Modo edición
      this.categoriaService.updateCategoria(this.categoriaId, categoria).subscribe({
        next: (response) => {
          this.debugResponse = response;
          this.showSwalToast('¡Categoría actualizada!', 'success');
          setTimeout(() => {
            this.router.navigate(['../'], { relativeTo: this.route });
          }, 1800);
        },
        error: (error) => {
          this.debugError = error;
          this.showSwalError('No se pudo actualizar la categoría.');
          this.errores = [{ mensaje: 'Error al actualizar categoría' }];
        }
      });
    } else {
      // Modo agregar
      this.categoriaService.addCategoria(categoria).subscribe({
        next: (response) => {
          this.debugResponse = response;
          this.showSwalToast('¡Categoría agregada!', 'success');
          setTimeout(() => {
            this.router.navigate(['../'], { relativeTo: this.route });
          }, 1800);
        },
        error: (error) => {
          this.debugError = error;
          this.showSwalError('No se pudo agregar la categoría.');
          this.errores = [{ mensaje: 'Error al agregar categoría' }];
        }
      });
    }
  }
}
