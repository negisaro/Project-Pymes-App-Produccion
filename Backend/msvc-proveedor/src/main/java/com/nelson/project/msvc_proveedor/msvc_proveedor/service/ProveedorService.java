package com.nelson.project.msvc_proveedor.msvc_proveedor.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;


public interface ProveedorService {
    List<Proveedor> findAll();

    Optional<Proveedor> findById(Long id);

    Proveedor save(Proveedor proveedor);

    void deleteById(Long id);

    // Buscar proveedores activos
    List<Proveedor> findActivos();

    // Buscar proveedores por nombre (case insensitive)
    List<Proveedor> findByNombre(String nombre);

    // Buscar proveedores por IDs de productos (integración microservicio producto)
    List<Proveedor> findByProductoId(Long productoId);

    // Buscar por lista de IDs
    List<Proveedor> findByIds(List<Long> ids);

    // Paginación para grandes volúmenes de datos
    Page<Proveedor> findAll(Pageable pageable);
}
