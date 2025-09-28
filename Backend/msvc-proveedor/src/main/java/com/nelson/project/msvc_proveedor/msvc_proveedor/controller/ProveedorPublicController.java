package com.nelson.project.msvc_proveedor.msvc_proveedor.controller;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.service.ProveedorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/proveedores")
public class ProveedorPublicController {

  private final ProveedorService service;

  public ProveedorPublicController(ProveedorService service) {
    this.service = service;
  }

  @GetMapping("/list")
  public ResponseEntity<Page<ProveedorDTO>> getAllPaged(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    Page<ProveedorDTO> proveedores = service.findAll(
      PageRequest.of(page, size)
    );
    return ResponseEntity.ok(proveedores);
  }
}
