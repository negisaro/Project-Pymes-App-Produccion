/**
 * Value Object: Quantity
 * Representa una cantidad válida en el dominio.
 */
export class Quantity {
  private static readonly MIN_QUANTITY = 1;
  private static readonly MAX_QUANTITY = 999;

  private constructor(private readonly _value: number) {}

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
  static zero(): Quantity {
    return new Quantity(0);
  }
  get value(): number {
    return this._value;
  }
  add(other: Quantity): Quantity {
    const newValue = this._value + other._value;
    if (newValue > Quantity.MAX_QUANTITY) {
      throw new Error(`La suma de cantidades excede el máximo permitido (${Quantity.MAX_QUANTITY})`);
    }
    return new Quantity(newValue);
  }
  subtract(other: Quantity): Quantity {
    const newValue = this._value - other._value;
    if (newValue < 0) {
      throw new Error('La cantidad no puede ser negativa');
    }
    return new Quantity(newValue);
  }
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
  isZero(): boolean {
    return this._value === 0;
  }
  isNegative(): boolean {
    return this._value < 0;
  }
  equals(other: Quantity): boolean {
    return this._value === other._value;
  }
  isGreaterThan(other: Quantity): boolean {
    return this._value > other._value;
  }
  isLessThan(other: Quantity): boolean {
    return this._value < other._value;
  }
  toString(): string {
    return this._value.toString();
  }
}
