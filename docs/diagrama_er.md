# Diagrama entidad-relación

![Diagrama entidad-relación](diagramas/diagrama_entidad_relacion.png)

Fuente Mermaid del diagrama:

Base de datos SQLite: `data/turnos.db` (script: `sql/crear_y_cargar.sql`).

```mermaid
erDiagram
    CLIENTES ||--o{ TURNOS : "solicita"
    SERVICIOS ||--o{ TURNOS : "se brinda en"

    CLIENTES {
        INTEGER codigo PK "> 0"
        TEXT nombre "NOT NULL"
        TEXT telefono "8 dígitos"
        TEXT estado "ACTIVO | INACTIVO"
    }

    SERVICIOS {
        INTEGER codigo PK "> 0"
        TEXT nombre "NOT NULL"
        TEXT estado "ACTIVO | INACTIVO"
    }

    TURNOS {
        INTEGER numero PK "AUTOINCREMENT (no se reutiliza)"
        INTEGER codigo_cliente FK "-> clientes.codigo"
        INTEGER codigo_servicio FK "-> servicios.codigo"
        TEXT fecha_generacion "yyyy-MM-dd HH:mm:ss"
        TEXT estado "PENDIENTE | ATENDIDO | CANCELADO"
        TEXT fecha_finalizacion "atención o cancelación"
    }
```

## Restricciones

- `turnos.codigo_cliente` → `clientes.codigo` y `turnos.codigo_servicio` → `servicios.codigo` (claves foráneas; se habilitan en cada conexión con `PRAGMA foreign_keys = ON`).
- Índice único parcial `ux_turnos_pendiente_por_cliente`: un cliente solo puede tener un turno `PENDIENTE` a la vez.
- `CHECK` sobre `fecha_finalizacion`: es nula solo mientras el turno está pendiente.
- `numero` usa `AUTOINCREMENT`, por lo que SQLite nunca reutiliza números de turno, incluso si se borraran filas.
