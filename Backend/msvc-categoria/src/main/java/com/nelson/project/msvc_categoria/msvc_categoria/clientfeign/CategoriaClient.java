package com.nelson.project.msvc_categoria.msvc_categoria.clientfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;

@FeignClient(name = "msvc-categoria", url = "http://localhost:8085")
public interface CategoriaClient {
    @GetMapping("/categorias/{id}")
    CategoriaDTO getCategoriaById(@PathVariable("id") Long id);
}
