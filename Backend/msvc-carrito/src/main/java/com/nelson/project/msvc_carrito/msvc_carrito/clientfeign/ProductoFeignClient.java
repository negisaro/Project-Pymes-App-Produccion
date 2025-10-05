package com.nelson.project.msvc_carrito.msvc_carrito.clientfeign;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-producto")
public interface ProductoFeignClient {
  @GetMapping("/productos/{id}")
  ProductoDto getProductoById(@PathVariable("id") Long id);
}
