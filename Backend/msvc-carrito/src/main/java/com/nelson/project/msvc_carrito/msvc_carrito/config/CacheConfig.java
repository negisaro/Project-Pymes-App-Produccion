package com.nelson.project.msvc_carrito.msvc_carrito.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuración de caché para el microservicio de carrito.
 *
 * Implementa una estrategia de caché multi-nivel:
 * - Caché local en memoria para datos frecuentemente accedidos
 * - Caché distribuido Redis para escalabilidad
 * - TTL configurables por tipo de dato
 * - Serialización JSON optimizada
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@Configuration
@EnableCaching
public class CacheConfig {

  public static final String CARRITO_CACHE = "carrito";
  public static final String PRODUCTO_CACHE = "producto";
  public static final String USUARIO_CACHE = "usuario";
  public static final String DESCUENTO_CACHE = "descuento";
  public static final String METRICAS_CACHE = "metricas";

  /**
   * Configuración de caché para entorno de desarrollo (en memoria).
   */
  @Bean
  @Profile("dev")
  public CacheManager cacheManagerDev() {
    return new ConcurrentMapCacheManager(
      CARRITO_CACHE,
      PRODUCTO_CACHE,
      USUARIO_CACHE,
      DESCUENTO_CACHE,
      METRICAS_CACHE
    );
  }

  /**
   * Configuración de caché distribuido Redis para producción.
   */
  @Bean
  @Profile({ "prod", "staging" })
  public CacheManager cacheManagerRedis(
    RedisConnectionFactory redisConnectionFactory
  ) {
    return RedisCacheManager.builder(redisConnectionFactory)
      .cacheDefaults(
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(30))
          .serializeKeysWith(
            org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(
              new StringRedisSerializer()
            )
          )
          .serializeValuesWith(
            org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(
              new GenericJackson2JsonRedisSerializer()
            )
          )
          .disableCachingNullValues()
      )
      .withCacheConfiguration(
        CARRITO_CACHE,
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(15)) // Carritos: 15 minutos
      )
      .withCacheConfiguration(
        PRODUCTO_CACHE,
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofHours(1)) // Productos: 1 hora
      )
      .withCacheConfiguration(
        USUARIO_CACHE,
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(30)) // Usuarios: 30 minutos
      )
      .withCacheConfiguration(
        DESCUENTO_CACHE,
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(10)) // Descuentos: 10 minutos
      )
      .withCacheConfiguration(
        METRICAS_CACHE,
        org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(5)) // Métricas: 5 minutos
      )
      .build();
  }

  /**
   * RedisTemplate personalizado para operaciones de caché avanzadas.
   */
  @Bean
  @Profile({ "prod", "staging" })
  public RedisTemplate<String, Object> redisTemplate(
    RedisConnectionFactory redisConnectionFactory
  ) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    // Configurar serializadores
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    template.afterPropertiesSet();

    return template;
  }
}
