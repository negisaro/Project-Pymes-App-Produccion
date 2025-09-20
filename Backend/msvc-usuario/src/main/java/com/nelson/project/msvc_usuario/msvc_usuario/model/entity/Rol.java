package com.nelson.project.msvc_usuario.msvc_usuario.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
public class Rol extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 50)
  private String name;

  @JsonIgnoreProperties({ "roles", "handler", "hibernateLazyInitializer" })
  @ManyToMany(mappedBy = "roles")
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
