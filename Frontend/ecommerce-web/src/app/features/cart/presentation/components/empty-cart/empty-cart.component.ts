import { Component } from '@angular/core';

/**
 * 🛒 Componente temporal: Empty Cart
 */
@Component({
  selector: 'app-empty-cart',
  template: `
    <div class="empty-cart">
      <h3>Carrito vacío</h3>
      <p>No tienes productos en tu carrito aún.</p>
      <button type="button" class="btn-primary">
        Continuar comprando
      </button>
    </div>
  `,
  styles: [`
    .empty-cart {
      text-align: center;
      padding: 2rem;
    }
    .btn-primary {
      padding: 0.5rem 1rem;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  `]
})
export class EmptyCartComponent {
}