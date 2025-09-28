package com.nelson.project.msvc_usuario.msvc_usuario.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// ...existing imports...
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UsuarioService usuarioService;

  public JwtAuthenticationFilter(
    AuthenticationManager authenticationManager,
    JwtService jwtService,
    UsuarioService usuarioService
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.usuarioService = usuarioService;
    setFilterProcessesUrl("/public/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    // Ignorar OPTIONS para evitar error de Jackson en preflight CORS
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return null;
    }
    try {
      Usuario user = new ObjectMapper()
        .readValue(request.getInputStream(), Usuario.class);
      // ...existing code...
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
          user.getUsername(),
          user.getPassword()
        );
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      // ...existing code...
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

    // ...existing code...

    // Obtener datos completos del usuario
    UsuarioDto usuario = usuarioService.findByUsername(username).orElse(null);
    Map<String, Object> body = jwtService.buildResponseBody(
      token,
      usuario != null ? usuario.getId() : null,
      username,
      usuario != null ? usuario.getName() : null,
      usuario != null ? usuario.getLastname() : null,
      usuario != null ? usuario.getEmail() : null,
      usuario != null ? usuario.getActive() : null,
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
    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "INVALID_CREDENTIALS");
    body.put("message", "Credenciales inválidas");
    body.put("detalle", failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
  }
}
