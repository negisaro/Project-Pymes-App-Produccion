package com.nelson.project.msvc_usuario.msvc_usuario.repository;

import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.PasswordResetToken;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository
  extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByToken(String token);
  List<PasswordResetToken> findByUsuarioAndUsedIsFalse(Usuario usuario);
  List<PasswordResetToken> findByUsuarioAndExpiresAtBefore(
    Usuario usuario,
    LocalDateTime time
  );
}
