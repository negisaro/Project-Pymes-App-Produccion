package com.nelson.project.msvc_usuario.msvc_usuario.security;

/**
 * Centraliza los endpoints públicos usados en la configuración de seguridad.
 */
public final class SecurityPaths {

  public static final String[] PUBLIC_POST = {
    "/auth/login",
    "/usuarios/register",
    "/auth/forgot-password",
    "/auth/reset-password",
  };
  public static final String[] PUBLIC_GET = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
  };

  private SecurityPaths() {}
}
