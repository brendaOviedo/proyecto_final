-- =====================================================================
-- Sistema de Turnos - Proyecto Final Programación II
-- Crea la base de datos SQLite (data/turnos.db) y carga los datos iniciales.
--
-- Uso desde la carpeta raíz del proyecto:
--     sqlite3 data/turnos.db < sql/crear_y_cargar.sql
--
-- El script es idempotente: no borra información existente.
-- Para empezar desde cero, elimine primero el archivo data/turnos.db.
-- =====================================================================

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS clientes (
    codigo    INTEGER PRIMARY KEY CHECK (codigo > 0),
    nombre    TEXT    NOT NULL CHECK (length(trim(nombre)) > 0),
    telefono  TEXT    NOT NULL CHECK (length(telefono) = 8 AND telefono NOT GLOB '*[^0-9]*'),
    estado    TEXT    NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS servicios (
    codigo    INTEGER PRIMARY KEY CHECK (codigo > 0),
    nombre    TEXT    NOT NULL CHECK (length(trim(nombre)) > 0),
    estado    TEXT    NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

-- AUTOINCREMENT garantiza que el número de turno nunca se reutilice.
CREATE TABLE IF NOT EXISTS turnos (
    numero              INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo_cliente      INTEGER NOT NULL,
    codigo_servicio     INTEGER NOT NULL,
    fecha_generacion    TEXT    NOT NULL,
    estado              TEXT    NOT NULL DEFAULT 'PENDIENTE'
                                CHECK (estado IN ('PENDIENTE', 'ATENDIDO', 'CANCELADO')),
    fecha_finalizacion  TEXT,
    CONSTRAINT fk_turnos_clientes  FOREIGN KEY (codigo_cliente)  REFERENCES clientes (codigo),
    CONSTRAINT fk_turnos_servicios FOREIGN KEY (codigo_servicio) REFERENCES servicios (codigo),
    CONSTRAINT ck_turnos_fecha_finalizacion CHECK (
        (estado = 'PENDIENTE' AND fecha_finalizacion IS NULL) OR
        (estado <> 'PENDIENTE' AND fecha_finalizacion IS NOT NULL))
);

-- Un cliente solo puede tener un turno pendiente a la vez.
CREATE UNIQUE INDEX IF NOT EXISTS ux_turnos_pendiente_por_cliente
    ON turnos (codigo_cliente) WHERE estado = 'PENDIENTE';

CREATE INDEX IF NOT EXISTS ix_turnos_estado ON turnos (estado);

-- Datos iniciales
INSERT OR IGNORE INTO clientes (codigo, nombre, telefono, estado) VALUES
    (1001, 'Ana López',      '55501001', 'ACTIVO'),
    (1002, 'Carlos Pérez',   '55501002', 'ACTIVO'),
    (1003, 'María García',   '55501003', 'ACTIVO'),
    (1004, 'José Hernández', '55501004', 'ACTIVO'),
    (1005, 'Luisa Morales',  '55501005', 'ACTIVO');

INSERT OR IGNORE INTO servicios (codigo, nombre, estado) VALUES
    (1, 'Información',             'ACTIVO'),
    (2, 'Entrega de documentos',   'ACTIVO'),
    (3, 'Actualización de datos',  'ACTIVO');
