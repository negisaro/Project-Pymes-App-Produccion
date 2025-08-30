package com.project.nelson.msvc_user_auth.usuario.repository;

import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  @EntityGraph(attributePaths = "roles")
  Optional<Usuario> findByUsername(String username);

  Optional<Usuario> findByEmail(String email);

  boolean existsByUsername(String username);

  @Query(
    "SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.name = :rolName"
  )
  long countByRolName(String rolName);
}
