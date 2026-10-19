-- ============================================================
-- Esquema de la base de datos `modulo2`
-- Coherente con las entidades JPA (Profesor, Materia)
-- y con JdbcUserDetailsManager (users, authorities)
-- ============================================================



-- ------------------------------------------------------------
-- Tabla `profesor`  ↔  Profesor.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `profesor` (
                            `id`        int NOT NULL AUTO_INCREMENT,
                            `apellidos` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                            `direccion` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                            `nombre`    varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ------------------------------------------------------------
-- Tabla `materias`  ↔  Materia.java
-- FK a profesor.id (mismo tipo: int)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `materias` (
                            `id`            int NOT NULL AUTO_INCREMENT,
                            `horas_credito` int DEFAULT NULL,
                            `nombre`        varchar(255) COLLATE utf8mb4_bin NOT NULL,
                            `num_creditos`  int DEFAULT NULL,
                            `id_profesor`   int DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKlnqh687mx9fl9eekej6wn703b` (`id_profesor`),
                            CONSTRAINT `FKlnqh687mx9fl9eekej6wn703b` FOREIGN KEY (`id_profesor`)
                                REFERENCES `profesor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ------------------------------------------------------------
-- Tabla `users`  →  JdbcUserDetailsManager
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
                         `username` varchar(100) NOT NULL COLLATE utf8mb4_bin,
                         `password` varchar(100) NOT NULL,
                         `enabled`  tinyint(1) NOT NULL DEFAULT 1,
                         PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ------------------------------------------------------------
-- Tabla `authorities`  →  JdbcUserDetailsManager
-- FK a users.username (mismo COLLATE: utf8mb4_bin)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `authorities` (
                               `username`  varchar(100) NOT NULL COLLATE utf8mb4_bin,
                               `authority` varchar(50)  NOT NULL,
                               UNIQUE KEY `authorities_idx_1` (`username`, `authority`),
                               CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`)
                                   REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ------------------------------------------------------------
-- Datos iniciales de seguridad
-- {noop} = contraseña en texto plano (decisión consciente)
-- ------------------------------------------------------------
INSERT IGNORE INTO `users` (`username`, `password`, `enabled`) VALUES
                                                            ('rector',   '{noop}1234', 1),
                                                            ('profesor', '{noop}1234', 1),
                                                            ('alumno',   '{noop}1234', 1);

INSERT IGNORE INTO `authorities` (`username`, `authority`) VALUES
                                                        ('rector',   'ROLE_RECTOR'),
                                                        ('profesor', 'ROLE_PROFESOR'),
                                                        ('alumno',   'ROLE_ALUMNO');
