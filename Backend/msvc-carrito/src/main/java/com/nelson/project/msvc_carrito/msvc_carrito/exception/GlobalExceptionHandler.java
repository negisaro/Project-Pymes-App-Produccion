package com.nelson.project.msvc_carrito.msvc_carrito.exception;

import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.ErrorResponseDto;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Manejador global de excepciones empresarial para el microservicio de carrito.
 *
 * Implementa un manejo robusto y centralizado de errores siguiendo las mejores prácticas:
 * - Respuestas de error estandarizadas y consistentes
 * - Logging estructurado para auditoría y debugging
 * - Códigos de error específicos para diferentes tipos de fallos
 * - Información contextual rica para troubleshooting
 * - Seguridad en el manejo de información sensible
 * - Soporte para correlación de requests distribuidos
 *
 * @author Nelson
 * @version 2.0.0
 * @since 2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(
    GlobalExceptionHandler.class
  );

  // ============================================
  // EXCEPCIONES DE NEGOCIO ESPECÍFICAS
  // ============================================

  @ExceptionHandler(UsuarioNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUsuarioNotFound(
    UsuarioNotFoundException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Usuario no encontrado - ID: {} | CorrelationId: {}",
      ex.getUsuarioId(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.NOT_FOUND.value())
      .error("Usuario No Encontrado")
      .message(ex.getMessage())
      .errorCode("USUARIO_NOT_FOUND")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        ex.getUsuarioId() != null
          ? java.util.Map.of("usuarioId", ex.getUsuarioId())
          : null
      )
      .suggestions(
        List.of(
          "Verifique que el ID del usuario sea correcto",
          "Asegúrese de que el usuario esté registrado en el sistema"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(ProductoNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleProductoNotFound(
    ProductoNotFoundException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Producto no encontrado - ID: {} | CorrelationId: {}",
      ex.getProductoId(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.NOT_FOUND.value())
      .error("Producto No Encontrado")
      .message(ex.getMessage())
      .errorCode("PRODUCTO_NOT_FOUND")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        ex.getProductoId() != null
          ? java.util.Map.of("productoId", ex.getProductoId())
          : null
      )
      .suggestions(
        List.of(
          "Verifique que el ID del producto sea correcto",
          "El producto podría haber sido descontinuado"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(CarritoNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleCarritoNotFound(
    CarritoNotFoundException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Carrito no encontrado - ID: {} | CorrelationId: {}",
      ex.getCarritoId(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.NOT_FOUND.value())
      .error("Carrito No Encontrado")
      .message(ex.getMessage())
      .errorCode("CARRITO_NOT_FOUND")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        ex.getCarritoId() != null
          ? java.util.Map.of("carritoId", ex.getCarritoId())
          : null
      )
      .suggestions(
        List.of(
          "Verifique que el carrito exista para este usuario",
          "Intente crear un nuevo carrito si es necesario"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(StockInsuficienteException.class)
  public ResponseEntity<ErrorResponseDto> handleStockInsuficiente(
    StockInsuficienteException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Stock insuficiente - Producto: {}, Solicitado: {}, Disponible: {} | CorrelationId: {}",
      ex.getProductoId(),
      ex.getCantidadSolicitada(),
      ex.getStockDisponible(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Stock Insuficiente")
      .message(ex.getMessage())
      .errorCode("STOCK_INSUFICIENTE")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        java.util.Map.of(
          "productoId",
          ex.getProductoId(),
          "cantidadSolicitada",
          ex.getCantidadSolicitada(),
          "stockDisponible",
          ex.getStockDisponible()
        )
      )
      .suggestions(
        List.of(
          String.format(
            "Reduzca la cantidad a máximo %d unidades",
            ex.getStockDisponible()
          ),
          "Consulte productos alternativos disponibles"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(LimiteCarritoExcedidoException.class)
  public ResponseEntity<ErrorResponseDto> handleLimiteCarritoExcedido(
    LimiteCarritoExcedidoException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Límite de carrito excedido - Límite: {}, Actual: {} | CorrelationId: {}",
      ex.getLimiteMaximo(),
      ex.getCantidadActual(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Límite de Carrito Excedido")
      .message(ex.getMessage())
      .errorCode("LIMITE_CARRITO_EXCEDIDO")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        java.util.Map.of(
          "limiteMaximo",
          ex.getLimiteMaximo(),
          "cantidadActual",
          ex.getCantidadActual()
        )
      )
      .suggestions(
        List.of(
          String.format(
            "El límite máximo es de %d items",
            ex.getLimiteMaximo()
          ),
          "Remueva algunos items antes de agregar nuevos"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(DescuentoInvalidoException.class)
  public ResponseEntity<ErrorResponseDto> handleDescuentoInvalido(
    DescuentoInvalidoException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Descuento inválido - Código: {} | CorrelationId: {}",
      ex.getCodigoDescuento(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Descuento Inválido")
      .message(ex.getMessage())
      .errorCode("DESCUENTO_INVALIDO")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        ex.getCodigoDescuento() != null
          ? java.util.Map.of("codigoDescuento", ex.getCodigoDescuento())
          : null
      )
      .suggestions(
        List.of(
          "Verifique que el código de descuento sea correcto",
          "El descuento podría haber expirado o no ser aplicable a estos productos"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> handleBusinessException(
    BusinessException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.error(
      "Error de negocio - Código: {}, Mensaje: {} | CorrelationId: {}",
      ex.getErrorCode(),
      ex.getMessage(),
      correlationId,
      ex
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Error de Regla de Negocio")
      .message(ex.getMessage())
      .errorCode(ex.getErrorCode())
      .details(ex.getDetails())
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(convertParametersToMap(ex.getParameters()))
      .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  // ============================================
  // EXCEPCIONES DE VALIDACIÓN
  // ============================================

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(
    MethodArgumentNotValidException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Error de validación en request body | CorrelationId: {}",
      correlationId
    );

    List<ErrorResponseDto.FieldErrorDto> fieldErrors = ex
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .map(this::mapFieldError)
      .collect(Collectors.toList());

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Error de Validación")
      .message("Los datos enviados contienen errores de validación")
      .errorCode("VALIDATION_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .fieldErrors(fieldErrors)
      .suggestions(
        List.of(
          "Verifique que todos los campos requeridos estén presentes",
          "Asegúrese de que los valores cumplan con los formatos esperados"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
    ConstraintViolationException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Error de validación en parámetros | CorrelationId: {}",
      correlationId
    );

    List<ErrorResponseDto.FieldErrorDto> fieldErrors = ex
      .getConstraintViolations()
      .stream()
      .map(this::mapConstraintViolation)
      .collect(Collectors.toList());

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Error de Validación de Parámetros")
      .message("Los parámetros enviados no cumplen con las restricciones")
      .errorCode("PARAMETER_VALIDATION_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .fieldErrors(fieldErrors)
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // ============================================
  // EXCEPCIONES DE SEGURIDAD
  // ============================================

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
    AuthenticationException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn("Error de autenticación | CorrelationId: {}", correlationId);

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("No Autenticado")
      .message("Las credenciales proporcionadas no son válidas")
      .errorCode("AUTHENTICATION_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Verifique sus credenciales de acceso",
          "El token de autenticación podría haber expirado"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
    AccessDeniedException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn("Acceso denegado a recurso | CorrelationId: {}", correlationId);

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.FORBIDDEN.value())
      .error("Acceso Denegado")
      .message("No tiene permisos suficientes para acceder a este recurso")
      .errorCode("ACCESS_DENIED")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Contacte al administrador para solicitar los permisos necesarios",
          "Verifique que esté accediendo al recurso correcto"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  // ============================================
  // EXCEPCIONES DE INTEGRACIÓN
  // ============================================

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ErrorResponseDto> handleFeignException(
    FeignException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.error(
      "Error en comunicación con servicio externo - Status: {} | CorrelationId: {}",
      ex.status(),
      correlationId,
      ex
    );

    String message = ex.status() >= 500
      ? "Servicio externo temporalmente no disponible"
      : "Error en la comunicación con servicio externo";

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.SERVICE_UNAVAILABLE.value())
      .error("Error de Integración")
      .message(message)
      .errorCode("EXTERNAL_SERVICE_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(java.util.Map.of("externalServiceStatus", ex.status()))
      .suggestions(
        List.of(
          "Intente nuevamente en unos momentos",
          "Si el problema persiste, contacte al soporte técnico"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
  }

  // ============================================
  // EXCEPCIONES DE BASE DE DATOS
  // ============================================

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
    DataIntegrityViolationException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.error(
      "Violación de integridad de datos | CorrelationId: {}",
      correlationId,
      ex
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Conflicto de Datos")
      .message("La operación viola restricciones de integridad de datos")
      .errorCode("DATA_INTEGRITY_VIOLATION")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Verifique que los datos no estén duplicados",
          "Asegúrese de que las referencias sean válidas"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponseDto> handleDataAccessException(
    DataAccessException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.error("Error de acceso a datos | CorrelationId: {}", correlationId, ex);

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("Error de Base de Datos")
      .message("Error interno en el acceso a datos")
      .errorCode("DATABASE_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Intente nuevamente en unos momentos",
          "Si el problema persiste, contacte al soporte técnico"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  // ============================================
  // EXCEPCIONES GENERALES DE HTTP
  // ============================================

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(
    HttpMessageNotReadableException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn("Request body malformado | CorrelationId: {}", correlationId);

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Request Malformado")
      .message("El cuerpo de la petición no puede ser procesado")
      .errorCode("MALFORMED_REQUEST")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Verifique que el JSON esté bien formado",
          "Asegúrese de usar el Content-Type correcto"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponseDto> handleMissingParameter(
    MissingServletRequestParameterException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Parámetro requerido faltante: {} | CorrelationId: {}",
      ex.getParameterName(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Parámetro Requerido Faltante")
      .message(
        String.format("El parámetro '%s' es requerido", ex.getParameterName())
      )
      .errorCode("MISSING_PARAMETER")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(java.util.Map.of("missingParameter", ex.getParameterName()))
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
    MethodArgumentTypeMismatchException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Tipo de parámetro incorrecto - Parámetro: {}, Valor: {} | CorrelationId: {}",
      ex.getName(),
      ex.getValue(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Tipo de Parámetro Incorrecto")
      .message(
        String.format(
          "El parámetro '%s' debe ser de tipo %s",
          ex.getName(),
          (ex.getRequiredType() != null)
            ? ex.getRequiredType().getSimpleName()
            : "desconocido"
        )
      )
      .errorCode("PARAMETER_TYPE_MISMATCH")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        java.util.Map.of(
          "parameter",
          ex.getName(),
          "providedValue",
          ex.getValue(),
          "expectedType",
          (ex.getRequiredType() != null)
            ? ex.getRequiredType().getSimpleName()
            : "desconocido"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodNotSupported(
    HttpRequestMethodNotSupportedException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Método HTTP no soportado: {} | CorrelationId: {}",
      ex.getMethod(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.METHOD_NOT_ALLOWED.value())
      .error("Método No Permitido")
      .message(
        String.format(
          "El método %s no está permitido para este endpoint",
          ex.getMethod()
        )
      )
      .errorCode("METHOD_NOT_ALLOWED")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .parameters(
        java.util.Map.of(
          "requestedMethod",
          ex.getMethod(),
          "supportedMethods",
          ex.getSupportedMethods()
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleNoHandlerFound(
    NoHandlerFoundException ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.warn(
      "Endpoint no encontrado: {} {} | CorrelationId: {}",
      ex.getHttpMethod(),
      ex.getRequestURL(),
      correlationId
    );

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.NOT_FOUND.value())
      .error("Endpoint No Encontrado")
      .message("El endpoint solicitado no existe")
      .errorCode("ENDPOINT_NOT_FOUND")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Verifique la URL del endpoint",
          "Consulte la documentación de la API"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  // ============================================
  // EXCEPCIONES GENERALES
  // ============================================

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(
    Exception ex,
    HttpServletRequest request
  ) {
    String correlationId = generateCorrelationId();
    log.error("Error inesperado | CorrelationId: {}", correlationId, ex);

    ErrorResponseDto error = ErrorResponseDto.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("Error Interno del Servidor")
      .message("Ha ocurrido un error inesperado")
      .errorCode("INTERNAL_SERVER_ERROR")
      .path(request.getRequestURI())
      .method(request.getMethod())
      .correlationId(correlationId)
      .suggestions(
        List.of(
          "Intente nuevamente en unos momentos",
          "Si el problema persiste, contacte al soporte técnico con el ID de correlación"
        )
      )
      .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  // ============================================
  // MÉTODOS AUXILIARES
  // ============================================

  private ErrorResponseDto.FieldErrorDto mapFieldError(FieldError fieldError) {
    return ErrorResponseDto.FieldErrorDto.builder()
      .field(fieldError.getField())
      .rejectedValue(fieldError.getRejectedValue())
      .message(fieldError.getDefaultMessage())
      .errorCode(fieldError.getCode())
      .build();
  }

  private ErrorResponseDto.FieldErrorDto mapConstraintViolation(
    ConstraintViolation<?> violation
  ) {
    String fieldName = violation.getPropertyPath().toString();
    return ErrorResponseDto.FieldErrorDto.builder()
      .field(fieldName)
      .rejectedValue(violation.getInvalidValue())
      .message(violation.getMessage())
      .errorCode("CONSTRAINT_VIOLATION")
      .build();
  }

  private String generateCorrelationId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Convierte un array de parámetros a un Map para el ErrorResponseDto.
   */
  private Map<String, Object> convertParametersToMap(Object[] parameters) {
    if (parameters == null || parameters.length == 0) {
      return Map.of();
    }

    Map<String, Object> parameterMap = new HashMap<>();
    for (int i = 0; i < parameters.length; i++) {
      parameterMap.put("param" + i, parameters[i]);
    }
    return parameterMap;
  }
}
