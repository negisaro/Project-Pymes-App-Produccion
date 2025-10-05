import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsListComponent } from './pages/products-list/products-list.component';
import { ProductFormComponent } from './pages/product-form/product-form.component';

@NgModule({
  declarations: [
    ProductsListComponent,
    ProductFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ProductsRoutingModule
  ]
})
export class ProductsModule { }
