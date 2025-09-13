package com.nelson.project.msvc_producto.msvc_producto.mapper;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;

public final class ProductoMapper {

  private ProductoMapper() {
    // Utility class, no instanciar
  }

  public static ProductoDto toDto(Producto producto) {
    ProductoDto dto = new ProductoDto();
    dto.setId(producto.getId());
    dto.setNombre(producto.getNombre());
    dto.setDescripcion(producto.getDescripcion());
    dto.setPrecio(producto.getPrecio());
    dto.setStock(producto.getStock());
    dto.setFechaCreacion(producto.getFechaCreacion());
    dto.setFechaActualizacion(producto.getFechaActualizacion());
    dto.setCategoriaId(producto.getCategoriaId());
    dto.setProveedorId(producto.getProveedorId());
    dto.setImagenes(producto.getImagenes());
    dto.setEstado(producto.getEstado());
    return dto;
  }

  public static Producto fromCreateDto(ProductoCreateDto dto) {
    Producto entity = new Producto();
    entity.setNombre(dto.getNombre());
    entity.setDescripcion(dto.getDescripcion());
    entity.setPrecio(dto.getPrecio());
    entity.setStock(dto.getStock());
    entity.setCategoriaId(dto.getCategoriaId());
    entity.setProveedorId(dto.getProveedorId());
    entity.setImagenes(dto.getImagenes());
    entity.setEstado(dto.getEstado());
    return entity;
  }
}
