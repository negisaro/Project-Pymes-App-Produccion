package com.nelson.project.msvc_producto.msvc_producto.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entidad Producto.
 * Representa los productos en el sistema, con validaciones y estructura profesional.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "productos")
@EntityListeners(AuditingEntityListener.class)
public class Producto extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;

  @Column(length = 1000)
  private String descripcion;

  @Column(nullable = false)
  @NotNull(message = "El precio es obligatorio")
  @Positive(message = "El precio debe ser positivo")
  private BigDecimal precio;

  @Column(nullable = false)
  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  // Relación moderna: solo guardamos el id, la consulta se hace vía REST a microservicio Categoria
  @Column(name = "categoria_id", nullable = false)
  @NotNull(message = "La categoría es obligatoria")
  private Long categoriaId;

  // Relación moderna: solo guardamos el id, la consulta se hace vía REST a microservicio Proveedor
  @Column(name = "proveedor_id", nullable = false)
  @NotNull(message = "El proveedor es obligatorio")
  private Long proveedorId;

  // Imágenes: lista de URLs
  @ElementCollection
  @CollectionTable(
    name = "producto_imagenes",
    joinColumns = @JoinColumn(name = "producto_id")
  )
  @Column(name = "url")
  private List<String> imagenes;

  @Column(nullable = false)
  @NotNull(message = "El estado es obligatorio")
  private Boolean estado;
}
