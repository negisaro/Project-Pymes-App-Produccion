package com.nelson.project.msvc_producto.msvc_producto.clientfeign;

import com.nelson.project.msvc_producto.msvc_producto.model.dto.ProveedorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-proveedor", url = "http://localhost:8084")
public interface ProveedorClient {
  @GetMapping("/proveedores/{id}")
  ProveedorDTO getProveedorById(@PathVariable("id") Long id);
}
