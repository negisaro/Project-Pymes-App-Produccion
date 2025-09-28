package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.mapper.UsuarioMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RolRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.service.EmailService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementación profesional y escalable del servicio de usuarios.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioMapper usuarioMapper;
  private final EmailService emailService;

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
  public UsuarioDto save(UsuarioCreateDto usuarioCreateDto) {
    if (
      usuarioCreateDto == null ||
      !StringUtils.hasText(usuarioCreateDto.getUsername()) ||
      !StringUtils.hasText(usuarioCreateDto.getPassword()) ||
      !StringUtils.hasText(usuarioCreateDto.getEmail()) ||
      !StringUtils.hasText(usuarioCreateDto.getName()) ||
      !StringUtils.hasText(usuarioCreateDto.getLastname())
    ) {
      throw new CustomException(
        "Error al actualizar los datos del usuario, verifica los campos obligatorios.",
        400,
        ErrorCodes.USER_UPDATE_FAILED
      );
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
  public UsuarioDto saveWithRoleUser(UsuarioCreateDto usuarioCreateDto) {
    if (
      usuarioCreateDto == null ||
      !StringUtils.hasText(usuarioCreateDto.getUsername()) ||
      !StringUtils.hasText(usuarioCreateDto.getPassword()) ||
      !StringUtils.hasText(usuarioCreateDto.getEmail()) ||
      !StringUtils.hasText(usuarioCreateDto.getName()) ||
      !StringUtils.hasText(usuarioCreateDto.getLastname())
    ) {
      throw new CustomException(
        "Error al registrar el usuario, verifica los campos obligatorios.",
        400,
        ErrorCodes.USER_UPDATE_FAILED
      );
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
  public UsuarioDto update(Long id, UsuarioCreateDto usuarioCreateDto) {
    validarDatosUsuario(usuarioCreateDto);
    Usuario usuario = usuarioRepository
      .findById(id)
      .orElseThrow(() ->
        new CustomException(
          "No se encontró el usuario solicitado.",
          404,
          ErrorCodes.USER_NOT_FOUND
        )
      );

    // Validar si el username está cambiando y ya existe en otro usuario
    if (
      !usuario.getUsername().equals(usuarioCreateDto.getUsername()) &&
      usuarioRepository.existsByUsername(usuarioCreateDto.getUsername())
    ) {
      throw new CustomException(
        "El usuario ya existe con ese nombre de usuario o correo electrónico.",
        409,
        ErrorCodes.USER_ALREADY_EXISTS
      );
    }
    // Validar si el email está cambiando y ya existe en otro usuario
    if (
      !usuario.getEmail().equals(usuarioCreateDto.getEmail()) &&
      usuarioRepository
        .findAll()
        .stream()
        .anyMatch(
          u ->
            u.getEmail().equals(usuarioCreateDto.getEmail()) &&
            !u.getId().equals(id)
        )
    ) {
      throw new CustomException(
        "El email ingresado ya está registrado.",
        409,
        ErrorCodes.EMAIL_ALREADY_REGISTERED
      );
    }

    // Actualizar campos permitidos
    usuario.updateName(usuarioCreateDto.getName());
    usuario.updateLastname(usuarioCreateDto.getLastname());
    usuario.updateUsername(usuarioCreateDto.getUsername());
    usuario.updateEmail(usuarioCreateDto.getEmail());
    // Solo actualizar password si viene un valor no vacío
    if (StringUtils.hasText(usuarioCreateDto.getPassword())) {
      usuario.updatePassword(
        passwordEncoder.encode(usuarioCreateDto.getPassword())
      );
    }

    // Actualizar roles si vienen en el DTO
    if (usuarioCreateDto.getRolesIds() != null) {
      usuario.getRoles().clear();
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
    }

    Usuario updated = usuarioRepository.save(usuario);
    return usuarioMapper.toDto(updated);
  }

  /**
   * Valida los datos críticos de un usuario para creación o actualización.
   * @param usuario datos a validar
   * @throws UsuarioServiceException si algún dato es inválido
   */
  private void validarDatosUsuario(UsuarioCreateDto usuario) {
    if (
      usuario == null ||
      !StringUtils.hasText(usuario.getUsername()) ||
      !StringUtils.hasText(usuario.getEmail()) ||
      !StringUtils.hasText(usuario.getName()) ||
      !StringUtils.hasText(usuario.getLastname())
    ) {
      throw new CustomException(
        "Error al actualizar los datos del usuario, verifica los campos obligatorios.",
        400,
        ErrorCodes.USER_UPDATE_FAILED
      );
    }
    // Email formato básico
    if (!usuario.getEmail().contains("@") || usuario.getEmail().length() < 5) {
      throw new CustomException(
        "El email ingresado ya está registrado.",
        409,
        ErrorCodes.EMAIL_ALREADY_REGISTERED
      );
    }
    // Username mínimo 4 caracteres
    if (usuario.getUsername().length() < 4) {
      throw new CustomException(
        "El usuario ya existe con ese nombre de usuario o correo electrónico.",
        409,
        ErrorCodes.USER_ALREADY_EXISTS
      );
    }
  }

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
  @Override
  @Transactional
  public boolean resetPassword(String token, String newPassword) {
    if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) {
      throw new CustomException(
        "La contraseña no cumple con los requisitos de seguridad.",
        400,
        ErrorCodes.INVALID_PASSWORD
      );
    }
    Usuario usuario = usuarioRepository
      .findByResetToken(token)
      .orElseThrow(() ->
        new CustomException(
          "Token de recuperación inválido.",
          404,
          ErrorCodes.USER_NOT_FOUND
        )
      );
    if (
      usuario.getResetTokenExpiry() == null ||
      usuario.getResetTokenExpiry().isBefore(LocalDateTime.now())
    ) {
      throw new CustomException(
        "El token de recuperación ha expirado.",
        400,
        ErrorCodes.INVALID_PASSWORD
      );
    }
    usuario.updatePassword(passwordEncoder.encode(newPassword));
    usuario.setResetToken(null);
    usuario.setResetTokenExpiry(null);
    usuarioRepository.save(usuario);
    return true;
  }

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

  @Value("${app.reset-password.expiry-minutes:30}")
  private int resetTokenExpiryMinutes;

  public UsuarioServiceImpl(
    UsuarioRepository usuarioRepository,
    RolRepository rolRepository,
    PasswordEncoder passwordEncoder,
    UsuarioMapper usuarioMapper,
    EmailService emailService
  ) {
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
    this.passwordEncoder = passwordEncoder;
    this.usuarioMapper = usuarioMapper;
    this.emailService = emailService;
  }

  @Override
  @Transactional
  public void sendPasswordResetToken(String email) {
    Usuario usuario = usuarioRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new CustomException(
          "No se encontró el usuario con ese email.",
          404,
          ErrorCodes.USER_NOT_FOUND
        )
      );
    String token = UUID.randomUUID().toString();
    usuario.setResetToken(token);
    usuario.setResetTokenExpiry(
      LocalDateTime.now().plusMinutes(resetTokenExpiryMinutes)
    );
    usuarioRepository.save(usuario);
    try {
      emailService.sendPasswordResetToken(
        usuario.getEmail(),
        token,
        resetTokenExpiryMinutes
      );
      log.info(
        "[UsuarioService] Token de recuperación enviado a {}",
        usuario.getEmail()
      );
    } catch (Exception e) {
      log.error(
        "[UsuarioService] Error enviando token de recuperación a {}: {}",
        usuario.getEmail(),
        e.getMessage(),
        e
      );
      throw new CustomException(
        "No se pudo enviar el email de recuperación. Intenta más tarde.",
        500,
        ErrorCodes.EMAIL_SEND_ERROR
      );
    }
  }
}
