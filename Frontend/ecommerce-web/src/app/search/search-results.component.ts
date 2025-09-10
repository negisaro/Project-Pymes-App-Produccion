// ...existing imports...
// El método isInCart ya está implementado dentro de la clase SearchResultsComponent más abajo
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../shared/services/cart.service';

interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  image: string;
}

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent implements OnInit {
  query: string = '';
  filteredProducts: Product[] = [];
  cartProducts: Product[] = [];

  // Simulación de productos, reemplazar por llamada a API en el futuro
  allProducts: Product[] = [
    { id: 1, name: 'Aceite de Oliva Extra Virgen', description: 'Botella 500ml, calidad premium.', price: 28900, image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80' },
    { id: 2, name: 'Mantecado Artesanal', description: 'Dulce tradicional, caja de 12 unidades.', price: 15500, image: 'https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80' },
    { id: 3, name: 'Crema Facial Hidratante', description: 'Crema con ácido hialurónico para todo tipo de piel.', price: 34900, image: 'https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?auto=format&fit=crop&w=400&q=80' },
    { id: 4, name: 'Shampoo Nutritivo', description: 'Shampoo con extracto de argán, fortalece y da brillo.', price: 18900, image: 'https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=400&q=80' },
    { id: 5, name: 'Labial Mate', description: 'Color intenso y larga duración, variedad de tonos.', price: 12500, image: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80' },
    { id: 6, name: 'Gel Limpiador Facial', description: 'Elimina impurezas y deja la piel fresca y suave.', price: 9900, image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80' },
    { id: 7, name: 'Café Premium Molido', description: 'Paquete de 500g, aroma intenso y sabor único.', price: 18500, image: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=400&q=80' },
    { id: 8, name: 'Jabón Artesanal de Coco', description: 'Barra natural, hidratante y suave para la piel.', price: 7900, image: 'https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80' },
    { id: 9, name: 'Té Verde Orgánico', description: 'Caja de 20 sobres, antioxidante y refrescante.', price: 11200, image: 'https://images.unsplash.com/photo-1465101178521-c1a4c8a0f8d9?auto=format&fit=crop&w=400&q=80' },
    { id: 10, name: 'Proteína Vegetal', description: 'Bolsa de 1kg, ideal para dietas saludables.', price: 45900, image: 'https://images.unsplash.com/photo-1519864600265-abb23847ef2c?auto=format&fit=crop&w=400&q=80' },
    { id: 11, name: 'Desodorante Natural', description: 'Sin aluminio, aroma fresco y duradero.', price: 13900, image: 'https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?auto=format&fit=crop&w=400&q=80' },
    { id: 12, name: 'Miel de Abeja Pura', description: 'Frasco de 350g, endulzante natural.', price: 16900, image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80' },
    { id: 13, name: 'Agua Micelar', description: 'Limpieza facial suave, apta para todo tipo de piel.', price: 15900, image: 'https://images.unsplash.com/photo-1522337660859-02fbefca4702?auto=format&fit=crop&w=400&q=80' },
    { id: 14, name: 'Mascarilla Capilar', description: 'Nutre y repara el cabello, presentación 250ml.', price: 21900, image: 'https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80' },
    { id: 15, name: 'Aceite Esencial de Lavanda', description: 'Frasco de 30ml, relajante y aromático.', price: 24900, image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80' }
  ];

  constructor(private route: ActivatedRoute, private cartService: CartService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params['q'] || '';
      this.filterProducts();
    });
  }

  filterProducts() {
    const term = this.query.trim().toLowerCase();
    if (term.length === 0) {
      this.filteredProducts = [];
      return;
    }
    this.filteredProducts = this.allProducts.filter(p =>
      p.name.toLowerCase().includes(term) ||
      p.description.toLowerCase().includes(term)
    );
  }

  addToCart(product: Product) {
    if (!this.cartProducts.find(p => p.id === product.id)) {
      this.cartProducts.push(product);
      this.cartService.setCartCount(this.cartProducts.length);
    }
  }

  removeFromCart(product: Product) {
    this.cartProducts = this.cartProducts.filter(p => p.id !== product.id);
    this.cartService.setCartCount(this.cartProducts.length);
  }

  getCartTotal(): number {
    return this.cartProducts.reduce((total, p) => total + p.price, 0);
  }

  isInCart(product: Product): boolean {
    return this.cartProducts.some(p => p.id === product.id);
  }

  // Para datos reales, reemplaza allProducts por llamada a API y cartProducts por persistencia global
}
