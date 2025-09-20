package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.LoginResponseDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginResponseMapper {
  LoginResponseMapper INSTANCE = Mappers.getMapper(LoginResponseMapper.class);

  // El token se debe setear manualmente después
  @Mapping(target = "token", ignore = true)
  LoginResponseDto usuarioDtoToLoginResponseDto(UsuarioDto usuarioDto);
}
