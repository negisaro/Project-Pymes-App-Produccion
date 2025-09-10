package com.project.nelson.msvc_user_auth.usuario.service.impl;

import com.project.nelson.msvc_user_auth.usuario.mapper.UsuarioMapper;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.repository.RolRepository;
import com.project.nelson.msvc_user_auth.usuario.repository.UsuarioRepository;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación profesional y escalable del servicio de usuarios.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

  @Override
  @Transactional
  public boolean resetPassword(String token, String newPassword) {
    // Simulación: buscar usuario por token (en la vida real, deberías tener una entidad para el token)
    // Aquí solo se busca por username = token para ejemplo
    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(token);
    if (usuarioOpt.isPresent()) {
      Usuario usuario = usuarioOpt.get();
      usuario.setPassword(passwordEncoder.encode(newPassword));
      usuarioRepository.save(usuario);
      logger.info(
        "Contraseña restablecida para usuario: {}",
        usuario.getUsername()
      );
      return true;
    }
    logger.warn(
      "No se pudo restablecer la contraseña. Token inválido: {}",
      token
    );
    return false;
  }

  private static final Logger logger = LoggerFactory.getLogger(
    UsuarioServiceImpl.class
  );

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private RolRepository rolRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsuarioMapper usuarioMapper;

  @PostConstruct
  public void initAdminUser() {
    if (usuarioRepository.countByRolName("ROLE_ADMIN") == 0) {
      logger.info("No existe usuario admin, creando uno por defecto...");
      Rol adminRol = rolRepository
        .findByName("ROLE_ADMIN")
        .orElseGet(() -> rolRepository.save(new Rol("ROLE_ADMIN")));

      Usuario admin = new Usuario();
      admin.setName("Administrador");
      admin.setLastname("Principal");
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setEmail("admin@correo.com");
      admin.setActive(true);
      admin.setRoles(List.of(adminRol));
      usuarioRepository.save(admin);
      logger.info("Usuario admin creado por inicialización.");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Usuario> findAll(Pageable pageable) {
    logger.info("Buscando usuarios paginados: {}", pageable);
    return usuarioRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> findAll() {
    logger.info("Buscando todos los usuarios");
    return usuarioRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UsuarioDto> findAllDto() {
    logger.info("Buscando todos los usuarios (DTO)");
    return usuarioRepository
      .findAll()
      .stream()
      .map(usuarioMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> findById(Long id) {
    logger.info("Buscando usuario por id: {}", id);
    return usuarioRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findDtoById(Long id) {
    logger.info("Buscando usuario (DTO) por id: {}", id);
    return usuarioRepository.findById(id).map(usuarioMapper::toDto);
  }

  @Override
  @Transactional
  public Usuario save(Usuario usuario) {
    logger.info("Guardando usuario: {}", usuario.getUsername());
    if (usuario.getPassword() != null) {
      if (
        usuario.getId() == null || !usuario.getPassword().startsWith("$2a$")
      ) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
      }
    }
    return usuarioRepository.save(usuario);
  }

  @Override
  @Transactional
  public Usuario saveWithRoleUser(Usuario usuario) {
    Rol userRol = rolRepository
      .findByName("ROLE_USER")
      .orElseGet(() -> rolRepository.save(new Rol("ROLE_USER")));
    usuario.setRoles(List.of(userRol));
    return save(usuario);
  }

  @Override
  @Transactional
  public UsuarioDto saveDto(UsuarioDto usuarioDto) {
    logger.info("Guardando usuario (DTO): {}", usuarioDto.getUsername());
    Usuario usuario = usuarioMapper.toEntity(usuarioDto);
    Usuario saved = save(usuario);
    return usuarioMapper.toDto(saved);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    logger.warn("Eliminando usuario por id: {}", id);
    usuarioRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> findByUsername(String username) {
    logger.info("Buscando usuario por username: {}", username);
    return usuarioRepository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findDtoByUsername(String username) {
    logger.info("Buscando usuario (DTO) por username: {}", username);
    return usuarioRepository.findByUsername(username).map(usuarioMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> findByEmail(String email) {
    logger.info("Buscando usuario por email: {}", email);
    return usuarioRepository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findDtoByEmail(String email) {
    logger.info("Buscando usuario (DTO) por email: {}", email);
    return usuarioRepository.findByEmail(email).map(usuarioMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByUsername(String username) {
    logger.info("Verificando existencia de usuario por username: {}", username);
    return usuarioRepository.existsByUsername(username);
  }
}
