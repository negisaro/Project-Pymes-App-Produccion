export interface Product {
  id?: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  categoriaId: number;
  proveedorId: number;
  imagenes?: string[];
  estado: boolean;
}