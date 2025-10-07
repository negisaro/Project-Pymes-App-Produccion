import { CartItemDto } from './cart-item.dto';
export interface CartDto {
  id: string;
  usuarioId: number;
  items: CartItemDto[];
  total: number;
  creadoEn: string;
  actualizadoEn: string;
}
