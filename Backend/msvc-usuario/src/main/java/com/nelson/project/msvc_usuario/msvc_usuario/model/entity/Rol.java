package com.nelson.project.msvc_usuario.msvc_usuario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
  name = "roles",
  indexes = { @Index(name = "idx_rol_name", columnList = "name") }
)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Rol extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotBlank
  @Column(unique = true, nullable = false, length = 50)
  private String name;

  @JsonIgnoreProperties({ "roles", "handler", "hibernateLazyInitializer" })
  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private final List<Usuario> usuarios = new ArrayList<>();

  @Builder.Default
  @Column(nullable = false)
  private boolean activo = true;

  // Métodos para manipular usuarios de forma controlada (encapsulamiento)
  public void addUsuario(Usuario usuario) {
    if (usuario != null && !usuarios.contains(usuario)) {
      usuarios.add(usuario);
    }
  }

  public void removeUsuario(Usuario usuario) {
    usuarios.remove(usuario);
  }

  public List<Usuario> getUsuarios() {
    return java.util.Collections.unmodifiableList(usuarios);
  }

  // Métodos para activar/desactivar rol
  public void activar() {
    this.activo = true;
  }

  public void desactivar() {
    this.activo = false;
  }
}
