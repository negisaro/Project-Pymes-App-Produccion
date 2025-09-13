
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { AddProductoComponent } from './pages/add-producto/add-producto.component';
import { ListProductoComponent } from './pages/list-producto/list-producto.component';
import { ProductoLayoutComponent } from './producto-layout/producto-layout.component';
import { ProductoRoutingModule } from './producto-routing.module';


@NgModule({
  declarations: [
    AddProductoComponent,
    ListProductoComponent,
    ProductoLayoutComponent
  ],
  imports: [
    CommonModule,
    ProductoRoutingModule,
    ReactiveFormsModule,
    SharedModule
  ],
})
export class ProductoModule { }
