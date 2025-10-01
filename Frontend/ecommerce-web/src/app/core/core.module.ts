import { NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { SharedModule } from '../shared/shared.module';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { NavbarShellComponent } from './navigation/navbar-shell.component';
import { NavbarBrandComponent } from './navigation/navbar-brand.component';
import { NavbarSearchComponent } from './navigation/navbar-search.component';
import { NavbarLinksComponent } from './navigation/navbar-links.component';
import { NavbarCartIndicatorComponent } from './navigation/navbar-cart-indicator.component';
import { NavbarUserMenuComponent } from './navigation/navbar-user-menu.component';
import { NavbarThemeToggleComponent } from './navigation/navbar-theme-toggle.component';
import { CartFlyoutComponent } from '../features/cart/components/cart-flyout/cart-flyout.component';
import { CartStore } from '../state/cart.store';

@NgModule({
  declarations: [
    MainLayoutComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    NavbarShellComponent,
    NavbarBrandComponent,
    NavbarSearchComponent,
    NavbarLinksComponent,
    NavbarCartIndicatorComponent,
    NavbarUserMenuComponent,
    NavbarThemeToggleComponent,
    CartFlyoutComponent
  ],
  imports: [CommonModule, RouterModule, FormsModule, SharedModule],
  exports: [
    MainLayoutComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    NavbarShellComponent
  ],
  providers: [CartStore]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parent: CoreModule) {
    if (parent) {
      throw new Error('CoreModule ya fue cargado. Importe solo en AppModule.');
    }
  }
}
