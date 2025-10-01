-- MIGRACIÓN DESACTIVADA TEMPORALMENTE
-- Motivo: Necesario continuar el desarrollo sin ejecutar DDL hasta estabilizar el modelo.
-- Cuando se reactive: eliminar el bloque de comentarios y validar en una BD limpia.
/*
-- V1: Esquema inicial (MySQL) - usuarios, roles y relación user_roles
-- Dialecto: MySQL / MariaDB
-- Notas:
--  * Se usa AUTO_INCREMENT para las PK.
--  * BOOLEAN es alias de TINYINT(1) en MySQL.
--  * Se usan VARCHAR en lugar de NVARCHAR.
--  * No se añaden ON UPDATE automáticos para no interferir con auditoría JPA.
--  * Ajustar collation/charset global (utf8mb4) si no está definido a nivel de base de datos.

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    creado_en DATETIME NULL,
    actualizado_en DATETIME NULL,
    CONSTRAINT uk_roles_name UNIQUE (name),
    INDEX idx_rol_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    creado_en DATETIME NULL,
    actualizado_en DATETIME NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT uk_usuario_username UNIQUE (username),
    INDEX idx_usuario_email (email),
    INDEX idx_usuario_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    rol_id  BIGINT NOT NULL,
    PRIMARY KEY (user_id, rol_id),
    CONSTRAINT fk_user_roles_usuario FOREIGN KEY (user_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentario: Las columnas de auditoría (creado_en / actualizado_en) serán gestionadas por listeners (@PrePersist/@PreUpdate)
*/
