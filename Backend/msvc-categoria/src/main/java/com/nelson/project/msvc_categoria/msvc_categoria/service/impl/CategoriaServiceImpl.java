package com.nelson.project.msvc_categoria.msvc_categoria.service.impl;

import com.nelson.project.msvc_categoria.msvc_categoria.exception.CategoriaBusinessException;
import com.nelson.project.msvc_categoria.msvc_categoria.exception.CategoriaNotFoundException;
import com.nelson.project.msvc_categoria.msvc_categoria.mapper.CategoriaMapper;
import com.nelson.project.msvc_categoria.msvc_categoria.mapper.CategoriaMapperHelper;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaFilterDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaSummaryDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaUpdateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import com.nelson.project.msvc_categoria.msvc_categoria.repository.CategoriaRepository;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementación empresarial del servicio de categorías.
 * Refactorizada para soportar todas las operaciones del repository con principios SOLID.
 *
 * Principios aplicados:
 * - Single Responsibility: Cada método tiene una responsabilidad específica
 * - Open/Closed: Extensible mediante interfaces
 * - Liskov Substitution: Implementa correctamente la interfaz
 * - Interface Segregation: Interfaces específicas por funcionalidad
 * - Dependency Inversion: Depende de abstracciones
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

  private final CategoriaRepository categoriaRepository;
  private final CategoriaMapper categoriaMapper;
  private final CategoriaMapperHelper categoriaMapperHelper;

  // ========================================
  // OPERACIONES CRUD BÁSICAS
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findAll() {
    log.debug("Obteniendo todas las categorías");
    return categoriaRepository
      .findAll()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaDTO> findById(Long id) {
    log.debug("Buscando categoría con ID: {}", id);
    validateId(id);
    return categoriaRepository.findById(id).map(categoriaMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoriaDTO> findAll(Pageable pageable) {
    log.debug(
      "Obteniendo categorías paginadas: page={}, size={}",
      pageable.getPageNumber(),
      pageable.getPageSize()
    );
    return categoriaRepository.findAll(pageable).map(categoriaMapper::toDto);
  }

  @Override
  public CategoriaDTO save(CategoriaCreateDto categoriaCreateDto) {
    log.info("Creando nueva categoría: {}", categoriaCreateDto.getNombre());

    validateCreateDto(categoriaCreateDto);
    validateBusinessRulesForCreate(categoriaCreateDto);

    Categoria categoria = categoriaMapper.fromCreateDto(categoriaCreateDto);

    // Aplicar reglas de negocio automáticas
    applyBusinessRules(categoria);

    Categoria saved = categoriaRepository.save(categoria);

    log.info("Categoría creada exitosamente con ID: {}", saved.getId());
    return categoriaMapper.toDto(saved);
  }

  @Override
  public CategoriaDTO update(Long id, CategoriaCreateDto categoriaCreateDto) {
    log.info("Actualizando categoría con ID: {}", id);

    validateId(id);
    validateCreateDto(categoriaCreateDto);

    Categoria categoria = findEntityById(id);
    validateBusinessRulesForUpdate(categoriaCreateDto, id);

    categoriaMapper.updateFromCreateDto(categoriaCreateDto, categoria);
    applyBusinessRules(categoria);

    Categoria updated = categoriaRepository.save(categoria);

    log.info("Categoría actualizada exitosamente con ID: {}", id);
    return categoriaMapper.toDto(updated);
  }

  @Override
  public CategoriaDTO updateWithSpecificDto(
    Long id,
    CategoriaUpdateDto categoriaUpdateDto
  ) {
    log.info("Actualizando categoría con DTO específico, ID: {}", id);

    validateId(id);
    validateUpdateDto(categoriaUpdateDto);

    Categoria categoria = findEntityById(id);
    validateBusinessRulesForSpecificUpdate(categoriaUpdateDto, id);

    categoriaMapper.updateEntityFromDto(categoriaUpdateDto, categoria);
    applyBusinessRules(categoria);

    Categoria updated = categoriaRepository.save(categoria);

    log.info(
      "Categoría actualizada con DTO específico exitosamente, ID: {}",
      id
    );
    return categoriaMapper.toDto(updated);
  }

  @Override
  public void deleteById(Long id) {
    log.info("Eliminando lógicamente categoría con ID: {}", id);

    validateId(id);
    Categoria categoria = findEntityById(id);

    validateCanDelete(categoria);

    // Soft delete
    categoria.setEliminado(true);
    categoria.setEliminadoPor("SYSTEM"); // TODO: Obtener usuario actual
    categoria.setMotivoEliminacion("Eliminación solicitada por usuario");

    categoriaRepository.save(categoria);

    log.info("Categoría eliminada lógicamente con ID: {}", id);
  }

  @Override
  public void hardDeleteById(Long id) {
    log.warn("Eliminando físicamente categoría con ID: {}", id);

    validateId(id);
    Categoria categoria = findEntityById(id);

    validateCanHardDelete(categoria);

    categoriaRepository.deleteById(id);

    log.warn("Categoría eliminada físicamente con ID: {}", id);
  }

  // ========================================
  // OPERACIONES DE JERARQUÍA
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findRootCategories() {
    log.debug("Obteniendo categorías raíz");
    return categoriaRepository
      .findByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findSubcategories(Long categoriaPadreId) {
    log.debug(
      "Obteniendo subcategorías para categoría padre ID: {}",
      categoriaPadreId
    );
    validateId(categoriaPadreId);
    return categoriaRepository
      .findByCategoriaPadreIdAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion(
        categoriaPadreId
      )
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findByLevel(Integer nivel) {
    log.debug("Obteniendo categorías por nivel: {}", nivel);
    if (nivel == null || nivel < 0) {
      throw new CategoriaBusinessException(
        "El nivel debe ser un número positivo"
      );
    }
    return categoriaRepository
      .findByNivelAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion(nivel)
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaDTO> findWithSubcategories(Long id) {
    log.debug("Obteniendo categoría con subcategorías, ID: {}", id);
    validateId(id);
    return categoriaRepository
      .findById(id)
      .map(categoriaMapper::toDtoWithSubcategorias);
  }

  // ========================================
  // BÚSQUEDAS POR IDENTIFICADORES
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaDTO> findByCode(String codigo) {
    log.debug("Buscando categoría por código: {}", codigo);
    if (!StringUtils.hasText(codigo)) {
      return Optional.empty();
    }
    return categoriaRepository
      .findByCodigoAndEliminadoFalse(codigo.trim())
      .map(categoriaMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaDTO> findBySlug(String slug) {
    log.debug("Buscando categoría por slug: {}", slug);
    if (!StringUtils.hasText(slug)) {
      return Optional.empty();
    }
    return categoriaRepository
      .findBySlugAndEliminadoFalse(slug.trim())
      .map(categoriaMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByCode(String codigo) {
    if (!StringUtils.hasText(codigo)) {
      return false;
    }
    return categoriaRepository.existsByCodigoAndEliminadoFalse(codigo.trim());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsBySlug(String slug) {
    if (!StringUtils.hasText(slug)) {
      return false;
    }
    return categoriaRepository.existsBySlugAndEliminadoFalse(slug.trim());
  }

  // ========================================
  // OPERACIONES EMPRESARIALES
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findFeaturedCategories() {
    log.debug("Obteniendo categorías destacadas");
    return categoriaRepository
      .findByDestacadaTrueAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findCategoriesAllowingProducts() {
    log.debug("Obteniendo categorías que permiten productos");
    return categoriaRepository
      .findByPermiteProductosTrueAndActivoTrueAndEliminadoFalseOrderByNombre()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoriaDTO> findByDepartment(
    String departamento,
    Pageable pageable
  ) {
    log.debug("Obteniendo categorías por departamento: {}", departamento);
    if (!StringUtils.hasText(departamento)) {
      throw new CategoriaBusinessException(
        "El departamento no puede estar vacío"
      );
    }
    return categoriaRepository
      .findByDepartamentoAndActivoTrueAndEliminadoFalse(
        departamento.trim(),
        pageable
      )
      .map(categoriaMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findByType(Categoria.TipoCategoria tipo) {
    log.debug("Obteniendo categorías por tipo: {}", tipo);
    if (tipo == null) {
      throw new CategoriaBusinessException(
        "El tipo de categoría no puede ser nulo"
      );
    }
    return categoriaRepository
      .findByTipoAndActivoTrueAndEliminadoFalseOrderByPopularidadDesc(tipo)
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findByApprovalStatus(
    Categoria.EstadoCategoria estadoAprobacion
  ) {
    log.debug(
      "Obteniendo categorías por estado de aprobación: {}",
      estadoAprobacion
    );
    if (estadoAprobacion == null) {
      throw new CategoriaBusinessException(
        "El estado de aprobación no puede ser nulo"
      );
    }
    return categoriaRepository
      .findByEstadoAprobacionAndEliminadoFalseOrderByFechaCreacion(
        estadoAprobacion
      )
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findVisibleInMenu() {
    log.debug("Obteniendo categorías visibles en menú");
    return categoriaRepository
      .findByVisibleEnMenuTrueAndActivoTrueAndEliminadoFalseOrderByOrdenVisualizacion()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findRequiringApproval() {
    log.debug("Obteniendo categorías que requieren aprobación");
    return categoriaRepository
      .findByRequiereAprobacionTrueAndActivoTrueAndEliminadoFalseOrderByFechaCreacion()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findByPriceRange(
    BigDecimal precioMin,
    BigDecimal precioMax
  ) {
    log.debug(
      "Obteniendo categorías por rango de precios: {} - {}",
      precioMin,
      precioMax
    );
    if (
      precioMin == null ||
      precioMax == null ||
      precioMin.compareTo(precioMax) > 0
    ) {
      throw new CategoriaBusinessException(
        "El rango de precios debe ser válido"
      );
    }
    return categoriaRepository
      .findCategoriasEnRangoPrecios(precioMin, precioMax)
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  // ========================================
  // OPERACIONES DE PERFORMANCE Y ANALYTICS
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaSummaryDto> findMostPopular(int limit) {
    log.debug("Obteniendo categorías más populares, límite: {}", limit);
    validateLimit(limit);
    return categoriaRepository
      .findTop10ByActivoTrueAndEliminadoFalseOrderByPopularidadDesc()
      .stream()
      .limit(limit)
      .map(categoriaMapper::toSummaryDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaSummaryDto> findBestSelling(int limit) {
    log.debug("Obteniendo categorías más vendidas, límite: {}", limit);
    validateLimit(limit);
    return categoriaRepository
      .findTop20ByActivoTrueAndEliminadoFalseOrderByTotalVentasDesc()
      .stream()
      .limit(limit)
      .map(categoriaMapper::toSummaryDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaSummaryDto> findCategoriesWithProducts() {
    log.debug("Obteniendo categorías con productos");
    return categoriaRepository
      .findCategoriasConProductos()
      .stream()
      .map(categoriaMapper::toSummaryDtoWithoutChildren)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> searchByText(String texto) {
    log.debug("Buscando categorías por texto: {}", texto);
    if (!StringUtils.hasText(texto)) {
      return List.of();
    }
    return categoriaRepository
      .buscarPorTexto(texto.trim())
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> searchByKeyword(String palabraClave) {
    log.debug("Buscando categorías por palabra clave: {}", palabraClave);
    if (!StringUtils.hasText(palabraClave)) {
      return List.of();
    }
    return categoriaRepository
      .buscarPorPalabraClave(palabraClave.trim())
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaSummaryDto> findGrowingCategories(int limit) {
    log.debug("Obteniendo categorías con crecimiento, límite: {}", limit);
    validateLimit(limit);
    Pageable pageable = PageRequest.of(0, Math.min(limit, 50));
    return categoriaRepository
      .findCategoriasConCrecimiento(pageable)
      .stream()
      .map(categoriaMapper::toSummaryDto)
      .collect(Collectors.toList());
  }

  // ========================================
  // OPERACIONES DE ESTADÍSTICAS
  // ========================================

  @Override
  @Transactional(readOnly = true)
  public List<Object[]> getCategoryStatsByDepartment() {
    log.debug("Obteniendo estadísticas por departamento");
    return categoriaRepository.obtenerEstadisticasPorDepartamento();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Object[]> countCategoriesByLevel() {
    log.debug("Contando categorías por nivel");
    return categoriaRepository.contarCategoriasPorNivel();
  }

  @Override
  @Transactional(readOnly = true)
  public long getTotalActiveCategories() {
    log.debug("Obteniendo total de categorías activas");
    return categoriaRepository.count(); // Esto puede ser optimizado con una query específica
  }

  @Override
  @Transactional(readOnly = true)
  public long countByApprovalStatus(Categoria.EstadoCategoria estado) {
    log.debug("Contando categorías por estado: {}", estado);
    if (estado == null) {
      throw new CategoriaBusinessException("El estado no puede ser nulo");
    }
    return categoriaRepository
      .findByEstadoAprobacionAndEliminadoFalseOrderByFechaCreacion(estado)
      .size();
  }

  // ========================================
  // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDAD
  // ========================================

  /**
   * Valida que el ID sea válido.
   */
  private void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new CategoriaBusinessException(
        "El ID de la categoría debe ser válido"
      );
    }
  }

  /**
   * Valida que el límite sea válido para consultas paginadas.
   */
  private void validateLimit(int limit) {
    if (limit <= 0 || limit > 1000) {
      throw new CategoriaBusinessException(
        "El límite debe estar entre 1 y 1000"
      );
    }
  }

  /**
   * Busca una entidad por ID y lanza excepción si no existe.
   */
  private Categoria findEntityById(Long id) {
    return categoriaRepository
      .findById(id)
      .orElseThrow(() -> new CategoriaNotFoundException(id));
  }

  /**
   * Valida el DTO de creación.
   */
  private void validateCreateDto(CategoriaCreateDto dto) {
    if (dto == null) {
      throw new CategoriaBusinessException(
        "Los datos de la categoría no pueden ser nulos"
      );
    }
    if (!StringUtils.hasText(dto.getNombre())) {
      throw new CategoriaBusinessException(
        "El nombre de la categoría es obligatorio"
      );
    }
    if (dto.getNombre().length() > 100) {
      throw new CategoriaBusinessException(
        "El nombre no puede exceder 100 caracteres"
      );
    }
  }

  /**
   * Valida el DTO de actualización específico.
   */
  private void validateUpdateDto(CategoriaUpdateDto dto) {
    if (dto == null) {
      throw new CategoriaBusinessException(
        "Los datos de actualización no pueden ser nulos"
      );
    }
    // Validaciones específicas del DTO de actualización
    if (dto.getNombre() != null && dto.getNombre().length() > 100) {
      throw new CategoriaBusinessException(
        "El nombre no puede exceder 100 caracteres"
      );
    }
  }

  /**
   * Valida reglas de negocio para creación.
   */
  private void validateBusinessRulesForCreate(CategoriaCreateDto dto) {
    // Validar código único si se proporciona
    if (StringUtils.hasText(dto.getCodigo())) {
      if (existsByCode(dto.getCodigo())) {
        throw new CategoriaBusinessException(
          "Ya existe una categoría con el código: " + dto.getCodigo()
        );
      }
    }

    // Validar slug único si se proporciona
    if (StringUtils.hasText(dto.getSlug())) {
      if (existsBySlug(dto.getSlug())) {
        throw new CategoriaBusinessException(
          "Ya existe una categoría con el slug: " + dto.getSlug()
        );
      }
    }

    // Validar nombre no contiene caracteres peligrosos
    if (dto.getNombre().matches(".*[<>\"'&].*")) {
      throw new CategoriaBusinessException(
        "El nombre contiene caracteres no permitidos"
      );
    }
  }

  /**
   * Valida reglas de negocio para actualización.
   */
  private void validateBusinessRulesForUpdate(
    CategoriaCreateDto dto,
    Long excludeId
  ) {
    // Validar código único excluyendo la categoría actual
    if (StringUtils.hasText(dto.getCodigo())) {
      Optional<CategoriaDTO> existing = findByCode(dto.getCodigo());
      if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
        throw new CategoriaBusinessException(
          "Ya existe una categoría con el código: " + dto.getCodigo()
        );
      }
    }

    // Validaciones adicionales...
    validateBasicBusinessRules(dto.getNombre(), dto.getDescripcion());
  }

  /**
   * Valida reglas de negocio para actualización específica.
   */
  private void validateBusinessRulesForSpecificUpdate(
    CategoriaUpdateDto dto,
    Long excludeId
  ) {
    // El CategoriaUpdateDto no tiene código, solo validar nombre y descripción
    if (StringUtils.hasText(dto.getNombre())) {
      validateBasicBusinessRules(dto.getNombre(), dto.getDescripcion());

      // Validar nombre único excluyendo la categoría actual
      List<Categoria> categorias = categoriaRepository.findAll();
      boolean nombreExists = categorias
        .stream()
        .anyMatch(
          cat ->
            cat.getNombre().equalsIgnoreCase(dto.getNombre().trim()) &&
            !cat.getId().equals(excludeId) &&
            !cat.getEliminado()
        );

      if (nombreExists) {
        throw new CategoriaBusinessException(
          "Ya existe una categoría con el nombre: " + dto.getNombre()
        );
      }
    }

    // Validar slug único si se proporciona
    if (StringUtils.hasText(dto.getSlug())) {
      Optional<CategoriaDTO> existing = findBySlug(dto.getSlug());
      if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
        throw new CategoriaBusinessException(
          "Ya existe una categoría con el slug: " + dto.getSlug()
        );
      }
    }
  }

  /**
   * Validaciones básicas de reglas de negocio.
   */
  private void validateBasicBusinessRules(String nombre, String descripcion) {
    if (StringUtils.hasText(nombre) && nombre.matches(".*[<>\"'&].*")) {
      throw new CategoriaBusinessException(
        "El nombre contiene caracteres no permitidos"
      );
    }

    if (StringUtils.hasText(descripcion) && descripcion.length() > 2000) {
      throw new CategoriaBusinessException(
        "La descripción no puede exceder 2000 caracteres"
      );
    }
  }

  /**
   * Aplica reglas de negocio automáticas a la entidad.
   */
  private void applyBusinessRules(Categoria categoria) {
    // Generar código si no existe
    if (!StringUtils.hasText(categoria.getCodigo())) {
      categoria.generarCodigo();

      // Asegurar unicidad del código generado
      String codigoBase = categoria.getCodigo();
      int contador = 1;
      while (
        categoriaRepository.existsByCodigoAndEliminadoFalse(
          categoria.getCodigo()
        )
      ) {
        categoria.setCodigo(codigoBase + "_" + contador);
        contador++;
        // Limitar a 20 caracteres
        if (categoria.getCodigo().length() > 20) {
          categoria.setCodigo(categoria.getCodigo().substring(0, 20));
        }
      }
    }

    // Generar slug si no existe
    if (!StringUtils.hasText(categoria.getSlug())) {
      categoria.generarSlug();
    }

    // Generar ruta completa
    categoria.generarRutaCompleta();

    // Calcular popularidad
    categoria.calcularPopularidad();

    // Establecer valores por defecto
    if (categoria.getActivo() == null) {
      categoria.setActivo(true);
    }

    if (categoria.getEliminado() == null) {
      categoria.setEliminado(false);
    }

    // Usar helper para cálculos complejos si es necesario
    if (categoriaMapperHelper != null) {
      // Aplicar lógica compleja usando el helper
      // Por ejemplo: calcular jerarquía, validar niveles, etc.
    }
  }

  /**
   * Valida si una categoría puede ser eliminada.
   */
  private void validateCanDelete(Categoria categoria) {
    // Verificar si tiene subcategorías
    if (categoria.tieneSubcategorias()) {
      throw new CategoriaBusinessException(
        "No se puede eliminar la categoría porque tiene subcategorías asociadas"
      );
    }

    // Verificar si tiene productos (si el campo totalProductos > 0)
    if (
      categoria.getTotalProductos() != null && categoria.getTotalProductos() > 0
    ) {
      throw new CategoriaBusinessException(
        "No se puede eliminar la categoría porque tiene productos asociados"
      );
    }
  }

  /**
   * Valida si una categoría puede ser eliminada físicamente.
   */
  private void validateCanHardDelete(Categoria categoria) {
    validateCanDelete(categoria);

    // Validaciones adicionales para eliminación física
    if (!categoria.getEliminado()) {
      throw new CategoriaBusinessException(
        "Solo se pueden eliminar físicamente categorías que ya estén eliminadas lógicamente"
      );
    }
  }

  // ========================================
  // MÉTODOS DE FILTROS Y PAGINACIÓN
  // ========================================

  @Override
  @Transactional(readOnly = true)
  @Cacheable(
    value = "categorias",
    key = "'filter_' + #filter.toString() + '_page_' + #pageable.pageNumber"
  )
  public Page<CategoriaDTO> findByFilters(
    CategoriaFilterDto filter,
    Pageable pageable
  ) {
    log.info(
      "🔍 Buscando categorías con filtros: {}, página: {}",
      filter,
      pageable.getPageNumber()
    );

    try {
      Page<Categoria> categorias;

      if (hasActiveFilters(filter)) {
        categorias = categoriaRepository.findByFilters(
          filter.getTexto(), // nombre
          filter.getTexto(), // descripcion (same text)
          filter.getDepartamento(),
          filter.getTipo(),
          filter.getEstadoAprobacion(), // estado
          filter.getActivo(),
          filter.getDestacada(), // destacado
          filter.getPermiteProductos(),
          filter.getCategoriaPadreId(),
          filter.getNivel(),
          pageable
        );
      } else {
        categorias = categoriaRepository.findAllActive(pageable);
      }

      Page<CategoriaDTO> result = categorias.map(categoriaMapper::toDto);
      log.info(
        "✅ Encontradas {} categorías con filtros",
        result.getTotalElements()
      );
      return result;
    } catch (Exception e) {
      log.error(
        "❌ Error al buscar categorías con filtros: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al aplicar filtros de búsqueda: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(
    value = "categorias",
    key = "'active_page_' + #pageable.pageNumber"
  )
  public Page<CategoriaDTO> findAllActive(Pageable pageable) {
    log.info(
      "📋 Obteniendo categorías activas - página: {}",
      pageable.getPageNumber()
    );

    try {
      Page<Categoria> categorias = categoriaRepository.findAllActive(pageable);
      Page<CategoriaDTO> result = categorias.map(categoriaMapper::toDto);

      log.info("✅ Obtenidas {} categorías activas", result.getTotalElements());
      return result;
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener categorías activas: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al obtener categorías activas: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaStats", key = "'countActive'")
  public long countAllActive() {
    log.info("🔢 Contando categorías activas");

    try {
      long count = categoriaRepository.countAllActive();
      log.info("✅ Total de categorías activas: {}", count);
      return count;
    } catch (Exception e) {
      log.error("❌ Error al contar categorías activas: {}", e.getMessage(), e);
      throw new CategoriaBusinessException(
        "Error al contar categorías activas: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaStats", key = "'countInactive'")
  public long countAllInactive() {
    log.info("🔢 Contando categorías inactivas");

    try {
      long count = categoriaRepository.countAllInactive();
      log.info("✅ Total de categorías inactivas: {}", count);
      return count;
    } catch (Exception e) {
      log.error(
        "❌ Error al contar categorías inactivas: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al contar categorías inactivas: " + e.getMessage()
      );
    }
  }

  // ========================================
  // OPERACIONES DE ACTUALIZACIÓN ESPECÍFICAS
  // ========================================

  @Override
  @Transactional
  @CacheEvict(
    value = { "categorias", "categoriaStats", "categoriaHierarchy" },
    allEntries = true
  )
  public CategoriaDTO update(Long id, CategoriaUpdateDto categoriaUpdateDto) {
    log.info("🔄 Actualizando categoría ID: {} con DTO específico", id);

    try {
      // Validar existencia
      Categoria existingCategoria = categoriaRepository
        .findByIdAndEliminadoFalse(id)
        .orElseThrow(() ->
          new CategoriaNotFoundException(
            String.format(
              "Categoría con ID %d no encontrada o está eliminada",
              id
            )
          )
        );

      // Validar reglas de negocio específicas para actualización
      validateBusinessRulesForSpecificUpdate(categoriaUpdateDto, id);

      // Aplicar cambios usando el mapper
      categoriaMapper.updateEntityFromDto(
        categoriaUpdateDto,
        existingCategoria
      );

      // La auditoría se maneja automáticamente con @PreUpdate
      // No necesitamos establecer campos de auditoría manualmente

      // Guardar
      Categoria savedCategoria = categoriaRepository.save(existingCategoria);
      CategoriaDTO result = categoriaMapper.toDto(savedCategoria);

      log.info("✅ Categoría actualizada exitosamente: {}", result.getNombre());
      return result;
    } catch (CategoriaNotFoundException e) {
      log.error(
        "❌ Categoría no encontrada para actualizar: {}",
        e.getMessage()
      );
      throw e;
    } catch (CategoriaBusinessException e) {
      log.error(
        "❌ Violación de reglas de negocio en actualización: {}",
        e.getMessage()
      );
      throw e;
    } catch (Exception e) {
      log.error(
        "❌ Error inesperado al actualizar categoría: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error interno al actualizar la categoría: " + e.getMessage()
      );
    }
  }

  // ========================================
  // MÉTODOS AUXILIARES PARA FILTROS
  // ========================================

  /**
   * Verifica si el filtro tiene valores activos que requieren aplicar filtros específicos.
   */
  private boolean hasActiveFilters(CategoriaFilterDto filter) {
    return (
      filter != null &&
      (StringUtils.hasText(filter.getTexto()) ||
        StringUtils.hasText(filter.getDepartamento()) ||
        filter.getTipo() != null ||
        filter.getEstadoAprobacion() != null ||
        filter.getActivo() != null ||
        filter.getDestacada() != null ||
        filter.getPermiteProductos() != null ||
        filter.getCategoriaPadreId() != null ||
        filter.getNivel() != null)
    );
  }

  // ========================================
  // MÉTODOS ADICIONALES PARA CONTROLADORES
  // ========================================

  @Override
  @Transactional(readOnly = true)
  @Cacheable(
    value = "categorias",
    key = "'activeSummary_page_' + #pageable.pageNumber"
  )
  public Page<CategoriaSummaryDto> findAllActiveSummary(Pageable pageable) {
    log.info(
      "📋 Obteniendo categorías activas en formato resumen - página: {}",
      pageable.getPageNumber()
    );

    try {
      // Timeout para evitar spinners infinitos (max 10 segundos)
      long startTime = System.currentTimeMillis();

      Page<Categoria> categorias = categoriaRepository.findAllActive(pageable);

      long duration = System.currentTimeMillis() - startTime;
      if (duration > 8000) { // Si demora más de 8 segundos, advertir
        log.warn(
          "⚠️  Consulta de categorías demoró {} ms - considerando optimización",
          duration
        );
      }

      Page<CategoriaSummaryDto> result = categorias.map(
        categoriaMapper::toSummaryDto
      );

      log.info(
        "✅ Obtenidas {} categorías activas en formato resumen en {} ms",
        result.getTotalElements(),
        duration
      );
      return result;
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener categorías activas en formato resumen: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al obtener categorías activas: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoriaDTO> searchByText(
    String texto,
    Boolean soloActivas,
    Pageable pageable
  ) {
    log.info(
      "🔍 Buscando categorías por texto: '{}', solo activas: {}",
      texto,
      soloActivas
    );

    try {
      // Usar el repository existente como base
      List<CategoriaDTO> allResults = searchByText(texto);

      // Filtrar por estado activo si se especifica
      List<CategoriaDTO> filteredResults = allResults
        .stream()
        .filter(
          cat -> soloActivas == null || cat.getActivo().equals(soloActivas)
        )
        .collect(Collectors.toList());

      // Crear página manualmente (simplificado para corregir errores)
      int start = (int) pageable.getOffset();
      int end = Math.min(
        (start + pageable.getPageSize()),
        filteredResults.size()
      );
      List<CategoriaDTO> pageContent = filteredResults.subList(start, end);

      // Crear Page usando Spring Data
      return new org.springframework.data.domain.PageImpl<>(
        pageContent,
        pageable,
        filteredResults.size()
      );
    } catch (Exception e) {
      log.error(
        "❌ Error al buscar categorías por texto: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error en búsqueda por texto: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categorias", key = "'navigationMenu_' + #maxNivel")
  public List<CategoriaSummaryDto> findNavigationMenu(Integer maxNivel) {
    log.info("🧭 Obteniendo menú de navegación con nivel máximo: {}", maxNivel);

    try {
      // Obtener categorías visibles en menú hasta el nivel especificado
      List<Categoria> menuCategories =
        categoriaRepository.findVisibleInMenuWithLevel(maxNivel);
      List<CategoriaSummaryDto> result = menuCategories
        .stream()
        .map(categoriaMapper::toSummaryDto)
        .collect(Collectors.toList());

      log.info(
        "✅ Obtenidas {} categorías para menú de navegación",
        result.size()
      );
      return result;
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener menú de navegación: {}",
        e.getMessage(),
        e
      );
      // Fallback: devolver categorías raíz activas
      List<Categoria> fallbackCategories =
        categoriaRepository.findByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalse();
      return fallbackCategories
        .stream()
        .map(categoriaMapper::toSummaryDto)
        .collect(Collectors.toList());
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categorias", key = "'featuredSummary_' + #limite")
  public List<CategoriaSummaryDto> findFeaturedCategoriesSummary(
    Integer limite
  ) {
    log.info(
      "⭐ Obteniendo categorías destacadas en formato resumen, límite: {}",
      limite
    );

    try {
      List<Categoria> featured =
        categoriaRepository.findByDestacadaTrueAndActivoTrueAndEliminadoFalse(
          limite != null ? PageRequest.of(0, limite) : PageRequest.of(0, 10)
        );
      return featured
        .stream()
        .map(categoriaMapper::toSummaryDto)
        .collect(Collectors.toList());
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener categorías destacadas: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al obtener categorías destacadas: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaSummaryDto> searchActiveCategoriesSummary(
    String texto,
    Integer limite
  ) {
    log.info(
      "🔍 Buscando categorías activas por texto: '{}', límite: {}",
      texto,
      limite
    );

    try {
      List<Categoria> searchResults =
        categoriaRepository.findByNombreContainingIgnoreCaseAndActivoTrueAndEliminadoFalse(
          texto,
          limite != null ? PageRequest.of(0, limite) : PageRequest.of(0, 20)
        );
      return searchResults
        .stream()
        .map(categoriaMapper::toSummaryDto)
        .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("❌ Error al buscar categorías activas: {}", e.getMessage(), e);
      throw new CategoriaBusinessException(
        "Error en búsqueda de categorías activas: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaSummaryDto> findByIdActiveSummary(Long id) {
    log.info("🔍 Buscando categoría activa por ID: {}", id);

    try {
      Optional<Categoria> categoria =
        categoriaRepository.findByIdAndActivoTrueAndEliminadoFalse(id);
      return categoria.map(categoriaMapper::toSummaryDto);
    } catch (Exception e) {
      log.error(
        "❌ Error al buscar categoría activa por ID: {}",
        e.getMessage(),
        e
      );
      return Optional.empty();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoriaDTO> findSubcategoriesByParentId(Long parentId) {
    return findSubcategories(parentId);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaHierarchy", key = "'activeHierarchy'")
  public List<CategoriaDTO> findActiveCategoryHierarchy() {
    log.info("🌳 Obteniendo jerarquía de categorías activas");

    try {
      List<Categoria> activeRoots =
        categoriaRepository.findByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalse();
      return activeRoots
        .stream()
        .map(categoriaMapper::toDtoWithSubcategorias)
        .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("❌ Error al obtener jerarquía activa: {}", e.getMessage(), e);
      throw new CategoriaBusinessException(
        "Error al obtener jerarquía activa: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaHierarchy", key = "'fullHierarchy'")
  public List<CategoriaDTO> findFullCategoryHierarchy() {
    log.info("🌳 Obteniendo jerarquía completa de categorías");

    try {
      List<Categoria> allRoots =
        categoriaRepository.findByCategoriaPadreIsNullAndEliminadoFalse();
      return allRoots
        .stream()
        .map(categoriaMapper::toDtoWithSubcategorias)
        .collect(Collectors.toList());
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener jerarquía completa: {}",
        e.getMessage(),
        e
      );
      throw new CategoriaBusinessException(
        "Error al obtener jerarquía completa: " + e.getMessage()
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaStats", key = "'countAll'")
  public long countAll() {
    return categoriaRepository.count();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaStats", key = "'countRoots'")
  public long countRootCategories() {
    return categoriaRepository.countByCategoriaPadreIsNullAndEliminadoFalse();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "categoriaStats", key = "'countActiveRoots'")
  public long countActiveRootCategories() {
    return categoriaRepository.countByCategoriaPadreIsNullAndActivoTrueAndEliminadoFalse();
  }

  @Override
  @Transactional(readOnly = true)
  public Double getAverageSubcategoriesPerCategory() {
    // Implementación simplificada
    try {
      long totalCategories = countAll();
      long rootCategories = countRootCategories();
      if (rootCategories == 0) return 0.0;
      return (double) (totalCategories - rootCategories) / rootCategories;
    } catch (Exception e) {
      log.error(
        "❌ Error al calcular promedio de subcategorías: {}",
        e.getMessage(),
        e
      );
      return 0.0;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getMaxHierarchyLevel() {
    try {
      return categoriaRepository.findMaxNivelHierarquia().orElse(1);
    } catch (Exception e) {
      log.error("❌ Error al obtener nivel máximo: {}", e.getMessage(), e);
      return 1;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public long countCategoriesWithoutProducts() {
    return categoriaRepository.countByTotalProductosAndEliminadoFalse(0L);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoriaDTO> getCategoryWithMostProducts() {
    try {
      Optional<Categoria> topCategory =
        categoriaRepository.findTopByOrderByTotalProductosDesc();
      return topCategory.map(categoriaMapper::toDto);
    } catch (Exception e) {
      log.error(
        "❌ Error al obtener categoría con más productos: {}",
        e.getMessage(),
        e
      );
      return Optional.empty();
    }
  }
}
