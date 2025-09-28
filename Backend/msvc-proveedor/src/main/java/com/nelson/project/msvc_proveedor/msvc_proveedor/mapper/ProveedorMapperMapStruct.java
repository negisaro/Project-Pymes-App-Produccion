package com.nelson.project.msvc_proveedor.msvc_proveedor.mapper;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProveedorMapperMapStruct {
  ProveedorDTO toDto(Proveedor proveedor);

  @Mapping(target = "id", ignore = true)
  Proveedor fromCreateDto(ProveedorCreateDTO dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(
    ProveedorCreateDTO dto,
    @MappingTarget Proveedor proveedor
  );
}
