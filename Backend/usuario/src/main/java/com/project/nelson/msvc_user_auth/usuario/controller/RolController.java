package com.project.nelson.msvc_user_auth.usuario.controller;

import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.RolDto;
import com.project.nelson.msvc_user_auth.usuario.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Endpoints para gestión de roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(summary = "Listar todos los roles", description = "Devuelve la lista de roles")
    @GetMapping
    public ResponseEntity<List<RolDto>> getAllRoles() {
        List<RolDto> roles = rolService.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener rol por ID", description = "Devuelve un rol específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<RolDto> getRolById(@PathVariable Long id) {
        Optional<Rol> rol = rolService.findById(id);
        return rol.map(r -> ResponseEntity.ok(toDto(r)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo rol", description = "Crea un rol")
    @PostMapping
    public ResponseEntity<RolDto> createRol(@RequestBody RolDto rolDto) {
        Rol rol = toEntity(rolDto);
        Rol saved = rolService.save(rol);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente")
    @PutMapping("/{id}")
    public ResponseEntity<RolDto> updateRol(@PathVariable Long id, @RequestBody RolDto rolDto) {
        Optional<Rol> existing = rolService.findById(id);
        if (existing.isPresent()) {
            Rol rol = toEntity(rolDto);
            rol.setId(id);
            Rol updated = rolService.save(rol);
            return ResponseEntity.ok(toDto(updated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        Optional<Rol> existing = rolService.findById(id);
        if (existing.isPresent()) {
            rolService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Convertir entidad a DTO
    private RolDto toDto(Rol rol) {
        return new RolDto(rol.getId(), rol.getName(), rol.isActivo());
    }

    // Convertir DTO a entidad
    private Rol toEntity(RolDto dto) {
        Rol rol = new Rol();
        rol.setId(dto.getId());
        rol.setName(dto.getName());
        rol.setActivo(dto.isActivo());
        return rol;
    }
}