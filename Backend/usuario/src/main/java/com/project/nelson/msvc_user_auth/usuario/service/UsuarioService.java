package com.project.nelson.msvc_user_auth.usuario.service;

import java.util.List;
import java.util.Optional;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz profesional y escalable para el servicio de usuarios.
 * Define operaciones CRUD y de consulta para entidad y DTO.
 */
public interface UsuarioService {
  Page<Usuario> findAll(Pageable pageable);

  List<Usuario> findAll();

  List<UsuarioDto> findAllDto();

  Optional<Usuario> findById(Long id);

  Optional<UsuarioDto> findDtoById(Long id);

  Usuario save(Usuario usuario);

  UsuarioDto saveDto(UsuarioDto usuarioDto);

  void deleteById(Long id);

  Optional<Usuario> findByUsername(String username);

  Optional<UsuarioDto> findDtoByUsername(String username);

  Optional<Usuario> findByEmail(String email);

  Optional<UsuarioDto> findDtoByEmail(String email);

  boolean existsByUsername(String username);

  Usuario saveWithRoleUser(Usuario usuario); // Añadido para el registro público
}
