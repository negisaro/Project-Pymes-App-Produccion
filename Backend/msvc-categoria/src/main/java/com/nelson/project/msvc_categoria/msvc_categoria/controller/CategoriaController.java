package com.nelson.project.msvc_categoria.msvc_categoria.controller;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de categorías.
 * Arquitectura profesional, funcional y alineada con SOLID.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  @Autowired
  private CategoriaService categoriaService;

  /**
   * Obtiene todas las categorías paginadas.
   */

  @GetMapping("/list")
  public ResponseEntity<Page<CategoriaDTO>> getAllPaged(
    @RequestParam(defaultValue = "0") Integer page,
    @RequestParam(defaultValue = "10") Integer size
  ) {
    Page<CategoriaDTO> dtoPage = categoriaService.findAll(
      PageRequest.of(page, size)
    );
    return ResponseEntity.ok(dtoPage);
  }

  /**
   * Obtiene una categoría por su ID.
   */
  @GetMapping("/list/{id}")
  public ResponseEntity<CategoriaDTO> getById(@PathVariable Long id) {
    Optional<CategoriaDTO> categoria = categoriaService.findById(id);
    return categoria
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Crea una nueva categoría.
   */

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/create")
  public ResponseEntity<CategoriaDTO> create(
    @Valid @RequestBody CategoriaCreateDto categoriaCreateDto
  ) {
    CategoriaDTO created = categoriaService.save(categoriaCreateDto);
    return ResponseEntity.ok(created);
  }

  /**
   * Actualiza una categoría existente.
   */
  @PutMapping("/update/{id}")
  public ResponseEntity<CategoriaDTO> update(
    @PathVariable Long id,
    @Valid @RequestBody CategoriaCreateDto categoriaCreateDto
  ) {
    if (categoriaService.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    CategoriaDTO updated = categoriaService.update(id, categoriaCreateDto);
    return ResponseEntity.ok(updated);
  }

  /**
   * Elimina una categoría por su ID.
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (categoriaService.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    categoriaService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
