import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../../interfaces/categoria';
import { CategoriaService, PaginaCategoria } from '../../service/categoria.service';

@Component({
  selector: 'app-list-categoria',
  templateUrl: './list-categoria.component.html',
  styleUrls: ['./list-categoria.component.css']
})
export class ListCategoriaComponent implements OnInit {
  categorias: Categoria[] = [];
  mensaje: string | null = null;
  errores: any[] = [];
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  constructor(private categoriaService: CategoriaService, private router: Router) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.getCategoriasPaginadas(this.page, this.size).subscribe({
      next: (data: PaginaCategoria) => {
        this.categorias = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (err) => this.errores = [{ mensaje: 'Error al cargar categorías' }]
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
    // Implementar navegación o modal para editar
  }

  eliminarCategoria(id: number): void {
    this.categoriaService.deleteCategoria(id).subscribe({
      next: () => {
        this.mensaje = 'Categoría eliminada correctamente';
        this.cargarCategorias();
      },
      error: () => this.errores = [{ mensaje: 'Error al eliminar categoría' }]
    });
  }

  agregarCategoria() {
    this.router.navigate(['/dashboard/categoria/add']);
  }
}
