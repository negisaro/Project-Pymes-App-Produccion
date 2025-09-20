package com.nelson.project.msvc_producto.msvc_producto.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

/**
 * Componente para inicializar directorios necesarios al arrancar la aplicación.
 */
@Component
public class DirectoryInitializer {

  private static final Logger logger = LoggerFactory.getLogger(
    DirectoryInitializer.class
  );

  @PostConstruct
  public void initDirectories() {
    try {
      Path uploadsDir = Paths.get("uploads");

      if (!Files.exists(uploadsDir)) {
        Files.createDirectories(uploadsDir);
        logger.info(
          "✅ Directorio uploads creado: {}",
          uploadsDir.toAbsolutePath()
        );
      } else {
        logger.info(
          "✅ Directorio uploads ya existe: {}",
          uploadsDir.toAbsolutePath()
        );
      }

      // Verificar permisos de escritura
      if (!Files.isWritable(uploadsDir)) {
        logger.error(
          "❌ No se puede escribir en el directorio uploads: {}",
          uploadsDir.toAbsolutePath()
        );
      } else {
        logger.info(
          "✅ Permisos de escritura verificados para directorio uploads"
        );
      }
    } catch (Exception e) {
      logger.error(
        "❌ Error al crear directorio uploads: {}",
        e.getMessage(),
        e
      );
    }
  }
}
