import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FooterComponent } from './components/footer';
import { NavbarComponent } from './components/navbar';
import { SidebarComponent } from './components/sidebar';
import { AboutPageComponent } from './pages/about-page/about-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';



@NgModule({
  declarations: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
  ],
  exports: [
    AboutPageComponent,
    FooterComponent,
    HomePageComponent,
    NavbarComponent,
    SidebarComponent,
  ]
})
export class SharedModule { }
