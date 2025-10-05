/**
 * Category DTOs - Professional Clean Architecture
 * Aligned with backend microservice msvc-categoria
 */

/**
 * Tipos de categoría disponibles
 */
export enum CategoryType {
  PRODUCTO = 'PRODUCTO',
  SERVICIO = 'SERVICIO',
  DIGITAL = 'DIGITAL',
  FISICA = 'FISICA'
}

/**
 * Estados de aprobación de categorías
 */
export enum CategoryStatus {
  PENDIENTE = 'PENDIENTE',
  APROBADA = 'APROBADA',
  RECHAZADA = 'RECHAZADA'
}

/**
 * DTO para crear nuevas categorías.
 * Aligned with CategoriaCreateDto from backend.
 */
export interface CategoryCreateDto {
  /** Nombre de la categoría */
  nombre: string;

  /** Código único alfanumérico */
  codigo?: string;

  /** Descripción detallada */
  descripcion?: string;

  /** Estado activo/inactivo */
  activo?: boolean;

  /** ID de la categoría padre */
  categoriaPadreId?: number;

  /** Orden para mostrar en listas */
  ordenVisualizacion?: number;

  /** Color asociado en hexadecimal */
  colorHex?: string;

  /** Nombre del icono */
  icono?: string;

  /** URL de imagen principal */
  imagenUrl?: string;

  /** Meta título para SEO */
  metaTitulo?: string;

  /** Meta descripción para SEO */
  metaDescripcion?: string;

  /** Palabras clave separadas por comas */
  palabrasClave?: string;

  /** Permite productos directamente */
  permiteProductos?: boolean;

  /** Requiere aprobación para productos */
  requiereAprobacion?: boolean;

  /** Comisión por defecto */
  comisionDefecto?: number;

  /** Tipo de categoría */
  tipo?: CategoryType;
}

/**
 * DTO para actualizar categorías existentes.
 * Aligned with CategoriaUpdateDto from backend.
 */
export interface CategoryUpdateDto extends Partial<CategoryCreateDto> {
  /** ID de la categoría a actualizar */
  id: number;

  /** Motivo del cambio */
  motivoCambio?: string;
}

/**
 * DTO completo para transferencia de datos de Category.
 * Aligned with CategoriaDTO from backend msvc-categoria.
 */
export interface CategoryDto {
  // ========================================
  // CAMPOS BÁSICOS
  // ========================================

  /** Identificador único de la categoría */
  id: number;

  /** Nombre de la categoría */
  nombre: string;

  /** Código único alfanumérico */
  codigo: string;

  /** Descripción detallada */
  descripcion?: string;

  /** Estado activo/inactivo */
  activo: boolean;

  // ========================================
  // JERARQUÍA
  // ========================================

  /** ID de la categoría padre (si es subcategoría) */
  categoriaPadreId?: number;

  /** Nombre de la categoría padre */
  categoriaPadreNombre?: string;

  /** Lista de subcategorías */
  subcategorias?: CategoryDto[];

  /** Nivel en la jerarquía (0 = raíz) */
  nivel?: number;

  /** Ruta completa en texto */
  rutaCompleta?: string;

  // ========================================
  // VISUALIZACIÓN
  // ========================================

  /** Orden para mostrar en listas */
  ordenVisualizacion?: number;

  /** Color asociado en hexadecimal */
  colorHex?: string;

  /** Nombre del icono */
  icono?: string;

  /** URL de imagen principal */
  imagenUrl?: string;

  /** URL de thumbnail */
  imagenThumbnailUrl?: string;

  // ========================================
  // SEO Y MARKETING
  // ========================================

  /** Slug para URLs amigables */
  slug?: string;

  /** Meta título para SEO */
  metaTitulo?: string;

  /** Meta descripción para SEO */
  metaDescripcion?: string;

  /** Palabras clave separadas por comas */
  palabrasClave?: string;

  // ========================================
  // CONFIGURACIÓN DE NEGOCIO
  // ========================================

  /** Permite productos directamente */
  permiteProductos?: boolean;

  /** Requiere aprobación para productos */
  requiereAprobacion?: boolean;

  /** Comisión por defecto */
  comisionDefecto?: number;

  /** Margen mínimo requerido */
  margenMinimo?: number;

  // ========================================
  // AUDITORÍA
  // ========================================

  /** Fecha de creación */
  fechaCreacion?: string; // ISO date string

  /** Fecha de última modificación */
  fechaModificacion?: string; // ISO date string

  /** Usuario que creó la categoría */
  creadoPor?: string;

  /** Usuario que modificó por última vez */
  modificadoPor?: string;

  // ========================================
  // SOFT DELETE
  // ========================================

  /** Indica si está eliminada lógicamente */
  eliminado?: boolean;

  /** Usuario que eliminó */
  eliminadoPor?: string;

  /** Motivo de la eliminación */
  motivoEliminacion?: string;
}

/**
 * DTO resumido para listados optimizados.
 * Aligned with CategoriaSummaryDto from backend.
 */
export interface CategorySummaryDto {
  /** Identificador único */
  id: number;

  /** Nombre de la categoría */
  nombre: string;

  /** Código único */
  codigo: string;

  /** Descripción detallada */
  descripcion?: string;

  /** Estado activo/inactivo */
  activo: boolean;

  /** ID de la categoría padre */
  categoriaPadreId?: number;

  /** Nivel en la jerarquía */
  nivel?: number;

  /** Color asociado */
  colorHex?: string;

  /** Icono */
  icono?: string;

  /** Cantidad de subcategorías */
  cantidadSubcategorias?: number;

  /** Cantidad de productos */
  cantidadProductos?: number;
}

/**
 * DTO para filtros de búsqueda.
 * Aligned with CategoriaFilterDto from backend.
 */
export interface CategoryFilterDto {
  /** Texto a buscar en nombre, código o descripción */
  texto?: string;

  /** Código específico */
  codigo?: string;

  /** ID de categoría padre */
  categoriaPadreId?: number;

  /** Estado activo/inactivo */
  activo?: boolean;

  /** Tipo de categoría */
  tipo?: CategoryType;

  /** Estado de aprobación */
  estadoAprobacion?: CategoryStatus;

  /** Nivel mínimo en jerarquía */
  nivelMinimo?: number;

  /** Nivel máximo en jerarquía */
  nivelMaximo?: number;

  /** Fecha de creación desde */
  fechaCreacionDesde?: string;

  /** Fecha de creación hasta */
  fechaCreacionHasta?: string;
}
