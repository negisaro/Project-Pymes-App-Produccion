package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.mapper.UsuarioMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioUpdateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RolRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

/**
 * Implementación profesional y escalable del servicio de usuarios.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
@Slf4j
@Validated
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioMapper usuarioMapper;

  /**
   * Obtiene todos los usuarios como DTO.
   * @return lista de usuarios DTO
   */
  @Override
  @Transactional(readOnly = true)
  public List<UsuarioDto> findAll() {
    // Buscando todos los usuarios (DTO)
    return usuarioRepository
      .findAll()
      .stream()
      .map(usuarioMapper::toDto)
      .collect(Collectors.toList());
  }

  /**
   * Busca un usuario por su ID y lo retorna como DTO. Lanza excepción si no existe.
   * @param id identificador del usuario
   * @return Optional<UsuarioDto> con el usuario encontrado
   * @throws UsuarioServiceException si el usuario no existe
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findById(Long id) {
    return usuarioRepository
      .findById(id)
      .map(usuarioMapper::toDto)
      .or(() -> {
        throw new CustomException(
          "No se encontró el usuario solicitado.",
          404,
          ErrorCodes.USER_NOT_FOUND
        );
      });
  }

  /**
   * Obtiene todos los usuarios paginados como DTO.
   * @param pageable información de paginación
   * @return página de usuarios DTO
   */
  @Override
  @Transactional(readOnly = true)
  public Page<UsuarioDto> findAll(Pageable pageable) {
    // Buscando usuarios paginados (DTO)
    return usuarioRepository.findAll(pageable).map(usuarioMapper::toDto);
  }

  /**
   * Guarda un usuario a partir de un DTO, validando datos críticos y unicidad de username.
   * @param usuarioCreateDto DTO con los datos del usuario
   * @return UsuarioDto guardado
   * @throws UsuarioServiceException si los datos son inválidos o el username ya existe
   */
  @Override
  @Transactional
  public UsuarioDto save(
    @jakarta.validation.Valid UsuarioCreateDto usuarioCreateDto
  ) {
    if (usuarioCreateDto == null) {
      throw new CustomException("DTO nulo", 400, ErrorCodes.USER_UPDATE_FAILED);
    }
    if (usuarioRepository.existsByUsername(usuarioCreateDto.getUsername())) {
      throw new CustomException(
        "El usuario ya existe con ese nombre de usuario o correo electrónico.",
        409,
        ErrorCodes.USER_ALREADY_EXISTS
      );
    }
    // Validar que el admin asigne al menos un rol
    if (
      usuarioCreateDto.getRolesIds() == null ||
      usuarioCreateDto.getRolesIds().isEmpty()
    ) {
      throw new CustomException(
        "Debes asignar al menos un rol al usuario.",
        400,
        ErrorCodes.USER_UPDATE_FAILED
      );
    }
    Usuario usuario = Usuario.builder()
      .name(usuarioCreateDto.getName())
      .lastname(usuarioCreateDto.getLastname())
      .username(usuarioCreateDto.getUsername())
      .email(usuarioCreateDto.getEmail())
      .active(true)
      .password(passwordEncoder.encode(usuarioCreateDto.getPassword()))
      .build();
    // Asignar roles según rolesIds
    for (Long rolId : usuarioCreateDto.getRolesIds()) {
      Rol rol = rolRepository
        .findById(rolId)
        .orElseThrow(() ->
          new CustomException(
            "Rol no encontrado con id: " + rolId,
            404,
            ErrorCodes.USER_UPDATE_FAILED
          )
        );
      usuario.addRol(rol);
    }
    Usuario saved = usuarioRepository.save(usuario);
    return usuarioMapper.toDto(saved);
  }

  /**
   * Guarda un usuario con el rol USER asignado, validando datos críticos y unicidad de username.
   * @param usuarioDto DTO con los datos del usuario
   * @return UsuarioDto guardado con rol USER
   * @throws UsuarioServiceException si los datos son inválidos o el username ya existe
   */
  @Override
  @Transactional
  public UsuarioDto saveWithRoleUser(
    @jakarta.validation.Valid UsuarioCreateDto usuarioCreateDto
  ) {
    if (usuarioCreateDto == null) {
      throw new CustomException("DTO nulo", 400, ErrorCodes.USER_UPDATE_FAILED);
    }
    if (usuarioRepository.existsByUsername(usuarioCreateDto.getUsername())) {
      throw new CustomException(
        "El usuario ya existe con ese nombre de usuario o correo electrónico.",
        409,
        ErrorCodes.USER_ALREADY_EXISTS
      );
    }
    Usuario usuario = Usuario.builder()
      .name(usuarioCreateDto.getName())
      .lastname(usuarioCreateDto.getLastname())
      .username(usuarioCreateDto.getUsername())
      .email(usuarioCreateDto.getEmail())
      .active(true)
      .password(passwordEncoder.encode(usuarioCreateDto.getPassword()))
      .build();
    // Siempre asignar solo el rol CLIENT
    Rol clientRol = rolRepository
      .findByName("ROLE_CLIENT")
      .orElseGet(() -> {
        Rol nuevoRol = Rol.builder().name("ROLE_CLIENT").activo(true).build();
        return rolRepository.save(nuevoRol);
      });
    usuario.addRol(clientRol);
    Usuario saved = usuarioRepository.save(usuario);
    return usuarioMapper.toDto(saved);
  }

  /**
   * Actualiza un usuario existente por su ID usando un DTO de creación.
   * Valida unicidad de username y email, y actualiza solo campos permitidos.
   * @param id identificador del usuario a actualizar
   * @param usuarioCreateDto DTO con los nuevos datos del usuario
   * @return UsuarioDto actualizado
   * @throws UsuarioServiceException si el usuario no existe, los datos son inválidos o el username/email ya existen en otro usuario
   */
  @Override
  @Transactional
  public UsuarioDto update(
    Long id,
    @jakarta.validation.Valid UsuarioUpdateDto usuarioUpdateDto
  ) {
    if (usuarioUpdateDto == null) {
      throw new CustomException("DTO nulo", 400, ErrorCodes.USER_UPDATE_FAILED);
    }
    Usuario usuario = usuarioRepository
      .findById(id)
      .orElseThrow(() ->
        new CustomException(
          "No se encontró el usuario solicitado.",
          404,
          ErrorCodes.USER_NOT_FOUND
        )
      );
    // Username
    if (
      usuarioUpdateDto.getUsername() != null &&
      !usuario.getUsername().equals(usuarioUpdateDto.getUsername()) &&
      usuarioRepository.existsByUsername(usuarioUpdateDto.getUsername())
    ) {
      throw new CustomException(
        "El usuario ya existe con ese nombre de usuario.",
        409,
        ErrorCodes.USER_ALREADY_EXISTS
      );
    }
    // Email
    if (
      usuarioUpdateDto.getEmail() != null &&
      !usuario.getEmail().equals(usuarioUpdateDto.getEmail()) &&
      usuarioRepository.existsByEmailAndIdNot(usuarioUpdateDto.getEmail(), id)
    ) {
      throw new CustomException(
        "El email ingresado ya está registrado.",
        409,
        ErrorCodes.EMAIL_ALREADY_REGISTERED
      );
    }

    // Campos simples usando métodos de dominio
    if (usuarioUpdateDto.getName() != null) {
      usuario.updateName(usuarioUpdateDto.getName());
    }
    if (usuarioUpdateDto.getLastname() != null) {
      usuario.updateLastname(usuarioUpdateDto.getLastname());
    }
    if (usuarioUpdateDto.getUsername() != null) {
      usuario.updateUsername(usuarioUpdateDto.getUsername());
    }
    if (usuarioUpdateDto.getEmail() != null) {
      usuario.updateEmail(usuarioUpdateDto.getEmail());
    }
    if (StringUtils.hasText(usuarioUpdateDto.getPassword())) {
      usuario.updatePassword(
        passwordEncoder.encode(usuarioUpdateDto.getPassword())
      );
    }
    if (usuarioUpdateDto.getRolesIds() != null) {
      usuario.getRoles().clear();
      for (Long rolId : usuarioUpdateDto.getRolesIds()) {
        Rol rol = rolRepository
          .findById(rolId)
          .orElseThrow(() ->
            new CustomException(
              "Rol no encontrado con id: " + rolId,
              404,
              ErrorCodes.USER_UPDATE_FAILED
            )
          );
        usuario.addRol(rol);
      }
    }
    return usuarioMapper.toDto(usuarioRepository.save(usuario));
  }

  /**
   * Valida los datos críticos de un usuario para creación o actualización.
   * @param usuario datos a validar
   * @throws UsuarioServiceException si algún dato es inválido
   */
  // Método de validación manual eliminado: se delega a Bean Validation (@Valid) y reglas específicas (unicidad, roles requeridos).

  /**
   * Elimina un usuario por su ID. Lanza excepción si no existe.
   * @param id identificador del usuario
   * @throws UsuarioServiceException si el usuario no existe
   */
  @Override
  @Transactional
  public void deleteById(Long id) {
    try {
      usuarioRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new CustomException(
        "No se encontró el usuario solicitado.",
        404,
        ErrorCodes.USER_NOT_FOUND
      );
    }
  }

  /**
   * Restablece la contraseña de un usuario usando un token y la nueva contraseña.
   * Lanza excepción si el token o la contraseña son vacíos o el usuario no existe.
   * @param token Token de recuperación (usualmente username)
   * @param newPassword Nueva contraseña
   * @return true si la contraseña fue cambiada correctamente
   * @throws UsuarioServiceException si el token es inválido o datos incompletos
   */

  /**
   * Busca un usuario por su username y lo retorna como DTO. Lanza excepción si no existe.
   * @param username nombre de usuario
   * @return Optional<UsuarioDto> con el usuario encontrado
   * @throws UsuarioServiceException si el usuario no existe
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findByUsername(String username) {
    return usuarioRepository
      .findByUsername(username)
      .map(usuarioMapper::toDto)
      .or(() -> {
        throw new CustomException(
          "No se encontró el usuario solicitado.",
          404,
          ErrorCodes.USER_NOT_FOUND
        );
      });
  }

  /**
   * Busca un usuario por su email y lo retorna como DTO. Lanza excepción si no existe.
   * @param email email del usuario
   * @return Optional<UsuarioDto> con el usuario encontrado
   * @throws UsuarioServiceException si el usuario no existe
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<UsuarioDto> findByEmail(String email) {
    return usuarioRepository
      .findByEmail(email)
      .map(usuarioMapper::toDto)
      .or(() -> {
        throw new CustomException(
          "No se encontró el usuario solicitado.",
          404,
          ErrorCodes.USER_NOT_FOUND
        );
      });
  }

  /**
   * Verifica si existe un usuario por su username.
   * @param username nombre de usuario
   * @return true si existe, false si no
   */
  @Override
  @Transactional(readOnly = true)
  public boolean existsByUsername(String username) {
    // Verificando existencia de usuario por username
    return usuarioRepository.existsByUsername(username);
  }

  public UsuarioServiceImpl(
    UsuarioRepository usuarioRepository,
    RolRepository rolRepository,
    PasswordEncoder passwordEncoder,
    UsuarioMapper usuarioMapper
  ) {
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
    this.passwordEncoder = passwordEncoder;
    this.usuarioMapper = usuarioMapper;
  }
  // Métodos legacy de recuperación de contraseña eliminados. Ahora se usa PasswordRecoveryService.
}
