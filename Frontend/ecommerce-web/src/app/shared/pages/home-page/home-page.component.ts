
import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { ProductoService, PaginaProducto } from '../../../producto/service/producto.service';
import { Producto } from '../../../producto/interfaces/producto';
import { environment } from '../../../../environments/environments';
import Swal from 'sweetalert2';

@Component({
  selector: 'shared-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit {
  availableProducts: Producto[] = [];
  cartProducts: Producto[] = [];
  page = 0;
  size = 12;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cartService: CartService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.updateCartCount();
  }

  cargarProductos(): void {
    this.productoService.getProductosPaginados(this.page, this.size).subscribe({
      next: (resp: PaginaProducto) => {
        this.availableProducts = resp.content;
        this.totalPages = resp.totalPages;
        this.totalElements = resp.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudieron cargar los productos.' });
      }
    });
  }

  addToCart(product: Producto) {
    if (!this.cartProducts.find(p => p.id === product.id)) {
      this.cartProducts.push(product);
      this.updateCartCount();
      Swal.fire({ icon: 'success', title: 'Agregado', text: 'Producto agregado al carrito', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
    }
  }

  removeFromCart(product: Producto) {
    this.cartProducts = this.cartProducts.filter(p => p.id !== product.id);
    this.updateCartCount();
    Swal.fire({ icon: 'info', title: 'Eliminado', text: 'Producto eliminado del carrito', timer: 1200, showConfirmButton: false, toast: true, position: 'top-end' });
  }

  updateCartCount() {
    this.cartService.setCartCount(this.cartProducts.length);
  }

  getCartTotal(): number {
    return this.cartProducts.reduce((total, p) => total + (p.precio || 0), 0);
  }

  pagarConWompi() {
    const total = this.getCartTotal();
    if (total <= 0) {
      Swal.fire({ icon: 'info', title: 'Carrito vacío', text: 'Agrega productos antes de pagar.', timer: 1500, showConfirmButton: false, toast: true, position: 'top-end' });
      return;
    }
    fetch('http://localhost:8080/api/payments/wompi-checkout', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ amount: total * 100 })
    })
      .then(response => response.json())
      .then(data => {
        if (data.checkoutUrl) {
          window.location.href = data.checkoutUrl;
        } else {
          Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo generar el pago.' });
        }
      })
      .catch(() => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error al conectar con el servidor de pagos.' });
      });
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath) return 'https://via.placeholder.com/400x180?text=Sin+imagen';
    if (imagePath.startsWith('http')) return imagePath;
    return `${environment.baseUrl}${imagePath}`;
  }

  onImgError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://via.placeholder.com/400x180?text=Imagen+no+disponible';
  }

  finalizarCompra() {
    Swal.fire({
      icon: 'info',
      title: 'En desarrollo',
      text: 'La pasarela de pago está en proceso de implementación. Disculpe las molestias.',
      timer: 2000,
      showConfirmButton: false,
      toast: true,
      position: 'top-end'
    }).then(() => {
      this.router.navigate(['/dashboard/productos']);
    });
  }
}
