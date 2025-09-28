package com.nelson.project.msvc_usuario.msvc_usuario.service;

import com.nelson.project.msvc_usuario.msvc_usuario.mapper.RefreshTokenMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RefreshTokenDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.RefreshToken;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

  @Value("${jwt.refresh.expiration.ms:604800000}") // 7 días por defecto
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final RefreshTokenMapper refreshTokenMapper;

  public RefreshTokenService(
    RefreshTokenRepository refreshTokenRepository,
    RefreshTokenMapper refreshTokenMapper
  ) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTokenMapper = refreshTokenMapper;
  }

  public RefreshTokenDto createRefreshToken(String username) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUsername(username);
    refreshToken.setExpiryDate(
      Instant.now().plusMillis(refreshTokenDurationMs)
    );
    refreshToken.setToken(UUID.randomUUID().toString());
    RefreshToken saved = refreshTokenRepository.save(refreshToken);
    return refreshTokenMapper.toDto(saved);
  }

  public Optional<RefreshTokenDto> findByToken(String token) {
    return refreshTokenRepository
      .findByToken(token)
      .map(refreshTokenMapper::toDto);
  }

  public boolean isExpired(RefreshTokenDto tokenDto) {
    return tokenDto.getExpiryDate().isBefore(Instant.now());
  }

  public void deleteByUsername(String username) {
    refreshTokenRepository.deleteByUsername(username);
  }
}
