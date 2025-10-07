import { Injectable } from '@angular/core';
import { Cart } from '../../core/models/cart';
import { UserId } from '../../../../shared/value-objects/user-id';

/**
 * 💾 Entrada de caché
 */
interface CacheEntry<T> {
  data: T;
  timestamp: number;
  expiresAt: number;
}

/**
 * 🗄️ Servicio de Caché del Carrito
 * 
 * Maneja el almacenamiento en caché del carrito para mejorar performance.
 * Utiliza localStorage para persistencia entre sesiones.
 */
@Injectable({
  providedIn: 'root'
})
export class CartCacheService {
  
  private readonly CACHE_PREFIX = 'cart_cache_';
  private readonly DEFAULT_TTL = 30 * 60 * 1000; // 30 minutos
  private readonly memoryCache = new Map<string, CacheEntry<any>>();
  
  /**
   * Almacena un carrito en caché
   */
  cacheCart(userId: UserId, cart: Cart): void {
    const key = this.getUserCartKey(userId);
    const entry: CacheEntry<Cart> = {
      data: cart,
      timestamp: Date.now(),
      expiresAt: Date.now() + this.DEFAULT_TTL
    };
    
    // Caché en memoria
    this.memoryCache.set(key, entry);
    
    // Caché en localStorage
    try {
      localStorage.setItem(key, JSON.stringify({
        cartData: this.serializeCart(cart),
        timestamp: entry.timestamp,
        expiresAt: entry.expiresAt
      }));
    } catch (error) {
      console.warn('Error guardando carrito en localStorage:', error);
    }
  }
  
  /**
   * Obtiene un carrito del caché
   */
  getCachedCart(userId: UserId): Cart | null {
    const key = this.getUserCartKey(userId);
    
    // Intentar desde memoria primero
    const memoryEntry = this.memoryCache.get(key);
    if (memoryEntry && !this.isExpired(memoryEntry)) {
      return memoryEntry.data;
    }
    
    // Intentar desde localStorage
    try {
      const localData = localStorage.getItem(key);
      if (localData) {
        const parsed = JSON.parse(localData);
        
        if (!this.isExpiredByTimestamp(parsed.expiresAt)) {
          const cart = this.deserializeCart(parsed.cartData);
          
          // Restaurar en memoria
          this.memoryCache.set(key, {
            data: cart,
            timestamp: parsed.timestamp,
            expiresAt: parsed.expiresAt
          });
          
          return cart;
        } else {
          // Expirado, limpiar
          localStorage.removeItem(key);
        }
      }
    } catch (error) {
      console.warn('Error leyendo carrito de localStorage:', error);
    }
    
    return null;
  }
  
  /**
   * Invalida el caché de un usuario
   */
  invalidateUserCache(userId: UserId): void {
    const key = this.getUserCartKey(userId);
    
    // Limpiar memoria
    this.memoryCache.delete(key);
    
    // Limpiar localStorage
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.warn('Error limpiando caché de localStorage:', error);
    }
  }
  
  /**
   * Limpia todo el caché expirado
   */
  cleanExpiredCache(): void {
    const now = Date.now();
    
    // Limpiar memoria
    for (const [key, entry] of this.memoryCache.entries()) {
      if (this.isExpired(entry)) {
        this.memoryCache.delete(key);
      }
    }
    
    // Limpiar localStorage
    try {
      const keysToRemove: string[] = [];
      
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (key && key.startsWith(this.CACHE_PREFIX)) {
          const data = localStorage.getItem(key);
          if (data) {
            try {
              const parsed = JSON.parse(data);
              if (this.isExpiredByTimestamp(parsed.expiresAt)) {
                keysToRemove.push(key);
              }
            } catch (error) {
              // Datos corruptos, eliminar
              keysToRemove.push(key);
            }
          }
        }
      }
      
      keysToRemove.forEach(key => localStorage.removeItem(key));
    } catch (error) {
      console.warn('Error limpiando caché expirado:', error);
    }
  }
  
  /**
   * Obtiene el tamaño del caché en memoria
   */
  getCacheSize(): number {
    return this.memoryCache.size;
  }
  
  /**
   * Limpia completamente el caché
   */
  clearAll(): void {
    // Limpiar memoria
    this.memoryCache.clear();
    
    // Limpiar localStorage
    try {
      const keysToRemove: string[] = [];
      
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (key && key.startsWith(this.CACHE_PREFIX)) {
          keysToRemove.push(key);
        }
      }
      
      keysToRemove.forEach(key => localStorage.removeItem(key));
    } catch (error) {
      console.warn('Error limpiando todo el caché:', error);
    }
  }
  
  /**
   * Genera la clave de caché para un usuario
   */
  private getUserCartKey(userId: UserId): string {
    return `${this.CACHE_PREFIX}user_${userId.value}`;
  }
  
  /**
   * Verifica si una entrada está expirada
   */
  private isExpired(entry: CacheEntry<any>): boolean {
    return Date.now() > entry.expiresAt;
  }
  
  /**
   * Verifica si un timestamp está expirado
   */
  private isExpiredByTimestamp(expiresAt: number): boolean {
    return Date.now() > expiresAt;
  }
  
  /**
   * Serializa un carrito para almacenamiento
   */
  private serializeCart(cart: Cart): any {
    // Por ahora retornamos un objeto simple
    // En implementación completa, aquí iría la lógica de serialización
    return {
      id: cart.id.value,
      userId: cart.userId?.value || null,
      items: cart.items.map(item => ({
        id: item.id.value,
        productId: item.product.id,
        quantity: item.quantity.value,
        unitPrice: item.unitPrice.amount,
        addedAt: item.addedAt.toISOString()
      })),
      createdAt: cart.createdAt.toISOString(),
      updatedAt: cart.updatedAt.toISOString()
    };
  }
  
  /**
   * Deserializa un carrito desde almacenamiento
   */
  private deserializeCart(data: any): Cart {
    // Por ahora retornamos null
    // En implementación completa, aquí iría la lógica de deserialización
    // que requiere acceso a repositorios de productos para reconstruir el carrito
    throw new Error('Deserialización de carrito no implementada aún');
  }
}