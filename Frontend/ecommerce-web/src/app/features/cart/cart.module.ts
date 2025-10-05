import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CartRoutingModule } from './cart.routing';
import { CartFlyoutComponent } from './components/cart-flyout/cart-flyout.component';

@NgModule({
  imports: [CommonModule, FormsModule, CartRoutingModule],
  declarations: [CartFlyoutComponent],
  exports: [CartFlyoutComponent]
})
export class CartModule {}
