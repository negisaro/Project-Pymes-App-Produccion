package com.project.nelson.msvc_user_auth.usuario.mapper;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.RolDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;
import java.util.List;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolMapper {
  RolDto toDto(Rol rol);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "usuario", ignore = true)
  Rol toEntity(RolDto rolDto);

  List<RolDto> toDtoList(List<Rol> roles);
  List<Rol> toEntityList(List<RolDto> rolesDto);
}
