package com.nelson.project.msvc_proveedor.msvc_proveedor.clientfeign;

import com.nelson.project.msvc_proveedor.msvc_proveedor.config.FeignClientConfig;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario", configuration = FeignClientConfig.class)
public interface UsuarioFeignClient {
  @GetMapping("/usuarios/{username}")
  UsuarioDto findByUsername(@PathVariable("username") String username);
}
