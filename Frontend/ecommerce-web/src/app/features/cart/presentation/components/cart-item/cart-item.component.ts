import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CartItem } from '../../../core/models/cart-item';

/**
 * 📦 Componente temporal: Cart Item
 */
@Component({
  selector: 'app-cart-item',
  template: `
    <div class="cart-item">
      <h4>Item (Temporal)</h4>
      <p>Implementación en desarrollo...</p>
    </div>
  `,
  styles: [`
    .cart-item {
      padding: 0.5rem;
      border-bottom: 1px solid #eee;
    }
  `]
})
export class CartItemComponent {
  @Input() item!: CartItem;
  @Input() loading = false;
  @Output() quantityChange = new EventEmitter<number>();
  @Output() remove = new EventEmitter<void>();
}