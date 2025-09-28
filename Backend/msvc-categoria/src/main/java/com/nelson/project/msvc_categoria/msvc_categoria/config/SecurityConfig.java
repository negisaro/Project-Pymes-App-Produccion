package com.nelson.project.msvc_categoria.msvc_categoria.config;

import com.nelson.project.msvc_categoria.msvc_categoria.security.SecurityPaths;
import com.nelson.project.msvc_categoria.msvc_categoria.security.UserDetailsServiceImpl;
import com.nelson.project.msvc_categoria.msvc_categoria.security.filter.JwtValidationFilter;
import com.nelson.project.msvc_categoria.msvc_categoria.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración profesional y moderna de seguridad para el microservicio.
 */
@Configuration
public class SecurityConfig {

  /**
   * Bean para el filtro de validación JWT.
   */
  @Bean
  public JwtValidationFilter jwtValidationFilter(
    JwtService jwtService,
    UserDetailsServiceImpl userDetailsService
  ) {
    return new JwtValidationFilter(jwtService, userDetailsService);
  }

  /**
   * Configuración principal de la cadena de filtros de seguridad.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    JwtValidationFilter jwtValidationFilter
  ) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(authz ->
        authz
          .requestMatchers(SecurityPaths.PUBLIC_GET)
          .permitAll()
          .requestMatchers(SecurityPaths.PUBLIC_POST)
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(
        jwtValidationFilter,
        UsernamePasswordAuthenticationFilter.class
      );

    // Puedes agregar configuración de CORS, manejo de excepciones, etc. aquí

    return http.build();
  }
}
