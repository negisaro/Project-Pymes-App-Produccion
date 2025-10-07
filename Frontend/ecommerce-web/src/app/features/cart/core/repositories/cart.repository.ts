import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Cart } from '../models/cart';
import { CartItem } from '../models/cart-item';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartId } from '../../../../shared/value-objects/cart-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { Quantity } from '../../../../shared/value-objects/quantity';

/**
 * 📦 Interfaz del repositorio de carrito (Puerto del dominio)
 * 
 * Define el contrato que debe implementar cualquier repositorio de carrito.
 * Siguiendo el principio de inversión de dependencias, el dominio define
 * qué necesita, pero no cómo se implementa.
 */
export interface CartRepository {
  
  /**
   * Obtiene el carrito activo de un usuario
   * @param userId ID del usuario
   * @returns Observable con el carrito del usuario o carrito vacío si no existe
   */
  findByUserId(userId: UserId): Observable<Cart>;
  
  /**
   * Guarda el carrito completo
   * @param cart Carrito a guardar
   * @returns Observable con el carrito guardado
   */
  save(cart: Cart): Observable<Cart>;
  
  /**
   * Agrega un item al carrito
   * @param cartId ID del carrito
   * @param item Item a agregar
   * @returns Observable con el carrito actualizado
   */
  addItem(cartId: CartId, item: CartItem): Observable<Cart>;
  
  /**
   * Actualiza la cantidad de un item en el carrito
   * @param cartId ID del carrito
   * @param itemId ID del item
   * @param quantity Nueva cantidad
   * @returns Observable con el carrito actualizado
   */
  updateItemQuantity(cartId: CartId, itemId: CartItemId, quantity: Quantity): Observable<Cart>;
  
  /**
   * Elimina un item del carrito
   * @param cartId ID del carrito
   * @param itemId ID del item a eliminar
   * @returns Observable con el carrito actualizado
   */
  removeItem(cartId: CartId, itemId: CartItemId): Observable<Cart>;
  
  /**
   * Limpia completamente el carrito
   * @param cartId ID del carrito
   * @returns Observable con el carrito vacío
   */
  clear(cartId: CartId): Observable<Cart>;
}

/**
 * 🔑 Token de inyección para el repositorio de carrito
 * 
 * Permite inyectar la implementación del repositorio usando Angular DI
 * sin acoplar el dominio a una implementación específica
 */
export const CART_REPOSITORY_TOKEN = new InjectionToken<CartRepository>('CartRepository');