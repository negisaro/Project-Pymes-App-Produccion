package com.nelson.project.msvc_proveedor.msvc_proveedor.security.filter;

import com.nelson.project.msvc_proveedor.msvc_proveedor.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
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
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    String path = request.getServletPath();
    if (
      path.startsWith("/public") ||
      path.startsWith("/swagger-ui") ||
      path.startsWith("/v3/api-docs")
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      jwt = authHeader.substring(7);
      username = jwtService.extractUsername(jwt);

      if (
        username != null &&
        SecurityContextHolder.getContext().getAuthentication() == null
      ) {
        List<String> roles = jwtService.extractRoles(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(
          username
        );

        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            roles
              .stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList())
          );
        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
      filterChain.doFilter(request, response);
    } catch (io.jsonwebtoken.JwtException ex) {
      // Lanza la excepción para que la maneje el filtro global
      throw ex;
    }
  }
}
