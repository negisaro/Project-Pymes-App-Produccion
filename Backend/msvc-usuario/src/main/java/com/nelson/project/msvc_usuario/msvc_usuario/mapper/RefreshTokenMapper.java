package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RefreshTokenDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
  RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

  @Mapping(source = "id", target = "token")
  RefreshTokenDto toDto(RefreshToken entity);

  @Mapping(source = "token", target = "id")
  RefreshToken toEntity(RefreshTokenDto dto);
}
