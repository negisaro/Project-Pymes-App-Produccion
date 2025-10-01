import { Injectable } from '@angular/core';

export interface ProductDetailModel {
  id: string;
  name: string;
  price: number;
  description: string;
  images: string[];
}

@Injectable({ providedIn: 'root' })
export class ProductDetailService {
  async get(id: string): Promise<ProductDetailModel | null> {
    await new Promise(r => setTimeout(r, 400));
    const idx = parseInt(id.replace(/\D/g,''),10) || 1;
    return {
      id,
      name: 'Producto ' + id,
      price: Number((Math.random()*90 + 10).toFixed(2)),
      description: 'Descripción breve y atractiva del producto ' + id + ' con beneficios clave y llamada a la acción.',
      images: [
        `https://picsum.photos/seed/detail${idx}/800/800`,
        `https://picsum.photos/seed/detail${idx+1}/800/800`,
        `https://picsum.photos/seed/detail${idx+2}/800/800`
      ]
    };
  }
}
