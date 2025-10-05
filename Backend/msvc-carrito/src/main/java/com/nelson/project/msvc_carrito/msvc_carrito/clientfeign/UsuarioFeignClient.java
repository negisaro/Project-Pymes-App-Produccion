package com.nelson.project.msvc_carrito.msvc_carrito.clientfeign;

import com.nelson.project.msvc_carrito.msvc_carrito.config.configfeign.FeignClientConfig;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuario", configuration = FeignClientConfig.class)
public interface UsuarioFeignClient {
  @GetMapping("/usuarios/{id}")
  UsuarioDto getUsuarioById(@PathVariable("id") Long id);

  @GetMapping("/usuarios/{username}")
  UsuarioDto findByUsername(@PathVariable("username") String username);
}
