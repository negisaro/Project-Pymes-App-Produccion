package com.project.nelson.msvc_user_auth.usuario.model.dtos;

import java.io.Serializable;

public class RolDto implements Serializable {
    private Long id;
    private String name;
    private boolean activo;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public RolDto() {
    }

    /**
     * Constructor completo.
     */
    public RolDto(Long id, String name, boolean activo) {
        this.id = id;
        this.name = name;
        this.activo = activo;
    }
}
