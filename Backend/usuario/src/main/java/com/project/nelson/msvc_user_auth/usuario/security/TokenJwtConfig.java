package com.project.nelson.msvc_user_auth.usuario.security;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Configuración centralizada para el manejo de JWT en la aplicación.
 * Proporciona claves, prefijos y cabeceras estándar para seguridad empresarial.
 * Clase de utilidades, no instanciable.
 */
public final class TokenJwtConfig {

    /**
     * Clave secreta para la firma y validación de JWT (HS256).
     * En producción, debe gestionarse de forma segura (por variables de entorno o
     * vault).
     * Debe tener al menos 32 caracteres.
     */
    private static final String JWT_SECRET_BASE64 = "pJ9KkV7b6bDPqKhtWZfLzN6rQ3wXy5VtU2hXyqGz4A8E=";

    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET_BASE64));

    /**
     * Prefijo estándar para el token JWT en la cabecera Authorization.
     */
    public static final String PREFIX_TOKEN = "Bearer ";

    /**
     * Nombre de la cabecera HTTP donde se envía el JWT.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Tipo de contenido para respuestas JSON.
     */
    public static final String CONTENT_TYPE = "application/json";

    /**
     * Constructor privado para evitar instanciación.
     */
    private TokenJwtConfig() {
        // Clase de utilidades, no instanciable
    }
}
