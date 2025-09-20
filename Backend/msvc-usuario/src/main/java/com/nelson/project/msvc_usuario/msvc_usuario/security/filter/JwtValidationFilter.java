package com.nelson.project.msvc_usuario.msvc_usuario.security.filter;

import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtValidationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtValidationFilter(
    JwtService jwtService,
    UserDetailsService userDetailsService
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @SuppressWarnings("null")
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
      path.equals("/auth/forgot-password") ||
      path.equals("/auth/reset-password") ||
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
      // ...existing code...
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
        // ...existing code...
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "INVALID_TOKEN");
        body.put("message", "Token inválido o expirado");
        body.put("detalle", "Token inválido para usuario: " + username);
        response
          .getWriter()
          .write(
            new com.fasterxml.jackson.databind.ObjectMapper()
              .writeValueAsString(body)
          );
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
