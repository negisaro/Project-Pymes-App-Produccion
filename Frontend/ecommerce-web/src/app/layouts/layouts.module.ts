import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { AdminLayoutComponent } from './admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './auth-layout/auth-layout.component';
import { ClientLayoutComponent } from './client-layout/cliente-layout.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    MainLayoutComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    ClientLayoutComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule
  ],
  exports: [
    MainLayoutComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    ClientLayoutComponent
  ]
})
export class LayoutsModule { }
