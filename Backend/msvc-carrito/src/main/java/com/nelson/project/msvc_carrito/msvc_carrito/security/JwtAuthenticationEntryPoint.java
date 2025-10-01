package com.nelson.project.msvc_carrito.msvc_carrito.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Entry point personalizado para manejar errores de autenticación JWT.
 *
 * Se ejecuta cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Proporciona respuestas de error estructuradas y logging de auditoría.
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException
  ) throws IOException, ServletException {
    String correlationId = request.getHeader("X-Correlation-ID");
    String requestUri = request.getRequestURI();
    String clientIp = getClientIp(request);
    String userAgent = request.getHeader("User-Agent");

    // Log de auditoría de acceso no autorizado
    log.warn(
      "🚫 UNAUTHORIZED_ACCESS | URI: {} | ClientIP: {} | UserAgent: {} | Error: {} | CorrelationId: {}",
      requestUri,
      clientIp,
      userAgent,
      authException.getMessage(),
      correlationId
    );

    // Configurar respuesta HTTP
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    if (correlationId != null) {
      response.setHeader("X-Correlation-ID", correlationId);
    }

    // Crear respuesta de error estructurada
    Map<String, Object> errorResponse = Map.of(
      "timestamp",
      LocalDateTime.now().toString(),
      "status",
      HttpStatus.UNAUTHORIZED.value(),
      "error",
      "No Autorizado",
      "message",
      "Acceso denegado: credenciales de autenticación requeridas",
      "errorCode",
      "AUTHENTICATION_REQUIRED",
      "path",
      requestUri,
      "method",
      request.getMethod(),
      "correlationId",
      correlationId != null ? correlationId : "N/A",
      "suggestions",
      List.of(
        "Incluya un token JWT válido en el header Authorization",
        "Use el formato: Authorization: Bearer <token>",
        "Verifique que el token no haya expirado",
        "Asegúrese de estar autenticado antes de acceder a este recurso"
      ),
      "documentationLinks",
      List.of(
        "https://api-docs.pymes-app.com/authentication",
        "https://api-docs.pymes-app.com/jwt-guide"
      )
    );

    // Escribir respuesta JSON
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (xRealIp != null && !xRealIp.isEmpty()) {
      return xRealIp;
    }

    return request.getRemoteAddr();
  }
}
