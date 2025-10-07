import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CartFacade } from '../../facades/cart.facade';
import { Observable } from 'rxjs';
import { Cart } from '../../../core/models/cart';

@Component({
  selector: 'app-cart-flyout',
  template: `
    <div class="cart-flyout" *ngIf="open">
      <h3>Mi Carrito</h3>
      <ng-container *ngIf="cart$ | async as cart; else emptyCart">
        <div *ngIf="cart.items.length > 0; else emptyCart">
          <div *ngFor="let item of cart.items" class="cart-item">
            <img [src]="item.product.imageUrl" alt="{{item.product.name}}" class="cart-item-img" />
            <div class="cart-item-info">
              <div class="cart-item-title">{{ item.product.name }}</div>
              <div class="cart-item-qty">
                <button (click)="updateQty(cart.userId, item.id.value, item.quantity.value - 1)" [disabled]="item.quantity.value <= 1">-</button>
                <span>{{ item.quantity.value }}</span>
                <button (click)="updateQty(cart.userId, item.id.value, item.quantity.value + 1)">+</button>
              </div>
              <div class="cart-item-price">{{ item.unitPrice.amount | currency:'COP':'symbol':'1.0-0' }}</div>
              <button class="cart-item-remove" (click)="remove(cart.userId, item.id.value)">Eliminar</button>
            </div>
          </div>
          <div class="cart-totals" *ngIf="totals$ | async as totals">
            <div><strong>Subtotal:</strong> {{ totals.subtotal.amount | currency:'COP':'symbol':'1.0-0' }}</div>
            <div><strong>Total:</strong> {{ totals.total.amount | currency:'COP':'symbol':'1.0-0' }}</div>
          </div>
          <button class="btn btn-primary w-100 mt-3" (click)="checkout()">Pagar</button>
        </div>
      </ng-container>
      <ng-template #emptyCart>
        <div class="cart-empty">Tu carrito está vacío.</div>
      </ng-template>
      <button class="cart-flyout-close" (click)="close.emit()">Cerrar</button>
    </div>
  `,
  styles: [
    `
    .cart-flyout { min-width: 320px; max-width: 400px; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-radius: 8px; padding: 1rem; position: relative; }
    .cart-item { display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; }
    .cart-item-img { width: 48px; height: 48px; object-fit: cover; border-radius: 4px; }
    .cart-item-info { flex: 1; }
    .cart-item-title { font-weight: bold; }
    .cart-item-qty { display: flex; align-items: center; gap: 0.5rem; margin: 0.5rem 0; }
    .cart-item-price { font-size: 1rem; color: #1976d2; }
    .cart-item-remove { background: none; border: none; color: #d32f2f; cursor: pointer; margin-left: 0.5rem; }
    .cart-totals { border-top: 1px solid #eee; padding-top: 1rem; margin-top: 1rem; }
    .cart-empty { text-align: center; color: #888; margin: 2rem 0; }
    .cart-flyout-close { position: absolute; top: 8px; right: 8px; background: none; border: none; font-size: 1.2rem; cursor: pointer; }
    `
  ]
})
export class CartFlyoutComponent {
  @Input() open = false;
  @Output() openChange = new EventEmitter<boolean>();
  @Output() close = new EventEmitter<void>();

  cart$: Observable<Cart | null>;
  totals$: Observable<any>;

  constructor(public cartFacade: CartFacade) {
    this.cart$ = this.cartFacade.cart$;
    this.totals$ = this.cartFacade.totals$;
  }

  updateQty(userId: any, itemId: string, newQty: number) {
    if (newQty < 1) return;
    this.cartFacade.updateItemQuantity(userId, itemId, newQty).subscribe();
  }

  remove(userId: any, itemId: string) {
    this.cartFacade.removeItem(userId, itemId).subscribe();
  }

  checkout() {
    // Aquí puedes navegar a la página de checkout o emitir un evento
    // this.router.navigate(['/checkout']);
    alert('Funcionalidad de pago en desarrollo');
  }
}
