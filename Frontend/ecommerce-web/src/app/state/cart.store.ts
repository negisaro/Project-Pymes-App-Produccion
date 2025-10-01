import { Injectable } from '@angular/core';
import { signal, computed } from '@angular/core';

export interface CartItem {
  id: string;
  name: string;
  price: number;
  qty: number;
  thumbnailUrl?: string;
  maxQty?: number;
}

@Injectable({ providedIn: 'root' })
export class CartStore {
  private readonly _items = signal<CartItem[]>(this.restore());

  readonly items = computed(() => this._items());
  readonly total = computed(() => this._items().reduce((sum, i) => sum + i.price * i.qty, 0));
  readonly count = computed(() => this._items().reduce((sum, i) => sum + i.qty, 0));

  add(item: CartItem) {
    this._items.update(list => {
      const existing = list.find(i => i.id === item.id);
      if (existing) {
        return list.map(i => i.id === item.id ? { ...i, qty: Math.min((i.qty + item.qty), i.maxQty ?? (i.qty + item.qty)) } : i);
      }
      return [...list, item];
    });
    this.persist();
  }

  updateQty(id: string, qty: number) {
    if (qty <= 0) { this.remove(id); return; }
    this._items.update(list => list.map(i => i.id === id ? { ...i, qty } : i));
    this.persist();
  }

  remove(id: string) {
    this._items.update(list => list.filter(i => i.id !== id));
    this.persist();
  }

  clear() {
    this._items.set([]);
    this.persist();
  }

  private persist() {
    try { localStorage.setItem('app.cart', JSON.stringify(this._items())); } catch {}
  }

  private restore(): CartItem[] {
    try {
      const raw = localStorage.getItem('app.cart');
      return raw ? JSON.parse(raw) : [];
    } catch { return []; }
  }
}
