package com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

  private Long id;
  private String nombre;
  private String descripcion;
  private String contacto;
  private Boolean activo;

  // Relación con productos (IDs de productos en el microservicio producto)
  private List<Long> productosId;
}
