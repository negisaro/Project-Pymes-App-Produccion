/**
 * 📊 Value Object: Quantity
 * 
 * Representa una cantidad en el carrito.
 * Encapsula las reglas de negocio para cantidades válidas.
 */
export class Quantity {
  private static readonly MIN_QUANTITY = 1;
  private static readonly MAX_QUANTITY = 999;
  
  private constructor(private readonly _value: number) {}
  
  /**
   * Crea una nueva Quantity
   * @param value El valor numérico de la cantidad
   * @throws Error si el valor no es válido
   */
  static create(value: number): Quantity {
    if (!Number.isInteger(value)) {
      throw new Error('La cantidad debe ser un número entero');
    }
    
    if (value < Quantity.MIN_QUANTITY) {
      throw new Error(`La cantidad debe ser mayor o igual a ${Quantity.MIN_QUANTITY}`);
    }
    
    if (value > Quantity.MAX_QUANTITY) {
      throw new Error(`La cantidad no puede exceder ${Quantity.MAX_QUANTITY}`);
    }
    
    return new Quantity(value);
  }
  
  /**
   * Crea una cantidad cero (útil para cálculos)
   */
  static zero(): Quantity {
    return new Quantity(0);
  }
  
  /**
   * Obtiene el valor numérico
   */
  get value(): number {
    return this._value;
  }
  
  /**
   * Suma otra cantidad a esta
   */
  add(other: Quantity): Quantity {
    const newValue = this._value + other._value;
    if (newValue > Quantity.MAX_QUANTITY) {
      throw new Error(`La suma de cantidades excede el máximo permitido (${Quantity.MAX_QUANTITY})`);
    }
    return new Quantity(newValue);
  }
  
  /**
   * Resta otra cantidad de esta
   */
  subtract(other: Quantity): Quantity {
    const newValue = this._value - other._value;
    if (newValue < 0) {
      throw new Error('La cantidad no puede ser negativa');
    }
    return new Quantity(newValue);
  }
  
  /**
   * Multiplica por un factor
   */
  multiply(factor: number): Quantity {
    if (!Number.isInteger(factor) || factor < 0) {
      throw new Error('El factor debe ser un número entero no negativo');
    }
    
    const newValue = this._value * factor;
    if (newValue > Quantity.MAX_QUANTITY) {
      throw new Error(`El resultado de la multiplicación excede el máximo permitido`);
    }
    
    return new Quantity(newValue);
  }
  
  /**
   * Verifica si la cantidad es cero
   */
  isZero(): boolean {
    return this._value === 0;
  }
  
  /**
   * Verifica si la cantidad es negativa
   */
  isNegative(): boolean {
    return this._value < 0;
  }
  
  /**
   * Compara dos cantidades por igualdad
   */
  equals(other: Quantity): boolean {
    return this._value === other._value;
  }
  
  /**
   * Compara si esta cantidad es mayor que otra
   */
  isGreaterThan(other: Quantity): boolean {
    return this._value > other._value;
  }
  
  /**
   * Compara si esta cantidad es menor que otra
   */
  isLessThan(other: Quantity): boolean {
    return this._value < other._value;
  }
  
  /**
   * Convierte a string para serialización
   */
  toString(): string {
    return this._value.toString();
  }
}