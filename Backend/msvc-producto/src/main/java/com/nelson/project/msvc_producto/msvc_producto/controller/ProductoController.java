package com.nelson.project.msvc_producto.msvc_producto.controller;

import com.nelson.project.msvc_producto.msvc_producto.mapper.ProductoMapper;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  private final ProductoService productoService;
  private final ProductoMapper productoMapper;

  public ProductoController(
    ProductoService productoService,
    ProductoMapper productoMapper
  ) {
    this.productoService = productoService;
    this.productoMapper = productoMapper;
  }

  /**
   * Listar productos con paginación.
   * Ejemplo: /productos/list?page=0&size=8
   */
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  @GetMapping("/list")
  public ResponseEntity<Page<ProductoDto>> getAllPaged(
    @PageableDefault(size = 8) Pageable pageable
  ) {
    Page<Producto> productos = productoService.findAll(pageable);
    Page<ProductoDto> productosDto = productos.map(productoMapper::toDto);
    return ResponseEntity.ok(productosDto);
  }

  /**
   * Buscar productos por nombre (contiene, ignorando mayúsculas/minúsculas).
   * Ejemplo: /productos/search?nombre=tv
   */
  @GetMapping("/search")
  public ResponseEntity<List<ProductoDto>> searchByNombre(
    @RequestParam String nombre
  ) {
    List<Producto> productos = productoService.findByNombreContainingIgnoreCase(
      nombre
    );
    List<ProductoDto> productosDto = productos
      .stream()
      .map(productoMapper::toDto)
      .toList();
    return ResponseEntity.ok(productosDto);
  }

  /**
   * Buscar productos por estado (activo/inactivo) con paginación.
   * Ejemplo: /productos/estado?estado=true&page=0&size=8
   */
  @GetMapping("/estado")
  public ResponseEntity<Page<ProductoDto>> searchByEstado(
    @RequestParam Boolean estado,
    @PageableDefault(size = 8) Pageable pageable
  ) {
    Page<Producto> productos = productoService.findByEstado(estado, pageable);
    Page<ProductoDto> productosDto = productos.map(productoMapper::toDto);
    return ResponseEntity.ok(productosDto);
  }

  /**
   * Buscar productos por rango de precio.
   * Ejemplo: /productos/precio?min=1000&max=5000
   */
  @GetMapping("/precio")
  public ResponseEntity<List<ProductoDto>> searchByPrecio(
    @RequestParam BigDecimal min,
    @RequestParam BigDecimal max
  ) {
    List<Producto> productos = productoService.findByPrecioBetween(min, max);
    List<ProductoDto> productosDto = productos
      .stream()
      .map(productoMapper::toDto)
      .toList();
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
      .map(p -> ResponseEntity.ok(productoMapper.toDto(p)))
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
    Producto producto = productoMapper.fromCreateDto(productoDto);
    Producto saved = productoService.save(producto);
    return ResponseEntity.ok(productoMapper.toDto(saved));
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
    Producto producto = productoMapper.fromCreateDto(productoDto);
    producto.setId(id);
    Producto updated = productoService.save(producto);
    return ResponseEntity.ok(productoMapper.toDto(updated));
  }

  /**
   * Eliminar un producto por su ID.
   * Ejemplo: DELETE /productos/delete/5
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (productoService.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    productoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Subir una imagen para un producto. Solo permite imágenes JPG, PNG, JPEG y tamaño máximo 2MB.
   * Ejemplo: POST /productos/upload
   */
  @PostMapping("/upload")
  public ResponseEntity<Map<String, String>> uploadImagen(
    @RequestParam("file") MultipartFile file
  ) throws IOException {
    Map<String, String> response = new HashMap<>();
    String contentType = file.getContentType();
    long maxSize = 2 * 1024 * 1024; // 2MB
    if (
      file.isEmpty() ||
      contentType == null ||
      !(contentType.equals("image/jpeg") ||
        contentType.equals("image/png") ||
        contentType.equals("image/jpg"))
    ) {
      response.put(
        "error",
        "Solo se permiten archivos de imagen JPG, JPEG o PNG."
      );
      return ResponseEntity.badRequest().body(response);
    }
    if (file.getSize() > maxSize) {
      response.put("error", "El tamaño máximo permitido es 2MB.");
      return ResponseEntity.badRequest().body(response);
    }
    String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path uploadsDir = Paths.get("uploads");
    Files.createDirectories(uploadsDir);
    Path ruta = uploadsDir.resolve(nombreArchivo);
    Files.copy(
      file.getInputStream(),
      ruta,
      java.nio.file.StandardCopyOption.REPLACE_EXISTING
    );
    String url = "/uploads/" + nombreArchivo;
    response.put("url", url);
    return ResponseEntity.ok(response);
  }

  /**
   * Buscar usuario por username (flujo seguro y profesional).
   * @param username Nombre de usuario
   * @return ResponseEntity con el usuario o not found
   */
  @GetMapping("/{username}")
  public ResponseEntity<UsuarioDto> findByUsername(
    @PathVariable String username
  ) {
    Optional<UsuarioDto> usuarioOpt = productoService.findByUsername(username);
    return usuarioOpt
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
