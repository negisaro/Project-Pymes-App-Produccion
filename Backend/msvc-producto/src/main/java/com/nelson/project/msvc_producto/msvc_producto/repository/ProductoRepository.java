package com.nelson.project.msvc_producto.msvc_producto.repository;

import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
  /**
   * Buscar productos por nombre (contiene, ignorando mayúsculas/minúsculas).
   * @param nombre parte del nombre a buscar
   * @return lista de productos que contienen el nombre
   */
  List<Producto> findByNombreContainingIgnoreCase(String nombre);

  /**
   * Buscar productos por estado (activo/inactivo) con paginación.
   * @param estado true para activos, false para inactivos
   * @param pageable paginación
   * @return página de productos filtrados por estado
   */
  Page<Producto> findByEstado(Boolean estado, Pageable pageable);

  /**
   * Buscar productos por rango de precio.
   * @param min precio mínimo
   * @param max precio máximo
   * @return lista de productos en el rango de precio
   */
  List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
}
