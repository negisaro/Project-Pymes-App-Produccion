import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Cart } from '../models/cart';
import { Money } from '../../../../shared/value-objects/money';

/**
 * 💰 Información de totales del carrito
 */
export interface CartTotals {
  subtotal: Money;
  tax: Money;
  shipping: Money;
  discounts: Money;
  total: Money;
  itemCount: number;
  uniqueItemCount: number;
}

/**
 * 🎯 Caso de Uso: Calcular Totales del Carrito
 * 
 * Maneja el cálculo de todos los totales del carrito incluyendo:
 * - Subtotal
 * - Impuestos
 * - Envío
 * - Descuentos
 * - Total final
 */
@Injectable({
  providedIn: 'root'
})
export class CalculateCartTotalsUseCase {
  
  // Configuración de impuestos (IVA en Colombia)
  private readonly TAX_RATE = 0.19; // 19%
  
  // Configuración de envío gratuito
  private readonly FREE_SHIPPING_THRESHOLD = 150000; // $150,000 COP
  private readonly SHIPPING_COST = 15000; // $15,000 COP
  
  /**
   * Ejecuta el cálculo de totales
   * @param cart Carrito para calcular totales
   * @returns Observable con los totales calculados
   */
  execute(cart: Cart): Observable<CartTotals> {
    try {
      const totals = this.calculateTotals(cart);
      return of(totals);
    } catch (error) {
      console.error('Error calculando totales:', error);
      // Retornar totales en cero en caso de error
      return of(this.getEmptyTotals());
    }
  }
  
  /**
   * Calcula todos los totales del carrito
   */
  private calculateTotals(cart: Cart): CartTotals {
    // 1. Subtotal (suma de todos los items)
    const subtotal = cart.subtotal;
    
    // 2. Descuentos (por ahora cero, se puede extender)
    const discounts = Money.zero('COP');
    
    // 3. Base gravable (subtotal - descuentos)
    const taxableAmount = subtotal.subtract(discounts);
    
    // 4. Impuestos (IVA)
    const tax = taxableAmount.multiply(this.TAX_RATE);
    
    // 5. Envío
    const shipping = this.calculateShipping(subtotal);
    
    // 6. Total final
    const total = subtotal
      .add(tax)
      .add(shipping)
      .subtract(discounts);
    
    return {
      subtotal,
      tax,
      shipping,
      discounts,
      total,
      itemCount: cart.itemCount,
      uniqueItemCount: cart.uniqueItemCount
    };
  }
  
  /**
   * Calcula el costo de envío
   */
  private calculateShipping(subtotal: Money): Money {
    // Envío gratuito para compras mayores al umbral
    if (subtotal.amount >= this.FREE_SHIPPING_THRESHOLD) {
      return Money.zero('COP');
    }
    
    // Envío gratuito para carritos vacíos
    if (subtotal.isZero()) {
      return Money.zero('COP');
    }
    
    // Costo fijo de envío
    return Money.create(this.SHIPPING_COST, 'COP');
  }
  
  /**
   * Retorna totales vacíos para casos de error
   */
  private getEmptyTotals(): CartTotals {
    const zero = Money.zero('COP');
    
    return {
      subtotal: zero,
      tax: zero,
      shipping: zero,
      discounts: zero,
      total: zero,
      itemCount: 0,
      uniqueItemCount: 0
    };
  }
  
  /**
   * Calcula solo el total rápido (para displays simples)
   */
  calculateQuickTotal(cart: Cart): Money {
    try {
      const subtotal = cart.subtotal;
      const tax = subtotal.multiply(this.TAX_RATE);
      const shipping = this.calculateShipping(subtotal);
      
      return subtotal.add(tax).add(shipping);
    } catch (error) {
      console.error('Error en cálculo rápido:', error);
      return Money.zero('COP');
    }
  }
  
  /**
   * Verifica si el carrito califica para envío gratuito
   */
  qualifiesForFreeShipping(cart: Cart): boolean {
    return cart.subtotal.amount >= this.FREE_SHIPPING_THRESHOLD;
  }
  
  /**
   * Calcula cuánto falta para envío gratuito
   */
  amountForFreeShipping(cart: Cart): Money | null {
    if (this.qualifiesForFreeShipping(cart)) {
      return null;
    }
    
    const remaining = this.FREE_SHIPPING_THRESHOLD - cart.subtotal.amount;
    return Money.create(remaining, 'COP');
  }
}