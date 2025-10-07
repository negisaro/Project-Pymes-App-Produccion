import { Component, Input, Output, EventEmitter } from '@angular/core';

/**
 * 🔢 Componente temporal: Quantity Selector
 */
@Component({
  selector: 'app-quantity-selector',
  template: `
    <div class="quantity-selector">
      <button type="button" (click)="decrease()">-</button>
      <span>{{ value }}</span>
      <button type="button" (click)="increase()">+</button>
    </div>
  `,
  styles: [`
    .quantity-selector {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    button {
      padding: 0.25rem 0.5rem;
      border: 1px solid #ddd;
      background: white;
      cursor: pointer;
    }
  `]
})
export class QuantitySelectorComponent {
  @Input() value = 1;
  @Input() min = 1;
  @Input() max = 999;
  @Input() disabled = false;
  @Output() valueChange = new EventEmitter<number>();
  
  increase(): void {
    if (this.value < this.max && !this.disabled) {
      this.valueChange.emit(this.value + 1);
    }
  }
  
  decrease(): void {
    if (this.value > this.min && !this.disabled) {
      this.valueChange.emit(this.value - 1);
    }
  }
}