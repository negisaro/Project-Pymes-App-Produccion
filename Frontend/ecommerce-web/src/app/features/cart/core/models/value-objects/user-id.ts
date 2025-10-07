/**
 * 🆔 Value Object: UserId
 * 
 * Representa el identificador único de un usuario.
 * Encapsula las reglas de validación para IDs de usuario.
 */
export class UserId {
  private constructor(private readonly _value: number) {}
  
  /**
   * Crea un nuevo UserId
   * @param value El valor numérico del ID
   * @throws Error si el valor no es válido
   */
  static create(value: number): UserId {
    if (!Number.isInteger(value) || value <= 0) {
      throw new Error('UserId debe ser un número entero positivo');
    }
    
    if (value > Number.MAX_SAFE_INTEGER) {
      throw new Error('UserId excede el valor máximo permitido');
    }
    
    return new UserId(value);
  }
  
  /**
   * Crea un UserId desde un string (para deserialización)
   */
  static fromString(value: string): UserId {
    const numericValue = parseInt(value, 10);
    if (isNaN(numericValue)) {
      throw new Error('UserId debe ser un número válido');
    }
    return UserId.create(numericValue);
  }
  
  /**
   * Obtiene el valor numérico del ID
   */
  get value(): number {
    return this._value;
  }
  
  /**
   * Compara dos UserIds por igualdad
   */
  equals(other: UserId): boolean {
    return this._value === other._value;
  }
  
  /**
   * Convierte a string para serialización
   */
  toString(): string {
    return this._value.toString();
  }
}