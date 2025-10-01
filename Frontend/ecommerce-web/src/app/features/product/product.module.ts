import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductRoutingModule } from './product.routing';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';

@NgModule({
  declarations: [ProductDetailComponent],
  imports: [CommonModule, RouterModule, ProductRoutingModule]
})
export class ProductModule {}
