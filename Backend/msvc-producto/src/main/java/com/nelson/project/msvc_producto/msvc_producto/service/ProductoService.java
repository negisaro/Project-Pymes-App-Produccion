package com.nelson.project.msvc_producto.msvc_producto.service;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestión de productos.
 */
public interface ProductoService {
  /**
   * Lista todos los productos.
   */
  List<Producto> findAll();

  /**
   * Lista productos con paginación.
   */
  Page<Producto> findAll(Pageable pageable);

  /**
   * Busca producto por ID.
   */
  Optional<Producto> findById(Long id);

  /**
   * Guarda o actualiza producto.
   */
  Producto save(Producto producto);

  /**
   * Elimina producto por ID.
   */
  void deleteById(Long id);

  Optional<UsuarioDto> findByUsername(String username);

  /**
   * Buscar productos por nombre (contiene, ignorando mayúsculas/minúsculas).
   */
  List<Producto> findByNombreContainingIgnoreCase(String nombre);

  /**
   * Buscar productos por estado (activo/inactivo) con paginación.
   */
  Page<Producto> findByEstado(Boolean estado, Pageable pageable);

  /**
   * Buscar productos por rango de precio.
   */
  List<Producto> findByPrecioBetween(
    java.math.BigDecimal min,
    java.math.BigDecimal max
  );
}
