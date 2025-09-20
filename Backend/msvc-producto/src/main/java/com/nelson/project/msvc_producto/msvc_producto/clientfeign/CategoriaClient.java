package com.nelson.project.msvc_producto.msvc_producto.clientfeign;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.CategoriaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-categoria")
public interface CategoriaClient {
  @GetMapping("/categorias/{id}")
  CategoriaDTO getCategoriaById(@PathVariable("id") Long id);
}
