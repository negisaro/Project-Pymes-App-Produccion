package com.nelson.project.msvc_producto.msvc_producto.mapper;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
  ProductoDto toDto(Producto producto);
  Producto fromCreateDto(ProductoCreateDto dto);
}
