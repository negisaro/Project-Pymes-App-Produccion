package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RefreshTokenDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RefreshTokenMapper {
  @Mapping(target = "token", source = "token")
  @Mapping(target = "username", source = "username")
  @Mapping(target = "expiryDate", source = "expiryDate")
  RefreshTokenDto toDto(RefreshToken entity);

  // Al reconstruir entidad desde DTO (caso poco común), los campos no presentes se dejan por defecto.
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "revoked", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  RefreshToken toEntity(RefreshTokenDto dto);
}
