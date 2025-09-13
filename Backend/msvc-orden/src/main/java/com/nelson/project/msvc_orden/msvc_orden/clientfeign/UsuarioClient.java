package com.nelson.project.msvc_orden.msvc_orden.clientfeign;

import com.nelson.project.msvc_orden.msvc_orden.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario", url = "http://localhost:8081")
public interface UsuarioClient {
  @GetMapping("/usuarios/{id}")
  UsuarioDto getUsuarioById(@PathVariable("id") Long id);
}
