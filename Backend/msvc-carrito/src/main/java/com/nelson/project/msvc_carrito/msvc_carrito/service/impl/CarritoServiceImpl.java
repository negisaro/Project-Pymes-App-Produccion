package com.nelson.project.msvc_carrito.msvc_carrito.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nelson.project.msvc_carrito.msvc_carrito.mapper.CarritoMapper;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import com.nelson.project.msvc_carrito.msvc_carrito.repository.CarritoRepository;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoService;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;
    private final CarritoMapper carritoMapper;

    public CarritoServiceImpl(CarritoRepository carritoRepository, CarritoMapper carritoMapper) {
        this.carritoRepository = carritoRepository;
        this.carritoMapper = carritoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDto obtenerCarritoPorUsuario(Long usuarioId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);
        return carritoOpt.map(carritoMapper::toDto).orElse(null);
    }

    @Override
    public CarritoDto agregarItem(Long usuarioId, Long productoId, Integer cantidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agregarItem'");
    }

    @Override
    public CarritoDto quitarItem(Long usuarioId, Long productoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'quitarItem'");
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'vaciarCarrito'");
    }

    // Métodos agregar/quitar/vaciar implementados aquí siguiendo lógica SOLID
}

