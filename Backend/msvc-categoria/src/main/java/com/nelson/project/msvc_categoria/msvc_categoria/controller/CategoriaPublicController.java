package com.nelson.project.msvc_categoria.msvc_categoria.controller;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/categorias")
public class CategoriaPublicController {

  private final CategoriaService categoriaService;

  public CategoriaPublicController(CategoriaService categoriaService) {
    this.categoriaService = categoriaService;
  }

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
}
