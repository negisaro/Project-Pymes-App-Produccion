package com.nelson.project.msvc_producto.msvc_producto.security;

import com.nelson.project.msvc_producto.msvc_producto.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Implementación profesional de UserDetailsService para Spring Security.
 * Inyección por constructor y manejo robusto de errores.
 * No incluye password ya que el usuario es llamado por Feign y no se proporciona ese campo.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UsuarioFeignClient usuarioFeignClient;

  private static final Logger logger = LoggerFactory.getLogger(
    UserDetailsServiceImpl.class
  );

  public UserDetailsServiceImpl(UsuarioFeignClient usuarioFeignClient) {
    this.usuarioFeignClient = usuarioFeignClient;
  }

  /**
   * Carga el usuario por username desde el microservicio de usuarios.
   * @param username Nombre de usuario
   * @return UserDetails para Spring Security
   * @throws UsernameNotFoundException si el usuario no existe o está inactivo
   */
  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    try {
      UsuarioDto usuario = usuarioFeignClient.findByUsername(username);
      if (usuario == null || !usuario.isActive()) {
        throw new UsernameNotFoundException(
          "Usuario no encontrado o inactivo: " + username
        );
      }
      return new org.springframework.security.core.userdetails.User(
        usuario.getUsername(),
        "",
        usuario
          .getRoles()
          .stream()
          .filter(rol -> {
            boolean valido =
              rol != null && rol.getName() != null && !rol.getName().isBlank();
            if (!valido) {
              logger.warn(
                "Rol nulo o sin name detectado para usuario: {}",
                usuario.getUsername()
              );
            }
            return valido;
          })
          .map(rol -> new SimpleGrantedAuthority(rol.getName()))
          .collect(Collectors.toList())
      );
    } catch (Exception e) {
      throw new UsernameNotFoundException(
        "Error consultando usuario: " + username,
        e
      );
    }
  }
}
