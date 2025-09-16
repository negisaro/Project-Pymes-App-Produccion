package com.nelson.project.msvc_proveedor.msvc_proveedor.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

  private static final Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
          String authHeader = attrs.getRequest().getHeader("Authorization");
          logger.info("[Feign] Authorization header encontrado: {}", authHeader);
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
            template.header("Authorization", authHeader);
          }
        } else {
          logger.warn("[Feign] No se pudo obtener ServletRequestAttributes para extraer Authorization header.");
        }
      }
    };
  }
}