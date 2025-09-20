package com.nelson.project.msvc_usuario.msvc_usuario.config.configsecurity;

import com.nelson.project.msvc_usuario.msvc_usuario.security.JpaUserDetailsService;
import com.nelson.project.msvc_usuario.msvc_usuario.security.SecurityPaths;
import com.nelson.project.msvc_usuario.msvc_usuario.security.filter.JwtAuthenticationFilter;
import com.nelson.project.msvc_usuario.msvc_usuario.security.filter.JwtValidationFilter;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtService jwtService;
  private final UsuarioService usuarioService;
  private final JpaUserDetailsService userDetailsService;

  public SecurityConfig(
    AuthenticationConfiguration authenticationConfiguration,
    JwtService jwtService,
    UsuarioService usuarioService,
    JpaUserDetailsService userDetailsService
  ) {
    this.authenticationConfiguration = authenticationConfiguration;
    this.jwtService = jwtService;
    this.usuarioService = usuarioService;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
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
      .addFilter(
        new JwtAuthenticationFilter(
          authenticationManager(),
          jwtService,
          usuarioService
        )
      )
      .addFilterBefore(
        new JwtValidationFilter(jwtService, userDetailsService),
        JwtAuthenticationFilter.class
      )
      .build();
  }
}
