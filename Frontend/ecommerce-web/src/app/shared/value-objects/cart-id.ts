/**
 * Value Object: CartId
 * Identificador único de un carrito.
 */
export class CartId {
  private constructor(private readonly _value: string) {}

  static create(value: string): CartId {
    if (!value || value.trim().length === 0) {
      throw new Error('CartId no puede estar vacío');
    }
    if (value.length > 50) {
      throw new Error('CartId no puede exceder 50 caracteres');
    }
    return new CartId(value.trim());
  }
  static generate(): CartId {
    const timestamp = Date.now().toString(36);
    const random = Math.random().toString(36).substring(2, 8);
    return CartId.create(`cart_${timestamp}_${random}`);
  }
  static fromString(value: string): CartId {
    return CartId.create(value);
  }
  get value(): string {
    return this._value;
  }
  equals(other: CartId): boolean {
    return this._value === other._value;
  }
  toString(): string {
    return this._value;
  }
}
