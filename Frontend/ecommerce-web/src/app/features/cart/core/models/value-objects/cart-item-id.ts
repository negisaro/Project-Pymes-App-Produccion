/**
 * 🆔 Value Object: CartItemId
 * 
 * Representa el identificador único de un item en el carrito.
 */
export class CartItemId {
  private constructor(private readonly _value: string) {}
  
  /**
   * Crea un nuevo CartItemId
   * @param value El valor del ID
   * @throws Error si el valor no es válido
   */
  static create(value: string): CartItemId {
    if (!value || value.trim().length === 0) {
      throw new Error('CartItemId no puede estar vacío');
    }
    
    if (value.length > 50) {
      throw new Error('CartItemId no puede exceder 50 caracteres');
    }
    
    return new CartItemId(value.trim());
  }
  
  /**
   * Genera un nuevo CartItemId único
   */
  static generate(): CartItemId {
    const timestamp = Date.now().toString(36);
    const random = Math.random().toString(36).substring(2, 8);
    return CartItemId.create(`item_${timestamp}_${random}`);
  }
  
  /**
   * Crea un CartItemId desde un string
   */
  static fromString(value: string): CartItemId {
    return CartItemId.create(value);
  }
  
  /**
   * Obtiene el valor del ID
   */
  get value(): string {
    return this._value;
  }
  
  /**
   * Compara dos CartItemIds por igualdad
   */
  equals(other: CartItemId): boolean {
    return this._value === other._value;
  }
  
  /**
   * Convierte a string para serialización
   */
  toString(): string {
    return this._value;
  }
}