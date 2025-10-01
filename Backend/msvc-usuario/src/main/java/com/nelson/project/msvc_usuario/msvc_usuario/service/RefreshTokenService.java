package com.nelson.project.msvc_usuario.msvc_usuario.service;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.RefreshToken;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RefreshTokenRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.security.TokenJwtConfig;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RefreshTokenService {

  private final RefreshTokenRepository repository;
  private final TokenJwtConfig tokenJwtConfig;

  public RefreshTokenService(
    RefreshTokenRepository repository,
    TokenJwtConfig tokenJwtConfig
  ) {
    this.repository = repository;
    this.tokenJwtConfig = tokenJwtConfig;
  }

  public RefreshToken generate(String username) {
    log.debug(
      "[RefreshTokenService] Generando refresh token para usuario={}",
      username
    );
    RefreshToken rt = new RefreshToken();
    rt.setUsername(username);
    rt.setToken(UUID.randomUUID().toString());
    rt.setExpiryDate(
      Instant.now()
        .plus(tokenJwtConfig.getRefreshExpirationMinutes(), ChronoUnit.MINUTES)
    );
    RefreshToken saved = repository.save(rt);
    log.trace(
      "[RefreshTokenService] Token generado id={}, expira={} ",
      saved.getId(),
      saved.getExpiryDate()
    );
    return saved;
  }

  public Optional<RefreshToken> findEntityByToken(String token) {
    return repository.findByToken(token);
  }

  public RefreshToken validateUsableOrThrow(RefreshToken rt) {
    if (rt.isRevoked()) {
      log.debug(
        "[RefreshTokenService] Token revocado username={} token={}",
        rt.getUsername(),
        rt.getToken()
      );
      throw new CustomException(
        "Refresh token revocado",
        400,
        ErrorCodes.INVALID_TOKEN
      );
    }
    if (rt.getExpiryDate().isBefore(Instant.now())) {
      log.debug(
        "[RefreshTokenService] Token expirado username={} token={}",
        rt.getUsername(),
        rt.getToken()
      );
      throw new CustomException(
        "Refresh token expirado",
        400,
        ErrorCodes.INVALID_TOKEN
      );
    }
    return rt;
  }

  public RefreshToken rotate(RefreshToken oldToken) {
    log.debug(
      "[RefreshTokenService] Rotando refresh token para username={}",
      oldToken.getUsername()
    );
    oldToken.setRevoked(true);
    repository.save(oldToken);
    RefreshToken nuevo = generate(oldToken.getUsername());
    log.trace(
      "[RefreshTokenService] Rotación completada oldToken={} newToken={}",
      oldToken.getToken(),
      nuevo.getToken()
    );
    return nuevo;
  }

  public void revokeAllForUser(String username) {
    log.info(
      "[RefreshTokenService] Revocando todos los refresh tokens para usuario={}",
      username
    );
    repository.deleteByUsername(username);
  }
}
