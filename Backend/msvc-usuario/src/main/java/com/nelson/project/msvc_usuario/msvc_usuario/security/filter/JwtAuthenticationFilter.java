package com.nelson.project.msvc_usuario.msvc_usuario.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.assembler.AuthResponseAssembler;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.AuthResponseDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.security.SecurityErrorUtil;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Filtro de autenticación que procesa login y devuelve un AuthResponseDto.
 * Ahora usa el ObjectMapper inyectado (no crea instancias manuales).
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final AuthResponseAssembler authResponseAssembler;
  private final ObjectMapper objectMapper;

  public JwtAuthenticationFilter(
    AuthenticationManager authenticationManager,
    JwtService jwtService,
    AuthResponseAssembler authResponseAssembler,
    ObjectMapper objectMapper
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.authResponseAssembler = authResponseAssembler;
    this.objectMapper = objectMapper;
    setFilterProcessesUrl("/public/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return null;
    }
    try {
      Usuario user = objectMapper.readValue(request.getInputStream(), Usuario.class);
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      try {
        SecurityErrorUtil.writeError(
          response,
          HttpServletResponse.SC_BAD_REQUEST,
          ErrorCodes.VALIDATION_ERROR,
          "Error leyendo credenciales",
            e.getMessage()
        );
      } catch (IOException ignored) {}
      return null;
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    org.springframework.security.core.userdetails.User principal =
      (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

    String username = principal.getUsername();
    String token = jwtService.generateToken(principal, authResult.getAuthorities(), username);

    Claims claims = jwtService.parseToken(token);
    Date expiration = claims.getExpiration();

    AuthResponseDto body = authResponseAssembler.from(
      token,
      expiration,
      username,
      authResult.getAuthorities()
    );

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  ) throws IOException, ServletException {
    SecurityErrorUtil.writeError(
      response,
      HttpServletResponse.SC_UNAUTHORIZED,
      ErrorCodes.INVALID_CREDENTIALS,
      "Credenciales inválidas",
      failed.getMessage()
    );
  }
}