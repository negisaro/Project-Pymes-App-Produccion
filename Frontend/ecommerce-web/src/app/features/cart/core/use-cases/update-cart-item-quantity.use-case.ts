import { Injectable, Inject } from '@angular/core';
import { Observable, map, catchError, throwError } from 'rxjs';
import { CartRepository, CART_REPOSITORY_TOKEN } from '../repositories/cart.repository';
import { Cart } from '../models/cart';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { Quantity } from '../../../../shared/value-objects/quantity';

/**
 * 📝 Comando para actualizar cantidad de item
 */
export interface UpdateCartItemQuantityCommand {
  userId: UserId;
  itemId: CartItemId;
  newQuantity: number;
}

/**
 * 🎯 Caso de Uso: Actualizar Cantidad de Item en Carrito
 * 
 * Maneja la actualización de cantidad de un item específico en el carrito.
 * Incluye validaciones y lógica de eliminación automática si cantidad = 0.
 */
@Injectable({
  providedIn: 'root'
})
export class UpdateCartItemQuantityUseCase {
  
  constructor(
    @Inject(CART_REPOSITORY_TOKEN) private cartRepository: CartRepository
  ) {}
  
  /**
   * Ejecuta el caso de uso
   * @param command Comando con los datos necesarios
   * @returns Observable con el carrito actualizado
   */
  execute(command: UpdateCartItemQuantityCommand): Observable<Cart> {
    // 1. Validar comando
    this.validateCommand(command);
    
    // 2. Obtener carrito actual
    return this.cartRepository.findByUserId(command.userId).pipe(
      
      // 3. Aplicar lógica de dominio
      map(cart => {
        try {
          // Verificar que el item existe en el carrito
          const item = cart.getItem(command.itemId);
          if (!item) {
            throw new Error('El item no existe en el carrito');
          }
          
          // Si la nueva cantidad es 0, eliminar el item
          if (command.newQuantity === 0) {
            return cart.removeItem(command.itemId);
          }
          
          // Crear quantity object y actualizar
          const newQuantity = Quantity.create(command.newQuantity);
          return cart.updateItemQuantity(command.itemId, newQuantity);
          
        } catch (error) {
          throw new Error(`Error actualizando cantidad: ${(error as Error).message}`);
        }
      }),
      
      // 4. Manejo de errores
      catchError(error => {
        console.error('Error en UpdateCartItemQuantityUseCase:', error);
        return throwError(() => new Error(`No se pudo actualizar la cantidad: ${error.message}`));
      })
    );
  }
  
  /**
   * Valida el comando de entrada
   */
  private validateCommand(command: UpdateCartItemQuantityCommand): void {
    if (!command.userId) {
      throw new Error('UserId es requerido');
    }
    
    if (!command.itemId) {
      throw new Error('ItemId es requerido');
    }
    
    if (!Number.isInteger(command.newQuantity) || command.newQuantity < 0) {
      throw new Error('La cantidad debe ser un número entero no negativo');
    }
    
    if (command.newQuantity > 999) {
      throw new Error('La cantidad no puede exceder 999 unidades');
    }
  }
}