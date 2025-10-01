import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { ProductTileModel } from '../../components/product-tile/product-tile.component';
import { CatalogDataService } from '../../services/catalog-data.service';

@Component({
  selector: 'app-catalog-page',
  templateUrl: './catalog-page.component.html',
  styleUrls: ['./catalog-page.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CatalogPageComponent implements OnInit {
  loading = signal(true);
  products = signal<ProductTileModel[]>([]);

  constructor(private data: CatalogDataService) {}

  ngOnInit(): void {
  this.data.list().then((items: ProductTileModel[]) => { this.products.set(items); this.loading.set(false); });
  }
}
