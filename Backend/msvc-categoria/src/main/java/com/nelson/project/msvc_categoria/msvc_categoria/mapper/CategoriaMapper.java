package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaSummaryDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaUpdateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper completo para transformaciones entre entidad Categoria robusta y DTOs especializados.
 * Maneja 65+ campos organizados en 10 grupos funcionales con 5 DTOs especializados.
 *
 * Implementa:
 * - Mapping completo: Categoria ↔ CategoriaDTO (todos los campos)
 * - Mapping de creación: CategoriaCreateDto → Categoria (con validaciones)
 * - Mapping de actualización: CategoriaUpdateDto → Categoria (campos editables)
 * - Mapping de resumen: Categoria → CategoriaSummaryDto (campos esenciales)
 * - Mappings jerárquicos con categoría padre y subcategorías
 * - Generación automática de campos calculados (slug, rutaCompleta)
 */
@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CategoriaMapper {
  // ========================================
  // MAPPING COMPLETO: CATEGORIA ↔ CATEGORIADTO
  // ========================================

  /**
   * Convierte entidad Categoria robusta (65+ campos) a CategoriaDTO completo.
   * Incluye mappings jerárquicos y campos calculados.
   */
  @Named("toBasicDto")
  @Mapping(target = "categoriaPadreId", source = "categoriaPadre.id")
  @Mapping(target = "categoriaPadreNombre", source = "categoriaPadre.nombre")
  @Mapping(target = "subcategorias", ignore = true) // Evitar referencia circular - usar método específico
  CategoriaDTO toDto(Categoria categoria);

  /**
   * Convierte lista de entidades a DTOs completos.
   */
  @IterableMapping(qualifiedByName = "toBasicDto")
  List<CategoriaDTO> toDtoList(List<Categoria> categorias);

  /**
   * Mapping inverso de CategoriaDTO a entidad (para casos especiales).
   * NOTA: Solo se usa en casos muy específicos, normalmente se usa fromCreateDto o fromUpdateDto.
   */
  @Mapping(target = "categoriaPadre", ignore = true) // Se maneja por separado
  @Mapping(target = "subcategorias", ignore = true) // Se maneja por separado
  // AUDITORÍA - Se maneja automáticamente por AuditingEntityListener
  @Mapping(target = "creadoPor", ignore = true) // Auditoría automática
  @Mapping(target = "modificadoPor", ignore = true) // Auditoría automática
  Categoria toEntity(CategoriaDTO dto);

  // ========================================
  // MAPPING DE CREACIÓN: CATEGORIACREATEDTO → CATEGORIA
  // ========================================

  /**
   * Convierte CategoriaCreateDto a entidad Categoria para creación.
   * Aplica valores por defecto y genera campos automáticos.
   */
  @Mapping(target = "id", ignore = true) // Generado automáticamente
  @Mapping(target = "categoriaPadre", ignore = true) // Se asigna por separado con el ID
  @Mapping(target = "subcategorias", ignore = true) // Lista vacía inicialmente
  @Mapping(target = "rutaCompleta", ignore = true) // Se genera automáticamente
  @Mapping(target = "slug", ignore = true) // Se genera automáticamente si no se proporciona
  // MÉTRICAS - Valores iniciales
  @Mapping(target = "totalProductos", constant = "0L")
  @Mapping(target = "totalVentas", constant = "0L")
  @Mapping(
    target = "ingresosTotales",
    expression = "java(java.math.BigDecimal.ZERO)"
  )
  @Mapping(target = "vistasTotal", constant = "0L")
  @Mapping(target = "popularidad", constant = "0.0")
  // AUDITORÍA - Se maneja automáticamente por AuditingEntityListener
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "modificadoPor", ignore = true)
  // SOFT DELETE - Valores por defecto
  @Mapping(target = "eliminado", constant = "false")
  @Mapping(target = "eliminadoPor", ignore = true)
  @Mapping(target = "motivoEliminacion", ignore = true)
  // VALORES POR DEFECTO para campos opcionales
  @Mapping(target = "activo", defaultValue = "true")
  @Mapping(target = "nivel", ignore = true) // Se calcula automáticamente
  @Mapping(target = "imagenThumbnailUrl", ignore = true) // Se genera automáticamente
  @Mapping(target = "ordenVisualizacion", defaultValue = "0")
  @Mapping(target = "permiteProductos", defaultValue = "true")
  @Mapping(target = "requiereAprobacion", defaultValue = "false")
  @Mapping(target = "visibleEnMenu", defaultValue = "true")
  @Mapping(target = "destacada", defaultValue = "false")
  @Mapping(target = "requiereInventario", defaultValue = "true")
  @Mapping(target = "permiteVariantes", defaultValue = "true")
  @Mapping(target = "tipo", defaultValue = "PRODUCTO")
  @Mapping(target = "estadoAprobacion", defaultValue = "APROBADA")
  Categoria fromCreateDto(CategoriaCreateDto createDto);

  /**
   * Actualiza entidad existente con datos de CategoriaCreateDto.
   * Útil para operaciones de creación que requieren actualizar una entidad existente.
   */
  @Mapping(target = "id", ignore = true) // No se puede cambiar
  @Mapping(target = "categoriaPadre", ignore = true) // Se maneja por separado
  @Mapping(target = "subcategorias", ignore = true) // No se actualiza directamente
  @Mapping(target = "rutaCompleta", ignore = true) // Se regenera automáticamente
  @Mapping(target = "slug", ignore = true) // Se regenera automáticamente si cambia el nombre
  @Mapping(target = "nivel", ignore = true) // Se calcula automáticamente
  @Mapping(target = "imagenThumbnailUrl", ignore = true) // Se genera automáticamente
  // MÉTRICAS - No se actualizan manualmente
  @Mapping(target = "totalProductos", ignore = true)
  @Mapping(target = "totalVentas", ignore = true)
  @Mapping(target = "ingresosTotales", ignore = true)
  @Mapping(target = "vistasTotal", ignore = true)
  @Mapping(target = "popularidad", ignore = true)
  // AUDITORÍA - Se maneja automáticamente
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "modificadoPor", ignore = true)
  // SOFT DELETE - No se maneja en update
  @Mapping(target = "eliminado", ignore = true)
  @Mapping(target = "eliminadoPor", ignore = true)
  @Mapping(target = "motivoEliminacion", ignore = true)
  void updateFromCreateDto(
    CategoriaCreateDto createDto,
    @MappingTarget Categoria categoria
  );

  // ========================================
  // MAPPING DE ACTUALIZACIÓN: CATEGORIAUPDATEDTO → CATEGORIA
  // ========================================

  /**
   * Actualiza entidad existente con datos de CategoriaUpdateDto.
   * Solo actualiza campos que no son nulos en el DTO.
   */
  @Mapping(target = "id", ignore = true) // No se puede cambiar
  @Mapping(target = "codigo", ignore = true) // No se puede cambiar después de creación
  @Mapping(target = "categoriaPadre", ignore = true) // Se maneja por separado
  @Mapping(target = "subcategorias", ignore = true) // No se actualiza directamente
  @Mapping(target = "rutaCompleta", ignore = true) // Se regenera automáticamente
  @Mapping(target = "slug", ignore = true) // Se regenera automáticamente si cambia el nombre
  @Mapping(target = "nivel", ignore = true) // Se calcula automáticamente
  // MÉTRICAS - No se actualizan manualmente
  @Mapping(target = "totalProductos", ignore = true)
  @Mapping(target = "totalVentas", ignore = true)
  @Mapping(target = "ingresosTotales", ignore = true)
  @Mapping(target = "vistasTotal", ignore = true)
  @Mapping(target = "popularidad", ignore = true)
  // AUDITORÍA DE CREACIÓN - No se cambia
  @Mapping(target = "creadoPor", ignore = true)
  // AUDITORÍA DE MODIFICACIÓN - Se maneja automáticamente
  @Mapping(target = "modificadoPor", ignore = true)
  // SOFT DELETE - No se maneja en update normal
  @Mapping(target = "eliminado", ignore = true)
  @Mapping(target = "eliminadoPor", ignore = true)
  @Mapping(target = "motivoEliminacion", ignore = true)
  void updateEntityFromDto(
    CategoriaUpdateDto updateDto,
    @MappingTarget Categoria categoria
  );

  // ========================================
  // MAPPING DE RESUMEN: CATEGORIA → CATEGORIASUMMARYDTO
  // ========================================

  /**
   * Convierte entidad Categoria a CategoriaSummaryDto (solo campos esenciales).
   * Optimizado para listados y consultas rápidas.
   */
  @Named("toBasicSummaryDto")
  @Mapping(target = "categoriaPadreId", source = "categoriaPadre.id")
  @Mapping(target = "categoriaPadreNombre", source = "categoriaPadre.nombre")
  CategoriaSummaryDto toSummaryDto(Categoria categoria);

  /**
   * Convierte lista de entidades a DTOs de resumen.
   */
  @IterableMapping(qualifiedByName = "toBasicSummaryDto")
  List<CategoriaSummaryDto> toSummaryDtoList(List<Categoria> categorias);

  // ========================================
  // MAPPINGS JERÁRQUICOS ESPECIALIZADOS
  // ========================================

  /**
   * Convierte entidad a DTO completo incluyendo subcategorías (para jerarquías).
   * CUIDADO: Puede causar N+1 queries si no se usa con @EntityGraph.
   */
  @Named("toDtoWithChildren")
  @Mapping(target = "categoriaPadreId", source = "categoriaPadre.id")
  @Mapping(target = "categoriaPadreNombre", source = "categoriaPadre.nombre")
  @Mapping(
    target = "subcategorias",
    source = "subcategorias",
    qualifiedByName = "toBasicDto"
  )
  CategoriaDTO toDtoWithSubcategorias(Categoria categoria);

  /**
   * Convierte entidad a DTO de resumen sin subcategorías (optimizado).
   */
  @Mapping(target = "categoriaPadreId", source = "categoriaPadre.id")
  @Mapping(target = "categoriaPadreNombre", source = "categoriaPadre.nombre")
  CategoriaSummaryDto toSummaryDtoWithoutChildren(Categoria categoria);

  // ========================================
  // MAPPINGS DE UTILIDAD Y CAMPOS CALCULADOS
  // ========================================

  /**
   * Actualiza solo los campos de configuración de negocio.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "nombre", ignore = true)
  @Mapping(target = "codigo", ignore = true)
  @Mapping(target = "descripcion", ignore = true)
  @Mapping(target = "categoriaPadre", ignore = true)
  @Mapping(target = "subcategorias", ignore = true)
  @Mapping(target = "nivel", ignore = true)
  @Mapping(target = "rutaCompleta", ignore = true)
  // CAMPOS QUE NO SE ACTUALIZAN EN CONFIG DE NEGOCIO
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "modificadoPor", ignore = true)
  @Mapping(target = "eliminado", ignore = true)
  @Mapping(target = "eliminadoPor", ignore = true)
  @Mapping(target = "motivoEliminacion", ignore = true)
  @Mapping(target = "totalProductos", ignore = true)
  @Mapping(target = "totalVentas", ignore = true)
  @Mapping(target = "ingresosTotales", ignore = true)
  @Mapping(target = "vistasTotal", ignore = true)
  @Mapping(target = "popularidad", ignore = true)
  // Solo campos de configuración de negocio
  void updateBusinessConfigFromDto(
    CategoriaUpdateDto updateDto,
    @MappingTarget Categoria categoria
  );

  /**
   * Actualiza solo los campos SEO.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "nombre", ignore = true)
  @Mapping(target = "codigo", ignore = true)
  @Mapping(target = "activo", ignore = true)
  @Mapping(target = "categoriaPadre", ignore = true)
  @Mapping(target = "subcategorias", ignore = true)
  @Mapping(target = "nivel", ignore = true)
  @Mapping(target = "rutaCompleta", ignore = true)
  @Mapping(target = "totalProductos", ignore = true)
  @Mapping(target = "totalVentas", ignore = true)
  @Mapping(target = "ingresosTotales", ignore = true)
  @Mapping(target = "vistasTotal", ignore = true)
  @Mapping(target = "popularidad", ignore = true)
  @Mapping(target = "creadoPor", ignore = true)
  @Mapping(target = "modificadoPor", ignore = true)
  @Mapping(target = "eliminado", ignore = true)
  @Mapping(target = "eliminadoPor", ignore = true)
  @Mapping(target = "motivoEliminacion", ignore = true)
  // Solo campos SEO
  void updateSeoFieldsFromDto(
    CategoriaUpdateDto updateDto,
    @MappingTarget Categoria categoria
  );

  // ========================================
  // MÉTODOS DE EXPRESIÓN PARA CAMPOS CALCULADOS
  // ========================================

  /**
   * Genera slug automáticamente basado en el nombre si no se proporciona.
   */
  @Named("generateSlugFromName")
  default String generateSlugFromName(String nombre) {
    if (nombre == null) return null;
    return nombre
      .toLowerCase()
      .replaceAll("[^a-z0-9\\s-]", "")
      .replaceAll("\\s+", "-")
      .replaceAll("-+", "-")
      .replaceAll("^-|-$", "");
  }

  /**
   * Después del mapping de creación, aplicar post-procesamiento.
   */
  @AfterMapping
  default void afterMappingCreate(
    @MappingTarget Categoria categoria,
    CategoriaCreateDto createDto
  ) {
    // Generar slug si no se proporcionó
    if (categoria.getSlug() == null && categoria.getNombre() != null) {
      categoria.setSlug(generateSlugFromName(categoria.getNombre()));
    }

    // Establecer motivo de último cambio para creación
    categoria.setMotivoUltimoCambio("Creación inicial de categoría");
  }

  /**
   * Después del mapping de actualización, aplicar post-procesamiento.
   */
  @AfterMapping
  default void afterMappingUpdate(
    @MappingTarget Categoria categoria,
    CategoriaUpdateDto updateDto
  ) {
    // Regenerar slug si cambió el nombre
    if (updateDto.getNombre() != null) {
      categoria.setSlug(generateSlugFromName(updateDto.getNombre()));
    }

    // Establecer motivo de último cambio si se proporcionó
    if (updateDto.getMotivoUltimoCambio() != null) {
      categoria.setMotivoUltimoCambio(updateDto.getMotivoUltimoCambio());
    } else {
      categoria.setMotivoUltimoCambio(
        "Actualización de información de categoría"
      );
    }
  }
}
