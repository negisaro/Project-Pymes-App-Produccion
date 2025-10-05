import { Product } from './product.interface';

export interface ProductPage {
  content: Product[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}