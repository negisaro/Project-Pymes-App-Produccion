package com.project.nelson.msvc_user_auth.usuario.controller;

import com.project.nelson.msvc_user_auth.usuario.mapper.UsuarioMapper;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Crud de usuarios")
public class UsuarioController {

  private static final Logger logger = LoggerFactory.getLogger(
    UsuarioController.class
  );

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private UsuarioMapper usuarioMapper;

  @Operation(summary = "Obtener todos los usuarios")
  @GetMapping("/list")
  public ResponseEntity<List<UsuarioDto>> getAll() {
    logger.info("Obteniendo todos los usuarios");
    List<UsuarioDto> usuariosDto = usuarioService.findAllDto();
    return ResponseEntity.ok(usuariosDto);
  }

  @Operation(summary = "Obtener usuario por ID")
  @GetMapping("/list/{id}")
  public ResponseEntity<UsuarioDto> getById(@PathVariable Long id) {
    logger.info("Obteniendo usuario por id: {}", id);
    Optional<UsuarioDto> usuarioOpt = usuarioService.findDtoById(id);
    return usuarioOpt
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @Operation(
    summary = "Registrar usuario (registro público, rol USER por defecto)"
  )
  @PostMapping("/register")
  public ResponseEntity<UsuarioDto> register(
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    logger.info(
      "Registrando usuario (registro público): {}",
      usuarioDto.getUsername()
    );
    Usuario usuario = usuarioMapper.toEntity(usuarioDto);
    Usuario usuarioSaved = usuarioService.saveWithRoleUser(usuario);
    UsuarioDto dto = usuarioMapper.toDto(usuarioSaved);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @Operation(summary = "Crear usuario (solo backend/admin)")
  @PostMapping("/create")
  public ResponseEntity<UsuarioDto> create(
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    logger.info(
      "Creando usuario (por backend/admin): {}",
      usuarioDto.getUsername()
    );
    Usuario usuario = usuarioMapper.toEntity(usuarioDto);
    Usuario usuarioSaved = usuarioService.save(usuario);
    UsuarioDto dto = usuarioMapper.toDto(usuarioSaved);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @Operation(summary = "Actualizar usuario")
  @PutMapping("update/{id}")
  public ResponseEntity<UsuarioDto> update(
    @PathVariable Long id,
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    logger.info("Actualizando usuario id: {}", id);
    Optional<Usuario> usuarioOpt = usuarioService.findById(id);
    if (usuarioOpt.isPresent()) {
      Usuario usuarioToUpdate = usuarioMapper.toEntity(usuarioDto);
      usuarioToUpdate.setId(id);
      Usuario usuarioUpdated = usuarioService.save(usuarioToUpdate);
      UsuarioDto dto = usuarioMapper.toDto(usuarioUpdated);
      return ResponseEntity.ok(dto);
    }
    logger.warn("Usuario no encontrado para actualizar: {}", id);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @Operation(summary = "Eliminar usuario")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    logger.info("Eliminando usuario id: {}", id);
    Optional<Usuario> usuarioOpt = usuarioService.findById(id);
    if (usuarioOpt.isPresent()) {
      usuarioService.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    logger.warn("Usuario no encontrado para eliminar: {}", id);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}
