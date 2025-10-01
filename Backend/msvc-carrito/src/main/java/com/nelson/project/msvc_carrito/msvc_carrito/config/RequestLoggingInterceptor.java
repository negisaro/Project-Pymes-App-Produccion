package com.nelson.project.msvc_carrito.msvc_carrito.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Interceptor para logging detallado de requests y responses HTTP.
 *
 * Proporciona trazabilidad completa de todas las operaciones del API:
 * - Logging estructurado de requests entrantes
 * - Captura de responses salientes
 * - Medición de tiempos de respuesta
 * - Generación de IDs de correlación únicos
 * - Información contextual para auditoría y debugging
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
  private static final String START_TIME_ATTRIBUTE = "startTime";
  private static final String CORRELATION_ID_ATTRIBUTE = "correlationId";

  @Override
  public boolean preHandle(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler
  ) {
    long startTime = System.currentTimeMillis();
    String correlationId = getOrGenerateCorrelationId(request);

    // Guardar atributos para uso posterior
    request.setAttribute(START_TIME_ATTRIBUTE, startTime);
    request.setAttribute(CORRELATION_ID_ATTRIBUTE, correlationId);

    // Agregar correlation ID al response header
    response.setHeader(CORRELATION_ID_HEADER, correlationId);

    // Log de request entrante
    logIncomingRequest(request, correlationId);

    return true;
  }

  @Override
  public void afterCompletion(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler,
    @Nullable Exception ex
  ) {
    long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
    String correlationId = (String) request.getAttribute(
      CORRELATION_ID_ATTRIBUTE
    );
    long executionTime = System.currentTimeMillis() - startTime;

    // Log de response saliente
    logOutgoingResponse(request, response, correlationId, executionTime, ex);
  }

  private String getOrGenerateCorrelationId(HttpServletRequest request) {
    String correlationId = request.getHeader(CORRELATION_ID_HEADER);
    if (correlationId == null || correlationId.trim().isEmpty()) {
      correlationId = UUID.randomUUID().toString();
    }
    return correlationId;
  }

  private void logIncomingRequest(
    HttpServletRequest request,
    String correlationId
  ) {
    String clientIp = getClientIp(request);
    String userAgent = request.getHeader("User-Agent");
    String contentType = request.getContentType();

    log.info(
      "🔵 REQUEST_START | CorrelationId: {} | Method: {} | URI: {} | ClientIP: {} | UserAgent: {} | ContentType: {} | Timestamp: {}",
      correlationId,
      request.getMethod(),
      request.getRequestURI(),
      clientIp,
      userAgent,
      contentType,
      LocalDateTime.now()
    );

    // Log de parámetros de query (sin valores sensibles)
    if (!request.getParameterMap().isEmpty()) {
      log.debug(
        "📝 REQUEST_PARAMS | CorrelationId: {} | Parameters: {}",
        correlationId,
        sanitizeParameters(request.getParameterMap())
      );
    }

    // Log de request body si está disponible (solo para debugging en desarrollo)
    if (
      log.isDebugEnabled() && request instanceof ContentCachingRequestWrapper
    ) {
      ContentCachingRequestWrapper cachingRequest =
        (ContentCachingRequestWrapper) request;
      byte[] content = cachingRequest.getContentAsByteArray();
      if (content.length > 0) {
        String body = new String(content, StandardCharsets.UTF_8);
        log.debug(
          "📄 REQUEST_BODY | CorrelationId: {} | Body: {}",
          correlationId,
          sanitizeRequestBody(body)
        );
      }
    }
  }

  private void logOutgoingResponse(
    HttpServletRequest request,
    HttpServletResponse response,
    String correlationId,
    long executionTime,
    Exception ex
  ) {
    String status = ex != null ? "ERROR" : "SUCCESS";
    int httpStatus = response.getStatus();
    String contentType = response.getContentType();

    log.info(
      "🟢 REQUEST_END | CorrelationId: {} | Status: {} | HttpStatus: {} | ExecutionTime: {}ms | ContentType: {} | URI: {} | Timestamp: {}",
      correlationId,
      status,
      httpStatus,
      executionTime,
      contentType,
      request.getRequestURI(),
      LocalDateTime.now()
    );

    // Log adicional para errores
    if (ex != null) {
      log.error(
        "❌ REQUEST_ERROR | CorrelationId: {} | Exception: {} | Message: {}",
        correlationId,
        ex.getClass().getSimpleName(),
        ex.getMessage()
      );
    }

    // Métricas de performance
    if (executionTime > 5000) { // > 5 segundos
      log.warn(
        "🐌 SLOW_REQUEST | CorrelationId: {} | ExecutionTime: {}ms | URI: {}",
        correlationId,
        executionTime,
        request.getRequestURI()
      );
    } else if (executionTime > 1000) { // > 1 segundo
      log.info(
        "⚠️ MEDIUM_RESPONSE_TIME | CorrelationId: {} | ExecutionTime: {}ms | URI: {}",
        correlationId,
        executionTime,
        request.getRequestURI()
      );
    }

    // Log de response body en debugging (solo para errores o cuando esté habilitado)
    if (
      log.isDebugEnabled() && response instanceof ContentCachingResponseWrapper
    ) {
      ContentCachingResponseWrapper cachingResponse =
        (ContentCachingResponseWrapper) response;
      byte[] content = cachingResponse.getContentAsByteArray();
      if (content.length > 0) {
        String body = new String(content, StandardCharsets.UTF_8);
        log.debug(
          "📤 RESPONSE_BODY | CorrelationId: {} | Body: {}",
          correlationId,
          body
        );
      }
    }
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

  private Object sanitizeParameters(
    java.util.Map<String, String[]> parameterMap
  ) {
    // Sanitizar parámetros sensibles
    return parameterMap
      .entrySet()
      .stream()
      .collect(
        java.util.stream.Collectors.toMap(
          java.util.Map.Entry::getKey,
          entry -> {
            String key = entry.getKey().toLowerCase();
            if (
              key.contains("password") ||
              key.contains("token") ||
              key.contains("secret") ||
              key.contains("key")
            ) {
              return "[REDACTED]";
            }
            return java.util.Arrays.toString(entry.getValue());
          }
        )
      );
  }

  private String sanitizeRequestBody(String body) {
    // Sanitizar campos sensibles en el body
    if (
      body.toLowerCase().contains("password") ||
      body.toLowerCase().contains("token") ||
      body.toLowerCase().contains("secret")
    ) {
      return "[CONTAINS_SENSITIVE_DATA]";
    }

    // Limitar tamaño del body en logs
    if (body.length() > 1000) {
      return body.substring(0, 1000) + "... [TRUNCATED]";
    }

    return body;
  }
}
