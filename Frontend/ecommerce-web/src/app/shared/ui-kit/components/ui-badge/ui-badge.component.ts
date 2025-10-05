import { Component, Input } from '@angular/core';

type UIBadgeVariant = 'neutral' | 'success' | 'info' | 'warning' | 'danger';

type UIBadgeSize = 'sm' | 'md';

@Component({
  selector: 'ui-badge',
  templateUrl: './ui-badge.component.html',
  styleUrls: ['./ui-badge.component.scss']
})
export class UIBadgeComponent {
  @Input() variant: UIBadgeVariant = 'neutral';
  @Input() size: UIBadgeSize = 'md';
  @Input() pill = false;
  @Input() ariaLabel?: string;

  get classes(): string {
    return [
      'ui-badge',
      `ui-badge--${this.variant}`,
      `ui-badge--${this.size}`,
      this.pill ? 'ui-badge--pill' : ''
    ].filter(Boolean).join(' ');
  }
}
