import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Cart } from '../../core/models/cart';

/**
 * 📊 Estado del carrito
 */
export interface CartState {
  cart: Cart | null;
  loading: boolean;
  error: string | null;
  lastUpdated: Date | null;
}

/**
 * 🔧 Servicio de Estado del Carrito
 * 
 * Maneja el estado reactivo del carrito usando BehaviorSubject.
 * Centraliza la gestión de estado para toda la aplicación.
 */
@Injectable({
  providedIn: 'root'
})
export class CartStateService {
  
  private readonly initialState: CartState = {
    cart: null,
    loading: false,
    error: null,
    lastUpdated: null
  };
  
  private readonly stateSubject = new BehaviorSubject<CartState>(this.initialState);
  
  // Observables públicos
  public readonly state$ = this.stateSubject.asObservable();
  public readonly cart$ = this.state$.pipe(
    map(state => state.cart)
  );
  public readonly loading$ = this.state$.pipe(
    map(state => state.loading)
  );
  public readonly error$ = this.state$.pipe(
    map(state => state.error)
  );
  public readonly itemCount$ = this.cart$.pipe(
    map(cart => cart?.itemCount || 0)
  );
  public readonly total$ = this.cart$.pipe(
    map(cart => cart?.subtotal || null)
  );
  
  /**
   * Obtiene el estado actual
   */
  getCurrentState(): CartState {
    return this.stateSubject.value;
  }
  
  /**
   * Obtiene el carrito actual
   */
  getCurrentCart(): Cart | null {
    return this.stateSubject.value.cart;
  }
  
  /**
   * Actualiza el carrito
   */
  setCart(cart: Cart): void {
    this.updateState({
      cart,
      loading: false,
      error: null,
      lastUpdated: new Date()
    });
  }
  
  /**
   * Establece el estado de carga
   */
  setLoading(loading: boolean): void {
    this.updateState({
      loading,
      error: loading ? null : this.getCurrentState().error
    });
  }
  
  /**
   * Establece un error
   */
  setError(error: string): void {
    this.updateState({
      error,
      loading: false
    });
  }
  
  /**
   * Limpia el error
   */
  clearError(): void {
    this.updateState({
      error: null
    });
  }
  
  /**
   * Limpia completamente el estado
   */
  clearState(): void {
    this.stateSubject.next(this.initialState);
  }
  
  /**
   * Actualiza parcialmente el estado
   */
  private updateState(partialState: Partial<CartState>): void {
    const currentState = this.stateSubject.value;
    const newState = { ...currentState, ...partialState };
    this.stateSubject.next(newState);
  }
}

// Importar map operator
import { map } from 'rxjs/operators';