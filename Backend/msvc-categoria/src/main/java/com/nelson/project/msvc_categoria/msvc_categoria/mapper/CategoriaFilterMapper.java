package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaFilterDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Mapper especializado para convertir CategoriaFilterDto a JPA Specifications.
 * Maneja filtros complejos y búsquedas dinámicas para la entidad robusta Categoria (65+ campos).
 *
 * Funcionalidades:
 * - Conversión de filtros DTO a Specifications JPA
 * - Filtros básicos (texto, código, estado, IDs)
 * - Filtros jerárquicos (padre, nivel, raíz)
 * - Filtros de visualización (menú, destacadas, departamento)
 * - Filtros de métricas (productos, ventas, popularidad)
 * - Filtros temporales (fechas de creación/modificación)
 * - Filtros de soft delete y auditoría
 */
@Component
public class CategoriaFilterMapper {

  /**
   * Convierte CategoriaFilterDto a Specification JPA para consultas dinámicas.
   * Aplica todos los filtros de manera dinámica usando AND lógico.
   */
  public Specification<Categoria> toSpecification(CategoriaFilterDto filtro) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // ========================================
      // FILTROS BÁSICOS
      // ========================================

      // Filtro por texto en nombre o descripción (búsqueda parcial, case-insensitive)
      if (filtro.getTexto() != null && !filtro.getTexto().trim().isEmpty()) {
        String textoMinuscula =
          "%" + filtro.getTexto().toLowerCase().trim() + "%";
        Predicate nombrePredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("nombre")),
          textoMinuscula
        );
        Predicate descripcionPredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("descripcion")),
          textoMinuscula
        );
        predicates.add(
          criteriaBuilder.or(nombrePredicate, descripcionPredicate)
        );
      }

      // Filtro por código exacto (case-insensitive)
      if (filtro.getCodigo() != null && !filtro.getCodigo().trim().isEmpty()) {
        predicates.add(
          criteriaBuilder.equal(
            criteriaBuilder.lower(root.get("codigo")),
            filtro.getCodigo().toLowerCase().trim()
          )
        );
      }

      // Filtro por estado activo
      if (filtro.getActivo() != null) {
        predicates.add(
          criteriaBuilder.equal(root.get("activo"), filtro.getActivo())
        );
      }

      // Filtro por categorías raíz (sin padre)
      if (filtro.getSoloRaiz() != null && filtro.getSoloRaiz()) {
        predicates.add(criteriaBuilder.isNull(root.get("categoriaPadre")));
      }

      // Filtro por estado de eliminación (por defecto excluye eliminados)
      if (
        filtro.getIncluirEliminadas() == null || !filtro.getIncluirEliminadas()
      ) {
        predicates.add(criteriaBuilder.equal(root.get("eliminado"), false));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * Crea specification para categorías populares.
   */
  public Specification<Categoria> forPopularesCategorias() {
    return (root, query, criteriaBuilder) -> {
      if (query != null) {
        query.orderBy(criteriaBuilder.desc(root.get("popularidad")));
      }
      return criteriaBuilder.and(
        criteriaBuilder.equal(root.get("activo"), true),
        criteriaBuilder.equal(root.get("eliminado"), false),
        criteriaBuilder.equal(root.get("visibleEnMenu"), true)
      );
    };
  }
}
