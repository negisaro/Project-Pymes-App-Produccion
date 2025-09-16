export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
  estado: boolean;
  creadoEn: string; // ISO date string
  actualizadoEn: string; // ISO date string
}
