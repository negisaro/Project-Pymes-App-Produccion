package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualización parcial de usuario.
 * Todos los campos son opcionales; se actualizan sólo si no son null.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(max = 50)
  private String name;

  @Size(max = 50)
  private String lastname;

  @Size(min = 4, max = 20)
  private String username;

  @Size(min = 8, max = 100)
  private String password;

  @Email
  private String email;

  private List<Long> rolesIds;
}
