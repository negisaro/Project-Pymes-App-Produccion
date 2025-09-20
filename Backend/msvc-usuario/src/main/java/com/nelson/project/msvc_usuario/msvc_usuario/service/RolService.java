package com.nelson.project.msvc_usuario.msvc_usuario.service;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz profesional y escalable para el servicio de roles.
 * Define operaciones CRUD y de consulta para entidad y DTO.
 */
public interface RolService {
  /**
   * Actualiza un rol existente por su ID usando un DTO.
   * @param id identificador del rol
   * @param rolDto datos nuevos del rol
   * @return rol DTO actualizado
   */
  RolDto update(Long id, RolCreateDto rolCreateDto);

  /**
   * Obtiene todos los roles paginados como DTO.
   * @param pageable información de paginación
   * @return página de roles DTO
   */
  Page<RolDto> findAll(Pageable pageable);

  /**
   * Obtiene todos los roles como DTO.
   * @return lista de roles DTO
   */
  List<RolDto> findAll();

  /**
   * Busca un rol por ID (DTO).
   * @param id identificador del rol
   * @return rol DTO encontrado o vacío
   */
  Optional<RolDto> findById(Long id);

  /**
   * Guarda un rol (DTO).
   * @param rolDto rol DTO a guardar
   * @return rol DTO guardado
   */
  RolDto save(RolCreateDto rolCreateDto);

  /**
   * Elimina un rol por ID.
   * @param id identificador del rol
   */
  void deleteById(Long id);

  /**
   * Busca un rol por nombre (DTO).
   * @param name nombre del rol
   * @return rol DTO encontrado o vacío
   */
  Optional<RolDto> findByName(String name);
}
