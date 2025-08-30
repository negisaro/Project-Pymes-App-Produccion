package com.project.nelson.msvc_user_auth.usuario.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {

  private static final Logger logger = LoggerFactory.getLogger(
    JwtAuthenticationFilter.class
  );

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public JwtAuthenticationFilter(
    AuthenticationManager authenticationManager,
    JwtService jwtService
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    setFilterProcessesUrl("/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    try {
      Usuario user = new ObjectMapper()
        .readValue(request.getInputStream(), Usuario.class);
      logger.info(
        "[JwtAuthFilter] Login recibido: usuario={}, ip={}, headers={}",
        user.getUsername(),
        request.getRemoteAddr(),
        request.getHeaderNames()
      );
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
          user.getUsername(),
          user.getPassword()
        );
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      logger.error("[JwtAuthFilter] Error leyendo credenciales", e);
      throw new RuntimeException("Error leyendo credenciales de usuario", e);
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    org.springframework.security.core.userdetails.User user =
      (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
    String username = user.getUsername();
    String token = jwtService.generateToken(
      user,
      authResult.getAuthorities(),
      username
    );

    logger.info(
      "[JwtAuthFilter] Login exitoso: usuario={}, token={}",
      username,
      token
    );

    Map<String, Object> body = jwtService.buildResponseBody(
      token,
      username,
      authResult.getAuthorities()
    );

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  ) throws IOException, ServletException {
    logger.warn("[JwtAuthFilter] Login fallido: {}", failed.getMessage());
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Credenciales inválidas");
    body.put("details", failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
  }
}
