import { Component, ChangeDetectionStrategy, HostBinding, Signal, signal } from '@angular/core';
import { CartStore } from '../../../core/state/cart.store';

@Component({
  selector: 'app-navbar-shell',
  templateUrl: './navbar-shell.component.html',
  styleUrls: ['./navbar-shell.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarShellComponent {
  mobileOpen = signal(false);
  cartOpen = false;
  @HostBinding('class.is-open') get opened() { return this.mobileOpen(); }

  constructor(public cart: CartStore) {}

  toggleMobile() { this.mobileOpen.update(v => !v); }
  closeMobile() { this.mobileOpen.set(false); }
}
