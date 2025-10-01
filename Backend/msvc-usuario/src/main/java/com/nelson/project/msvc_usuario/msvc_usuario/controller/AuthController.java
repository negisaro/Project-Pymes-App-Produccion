package com.nelson.project.msvc_usuario.msvc_usuario.controller;

import static com.nelson.project.msvc_usuario.msvc_usuario.security.TokenJwtConfig.*;

import com.nelson.project.msvc_usuario.msvc_usuario.assembler.AuthResponseAssembler;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.AuthResponseDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.ForgotPasswordRequestDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.LoginDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.ResetPasswordRequestDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.security.service.JwtService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.PasswordRecoveryService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.RefreshTokenService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final PasswordRecoveryService passwordRecoveryService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final AuthResponseAssembler authResponseAssembler;

  public AuthController(
    UsuarioService usuarioService,
    AuthenticationManager authenticationManager,
    JwtService jwtService,
    PasswordRecoveryService passwordRecoveryService,
    RefreshTokenService refreshTokenService,
    AuthResponseAssembler authResponseAssembler
  ) {
    this.usuarioService = usuarioService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.passwordRecoveryService = passwordRecoveryService;
    this.refreshTokenService = refreshTokenService;
    this.authResponseAssembler = authResponseAssembler;
  }

  private static final Logger logger = LoggerFactory.getLogger(
    AuthController.class
  );

  /**
   * Login y generación del JWT.
   */
  @Operation(summary = "Login de usuario")
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(
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

      var refresh = refreshTokenService.generate(usuario.getUsername());
      var authResponse = authResponseAssembler.from(
        token,
        jwtService.parseToken(token).getExpiration(),
        usuario.getUsername(),
        authentication.getAuthorities()
      );
      authResponseAssembler.attachRefresh(
        authResponse,
        refresh.getToken(),
        refresh.getExpiryDate()
      );
      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(authResponse);
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
  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
    logger.info("[AuthController] Refresh token solicitado (rotación)");
    String refreshTokenStr = body.get("refreshToken");
    if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
      return ResponseEntity.badRequest()
        .body(Map.of("error", "refreshToken requerido"));
    }
    try {
      var refreshEntityOpt = refreshTokenService.findEntityByToken(
        refreshTokenStr
      );
      if (refreshEntityOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
          Map.of("error", "refreshToken no encontrado")
        );
      }
      var refreshEntity = refreshTokenService.validateUsableOrThrow(
        refreshEntityOpt.get()
      );
      String username = refreshEntity.getUsername();
      Optional<UsuarioDto> usuarioOpt = usuarioService.findByUsername(username);
      if (usuarioOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
          Map.of("error", "Usuario no encontrado")
        );
      }
      UsuarioDto usuario = usuarioOpt.get();
      // Construir userDetails mínimo para regenerar access token
      org.springframework.security.core.userdetails.User principal =
        new org.springframework.security.core.userdetails.User(
          usuario.getUsername(),
          "",
          Collections.emptyList()
        );
      String newAccess = jwtService.generateToken(
        principal,
        principal.getAuthorities(),
        usuario.getEmail()
      );
      var newRefresh = refreshTokenService.rotate(refreshEntity);
      var resp = authResponseAssembler.from(
        newAccess,
        jwtService.parseToken(newAccess).getExpiration(),
        usuario.getUsername(),
        principal.getAuthorities()
      );
      authResponseAssembler.attachRefresh(
        resp,
        newRefresh.getToken(),
        newRefresh.getExpiryDate()
      );
      return ResponseEntity.ok(resp);
    } catch (IllegalStateException ise) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        Map.of("error", ise.getMessage())
      );
    } catch (Exception ex) {
      logger.error("[AuthController] Error en rotación refresh token", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        Map.of("error", "Error interno")
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
      // Extraer roles del token para reconstruir authorities ligeras (sin re-autenticar)
      Claims claims = jwtService.parseToken(token);
      String rolesStr = claims.get("roles", String.class);
      Collection<
        org.springframework.security.core.GrantedAuthority
      > authorities = rolesStr == null || rolesStr.isBlank()
        ? Collections.emptyList()
        : Arrays.stream(rolesStr.split(","))
          .map(String::trim)
          .filter(r -> !r.isEmpty())
          .map(r -> (org.springframework.security.core.GrantedAuthority) () -> r
          )
          .collect(Collectors.toList());
      AuthResponseDto authResponse = authResponseAssembler.from(
        token,
        jwtService.parseToken(token).getExpiration(),
        usuario.getUsername(),
        authorities
      );
      logger.info(
        "[AuthController] Respuesta enviada al frontend: {}",
        authResponse
      );
      return ResponseEntity.ok()
        .header(HEADER_AUTHORIZATION, PREFIX_TOKEN + token)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .body(authResponse);
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
    passwordRecoveryService.requestReset(request.getEmail());
    return ResponseEntity.accepted()
      .body(
        Map.of(
          "mensaje",
          "Si el email existe, se enviará un correo con instrucciones para restablecer la contraseña."
        )
      );
  }

  @Operation(summary = "Valida un token de recuperación de contraseña")
  @GetMapping("/reset-password/validate")
  public ResponseEntity<?> validateResetToken(@RequestParam String token) {
    passwordRecoveryService.validateTokenOrThrow(token);
    return ResponseEntity.ok(Map.of("valido", true));
  }

  @Operation(summary = "Restablece la contraseña usando un token")
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(
    @RequestBody @Valid ResetPasswordRequestDto request
  ) {
    passwordRecoveryService.resetPassword(
      request.getToken(),
      request.getNewPassword()
    );
    return ResponseEntity.ok(
      Map.of("mensaje", "Contraseña restablecida correctamente.")
    );
  }
}
