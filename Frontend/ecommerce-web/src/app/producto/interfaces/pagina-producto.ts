import { Producto } from './producto';

export interface PaginaProducto {
  content: Producto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
