package com.nelson.project.msvc_producto.msvc_producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @SuppressWarnings("null")
    Page<Producto> findAll(Pageable pageable);
}
