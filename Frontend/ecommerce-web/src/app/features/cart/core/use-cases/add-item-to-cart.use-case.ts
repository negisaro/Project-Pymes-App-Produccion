import { Injectable, Inject } from '@angular/core';
import { Observable, map, catchError, throwError } from 'rxjs';
import { CartRepository, CART_REPOSITORY_TOKEN } from '../repositories/cart.repository';
import { Cart } from '../models/cart';
import { UserId } from '../../../../shared/value-objects/user-id';
import { Quantity } from '../../../../shared/value-objects/quantity';
import { IProduct, ProductCartHelper } from '../../shared/types/product.interface';

/**
 * 📝 Comando para agregar item al carrito
 */
export interface AddItemToCartCommand {
  userId: UserId;
  product: IProduct;
  quantity: number;
}

/**
 * 🎯 Caso de Uso: Agregar Item al Carrito
 *
 * Encapsula la lógica de negocio para agregar un producto al carrito.
 * Maneja validaciones, reglas de negocio y persistencia.
 */
@Injectable({
  providedIn: 'root'
})
export class AddItemToCartUseCase {

  constructor(
    @Inject(CART_REPOSITORY_TOKEN) private cartRepository: CartRepository
  ) {}

  /**
   * Ejecuta el caso de uso
   * @param command Comando con los datos necesarios
   * @returns Observable con el carrito actualizado
   */
  execute(command: AddItemToCartCommand): Observable<Cart> {
    // 1. Validar comando
    this.validateCommand(command);

    // 2. Crear quantity object
    const quantity = Quantity.create(command.quantity);

    // 3. Obtener carrito actual del usuario
    return this.cartRepository.findByUserId(command.userId).pipe(

      // 4. Aplicar lógica de dominio
      map(cart => {
        try {
          return cart.addItem(command.product, quantity);
        } catch (error) {
          throw new Error(`Error agregando item al carrito: ${(error as Error).message}`);
        }
      }),

      // 5. Guardar carrito actualizado
      map(updatedCart => {
        // Aquí podríamos agregar lógica adicional como eventos
        return updatedCart;
      }),

      // 6. Persistir cambios
      // Nota: El repository se encarga de la persistencia

      // 7. Manejo de errores
      catchError(error => {
        console.error('Error en AddItemToCartUseCase:', error);
        return throwError(() => new Error(`No se pudo agregar el producto al carrito: ${error.message}`));
      })
    );
  }

  /**
   * Valida el comando de entrada
   */
  private validateCommand(command: AddItemToCartCommand): void {
    if (!command.userId) {
      throw new Error('UserId es requerido');
    }

    if (!command.product) {
      throw new Error('Product es requerido');
    }

    if (!ProductCartHelper.isAvailable(command.product)) {
      throw new Error('El producto no está disponible');
    }

    if (!Number.isInteger(command.quantity) || command.quantity <= 0) {
      throw new Error('La cantidad debe ser un número entero positivo');
    }

    if (command.quantity > 999) {
      throw new Error('La cantidad no puede exceder 999 unidades');
    }
  }
}
