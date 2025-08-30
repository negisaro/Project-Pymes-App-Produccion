package com.project.nelson.msvc_user_auth.usuario.config.configsecurity;

import com.project.nelson.msvc_user_auth.usuario.security.JpaUserDetailsService;
import com.project.nelson.msvc_user_auth.usuario.security.filter.JwtAuthenticationFilter;
import com.project.nelson.msvc_user_auth.usuario.security.filter.JwtValidationFilter;
import com.project.nelson.msvc_user_auth.usuario.security.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Endpoints públicos POST
    private static final String[] PUBLIC_POST = {
        "/auth/login",
        "/usuarios/register",
    };

    // Endpoints públicos GET
    private static final String[] PUBLIC_GET = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
    };

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        logger.info("[SecurityConfig] Exponiendo bean AuthenticationManager");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("[SecurityConfig] Exponiendo bean PasswordEncoder (BCrypt)");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("[SecurityConfig] Configurando SecurityFilterChain");
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                .anyRequest().authenticated()
            )
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
            .addFilterBefore(new JwtValidationFilter(jwtService, userDetailsService), JwtAuthenticationFilter.class)
            .build();
    }
}