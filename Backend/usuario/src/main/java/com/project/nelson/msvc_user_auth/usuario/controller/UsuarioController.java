package com.project.nelson.msvc_user_auth.usuario.controller;

import com.project.nelson.msvc_user_auth.usuario.mapper.UsuarioMapper;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(
  name = "Usuarios",
  description = "CRUD profesional y escalable de usuarios"
)
public class UsuarioController {

  private static final Logger logger = LoggerFactory.getLogger(
    UsuarioController.class
  );

  private final UsuarioService usuarioService;
  private final UsuarioMapper usuarioMapper;

  @Autowired
  public UsuarioController(
    UsuarioService usuarioService,
    UsuarioMapper usuarioMapper
  ) {
    this.usuarioService = usuarioService;
    this.usuarioMapper = usuarioMapper;
  }

  @Operation(summary = "Listar usuarios paginados")
  @GetMapping("/list")
  public ResponseEntity<Page<UsuarioDto>> list(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "id") String sort
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    Page<Usuario> usuarios = usuarioService.findAll(pageable);
    Page<UsuarioDto> usuariosDto = usuarios.map(usuarioMapper::toDto);
    return ResponseEntity.ok(usuariosDto);
  }

  @Operation(summary = "Obtener usuario por ID")
  @GetMapping("/list/{id}")
  public ResponseEntity<UsuarioDto> getById(@PathVariable Long id) {
    return usuarioService
      .findById(id)
      .map(usuarioMapper::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @Operation(summary = "Registrar usuario público")
  @PostMapping("/register")
  public ResponseEntity<UsuarioDto> register(
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    Usuario usuario = usuarioMapper.toEntity(usuarioDto);
    Usuario usuarioSaved = usuarioService.saveWithRoleUser(usuario);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      usuarioMapper.toDto(usuarioSaved)
    );
  }

  @Operation(summary = "Crear usuario (admin)")
  @PostMapping("/create")
  public ResponseEntity<UsuarioDto> create(
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    Usuario usuario = usuarioMapper.toEntity(usuarioDto);
    Usuario usuarioSaved = usuarioService.save(usuario);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      usuarioMapper.toDto(usuarioSaved)
    );
  }

  @Operation(summary = "Actualizar usuario")
  @PutMapping("/update/{id}")
  public ResponseEntity<UsuarioDto> update(
    @PathVariable Long id,
    @RequestBody @Valid UsuarioDto usuarioDto
  ) {
    return usuarioService
      .findById(id)
      .map(usuario -> {
        Usuario usuarioToUpdate = usuarioMapper.toEntity(usuarioDto);
        usuarioToUpdate.setId(id);
        Usuario usuarioUpdated = usuarioService.save(usuarioToUpdate);
        return ResponseEntity.ok(usuarioMapper.toDto(usuarioUpdated));
      })
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @Operation(summary = "Eliminar usuario")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    return usuarioService
      .findById(id)
      .map(usuario -> {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
      })
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @Operation(summary = "Buscar usuario por username")
  @GetMapping("/{username}")
  public ResponseEntity<UsuarioDto> findByUsername(
    @PathVariable String username
  ) {
    logger.info("Buscando usuario por username: {}", username);
    return usuarioService
      .findByUsername(username)
      .map(usuarioMapper::toDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
}
