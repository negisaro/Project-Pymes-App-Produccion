package com.nelson.project.msvc_orden.msvc_orden.service;

import com.nelson.project.msvc_orden.msvc_orden.model.dto.OrdenDto;

public interface OrdenService {
  OrdenDto obtenerOrdenPorUsuario(Long usuarioId);
  OrdenDto agregarItem(Long usuarioId, Long productoId, Integer cantidad);
  OrdenDto quitarItem(Long usuarioId, Long productoId);
  void vaciarOrden(Long usuarioId);
}
