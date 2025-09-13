import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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

  constructor(
    private fb: FormBuilder,
    private categoriaService: CategoriaService,
    private router: Router
  ) {
    this.categoriaForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      estado: [true, Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.categoriaForm.invalid) return;
    const categoria: Categoria = this.categoriaForm.value;
    this.categoriaService.addCategoria(categoria).subscribe({
      next: () => {
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
      error: () => {
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
