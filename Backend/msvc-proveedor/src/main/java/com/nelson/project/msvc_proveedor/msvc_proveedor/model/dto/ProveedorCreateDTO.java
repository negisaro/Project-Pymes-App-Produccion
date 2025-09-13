package com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El contacto es obligatorio")
    private String contacto;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    // Relación con productos (IDs de productos en el microservicio producto)
    private List<Long> productosId;
}

