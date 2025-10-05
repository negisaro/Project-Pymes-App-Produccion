package com.nelson.project.msvc_categoria.msvc_categoria.repository;

import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para la gestión de entidades Categoria.
 * Proporciona métodos especializados para consultas de jerarquía, estado,
 * lógica empresarial y analytics con un enfoque en performance y uniformidad.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  // ========================================
  // CONSULTAS BÁSICAS OVERRIDE
  // ========================================

  /**
   * Override del método findAll con paginación para mantener uniformidad.
   */
  @SuppressWarnings("null")
  Page<Categoria> findAll(Pageable pageable);

  // ========================================
  // CONSULTAS DE JERARQUÍA
  // ========================================

  /**
   * Obtiene todas las categorías raíz (sin padre) activas y no eliminadas.
   * Ordenadas por orden de visualización para mantener consistencia en UI.
   *
   * @return Lista de categorías raíz ordenadas
   */
  List<
    Categoria
  > findByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion();

  /**
   * Obtiene todas las subcategorías de una categoría padre específica.
   * Filtradas por estado activo y no eliminado, ordenadas por visualización.
   *
   * @param categoriaPadreId ID de la categoría padre
   * @return Lista de subcategorías ordenadas
   */
  List<
    Categoria
  > findByCategoriaPadreIdAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion(
    Long categoriaPadreId
  );

  /**
   * Obtiene categorías por nivel específico en la jerarquía.
   * Útil para navegación y menús multinivel.
   *
   * @param nivel Nivel de profundidad en la jerarquía
   * @return Lista de categorías del nivel especificado
   */
  List<
    Categoria
  > findByNivelAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion(
    Integer nivel
  );

  // ========================================
  // CONSULTAS POR ESTADO Y IDENTIFICADORES
  // ========================================

  /**
   * Busca una categoría por su código único.
   * Método crítico para APIs y identificación de categorías.
   *
   * @param codigo Código único de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<Categoria> findByCodigoAndEliminadoFalse(String codigo);

  /**
   * Busca una categoría por su slug (URL amigable).
   * Esencial para SEO y navegación web.
   *
   * @param slug Slug único de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<Categoria> findBySlugAndEliminadoFalse(String slug);

  /**
   * Obtiene todas las categorías destacadas activas.
   * Para mostrar en homepage y secciones promocionales.
   *
   * @return Lista de categorías destacadas ordenadas
   */
  List<
    Categoria
  > findByDestacadaTrueAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion();

  /**
   * Obtiene categorías por estado de aprobación específico.
   * Para workflows de moderación y administración.
   *
   * @param estadoAprobacion Estado de aprobación a filtrar
   * @return Lista de categorías con el estado especificado
   */
  List<Categoria> findByEstadoAprobacionAndEliminadoFalseOrderByFechaCreacion(
    Categoria.EstadoCategoria estadoAprobacion
  );

  /**
   * Verifica si existe una categoría con el código especificado.
   * Para validaciones de unicidad en creación/edición.
   *
   * @param codigo Código a verificar
   * @return true si existe, false si no
   */
  boolean existsByCodigoAndEliminadoFalse(String codigo);

  /**
   * Verifica si existe una categoría con el slug especificado.
   * Para validaciones de unicidad en URLs.
   *
   * @param slug Slug a verificar
   * @return true si existe, false si no
   */
  boolean existsBySlugAndEliminadoFalse(String slug);

  // ========================================
  // CONSULTAS EMPRESARIALES AVANZADAS
  // ========================================

  /**
   * Obtiene categorías que permiten productos directamente.
   * Crítico para lógica de negocio y validaciones de productos.
   *
   * @return Lista de categorías que pueden contener productos
   */
  List<
    Categoria
  > findByPermiteProductosTrueAndActivoTrueAndEliminadoFalseOrderByNombre();

  /**
   * Obtiene categorías por departamento específico con paginación.
   * Para filtros departamentales y organización empresarial.
   *
   * @param departamento Nombre del departamento
   * @param pageable Configuración de paginación
   * @return Page de categorías del departamento
   */
  Page<Categoria> findByDepartamentoAndActivoTrueAndEliminadoFalse(
    String departamento,
    Pageable pageable
  );

  /**
   * Obtiene categorías por tipo específico ordenadas por popularidad.
   * Para filtros por tipo de producto/servicio.
   *
   * @param tipo Tipo de categoría
   * @return Lista de categorías del tipo especificado
   */
  List<
    Categoria
  > findByTipoAndActivoTrueAndEliminadoFalseOrderByPopularidadDesc(
    Categoria.TipoCategoria tipo
  );

  /**
   * Obtiene categorías que requieren aprobación.
   * Para workflows de moderación y control de calidad.
   *
   * @return Lista de categorías con aprobación requerida
   */
  List<
    Categoria
  > findByRequiereAprobacionTrueAndActivoTrueAndEliminadoFalseOrderByFechaCreacion();

  /**
   * Obtiene categorías visibles en menú público.
   * Para construcción de menús de navegación.
   *
   * @return Lista de categorías visibles en menú
   */
  List<
    Categoria
  > findByVisibleEnMenuTrueAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion();

  /**
   * Obtiene categorías con productos dentro de un rango de precios.
   * Para filtros de rango de precios en catálogos.
   *
   * @param precioMin Precio mínimo
   * @param precioMax Precio máximo
   * @return Lista de categorías en el rango de precios
   */
  @Query(
    "SELECT c FROM Categoria c WHERE c.precioMinimo >= :precioMin AND c.precioMaximo <= :precioMax AND c.activo = true AND c.eliminado = false ORDER BY c.nombre"
  )
  List<Categoria> findCategoriasEnRangoPrecios(
    @Param("precioMin") java.math.BigDecimal precioMin,
    @Param("precioMax") java.math.BigDecimal precioMax
  );

  // ========================================
  // CONSULTAS DE PERFORMANCE Y ANALYTICS
  // ========================================

  /**
   * Obtiene las categorías más populares (top 10).
   * Para dashboards y recomendaciones en homepage.
   *
   * @return Lista de las 10 categorías más populares
   */
  List<
    Categoria
  > findTop10ByActivoTrueAndEliminadoFalseOrderByPopularidadDesc();

  /**
   * Obtiene las categorías más vendidas (top 20).
   * Para analytics de ventas y reportes comerciales.
   *
   * @return Lista de las 20 categorías con más ventas
   */
  List<
    Categoria
  > findTop20ByActivoTrueAndEliminadoFalseOrderByTotalVentasDesc();

  /**
   * Obtiene categorías que tienen productos asociados.
   * Para filtros de búsqueda y navegación efectiva.
   *
   * @return Lista de categorías con productos
   */
  @Query(
    "SELECT c FROM Categoria c WHERE c.totalProductos > 0 AND c.activo = true AND c.eliminado = false ORDER BY c.totalProductos DESC"
  )
  List<Categoria> findCategoriasConProductos();

  /**
   * Busca categorías por texto en nombre o descripción.
   * Para funcionalidad de búsqueda general y filtros.
   *
   * @param texto Texto a buscar (case-insensitive)
   * @return Lista de categorías que coinciden con el texto
   */
  @Query(
    "SELECT c FROM Categoria c WHERE (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))) AND c.activo = true AND c.eliminado = false ORDER BY c.popularidad DESC"
  )
  List<Categoria> buscarPorTexto(@Param("texto") String texto);

  /**
   * Busca categorías por palabras clave específicas.
   * Para SEO y búsquedas especializadas.
   *
   * @param palabraClave Palabra clave a buscar
   * @return Lista de categorías con la palabra clave
   */
  @Query(
    "SELECT c FROM Categoria c WHERE LOWER(c.palabrasClave) LIKE LOWER(CONCAT('%', :palabraClave, '%')) AND c.activo = true AND c.eliminado = false ORDER BY c.popularidad DESC"
  )
  List<Categoria> buscarPorPalabraClave(
    @Param("palabraClave") String palabraClave
  );

  /**
   * Obtiene estadísticas de categorías por departamento.
   * Para reportes analíticos y dashboards ejecutivos.
   *
   * @return Lista con conteo de categorías por departamento
   */
  @Query(
    "SELECT c.departamento, COUNT(c) as total FROM Categoria c WHERE c.activo = true AND c.eliminado = false GROUP BY c.departamento ORDER BY total DESC"
  )
  List<Object[]> obtenerEstadisticasPorDepartamento();

  /**
   * Obtiene categorías con mayor crecimiento en ventas.
   * Para identificar tendencias y oportunidades de negocio.
   *
   * @param limite Número máximo de resultados
   * @return Lista de categorías con crecimiento en ventas
   */
  @Query(
    value = "SELECT c FROM Categoria c WHERE c.totalVentas > 0 AND c.activo = true AND c.eliminado = false ORDER BY c.totalVentas DESC, c.popularidad DESC"
  )
  List<Categoria> findCategoriasConCrecimiento(Pageable pageable);

  /**
   * Cuenta el total de categorías activas por nivel jerárquico.
   * Para análisis de profundidad del catálogo.
   *
   * @return Lista con nivel y conteo de categorías
   */
  @Query(
    "SELECT c.nivel, COUNT(c) as total FROM Categoria c WHERE c.activo = true AND c.eliminado = false GROUP BY c.nivel ORDER BY c.nivel"
  )
  List<Object[]> contarCategoriasPorNivel();

  // ========================================
  // MÉTODOS DE FILTROS Y CONTEO
  // ========================================

  /**
   * Busca categorías aplicando múltiples filtros dinámicos.
   * Utiliza JPQL con parámetros opcionales para máxima flexibilidad.
   *
   * @param nombre Filtro por nombre (busca coincidencias parciales)
   * @param descripcion Filtro por descripción (busca coincidencias parciales)
   * @param departamento Filtro exacto por departamento
   * @param tipo Filtro por tipo de categoría
   * @param estado Filtro por estado de aprobación
   * @param activo Filtro por estado activo/inactivo
   * @param destacado Filtro por categorías destacadas
   * @param permiteProductos Filtro por categorías que permiten productos
   * @param categoriaPadreId Filtro por categoría padre
   * @param nivel Filtro por nivel jerárquico
   * @param pageable Configuración de paginación
   * @return Page con categorías filtradas
   */
  @Query(
    """
    SELECT c FROM Categoria c
    WHERE c.eliminado = false
    AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
    AND (:descripcion IS NULL OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
    AND (:departamento IS NULL OR c.departamento = :departamento)
    AND (:tipo IS NULL OR c.tipo = :tipo)
    AND (:estado IS NULL OR c.estadoAprobacion = :estado)
    AND (:activo IS NULL OR c.activo = :activo)
    AND (:destacado IS NULL OR c.destacada = :destacado)
    AND (:permiteProductos IS NULL OR c.permiteProductos = :permiteProductos)
    AND (:categoriaPadreId IS NULL OR c.categoriaPadre.id = :categoriaPadreId)
    AND (:nivel IS NULL OR c.nivel = :nivel)
    ORDER BY c.ordenVisualizacion ASC, c.nombre ASC
    """
  )
  Page<Categoria> findByFilters(
    @Param("nombre") String nombre,
    @Param("descripcion") String descripcion,
    @Param("departamento") String departamento,
    @Param("tipo") Categoria.TipoCategoria tipo,
    @Param("estado") Categoria.EstadoCategoria estado,
    @Param("activo") Boolean activo,
    @Param("destacado") Boolean destacado,
    @Param("permiteProductos") Boolean permiteProductos,
    @Param("categoriaPadreId") Long categoriaPadreId,
    @Param("nivel") Integer nivel,
    Pageable pageable
  );

  /**
   * Obtiene todas las categorías activas con paginación.
   * Método optimizado para listados generales sin filtros.
   *
   * @param pageable Configuración de paginación
   * @return Page con categorías activas
   */
  @Query(
    "SELECT c FROM Categoria c WHERE c.activo = true AND c.eliminado = false ORDER BY c.ordenVisualizacion ASC, c.nombre ASC"
  )
  Page<Categoria> findAllActive(Pageable pageable);

  /**
   * Cuenta el total de categorías activas.
   * Método optimizado para estadísticas rápidas.
   *
   * @return Número total de categorías activas
   */
  @Query(
    "SELECT COUNT(c) FROM Categoria c WHERE c.activo = true AND c.eliminado = false"
  )
  long countAllActive();

  /**
   * Cuenta el total de categorías inactivas.
   * Método optimizado para estadísticas rápidas.
   *
   * @return Número total de categorías inactivas
   */
  @Query(
    "SELECT COUNT(c) FROM Categoria c WHERE c.activo = false AND c.eliminado = false"
  )
  long countAllInactive();

  /**
   * Busca una categoría por ID que no esté eliminada.
   * Método de conveniencia para operaciones de actualización.
   *
   * @param id ID de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<Categoria> findByIdAndEliminadoFalse(Long id);

  // ========================================
  // MÉTODOS ADICIONALES PARA CONTROLADORES
  // ========================================

  /**
   * Obtiene categorías visibles en menú hasta un nivel específico.
   *
   * @param maxNivel Nivel máximo a incluir
   * @return Lista de categorías para menú
   */
  @Query(
    "SELECT c FROM Categoria c WHERE c.visibleEnMenu = true AND c.activo = true AND c.eliminado = false AND (:maxNivel IS NULL OR c.nivel <= :maxNivel) ORDER BY c.nivel ASC, c.ordenVisualizacion ASC"
  )
  List<Categoria> findVisibleInMenuWithLevel(
    @Param("maxNivel") Integer maxNivel
  );

  /**
   * Busca categorías raíz activas.
   *
   * @return Lista de categorías raíz activas
   */
  List<Categoria> findByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalse();

  /**
   * Busca todas las categorías raíz no eliminadas.
   *
   * @return Lista de categorías raíz
   */
  List<Categoria> findByCategoriaPadreIsNullAndEliminadoFalse();

  /**
   * Cuenta categorías raíz no eliminadas.
   *
   * @return Número de categorías raíz
   */
  long countByCategoriaPadreIsNullAndEliminadoFalse();

  /**
   * Cuenta categorías raíz activas.
   *
   * @return Número de categorías raíz activas
   */
  long countByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalse();

  /**
   * Encuentra el nivel máximo de jerarquía.
   *
   * @return Optional con el nivel máximo
   */
  @Query("SELECT MAX(c.nivel) FROM Categoria c WHERE c.eliminado = false")
  Optional<Integer> findMaxNivelHierarquia();

  /**
   * Cuenta categorías con número específico de productos.
   *
   * @param totalProductos Número de productos
   * @return Número de categorías
   */
  long countByTotalProductosAndEliminadoFalse(Long totalProductos);

  /**
   * Encuentra la categoría con más productos.
   *
   * @return Optional de la categoría con más productos
   */
  Optional<Categoria> findTopByOrderByTotalProductosDesc();

  /**
   * Encuentra categorías destacadas activas con límite.
   *
   * @param pageable Configuración de paginación para limitar resultados
   * @return Lista de categorías destacadas
   */
  List<Categoria> findByDestacadaTrueAndActivoTrueAndEliminadoFalse(
    Pageable pageable
  );

  /**
   * Busca categorías por nombre que contenga texto, activas y no eliminadas.
   *
   * @param texto Texto a buscar en el nombre
   * @param pageable Configuración de paginación para limitar resultados
   * @return Lista de categorías que coinciden
   */
  List<
    Categoria
  > findByNombreContainingIgnoreCaseAndActivoTrueAndEliminadoFalse(
    String texto,
    Pageable pageable
  );

  /**
   * Busca una categoría por ID que esté activa y no eliminada.
   *
   * @param id ID de la categoría
   * @return Optional de la categoría encontrada
   */
  Optional<Categoria> findByIdAndActivoTrueAndEliminadoFalse(Long id);
}
