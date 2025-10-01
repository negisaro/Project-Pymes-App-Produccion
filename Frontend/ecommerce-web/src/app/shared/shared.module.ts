import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { AboutPageComponent } from './pages/about-page/about-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProductSparklineComponent } from './components/product-sparkline/product-sparkline.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AdminSidebarComponent } from '../admin-dashboard/components/admin-sidebar/admin-sidebar.component';
import { AdminTopbarComponent } from '../admin-dashboard/components/admin-topbar/admin-topbar.component';
import { UiKitModule } from '../ui-kit/ui-kit.module';

@NgModule({
  declarations: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent,
    AdminSidebarComponent,
    AdminTopbarComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    NgApexchartsModule,
    UiKitModule
  ],
  exports: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent,
    AdminSidebarComponent,
    AdminTopbarComponent,
    UiKitModule
  ]
})
export class SharedModule { }
// Nota: Imports duplicados movidos al inicio para evitar errores de compilación.
