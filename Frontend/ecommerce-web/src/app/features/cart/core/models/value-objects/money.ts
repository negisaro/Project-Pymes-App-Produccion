/**
 * 💰 Value Object: Money
 * 
 * Representa un valor monetario con moneda específica.
 * Encapsula las reglas de negocio para operaciones monetarias.
 */
export class Money {
  private constructor(
    private readonly _amount: number,
    private readonly _currency: string
  ) {}
  
  /**
   * Crea un nuevo Money
   * @param amount Cantidad monetaria
   * @param currency Código de moneda (ej: 'COP', 'USD')
   * @throws Error si los valores no son válidos
   */
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
    
    // Redondear a 2 decimales para evitar problemas de precisión
    const roundedAmount = Math.round(amount * 100) / 100;
    
    return new Money(roundedAmount, currency.toUpperCase());
  }
  
  /**
   * Crea un Money con valor cero
   */
  static zero(currency: string = 'COP'): Money {
    return Money.create(0, currency);
  }
  
  /**
   * Obtiene la cantidad
   */
  get amount(): number {
    return this._amount;
  }
  
  /**
   * Obtiene la moneda
   */
  get currency(): string {
    return this._currency;
  }
  
  /**
   * Suma otro Money a este
   * @param other El Money a sumar
   * @throws Error si las monedas no coinciden
   */
  add(other: Money): Money {
    this.ensureSameCurrency(other);
    return Money.create(this._amount + other._amount, this._currency);
  }
  
  /**
   * Resta otro Money de este
   */
  subtract(other: Money): Money {
    this.ensureSameCurrency(other);
    const result = this._amount - other._amount;
    if (result < 0) {
      throw new Error('El resultado de la resta no puede ser negativo');
    }
    return Money.create(result, this._currency);
  }
  
  /**
   * Multiplica por un factor
   */
  multiply(factor: number): Money {
    if (!Number.isFinite(factor) || factor < 0) {
      throw new Error('El factor debe ser un número positivo válido');
    }
    return Money.create(this._amount * factor, this._currency);
  }
  
  /**
   * Divide por un divisor
   */
  divide(divisor: number): Money {
    if (!Number.isFinite(divisor) || divisor <= 0) {
      throw new Error('El divisor debe ser un número positivo válido');
    }
    return Money.create(this._amount / divisor, this._currency);
  }
  
  /**
   * Verifica si el valor es cero
   */
  isZero(): boolean {
    return this._amount === 0;
  }
  
  /**
   * Verifica si es mayor que otro Money
   */
  isGreaterThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount > other._amount;
  }
  
  /**
   * Verifica si es menor que otro Money
   */
  isLessThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount < other._amount;
  }
  
  /**
   * Compara por igualdad
   */
  equals(other: Money): boolean {
    return this._amount === other._amount && this._currency === other._currency;
  }
  
  /**
   * Formatea como string para display
   */
  format(): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: this._currency
    }).format(this._amount);
  }
  
  /**
   * Convierte a string para serialización
   */
  toString(): string {
    return `${this._amount} ${this._currency}`;
  }
  
  /**
   * Valida que dos Money tengan la misma moneda
   */
  private ensureSameCurrency(other: Money): void {
    if (this._currency !== other._currency) {
      throw new Error(`No se pueden operar monedas diferentes: ${this._currency} vs ${other._currency}`);
    }
  }
}