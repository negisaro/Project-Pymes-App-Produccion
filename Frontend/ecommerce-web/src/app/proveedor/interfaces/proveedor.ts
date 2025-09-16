export interface Proveedor {
  id?: number;
  nombre: string;
  descripcion: string;
  contacto?: string;
  activo: boolean;
  creadoEn?: string;
  actualizadoEn?: string;
}
