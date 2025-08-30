package com.project.nelson.msvc_user_auth.usuario.security.filter;

import com.project.nelson.msvc_user_auth.usuario.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtValidationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(
    JwtValidationFilter.class
  );

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtValidationFilter(
    JwtService jwtService,
    UserDetailsService userDetailsService
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String path = request.getServletPath();
    if (
      path.equals("/auth/login") ||
      path.equals("/usuarios/register") ||
      path.startsWith("/swagger-ui") ||
      path.startsWith("/v3/api-docs")
    ) {
      // Exenta rutas públicas y Swagger
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");
    String jwt = null;
    String username = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      username = jwtService.extractUsername(jwt);
      logger.info("[JwtValidationFilter] Token recibido: {}", jwt);
      logger.info("[JwtValidationFilter] Usuario extraído: {}", username);
    }

    if (
      username != null &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info(
          "[JwtValidationFilter] Autenticación para usuario: {} establecida en SecurityContext",
          username
        );
      } else {
        logger.warn(
          "[JwtValidationFilter] Token inválido para usuario: {}",
          username
        );
      }
    }

    filterChain.doFilter(request, response);
  }
}