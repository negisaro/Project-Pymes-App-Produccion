import { Component, Input } from '@angular/core';
import { CartTotals } from '../../../core/use-cases/calculate-cart-totals.use-case';

/**
 * 💰 Componente temporal: Cart Summary
 */
@Component({
  selector: 'app-cart-summary',
  template: `
    <div class="cart-summary">
      <h4>Resumen (Temporal)</h4>
      <p>Implementación en desarrollo...</p>
    </div>
  `,
  styles: [`
    .cart-summary {
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 4px;
    }
  `]
})
export class CartSummaryComponent {
  @Input() totals!: CartTotals;
  @Input() loading = false;
}