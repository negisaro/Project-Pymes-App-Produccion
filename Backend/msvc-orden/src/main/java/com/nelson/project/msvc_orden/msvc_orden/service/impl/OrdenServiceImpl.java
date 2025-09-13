package com.nelson.project.msvc_orden.msvc_orden.service.impl;

import com.nelson.project.msvc_orden.msvc_orden.model.dto.OrdenDto;
import com.nelson.project.msvc_orden.msvc_orden.service.OrdenService;

public class OrdenServiceImpl implements OrdenService {
  @Override
  public OrdenDto obtenerOrdenPorUsuario(Long usuarioId) {
    // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerOrdenPorUsuario'");
  }

  @Override
  public OrdenDto agregarItem(Long usuarioId, Long productoId, Integer cantidad) {
    // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agregarItem'");
  }

  @Override
  public OrdenDto quitarItem(Long usuarioId, Long productoId) {
    // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'quitarItem'");
  }

  @Override
  public void vaciarOrden(Long usuarioId) {
    // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'vaciarOrden'");
  }
}
