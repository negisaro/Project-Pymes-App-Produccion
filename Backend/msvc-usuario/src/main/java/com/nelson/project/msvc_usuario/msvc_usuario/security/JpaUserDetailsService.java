package com.nelson.project.msvc_usuario.msvc_usuario.security;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// ...existing imports...
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final UsuarioRepository repository;

  public JpaUserDetailsService(UsuarioRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<Usuario> userOptional = repository.findByUsername(username);

    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException(
        String.format("Username '%s' no existe en el sistema.", username)
      );
    }

    Usuario user = userOptional.get();

    if (!user.isActive()) {
      throw new UsernameNotFoundException(
        String.format(
          "Usuario '%s' está inactivo. Contacte al administrador.",
          username
        )
      );
    }

    List<GrantedAuthority> authorities = user
      .getRoles()
      .stream()
      .map(role -> {
        String roleName = role.getName();
        if (roleName != null && !roleName.startsWith("ROLE_")) {
          roleName = "ROLE_" + roleName;
        }
        return roleName;
      })
      .filter(roleName -> roleName != null && !roleName.trim().isEmpty())
      .distinct()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      true,
      true,
      true,
      true,
      authorities
    );
  }
}
