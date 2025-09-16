import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProveedorRoutingModule } from './proveedor-routing.module';
import { SharedModule } from '../shared/shared.module';
import { ListProveedorComponent } from './pages/list-proveedor/list-proveedor.component';
import { AddProveedorComponent } from './pages/add-proveedor/add-proveedor.component';
import { ProveedorLayoutComponent } from './proveedor-layout/proveedor-layout.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ListProveedorComponent,
    AddProveedorComponent,
    ProveedorLayoutComponent,
  ],
  imports: [
    CommonModule,
    ProveedorRoutingModule,
  ReactiveFormsModule,
  FormsModule,
    SharedModule,
  ],
})
export class ProveedorModule {}
