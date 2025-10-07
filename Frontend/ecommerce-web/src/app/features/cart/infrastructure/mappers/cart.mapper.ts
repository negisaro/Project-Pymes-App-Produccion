import { CartDto } from '../../core/dto/cart.dto';
import { CartItemDto } from '../../core/dto/cart-item.dto';
import { Cart } from '../../core/models/cart';
import { CartItem } from '../../core/models/cart-item';
import { CartId } from '../../../../shared/value-objects/cart-id';
import { UserId } from '../../../../shared/value-objects/user-id';
import { CartItemId } from '../../../../shared/value-objects/cart-item-id';
import { Quantity } from '../../../../shared/value-objects/quantity';
import { Money } from '../../../../shared/value-objects/money';
import { IProduct } from '../../shared/types/product.interface';

export class CartMapper {
  static fromDto(dto: CartDto, productResolver: (productoId: number) => IProduct): Cart {
    return new Cart(
      CartId.fromString(dto.id.toString()),
      UserId.create(dto.usuarioId),
      dto.items.map(itemDto => CartMapper.itemFromDto(itemDto, productResolver)),
      new Date(dto.creadoEn),
      new Date(dto.actualizadoEn)
    );
  }

  static toDto(cart: Cart): CartDto {
    return {
      id: cart.id.value,
      usuarioId: cart.userId ? cart.userId.value : 0,
      items: cart.items.map(CartMapper.itemToDto),
      total: cart.subtotal.amount,
      creadoEn: cart.createdAt.toISOString(),
      actualizadoEn: cart.updatedAt.toISOString()
    };
  }

  static itemFromDto(dto: CartItemDto, productResolver: (productoId: number) => IProduct): CartItem {
    return new CartItem(
      CartItemId.fromString(dto.id.toString()),
      productResolver(dto.productoId),
      Quantity.create(dto.cantidad),
      Money.create(dto.precioUnitario, 'COP'),
      new Date() // No hay campo en el DTO, se puede ajustar si se agrega
    );
  }

  static itemToDto(item: CartItem): CartItemDto {
    return {
      id: item.id.value,
      productoId: item.product.id,
      nombreProducto: item.product.name,
      cantidad: item.quantity.value,
      precioUnitario: item.unitPrice.amount,
      subtotal: item.subtotal.amount
    };
  }
}
