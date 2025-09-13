package com.nelson.project.msvc_orden.msvc_orden.clientfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nelson.project.msvc_orden.msvc_orden.model.dto.ProductoDto;


@FeignClient(name = "product-service", url = "http://localhost:8085")
public interface ProductoClient {
  @GetMapping("/productos/{id}")
  ProductoDto getProductoById(@PathVariable("id") Long id);
}
