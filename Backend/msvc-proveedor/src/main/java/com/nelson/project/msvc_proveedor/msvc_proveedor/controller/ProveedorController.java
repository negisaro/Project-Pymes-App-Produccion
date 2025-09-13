package com.nelson.project.msvc_proveedor.msvc_proveedor.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import com.nelson.project.msvc_proveedor.msvc_proveedor.mapper.ProveedorMapper;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;
import com.nelson.project.msvc_proveedor.msvc_proveedor.service.ProveedorService;
import jakarta.validation.Valid;

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
            @RequestParam(defaultValue = "10") int size) {
        Page<ProveedorDTO> proveedores = service.findAll(PageRequest.of(page, size))
                .map(ProveedorMapper::toDto);
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable Long id) {
        Optional<Proveedor> proveedor = service.findById(id);
        return proveedor.map(p -> ResponseEntity.ok(ProveedorMapper.toDto(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorDTO>> getActivos() {
        List<ProveedorDTO> activos = service.findActivos()
                .stream().map(ProveedorMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProveedorDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<ProveedorDTO> resultado = service.findByNombre(nombre)
                .stream().map(ProveedorMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/por-producto/{productoId}")
    public ResponseEntity<List<ProveedorDTO>> getByProductoId(@PathVariable Long productoId) {
        List<ProveedorDTO> proveedores = service.findByProductoId(productoId)
                .stream().map(ProveedorMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(proveedores);
    }

    @PostMapping("/create")
    public ResponseEntity<ProveedorDTO> create(@Valid @RequestBody ProveedorCreateDTO dto) {
        Proveedor proveedor = ProveedorMapper.fromCreateDto(dto);
        Proveedor created = service.save(proveedor);
        return ResponseEntity.ok(ProveedorMapper.toDto(created));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProveedorDTO> update(@PathVariable Long id, @Valid @RequestBody ProveedorCreateDTO dto) {
        Optional<Proveedor> proveedorOpt = service.findById(id);
        if (proveedorOpt.isEmpty())
            return ResponseEntity.notFound().build();
        Proveedor proveedor = proveedorOpt.get();
        proveedor.setNombre(dto.getNombre());
        proveedor.setDescripcion(dto.getDescripcion());
        proveedor.setContacto(dto.getContacto());
        proveedor.setActivo(dto.getActivo());
        proveedor.setProductosId(dto.getProductosId());
        Proveedor updated = service.save(proveedor);
        return ResponseEntity.ok(ProveedorMapper.toDto(updated));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

