package com.nelson.project.msvc_proveedor.msvc_proveedor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar proveedores activos
    List<Proveedor> findByActivoTrue();

    // Buscar por nombre (case insensitive)
    List<Proveedor> findByNombreIgnoreCaseContaining(String nombre);

    // Buscar por lista de IDs
    List<Proveedor> findByIdIn(List<Long> ids);

    // Buscar proveedor por nombre exacto
    Optional<Proveedor> findByNombre(String nombre);
}

