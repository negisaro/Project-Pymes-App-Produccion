import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

/**
 * @deprecated Servicio temporal - Será reemplazado por la nueva implementación de Clean Architecture
 * Este servicio existe solo para evitar errores de compilación durante la migración
 */
@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartCountSubject = new BehaviorSubject<number>(0);
  public cartCount$ = this.cartCountSubject.asObservable();

  setCartCount(count: number): void {
    this.cartCountSubject.next(count);
  }

  getCartCount(): number {
    return this.cartCountSubject.value;
  }
}