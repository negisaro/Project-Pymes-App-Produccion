import { Injectable, Inject } from '@angular/core';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { map, switchMap, catchError, tap } from 'rxjs/operators';

import { AddItemToCartUseCase, AddItemToCartCommand } from '../../core/use-cases/add-item-to-cart.use-case';
import { UpdateCartItemQuantityUseCase, UpdateCartItemQuantityCommand } from '../../core/use-cases/update-cart-item-quantity.use-case';
import { RemoveCartItemUseCase, RemoveCartItemCommand } from '../../core/use-cases/remove-cart-item.use-case';
import { CalculateCartTotalsUseCase, CartTotals } from '../../core/use-cases/calculate-cart-totals.use-case';

import { CartStateService } from '../../infrastructure/state/cart-state.service';
import { Cart } from '../../core/models/cart';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { IProduct } from '../../shared/types/product.interface';

/**
 * 🎭 Facade del Carrito
 * 
 * Proporciona una API simplificada para que los componentes interactúen
 * con toda la funcionalidad del carrito sin conocer los casos de uso específicos.
 * 
 * Siguiendo el patrón Facade, encapsula la complejidad de los casos de uso
 * y el estado del carrito en una interfaz simple y cohesiva.
 */
@Injectable({
  providedIn: 'root'
})
export class CartFacade {
  
  constructor(
    private cartState: CartStateService,
    private addItemUseCase: AddItemToCartUseCase,
    private updateQuantityUseCase: UpdateCartItemQuantityUseCase,
    private removeItemUseCase: RemoveCartItemUseCase,
    private calculateTotalsUseCase: CalculateCartTotalsUseCase
  ) {}

  // Observables públicos para componentes
  public get cart$() { return this.cartState.cart$; }
  public get loading$() { return this.cartState.loading$; }
  public get error$() { return this.cartState.error$; }
  public get itemCount$() { return this.cartState.itemCount$; }
  
  // Observable de totales calculados
  public get totals$(): Observable<CartTotals | null> {
    return this.cart$.pipe(
      switchMap(cart => {
        if (!cart) {
          return [null];
        }
        return this.calculateTotalsUseCase.execute(cart);
      }),
      catchError(error => {
        console.error('Error calculando totales:', error);
        return [null];
      })
    );
  }
  
  /**
   * Agrega un producto al carrito
   */
  addItem(userId: UserId, product: IProduct, quantity: number): Observable<Cart> {
    this.cartState.setLoading(true);
    this.cartState.clearError();
    
    const command: AddItemToCartCommand = {
      userId,
      product,
      quantity
    };
    
    return this.addItemUseCase.execute(command).pipe(
      tap(cart => {
        this.cartState.setCart(cart);
      }),
      catchError(error => {
        this.cartState.setError(error.message);
        throw error;
      })
    );
  }
  
  /**
   * Actualiza la cantidad de un item
   */
  updateItemQuantity(userId: UserId, itemId: string, newQuantity: number): Observable<Cart> {
    this.cartState.setLoading(true);
    this.cartState.clearError();
    
    const command: UpdateCartItemQuantityCommand = {
      userId,
      itemId: CartItemId.fromString(itemId),
      newQuantity
    };
    
    return this.updateQuantityUseCase.execute(command).pipe(
      tap(cart => {
        this.cartState.setCart(cart);
      }),
      catchError(error => {
        this.cartState.setError(error.message);
        throw error;
      })
    );
  }
  
  /**
   * Elimina un item del carrito
   */
  removeItem(userId: UserId, itemId: string): Observable<Cart> {
    this.cartState.setLoading(true);
    this.cartState.clearError();
    
    const command: RemoveCartItemCommand = {
      userId,
      itemId: CartItemId.fromString(itemId)
    };
    
    return this.removeItemUseCase.execute(command).pipe(
      tap(cart => {
        this.cartState.setCart(cart);
      }),
      catchError(error => {
        this.cartState.setError(error.message);
        throw error;
      })
    );
  }
  
  /**
   * Verifica si un producto está en el carrito
   */
  isProductInCart(productId: number): Observable<boolean> {
    return this.cart$.pipe(
      map(cart => cart ? cart.hasProduct(productId) : false)
    );
  }
  
  /**
   * Obtiene la cantidad de un producto específico en el carrito
   */
  getProductQuantity(productId: number): Observable<number> {
    return this.cart$.pipe(
      map(cart => {
        if (!cart) return 0;
        
        const item = cart.items.find(item => item.product.id === productId);
        return item ? item.quantity.value : 0;
      })
    );
  }
  
  /**
   * Obtiene el total del carrito
   */
  getCartTotal(): Observable<string> {
    return this.totals$.pipe(
      map(totals => totals ? totals.total.format() : '$0')
    );
  }
  
  /**
   * Verifica si el carrito está vacío
   */
  isEmpty(): Observable<boolean> {
    return this.cart$.pipe(
      map(cart => !cart || cart.isEmpty)
    );
  }
  
  /**
   * Limpia el error actual
   */
  clearError(): void {
    this.cartState.clearError();
  }
  
  /**
   * Obtiene el carrito actual (snapshot)
   */
  getCurrentCart(): Cart | null {
    return this.cartState.getCurrentCart();
  }
  
  /**
   * Helper para componentes: Información resumida del carrito
   */
  getCartSummary(): Observable<CartSummaryInfo> {
    return combineLatest([
      this.cart$,
      this.totals$,
      this.loading$
    ]).pipe(
      map(([cart, totals, loading]) => ({
        itemCount: cart?.itemCount || 0,
        uniqueItemCount: cart?.uniqueItemCount || 0,
        subtotal: totals?.subtotal.format() || '$0',
        total: totals?.total.format() || '$0',
        isEmpty: !cart || cart.isEmpty,
        loading
      }))
    );
  }
}

/**
 * Información resumida del carrito para componentes
 */
export interface CartSummaryInfo {
  itemCount: number;
  uniqueItemCount: number;
  subtotal: string;
  total: string;
  isEmpty: boolean;
  loading: boolean;
}