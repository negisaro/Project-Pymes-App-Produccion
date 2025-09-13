package com.nelson.project.msvc_proveedor.msvc_proveedor.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.entity.Proveedor;
import com.nelson.project.msvc_proveedor.msvc_proveedor.repository.ProveedorRepository;
import com.nelson.project.msvc_proveedor.msvc_proveedor.service.ProveedorService;


@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repository;

    public ProveedorServiceImpl(ProveedorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Proveedor> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return repository.save(proveedor);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Proveedor> findActivos() {
        return repository.findByActivoTrue();
    }

    @Override
    public List<Proveedor> findByNombre(String nombre) {
        return repository.findByNombreIgnoreCaseContaining(nombre);
    }

    @Override
    public List<Proveedor> findByProductoId(Long productoId) {
        return repository.findAll().stream()
                .filter(p -> p.getProductosId() != null && p.getProductosId().contains(productoId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> findByIds(List<Long> ids) {
        return repository.findByIdIn(ids);
    }

    @Override
    public Page<Proveedor> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

