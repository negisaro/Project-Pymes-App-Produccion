package com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del proveedor */
  private Long id;

  /** Nombre del proveedor */
  private String nombre;

  /** Descripción del proveedor */
  private String descripcion;

  /** Información de contacto */
  private String contacto;

  /** Estado de activación */
  private Boolean activo;
}
