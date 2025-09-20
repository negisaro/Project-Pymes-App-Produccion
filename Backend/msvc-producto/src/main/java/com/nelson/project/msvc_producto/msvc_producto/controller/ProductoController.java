package com.nelson.project.msvc_producto.msvc_producto.controller;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoCreateDto;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProductoDto;
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
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(
    ProductoController.class
  );

  private final ProductoService productoService;

  public ProductoController(ProductoService productoService) {
    this.productoService = productoService;
  }

  /**
   * Listar productos con paginación.
   * Ejemplo: /productos/list?page=0&size=8
   */
  @GetMapping("/list")
  public ResponseEntity<Page<ProductoDto>> getAllPaged(
    @PageableDefault(size = 8) Pageable pageable
  ) {
    Page<ProductoDto> productosDto = productoService.findAll(pageable);
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
    List<ProductoDto> productosDto =
      productoService.findByNombreContainingIgnoreCase(nombre);
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
    Page<ProductoDto> productosDto = productoService.findByEstado(
      estado,
      pageable
    );
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
    List<ProductoDto> productosDto = productoService.findByPrecioBetween(
      min,
      max
    );
    return ResponseEntity.ok(productosDto);
  }

  /**
   * Obtener un producto por su ID.
   * Ejemplo: /productos/list/5
   */
  @GetMapping("/list/{id}")
  public ResponseEntity<ProductoDto> getById(@PathVariable Long id) {
    return productoService
      .findById(id)
      .map(ResponseEntity::ok)
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
    logger.info("=== DEBUG CREATE PRODUCTO ===");
    logger.info("DTO recibido (toString): {}", productoDto);
    logger.info("Imagenes en DTO: {}", productoDto.getImagenes());
    // LOG BACKEND: Mostrar campos individuales del DTO
    logger.info("Campos recibidos:");
    logger.info("  nombre: {}", productoDto.getNombre());
    logger.info("  descripcion: {}", productoDto.getDescripcion());
    logger.info("  precio: {}", productoDto.getPrecio());
    logger.info("  stock: {}", productoDto.getStock());
    logger.info("  categoriaId: {}", productoDto.getCategoriaId());
    logger.info("  proveedorId: {}", productoDto.getProveedorId());
    logger.info("  estado: {}", productoDto.getEstado());
    ProductoDto saved = productoService.create(productoDto);
    return ResponseEntity.ok(saved);
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
    try {
      ProductoDto updated = productoService.update(id, productoDto);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
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
  ) {
    Map<String, String> response = new HashMap<>();

    try {
      // Validaciones básicas
      if (file.isEmpty()) {
        response.put("error", "El archivo está vacío.");
        return ResponseEntity.badRequest().body(response);
      }

      String contentType = file.getContentType();
      long maxSize = 2 * 1024 * 1024; // 2MB

      // Validar tipo de archivo
      if (
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

      // Validar tamaño
      if (file.getSize() > maxSize) {
        response.put("error", "El tamaño máximo permitido es 2MB.");
        return ResponseEntity.badRequest().body(response);
      }

      // Validar nombre del archivo
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null || originalFilename.trim().isEmpty()) {
        response.put("error", "El archivo no tiene nombre válido.");
        return ResponseEntity.badRequest().body(response);
      }

      // Validar que realmente sea una imagen
      try (var is = file.getInputStream()) {
        if (javax.imageio.ImageIO.read(is) == null) {
          response.put("error", "El archivo no es una imagen válida.");
          return ResponseEntity.badRequest().body(response);
        }
      }

      // Crear directorio si no existe (usar ruta absoluta para Docker)
      Path uploadsDir = Paths.get("uploads");
      Files.createDirectories(uploadsDir);
      System.out.println(
        "Ruta absoluta uploads: " + uploadsDir.toAbsolutePath()
      );

      // Generar nombre único para el archivo
      String nombreArchivo =
        UUID.randomUUID() + "_" + originalFilename.replaceAll("\\s+", "_");
      Path ruta = uploadsDir.resolve(nombreArchivo);

      // Guardar archivo
      Files.copy(
        file.getInputStream(),
        ruta,
        java.nio.file.StandardCopyOption.REPLACE_EXISTING
      );

      String url = "/uploads/" + nombreArchivo;
      response.put("url", url);
      response.put("message", "Imagen subida exitosamente");

      System.out.println("=== DEBUG UPLOAD IMAGE ===");
      System.out.println("Archivo subido: " + nombreArchivo);
      System.out.println("URL generada: " + url);

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      System.err.println("Error de IO al subir imagen: " + e.getMessage());
      response.put("error", "No se pudo guardar el archivo en el servidor.");
      return ResponseEntity.status(500).body(response);
    } catch (Exception e) {
      System.err.println("Error general al subir imagen: " + e.getMessage());
      e.printStackTrace();
      response.put(
        "error",
        "Error interno al subir la imagen: " + e.getMessage()
      );
      return ResponseEntity.status(500).body(response);
    }
  }
}
