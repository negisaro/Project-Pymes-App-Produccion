import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { Quantity } from '../../../../shared/value-objects/quantity';
import { Money } from '../../../../shared/value-objects/money';
import { IProduct, ProductCartHelper } from '../../shared/types/product.interface';

/**
 * 📦 Entidad: CartItem
 * 
 * Representa un item individual dentro del carrito.
 * Encapsula la relación entre un producto y su cantidad en el carrito.
 */
export class CartItem {
  
  constructor(
    public readonly id: CartItemId,
    public readonly product: IProduct,
    public readonly quantity: Quantity,
    public readonly unitPrice: Money,
    public readonly addedAt: Date
  ) {}
  
  /**
   * Crea un nuevo CartItem
   */
  static create(product: IProduct, quantity: Quantity): CartItem {
    return new CartItem(
      CartItemId.generate(),
      product,
      quantity,
      product.price, // Capturar el precio actual del producto
      new Date()
    );
  }
  
  /**
   * Actualiza la cantidad del item
   */
  updateQuantity(newQuantity: Quantity): CartItem {
    if (newQuantity.isZero() || newQuantity.isNegative()) {
      throw new Error('La cantidad debe ser positiva');
    }
    
    return new CartItem(
      this.id,
      this.product,
      newQuantity,
      this.unitPrice,
      this.addedAt
    );
  }
  
  /**
   * Calcula el subtotal del item (precio unitario × cantidad)
   */
  get subtotal(): Money {
    return this.unitPrice.multiply(this.quantity.value);
  }
  
  /**
   * Verifica si el item está disponible
   * (producto disponible y con stock suficiente)
   */
  get isAvailable(): boolean {
    return ProductCartHelper.isAvailable(this.product) && 
           ProductCartHelper.hasStockFor(this.product, this.quantity);
  }
  
  /**
   * Verifica si el precio del item cambió respecto al precio actual del producto
   */
  get hasPriceChanged(): boolean {
    return !this.unitPrice.equals(this.product.price);
  }
  
  /**
   * Obtiene el precio actual del producto para comparación
   */
  get currentProductPrice(): Money {
    return this.product.price;
  }
  
  /**
   * Calcula la diferencia de precio si cambió
   */
  get priceDifference(): Money | null {
    if (!this.hasPriceChanged) {
      return null;
    }
    
    if (this.currentProductPrice.isGreaterThan(this.unitPrice)) {
      return this.currentProductPrice.subtract(this.unitPrice);
    } else {
      return this.unitPrice.subtract(this.currentProductPrice);
    }
  }
  
  /**
   * Verifica si el precio aumentó
   */
  get priceIncreased(): boolean {
    return this.hasPriceChanged && 
           this.currentProductPrice.isGreaterThan(this.unitPrice);
  }
  
  /**
   * Verifica si el precio disminuyó
   */
  get priceDecreased(): boolean {
    return this.hasPriceChanged && 
           this.unitPrice.isGreaterThan(this.currentProductPrice);
  }
  
  /**
   * Actualiza el precio del item al precio actual del producto
   */
  updateToCurrentPrice(): CartItem {
    return new CartItem(
      this.id,
      this.product,
      this.quantity,
      this.product.price,
      this.addedAt
    );
  }
  
  /**
   * Obtiene información de display del item
   */
  get displayInfo(): CartItemDisplayInfo {
    return {
      id: this.id.value,
      productName: this.product.name,
      productImage: this.product.imageUrl,
      quantity: this.quantity.value,
      unitPrice: this.unitPrice.format(),
      subtotal: this.subtotal.format(),
      isAvailable: this.isAvailable,
      hasPriceChanged: this.hasPriceChanged
    };
  }
}

/**
 * Información de display para el CartItem
 */
export interface CartItemDisplayInfo {
  id: string;
  productName: string;
  productImage: string;
  quantity: number;
  unitPrice: string;
  subtotal: string;
  isAvailable: boolean;
  hasPriceChanged: boolean;
}