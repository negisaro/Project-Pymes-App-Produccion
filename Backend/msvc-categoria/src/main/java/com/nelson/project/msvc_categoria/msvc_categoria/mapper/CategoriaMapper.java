package com.nelson.project.msvc_categoria.msvc_categoria.mapper;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;

public final class CategoriaMapper {

  private CategoriaMapper() {}

  /**
   * Convierte una entidad Categoria a un DTO.
   */
  public static CategoriaDTO toDto(Categoria categoria) {
    if (categoria == null) return null;
    CategoriaDTO dto = new CategoriaDTO();
    dto.setId(categoria.getId());
    dto.setNombre(categoria.getNombre());
    dto.setDescripcion(categoria.getDescripcion());
    dto.setEstado(Boolean.TRUE.equals(categoria.getEstado()));
    dto.setCreadoEn(categoria.getCreadoEn());
    dto.setActualizadoEn(categoria.getActualizadoEn());
    return dto;
  }

  /**
   * Convierte un DTO de creación a una entidad Categoria.
   */
  public static Categoria fromCreateDto(CategoriaCreateDto dto) {
    if (dto == null) return null;
    Categoria categoria = new Categoria();
    categoria.setNombre(dto.getNombre());
    categoria.setDescripcion(dto.getDescripcion());
    categoria.setEstado(dto.isEstado());
    return categoria;
  }

  /**
   * Actualiza una entidad Categoria existente con los datos del DTO.
   */
  public static void updateEntityFromDto(
    Categoria categoria,
    CategoriaCreateDto dto
  ) {
    if (categoria == null || dto == null) return;
    categoria.setNombre(dto.getNombre());
    categoria.setDescripcion(dto.getDescripcion());
    categoria.setEstado(dto.isEstado());
  }
}
