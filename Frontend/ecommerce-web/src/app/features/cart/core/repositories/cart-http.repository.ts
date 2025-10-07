import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { CartRepository } from './cart.repository';
import { Cart } from '../models/cart';
import { CartItem } from '../models/cart-item';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartId } from '../../../../shared/value-objects/cart-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { Quantity } from '../../../../shared/value-objects/quantity';
import { CartDto } from '../dto/cart.dto';
import { CartItemDto } from '../dto/cart-item.dto';
import { ItemCarritoRequestDto } from '../dto/item-carrito-request.dto';
import { CartMapper } from '../../infrastructure/mappers/cart.mapper';
import { IProduct } from '../../shared/types/product.interface';
import { Money } from '../../../../shared/value-objects/money';

@Injectable({ providedIn: 'root' })
export class CartHttpRepository implements CartRepository {
  private readonly baseUrl = '/api/public/carrito';

  constructor(private http: HttpClient) {}

  // Resolver temporal de producto (debe ser reemplazado por inyección real)
  private productResolver(productoId: number): IProduct {
    return {
      id: productoId,
      name: 'Producto',
      description: '',
      price: Money.create(0, 'COP'),
      imageUrl: '',
      category: '',
      stockQuantity: 0,
      isActive: true
    };
  }

  findByUserId(userId: UserId): Observable<Cart> {
    return this.http.get<CartDto>(`${this.baseUrl}/${userId.value}`)
      .pipe(
        map(dto => CartMapper.fromDto(dto, this.productResolver)),
        catchError(err => { throw err; })
      );
  }

  save(cart: Cart): Observable<Cart> {
    const dto = CartMapper.toDto(cart);
    return this.http.put<CartDto>(`${this.baseUrl}/crear/${cart.userId?.value}`, dto)
      .pipe(
        map(dto => CartMapper.fromDto(dto, this.productResolver)),
        catchError(err => { throw err; })
      );
  }

  addItem(cartId: CartId, item: CartItem): Observable<Cart> {
    const itemDto: ItemCarritoRequestDto = {
      productoId: item.product.id,
      cantidad: item.quantity.value
    };
    return this.http.post<CartDto>(`${this.baseUrl}/${cartId.value}/items`, itemDto)
      .pipe(
        map(dto => CartMapper.fromDto(dto, this.productResolver)),
        catchError(err => { throw err; })
      );
  }

  updateItemQuantity(cartId: CartId, itemId: CartItemId, quantity: Quantity): Observable<Cart> {
    return this.http.put<CartDto>(`${this.baseUrl}/${cartId.value}/items/${itemId.value}?nuevaCantidad=${quantity.value}`, {})
      .pipe(
        map(dto => CartMapper.fromDto(dto, this.productResolver)),
        catchError(err => { throw err; })
      );
  }

  removeItem(cartId: CartId, itemId: CartItemId): Observable<Cart> {
    return this.http.delete<CartDto>(`${this.baseUrl}/${cartId.value}/items/${itemId.value}`)
      .pipe(
        map(dto => CartMapper.fromDto(dto, this.productResolver)),
        catchError(err => { throw err; })
      );
  }

  clear(cartId: CartId): Observable<Cart> {
    return this.http.delete<void>(`${this.baseUrl}/${cartId.value}/vaciar`)
      .pipe(
        map(() => Cart.createEmpty(UserId.create(0))),
        catchError(err => { throw err; })
      );
  }
}
