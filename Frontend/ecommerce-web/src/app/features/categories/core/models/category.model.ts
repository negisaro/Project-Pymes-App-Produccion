/**
 * Category Domain Models - Clean Architecture Core
 * Business logic entities and value objects
 */

import { CategoryDto, CategoryType, CategoryStatus } from './category.dto';

/**
 * Category Domain Entity
 * Core business entity with business rules and invariants
 */
export class Category {
  constructor(
    private readonly _id: number,
    private _nombre: string,
    private _codigo: string,
    private _descripcion: string | null = null,
    private _activo: boolean = true,
    private _categoriaPadreId: number | null = null,
    private _ordenVisualizacion: number = 0,
    private _colorHex: string | null = null,
    private _icono: string | null = null,
    private _imagenUrl: string | null = null,
    private _metaTitulo: string | null = null,
    private _metaDescripcion: string | null = null,
    private _palabrasClave: string | null = null,
    private _permiteProductos: boolean = true,
    private _requiereAprobacion: boolean = false,
    private _comisionDefecto: number | null = null,
    private _tipo: CategoryType = CategoryType.PRODUCTO,
    private _fechaCreacion: Date = new Date(),
    private _fechaModificacion: Date = new Date(),
    private _creadoPor: string | null = null,
    private _modificadoPor: string | null = null,
    private _eliminado: boolean = false,
    private _subcategorias: Category[] = []
  ) {
    this.validateBusinessRules();
  }

  // ========================================
  // GETTERS
  // ========================================

  get id(): number { return this._id; }
  get nombre(): string { return this._nombre; }
  get codigo(): string { return this._codigo; }
  get descripcion(): string | null { return this._descripcion; }
  get activo(): boolean { return this._activo; }
  get categoriaPadreId(): number | null { return this._categoriaPadreId; }
  get ordenVisualizacion(): number { return this._ordenVisualizacion; }
  get colorHex(): string | null { return this._colorHex; }
  get icono(): string | null { return this._icono; }
  get imagenUrl(): string | null { return this._imagenUrl; }
  get metaTitulo(): string | null { return this._metaTitulo; }
  get metaDescripcion(): string | null { return this._metaDescripcion; }
  get palabrasClave(): string | null { return this._palabrasClave; }
  get permiteProductos(): boolean { return this._permiteProductos; }
  get requiereAprobacion(): boolean { return this._requiereAprobacion; }
  get comisionDefecto(): number | null { return this._comisionDefecto; }
  get tipo(): CategoryType { return this._tipo; }
  get fechaCreacion(): Date { return this._fechaCreacion; }
  get fechaModificacion(): Date { return this._fechaModificacion; }
  get creadoPor(): string | null { return this._creadoPor; }
  get modificadoPor(): string | null { return this._modificadoPor; }
  get eliminado(): boolean { return this._eliminado; }
  get subcategorias(): Category[] { return this._subcategorias; }

  // ========================================
  // BUSINESS METHODS
  // ========================================

  /**
   * Actualiza información básica de la categoría
   */
  updateBasicInfo(nombre: string, descripcion?: string): void {
    this.validateNombre(nombre);
    this._nombre = nombre;
    this._descripcion = descripcion || null;
    this.updateModificationInfo();
  }

  /**
   * Actualiza configuración de visualización
   */
  updateVisualization(colorHex?: string, icono?: string, ordenVisualizacion?: number): void {
    this._colorHex = colorHex || null;
    this._icono = icono || null;
    this._ordenVisualizacion = ordenVisualizacion || this._ordenVisualizacion;
    this.updateModificationInfo();
  }

  /**
   * Actualiza configuración SEO
   */
  updateSeoInfo(metaTitulo?: string, metaDescripcion?: string, palabrasClave?: string): void {
    this._metaTitulo = metaTitulo || null;
    this._metaDescripcion = metaDescripcion || null;
    this._palabrasClave = palabrasClave || null;
    this.updateModificationInfo();
  }

  /**
   * Activa la categoría
   */
  activate(): void {
    this._activo = true;
    this.updateModificationInfo();
  }

  /**
   * Desactiva la categoría
   */
  deactivate(): void {
    this._activo = false;
    this.updateModificationInfo();
  }

  /**
   * Elimina lógicamente la categoría
   */
  softDelete(motivo?: string): void {
    this._eliminado = true;
    this.updateModificationInfo();
  }

  /**
   * Restaura una categoría eliminada
   */
  restore(): void {
    this._eliminado = false;
    this.updateModificationInfo();
  }

  /**
   * Agrega una subcategoría
   */
  addSubcategory(subcategoria: Category): void {
    if (subcategoria.categoriaPadreId !== this._id) {
      throw new Error('La subcategoría debe tener como padre esta categoría');
    }
    this._subcategorias.push(subcategoria);
  }

  /**
   * Verifica si es categoría raíz
   */
  isRoot(): boolean {
    return this._categoriaPadreId === null;
  }

  /**
   * Verifica si tiene subcategorías
   */
  hasSubcategories(): boolean {
    return this._subcategorias.length > 0;
  }

  /**
   * Obtiene la cantidad de subcategorías
   */
  getSubcategoryCount(): number {
    return this._subcategorias.length;
  }

  /**
   * Convierte a DTO para transferencia
   */
  toDto(): CategoryDto {
    return {
      id: this._id,
      nombre: this._nombre,
      codigo: this._codigo,
      descripcion: this._descripcion || undefined,
      activo: this._activo,
      categoriaPadreId: this._categoriaPadreId || undefined,
      ordenVisualizacion: this._ordenVisualizacion,
      colorHex: this._colorHex || undefined,
      icono: this._icono || undefined,
      imagenUrl: this._imagenUrl || undefined,
      metaTitulo: this._metaTitulo || undefined,
      metaDescripcion: this._metaDescripcion || undefined,
      palabrasClave: this._palabrasClave || undefined,
      permiteProductos: this._permiteProductos,
      requiereAprobacion: this._requiereAprobacion,
      comisionDefecto: this._comisionDefecto || undefined,
      fechaCreacion: this._fechaCreacion.toISOString(),
      fechaModificacion: this._fechaModificacion.toISOString(),
      creadoPor: this._creadoPor || undefined,
      modificadoPor: this._modificadoPor || undefined,
      eliminado: this._eliminado,
      subcategorias: this._subcategorias.map(sub => sub.toDto())
    };
  }

  /**
   * Crea una instancia desde DTO
   */
  static fromDto(dto: CategoryDto): Category {
    const category = new Category(
      dto.id,
      dto.nombre,
      dto.codigo,
      dto.descripcion,
      dto.activo,
      dto.categoriaPadreId,
      dto.ordenVisualizacion,
      dto.colorHex,
      dto.icono,
      dto.imagenUrl,
      dto.metaTitulo,
      dto.metaDescripcion,
      dto.palabrasClave,
      dto.permiteProductos,
      dto.requiereAprobacion,
      dto.comisionDefecto,
      CategoryType.PRODUCTO, // Default type
      dto.fechaCreacion ? new Date(dto.fechaCreacion) : new Date(),
      dto.fechaModificacion ? new Date(dto.fechaModificacion) : new Date(),
      dto.creadoPor,
      dto.modificadoPor,
      dto.eliminado || false
    );

    // Agregar subcategorías si existen
    if (dto.subcategorias) {
      dto.subcategorias.forEach(subDto => {
        category.addSubcategory(Category.fromDto(subDto));
      });
    }

    return category;
  }

  // ========================================
  // PRIVATE METHODS
  // ========================================

  private validateBusinessRules(): void {
    this.validateNombre(this._nombre);
    this.validateCodigo(this._codigo);
  }

  private validateNombre(nombre: string): void {
    if (!nombre || nombre.trim().length === 0) {
      throw new Error('El nombre de la categoría es requerido');
    }
    if (nombre.length < 2) {
      throw new Error('El nombre debe tener al menos 2 caracteres');
    }
    if (nombre.length > 100) {
      throw new Error('El nombre no puede exceder 100 caracteres');
    }
  }

  private validateCodigo(codigo: string): void {
    if (!codigo || codigo.trim().length === 0) {
      throw new Error('El código de la categoría es requerido');
    }
    if (!/^[A-Z0-9_-]+$/.test(codigo)) {
      throw new Error('El código solo puede contener letras mayúsculas, números, guiones y guiones bajos');
    }
  }

  private updateModificationInfo(): void {
    this._fechaModificacion = new Date();
  }
}

/**
 * Value Object para representar jerarquía de categorías
 */
export class CategoryHierarchy {
  constructor(
    private readonly _category: Category,
    private readonly _level: number,
    private readonly _path: string[]
  ) {}

  get category(): Category { return this._category; }
  get level(): number { return this._level; }
  get path(): string[] { return this._path; }
  get fullPath(): string { return this._path.join(' > '); }

  /**
   * Verifica si es hoja en la jerarquía
   */
  isLeaf(): boolean {
    return !this._category.hasSubcategories();
  }

  /**
   * Obtiene el nivel de anidamiento
   */
  getDepth(): number {
    return this._level;
  }
}
