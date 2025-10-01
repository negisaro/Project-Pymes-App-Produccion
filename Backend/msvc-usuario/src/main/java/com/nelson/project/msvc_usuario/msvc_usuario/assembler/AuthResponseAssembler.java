package com.nelson.project.msvc_usuario.msvc_usuario.assembler;

import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.AuthResponseDto;
import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Assembler para construir {@link AuthResponseDto} evitando lógica duplicada en controladores/servicios.
 * Extrae expiración desde los claims cuando se proporcionan; si no, permite inyectar una fecha explícita.
 */
@Component
public class AuthResponseAssembler {

  public AuthResponseDto from(
    String accessToken,
    Date expiration,
    String username,
    Collection<? extends GrantedAuthority> authorities
  ) {
    List<String> roles = authorities
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());
    return AuthResponseDto.builder()
      .accessToken(accessToken)
      .expiresAt(expiration.toInstant())
      .username(username)
      .roles(roles)
      .build();
  }

  public AuthResponseDto fromClaims(
    String accessToken,
    Claims claims,
    Collection<? extends GrantedAuthority> authorities
  ) {
    Date exp = claims.getExpiration();
    String username = claims.get("username", String.class);
    return from(accessToken, exp, username, authorities);
  }

  public AuthResponseDto attachRefresh(
    AuthResponseDto base,
    String refreshToken,
    Instant refreshExpiresAt
  ) {
    base.setRefreshToken(refreshToken);
    base.setRefreshExpiresAt(refreshExpiresAt);
    return base;
  }
}
