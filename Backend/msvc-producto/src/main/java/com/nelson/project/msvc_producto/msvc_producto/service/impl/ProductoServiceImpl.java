package com.nelson.project.msvc_producto.msvc_producto.service.impl;

import com.nelson.project.msvc_producto.msvc_producto.clientfeign.CategoriaClient;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.ProveedorClient;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_producto.msvc_producto.mapper.ProductoMapper;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.CategoriaDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProveedorDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import com.nelson.project.msvc_producto.msvc_producto.repository.ProductoRepository;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final ProductoMapper productoMapper;
  private final CategoriaClient categoriaClient;
  private final ProveedorClient proveedorClient;

  public ProductoServiceImpl(
    ProductoRepository productoRepository,
    ProductoMapper productoMapper,
    CategoriaClient categoriaClient,
    ProveedorClient proveedorClient,
    UsuarioFeignClient usuarioFeignClient
  ) {
    this.productoRepository = productoRepository;
    this.productoMapper = productoMapper;
    this.categoriaClient = categoriaClient;
    this.proveedorClient = proveedorClient;
  }

  /**
   * Obtiene todos los productos (DTO).
   */
  @Override
  public List<ProductoDto> findAll() {
    logger.info("Obteniendo todos los productos");
    return productoRepository
      .findAll()
      .stream()
      .map(productoMapper::toDto)
      .collect(Collectors.toList());
  }

  /**
   * Obtiene productos paginados (DTO).
   */
  @Override
  public Page<ProductoDto> findAll(Pageable pageable) {
    logger.info("Obteniendo productos paginados: {}", pageable);
    return productoRepository.findAll(pageable).map(productoMapper::toDto);
  }

  /**
   * Busca un producto por su ID (DTO).
   */
  @Override
  public Optional<ProductoDto> findById(Long id) {
    logger.info("Buscando producto por ID: {}", id);
    return productoRepository.findById(id).map(productoMapper::toDto);
  }

  /**
   * Crea un nuevo producto.
   */
  @Override
  public ProductoDto create(ProductoCreateDto productoCreateDto) {
    logger.info("Creando producto: {}", productoCreateDto.getNombre());
    logger.info(
      "Imagenes recibidas en DTO: {}",
      productoCreateDto.getImagenes()
    );
    Producto producto = productoMapper.fromCreateDto(productoCreateDto);
    logger.info("Imagenes mapeadas en entidad: {}", producto.getImagenes());
    Producto saved = productoRepository.save(producto);
    logger.info(
      "Producto guardado con ID: {} e imagenes: {}",
      saved.getId(),
      saved.getImagenes()
    );
    return productoMapper.toDto(saved);
  }

  /**
   * Actualiza un producto existente de forma profesional y segura.
   */
  @Override
  public ProductoDto update(Long id, ProductoCreateDto productoCreateDto) {
    logger.info("Actualizando producto con ID: {}", id);
    Producto producto = productoRepository
      .findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado")
      );
    productoMapper.updateEntityFromDto(productoCreateDto, producto);
    Producto updated = productoRepository.save(producto);
    return productoMapper.toDto(updated);
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
   * Buscar productos por nombre (DTO).
   */
  @Override
  public List<ProductoDto> findByNombreContainingIgnoreCase(String nombre) {
    logger.info("Buscando productos por nombre: {}", nombre);
    return productoRepository
      .findByNombreContainingIgnoreCase(nombre)
      .stream()
      .map(productoMapper::toDto)
      .collect(Collectors.toList());
  }

  /**
   * Buscar productos por estado (DTO, paginado).
   */
  @Override
  public Page<ProductoDto> findByEstado(Boolean estado, Pageable pageable) {
    logger.info("Buscando productos por estado: {}", estado);
    return productoRepository
      .findByEstado(estado, pageable)
      .map(productoMapper::toDto);
  }

  /**
   * Buscar productos por rango de precio (DTO).
   */
  @Override
  public List<ProductoDto> findByPrecioBetween(BigDecimal min, BigDecimal max) {
    logger.info("Buscando productos por rango de precio: {} - {}", min, max);
    return productoRepository
      .findByPrecioBetween(min, max)
      .stream()
      .map(productoMapper::toDto)
      .collect(Collectors.toList());
  }
}
