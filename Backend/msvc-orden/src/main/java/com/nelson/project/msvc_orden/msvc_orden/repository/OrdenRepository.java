package com.nelson.project.msvc_orden.msvc_orden.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nelson.project.msvc_orden.msvc_orden.model.entity.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByUsuarioId(Long usuarioId);
}

