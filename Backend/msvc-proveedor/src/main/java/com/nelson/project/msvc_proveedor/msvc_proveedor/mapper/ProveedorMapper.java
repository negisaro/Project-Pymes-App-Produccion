package com.nelson.project.msvc_proveedor.msvc_proveedor.mapper;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;

public final class ProveedorMapper {

  private ProveedorMapper() {
    // Utility class, no instanciar
  }

  /**
   * Convierte una entidad Proveedor a un DTO.
   */
  public static ProveedorDTO toDto(Proveedor proveedor) {
    if (proveedor == null) return null;
    return ProveedorDTO.builder()
      .id(proveedor.getId())
      .nombre(proveedor.getNombre())
      .descripcion(proveedor.getDescripcion())
      .contacto(proveedor.getContacto())
      .activo(proveedor.getActivo())
      .build();
  }

  /**
   * Convierte un DTO de creación a una entidad Proveedor.
   */
  public static Proveedor fromCreateDto(ProveedorCreateDTO dto) {
    if (dto == null) return null;
    return Proveedor.builder()
      .nombre(dto.getNombre())
      .descripcion(dto.getDescripcion())
      .contacto(dto.getContacto())
      .activo(dto.getActivo())
      .build();
  }

  /**
   * Actualiza una entidad Proveedor existente con los datos del DTO.
   */
  public static void updateEntityFromDto(
    Proveedor proveedor,
    ProveedorCreateDTO dto
  ) {
    if (proveedor == null || dto == null) return;
    proveedor.setNombre(dto.getNombre());
    proveedor.setDescripcion(dto.getDescripcion());
    proveedor.setContacto(dto.getContacto());
    proveedor.setActivo(dto.getActivo());
  }
}
