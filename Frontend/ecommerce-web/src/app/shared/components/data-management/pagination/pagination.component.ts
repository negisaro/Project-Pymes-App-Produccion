import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';

/**
 * Componente de paginación reutilizable
 * Proporciona navegación de páginas consistente y configurable
 */
@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit, OnChanges {

  // ===== INPUTS =====
  @Input() currentPage: number = 0;          // Página actual (0-indexed)
  @Input() totalPages: number = 0;           // Total de páginas
  @Input() totalItems: number = 0;           // Total de elementos
  @Input() pageSize: number = 10;            // Elementos por página
  @Input() pageSizeOptions: number[] = [5, 10, 25, 50, 100]; // Opciones de tamaño
  @Input() showSizeSelector: boolean = true;  // Mostrar selector de tamaño
  @Input() showFirstLast: boolean = true;     // Mostrar botones primera/última
  @Input() showPrevNext: boolean = true;      // Mostrar botones anterior/siguiente
  @Input() showPageNumbers: boolean = true;   // Mostrar números de página
  @Input() showInfo: boolean = true;          // Mostrar información de registros
  @Input() maxVisiblePages: number = 5;       // Máximo de páginas visibles
  @Input() size: 'sm' | 'md' | 'lg' = 'md';   // Tamaño del componente
  @Input() loading: boolean = false;          // Estado de carga

  // ===== OUTPUTS =====
  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();

  // ===== ESTADO INTERNO =====
  visiblePages: number[] = [];
  fromRecord: number = 0;
  toRecord: number = 0;

  constructor() { }

  ngOnInit(): void {
    this.calculateVisiblePages();
    this.calculateRecordRange();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currentPage'] || changes['totalPages'] || changes['maxVisiblePages']) {
      this.calculateVisiblePages();
    }

    if (changes['currentPage'] || changes['pageSize'] || changes['totalItems']) {
      this.calculateRecordRange();
    }
  }

  // ===== NAVEGACIÓN =====
  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages || page === this.currentPage || this.loading) {
      return;
    }

    this.pageChange.emit(page);
  }

  goToFirstPage(): void {
    this.goToPage(0);
  }

  goToLastPage(): void {
    this.goToPage(this.totalPages - 1);
  }

  goToPreviousPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  goToNextPage(): void {
    this.goToPage(this.currentPage + 1);
  }

  onPageSizeChange(newSize: number): void {
    if (newSize !== this.pageSize && !this.loading) {
      this.pageSizeChange.emit(newSize);
    }
  }

  // ===== CÁLCULOS =====
  private calculateVisiblePages(): void {
    if (this.totalPages <= this.maxVisiblePages) {
      // Mostrar todas las páginas si son pocas
      this.visiblePages = Array.from({length: this.totalPages}, (_, i) => i);
    } else {
      // Calcular rango de páginas visibles
      const half = Math.floor(this.maxVisiblePages / 2);
      let start = this.currentPage - half;
      let end = this.currentPage + half;

      // Ajustar si estamos al inicio
      if (start < 0) {
        end = end - start;
        start = 0;
      }

      // Ajustar si estamos al final
      if (end >= this.totalPages) {
        start = start - (end - this.totalPages + 1);
        end = this.totalPages - 1;
        start = Math.max(0, start);
      }

      this.visiblePages = Array.from({length: end - start + 1}, (_, i) => start + i);
    }
  }

  private calculateRecordRange(): void {
    if (this.totalItems === 0) {
      this.fromRecord = 0;
      this.toRecord = 0;
    } else {
      this.fromRecord = this.currentPage * this.pageSize + 1;
      this.toRecord = Math.min((this.currentPage + 1) * this.pageSize, this.totalItems);
    }
  }

  // ===== UTILIDADES =====
  isFirstPage(): boolean {
    return this.currentPage === 0;
  }

  isLastPage(): boolean {
    return this.currentPage === this.totalPages - 1;
  }

  shouldShowEllipsisBefore(): boolean {
    return this.visiblePages.length > 0 && this.visiblePages[0] > 0;
  }

  shouldShowEllipsisAfter(): boolean {
    return this.visiblePages.length > 0 &&
           this.visiblePages[this.visiblePages.length - 1] < this.totalPages - 1;
  }

  getPaginationClasses(): string {
    const classes = ['pagination'];

    if (this.size === 'sm') {
      classes.push('pagination-sm');
    } else if (this.size === 'lg') {
      classes.push('pagination-lg');
    }

    return classes.join(' ');
  }

  getPageItemClasses(page: number): string {
    const classes = ['page-item'];

    if (page === this.currentPage) {
      classes.push('active');
    }

    if (this.loading) {
      classes.push('disabled');
    }

    return classes.join(' ');
  }

  getNavItemClasses(disabled: boolean): string {
    const classes = ['page-item'];

    if (disabled || this.loading) {
      classes.push('disabled');
    }

    return classes.join(' ');
  }

  // ===== GETTERS PARA TEMPLATE =====
  get hasPages(): boolean {
    return this.totalPages > 1;
  }

  get hasRecords(): boolean {
    return this.totalItems > 0;
  }
}
