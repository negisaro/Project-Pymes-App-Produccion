package com.project.nelson.msvc_user_auth.usuario.service;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.RolDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz profesional y escalable para el servicio de roles.
 * Define operaciones CRUD y de consulta para entidad y DTO.
 */
public interface RolService {
  List<Rol> findAll();

  List<RolDto> findAllDto();

  Optional<Rol> findById(Long id);

  Optional<RolDto> findDtoById(Long id);

  Rol save(Rol rol);

  RolDto saveDto(RolDto rolDto);

  void deleteById(Long id);

  Optional<Rol> findByName(String name);

  Optional<RolDto> findDtoByName(String name);
}
