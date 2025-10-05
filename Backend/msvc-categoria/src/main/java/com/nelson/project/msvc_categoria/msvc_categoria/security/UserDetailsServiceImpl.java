package com.nelson.project.msvc_categoria.msvc_categoria.security;

import com.nelson.project.msvc_categoria.msvc_categoria.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.UsuarioDto;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UsuarioFeignClient usuarioFeignClient;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  public UserDetailsServiceImpl(UsuarioFeignClient usuarioFeignClient) {
    this.usuarioFeignClient = usuarioFeignClient;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      UsuarioDto usuario = usuarioFeignClient.findByUsername(username);
      if (usuario == null || !usuario.isActive()) {
        logger.warn("[UserDetailsServiceImpl] Usuario no encontrado o inactivo: {}", username);
        throw new UsernameNotFoundException("Usuario no encontrado o inactivo: " + username);
      }
      var authorities = usuario.getRoles()
        .stream()
        .filter(rol -> {
          boolean valido = rol != null && rol.getName() != null && !rol.getName().isBlank();
          if (!valido) {
            logger.warn("Rol nulo o sin name detectado para usuario: {}", usuario.getUsername());
          }
          return valido;
        })
        .map(rol -> {
          String rolName = rol.getName();
          if (!rolName.startsWith("ROLE_")) {
            rolName = "ROLE_" + rolName;
          }
          return new SimpleGrantedAuthority(rolName);
        })
        .collect(Collectors.toList());
      if (authorities.isEmpty()) {
        logger.info("[UserDetailsServiceImpl] Usuario sin roles: {}", username);
      }
      return new org.springframework.security.core.userdetails.User(
        usuario.getUsername(),
        "",
        authorities
      );
    } catch (Exception e) {
      logger.error("[UserDetailsServiceImpl] Error consultando usuario: {}", username, e);
      throw new UsernameNotFoundException("Error consultando usuario: " + username, e);
    }
  }
}