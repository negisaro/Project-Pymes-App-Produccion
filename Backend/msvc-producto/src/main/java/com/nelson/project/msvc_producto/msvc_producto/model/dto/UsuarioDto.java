package com.nelson.project.msvc_producto.msvc_producto.model.dto;

import java.util.List;

public class UsuarioDto {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean active;

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}