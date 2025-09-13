package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;

public final class CategoriaMapper {

  private CategoriaMapper() {}

  public static CategoriaDTO toDto(Categoria categoria) {
    CategoriaDTO dto = new CategoriaDTO();
    dto.setId(categoria.getId());
    dto.setNombre(categoria.getNombre());
    dto.setDescripcion(categoria.getDescripcion());
    dto.setEstado(categoria.isEstado());
    dto.setCreadoEn(categoria.getCreadoEn());
    dto.setActualizadoEn(categoria.getActualizadoEn());
    return dto;
  }

  public static Categoria fromCreateDto(CategoriaCreateDto categoriaCreateDto) {
    Categoria entity = new Categoria();
    entity.setId(categoriaCreateDto.getId());
    entity.setNombre(categoriaCreateDto.getNombre());
    entity.setDescripcion(categoriaCreateDto.getDescripcion());
    entity.setEstado(categoriaCreateDto.isEstado());
    entity.setProductosId(categoriaCreateDto.getProductosId());
    return entity;
  }
}
