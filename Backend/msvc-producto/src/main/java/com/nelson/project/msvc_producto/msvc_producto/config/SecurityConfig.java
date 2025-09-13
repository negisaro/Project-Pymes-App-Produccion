package com.nelson.project.msvc_producto.msvc_producto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.nelson.project.msvc_producto.msvc_producto.security.UserDetailsServiceImpl;
import com.nelson.project.msvc_producto.msvc_producto.security.filter.JwtValidationFilter;
import com.nelson.project.msvc_producto.msvc_producto.security.service.JwtService;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtService jwtService,
                                                   UserDetailsServiceImpl userDetailsService) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtValidationFilter(jwtService, userDetailsService),
                             JwtValidationFilter.class);

        return http.build();
    }
}
