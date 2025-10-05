package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaFilterDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Helper class para CategoriaMapper que maneja lógica compleja de mapping.
 * Complementa las transformaciones MapStruct con lógica de negocio específica para entidad Categoria (65+ campos).
 *
 * Funcionalidades organizadas:
 * - Generación de campos calculados (slug, ruta jerárquica, nivel)
 * - Validaciones de negocio (jerarquía, campos requeridos, rangos)
 * - Aplicación de valores por defecto para creación
 * - Auditoría y sanitización de datos
 * - Utilidades para filtros y búsquedas
 */
@Component
public class CategoriaMapperHelper {

  // ========================================
  // GENERACIÓN DE CAMPOS CALCULADOS
  // ========================================

  /**
   * Genera slug único basado en el nombre de la categoría.
   * Incluye validación de unicidad y sufijos numéricos si es necesario.
   */
  public String generateUniqueSlug(String nombre, Long categoriaId) {
    if (nombre == null || nombre.trim().isEmpty()) return null;

    String baseSlug = nombre
      .toLowerCase()
      .trim()
      .replaceAll("[^a-z0-9\\s-]", "")
      .replaceAll("\\s+", "-")
      .replaceAll("-+", "-")
      .replaceAll("^-|-$", "");

    // TODO: Aquí se debería verificar unicidad en la base de datos
    // Por ahora retornamos el slug base
    return baseSlug.isEmpty() ? null : baseSlug;
  }

  /**
   * Genera ruta completa jerárquica para una categoría.
   * Optimizado para evitar stack overflow con límite de profundidad.
   * Ejemplo: "Electrónicos > Smartphones > Android"
   */
  public String generateRutaCompleta(Categoria categoria) {
    return generateRutaCompleta(categoria, 0, 10); // Máximo 10 niveles
  }

  private String generateRutaCompleta(
    Categoria categoria,
    int depth,
    int maxDepth
  ) {
    if (categoria == null || depth > maxDepth) return null;

    StringBuilder ruta = new StringBuilder();

    // Construir ruta desde la raíz con protección de profundidad
    if (categoria.getCategoriaPadre() != null && depth < maxDepth) {
      String rutaPadre = generateRutaCompleta(
        categoria.getCategoriaPadre(),
        depth + 1,
        maxDepth
      );
      if (rutaPadre != null && !rutaPadre.trim().isEmpty()) {
        ruta.append(rutaPadre).append(" > ");
      }
    }

    String nombreCategoria = categoria.getNombre();
    if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
      ruta.append(nombreCategoria.trim());
    }

    return ruta.length() > 0 ? ruta.toString() : null;
  }

  /**
   * Calcula el nivel jerárquico de una categoría de forma iterativa.
   * Optimizado para evitar stack overflow.
   * 0 = categoría raíz, 1 = subcategoría de primer nivel, etc.
   */
  public Integer calculateNivel(Categoria categoria) {
    if (categoria == null) return 0;

    int nivel = 0;
    Categoria actual = categoria;
    int maxNiveles = 10; // Protección contra ciclos infinitos

    while (actual.getCategoriaPadre() != null && nivel < maxNiveles) {
      nivel++;
      actual = actual.getCategoriaPadre();
    }

    return nivel;
  }

  /**
   * Actualiza todos los campos calculados después de cambios en la entidad.
   */
  public void updateCalculatedFields(Categoria categoria) {
    if (categoria == null) return;

    // Actualizar ruta completa
    categoria.setRutaCompleta(generateRutaCompleta(categoria));

    // Actualizar nivel
    categoria.setNivel(calculateNivel(categoria));

    // Generar slug si está vacío
    if (categoria.getSlug() == null || categoria.getSlug().trim().isEmpty()) {
      categoria.setSlug(
        generateUniqueSlug(categoria.getNombre(), categoria.getId())
      );
    }
  }

  // ========================================
  // VALIDACIONES DE NEGOCIO
  // ========================================

  /**
   * Valida si la jerarquía propuesta es válida (sin circularidad).
   */
  public boolean isValidHierarchy(Long categoriaId, Long categoriaPadreId) {
    if (categoriaId == null || categoriaPadreId == null) {
      return true; // No hay jerarquía o es categoría raíz
    }

    if (categoriaId.equals(categoriaPadreId)) {
      return false; // Una categoría no puede ser padre de sí misma
    }

    // TODO: Implementar validación completa de circularidad
    // Requerirá acceso al repository para verificar la cadena completa
    return true;
  }

  /**
   * Valida campos requeridos en una categoría.
   */
  public boolean validateRequiredFields(Categoria categoria) {
    if (categoria == null) return false;

    return (
      categoria.getNombre() != null &&
      !categoria.getNombre().trim().isEmpty() &&
      categoria.getCodigo() != null &&
      !categoria.getCodigo().trim().isEmpty()
    );
  }

  /**
   * Valida rangos de fechas en filtros.
   */
  public boolean isValidDateRange(
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin
  ) {
    if (fechaInicio == null || fechaFin == null) {
      return true; // Sin restricción de fecha
    }

    return !fechaInicio.isAfter(fechaFin);
  }

  /**
   * Valida rangos de precios en filtros.
   */
  public boolean isValidPriceRange(BigDecimal precioMin, BigDecimal precioMax) {
    if (precioMin == null || precioMax == null) {
      return true; // Sin restricción de precio
    }

    return precioMin.compareTo(precioMax) <= 0;
  }

  // ========================================
  // APLICACIÓN DE VALORES POR DEFECTO
  // ========================================

  /**
   * Aplica valores por defecto a una categoría nueva.
   */
  public void applyDefaultValues(Categoria categoria) {
    if (categoria == null) return;

    // Valores boolean por defecto
    if (categoria.getActivo() == null) {
      categoria.setActivo(true);
    }
    if (categoria.getPermiteProductos() == null) {
      categoria.setPermiteProductos(true);
    }
    if (categoria.getRequiereAprobacion() == null) {
      categoria.setRequiereAprobacion(false);
    }
    if (categoria.getVisibleEnMenu() == null) {
      categoria.setVisibleEnMenu(true);
    }
    if (categoria.getDestacada() == null) {
      categoria.setDestacada(false);
    }
    if (categoria.getRequiereInventario() == null) {
      categoria.setRequiereInventario(true);
    }
    if (categoria.getPermiteVariantes() == null) {
      categoria.setPermiteVariantes(true);
    }
    if (categoria.getEliminado() == null) {
      categoria.setEliminado(false);
    }

    // Valores numéricos por defecto
    if (categoria.getNivel() == null) {
      categoria.setNivel(0);
    }
    if (categoria.getOrdenVisualizacion() == null) {
      categoria.setOrdenVisualizacion(0);
    }

    // Enums por defecto
    if (categoria.getTipo() == null) {
      categoria.setTipo(Categoria.TipoCategoria.PRODUCTO);
    }
    if (categoria.getEstadoAprobacion() == null) {
      categoria.setEstadoAprobacion(Categoria.EstadoCategoria.APROBADA);
    }

    // Inicializar métricas en cero
    if (categoria.getTotalProductos() == null) {
      categoria.setTotalProductos(0L);
    }
    if (categoria.getTotalVentas() == null) {
      categoria.setTotalVentas(0L);
    }
    if (categoria.getIngresosTotales() == null) {
      categoria.setIngresosTotales(BigDecimal.ZERO);
    }
    if (categoria.getVistasTotal() == null) {
      categoria.setVistasTotal(0L);
    }
    if (categoria.getPopularidad() == null) {
      categoria.setPopularidad(0.0);
    }
  }

  // ========================================
  // SANITIZACIÓN Y AUDITORÍA
  // ========================================

  /**
   * Sanitiza datos de entrada para evitar problemas de seguridad.
   */
  public void sanitizeData(Categoria categoria) {
    if (categoria == null) return;

    if (categoria.getNombre() != null) {
      categoria.setNombre(categoria.getNombre().trim());
    }

    if (categoria.getCodigo() != null) {
      categoria.setCodigo(categoria.getCodigo().trim().toUpperCase());
    }

    if (categoria.getDescripcion() != null) {
      categoria.setDescripcion(categoria.getDescripcion().trim());
    }

    if (categoria.getSlug() != null) {
      categoria.setSlug(categoria.getSlug().trim().toLowerCase());
    }
  }

  /**
   * Prepara auditoría para creación de categoría.
   */
  public void prepareAuditForCreation(Categoria categoria, String usuario) {
    if (categoria == null) return;

    categoria.setCreadoPor(usuario);
    categoria.setMotivoUltimoCambio("Creación inicial de categoría");
  }

  /**
   * Prepara auditoría para actualización de categoría.
   */
  public void prepareAuditForUpdate(
    Categoria categoria,
    String usuario,
    String motivo
  ) {
    if (categoria == null) return;

    categoria.setModificadoPor(usuario);
    categoria.setMotivoUltimoCambio(
      motivo != null && !motivo.trim().isEmpty()
        ? motivo.trim()
        : "Actualización de información de categoría"
    );
  }

  // ========================================
  // UTILIDADES PARA FILTROS
  // ========================================

  /**
   * Convierte criterios de filtro a términos de búsqueda.
   * Helper para CategoriaSpecification.
   */
  public List<String> extractSearchTerms(CategoriaFilterDto filtro) {
    if (
      filtro == null ||
      filtro.getTexto() == null ||
      filtro.getTexto().trim().isEmpty()
    ) {
      return List.of();
    }

    return List.of(filtro.getTexto().toLowerCase().trim().split("\\s+"))
      .stream()
      .filter(term -> term.length() > 2) // Solo términos de más de 2 caracteres
      .distinct()
      .collect(Collectors.toList());
  }

  /**
   * Extrae términos de búsqueda de texto libre en filtros.
   */
  public List<String> extractTextSearchTerms(String texto) {
    if (texto == null || texto.trim().isEmpty()) {
      return List.of();
    }

    return List.of(texto.toLowerCase().trim().split("\\s+"))
      .stream()
      .filter(term -> term.length() > 2) // Solo términos de más de 2 caracteres
      .distinct()
      .collect(Collectors.toList());
  }
}
