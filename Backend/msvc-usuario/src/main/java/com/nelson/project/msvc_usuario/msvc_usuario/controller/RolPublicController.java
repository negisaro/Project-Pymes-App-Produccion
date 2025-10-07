package com.nelson.project.msvc_usuario.msvc_usuario.controller;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolDto;
import com.nelson.project.msvc_usuario.msvc_usuario.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/roles")
@Tag(name = "Roles", description = "Endpoints para gestión de roles")
public class RolPublicController {

  @Autowired
  private RolService rolService;

  @Operation(
    summary = "Listar todos los roles",
    description = "Devuelve la lista de roles"
  )
  @GetMapping("/list")
  public ResponseEntity<List<RolDto>> getAllRoles() {
    return ResponseEntity.ok(rolService.findAll());
  }

  @Operation(
    summary = "Obtener rol por ID",
    description = "Devuelve un rol específico por su ID"
  )
  @GetMapping("/list/{id}")
  public ResponseEntity<RolDto> getRolById(@PathVariable Long id) {
    return rolService
      .findById(id)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
