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
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.categoriaForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      estado: [true, Validators.required]
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
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cargar la categoría para editar.',
              timer: 2000,
              showConfirmButton: false,
              toast: true,
              position: 'top-end'
            });
            this.router.navigate(['/dashboard/categoria/list']);
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.categoriaForm.invalid) return;
    const categoria: Categoria = this.categoriaForm.value;
    this.debugResponse = null;
    this.debugError = null;
    if (this.modoEdicion && this.categoriaId !== null) {
      // Modo edición
      this.categoriaService.updateCategoria(this.categoriaId, categoria).subscribe({
        next: (response) => {
          this.debugResponse = response;
          Swal.fire({
            icon: 'success',
            title: '¡Categoría actualizada!',
            text: 'La categoría se actualizó correctamente.',
            timer: 1800,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
          setTimeout(() => {
            this.router.navigate(['/dashboard/categoria/list']);
          }, 1800);
        },
        error: (error) => {
          this.debugError = error;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo actualizar la categoría.',
            timer: 2000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
          this.errores = [{ mensaje: 'Error al actualizar categoría' }];
        }
      });
    } else {
      // Modo agregar
      this.categoriaService.addCategoria(categoria).subscribe({
        next: (response) => {
          this.debugResponse = response;
          Swal.fire({
            icon: 'success',
            title: '¡Categoría agregada!',
            text: 'La categoría se agregó correctamente.',
            timer: 1800,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
          setTimeout(() => {
            this.router.navigate(['/dashboard/categoria/list']);
          }, 1800);
        },
        error: (error) => {
          this.debugError = error;
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo agregar la categoría.',
            timer: 2000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
          });
          this.errores = [{ mensaje: 'Error al agregar categoría' }];
        }
      });
    }
  }
}
