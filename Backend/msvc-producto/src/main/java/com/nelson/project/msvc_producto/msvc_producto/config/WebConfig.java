package com.nelson.project.msvc_producto.msvc_producto.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para servir archivos estáticos.
 * Permite acceder a las imágenes subidas a través de URLs.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @SuppressWarnings("null")
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Configurar para servir archivos estáticos desde el directorio uploads
    Path uploadPath = Paths.get("uploads");
    String uploadDir = uploadPath.toFile().getAbsolutePath();

    registry
      .addResourceHandler("/uploads/**")
      .addResourceLocations("file:" + uploadDir + "/");
  }
}
