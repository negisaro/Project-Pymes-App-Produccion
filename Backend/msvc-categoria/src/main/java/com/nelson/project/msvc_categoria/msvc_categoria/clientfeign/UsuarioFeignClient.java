package com.nelson.project.msvc_categoria.msvc_categoria.clientfeign;

import com.nelson.project.msvc_categoria.msvc_categoria.config.FeignClientConfig;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuario", configuration = FeignClientConfig.class)
public interface UsuarioFeignClient {
  @GetMapping("/usuarios/{username}")
  UsuarioDto findByUsername(@PathVariable("username") String username);
}
