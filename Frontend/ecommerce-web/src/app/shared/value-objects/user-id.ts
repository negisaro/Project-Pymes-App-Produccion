/**
 * Value Object: UserId
 * Identificador único de usuario.
 */
export class UserId {
  private constructor(private readonly _value: number) {}

  static create(value: number): UserId {
    if (!Number.isInteger(value) || value <= 0) {
      throw new Error('UserId debe ser un número entero positivo');
    }
    if (value > Number.MAX_SAFE_INTEGER) {
      throw new Error('UserId excede el valor máximo permitido');
    }
    return new UserId(value);
  }
  static fromString(value: string): UserId {
    const numericValue = parseInt(value, 10);
    if (isNaN(numericValue)) {
      throw new Error('UserId debe ser un número válido');
    }
    return UserId.create(numericValue);
  }
  get value(): number {
    return this._value;
  }
  equals(other: UserId): boolean {
    return this._value === other._value;
  }
  toString(): string {
    return this._value.toString();
  }
}
