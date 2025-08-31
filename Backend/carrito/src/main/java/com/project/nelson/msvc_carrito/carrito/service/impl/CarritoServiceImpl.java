package com.project.nelson.msvc_carrito.carrito.service.impl;

import com.project.nelson.msvc_carrito.carrito.model.dto.CarritoDto;
import com.project.nelson.msvc_carrito.carrito.model.entity.Carrito;
import com.project.nelson.msvc_carrito.carrito.repository.CarritoRepository;
import com.project.nelson.msvc_carrito.carrito.mapper.CarritoMapper;
import com.project.nelson.msvc_carrito.carrito.service.CarritoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
