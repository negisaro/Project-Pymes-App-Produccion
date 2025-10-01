import { Injectable } from '@angular/core';
import { ProductTileModel } from '../components/product-tile/product-tile.component';

@Injectable({ providedIn: 'root' })
export class CatalogDataService {
  async list(): Promise<ProductTileModel[]> {
    // Simulación de retardo + datos mock
    await new Promise(r => setTimeout(r, 500));
    return Array.from({ length: 24 }).map((_, i) => ({
      id: 'p-' + (i+1),
      name: 'Producto ' + (i+1),
      price: Number((Math.random() * 90 + 10).toFixed(2)),
      image: `https://picsum.photos/seed/prod${i}/400/400`,
      badge: i % 7 === 0 ? 'Nuevo' : (i % 5 === 0 ? 'Oferta' : undefined)
    }));
  }
}
