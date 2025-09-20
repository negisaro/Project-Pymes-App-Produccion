package com.nelson.project.msvc_producto.msvc_producto.clientfeign;

import com.nelson.project.msvc_producto.msvc_producto.config.FeignClientConfig;
import com.nelson.project.msvc_producto.msvc_producto.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuario", configuration = FeignClientConfig.class)
public interface UsuarioFeignClient {
  @GetMapping("/usuarios/{username}")
  UsuarioDto findByUsername(@PathVariable("username") String username);
}
