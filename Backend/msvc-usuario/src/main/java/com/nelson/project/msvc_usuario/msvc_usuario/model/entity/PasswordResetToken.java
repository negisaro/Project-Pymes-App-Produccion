package com.nelson.project.msvc_usuario.msvc_usuario.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(
  name = "password_reset_tokens",
  indexes = {
    @Index(name = "idx_prt_token", columnList = "token", unique = true),
    @Index(name = "idx_prt_user", columnList = "user_id"),
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 120)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private Usuario usuario;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "used", nullable = false)
  private boolean used;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public boolean isExpired() {
    return expiresAt.isBefore(LocalDateTime.now());
  }
}
