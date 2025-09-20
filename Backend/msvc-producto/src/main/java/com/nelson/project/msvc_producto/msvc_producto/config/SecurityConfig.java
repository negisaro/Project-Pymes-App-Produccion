package com.nelson.project.msvc_producto.msvc_producto.config;

import com.nelson.project.msvc_producto.msvc_producto.security.UserDetailsServiceImpl;
import com.nelson.project.msvc_producto.msvc_producto.security.filter.JwtValidationFilter;
import com.nelson.project.msvc_producto.msvc_producto.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public JwtValidationFilter jwtValidationFilter(
    JwtService jwtService,
    UserDetailsServiceImpl userDetailsService
  ) {
    return new JwtValidationFilter(jwtService, userDetailsService);
  }

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
          .requestMatchers(
            "/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/productos/list",
            "/uploads/**",
            "/productos/list/**"
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(
        jwtValidationFilter,
        UsernamePasswordAuthenticationFilter.class
      );

    return http.build();
  }
}
