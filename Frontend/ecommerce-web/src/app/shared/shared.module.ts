import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgApexchartsModule } from 'ng-apexcharts';
import { AboutPageComponent } from './pages/about-page/about-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProductSparklineComponent } from './components/product-sparkline/product-sparkline.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AdminSidebarComponent } from '../features/admin-dashboard/components/admin-sidebar/admin-sidebar.component';
import { AdminTopbarComponent } from '../features/admin-dashboard/components/admin-topbar/admin-topbar.component';
import { UIButtonComponent, UIBadgeComponent } from './ui-kit/components';
import { CartModule } from '../features/cart/cart.module';
import { NavbarShellComponent } from './components/navigation/navbar-shell.component';
import { NavbarBrandComponent } from './components/navigation/navbar-brand.component';
import { NavbarSearchComponent } from './components/navigation/navbar-search.component';
import { NavbarLinksComponent } from './components/navigation/navbar-links.component';
import { NavbarCartIndicatorComponent } from './components/navigation/navbar-cart-indicator.component';
import { NavbarUserMenuComponent } from './components/navigation/navbar-user-menu.component';
import { NavbarThemeToggleComponent } from './components/navigation/navbar-theme-toggle.component';

@NgModule({
  declarations: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent,
    AdminSidebarComponent,
    AdminTopbarComponent,
    NavbarShellComponent,
    NavbarBrandComponent,
    NavbarSearchComponent,
    NavbarLinksComponent,
    NavbarCartIndicatorComponent,
    NavbarUserMenuComponent,
    NavbarThemeToggleComponent,
    // UI Kit Components
    UIButtonComponent,
    UIBadgeComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    NgApexchartsModule,
    CartModule
  ],
  exports: [
    // Módulos
    CommonModule,
    RouterModule,
    FormsModule,
    NgApexchartsModule,
    // Componentes
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent,
    AdminSidebarComponent,
    AdminTopbarComponent,
    NavbarShellComponent,
    NavbarBrandComponent,
    NavbarSearchComponent,
    NavbarLinksComponent,
    NavbarCartIndicatorComponent,
    NavbarUserMenuComponent,
    NavbarThemeToggleComponent,
    // UI Kit Components
    UIButtonComponent,
    UIBadgeComponent,
    CartModule
  ]
})
export class SharedModule { }
// Nota: Imports duplicados movidos al inicio para evitar errores de compilación.
