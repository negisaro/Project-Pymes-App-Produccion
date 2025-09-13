package com.nelson.project.msvc_producto.msvc_producto.controller;

import com.nelson.project.msvc_producto.msvc_producto.mapper.ProductoMapper;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/productos")
public class ProductoController {

  @Autowired
  private ProductoService productoService;

  /**
   * Listar productos con paginación.
   * Ejemplo: /productos/list?page=0&size=8
   */
  @GetMapping("/list")
  public ResponseEntity<Page<ProductoDto>> getAllPaged(
    @PageableDefault(size = 8) Pageable pageable
  ) {
    Page<Producto> productos = productoService.findAll(pageable);
    Page<ProductoDto> productosDto = productos.map(ProductoMapper::toDto);
    return ResponseEntity.ok(productosDto);
  }

  /**
   * Obtener un producto por su ID.
   * Ejemplo: /productos/list/5
   */
  @GetMapping("/list/{id}")
  public ResponseEntity<ProductoDto> getById(@PathVariable Long id) {
    Optional<Producto> producto = productoService.findById(id);
    return producto
      .map(p -> ResponseEntity.ok(ProductoMapper.toDto(p)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Crear un nuevo producto.
   * Ejemplo: POST /productos/create
   */
  @PostMapping("/create")
  public ResponseEntity<ProductoDto> create(
    @Valid @RequestBody ProductoCreateDto productoDto
  ) {
    Producto producto = ProductoMapper.fromCreateDto(productoDto);
    Producto saved = productoService.save(producto);
    return ResponseEntity.ok(ProductoMapper.toDto(saved));
  }

  /**
   * Actualizar un producto existente por su ID.
   * Ejemplo: PUT /productos/update/5
   */
  @PutMapping("/update/{id}")
  public ResponseEntity<ProductoDto> update(
    @PathVariable Long id,
    @Valid @RequestBody ProductoCreateDto productoDto
  ) {
    Optional<Producto> existingOpt = productoService.findById(id);
    if (existingOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Producto producto = ProductoMapper.fromCreateDto(productoDto);
    producto.setId(id);
    Producto updated = productoService.save(producto);
    return ResponseEntity.ok(ProductoMapper.toDto(updated));
  }

  /**
   * Eliminar un producto por su ID.
   * Ejemplo: DELETE /productos/delete/5
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!productoService.findById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    productoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Subir una imagen para un producto.
   * Ejemplo: POST /productos/upload
   */
  @PostMapping("/upload")
  public ResponseEntity<Map<String, String>> uploadImagen(
    @RequestParam("file") MultipartFile file
  ) throws IOException {
    String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path uploadsDir = Paths.get("uploads");
    Files.createDirectories(uploadsDir); // Asegura que la carpeta exista
    Path ruta = uploadsDir.resolve(nombreArchivo);
    Files.copy(
      file.getInputStream(),
      ruta,
      java.nio.file.StandardCopyOption.REPLACE_EXISTING
    );
    String url = "/uploads/" + nombreArchivo;
    Map<String, String> response = new HashMap<>();
    response.put("url", url);
    return ResponseEntity.ok(response);
  }
}
