/**
 * Value Object: CartItemId
 * Identificador único de un item en el carrito.
 */
export class CartItemId {
  private constructor(private readonly _value: string) {}

  static create(value: string): CartItemId {
    if (!value || value.trim().length === 0) {
      throw new Error('CartItemId no puede estar vacío');
    }
    if (value.length > 50) {
      throw new Error('CartItemId no puede exceder 50 caracteres');
    }
    return new CartItemId(value.trim());
  }
  static generate(): CartItemId {
    const timestamp = Date.now().toString(36);
    const random = Math.random().toString(36).substring(2, 8);
    return CartItemId.create(`item_${timestamp}_${random}`);
  }
  static fromString(value: string): CartItemId {
    return CartItemId.create(value);
  }
  get value(): string {
    return this._value;
  }
  equals(other: CartItemId): boolean {
    return this._value === other._value;
  }
  toString(): string {
    return this._value;
  }
}
