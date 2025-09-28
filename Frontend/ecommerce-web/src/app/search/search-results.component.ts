
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../shared/services/cart.service';
import { PaginaProducto } from '../producto/interfaces/pagina-producto';
import { Producto } from '../producto/interfaces/producto';
import Swal from 'sweetalert2';
import { ProductoPublicService } from '../producto/service/producto.service.public';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent implements OnInit {
  query: string = '';

  filteredProducts: Producto[] = [];
  cartProducts: Producto[] = [];
  page = 0;
  size = 12;
  totalPages = 0;
  totalElements = 0;


  constructor(
    private route: ActivatedRoute,
    private cartService: CartService,
    private productoService: ProductoPublicService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params['q'] || '';
      this.buscarProductos();
    });
  }

  buscarProductos() {
    const term = this.query.trim();
    if (term.length === 0) {
      this.filteredProducts = [];
      return;
    }
    // Aquí se asume que el backend soporta búsqueda por nombre/descripción vía query param
    this.productoService.getProductosPaginados(0, this.size).subscribe({
      next: (resp: PaginaProducto) => {
        this.filteredProducts = resp.content.filter((p: Producto) =>
          (p.nombre && p.nombre.toLowerCase().includes(term.toLowerCase())) ||
          (p.descripcion && p.descripcion.toLowerCase().includes(term.toLowerCase()))
        );
        this.totalPages = resp.totalPages;
        this.totalElements = resp.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudieron buscar productos.' });
      }
    });
  }


  addToCart(product: Producto) {
    if (!this.cartProducts.find(p => p.id === product.id)) {
      this.cartProducts.push(product);
      this.cartService.setCartCount(this.cartProducts.length);
      Swal.fire({ icon: 'success', title: 'Agregado', text: 'Producto agregado al carrito', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
    }
  }


  removeFromCart(product: Producto) {
    this.cartProducts = this.cartProducts.filter(p => p.id !== product.id);
    this.cartService.setCartCount(this.cartProducts.length);
    Swal.fire({ icon: 'info', title: 'Eliminado', text: 'Producto eliminado del carrito', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
  }


  getCartTotal(): number {
    return this.cartProducts.reduce((total, p) => total + (p.precio || 0), 0);
  }


  isInCart(product: Producto): boolean {
    return this.cartProducts.some(p => p.id === product.id);
  }

  // Para datos reales, reemplaza allProducts por llamada a API y cartProducts por persistencia global
}
