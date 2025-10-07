import { CartId } from '../../../../shared/value-objects/cart-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { UserId } from '../../../../shared/value-objects/user-id';
import { Quantity } from '../../../../shared/value-objects/quantity';
import { Money } from '../../../../shared/value-objects/money';
import { CartItem } from './cart-item';
import { IProduct, ProductCartHelper } from '../../shared/types/product.interface';

/**
 * 🛒 Entidad: Cart
 * 
 * Representa el carrito de compras en el dominio.
 * Contiene toda la lógica de negocio relacionada con el carrito.
 */
export class Cart {
  
  constructor(
    public readonly id: CartId,
    public readonly userId: UserId | null,
    private readonly _items: CartItem[],
    private readonly _createdAt: Date,
    private readonly _updatedAt: Date
  ) {}
  
  /**
   * Crea un carrito vacío para un usuario
   */
  static createEmpty(userId: UserId): Cart {
    return new Cart(
      CartId.generate(),
      userId,
      [],
      new Date(),
      new Date()
    );
  }
  
  /**
   * Crea un carrito anónimo (sin usuario)
   */
  static createAnonymous(): Cart {
    return new Cart(
      CartId.generate(),
      null,
      [],
      new Date(),
      new Date()
    );
  }
  
  /**
   * Agrega un item al carrito
   */
  addItem(product: IProduct, quantity: Quantity): Cart {
    // Validar que el producto esté disponible
    if (!ProductCartHelper.isAvailable(product)) {
      throw new Error('El producto no está disponible');
    }
    
    // Buscar si el producto ya existe en el carrito
    const existingItemIndex = this._items.findIndex(item => 
      item.product.id === product.id
    );
    
    let newItems: CartItem[];
    
    if (existingItemIndex >= 0) {
      // Si existe, actualizar la cantidad
      const existingItem = this._items[existingItemIndex];
      const newQuantity = existingItem.quantity.add(quantity);
      const updatedItem = existingItem.updateQuantity(newQuantity);
      
      newItems = [...this._items];
      newItems[existingItemIndex] = updatedItem;
    } else {
      // Si no existe, crear nuevo item
      const newItem = CartItem.create(product, quantity);
      newItems = [...this._items, newItem];
    }
    
    return new Cart(
      this.id,
      this.userId,
      newItems,
      this._createdAt,
      new Date()
    );
  }
  
  /**
   * Actualiza la cantidad de un item específico
   */
  updateItemQuantity(itemId: CartItemId, newQuantity: Quantity): Cart {
    const itemIndex = this._items.findIndex(item => item.id.equals(itemId));
    
    if (itemIndex === -1) {
      throw new Error('Item no encontrado en el carrito');
    }
    
    // Si la cantidad es cero, eliminar el item
    if (newQuantity.isZero()) {
      return this.removeItem(itemId);
    }
    
    const updatedItem = this._items[itemIndex].updateQuantity(newQuantity);
    const newItems = [...this._items];
    newItems[itemIndex] = updatedItem;
    
    return new Cart(
      this.id,
      this.userId,
      newItems,
      this._createdAt,
      new Date()
    );
  }
  
  /**
   * Elimina un item del carrito
   */
  removeItem(itemId: CartItemId): Cart {
    const newItems = this._items.filter(item => !item.id.equals(itemId));
    
    return new Cart(
      this.id,
      this.userId,
      newItems,
      this._createdAt,
      new Date()
    );
  }
  
  /**
   * Limpia todos los items del carrito
   */
  clear(): Cart {
    return new Cart(
      this.id,
      this.userId,
      [],
      this._createdAt,
      new Date()
    );
  }
  
  /**
   * Obtiene los items del carrito (inmutable)
   */
  get items(): readonly CartItem[] {
    return Object.freeze([...this._items]);
  }
  
  /**
   * Obtiene la cantidad total de items
   */
  get itemCount(): number {
    return this._items.reduce((total, item) => total + item.quantity.value, 0);
  }
  
  /**
   * Obtiene la cantidad de tipos de productos únicos
   */
  get uniqueItemCount(): number {
    return this._items.length;
  }
  
  /**
   * Calcula el subtotal del carrito
   */
  get subtotal(): Money {
    if (this._items.length === 0) {
      return Money.zero('COP');
    }
    
    return this._items.reduce(
      (total, item) => total.add(item.subtotal),
      Money.zero('COP')
    );
  }
  
  /**
   * Verifica si el carrito está vacío
   */
  get isEmpty(): boolean {
    return this._items.length === 0;
  }
  
  /**
   * Verifica si contiene un producto específico
   */
  hasProduct(productId: number): boolean {
    return this._items.some(item => item.product.id === productId);
  }
  
  /**
   * Obtiene un item específico por ID
   */
  getItem(itemId: CartItemId): CartItem | null {
    return this._items.find(item => item.id.equals(itemId)) || null;
  }
  
  /**
   * Obtiene la fecha de creación
   */
  get createdAt(): Date {
    return new Date(this._createdAt);
  }
  
  /**
   * Obtiene la fecha de última actualización
   */
  get updatedAt(): Date {
    return new Date(this._updatedAt);
  }
  
  /**
   * Verifica si todos los items están disponibles
   */
  areAllItemsAvailable(): boolean {
    return this._items.every(item => item.isAvailable);
  }
  
  /**
   * Obtiene los items que no están disponibles
   */
  getUnavailableItems(): CartItem[] {
    return this._items.filter(item => !item.isAvailable);
  }
}