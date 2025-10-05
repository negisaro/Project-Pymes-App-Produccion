package com.nelson.project.msvc_categoria.msvc_categoria.service;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaFilterDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaSummaryDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaUpdateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de servicio para gestión empresarial de categorías.
 * Aplica buenas prácticas modernas, principios SOLID y patrones de diseño.
 * Refactorizada para soportar todas las operaciones empresariales del repository.
 */
public interface CategoriaService {
  // ========================================
  // OPERACIONES CRUD BÁSICAS
  // ========================================

  /**
   * Obtiene todas las categorías activas y no eliminadas.
   *
   * @return Lista completa de categorías
   */
  List<CategoriaDTO> findAll();

  /**
   * Busca una categoría por su ID.
   *
   * @param id ID de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<CategoriaDTO> findById(Long id);

  /**
   * Obtiene categorías con paginación.
   *
   * @param pageable Configuración de paginación
   * @return Page de categorías
   */
  Page<CategoriaDTO> findAll(Pageable pageable);

  /**
   * Crea una nueva categoría.
   *
   * @param categoriaCreateDto Datos para crear la categoría
   * @return Categoría creada
   */
  CategoriaDTO save(CategoriaCreateDto categoriaCreateDto);

  /**
   * Actualiza una categoría existente por su ID usando datos de creación.
   *
   * @param id ID de la categoría a actualizar
   * @param categoriaCreateDto Datos para actualizar
   * @return Categoría actualizada
   */
  CategoriaDTO update(Long id, CategoriaCreateDto categoriaCreateDto);

  /**
   * Actualiza una categoría existente usando DTO específico de actualización.
   *
   * @param id ID de la categoría a actualizar
   * @param categoriaUpdateDto Datos específicos para actualización
   * @return Categoría actualizada
   */
  CategoriaDTO updateWithSpecificDto(
    Long id,
    CategoriaUpdateDto categoriaUpdateDto
  );

  /**
   * Elimina lógicamente una categoría por su ID (soft delete).
   *
   * @param id ID de la categoría a eliminar
   */
  void deleteById(Long id);

  /**
   * Elimina físicamente una categoría por su ID (hard delete).
   * Solo para casos administrativos especiales.
   *
   * @param id ID de la categoría a eliminar permanentemente
   */
  void hardDeleteById(Long id);

  // ========================================
  // OPERACIONES DE JERARQUÍA
  // ========================================

  /**
   * Obtiene todas las categorías raíz (sin padre).
   *
   * @return Lista de categorías raíz ordenadas
   */
  List<CategoriaDTO> findRootCategories();

  /**
   * Obtiene las subcategorías de una categoría padre.
   *
   * @param categoriaPadreId ID de la categoría padre
   * @return Lista de subcategorías
   */
  List<CategoriaDTO> findSubcategories(Long categoriaPadreId);

  /**
   * Obtiene categorías por nivel jerárquico específico.
   *
   * @param nivel Nivel de profundidad
   * @return Lista de categorías del nivel especificado
   */
  List<CategoriaDTO> findByLevel(Integer nivel);

  /**
   * Obtiene una categoría con todas sus subcategorías.
   *
   * @param id ID de la categoría padre
   * @return Categoría con subcategorías incluidas
   */
  Optional<CategoriaDTO> findWithSubcategories(Long id);

  // ========================================
  // BÚSQUEDAS POR IDENTIFICADORES
  // ========================================

  /**
   * Busca una categoría por su código único.
   *
   * @param codigo Código de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<CategoriaDTO> findByCode(String codigo);

  /**
   * Busca una categoría por su slug (URL amigable).
   *
   * @param slug Slug de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<CategoriaDTO> findBySlug(String slug);

  /**
   * Verifica si existe una categoría con el código especificado.
   *
   * @param codigo Código a verificar
   * @return true si existe, false si no
   */
  boolean existsByCode(String codigo);

  /**
   * Verifica si existe una categoría con el slug especificado.
   *
   * @param slug Slug a verificar
   * @return true si existe, false si no
   */
  boolean existsBySlug(String slug);

  // ========================================
  // OPERACIONES EMPRESARIALES
  // ========================================

  /**
   * Obtiene categorías destacadas para homepage y promociones.
   *
   * @return Lista de categorías destacadas
   */
  List<CategoriaDTO> findFeaturedCategories();

  /**
   * Obtiene categorías que permiten productos directamente.
   *
   * @return Lista de categorías que pueden contener productos
   */
  List<CategoriaDTO> findCategoriesAllowingProducts();

  /**
   * Obtiene categorías por departamento con paginación.
   *
   * @param departamento Nombre del departamento
   * @param pageable Configuración de paginación
   * @return Page de categorías del departamento
   */
  Page<CategoriaDTO> findByDepartment(String departamento, Pageable pageable);

  /**
   * Obtiene categorías por tipo específico.
   *
   * @param tipo Tipo de categoría
   * @return Lista de categorías del tipo especificado
   */
  List<CategoriaDTO> findByType(Categoria.TipoCategoria tipo);

  /**
   * Obtiene categorías por estado de aprobación.
   *
   * @param estadoAprobacion Estado de aprobación
   * @return Lista de categorías con el estado especificado
   */
  List<CategoriaDTO> findByApprovalStatus(
    Categoria.EstadoCategoria estadoAprobacion
  );

  /**
   * Obtiene categorías visibles en menú público.
   *
   * @return Lista de categorías visibles en menú
   */
  List<CategoriaDTO> findVisibleInMenu();

  /**
   * Obtiene categorías que requieren aprobación.
   *
   * @return Lista de categorías con aprobación requerida
   */
  List<CategoriaDTO> findRequiringApproval();

  /**
   * Obtiene categorías dentro de un rango de precios.
   *
   * @param precioMin Precio mínimo
   * @param precioMax Precio máximo
   * @return Lista de categorías en el rango de precios
   */
  List<CategoriaDTO> findByPriceRange(
    BigDecimal precioMin,
    BigDecimal precioMax
  );

  // ========================================
  // OPERACIONES DE PERFORMANCE Y ANALYTICS
  // ========================================

  /**
   * Obtiene las categorías más populares.
   *
   * @param limit Número máximo de resultados
   * @return Lista de categorías más populares
   */
  List<CategoriaSummaryDto> findMostPopular(int limit);

  /**
   * Obtiene las categorías más vendidas.
   *
   * @param limit Número máximo de resultados
   * @return Lista de categorías más vendidas
   */
  List<CategoriaSummaryDto> findBestSelling(int limit);

  /**
   * Obtiene categorías que tienen productos asociados.
   *
   * @return Lista de categorías con productos
   */
  List<CategoriaSummaryDto> findCategoriesWithProducts();

  /**
   * Busca categorías por texto en nombre o descripción.
   *
   * @param texto Texto a buscar
   * @return Lista de categorías que coinciden
   */
  List<CategoriaDTO> searchByText(String texto);

  /**
   * Busca categorías por palabras clave.
   *
   * @param palabraClave Palabra clave a buscar
   * @return Lista de categorías con la palabra clave
   */
  List<CategoriaDTO> searchByKeyword(String palabraClave);

  /**
   * Obtiene categorías con mayor crecimiento en ventas.
   *
   * @param limit Número máximo de resultados
   * @return Lista de categorías con crecimiento
   */
  List<CategoriaSummaryDto> findGrowingCategories(int limit);

  // ========================================
  // OPERACIONES DE ESTADÍSTICAS
  // ========================================

  /**
   * Obtiene estadísticas de categorías por departamento.
   *
   * @return Lista con conteo de categorías por departamento
   */
  List<Object[]> getCategoryStatsByDepartment();

  /**
   * Cuenta categorías por nivel jerárquico.
   *
   * @return Lista con nivel y conteo de categorías
   */
  List<Object[]> countCategoriesByLevel();

  /**
   * Obtiene el total de categorías activas.
   *
   * @return Número total de categorías activas
   */
  long getTotalActiveCategories();

  /**
   * Obtiene el total de categorías por estado.
   *
   * @param estado Estado a contar
   * @return Número de categorías con el estado especificado
   */
  long countByApprovalStatus(Categoria.EstadoCategoria estado);

  // ========================================
  // MÉTODOS DE FILTROS Y PAGINACIÓN
  // ========================================

  /**
   * Busca categorías aplicando filtros con paginación.
   *
   * @param filter Filtros a aplicar
   * @param pageable Configuración de paginación
   * @return Page de categorías filtradas
   */
  Page<CategoriaDTO> findByFilters(
    CategoriaFilterDto filter,
    Pageable pageable
  );

  /**
   * Obtiene todas las categorías activas con paginación.
   *
   * @param pageable Configuración de paginación
   * @return Page de categorías activas
   */
  Page<CategoriaDTO> findAllActive(Pageable pageable);

  /**
   * Cuenta el total de categorías activas.
   *
   * @return Número total de categorías activas
   */
  long countAllActive();

  /**
   * Cuenta el total de categorías inactivas.
   *
   * @return Número total de categorías inactivas
   */
  long countAllInactive();

  // ========================================
  // OPERACIONES DE ACTUALIZACIÓN ESPECÍFICAS
  // ========================================

  /**
   * Actualiza una categoría usando un DTO específico de actualización.
   *
   * @param id ID de la categoría a actualizar
   * @param categoriaUpdateDto DTO con los datos a actualizar
   * @return DTO de la categoría actualizada
   */
  CategoriaDTO update(Long id, CategoriaUpdateDto categoriaUpdateDto);

  // ========================================
  // MÉTODOS ADICIONALES PARA CONTROLADORES
  // ========================================

  /**
   * Obtiene todas las categorías activas en formato resumen con paginación.
   *
   * @param pageable Configuración de paginación
   * @return Page de categorías en formato resumen
   */
  Page<CategoriaSummaryDto> findAllActiveSummary(Pageable pageable);

  /**
   * Busca categorías por texto en nombre/descripción con soporte para activas.
   *
   * @param texto Texto a buscar
   * @param soloActivas Solo categorías activas
   * @param pageable Configuración de paginación
   * @return Page de categorías que coinciden
   */
  Page<CategoriaDTO> searchByText(
    String texto,
    Boolean soloActivas,
    Pageable pageable
  );

  /**
   * Obtiene el menú de navegación jerárquico.
   *
   * @param maxNivel Nivel máximo a incluir
   * @return Lista de categorías para navegación
   */
  List<CategoriaSummaryDto> findNavigationMenu(Integer maxNivel);

  /**
   * Obtiene categorías destacadas en formato resumen.
   *
   * @param limite Número máximo de resultados
   * @return Lista de categorías destacadas
   */
  List<CategoriaSummaryDto> findFeaturedCategoriesSummary(Integer limite);

  /**
   * Busca categorías activas por texto en formato resumen.
   *
   * @param texto Texto a buscar
   * @param limite Número máximo de resultados
   * @return Lista de categorías que coinciden
   */
  List<CategoriaSummaryDto> searchActiveCategoriesSummary(
    String texto,
    Integer limite
  );

  /**
   * Obtiene una categoría activa por ID en formato resumen.
   *
   * @param id ID de la categoría
   * @return Optional de la categoría en formato resumen
   */
  Optional<CategoriaSummaryDto> findByIdActiveSummary(Long id);

  /**
   * Obtiene subcategorías de una categoría padre por ID.
   *
   * @param parentId ID de la categoría padre
   * @return Lista de subcategorías
   */
  List<CategoriaDTO> findSubcategoriesByParentId(Long parentId);

  /**
   * Obtiene la jerarquía completa de categorías activas.
   *
   * @return Lista de categorías con jerarquía
   */
  List<CategoriaDTO> findActiveCategoryHierarchy();

  /**
   * Obtiene la jerarquía completa de todas las categorías.
   *
   * @return Lista de categorías con jerarquía
   */
  List<CategoriaDTO> findFullCategoryHierarchy();

  /**
   * Cuenta el total de categorías.
   *
   * @return Número total de categorías
   */
  long countAll();

  /**
   * Cuenta categorías raíz (sin padre).
   *
   * @return Número de categorías raíz
   */
  long countRootCategories();

  /**
   * Cuenta categorías raíz activas.
   *
   * @return Número de categorías raíz activas
   */
  long countActiveRootCategories();

  /**
   * Obtiene el promedio de subcategorías por categoría.
   *
   * @return Promedio de subcategorías
   */
  Double getAverageSubcategoriesPerCategory();

  /**
   * Obtiene el nivel máximo de jerarquía.
   *
   * @return Nivel máximo
   */
  Integer getMaxHierarchyLevel();

  /**
   * Cuenta categorías sin productos.
   *
   * @return Número de categorías sin productos
   */
  long countCategoriesWithoutProducts();

  /**
   * Obtiene la categoría con más productos.
   *
   * @return Optional de la categoría con más productos
   */
  Optional<CategoriaDTO> getCategoryWithMostProducts();
}
