package com.nelson.project.msvc_carrito.msvc_carrito.clientfeign;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8085")
public interface ProductoClient {
  @GetMapping("/productos/{id}")
  ProductoDto getProductoById(@PathVariable("id") Long id);
}
