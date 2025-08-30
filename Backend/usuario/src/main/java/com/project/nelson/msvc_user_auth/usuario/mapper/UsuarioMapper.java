package com.project.nelson.msvc_user_auth.usuario.mapper;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import org.mapstruct.Mapper;


import java.util.List;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mappings({
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "roles", target = "roles")
    })
    UsuarioDto toDto(Usuario usuario);

    @Mappings({
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "roles", target = "roles")
    })
    Usuario toEntity(UsuarioDto usuarioDto);

    List<UsuarioDto> toDtoList(List<Usuario> usuarios);
    List<Usuario> toEntityList(List<UsuarioDto> usuariosDto);
}
