import { Component, EventEmitter, Input, Output } from '@angular/core';

type UIButtonVariant = 'primary' | 'secondary' | 'outline' | 'danger' | 'link';

type UIButtonSize = 'sm' | 'md' | 'lg';

@Component({
  selector: 'ui-button',
  templateUrl: './ui-button.component.html',
  styleUrls: ['./ui-button.component.scss']
})
export class UIButtonComponent {
  @Input() variant: UIButtonVariant = 'primary';
  @Input() size: UIButtonSize = 'md';
  @Input() disabled = false;
  @Input() loading = false;
  @Input() type: 'button' | 'submit' = 'button';
  @Input() ariaLabel?: string;
  @Output() pressed = new EventEmitter<Event>();

  onClick(event: Event) {
    if (this.disabled || this.loading) {
      event.preventDefault();
      return;
    }
    this.pressed.emit(event);
  }

  get classes(): string {
    return [
      'ui-btn',
      `ui-btn--${this.variant}`,
      `ui-btn--${this.size}`,
      this.loading ? 'ui-btn--loading' : '',
      this.disabled ? 'ui-btn--disabled' : ''
    ].filter(Boolean).join(' ');
  }
}
