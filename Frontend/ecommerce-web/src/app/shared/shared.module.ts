
import { AboutPageComponent } from './pages/about-page/about-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { ProductSparklineComponent } from './components/product-sparkline/product-sparkline.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { NgApexchartsModule } from 'ng-apexcharts';



@NgModule({
  declarations: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    NgApexchartsModule
  ],
  exports: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
    ProductSparklineComponent
  ]
})
export class SharedModule { }
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
