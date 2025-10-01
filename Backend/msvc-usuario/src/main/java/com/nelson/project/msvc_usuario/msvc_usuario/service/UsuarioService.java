package com.nelson.project.msvc_usuario.msvc_usuario.service;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioUpdateDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz profesional y escalable para el servicio de usuarios.
 * Define operaciones CRUD y de consulta para entidad y DTO.
 */
public interface UsuarioService {
  /**
   * Obtiene todos los usuarios como DTO.
   * @return lista de usuarios DTO
   */
  List<UsuarioDto> findAll();

  /**
   * Busca un usuario por ID (DTO).
   * @param id identificador del usuario
   * @return usuario DTO encontrado o vacío
   */
  Optional<UsuarioDto> findById(Long id);

  /**
   * Obtiene todos los usuarios paginados como DTO.
   * @param pageable información de paginación
   * @return página de usuarios DTO
   */
  Page<UsuarioDto> findAll(Pageable pageable);

  /**
   * Guarda un usuario (DTO).
   * @param usuarioDto usuario DTO a guardar
   * @return usuario DTO guardado
   */
  UsuarioDto save(@Valid UsuarioCreateDto usuarioCreateDto);

  /**
   * Actualiza un usuario (DTO).
   * @param usuarioCreateDto usuario DTO a actualizar
   * @return usuario DTO actualizado
   */
  UsuarioDto update(Long id, @Valid UsuarioUpdateDto usuarioUpdateDto);

  /**
   * Elimina un usuario por ID.
   * @param id identificador del usuario
   */
  void deleteById(Long id);

  /**
   * Busca un usuario por nombre de usuario (DTO).
   * @param username nombre de usuario
   * @return usuario DTO encontrado o vacío
   */
  Optional<UsuarioDto> findByUsername(String username);

  /**
   * Busca un usuario por email (DTO).
   * @param email email del usuario
   * @return usuario DTO encontrado o vacío
   */
  Optional<UsuarioDto> findByEmail(String email);

  /**
   * Verifica si existe un usuario por nombre de usuario.
   * @param username nombre de usuario
   * @return true si existe, false si no
   */
  boolean existsByUsername(String username);

  /**
   * Guarda un usuario y le asigna el rol USER (registro público).
   * @param usuarioDto usuario DTO a guardar
   * @return usuario DTO guardado
   */
  /**
   * Guarda un usuario y le asigna el rol CLIENT (registro público).
   * @param usuarioCreateDto usuario DTO a guardar
   * @return usuario DTO guardado
   */
  UsuarioDto saveWithRoleUser(@Valid UsuarioCreateDto usuarioCreateDto);
}
