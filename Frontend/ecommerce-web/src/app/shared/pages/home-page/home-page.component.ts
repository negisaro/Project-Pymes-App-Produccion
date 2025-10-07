import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { AuthService } from '../../../features/auth/services/auth.service';
import { AuthStatus } from '../../../features/auth/interfaces';
import { environment } from '../../../../environments/environments';
import { Product as ProductoBase } from '../../interfaces/product-public.interface';
import { ProductPublicService } from '../../services/product-public.service';
import { CategorySummaryDto } from '../../../features/categories/core/models';
import { CategoryPublicService } from '../../services/category-public.service';

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
  categoriasConProductos: Array<CategorySummaryDto & { productos: ProductoWithHistory[] }> = [];
  authStatus: AuthStatus = AuthStatus.checking;
  AuthStatus = AuthStatus; // Para usar en template

  @ViewChildren('carouselContainer') carouselContainers!: QueryList<ElementRef>;

  constructor(
    private productoService: ProductPublicService,
    private categoriaService: CategoryPublicService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Suscribirse al estado de autenticación para mostrar loader si está en checking
    this.authService.authStatus$.subscribe(status => {
      this.authStatus = status;
    });
    // Consultar categorías y productos en paralelo
    Promise.all([
      this.categoriaService.getPublicCategories().toPromise(),
      this.productoService.getProducts().toPromise()
    ]).then(([categorias, productos]) => {
      // Soportar respuesta paginada o array plano
      const categoriasList = categorias?.content || [];
      const productosList = productos?.content || [];
      // Debug: mostrar cuántos productos y categorías llegan
      console.log('[DEBUG] Categorías recibidas:', categoriasList.length, categoriasList);
      console.log('[DEBUG] Productos recibidos:', productosList.length, productosList);
      // Mockear historial de stock para cada producto
      productosList.forEach((p: ProductoWithHistory) => {
        // Si ya existe un historial real, no lo sobrescribas
        if (!('stockHistory' in p)) {
          // Genera un historial de 7 días con variaciones aleatorias
          const base = p.stock;
          p.stockHistory = Array.from({ length: 7 }).map((_, i) => Math.max(0, base - Math.floor(Math.random() * 3) + i));
        }
      });
      this.categoriasConProductos = (categoriasList || []).map((cat: CategorySummaryDto) => ({
        ...cat,
        productos: productosList.filter((p: ProductoWithHistory) => p.categoriaId === cat.id)
      })).filter((cat: any) => cat.productos.length > 0);
      // Debug: mostrar resultado final de categorías con productos
      console.log('[DEBUG] Categorías con productos:', this.categoriasConProductos.length, this.categoriasConProductos);
      if (productosList.length === 0) {
        console.warn('[DEBUG] No se recibieron productos del backend.');
      }
      if (this.categoriasConProductos.length === 0) {
        console.warn('[DEBUG] No hay categorías con productos para mostrar.');
      }
    });
  }

  ngAfterViewInit(): void {
    // Inyectar script de partículas premium para fondo animado
    const id = 'home-bg-particles-script';
    if (!document.getElementById(id)) {
      const s = document.createElement('script');
      s.id = id;
      s.src = 'assets/js/home-bg-particles.js';
      s.async = true;
      document.body.appendChild(s);
    }
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
    // Si la imagen viene como "/api/public/productos/img/archivo.jpg" o similar, anteponer baseUrl
    return `${environment.baseUrl}${imagePath}`;
  }

  onImgError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://via.placeholder.com/400x180?text=Imagen+no+disponible';
  }

  addToCart(product: ProductoWithHistory) {
    // Tu lógica de agregar al carrito
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(amount);
  }
}
