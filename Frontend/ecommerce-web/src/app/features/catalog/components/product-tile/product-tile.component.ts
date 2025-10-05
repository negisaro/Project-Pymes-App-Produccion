import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CartStore } from '../../../../core/state/cart.store';

export interface ProductTileModel {
  id: string;
  name: string;
  price: number;
  image?: string;
  badge?: string;
}

@Component({
  selector: 'app-product-tile',
  templateUrl: './product-tile.component.html',
  styleUrls: ['./product-tile.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductTileComponent {
  @Input() product!: ProductTileModel;
  @Output() view = new EventEmitter<ProductTileModel>();

  constructor(private cart: CartStore) {}

  add(){
    if(!this.product) return;
    this.cart.add({ id: this.product.id, name: this.product.name, price: this.product.price, qty: 1, thumbnailUrl: this.product.image });
  }
}
