package com.nelson.project.msvc_usuario.msvc_usuario.repository;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  /**
   * Busca un usuario por su nombre de usuario y carga los roles asociados.
   * @param username nombre de usuario
   * @return usuario encontrado o vacío
   */
  @EntityGraph(attributePaths = "roles")
  Optional<Usuario> findByUsername(String username);

  /**
   * Busca un usuario por su email.
   * @param email email del usuario
   * @return usuario encontrado o vacío
   */
  Optional<Usuario> findByEmail(String email);

  /**
   * Verifica si existe un usuario por su nombre de usuario.
   * @param username nombre de usuario
   * @return true si existe, false si no
   */
  boolean existsByUsername(String username);

  boolean existsByEmailAndIdNot(String email, Long id);

  /**
   * Cuenta la cantidad de usuarios que tienen un rol específico por nombre de rol.
   * @param rolName nombre del rol
   * @return cantidad de usuarios con ese rol
   */
  @Query(
    "SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.name = :rolName"
  )
  long countByRolName(String rolName);
}
