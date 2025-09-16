
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../../interfaces/categoria';
import { CategoriaService, PaginaCategoria } from '../../service/categoria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-categoria',
  templateUrl: './list-categoria.component.html',
  styleUrls: ['./list-categoria.component.css']
})
export class ListCategoriaComponent implements OnInit {
  categorias: Categoria[] = [];
  mensaje: string | null = null; // No se usará, solo para compatibilidad visual
  errores: any[] = []; // No se usará, solo para compatibilidad visual
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  constructor(private categoriaService: CategoriaService, private router: Router) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.mensaje = null;
    this.errores = [];
    this.categoriaService.getCategoriasPaginadas(this.page, this.size).subscribe({
      next: (data: PaginaCategoria) => {
        this.categorias = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error al cargar categorías' });
      }
    });
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarCategorias();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarCategorias();
    }
  }

  editarCategoria(categoria: Categoria): void {
    this.router.navigate(['/dashboard/categoria/edit', categoria.id]);
  }

  eliminarCategoria(id: number): void {
    Swal.fire({
      title: '¿Seguro que deseas eliminar la categoría?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.categoriaService.deleteCategoria(id).subscribe({
          next: () => {
            Swal.fire({ icon: 'success', title: 'Eliminada', text: 'Categoría eliminada correctamente' });
            this.cargarCategorias();
          },
          error: () => {
            Swal.fire({ icon: 'error', title: 'Error', text: 'Error al eliminar categoría' });
          }
        });
      }
    });
  }
  recargar(): void {
    this.cargarCategorias();
  }

  agregarCategoria() {
    this.router.navigate(['/dashboard/categoria/add']);
  }
}
