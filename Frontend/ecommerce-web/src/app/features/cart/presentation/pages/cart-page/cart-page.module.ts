import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { CartPageComponent } from './cart-page.component';

@NgModule({
  declarations: [
    CartPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', component: CartPageComponent }
    ])
  ]
})
export class CartPageModule { }