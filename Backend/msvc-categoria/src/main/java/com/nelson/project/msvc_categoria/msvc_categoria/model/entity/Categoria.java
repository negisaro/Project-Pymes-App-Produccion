package com.nelson.project.msvc_categoria.msvc_categoria.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categorias")
@EntityListeners(AuditingEntityListener.class)
public class Categoria extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;

  @Column(length = 1000)
  private String descripcion;

  @Column(nullable = false)
  @NotNull(message = "El estado es obligatorio")
  private Boolean estado;
}
