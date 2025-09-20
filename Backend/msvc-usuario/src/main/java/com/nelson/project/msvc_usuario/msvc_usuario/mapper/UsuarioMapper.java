package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RolMapper.class)
public interface UsuarioMapper {
  UsuarioDto toDto(Usuario usuario);

  List<UsuarioDto> toDtoList(List<Usuario> usuarios);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "resetToken", ignore = true)
  @Mapping(target = "resetTokenExpiry", ignore = true)
  Usuario fromCreateDto(UsuarioCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "resetToken", ignore = true)
  @Mapping(target = "resetTokenExpiry", ignore = true)
  @Mapping(target = "roles", ignore = true)
  void updateEntityFromDto(
    UsuarioCreateDto dto,
    @MappingTarget Usuario usuario
  );
}
