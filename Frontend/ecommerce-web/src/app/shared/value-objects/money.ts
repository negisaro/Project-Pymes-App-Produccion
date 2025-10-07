/**
 * Value Object: Money
 * Representa un valor monetario con moneda específica.
 */
export class Money {
  private constructor(
    private readonly _amount: number,
    private readonly _currency: string
  ) {}

  static create(amount: number, currency: string): Money {
    if (!Number.isFinite(amount) || amount < 0) {
      throw new Error('La cantidad debe ser un número positivo válido');
    }
    if (!currency || currency.trim().length === 0) {
      throw new Error('La moneda es requerida');
    }
    if (currency.length !== 3) {
      throw new Error('El código de moneda debe tener 3 caracteres');
    }
    const roundedAmount = Math.round(amount * 100) / 100;
    return new Money(roundedAmount, currency.toUpperCase());
  }

  static zero(currency: string = 'COP'): Money {
    return Money.create(0, currency);
  }

  get amount(): number {
    return this._amount;
  }
  get currency(): string {
    return this._currency;
  }
  add(other: Money): Money {
    this.ensureSameCurrency(other);
    return Money.create(this._amount + other._amount, this._currency);
  }
  subtract(other: Money): Money {
    this.ensureSameCurrency(other);
    const result = this._amount - other._amount;
    if (result < 0) {
      throw new Error('El resultado de la resta no puede ser negativo');
    }
    return Money.create(result, this._currency);
  }
  multiply(factor: number): Money {
    if (!Number.isFinite(factor) || factor < 0) {
      throw new Error('El factor debe ser un número positivo válido');
    }
    return Money.create(this._amount * factor, this._currency);
  }
  divide(divisor: number): Money {
    if (!Number.isFinite(divisor) || divisor <= 0) {
      throw new Error('El divisor debe ser un número positivo válido');
    }
    return Money.create(this._amount / divisor, this._currency);
  }
  isZero(): boolean {
    return this._amount === 0;
  }
  isGreaterThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount > other._amount;
  }
  isLessThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount < other._amount;
  }
  equals(other: Money): boolean {
    return this._amount === other._amount && this._currency === other._currency;
  }
  format(): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: this._currency
    }).format(this._amount);
  }
  toString(): string {
    return `${this._amount} ${this._currency}`;
  }
  private ensureSameCurrency(other: Money): void {
    if (this._currency !== other._currency) {
      throw new Error(`No se pueden operar monedas diferentes: ${this._currency} vs ${other._currency}`);
    }
  }
}
