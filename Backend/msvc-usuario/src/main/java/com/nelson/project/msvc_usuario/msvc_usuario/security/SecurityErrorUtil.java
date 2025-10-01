package com.nelson.project.msvc_usuario.msvc_usuario.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utilidad para uniformar respuestas de error JSON en la capa de seguridad.
 * Ahora usa el ObjectMapper administrado por Spring (con soporte Java Time).
 */
@Component
public final class SecurityErrorUtil {

  private static final Logger logger = LoggerFactory.getLogger(SecurityErrorUtil.class);
  private static ObjectMapper staticMapper;

  private final ObjectMapper mapper;

  public SecurityErrorUtil(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @PostConstruct
  void init() {
    staticMapper = this.mapper;
  }

  public static void writeError(
    HttpServletResponse response,
    int status,
    String error,
    String message,
    String detalle
  ) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");

    Map<String, Object> body = new HashMap<>();
    body.put("status", status);
    body.put("error", error);
    body.put("message", message);
    if (detalle != null) {
      body.put("detalle", detalle);
    }

    try {
      response.getWriter().write(staticMapper.writeValueAsString(body));
    } catch (IOException e) {
      logger.error("[SecurityErrorUtil] Error escribiendo respuesta JSON", e);
      throw e;
    }
  }
}