package com.nelson.project.msvc_carrito.msvc_carrito.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para el microservicio de carrito.
 *
 * Establece configuraciones globales para:
 * - Registro de interceptors para logging y monitoreo
 * - Configuración CORS para frontend web
 * - Configuraciones adicionales de Spring MVC
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final RequestLoggingInterceptor requestLoggingInterceptor;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry
      .addInterceptor(requestLoggingInterceptor)
      .addPathPatterns("/api/**")
      .excludePathPatterns(
        "/api/v1/carrito/health",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator/**"
      );
  }

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry
      .addMapping("/api/**")
      .allowedOriginPatterns(
        "http://localhost:*",
        "https://localhost:*",
        "https://*.pymes-app.com",
        "https://*.netlify.app",
        "https://*.vercel.app"
      )
      .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
      .allowedHeaders("*")
      .allowCredentials(true)
      .exposedHeaders("X-Correlation-ID", "X-Total-Count", "X-Total-Pages")
      .maxAge(3600);
  }
}
