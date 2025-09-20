package com.nelson.project.msvc_usuario.msvc_usuario.controller;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @Operation(summary = "Listar usuarios paginados")
  @GetMapping("/list")
  public ResponseEntity<Page<UsuarioDto>> list(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "id") String sort
  ) {
    try {
      Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
      Page<UsuarioDto> usuariosDto = usuarioService.findAll(pageable);
      return ResponseEntity.ok(usuariosDto);
    } catch (Exception ex) {
      throw new CustomException(
        "Error al listar usuarios",
        500,
        "LIST_USERS_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Obtener usuario por ID")
  @GetMapping("/list/{id}")
  public ResponseEntity<UsuarioDto> getById(@PathVariable Long id) {
    try {
      return usuarioService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() ->
          new CustomException(
            "Usuario no encontrado",
            HttpStatus.NOT_FOUND.value(),
            ErrorCodes.USER_NOT_FOUND
          )
        );
    } catch (Exception ex) {
      throw new CustomException(
        "Error al buscar usuario por ID",
        500,
        "GET_USER_BY_ID_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Registrar usuario público")
  @PostMapping("/register")
  public ResponseEntity<UsuarioDto> register(
    @RequestBody @Valid UsuarioCreateDto usuarioCreateDto
  ) {
    try {
      UsuarioDto usuarioSaved = usuarioService.saveWithRoleUser(
        usuarioCreateDto
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSaved);
    } catch (Exception ex) {
      throw new CustomException(
        "Error al registrar usuario",
        500,
        "REGISTER_USER_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Crear usuario (admin)")
  @PostMapping("/create")
  public ResponseEntity<UsuarioDto> create(
    @RequestBody @Valid UsuarioCreateDto usuarioCreateDto
  ) {
    try {
      UsuarioDto usuarioSaved = usuarioService.save(usuarioCreateDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSaved);
    } catch (Exception ex) {
      throw new CustomException(
        "Error al crear usuario (admin)",
        500,
        "CREATE_USER_ADMIN_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Actualizar usuario")
  @PutMapping("/update/{id}")
  public ResponseEntity<UsuarioDto> update(
    @PathVariable Long id,
    @RequestBody @Valid UsuarioCreateDto usuarioCreateDto
  ) {
    try {
      UsuarioDto usuarioUpdated = usuarioService.update(id, usuarioCreateDto);
      return ResponseEntity.ok(usuarioUpdated);
    } catch (Exception ex) {
      throw new CustomException(
        "Error al actualizar usuario",
        500,
        "UPDATE_USER_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Eliminar usuario")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    try {
      return usuarioService
        .findById(id)
        .map(usuario -> {
          usuarioService.deleteById(id);
          return ResponseEntity.noContent().build();
        })
        .orElseThrow(() ->
          new CustomException(
            "Usuario no encontrado para eliminar",
            HttpStatus.NOT_FOUND.value(),
            ErrorCodes.USER_NOT_FOUND
          )
        );
    } catch (Exception ex) {
      throw new CustomException(
        "Error al eliminar usuario",
        500,
        "DELETE_USER_ERROR",
        ex.getMessage()
      );
    }
  }

  @Operation(summary = "Buscar usuario por username")
  @GetMapping("/{username}")
  public ResponseEntity<UsuarioDto> findByUsername(
    @PathVariable String username
  ) {
    try {
      return usuarioService
        .findByUsername(username)
        .map(ResponseEntity::ok)
        .orElseThrow(() ->
          new CustomException(
            "Usuario no encontrado por username",
            HttpStatus.NOT_FOUND.value(),
            ErrorCodes.USER_NOT_FOUND
          )
        );
    } catch (Exception ex) {
      throw new CustomException(
        "Error al buscar usuario por username",
        500,
        "GET_USER_BY_USERNAME_ERROR",
        ex.getMessage()
      );
    }
  }
}
