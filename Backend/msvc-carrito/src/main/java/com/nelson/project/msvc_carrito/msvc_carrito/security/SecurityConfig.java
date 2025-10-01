package com.nelson.project.msvc_carrito.msvc_carrito.security;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración de seguridad empresarial para el microservicio de carrito.
 *
 * Implementa una estrategia de seguridad robusta incluyendo:
 * - Autenticación JWT con validación de firma
 * - Autorización basada en roles y métodos
 * - CORS configurado para múltiples orígenes
 * - Headers de seguridad avanzados
 * - Protección CSRF deshabilitada para API REST
 * - Gestión de sesiones stateless
 * - Rate limiting por usuario
 * - Auditoría de accesos
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final RateLimitingFilter rateLimitingFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    log.info("🔐 Configurando cadena de filtros de seguridad empresarial");

    http
      // Deshabilitar CSRF para API REST stateless
      .csrf(AbstractHttpConfigurer::disable)
      // Configurar CORS
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      // Configurar gestión de sesiones como stateless
      .sessionManagement(session ->
        session
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .maximumSessions(1)
          .maxSessionsPreventsLogin(false)
      )
      // Configurar headers de seguridad
      .headers(headers ->
        headers
          .frameOptions(frameOptions -> frameOptions.deny()) // Prevenir clickjacking
          .contentTypeOptions(contentTypeOptions -> {}) // Prevenir MIME sniffing
          .httpStrictTransportSecurity(hstsConfig ->
            hstsConfig
              .maxAgeInSeconds(31536000) // 1 año
              .includeSubDomains(true)
              .preload(true)
          )
          .referrerPolicy(referrerPolicy ->
            referrerPolicy.policy(
              ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN
            )
          )
          .addHeaderWriter((request, response) -> {
            // Headers de seguridad adicionales
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("X-Frame-Options", "DENY");
            response.setHeader("X-XSS-Protection", "1; mode=block");
            response.setHeader(
              "Strict-Transport-Security",
              "max-age=31536000; includeSubDomains; preload"
            );
            response.setHeader(
              "Referrer-Policy",
              "strict-origin-when-cross-origin"
            );
            response.setHeader(
              "Permissions-Policy",
              "geolocation=(), microphone=(), camera=()"
            );
          })
      )
      // Configurar manejo de excepciones de autenticación
      .exceptionHandling(ex ->
        ex
          .authenticationEntryPoint(jwtAuthenticationEntryPoint)
          .accessDeniedHandler((request, response, accessDeniedException) -> {
            log.warn(
              "🚫 ACCESS_DENIED | URI: {} | User: {} | Error: {}",
              request.getRequestURI(),
              request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : "anonymous",
              accessDeniedException.getMessage()
            );

            response.setStatus(403);
            response.setContentType("application/json");
            response
              .getWriter()
              .write(
                """
                {
                    "timestamp": "%s",
                    "status": 403,
                    "error": "Acceso Denegado",
                    "message": "No tiene permisos suficientes para acceder a este recurso",
                    "path": "%s"
                }
                """.formatted(
                    java.time.LocalDateTime.now(),
                    request.getRequestURI()
                  )
              );
          })
      )
      // Configurar autorización de endpoints
      .authorizeHttpRequests(authz ->
        authz
          // Endpoints públicos (sin autenticación)
          .requestMatchers(
            "/api/v1/carrito/health",
            "/api/v1/metrics/health",
            "/api/v1/metrics/liveness",
            "/api/v1/metrics/readiness"
          )
          .permitAll()
          // Swagger UI y documentación (público en desarrollo)
          .requestMatchers(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
          )
          .permitAll()
          // Actuator endpoints (solo health público)
          .requestMatchers("/actuator/health")
          .permitAll()
          .requestMatchers("/actuator/**")
          .hasRole("ADMIN")
          // Endpoints administrativos - solo para ADMIN
          .requestMatchers("/api/v1/admin/**")
          .hasRole("ADMIN")
          .requestMatchers(
            HttpMethod.DELETE,
            "/api/v1/carrito/*/limpiar-abandonados"
          )
          .hasRole("ADMIN")
          .requestMatchers(HttpMethod.POST, "/api/v1/carrito/optimizar-bd")
          .hasRole("ADMIN")
          // Endpoints de métricas - diferentes niveles de acceso
          .requestMatchers(HttpMethod.GET, "/api/v1/metrics/business")
          .hasAnyRole("ADMIN", "MANAGER")
          .requestMatchers(HttpMethod.GET, "/api/v1/metrics/info")
          .hasRole("ADMIN")
          // Operaciones de carrito - usuarios autenticados
          .requestMatchers(HttpMethod.GET, "/api/v1/carrito/**")
          .hasRole("USER")
          .requestMatchers(HttpMethod.POST, "/api/v1/carrito/**")
          .hasRole("USER")
          .requestMatchers(HttpMethod.PUT, "/api/v1/carrito/**")
          .hasRole("USER")
          .requestMatchers(HttpMethod.DELETE, "/api/v1/carrito/**")
          .hasRole("USER")
          // Cualquier otra request requiere autenticación
          .anyRequest()
          .authenticated()
      )
      // Agregar filtros en orden: Rate Limiting -> JWT Authentication
      .addFilterBefore(
        rateLimitingFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .addFilterAfter(jwtAuthenticationFilter, RateLimitingFilter.class);

    log.info("✅ Configuración de seguridad empresarial completada");
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    log.info("🌐 Configurando CORS para múltiples orígenes");

    CorsConfiguration configuration = new CorsConfiguration();

    // Orígenes permitidos (ajustar según el entorno)
    configuration.setAllowedOriginPatterns(
      Arrays.asList(
        "http://localhost:*",
        "https://localhost:*",
        "https://*.pymes-app.com",
        "https://*.netlify.app",
        "https://*.vercel.app",
        "https://*.herokuapp.com"
      )
    );

    // Métodos HTTP permitidos
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
    );

    // Headers permitidos
    configuration.setAllowedHeaders(
      Arrays.asList(
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "Accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers",
        "X-Correlation-ID",
        "X-Client-Version",
        "X-Device-Type"
      )
    );

    // Headers expuestos al frontend
    configuration.setExposedHeaders(
      Arrays.asList(
        "X-Correlation-ID",
        "X-Total-Count",
        "X-Total-Pages",
        "X-Current-Page",
        "X-Rate-Limit-Remaining",
        "X-Rate-Limit-Retry-After"
      )
    );

    // Permitir credenciales (cookies, headers de auth)
    configuration.setAllowCredentials(true);

    // Tiempo de caché para preflight requests
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);

    log.info("✅ Configuración CORS completada");
    return source;
  }
}
