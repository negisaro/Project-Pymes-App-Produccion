package com.nelson.project.msvc_producto.msvc_producto.clientfeign;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario", url = "http://localhost:8081") // Ajusta la URL base
public interface UsuarioFeignClient {
  @GetMapping("/usuarios/{username}")
  UsuarioDto findByUsername(@RequestParam("username") String username);
  // Puedes agregar otros métodos según los endpoints de tu msvc usuario
}
