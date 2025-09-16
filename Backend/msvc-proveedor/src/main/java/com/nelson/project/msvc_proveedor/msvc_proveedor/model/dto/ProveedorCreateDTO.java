package com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;

  @Size(
    max = 1000,
    message = "La descripción no puede superar los 1000 caracteres"
  )
  private String descripcion;

  @NotBlank(message = "El contacto es obligatorio")
  private String contacto;

  @NotNull(message = "El estado activo es obligatorio")
  private Boolean activo;
}
