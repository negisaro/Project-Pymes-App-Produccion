package com.project.nelson.msvc_user_auth.usuario.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;

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
