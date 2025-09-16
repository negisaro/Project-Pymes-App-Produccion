package com.nelson.project.msvc_proveedor.msvc_proveedor.controller;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.service.ProveedorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

  private final ProveedorService service;

  public ProveedorController(ProveedorService service) {
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

  @GetMapping("/list/{id}")
  public ResponseEntity<ProveedorDTO> getById(@PathVariable Long id) {
    Optional<ProveedorDTO> proveedor = service.findById(id);
    return proveedor
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/activos")
  public ResponseEntity<List<ProveedorDTO>> getActivos() {
    List<ProveedorDTO> activos = service.findActivos();
    return ResponseEntity.ok(activos);
  }

  @GetMapping("/buscar")
  public ResponseEntity<List<ProveedorDTO>> buscarPorNombre(
    @RequestParam String nombre
  ) {
    List<ProveedorDTO> resultado = service.findByNombre(nombre);
    return ResponseEntity.ok(resultado);
  }

  @GetMapping("/por-producto/{productoId}")
  public ResponseEntity<List<ProveedorDTO>> getByProductoId(
    @PathVariable Long productoId
  ) {
    List<ProveedorDTO> proveedores = service.findByProductoId(productoId);
    return ResponseEntity.ok(proveedores);
  }

  @PostMapping("/create")
  public ResponseEntity<ProveedorDTO> create(
    @Valid @RequestBody ProveedorCreateDTO dto
  ) {
    ProveedorDTO created = service.save(dto);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<ProveedorDTO> update(
    @PathVariable Long id,
    @Valid @RequestBody ProveedorCreateDTO dto
  ) {
    Optional<ProveedorDTO> proveedorOpt = service.findById(id);
    if (proveedorOpt.isEmpty()) return ResponseEntity.notFound().build();
    ProveedorDTO updated = service.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (service.findById(id).isEmpty()) return ResponseEntity.notFound()
      .build();
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
