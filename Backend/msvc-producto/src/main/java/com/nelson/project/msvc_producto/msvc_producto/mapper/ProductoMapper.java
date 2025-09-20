package com.nelson.project.msvc_producto.msvc_producto.mapper;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
  ProductoDto toDto(Producto producto);

  @Mapping(target = "id", ignore = true)
  Producto fromCreateDto(ProductoCreateDto dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(
    ProductoCreateDto dto,
    @MappingTarget Producto producto
  );
}
