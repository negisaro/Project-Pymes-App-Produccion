package com.nelson.project.msvc_usuario.msvc_usuario.repository;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio profesional y escalable para la entidad Rol.
 * Extiende JpaRepository y define métodos de consulta personalizados.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
  /**
   * Busca un rol por su nombre.
   *
   * @param name nombre del rol
   * @return rol encontrado o vacío
   */
  Optional<Rol> findByName(String name);

  /**
   * Verifica si existe un rol por su nombre.
   *
   * @param name nombre del rol
   * @return true si existe, false si no
   */
  boolean existsByName(String name);
}
