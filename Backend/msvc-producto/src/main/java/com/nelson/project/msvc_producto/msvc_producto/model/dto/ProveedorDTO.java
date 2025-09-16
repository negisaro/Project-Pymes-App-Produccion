package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para proveedor.
 * Representa los datos transferidos de proveedores.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Identificador único del proveedor */
  private Long id;

  /** Nombre del proveedor */
  private String nombre;

  /** Información de contacto */
  private String contacto;
}
