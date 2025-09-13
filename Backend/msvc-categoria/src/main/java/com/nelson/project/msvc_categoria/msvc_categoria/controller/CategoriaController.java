package com.nelson.project.msvc_categoria.msvc_categoria.controller;

import com.nelson.project.msvc_categoria.msvc_categoria.mapper.CategoriaMapper;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  @Autowired
  private CategoriaService categoriaService;

  @GetMapping("/list")
  public ResponseEntity<Page<CategoriaDTO>> getAllPaged(
    @RequestParam(defaultValue = "0", required = false) Integer page,
    @RequestParam(defaultValue = "10", required = false) Integer size
  ) {
    Page<Categoria> categorias = categoriaService.findAll(
      PageRequest.of(page, size)
    );
    Page<CategoriaDTO> dtoPage = categorias.map(CategoriaMapper::toDto);
    return ResponseEntity.ok(dtoPage);
  }

  @GetMapping("/list/{id}")
  public ResponseEntity<CategoriaDTO> getById(@PathVariable Long id) {
    Optional<Categoria> categoria = categoriaService.findById(id);
    return categoria
      .map(c -> ResponseEntity.ok(CategoriaMapper.toDto(c)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/create")
  public CategoriaDTO create(
    @Valid @RequestBody CategoriaCreateDto categoriaCreateDto
  ) {
    Categoria categoria = CategoriaMapper.fromCreateDto(categoriaCreateDto);
    Categoria saved = categoriaService.save(categoria);
    return CategoriaMapper.toDto(saved);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<CategoriaDTO> update(
    @PathVariable Long id,
    @RequestBody CategoriaCreateDto categoriaCreateDto
  ) {
    if (!categoriaService.findById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    categoriaCreateDto.setId(id);
    Categoria updated = categoriaService.save(
      CategoriaMapper.fromCreateDto(categoriaCreateDto)
    );
    return ResponseEntity.ok(CategoriaMapper.toDto(updated));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!categoriaService.findById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    categoriaService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
