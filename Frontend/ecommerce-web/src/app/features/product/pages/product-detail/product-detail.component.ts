import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title, Meta } from '@angular/platform-browser';
import { CartStore } from '../../../../state/cart.store';
import { ProductDetailService, ProductDetailModel } from '../../services/product-detail.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductDetailComponent implements OnInit {
  loading = signal(true);
  qty = signal(1);
  product = signal<ProductDetailModel | null>(null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ProductDetailService,
    private cart: CartStore,
    private title: Title,
    private meta: Meta
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if(!id){ this.router.navigateByUrl('/catalogo'); return; }
  this.service.get(id).then((p: ProductDetailModel | null) => {
      if(!p){ this.router.navigateByUrl('/catalogo'); return; }
      this.product.set(p);
      this.loading.set(false);
      this.applySeo(p);
    });
  }

  applySeo(p: ProductDetailModel){
    this.title.setTitle(p.name + ' | PymesApp');
    this.meta.updateTag({ name: 'description', content: `Compra ${p.name} al mejor precio. Envío rápido y seguro.` });
  }

  add(){
    const p = this.product();
    if(!p) return;
    this.cart.add({ id: p.id, name: p.name, price: p.price, qty: this.qty(), thumbnailUrl: p.images[0] });
  }

  onQtyInput(evt: Event){
    const value = +(evt.target as HTMLInputElement).value;
    if(Number.isNaN(value) || value < 1){
      this.qty.set(1);
      return;
    }
    this.qty.set(value);
  }
}
