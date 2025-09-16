package com.nelson.project.msvc_producto.msvc_producto.service.impl;

import com.nelson.project.msvc_producto.msvc_producto.clientfeign.CategoriaClient;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.ProveedorClient;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.CategoriaDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProveedorDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import com.nelson.project.msvc_producto.msvc_producto.repository.ProductoRepository;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

  private static final Logger logger = LoggerFactory.getLogger(
    ProductoServiceImpl.class
  );

  private final ProductoRepository productoRepository;
  private final CategoriaClient categoriaClient;
  private final ProveedorClient proveedorClient;
  private final UsuarioFeignClient usuarioFeignClient;

  public ProductoServiceImpl(
    ProductoRepository productoRepository,
    CategoriaClient categoriaClient,
    ProveedorClient proveedorClient,
    UsuarioFeignClient usuarioFeignClient
  ) {
    this.productoRepository = productoRepository;
    this.categoriaClient = categoriaClient;
    this.proveedorClient = proveedorClient;
    this.usuarioFeignClient = usuarioFeignClient;
  }

  /**
   * Obtiene todos los productos.
   */
  @Override
  public List<Producto> findAll() {
    logger.info("Obteniendo todos los productos");
    return productoRepository.findAll();
  }

  /**
   * Obtiene productos paginados.
   */
  @Override
  public Page<Producto> findAll(Pageable pageable) {
    logger.info("Obteniendo productos paginados: {}", pageable);
    return productoRepository.findAll(pageable);
  }

  /**
   * Busca un producto por su ID.
   */
  @Override
  public Optional<Producto> findById(Long id) {
    logger.info("Buscando producto por ID: {}", id);
    return productoRepository.findById(id);
  }

  /**
   * Guarda o actualiza un producto.
   */
  @Override
  public Producto save(Producto producto) {
    logger.info("Guardando producto: {}", producto.getNombre());
    return productoRepository.save(producto);
  }

  /**
   * Elimina un producto por su ID.
   */
  @Override
  public void deleteById(Long id) {
    logger.info("Eliminando producto por ID: {}", id);
    productoRepository.deleteById(id);
  }

  /**
   * Obtiene la categoría asociada a un producto desde el microservicio de categorías.
   */
  protected CategoriaDTO getCategoriaDeProducto(Long categoriaId) {
    logger.debug("Consultando categoría para producto, id: {}", categoriaId);
    try {
      return categoriaClient.getCategoriaById(categoriaId);
    } catch (Exception e) {
      logger.error("Error consultando categoría: {}", categoriaId, e);
      return null;
    }
  }

  /**
   * Obtiene el proveedor asociado a un producto desde el microservicio de proveedores.
   */
  protected ProveedorDTO getProveedorDeProducto(Long proveedorId) {
    logger.debug("Consultando proveedor para producto, id: {}", proveedorId);
    try {
      return proveedorClient.getProveedorById(proveedorId);
    } catch (Exception e) {
      logger.error("Error consultando proveedor: {}", proveedorId, e);
      return null;
    }
  }

  /**
   * Busca usuario por username usando el microservicio de usuarios.
   */
  @Override
  public Optional<UsuarioDto> findByUsername(String username) {
    logger.debug("Buscando usuario por username: {}", username);
    try {
      UsuarioDto usuario = usuarioFeignClient.findByUsername(username);
      return Optional.ofNullable(usuario);
    } catch (Exception e) {
      logger.error("Error consultando usuario: {}", username, e);
      return Optional.empty();
    }
  }

  /**
   * Buscar productos por nombre (contiene, ignorando mayúsculas/minúsculas).
   */
  @Override
  public List<Producto> findByNombreContainingIgnoreCase(String nombre) {
    logger.info("Buscando productos por nombre: {}", nombre);
    return productoRepository.findByNombreContainingIgnoreCase(nombre);
  }

  /**
   * Buscar productos por estado (activo/inactivo) con paginación.
   */
  @Override
  public Page<Producto> findByEstado(Boolean estado, Pageable pageable) {
    logger.info("Buscando productos por estado: {}", estado);
    return productoRepository.findByEstado(estado, pageable);
  }

  /**
   * Buscar productos por rango de precio.
   */
  @Override
  public List<Producto> findByPrecioBetween(
    java.math.BigDecimal min,
    java.math.BigDecimal max
  ) {
    logger.info("Buscando productos por rango de precio: {} - {}", min, max);
    return productoRepository.findByPrecioBetween(min, max);
  }
}
