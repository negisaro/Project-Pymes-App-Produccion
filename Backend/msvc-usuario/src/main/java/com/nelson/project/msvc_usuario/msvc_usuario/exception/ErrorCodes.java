package com.nelson.project.msvc_usuario.msvc_usuario.exception;

/**
 * Constantes de códigos de error estándar para CustomException.
 * Útil para comunicación clara con el frontend y manejo centralizado de errores.
 */
public final class ErrorCodes {

  public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
  public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
  public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
  public static final String UNAUTHORIZED_ACTION = "UNAUTHORIZED_ACTION";
  public static final String INVALID_ROLE = "INVALID_ROLE";
  public static final String EMAIL_ALREADY_REGISTERED =
    "EMAIL_ALREADY_REGISTERED";
  public static final String USER_HAS_ROLES = "USER_HAS_ROLES";
  public static final String USER_UPDATE_FAILED = "USER_UPDATE_FAILED";
  public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
  public static final String DUPLICATE_USERNAME = "DUPLICATE_USERNAME";
  public static final String DUPLICATE_EMAIL = "DUPLICATE_EMAIL";
  public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
  public static final String ACCESS_DENIED = "ACCESS_DENIED";
  public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
  public static final String INVALID_TOKEN = "INVALID_TOKEN";
  public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
  public static final String TOKEN_USED = "TOKEN_USED";
  public static final String PASSWORD_POLICY_VIOLATION =
    "PASSWORD_POLICY_VIOLATION";
  public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
  public static final String MAIL_CONFIG_ERROR = "MAIL_CONFIG_ERROR";
  public static final String TOKEN_ISSUER_INVALID = "TOKEN_ISSUER_INVALID";
  public static final String TOKEN_AUDIENCE_INVALID = "TOKEN_AUDIENCE_INVALID";
  public static final String TOKEN_SIGNATURE_INVALID =
    "TOKEN_SIGNATURE_INVALID";

  public static final String EMAIL_SEND_ERROR = "EMAIL_SEND_ERROR";

  private ErrorCodes() {}
}
