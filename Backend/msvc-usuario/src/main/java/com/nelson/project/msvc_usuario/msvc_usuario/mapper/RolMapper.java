package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RolMapper {
  RolDto toDto(Rol rol);

  List<RolDto> toDtoList(List<Rol> roles);

  Rol fromDto(RolDto dto);

  @Mapping(target = "id", ignore = true)
  Rol fromCreateDto(RolCreateDto dto);

  @Mappings(
    {
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "usuarios", ignore = true),
    }
  )
  void updateEntityFromDto(RolCreateDto dto, @MappingTarget Rol rol);
}
