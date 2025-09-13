package com.nelson.project.msvc_proveedor.msvc_proveedor.mapper;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;

public final class ProveedorMapper {

  private ProveedorMapper() {
    // Utility class, no instanciar
  }

  public static ProveedorDTO toDto(Proveedor proveedor) {
    if (proveedor == null) return null;
    return ProveedorDTO.builder()
      .id(proveedor.getId())
      .nombre(proveedor.getNombre())
      .descripcion(proveedor.getDescripcion())
      .contacto(proveedor.getContacto())
      .activo(proveedor.getActivo())
      .productosId(proveedor.getProductosId())
      .build();
  }

  public static Proveedor fromCreateDto(ProveedorCreateDTO dto) {
    if (dto == null) return null;
    return Proveedor.builder()
      .nombre(dto.getNombre())
      .descripcion(dto.getDescripcion())
      .contacto(dto.getContacto())
      .activo(dto.getActivo())
      .productosId(dto.getProductosId())
      .build();
  }
}
