package com.nelson.project.msvc_producto.msvc_producto.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
/**
 * Servicio para gestión de productos.
 */
public interface ProductoService {

    /**
     * Lista todos los productos.
     */
    List<Producto> findAll();

    /**
     * Lista productos con paginación.
     */
    Page<Producto> findAll(Pageable pageable);

    /**
     * Busca producto por ID.
     */
    Optional<Producto> findById(Long id);

    /**
     * Guarda o actualiza producto.
     */
    Producto save(Producto producto);

    /**
     * Elimina producto por ID.
     */
    void deleteById(Long id);
}
