import { Money } from '../../../../shared/value-objects/money';

/**
 * 🛍️ Interface de Producto para el Carrito
 *
 * Interface simplificada del producto que utiliza el carrito.
 * Evita dependencias circulares con el módulo de productos.
 */
export interface IProduct {
  id: number;
  name: string;
  description: string;
  price: Money;
  imageUrl: string;
  category: string;
  stockQuantity: number;
  isActive: boolean;
}

/**
 * Helper methods para productos en el contexto del carrito
 */
export class ProductCartHelper {

  /**
   * Verifica si el producto está disponible
   */
  static isAvailable(product: IProduct): boolean {
    return product.isActive && product.stockQuantity > 0;
  }

  /**
   * Verifica si hay stock suficiente para una cantidad
   */
  static hasStockFor(product: IProduct, quantity: { value: number }): boolean {
    return product.stockQuantity >= quantity.value;
  }

  /**
   * Crea un producto temporal para testing
   */
  static createMock(id: number, name: string, price: number): IProduct {
    return {
      id,
      name,
      description: `Descripción de ${name}`,
      price: Money.create(price, 'COP'),
      imageUrl: '/assets/images/placeholder.jpg',
      category: 'General',
      stockQuantity: 10,
      isActive: true
    };
  }
}
