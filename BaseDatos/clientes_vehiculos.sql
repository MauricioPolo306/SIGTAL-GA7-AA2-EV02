-- ============================================================
-- SIGTAL - Módulo Clientes y Vehículos
-- Base de datos necesaria para GA7-220501096-AA3-EV01
-- ============================================================

CREATE DATABASE IF NOT EXISTS sigtal
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sigtal;

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    documento VARCHAR(50) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(150),
    direccion VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vehiculos (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(80) NOT NULL,
    modelo VARCHAR(80) NOT NULL,
    anio INT NULL,
    color VARCHAR(50),
    tipo_vehiculo VARCHAR(50),
    id_cliente INT NOT NULL,
    CONSTRAINT fk_vehiculo_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Datos de demostración opcionales. No se duplican si ya existen.
INSERT INTO clientes (nombre, documento, telefono, correo, direccion)
SELECT 'Juan Perez', '12345678', '3502924517', 'juan@gmail.com', 'Carrera 95 #45-45'
WHERE NOT EXISTS (
    SELECT 1 FROM clientes WHERE documento = '12345678'
);

INSERT INTO vehiculos (placa, marca, modelo, anio, color, tipo_vehiculo, id_cliente)
SELECT 'ABC123', 'Chevrolet', 'Spark', 2020, 'Blanco', 'Automóvil', c.id_cliente
FROM clientes c
WHERE c.documento = '12345678'
  AND NOT EXISTS (SELECT 1 FROM vehiculos WHERE placa = 'ABC123');
