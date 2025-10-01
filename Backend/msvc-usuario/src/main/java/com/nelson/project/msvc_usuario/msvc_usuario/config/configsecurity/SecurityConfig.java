package com.nelson.project.msvc_usuario.msvc_usuario.config.configsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.assembler.AuthResponseAssembler;
import com.nelson.project.msvc_usuario.msvc_usuario.security.JpaUserDetailsService;
import com.nelson.project.msvc_usuario.msvc_usuario.security.SecurityPaths;
import com.nelson.project.msvc_usuario.msvc_usuario.security.filter.JwtAuthenticationFilter;
import com.nelson.project.msvc_usuario.msvc_usuario.security.filter.JwtValidationFilter;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad central.
 * Inyecta ObjectMapper en el filtro de autenticación para soportar Java Time y formato consistente.
 */
@Configuration
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtService jwtService;
  private final JpaUserDetailsService userDetailsService;
  private final AuthResponseAssembler authResponseAssembler;
  private final ObjectMapper objectMapper;

  public SecurityConfig(
    AuthenticationConfiguration authenticationConfiguration,
    JwtService jwtService,
    JpaUserDetailsService userDetailsService,
    AuthResponseAssembler authResponseAssembler,
    ObjectMapper objectMapper
  ) {
    this.authenticationConfiguration = authenticationConfiguration;
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.authResponseAssembler = authResponseAssembler;
    this.objectMapper = objectMapper;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter(
      authenticationManager(),
      jwtService,
      authResponseAssembler,
      objectMapper
    );

    return http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(authz ->
        authz
          .requestMatchers(HttpMethod.POST, SecurityPaths.PUBLIC_POST)
          .permitAll()
          .requestMatchers(HttpMethod.GET, SecurityPaths.PUBLIC_GET)
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .addFilter(authFilter)
      .addFilterBefore(
        new JwtValidationFilter(jwtService, userDetailsService),
        JwtAuthenticationFilter.class
      )
      .build();
  }
}
