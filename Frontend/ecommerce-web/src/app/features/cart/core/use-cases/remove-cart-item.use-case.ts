import { Injectable, Inject } from '@angular/core';
import { Observable, map, catchError, throwError } from 'rxjs';
import { CartRepository, CART_REPOSITORY_TOKEN } from '../repositories/cart.repository';
import { Cart } from '../models/cart';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';

/**
 * 📝 Comando para eliminar item del carrito
 */
export interface RemoveCartItemCommand {
  userId: UserId;
  itemId: CartItemId;
}

/**
 * 🎯 Caso de Uso: Eliminar Item del Carrito
 * 
 * Maneja la eliminación completa de un item del carrito.
 * Incluye validaciones y actualización de totales.
 */
@Injectable({
  providedIn: 'root'
})
export class RemoveCartItemUseCase {
  
  constructor(
    @Inject(CART_REPOSITORY_TOKEN) private cartRepository: CartRepository
  ) {}
  
  /**
   * Ejecuta el caso de uso
   * @param command Comando con los datos necesarios
   * @returns Observable con el carrito actualizado
   */
  execute(command: RemoveCartItemCommand): Observable<Cart> {
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
          
          // Eliminar el item
          return cart.removeItem(command.itemId);
          
        } catch (error) {
          throw new Error(`Error eliminando item: ${(error as Error).message}`);
        }
      }),
      
      // 4. Manejo de errores
      catchError(error => {
        console.error('Error en RemoveCartItemUseCase:', error);
        return throwError(() => new Error(`No se pudo eliminar el producto del carrito: ${error.message}`));
      })
    );
  }
  
  /**
   * Valida el comando de entrada
   */
  private validateCommand(command: RemoveCartItemCommand): void {
    if (!command.userId) {
      throw new Error('UserId es requerido');
    }
    
    if (!command.itemId) {
      throw new Error('ItemId es requerido');
    }
  }
}