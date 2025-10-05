// Interface para productos en contexto público (frontend de clientes)
export interface Product {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  fechaCreacion: string;
  fechaActualizacion: string;
  categoriaId: number;
  proveedorId: number;
  imagenes: string[];
  estado: string;
}

// Interface para paginación de productos
export interface ProductPage {
  content: Product[];
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  number: number;
  size: number;
}