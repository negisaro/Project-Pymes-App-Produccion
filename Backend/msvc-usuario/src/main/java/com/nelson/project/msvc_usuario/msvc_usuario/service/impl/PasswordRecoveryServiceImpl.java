package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.PasswordResetToken;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.PasswordResetTokenRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.UsuarioRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.service.EmailService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.PasswordRecoveryService;
import com.nelson.project.msvc_usuario.msvc_usuario.service.password.PasswordPolicy;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final PasswordPolicy passwordPolicy;

  @Value("${password.reset.expiration-minutes:30}")
  private int expirationMinutes;

  @Value("${password.reset.max-active-tokens:3}")
  private int maxActiveTokens;

  private static final SecureRandom random = new SecureRandom();

  @Override
  @Transactional
  public void requestReset(String email) {
    if (email == null || email.isBlank()) {
      return; // Silencioso
    }
    usuarioRepository
      .findByEmail(email)
      .ifPresentOrElse(
        usuario -> {
          limpiarTokensExpirados(usuario);
          limitarTokensActivos(usuario);
          String token = generarTokenSeguro();
          PasswordResetToken prt = PasswordResetToken.builder()
            .token(token)
            .usuario(usuario)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
            .used(false)
            .build();
          tokenRepository.save(prt);
          try {
            emailService.sendPasswordResetToken(
              usuario.getEmail(),
              token,
              expirationMinutes
            );
          } catch (Exception e) {
            log.error(
              "[PasswordRecoveryService] Error enviando email: {}",
              e.getMessage(),
              e
            );
            throw new CustomException(
              "No se pudo enviar el email de recuperación.",
              500,
              ErrorCodes.EMAIL_SEND_ERROR
            );
          }
        },
        () ->
          log.info(
            "[PasswordRecoveryService] Solicitud reset para email no registrado (silenciado)"
          )
      );
  }

  @Override
  @Transactional(readOnly = true)
  public void validateTokenOrThrow(String token) {
    PasswordResetToken prt = tokenRepository
      .findByToken(token)
      .orElseThrow(() ->
        new CustomException("Token inválido", 400, ErrorCodes.INVALID_TOKEN)
      );
    if (prt.isExpired()) {
      throw new CustomException(
        "Token expirado",
        400,
        ErrorCodes.TOKEN_EXPIRED
      );
    }
    if (prt.isUsed()) {
      throw new CustomException(
        "Token ya utilizado",
        400,
        ErrorCodes.TOKEN_USED
      );
    }
  }

  @Override
  @Transactional
  public void resetPassword(String token, String newPassword) {
    if (token == null || token.isBlank()) {
      throw new CustomException(
        "Token requerido",
        400,
        ErrorCodes.INVALID_TOKEN
      );
    }
    passwordPolicy.validate(newPassword);
    PasswordResetToken prt = tokenRepository
      .findByToken(token)
      .orElseThrow(() ->
        new CustomException("Token inválido", 400, ErrorCodes.INVALID_TOKEN)
      );
    if (prt.isExpired()) {
      throw new CustomException(
        "Token expirado",
        400,
        ErrorCodes.TOKEN_EXPIRED
      );
    }
    if (prt.isUsed()) {
      throw new CustomException(
        "Token ya utilizado",
        400,
        ErrorCodes.TOKEN_USED
      );
    }
    Usuario usuario = prt.getUsuario();
    usuario.updatePassword(passwordEncoder.encode(newPassword));
    prt.setUsed(true);
    tokenRepository.save(prt);
    usuarioRepository.save(usuario);
  }

  private void limpiarTokensExpirados(Usuario usuario) {
    List<PasswordResetToken> expirados =
      tokenRepository.findByUsuarioAndExpiresAtBefore(
        usuario,
        LocalDateTime.now()
      );
    if (!expirados.isEmpty()) {
      tokenRepository.deleteAll(expirados);
    }
  }

  private void limitarTokensActivos(Usuario usuario) {
    List<PasswordResetToken> activos =
      tokenRepository.findByUsuarioAndUsedIsFalse(usuario);
    if (activos.size() >= maxActiveTokens) {
      // Estrategia: eliminar el más antiguo
      activos.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
      PasswordResetToken oldest = activos.get(0);
      tokenRepository.delete(oldest);
    }
  }

  private String generarTokenSeguro() {
    // Mayor entropía que un UUID simple.
    byte[] bytes = new byte[32];
    random.nextBytes(bytes);
    return (
      Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) +
      "-" +
      UUID.randomUUID()
    );
  }
}
