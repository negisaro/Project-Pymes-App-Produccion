import { Component, ChangeDetectionStrategy, HostBinding, Signal, signal } from '@angular/core';
import { CartFacade } from '../../../features/cart/presentation/facades/cart.facade';

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

  constructor(public cartFacade: CartFacade) {}

  toggleMobile() { this.mobileOpen.update(v => !v); }
  closeMobile() { this.mobileOpen.set(false); }
}
