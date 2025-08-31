package com.project.nelson.msvc_user_auth.usuario.security.service;

import com.project.nelson.msvc_user_auth.usuario.security.TokenJwtConfig;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio profesional para manejo de JWT.
 * Variables y clave obtenidas desde TokenJwtConfig para uniformidad y seguridad.
 * Métodos de generación, validación, parsing y refresh de tokens JWT.
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    // Clave secreta y configuración obtenida de TokenJwtConfig
    private static final SecretKey SECRET_KEY = TokenJwtConfig.SECRET_KEY;
    private static final int TOKEN_EXPIRATION_MINUTES = 60;

    // ================= MÉTODOS PÚBLICOS PRINCIPALES =================

    /**
     * Genera un JWT válido para el usuario autenticado.
     */
    public String generateToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("email", email);
        claims.put("username", userDetails.getUsername());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MINUTES * 60 * 1000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        logger.info("[JwtService] Token generado para usuario: {}", userDetails.getUsername());
        return token;
    }

    /**
     * Parsea el token y devuelve los claims.
     */
    public Claims parseToken(String token) {
        return extractAllClaims(token);
    }

    /**
     * Refresca el token usando los claims del anterior.
     */
    @SuppressWarnings("unchecked")
    public String refreshToken(String oldToken, String email) {
        Claims claims = extractAllClaims(oldToken);
        String username = claims.get("username", String.class);
        List<String> roles = claims.get("roles", List.class);

        org.springframework.security.core.userdetails.User userDetails =
            new org.springframework.security.core.userdetails.User(
                username, "",
                roles != null ? roles.stream().map(r -> (GrantedAuthority) () -> r).collect(Collectors.toList()) : Collections.emptyList()
            );
        return generateToken(userDetails, userDetails.getAuthorities(), email);
    }

    /**
     * Extrae el username del token JWT.
     */
    public String extractUsername(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (Exception ex) {
            logger.error("[JwtService] Error al extraer username: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Valida si el token es correcto y pertenece al usuario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            boolean valid = username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (!valid) {
                logger.warn("[JwtService] Token inválido para usuario: {}", userDetails.getUsername());
            }
            return valid;
        } catch (Exception ex) {
            logger.error("[JwtService] Error al validar token: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Construye el cuerpo de respuesta para autenticación.
     */
    public Map<String, Object> buildResponseBody(String token, String username, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        body.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return body;
    }

    // ================= MÉTODOS PRIVADOS AUXILIARES =================

    /**
     * Verifica si el token está expirado.
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            boolean expired = expiration.before(new Date());
            if (expired) {
                logger.warn("[JwtService] Token expirado");
            }
            return expired;
        } catch (Exception ex) {
            logger.error("[JwtService] Error al verificar expiración: {}", ex.getMessage());
            return true;
        }
    }

    /**
     * Extrae todos los claims del token JWT.
     */
    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.debug("[JwtService] Claims extraídos: {}", claims);
            return claims;
        } catch (JwtException ex) {
            logger.error("[JwtService] Error al parsear claims: {}", ex.getMessage());
            throw ex;
        }
    }
}