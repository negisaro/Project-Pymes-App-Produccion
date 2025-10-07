/**
 * 🆔 Value Object: CartId
 * 
 * Representa el identificador único de un carrito.
 * Encapsula las reglas de negocio para validar IDs de carrito.
 */
export class CartId {
  private constructor(private readonly _value: string) {}
  
  /**
   * Crea un nuevo CartId
   * @param value El valor del ID
   * @throws Error si el valor no es válido
   */
  static create(value: string): CartId {
    if (!value || value.trim().length === 0) {
      throw new Error('CartId no puede estar vacío');
    }
    
    if (value.length > 50) {
      throw new Error('CartId no puede exceder 50 caracteres');
    }
    
    return new CartId(value.trim());
  }
  
  /**
   * Genera un nuevo CartId único
   */
  static generate(): CartId {
    const timestamp = Date.now().toString(36);
    const random = Math.random().toString(36).substring(2, 8);
    return CartId.create(`cart_${timestamp}_${random}`);
  }
  
  /**
   * Crea un CartId desde un string (para deserialización)
   */
  static fromString(value: string): CartId {
    return CartId.create(value);
  }
  
  /**
   * Obtiene el valor del ID
   */
  get value(): string {
    return this._value;
  }
  
  /**
   * Compara dos CartIds por igualdad
   */
  equals(other: CartId): boolean {
    return this._value === other._value;
  }
  
  /**
   * Convierte a string para serialización
   */
  toString(): string {
    return this._value;
  }
}