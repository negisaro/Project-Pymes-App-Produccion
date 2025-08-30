package com.project.nelson.msvc_user_auth.usuario.controller;

import static com.project.nelson.msvc_user_auth.usuario.security.TokenJwtConfig.*;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.LoginDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.security.service.JwtService;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación JWT.
 * Expone endpoints para login y logout.
 * Implementación profesional y escalable.
 */
@RestController
@RequestMapping("/auth")
@Tag(
  name = "Autenticación",
  description = "Endpoints para autenticación y gestión de sesión JWT"
)
public class AuthController {

  @Autowired
  private UsuarioService usuarioService;

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  /**
   * Endpoint para login y generación de JWT.
   *
   * @param userDto DTO con credenciales de usuario
   * @return ResponseEntity con token y datos del usuario
   */
  @Operation(
    summary = "Login de usuario",
    description = "Autentica al usuario y retorna un JWT junto con los datos básicos.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "DTO con credenciales de usuario",
      required = true,
      content = @io.swagger.v3.oas.annotations.media.Content(
        examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
          value = "{\"username\": \"admin\", \"password\": \"admin123\"}"
        )
      )
    ),
    responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Login exitoso",
        content = @io.swagger.v3.oas.annotations.media.Content(
          mediaType = "application/json",
          examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
            value = "{\"token\": \"jwt...\", \"id\": 1, \"name\": \"Juan\", \"lastName\": \"Pérez\", \"email\": \"juan@correo.com\", \"active\": true, \"roles\": [{\"id\": 1, \"name\": \"ADMIN\", \"active\": true}]}"
          )
        )
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "Credenciales inválidas"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error interno"
      ),
    }
  )
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
    logger.info("Intento de login para usuario: {}", loginDto.getUsername());
    try {
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
          loginDto.getUsername(),
          loginDto.getPassword()
        );
      Authentication authentication = authenticationManager.authenticate(authToken);
      logger.info("Autenticación exitosa para usuario: {}", loginDto.getUsername());
      UserDetails principal = (UserDetails) authentication.getPrincipal();
      String username = principal.getUsername();
      Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

      // Buscar el usuario real en la base de datos y el email
      Usuario usuario = usuarioService.findByUsername(username).orElse(null);
      String email = usuario != null ? usuario.getEmail() : "";

      // Genera el token incluyendo claims "rol" y "email"
      String token = jwtService.generateToken(principal, roles, email);

      Map<String, Object> body = jwtService.buildResponseBody(token, username, roles);

      // Agregar datos adicionales del usuario
      if (usuario != null) {
        body.put("id", usuario.getId());
        body.put("name", usuario.getName());
        body.put("lastname", usuario.getLastname());
        body.put("email", usuario.getEmail());
        body.put("active", usuario.isActive());
        body.put("username", usuario.getUsername());
        body.put(
          "roles",
          usuario
            .getRoles()
            .stream()
            .map(rol -> {
              Map<String, Object> rolMap = new HashMap<>();
              rolMap.put("id", rol.getId());
              rolMap.put("name", rol.getName());
              rolMap.put("active", rol.isActivo());
              return rolMap;
            })
            .toList()
        );
      } else {
        body.put("id", null);
        body.put("name", "");
        body.put("lastName", "");
        body.put("email", "");
        body.put("active", true);
        body.put("username", username);
        body.put("roles", java.util.Collections.emptyList());
      }

      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(body);
    } catch (BadCredentialsException ex) {
      logger.warn(
        "Login fallido para usuario: {} - Credenciales inválidas",
        loginDto.getUsername()
      );
      Map<String, String> error = new HashMap<>();
      error.put("error", "Credenciales inválidas");
      error.put("message", ex.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    } catch (Exception ex) {
      logger.error("Error interno en login: {}", ex.getMessage(), ex);
      Map<String, String> error = new HashMap<>();
      error.put("error", "Error interno");
      error.put("message", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  /**
   * Endpoint para cerrar sesión (logout).
   *
   * @return Mensaje de cierre de sesión
   */
  @Operation(
    summary = "Logout de usuario",
    description = "Cierra la sesión del usuario actual."
  )
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout() {
    logger.info("Logout solicitado");
    Map<String, String> body = new HashMap<>();
    body.put("message", "Sesión cerrada correctamente.");
    return ResponseEntity.ok(body);
  }

  /**
   * Endpoint para refrescar el token JWT.
   *
   * @param requestBody debe contener el token actual
   * @return nuevo token JWT
   */
  @Operation(
    summary = "Refresh token",
    description = "Renueva el JWT si es válido.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Token actual",
      required = true,
      content = @io.swagger.v3.oas.annotations.media.Content(
        examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
          value = "{\"token\": \"jwt...\"}"
        )
      )
    ),
    responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Token renovado"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "Token inválido"
      ),
    }
  )
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(
    @RequestBody Map<String, String> requestBody
  ) {
    String oldToken = requestBody.get("token");
    logger.info("Refresh token solicitado");
    try {
      if (oldToken == null || oldToken.isBlank()) {
        logger.warn("Token no proporcionado para refresh");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          Map.of("error", "Token requerido")
        );
      }
      // Extraer el email del token viejo
      String email = "";
      try {
        Claims claims = jwtService.parseToken(oldToken);
        email = claims.get("email", String.class);
      } catch (Exception e) {
        logger.warn("No se pudo extraer el email del token para refresh");
      }

      // Validar y renovar el token incluyendo el email
      String newToken = jwtService.refreshToken(oldToken, email);
      return ResponseEntity.ok(Map.of("token", newToken));
    } catch (Exception ex) {
      logger.error("Error al refrescar token: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Token inválido")
      );
    }
  }
}
