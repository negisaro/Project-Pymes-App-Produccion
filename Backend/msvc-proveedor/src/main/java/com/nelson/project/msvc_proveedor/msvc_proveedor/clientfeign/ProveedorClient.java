package com.nelson.project.msvc_proveedor.msvc_proveedor.clientfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;

@FeignClient(name = "msvc-proveedor", url = "http://localhost:8085")
public interface ProveedorClient {
    @GetMapping("/proveedores/{id}")
    ProveedorDTO getProveedorById(@PathVariable("id") Long id);
}