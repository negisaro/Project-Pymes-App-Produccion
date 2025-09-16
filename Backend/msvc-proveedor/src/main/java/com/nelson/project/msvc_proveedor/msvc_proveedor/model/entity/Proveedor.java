package com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "proveedores")
@EntityListeners(AuditingEntityListener.class)
public class Proveedor extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String nombre;

  @Size(max = 1000)
  @Column(length = 1000)
  private String descripcion;

  @NotBlank
  @Column(nullable = false)
  private String contacto;

  @Builder.Default
  @Column(nullable = false)
  private Boolean activo = true;
}
