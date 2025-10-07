import { Component, Input } from '@angular/core';

/**
 * Componente reutilizable: Empty State
 * Puede usarse para carrito, favoritos, pedidos, etc.
 */
@Component({
  selector: 'app-empty-state',
  template: `
    <div class="empty-state">
      <h3>{{ title }}</h3>
      <p>{{ description }}</p>
      <button *ngIf="buttonText" type="button" class="btn-primary" (click)="onButtonClick()">
        {{ buttonText }}
      </button>
    </div>
  `,
  styles: [`
    .empty-state {
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
export class EmptyStateComponent {
  @Input() title = 'Sin elementos';
  @Input() description = 'No hay elementos para mostrar.';
  @Input() buttonText?: string;
  @Input() buttonAction?: () => void;

  onButtonClick() {
    if (this.buttonAction) {
      this.buttonAction();
    }
  }
}
