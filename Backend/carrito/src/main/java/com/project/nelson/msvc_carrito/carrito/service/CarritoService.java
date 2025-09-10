package com.project.nelson.msvc_carrito.carrito.service;

import com.project.nelson.msvc_carrito.carrito.model.dto.CarritoDto;

public interface CarritoService {
  CarritoDto obtenerCarritoPorUsuario(Long usuarioId);
  CarritoDto agregarItem(Long usuarioId, Long productoId, Integer cantidad);
  CarritoDto quitarItem(Long usuarioId, Long productoId);
  void vaciarCarrito(Long usuarioId);
}
