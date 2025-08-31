package com.project.nelson.msvc_user_auth.usuario.model.dtos;

import java.util.List;

public class LoginResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String username;
    private Boolean active;
    private List<RolDto> roles;
    private String token;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public List<RolDto> getRoles() { return roles; }
    public void setRoles(List<RolDto> roles) { this.roles = roles; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}