package com.nelson.project.msvc_producto.msvc_producto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioDto usuario = usuarioFeignClient.findByUsername(username);
        if (usuario == null || !usuario.isActive()) {
            throw new UsernameNotFoundException("Usuario no encontrado o inactivo: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                "",
                usuario.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}