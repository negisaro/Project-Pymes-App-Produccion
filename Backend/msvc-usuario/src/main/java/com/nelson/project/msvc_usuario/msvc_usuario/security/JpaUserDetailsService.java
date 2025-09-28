package com.nelson.project.msvc_usuario.msvc_usuario.security;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
    private static final String ROLE_PREFIX = "ROLE_";

    private final UsuarioRepository repository;

    public JpaUserDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("[JpaUserDetailsService] Buscando usuario: {}", username);

        Optional<Usuario> userOptional = repository.findByUsername(username);

        if (userOptional.isEmpty()) {
            logger.warn("[JpaUserDetailsService] Usuario no encontrado: {}", username);
            throw new UsernameNotFoundException(
                String.format("Username '%s' no existe en el sistema.", username)
            );
        }

        Usuario user = userOptional.get();

        if (!user.isActive()) {
            logger.warn("[JpaUserDetailsService] Usuario inactivo: {}", username);
            throw new UsernameNotFoundException(
                String.format("Usuario '%s' está inactivo. Contacte al administrador.", username)
            );
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> {
                String roleName = role.getName();
                if (roleName != null && !roleName.startsWith(ROLE_PREFIX)) {
                    roleName = ROLE_PREFIX + roleName;
                }
                return roleName;
            })
            .filter(roleName -> roleName != null && !roleName.trim().isEmpty())
            .distinct()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        logger.info("[JpaUserDetailsService] Usuario autenticado: {} con roles: {}", username, authorities);

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isActive(), // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
}