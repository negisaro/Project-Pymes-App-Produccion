package com.nelson.project.msvc_carrito.msvc_carrito.controller;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

  private final CarritoService carritoService;

  public CarritoController(CarritoService carritoService) {
    this.carritoService = carritoService;
  }

  @GetMapping("/{usuarioId}")
  public CarritoDto obtenerCarrito(@PathVariable Long usuarioId) {
    return carritoService.obtenerCarritoPorUsuario(usuarioId);
  }
  // POST/DELETE para agregar/quitar ítems y vaciar carrito
}
