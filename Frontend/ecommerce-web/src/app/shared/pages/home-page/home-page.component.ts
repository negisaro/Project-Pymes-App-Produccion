
import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { Producto as ProductoBase } from '../../../producto/interfaces/producto';
import { ProductoPublicService } from '../../../producto/service/producto.service.public';
import { Categoria } from '../../../categoria/interfaces/categoria';
import { CategoriaPublicService } from '../../../categoria/service/categoria.service.public';

// Extiende Producto para incluir stockHistory opcional
export interface ProductoWithHistory extends ProductoBase {
  stockHistory?: number[];
}

@Component({
  selector: 'shared-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, AfterViewInit {
  categoriasConProductos: Array<Categoria & { productos: ProductoWithHistory[] }> = [];

  @ViewChildren('carouselContainer') carouselContainers!: QueryList<ElementRef>;

  constructor(
    private productoService: ProductoPublicService,
    private categoriaService: CategoriaPublicService
  ) {}

  ngOnInit(): void {
    // Consultar categorías y productos en paralelo
    Promise.all([
      this.categoriaService.getCategorias().toPromise(),
      this.productoService.getProductos().toPromise()
    ]).then(([categorias, productos]) => {
      const productosList = Array.isArray(productos) ? productos : [];
      // Mockear historial de stock para cada producto
      productosList.forEach((p: ProductoWithHistory) => {
        // Si ya existe un historial real, no lo sobrescribas
        if (!('stockHistory' in p)) {
          // Genera un historial de 7 días con variaciones aleatorias
          const base = p.stock;
          p.stockHistory = Array.from({ length: 7 }).map((_, i) => Math.max(0, base - Math.floor(Math.random() * 3) + i));
        }
      });
      this.categoriasConProductos = (categorias || []).map(cat => ({
        ...cat,
        productos: productosList.filter((p: ProductoWithHistory) => p.categoriaId === cat.id)
      })).filter(cat => cat.productos.length > 0);
    });
  }

  ngAfterViewInit(): void {
    // Si necesitas lógica tras renderizado, agrégala aquí
  }

  /**
   * Realiza scroll horizontal en el carrusel de la categoría indicada
   * @param categoriaNombre nombre de la categoría
   * @param direction -1 para izquierda, 1 para derecha
   */
  scrollCategoria(categoriaNombre: string, direction: number) {
    const container = this.carouselContainers.find(
      (ref) => ref.nativeElement.getAttribute('data-categoria') === categoriaNombre
    );
    if (container) {
      const card = container.nativeElement.querySelector('.product-card');
      const scrollAmount = card ? card.offsetWidth + 24 : 300; // 24px gap
      container.nativeElement.scrollBy({
        left: direction * scrollAmount * 2, // scrolla 2 cards
        behavior: 'smooth',
      });
    }
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return 'https://via.placeholder.com/400x180?text=Sin+imagen';
    if (imagePath.startsWith('http')) return imagePath;
    return `https://tuservidor.com/${imagePath}`;
  }

  onImgError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://via.placeholder.com/400x180?text=Imagen+no+disponible';
  }

  addToCart(product: ProductoWithHistory) {
    // Tu lógica de agregar al carrito
  }
}
