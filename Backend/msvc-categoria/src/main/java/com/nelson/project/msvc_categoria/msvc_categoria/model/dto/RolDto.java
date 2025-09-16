package com.nelson.project.msvc_categoria.msvc_categoria.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para rol de usuario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolDto implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String name;
}
