package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaCreateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Nombre del categoría */
  @NotBlank
  private String nombre;

  /** Descripción del categoría */
  @Size(max = 1000)
  private String descripcion;

  /** Estado del categoría (activo/inactivo) */
  @NotNull
  private Boolean estado;
}
