import { Money } from '../../../cart/core/models/value-objects/money';
import { Quantity } from '../../../cart/core/models/value-objects/quantity';

/**
 * 📦 ProductId Value Object temporal
 */
export class ProductId {
  private constructor(private readonly _value: number) {}
  
  static create(value: number): ProductId {
    if (!Number.isInteger(value) || value <= 0) {
      throw new Error('ProductId debe ser un número entero positivo');
    }
    return new ProductId(value);
  }
  
  get value(): number {
    return this._value;
  }
  
  equals(other: ProductId): boolean {
    return this._value === other._value;
  }
  
  toString(): string {
    return this._value.toString();
  }
}

/**
 * 📦 Entidad temporal: Product
 * 
 * Representación temporal de un producto para el dominio del carrito.
 * En la implementación completa, esto debería venir del módulo de productos.
 */
export class Product {
  
  constructor(
    public readonly id: ProductId,
    public readonly name: string,
    public readonly description: string,
    public readonly price: Money,
    public readonly imageUrl: string,
    public readonly stock: number,
    public readonly isActive: boolean = true
  ) {}
  
  /**
   * Crea un producto temporal para testing
   */
  static createTemp(
    id: number,
    name: string,
    price: number,
    stock: number = 10
  ): Product {
    return new Product(
      ProductId.create(id),
      name,
      `Descripción de ${name}`,
      Money.create(price, 'COP'),
      `/images/products/${id}.jpg`,
      stock,
      true
    );
  }
  
  /**
   * Verifica si el producto está disponible
   */
  isAvailable(): boolean {
    return this.isActive && this.stock > 0;
  }
  
  /**
   * Verifica si hay stock suficiente para una cantidad
   */
  hasStockFor(quantity: Quantity): boolean {
    return this.stock >= quantity.value;
  }
  
  /**
   * Obtiene el stock disponible
   */
  get availableStock(): number {
    return this.stock;
  }
  
  /**
   * Obtiene el precio formateado
   */
  get formattedPrice(): string {
    return this.price.format();
  }
}