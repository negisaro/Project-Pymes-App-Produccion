import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { Role, RoleName, User } from '../../../user/interfaces/user.interface';
import { AuthService } from '../../../auth/services/auth.service';
// Verifica que la ruta sea correcta y el archivo exista


@Component({
  selector: 'shared-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent {
  async pagarConWompi() {
    const total = this.getCartTotal();
    if (total <= 0) {
      window.alert('El carrito está vacío.');
      return;
    }
    try {
      const response = await fetch('http://localhost:8080/api/payments/wompi-checkout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount: total * 100 }) // Wompi espera centavos
      });
      const data = await response.json();
      if (data.checkoutUrl) {
        window.location.href = data.checkoutUrl;
      } else {
        window.alert('No se pudo generar el pago.');
      }
    } catch (err) {
      window.alert('Error al conectar con el servidor de pagos.');
    }
  }
  constructor(
    private authService: AuthService,
    private router: Router,
    private cartService: CartService
  ) {
    this.user = this.authService.getCurrentUser();
    this.updateCartCount();
  }
  onImgError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'https://via.placeholder.com/400x180?text=Imagen+no+disponible';
  }
  finalizarCompra() {
    if (window.confirm('La pasarela de pago está en proceso de implementación. Disculpe las molestias.\n\n¿Desea volver al inicio de compras?')) {
      this.router.navigate(['/dashboard/productos']);
    }
  }
  availableProducts: Array<{id:number, name:string, description:string, price:number, image:string}> = [
    {
      id: 1,
      name: 'Aceite de Oliva Extra Virgen',
      description: 'Botella 500ml, calidad premium.',
      price: 28900,
      image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 2,
      name: 'Mantecado Artesanal',
      description: 'Dulce tradicional, caja de 12 unidades.',
      price: 15500,
      image: 'https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 3,
      name: 'Crema Facial Hidratante',
      description: 'Crema con ácido hialurónico para todo tipo de piel.',
      price: 34900,
      image: 'https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 4,
      name: 'Shampoo Nutritivo',
      description: 'Shampoo con extracto de argán, fortalece y da brillo.',
      price: 18900,
      image: 'https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 5,
      name: 'Labial Mate',
      description: 'Color intenso y larga duración, variedad de tonos.',
      price: 12500,
      image: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 6,
      name: 'Gel Limpiador Facial',
      description: 'Elimina impurezas y deja la piel fresca y suave.',
      price: 9900,
      image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 7,
      name: 'Café Premium Molido',
      description: 'Paquete de 500g, aroma intenso y sabor único.',
      price: 18500,
      image: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 8,
      name: 'Jabón Artesanal de Coco',
      description: 'Barra natural, hidratante y suave para la piel.',
      price: 7900,
      image: 'https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 9,
      name: 'Té Verde Orgánico',
      description: 'Caja de 20 sobres, antioxidante y refrescante.',
      price: 11200,
      image: 'https://images.unsplash.com/photo-1465101178521-c1a4c8a0f8d9?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 10,
      name: 'Proteína Vegetal',
      description: 'Bolsa de 1kg, ideal para dietas saludables.',
      price: 45900,
      image: 'https://images.unsplash.com/photo-1519864600265-abb23847ef2c?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 11,
      name: 'Desodorante Natural',
      description: 'Sin aluminio, aroma fresco y duradero.',
      price: 13900,
      image: 'https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 12,
      name: 'Miel de Abeja Pura',
      description: 'Frasco de 350g, endulzante natural.',
      price: 16900,
      image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 13,
      name: 'Agua Micelar',
      description: 'Limpieza facial suave, apta para todo tipo de piel.',
      price: 15900,
      image: 'https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 14,
      name: 'Mascarilla Capilar',
      description: 'Nutre y repara el cabello, presentación 250ml.',
      price: 21900,
      image: 'https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80'
    },
    {
      id: 15,
      name: 'Aceite Esencial de Lavanda',
      description: 'Frasco de 30ml, relajante y aromático.',
      price: 24900,
      image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80'
    }
  ];

  cartProducts: Array<{id:number, name:string, description:string, price:number, image:string}> = [];

  addToCart(product: any) {
    if (!this.cartProducts.find(p => p.id === product.id)) {
      this.cartProducts.push(product);
  this.updateCartCount();
    }
  }
  // cartProducts ahora inicia vacío y se llena con addToCart

  removeFromCart(product: any) {
    this.cartProducts = this.cartProducts.filter(p => p.id !== product.id);
  this.updateCartCount();
  }
  updateCartCount() {
    this.cartService.setCartCount(this.cartProducts.length);
  }

  getCartTotal(): number {
    return this.cartProducts.reduce((total, p) => total + p.price, 0);
  }
  user: User | null = null;

  isAdmin(): boolean {
  return this.user?.roles?.some((r: Role) => r.name === RoleName.ADMIN) ?? false;
  }
  isSupervisor(): boolean {
  return this.user?.roles?.some((r: Role) => r.name === RoleName.SUPERVISOR) ?? false;
  }
  isUser(): boolean {
  return this.user?.roles?.some((r: Role) => r.name === RoleName.USER) ?? false;
  }
  isEmpleado(): boolean {
  return this.user?.roles?.some((r: Role) => r.name === RoleName.EMPLEADO) ?? false;
  }
}
