package com.project.nelson.msvc_carrito.carrito.controller;

import com.project.nelson.msvc_carrito.carrito.model.dto.CarritoDto;
import com.project.nelson.msvc_carrito.carrito.service.CarritoService;
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