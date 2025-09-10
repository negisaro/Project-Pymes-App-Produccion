package com.project.nelson.msvc_user_auth.usuario.clientfeign;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.UsuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario", url = "http://localhost:8082")
public interface UsuarioClient {
  @GetMapping("/usuarios/{id}")
  UsuarioDto getId(@PathVariable("id") Long id);
}
