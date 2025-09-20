package com.nelson.project.msvc_producto.msvc_producto.service;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio profesional y escalable para gestión de productos.
 */
public interface ProductoService {
  /**
   * Lista todos los productos (DTO).
   */
  List<ProductoDto> findAll();

  /**
   * Lista productos con paginación (DTO).
   */
  Page<ProductoDto> findAll(Pageable pageable);

  /**
   * Busca producto por ID (DTO).
   */
  Optional<ProductoDto> findById(Long id);

  /**
   * Crea un nuevo producto.
   */
  ProductoDto create(ProductoCreateDto productoCreateDto);

  /**
   * Actualiza un producto existente por su ID.
   * @param id identificador del producto a actualizar
   * @param productoCreateDto datos nuevos del producto
   * @return ProductoDto actualizado
   */
  ProductoDto update(Long id, ProductoCreateDto productoCreateDto);

  /**
   * Elimina producto por ID.
   */
  void deleteById(Long id);

  /**
   * Buscar productos por nombre (contiene, ignorando mayúsculas/minúsculas).
   */
  List<ProductoDto> findByNombreContainingIgnoreCase(String nombre);

  /**
   * Buscar productos por estado (activo/inactivo) con paginación.
   */
  Page<ProductoDto> findByEstado(Boolean estado, Pageable pageable);

  /**
   * Buscar productos por rango de precio.
   */
  List<ProductoDto> findByPrecioBetween(BigDecimal min, BigDecimal max);
}
