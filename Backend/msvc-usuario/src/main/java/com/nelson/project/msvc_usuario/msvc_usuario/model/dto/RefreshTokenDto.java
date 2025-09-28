package com.nelson.project.msvc_usuario.msvc_usuario.model.dto;

import java.time.Instant;

public class RefreshTokenDto {

  private String token;
  private String username;
  private Instant expiryDate;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }
}
