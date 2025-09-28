package com.nelson.project.msvc_producto.msvc_producto.controller;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/productos")
public class ProductoPublicController {

  private final ProductoService productoService;

  public ProductoPublicController(ProductoService productoService) {
    this.productoService = productoService;
  }

  /**
   * Listar productos con paginación.
   * Ejemplo: /productos/list?page=0&size=8
   */
  @GetMapping("/list")
  public ResponseEntity<Page<ProductoDto>> getAllPaged(
    @PageableDefault(size = 5) Pageable pageable
  ) {
    Page<ProductoDto> productosDto = productoService.findAll(pageable);
    return ResponseEntity.ok(productosDto);
  }
}
