package com.project.nelson.msvc_user_auth.usuario.controller;

import static com.project.nelson.msvc_user_auth.usuario.security.TokenJwtConfig.*;

import com.project.nelson.msvc_user_auth.usuario.model.dtos.LoginDto;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.LoginResponseDto;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.RolDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Usuario;
import com.project.nelson.msvc_user_auth.usuario.security.service.JwtService;
import com.project.nelson.msvc_user_auth.usuario.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
   * Login y generación del JWT.
   */
  @Operation(summary = "Login de usuario")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
    logger.info("Intento de login para usuario: {}", loginDto.getUsername());
    try {
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
      Authentication authentication = authenticationManager.authenticate(authToken);
      UserDetails principal = (UserDetails) authentication.getPrincipal();
      String username = principal.getUsername();

      Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
      if (usuarioOpt.isEmpty()) {
        logger.warn("Usuario no encontrado: {}", username);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Usuario no encontrado"));
      }
      Usuario usuario = usuarioOpt.get();

      String token = jwtService.generateToken(principal, authentication.getAuthorities(), usuario.getEmail());

      List<RolDto> rolesDto = usuario.getRoles().stream()
        .map(role -> new RolDto(role.getId(), role.getName(), role.isActivo()))
        .toList();

      LoginResponseDto responseDto = new LoginResponseDto();
      responseDto.setId(usuario.getId());
      responseDto.setName(usuario.getName());
      responseDto.setLastname(usuario.getLastname());
      responseDto.setEmail(usuario.getEmail());
      responseDto.setUsername(usuario.getUsername());
      responseDto.setActive(usuario.isActive());
      responseDto.setRoles(rolesDto);
      responseDto.setToken(token);

      Map<String, Object> body = Map.of(
        "usuario", responseDto,
        "token", token
      );

      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(body);

    } catch (BadCredentialsException ex) {
      logger.warn("Login fallido para usuario: {} - Credenciales inválidas", loginDto.getUsername());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", "Credenciales inválidas", "message", ex.getMessage()));
    } catch (Exception ex) {
      logger.error("Error interno en login: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Error interno", "message", ex.getMessage()));
    }
  }

  /**
   * Logout de usuario
   */
  @Operation(summary = "Logout de usuario")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout() {
    logger.info("Logout solicitado");
    Map<String, String> body = Map.of("message", "Sesión cerrada correctamente.");
    return ResponseEntity.ok(body);
  }

  /**
   * Refresh token JWT.
   */
  @Operation(summary = "Refresh token")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> requestBody) {
    String oldToken = requestBody.get("token");
    logger.info("Refresh token solicitado");
    try {
      if (oldToken == null || oldToken.isBlank()) {
        logger.warn("Token no proporcionado para refresh");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          Map.of("error", "Token requerido")
        );
      }
      String email = "";
      try {
        Claims claims = jwtService.parseToken(oldToken);
        email = claims.get("email", String.class);
      } catch (Exception e) {
        logger.warn("No se pudo extraer el email del token para refresh");
      }
      String newToken = jwtService.refreshToken(oldToken, email);
      return ResponseEntity.ok(Map.of("token", newToken));
    } catch (Exception ex) {
      logger.error("Error al refrescar token: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Token inválido")
      );
    }
  }

  /**
   * Verifica el estado de sesión y devuelve el usuario completo si el token es válido
   */
  @Operation(summary = "Verifica el estado de sesión y devuelve el usuario completo si el token es válido")
  @GetMapping("/check-token")
  public ResponseEntity<?> checkToken(@RequestHeader(name = "Authorization") String authHeader) {
    logger.info("Verificando token de sesión");
    String token = authHeader.replace("Bearer ", "");
    try {
      String username = jwtService.extractUsername(token);
      Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
      if (usuarioOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Usuario no encontrado"));
      }
      Usuario usuario = usuarioOpt.get();
      List<RolDto> rolesDto = usuario.getRoles().stream()
        .map(role -> new RolDto(role.getId(), role.getName(), role.isActivo()))
        .toList();

      LoginResponseDto responseDto = new LoginResponseDto();
      responseDto.setId(usuario.getId());
      responseDto.setName(usuario.getName());
      responseDto.setLastname(usuario.getLastname());
      responseDto.setEmail(usuario.getEmail());
      responseDto.setUsername(usuario.getUsername());
      responseDto.setActive(usuario.isActive());
      responseDto.setRoles(rolesDto);
      responseDto.setToken(token);

      Map<String, Object> body = Map.of(
        "usuario", responseDto,
        "token", token
      );
      return ResponseEntity.ok(body);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", "Token inválido"));
    }
  }

  /**
   * Envia email para recuperar la contraseña
   */
  @Operation(summary = "Envia email para recuperar la contraseña")
  @PostMapping("/forgot-password")
  public ResponseEntity<?> sendResetPasswordEmail(@RequestBody Map<String, String> body) {
    String username = body.get("username");
    Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
    if (usuarioOpt.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", "Usuario no encontrado"));
    }
    Usuario usuario = usuarioOpt.get();
    // Aquí iría la lógica real para generar token y enviar email:
    // usuarioService.sendResetPasswordEmail(usuario);

    return ResponseEntity.ok(Map.of("message", "Email de recuperación enviado"));
  }

  /**
   * Restablece la contraseña usando el token recibido por email
   */
  @Operation(summary = "Restablece la contraseña usando el token recibido por email")
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    String token = body.get("token");
    String newPassword = body.get("password");
    // Aquí iría la lógica real para validar el token y cambiar la contraseña:
    boolean success = usuarioService.resetPassword(token, newPassword);
    if (success) {
      return ResponseEntity.ok(Map.of("message", "Contraseña restablecida correctamente"));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "Token inválido o expirado"));
    }
  }
}