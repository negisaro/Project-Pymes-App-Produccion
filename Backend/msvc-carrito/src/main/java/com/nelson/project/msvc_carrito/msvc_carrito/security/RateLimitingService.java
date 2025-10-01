package com.nelson.project.msvc_carrito.msvc_carrito.security;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de Rate Limiting para proteger against ataques de fuerza bruta y abuso.
 *
 * Implementa múltiples estrategias:
 * - Límite por IP address
 * - Límite por usuario autenticado
 * - Ventanas deslizantes de tiempo
 * - Diferentes límites por endpoint
 * - Persistencia en Redis para entornos distribuidos
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitingService {

  private final RedisTemplate<String, Object> redisTemplate;

  // Cache local como fallback cuando Redis no está disponible
  private final ConcurrentHashMap<String, RateLimitInfo> localCache =
    new ConcurrentHashMap<>();

  // Configuración de límites por defecto
  private static final int DEFAULT_REQUESTS_PER_MINUTE = 60;
  private static final int LOGIN_REQUESTS_PER_MINUTE = 5;
  private static final int ADMIN_REQUESTS_PER_MINUTE = 100;

  // Configuración de límites por endpoint
  private static final ConcurrentHashMap<String, Integer> ENDPOINT_LIMITS =
    new ConcurrentHashMap<>();

  static {
    ENDPOINT_LIMITS.put("/api/v1/auth/login", LOGIN_REQUESTS_PER_MINUTE);
    ENDPOINT_LIMITS.put("/api/v1/carrito", DEFAULT_REQUESTS_PER_MINUTE);
    ENDPOINT_LIMITS.put("/api/v1/admin", ADMIN_REQUESTS_PER_MINUTE);
  }

  /**
   * Verifica si una request está dentro de los límites permitidos.
   */
  public boolean isAllowed(String identifier, String endpoint) {
    String key = generateKey(identifier, endpoint);
    int limit = getLimit(endpoint);

    try {
      return checkRateLimitRedis(key, limit);
    } catch (Exception e) {
      log.warn(
        "Redis no disponible para rate limiting, usando cache local: {}",
        e.getMessage()
      );
      return checkRateLimitLocal(key, limit);
    }
  }

  /**
   * Obtiene información actual del rate limit para un identificador.
   */
  public RateLimitStatus getRateLimitStatus(
    String identifier,
    String endpoint
  ) {
    String key = generateKey(identifier, endpoint);
    int limit = getLimit(endpoint);

    try {
      return getRateLimitStatusRedis(key, limit);
    } catch (Exception e) {
      log.debug("Redis no disponible, usando cache local para status");
      return getRateLimitStatusLocal(key, limit);
    }
  }

  /**
   * Incrementa el contador para un identificador específico.
   */
  public void incrementCounter(String identifier, String endpoint) {
    String key = generateKey(identifier, endpoint);

    try {
      incrementCounterRedis(key);
    } catch (Exception e) {
      log.debug("Redis no disponible, incrementando en cache local");
      incrementCounterLocal(key);
    }
  }

  /**
   * Resetea el contador para un identificador (útil para testing o casos especiales).
   */
  public void resetCounter(String identifier, String endpoint) {
    String key = generateKey(identifier, endpoint);

    try {
      redisTemplate.delete(key);
      log.info("🔄 RATE_LIMIT_RESET | Key: {}", key);
    } catch (Exception e) {
      localCache.remove(key);
      log.info("🔄 RATE_LIMIT_RESET_LOCAL | Key: {}", key);
    }
  }

  // ============================================
  // IMPLEMENTACIÓN REDIS
  // ============================================

  private boolean checkRateLimitRedis(String key, int limit) {
    Long currentCount = redisTemplate.opsForValue().increment(key);

    if (currentCount == 1) {
      // Primera request, establecer TTL
      redisTemplate.expire(key, Duration.ofMinutes(1));
    }

    boolean allowed = currentCount <= limit;

    if (!allowed) {
      log.warn(
        "🚫 RATE_LIMIT_EXCEEDED | Key: {} | Count: {} | Limit: {}",
        key,
        currentCount,
        limit
      );
    } else {
      log.debug(
        "✅ RATE_LIMIT_OK | Key: {} | Count: {} | Limit: {}",
        key,
        currentCount,
        limit
      );
    }

    return allowed;
  }

  private RateLimitStatus getRateLimitStatusRedis(String key, int limit) {
    Long currentCount = (Long) redisTemplate.opsForValue().get(key);
    if (currentCount == null) {
      currentCount = 0L;
    }

    Long ttl = redisTemplate.getExpire(key);

    return RateLimitStatus.builder()
      .limit(limit)
      .remaining(Math.max(0, limit - currentCount.intValue()))
      .resetTime(ttl != null && ttl > 0 ? ttl : 60)
      .allowed(currentCount <= limit)
      .build();
  }

  private void incrementCounterRedis(String key) {
    Long newCount = redisTemplate.opsForValue().increment(key);

    if (newCount == 1) {
      redisTemplate.expire(key, Duration.ofMinutes(1));
    }
  }

  // ============================================
  // IMPLEMENTACIÓN LOCAL (FALLBACK)
  // ============================================

  private boolean checkRateLimitLocal(String key, int limit) {
    RateLimitInfo info = localCache.computeIfAbsent(key, k ->
      new RateLimitInfo()
    );

    long now = System.currentTimeMillis();

    // Reset si ha pasado más de 1 minuto
    if (now - info.getWindowStart() > 60000) {
      info.reset(now);
    }

    boolean allowed = info.getCount().incrementAndGet() <= limit;

    if (!allowed) {
      log.warn(
        "🚫 RATE_LIMIT_EXCEEDED_LOCAL | Key: {} | Count: {} | Limit: {}",
        key,
        info.getCount().get(),
        limit
      );
    }

    return allowed;
  }

  private RateLimitStatus getRateLimitStatusLocal(String key, int limit) {
    RateLimitInfo info = localCache.get(key);
    if (info == null) {
      return RateLimitStatus.builder()
        .limit(limit)
        .remaining(limit)
        .resetTime(60L)
        .allowed(true)
        .build();
    }

    long now = System.currentTimeMillis();
    long elapsed = now - info.getWindowStart();
    long resetTime = Math.max(0, 60 - (elapsed / 1000));

    return RateLimitStatus.builder()
      .limit(limit)
      .remaining(Math.max(0, limit - info.getCount().get()))
      .resetTime(resetTime)
      .allowed(info.getCount().get() <= limit)
      .build();
  }

  private void incrementCounterLocal(String key) {
    RateLimitInfo info = localCache.computeIfAbsent(key, k ->
      new RateLimitInfo()
    );

    long now = System.currentTimeMillis();
    if (now - info.getWindowStart() > 60000) {
      info.reset(now);
    }

    info.getCount().incrementAndGet();
  }

  // ============================================
  // MÉTODOS AUXILIARES
  // ============================================

  private String generateKey(String identifier, String endpoint) {
    return String.format(
      "rate_limit:%s:%s",
      sanitizeIdentifier(identifier),
      sanitizeEndpoint(endpoint)
    );
  }

  private String sanitizeIdentifier(String identifier) {
    if (identifier == null) {
      return "anonymous";
    }
    // Remover caracteres especiales para evitar problemas con Redis
    return identifier.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  private String sanitizeEndpoint(String endpoint) {
    if (endpoint == null) {
      return "unknown";
    }

    // Normalizar endpoint para agrupar similares
    for (String pattern : ENDPOINT_LIMITS.keySet()) {
      if (endpoint.startsWith(pattern)) {
        return pattern;
      }
    }

    return "default";
  }

  private int getLimit(String endpoint) {
    String normalizedEndpoint = sanitizeEndpoint(endpoint);
    return ENDPOINT_LIMITS.getOrDefault(
      normalizedEndpoint,
      DEFAULT_REQUESTS_PER_MINUTE
    );
  }

  // ============================================
  // CLASES INTERNAS
  // ============================================

  @lombok.Data
  @lombok.Builder
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public static class RateLimitStatus {

    private int limit;
    private int remaining;
    private long resetTime;
    private boolean allowed;
  }

  @lombok.Data
  private static class RateLimitInfo {

    private AtomicInteger count = new AtomicInteger(0);
    private long windowStart = System.currentTimeMillis();

    public void reset(long newWindowStart) {
      this.count.set(0);
      this.windowStart = newWindowStart;
    }
  }
}
