import { Categoria } from './categoria';

export interface PaginaCategoria {
  content: Categoria[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
