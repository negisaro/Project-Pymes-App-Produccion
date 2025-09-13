package com.nelson.project.msvc_producto.msvc_producto.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.CategoriaClient;
import com.nelson.project.msvc_producto.msvc_producto.clientfeign.ProveedorClient;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.CategoriaDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProveedorDTO;
import com.nelson.project.msvc_producto.msvc_producto.model.entity.Producto;
import com.nelson.project.msvc_producto.msvc_producto.repository.ProductoRepository;
import com.nelson.project.msvc_producto.msvc_producto.service.ProductoService;


@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaClient categoriaClient;
    @Autowired
    private ProveedorClient proveedorClient;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    // Métodos para integración con microservicios
    public CategoriaDTO getCategoriaDeProducto(Long categoriaId) {
        return categoriaClient.getCategoriaById(categoriaId);
    }

    public ProveedorDTO getProveedorDeProducto(Long proveedorId) {
        return proveedorClient.getProveedorById(proveedorId);
    }
}

