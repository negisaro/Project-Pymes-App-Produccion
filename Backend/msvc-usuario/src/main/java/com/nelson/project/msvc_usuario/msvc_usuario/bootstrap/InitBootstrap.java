package com.nelson.project.msvc_usuario.msvc_usuario.bootstrap;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RolRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Bootstrap profesional para inicializar roles y usuario administrador.
 */
@Component
public class InitBootstrap {

  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;

  public InitBootstrap(
    UsuarioRepository usuarioRepository,
    RolRepository rolRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void init() {
    // Crear roles si no existen
    createRoleIfNotExists("ROLE_ADMIN");
    createRoleIfNotExists("ROLE_USER");
    createRoleIfNotExists("ROLE_CLIENT");

    // Crear usuario admin si no existe
    if (usuarioRepository.countByRolName("ROLE_ADMIN") == 0) {
      Rol adminRol = rolRepository.findByName("ROLE_ADMIN").get();
      Usuario admin = Usuario.builder()
        .name("Administrador")
        .lastname("Principal")
        .username("admin")
        .password(passwordEncoder.encode("admin123"))
        .email("admin@correo.com")
        .active(true)
        .build();
      admin.addRol(adminRol);
      usuarioRepository.save(admin);
    }
  }

  private void createRoleIfNotExists(String roleName) {
    rolRepository.findByName(roleName)
      .orElseGet(() -> rolRepository.save(Rol.builder().name(roleName).activo(true).build()));
  }
}
