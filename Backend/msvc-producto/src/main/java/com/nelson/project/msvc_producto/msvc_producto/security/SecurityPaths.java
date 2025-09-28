package com.nelson.project.msvc_producto.msvc_producto.security;

/**
 * Centraliza los endpoints públicos usados en la configuración de seguridad.
 */
public final class SecurityPaths {

  public static final String[] PUBLIC_POST = { "/public/**" };
  public static final String[] PUBLIC_GET = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/public/**",
  };

  private SecurityPaths() {}
}
