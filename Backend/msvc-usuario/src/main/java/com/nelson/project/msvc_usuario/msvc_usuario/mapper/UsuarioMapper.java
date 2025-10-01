package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioUpdateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper unificado de Usuario evitando definiciones duplicadas.
 * Maneja creación, actualización parcial y conversiones a DTO.
 */
@Mapper(
  componentModel = "spring",
  uses = { RolMapper.class },
  unmappedTargetPolicy = ReportingPolicy.IGNORE
  // , builder = @Builder(disableBuilder = true) // <- Descomenta si quieres evitar el builder de Lombok
)
public interface UsuarioMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "active", ignore = true)
  // @Mapping(target = "roles", ignore = true) // Eliminado para evitar error
  Usuario toEntity(UsuarioCreateDto dto);

  UsuarioDto toDto(Usuario entity);

  List<UsuarioDto> toDtoList(List<Usuario> entities);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "active", ignore = true)
  // MapStruct generaba método vacío (probablemente por métodos custom del agregado). Implementamos manualmente.
  default void updateEntityFromDto(
    UsuarioUpdateDto dto,
    @MappingTarget Usuario entity
  ) {
    if (dto == null || entity == null) return;
    if (dto.getName() != null) entity.updateName(dto.getName());
    if (dto.getLastname() != null) entity.updateLastname(dto.getLastname());
    if (dto.getUsername() != null) entity.updateUsername(dto.getUsername());
    if (dto.getEmail() != null) entity.updateEmail(dto.getEmail());
    if (dto.getPassword() != null) entity.updatePassword(dto.getPassword());
    // rolesIds se gestionará en servicio de dominio; aquí no se tocan roles directamente
  }
}
