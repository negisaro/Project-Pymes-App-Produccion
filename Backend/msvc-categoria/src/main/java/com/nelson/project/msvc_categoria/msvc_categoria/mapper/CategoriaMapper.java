package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
  CategoriaDTO toDto(Categoria categoria);

  @Mapping(target = "id", ignore = true)
  Categoria fromCreateDto(CategoriaCreateDto dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(
    CategoriaCreateDto dto,
    @MappingTarget Categoria categoria
  );
}
