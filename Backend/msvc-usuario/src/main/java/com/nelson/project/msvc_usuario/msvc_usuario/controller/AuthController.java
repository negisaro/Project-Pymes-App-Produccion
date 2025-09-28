package com.nelson.project.msvc_usuario.msvc_usuario.controller;

import static com.nelson.project.msvc_usuario.msvc_usuario.security.TokenJwtConfig.*;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.mapper.LoginResponseMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.ForgotPasswordRequestDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.LoginDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.LoginResponseDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.ResetPasswordRequestDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/public/auth")
@Tag(
  name = "Autenticación",
  description = "Endpoints para autenticación y gestión de sesión JWT"
)
public class AuthController {

  private final UsuarioService usuarioService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(
    UsuarioService usuarioService,
    AuthenticationManager authenticationManager,
    JwtService jwtService
  ) {
    this.usuarioService = usuarioService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  private static final Logger logger = LoggerFactory.getLogger(
    AuthController.class
  );

  /**
   * Login y generación del JWT.
   */
  @Operation(summary = "Login de usuario")
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(
    @RequestBody @Valid LoginDto loginDto
  ) {
    logger.info(
      "[AuthController] Intento de login para usuario: {}",
      loginDto.getUsername()
    );
    try {
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
          loginDto.getUsername(),
          loginDto.getPassword()
        );
      Authentication authentication = authenticationManager.authenticate(
        authToken
      );
      UserDetails principal = (UserDetails) authentication.getPrincipal();
      String username = principal.getUsername();

      Optional<UsuarioDto> usuarioOpt = usuarioService.findByUsername(username);
      if (usuarioOpt.isEmpty()) {
        throw new CustomException(
          "Usuario no encontrado",
          HttpStatus.UNAUTHORIZED.value(),
          ErrorCodes.USER_NOT_FOUND
        );
      }
      UsuarioDto usuario = usuarioOpt.get();

      String token = jwtService.generateToken(
        principal,
        authentication.getAuthorities(),
        usuario.getEmail()
      );

      LoginResponseDto responseDto =
        LoginResponseMapper.INSTANCE.usuarioDtoToLoginResponseDto(usuario);
      responseDto.setToken(token);
      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(responseDto);
    } catch (BadCredentialsException ex) {
      throw new CustomException(
        "Credenciales inválidas",
        HttpStatus.UNAUTHORIZED.value(),
        ErrorCodes.INVALID_PASSWORD,
        ex.getMessage()
      );
    } catch (Exception ex) {
      logger.error(
        "[AuthController] Error interno en login: {}",
        ex.getMessage(),
        ex
      );
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Operation(summary = "Registrar usuario público")
  @PostMapping("/register")
  public ResponseEntity<UsuarioDto> register(
    @RequestBody @Valid UsuarioCreateDto usuarioCreateDto
  ) {
    try {
      UsuarioDto usuarioSaved = usuarioService.saveWithRoleUser(
        usuarioCreateDto
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSaved);
    } catch (Exception ex) {
      throw new CustomException(
        "Error al registrar usuario",
        500,
        "REGISTER_USER_ERROR",
        ex.getMessage()
      );
    }
  }

  /**
   * Logout de usuario
   */
  @Operation(summary = "Logout de usuario")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout() {
    logger.info("[AuthController] Logout solicitado");
    Map<String, String> body = Map.of(
      "message",
      "Sesión cerrada correctamente."
    );
    return ResponseEntity.ok(body);
  }

  /**
   * Refresh token JWT.
   */
  @Operation(summary = "Refresh token")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(
    @RequestBody Map<String, String> requestBody
  ) {
    String oldToken = requestBody.get("token");
    logger.info("[AuthController] Refresh token solicitado");
    try {
      if (oldToken == null || oldToken.isBlank()) {
        logger.warn("[AuthController] Token no proporcionado para refresh");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          Map.of("error", "Token requerido")
        );
      }
      String email = "";
      try {
        Claims claims = jwtService.parseToken(oldToken);
        email = claims.get("email", String.class);
      } catch (Exception e) {
        logger.warn(
          "[AuthController] No se pudo extraer el email del token para refresh"
        );
      }
      String newToken = jwtService.refreshToken(oldToken, email);
      return ResponseEntity.ok(Map.of("token", newToken));
    } catch (Exception ex) {
      logger.error(
        "[AuthController] Error al refrescar token: {}",
        ex.getMessage(),
        ex
      );
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Token inválido")
      );
    }
  }

  /**
   * Verifica el estado de sesión y devuelve el usuario completo si el token es válido
   */
  @Operation(
    summary = "Verifica el estado de sesión y devuelve el usuario completo si el token es válido"
  )
  @GetMapping("/check-token")
  public ResponseEntity<?> checkToken(
    @RequestHeader(name = "Authorization") String authHeader
  ) {
    logger.info("[AuthController] Verificando token de sesión");
    String token = authHeader.replace("Bearer ", "");
    try {
      String username = jwtService.extractUsername(token);
      Optional<UsuarioDto> usuarioOpt = usuarioService.findByUsername(username);
      if (usuarioOpt.isEmpty()) {
        logger.warn(
          "[AuthController] Usuario no encontrado al verificar token: {}",
          username
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
          Map.of("error", "Usuario no encontrado")
        );
      }
      UsuarioDto usuario = usuarioOpt.get();
      LoginResponseDto responseDto =
        LoginResponseMapper.INSTANCE.usuarioDtoToLoginResponseDto(usuario);
      responseDto.setToken(token);
      logger.info(
        "[AuthController] Respuesta enviada al frontend: {}",
        responseDto
      );
      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(responseDto);
    } catch (Exception ex) {
      logger.warn("[AuthController] Token inválido al verificar sesión");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", "Token inválido")
      );
    }
  }

  @Operation(summary = "Solicita recuperación de contraseña (envía email)")
  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(
    @RequestBody @Valid ForgotPasswordRequestDto request
  ) {
    usuarioService.sendPasswordResetToken(request.getEmail());
    return ResponseEntity.ok(
      Map.of(
        "mensaje",
        "Si el email existe, se ha enviado un correo con instrucciones para restablecer la contraseña."
      )
    );
  }

  @Operation(summary = "Restablece la contraseña usando un token")
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(
    @RequestBody @Valid ResetPasswordRequestDto request
  ) {
    boolean ok = usuarioService.resetPassword(
      request.getToken(),
      request.getNewPassword()
    );
    if (ok) {
      return ResponseEntity.ok(
        Map.of("mensaje", "Contraseña restablecida correctamente.")
      );
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("mensaje", "No se pudo restablecer la contraseña.")
      );
    }
  }
}
