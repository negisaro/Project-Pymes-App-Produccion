package com.nelson.project.msvc_carrito.msvc_carrito.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
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
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de Rate Limiting que protege la aplicación contra abuso y ataques DDoS.
 *
 * Se ejecuta antes del filtro JWT para aplicar límites basados en:
 * - IP address para usuarios no autenticados
 * - Usuario ID para usuarios autenticados
 * - Endpoint específico
 * - Headers especiales de rate limiting
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Component
@Order(1) // Se ejecuta antes que el filtro JWT
@RequiredArgsConstructor
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

  private final RateLimitingService rateLimitingService;
  private final ObjectMapper objectMapper;

  // Endpoints exentos de rate limiting
  private static final List<String> EXEMPT_ENDPOINTS = List.of(
    "/api/v1/carrito/health",
    "/api/v1/metrics/health",
    "/api/v1/metrics/liveness",
    "/swagger-ui",
    "/v3/api-docs",
    "/actuator/health"
  );

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String requestUri = request.getRequestURI();
    String method = request.getMethod();
    String correlationId = request.getHeader("X-Correlation-ID");

    // Saltar rate limiting para endpoints exentos
    if (isExemptEndpoint(requestUri)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Determinar identificador para rate limiting
    String identifier = determineIdentifier(request);
    String endpoint = requestUri;

    try {
      // Verificar rate limit antes de procesar la request
      RateLimitingService.RateLimitStatus status =
        rateLimitingService.getRateLimitStatus(identifier, endpoint);

      if (!status.isAllowed()) {
        log.warn(
          "🚫 RATE_LIMIT_EXCEEDED | Identifier: {} | Endpoint: {} | Method: {} | ClientIP: {} | CorrelationId: {}",
          identifier,
          endpoint,
          method,
          getClientIp(request),
          correlationId
        );

        sendRateLimitExceededResponse(response, status, correlationId);
        return;
      }

      // Incrementar contador y agregar headers de rate limiting
      rateLimitingService.incrementCounter(identifier, endpoint);
      addRateLimitHeaders(response, status);

      log.debug(
        "✅ RATE_LIMIT_OK | Identifier: {} | Remaining: {} | Endpoint: {} | CorrelationId: {}",
        identifier,
        status.getRemaining(),
        endpoint,
        correlationId
      );

      // Continuar con el siguiente filtro
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error(
        "💥 RATE_LIMIT_ERROR | Identifier: {} | Endpoint: {} | Error: {} | CorrelationId: {}",
        identifier,
        endpoint,
        e.getMessage(),
        correlationId,
        e
      );

      // En caso de error, permitir la request (fail-open)
      filterChain.doFilter(request, response);
    }
  }

  private boolean isExemptEndpoint(String requestUri) {
    return EXEMPT_ENDPOINTS.stream()
      .anyMatch(exempt -> requestUri.startsWith(exempt));
  }

  private String determineIdentifier(HttpServletRequest request) {
    // Primero intentar obtener el usuario de un token JWT si existe
    String authHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      String userId = extractUserIdFromToken(authHeader.substring(7));
      if (userId != null) {
        return "user:" + userId;
      }
    }

    // Fallback a IP address
    String clientIp = getClientIp(request);
    return "ip:" + clientIp;
  }

  private String extractUserIdFromToken(String token) {
    try {
      // Nota: En una implementación completa, aquí decodificarías el JWT
      // Por ahora retornamos null para usar IP como identificador
      return null;
    } catch (Exception e) {
      log.debug("No se pudo extraer userId del token: {}", e.getMessage());
      return null;
    }
  }

  private void addRateLimitHeaders(
    HttpServletResponse response,
    RateLimitingService.RateLimitStatus status
  ) {
    response.setHeader("X-RateLimit-Limit", String.valueOf(status.getLimit()));
    response.setHeader(
      "X-RateLimit-Remaining",
      String.valueOf(status.getRemaining())
    );
    response.setHeader(
      "X-RateLimit-Reset",
      String.valueOf(status.getResetTime())
    );
  }

  private void sendRateLimitExceededResponse(
    HttpServletResponse response,
    RateLimitingService.RateLimitStatus status,
    String correlationId
  ) throws IOException {
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    if (correlationId != null) {
      response.setHeader("X-Correlation-ID", correlationId);
    }

    // Agregar headers de rate limiting
    addRateLimitHeaders(response, status);
    response.setHeader("Retry-After", String.valueOf(status.getResetTime()));

    Map<String, Object> errorResponse = Map.of(
      "timestamp",
      LocalDateTime.now().toString(),
      "status",
      HttpStatus.TOO_MANY_REQUESTS.value(),
      "error",
      "Límite de Requests Excedido",
      "message",
      String.format(
        "Has excedido el límite de %d requests por minuto",
        status.getLimit()
      ),
      "errorCode",
      "RATE_LIMIT_EXCEEDED",
      "correlationId",
      correlationId != null ? correlationId : "N/A",
      "rateLimitInfo",
      Map.of(
        "limit",
        status.getLimit(),
        "remaining",
        status.getRemaining(),
        "resetInSeconds",
        status.getResetTime()
      ),
      "suggestions",
      List.of(
        String.format(
          "Espere %d segundos antes de realizar más requests",
          status.getResetTime()
        ),
        "Implemente exponential backoff en su cliente",
        "Contacte soporte si necesita límites más altos"
      )
    );

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(xForwardedFor)) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (StringUtils.hasText(xRealIp)) {
      return xRealIp;
    }

    String xClientIp = request.getHeader("X-Client-IP");
    if (StringUtils.hasText(xClientIp)) {
      return xClientIp;
    }

    return request.getRemoteAddr();
  }
}
