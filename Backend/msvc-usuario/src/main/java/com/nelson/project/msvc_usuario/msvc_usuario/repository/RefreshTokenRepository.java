package com.nelson.project.msvc_usuario.msvc_usuario.repository;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository
  extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  void deleteByUsername(String username);
}
