import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-navbar-cart-indicator',
  template: `
    <button type="button" class="cart-btn" aria-label="Carrito" (click)="openRequested.emit()">
      🛒 <span class="badge" *ngIf="count as c">{{ c }}</span>
    </button>
  `,
  styles: [`.cart-btn{position:relative;display:inline-flex;align-items:center;gap:.25rem;background:none;border:0;cursor:pointer;font-size:1.1rem;}
  .badge{min-width:18px;height:18px;display:inline-flex;align-items:center;justify-content:center;font-size:.65rem;background:var(--color-primary);color:#fff;border-radius:999px;padding:0 4px;position:absolute;top:-6px;right:-10px;}`],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarCartIndicatorComponent { 
  @Input() count = 0; 
  @Output() openRequested = new EventEmitter<void>(); 
}
