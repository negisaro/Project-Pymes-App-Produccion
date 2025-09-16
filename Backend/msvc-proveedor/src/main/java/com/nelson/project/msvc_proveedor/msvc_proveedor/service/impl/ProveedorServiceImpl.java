package com.nelson.project.msvc_proveedor.msvc_proveedor.service.impl;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;
import com.nelson.project.msvc_proveedor.msvc_proveedor.repository.ProveedorRepository;
import com.nelson.project.msvc_proveedor.msvc_proveedor.service.ProveedorService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServiceImpl implements ProveedorService {

  private final ProveedorRepository repository;

  public ProveedorServiceImpl(ProveedorRepository repository) {
    this.repository = repository;
  }

  private ProveedorDTO toDTO(Proveedor proveedor) {
    if (proveedor == null) return null;
    return ProveedorDTO.builder()
      .id(proveedor.getId())
      .nombre(proveedor.getNombre())
      .descripcion(proveedor.getDescripcion())
      .contacto(proveedor.getContacto())
      .activo(proveedor.getActivo())
      .build();
  }

  private Proveedor toEntity(ProveedorCreateDTO dto) {
    if (dto == null) return null;
    return Proveedor.builder()
      .id(dto.getId())
      .nombre(dto.getNombre())
      .descripcion(dto.getDescripcion())
      .contacto(dto.getContacto())
      .activo(dto.getActivo())
      .build();
  }

  @Override
  public List<ProveedorDTO> findAll() {
    return repository
      .findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<ProveedorDTO> findById(Long id) {
    return repository.findById(id).map(this::toDTO);
  }

  @Override
  public ProveedorDTO save(ProveedorCreateDTO proveedorCreateDto) {
    Proveedor proveedor = toEntity(proveedorCreateDto);
    return toDTO(repository.save(proveedor));
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public List<ProveedorDTO> findActivos() {
    return repository
      .findByActivoTrue()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Override
  public List<ProveedorDTO> findByNombre(String nombre) {
    return repository
      .findByNombreIgnoreCaseContaining(nombre)
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Override
  public List<ProveedorDTO> findByProductoId(Long productoId) {
    // Suponiendo que existe una relación productosId en ProveedorDTO y Proveedor
    return repository
      .findAll()
      .stream()
      .filter(p -> {
        // Si tienes productosId en la entidad, descomenta y ajusta:
        // return p.getProductosId() != null && p.getProductosId().contains(productoId);
        return false;
      })
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Override
  public List<ProveedorDTO> findByIds(List<Long> ids) {
    return repository
      .findByIdIn(ids)
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Override
  public Page<ProveedorDTO> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toDTO);
  }

  @Override
  public ProveedorDTO update(Long id, ProveedorCreateDTO proveedorCreateDto) {
    Optional<Proveedor> optionalProveedor = repository.findById(id);
    if (optionalProveedor.isEmpty()) {
      throw new IllegalArgumentException(
        "Proveedor no encontrado con id: " + id
      );
    }
    Proveedor proveedor = optionalProveedor.get();
    proveedor.setNombre(proveedorCreateDto.getNombre());
    proveedor.setDescripcion(proveedorCreateDto.getDescripcion());
    proveedor.setContacto(proveedorCreateDto.getContacto());
    proveedor.setActivo(proveedorCreateDto.getActivo());
    return toDTO(repository.save(proveedor));
  }
}
