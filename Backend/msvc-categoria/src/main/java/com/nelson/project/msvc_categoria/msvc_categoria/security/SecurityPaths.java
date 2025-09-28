package com.nelson.project.msvc_categoria.msvc_categoria.security;

/**
 * Centraliza los endpoints públicos usados en la configuración de seguridad.
 */
public final class SecurityPaths {

  public static final String[] PUBLIC_POST = { "/public/**" };
  public static final String[] PUBLIC_GET = { "/public/**" };

  private SecurityPaths() {}
}
