package com.nelson.project.msvc_carrito.msvc_carrito.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI 3.0 para el microservicio de carrito de compras.
 *
 * Proporciona documentación interactiva completa del API REST incluyendo:
 * - Información detallada del servicio y versión
 * - Esquemas de seguridad JWT
 * - Configuración de servidores de desarrollo y producción
 * - Metadatos de contacto y licencia
 * - Ejemplos de uso y respuestas
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Configuration
public class OpenApiConfig {

  @Value("${server.port:8080}")
  private int serverPort;

  @Value("${spring.application.name:msvc-carrito}")
  private String applicationName;

  @Value("${info.app.version:2.0.0}")
  private String appVersion;

  @Bean
  public OpenAPI carritoOpenAPI() {
    return new OpenAPI()
      .info(createApiInfo())
      .servers(createServers())
      .addSecurityItem(createSecurityRequirement())
      .components(createComponents());
  }

  private Info createApiInfo() {
    return new Info()
      .title("Microservicio Carrito de Compras - API REST")
      .description(
        """
        ## API empresarial para gestión avanzada de carritos de compras

        Este microservicio proporciona funcionalidades completas para la gestión de carritos
        de compras en plataformas de e-commerce empresariales, incluyendo:

        ### 🛒 Funcionalidades Principales
        - **Gestión completa de carritos**: Crear, consultar, modificar y eliminar carritos
        - **Operaciones avanzadas de items**: Agregar, actualizar cantidades, eliminar items individuales
        - **Validaciones robustas**: Stock, límites, reglas de negocio personalizables
        - **Sistema de descuentos**: Códigos promocionales, cupones, descuentos automáticos
        - **Análisis y métricas**: Estadísticas de uso, reportes, recomendaciones
        - **Operaciones masivas**: Transferencia de carritos, operaciones en lote

        ### 🔒 Seguridad
        - Autenticación JWT obligatoria
        - Autorización basada en roles (USER/ADMIN)
        - Validación de propiedad de recursos
        - Auditoría completa de operaciones

        ### 📊 Características Técnicas
        - **Arquitectura**: Microservicios con Spring Boot 3.5.5
        - **Base de datos**: MySQL 8.0 con Redis para cache distribuido
        - **Integración**: Feign Client para comunicación con otros servicios
        - **Observabilidad**: Logging estructurado, métricas, health checks
        - **Calidad**: Validaciones Jakarta, manejo robusto de errores

        ### 🚀 Escalabilidad y Rendimiento
        - Cache distribuido multi-nivel
        - Patrones de retry con backoff exponencial
        - Transacciones optimizadas con niveles de aislamiento
        - Paginación eficiente para grandes volúmenes

        ### 📝 Documentación
        - OpenAPI 3.0 con ejemplos completos
        - Códigos de error estandarizados
        - Guías de integración y mejores prácticas
        """
      )
      .version(appVersion)
      .contact(
        new Contact()
          .name("Equipo de Desarrollo PYMES")
          .email("desarrollo@pymes-app.com")
          .url("https://github.com/tu-organizacion/msvc-carrito")
      )
      .license(
        new License()
          .name("MIT License")
          .url("https://opensource.org/licenses/MIT")
      )
      .termsOfService("https://pymes-app.com/terms");
  }

  private List<Server> createServers() {
    return List.of(
      new Server()
        .url("http://localhost:" + serverPort)
        .description("Servidor de Desarrollo Local"),
      new Server()
        .url("https://api-dev.pymes-app.com")
        .description("Servidor de Desarrollo"),
      new Server()
        .url("https://api-staging.pymes-app.com")
        .description("Servidor de Staging"),
      new Server()
        .url("https://api.pymes-app.com")
        .description("Servidor de Producción")
    );
  }

  private SecurityRequirement createSecurityRequirement() {
    return new SecurityRequirement().addList("JWT Bearer Token");
  }

  private Components createComponents() {
    return new Components()
      .addSecuritySchemes(
        "JWT Bearer Token",
        new SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
          .description(
            """
            Token JWT requerido para autenticación.

            **Formato**: `Bearer <token>`

            **Ejemplo**: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

            ### Obtener Token
            1. Autentícate en el servicio de usuarios: `POST /api/v1/auth/login`
            2. Incluye el token en el header: `Authorization: Bearer <token>`

            ### Roles Disponibles
            - **USER**: Acceso a operaciones de carrito propio
            - **ADMIN**: Acceso completo incluyendo estadísticas globales

            ### Duración
            - Los tokens tienen una validez de 24 horas
            - Refresh tokens disponibles para renovación automática
            """
          )
      );
  }
}
