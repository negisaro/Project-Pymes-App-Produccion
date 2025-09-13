package com.nelson.project.msvc_carrito.msvc_carrito.service;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;

public interface CarritoService {
  CarritoDto obtenerCarritoPorUsuario(Long usuarioId);
  CarritoDto agregarItem(Long usuarioId, Long productoId, Integer cantidad);
  CarritoDto quitarItem(Long usuarioId, Long productoId);
  void vaciarCarrito(Long usuarioId);
}
