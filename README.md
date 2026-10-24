# Sistema de Turnos — Oficina de Servicios Integrales

Proyecto final de **Programación II**. Aplicación de consola en Java 21 para administrar la atención de clientes en una oficina de servicios: registro de clientes y servicios, generación de turnos, atención en orden de llegada (FIFO), cancelación y reportes, con persistencia en SQLite mediante JDBC.

## Integrantes

| Nombre | Carné | Responsabilidad principal |
|---|---|---|
| _(Nombre integrante 1)_ | _(Carné)_ | Clientes y BST |
| _(Nombre integrante 2)_ | _(Carné)_ | Turnos y cola enlazada |
| _(Nombre integrante 3)_ | _(Carné)_ | Servicios, persistencia y reportes |

Todos los integrantes participaron en la integración, las pruebas y la documentación.

## Repositorio

<https://github.com/brendaOviedo/proyecto_final>

## Requisitos

- **JDK 21** o superior (`java -version`).
- **Apache Maven 3.9+** (`mvn -version`). En VS Code también puede usarse el Maven integrado del *Extension Pack for Java*.
- **VS Code** con el *Extension Pack for Java* (Microsoft).
- Opcional: **sqlite3** (CLI) para crear la base de datos manualmente o inspeccionarla.

La dependencia del driver JDBC (`org.xerial:sqlite-jdbc`) se descarga automáticamente con Maven.

## Abrir y ejecutar en VS Code

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/brendaOviedo/proyecto_final.git
   ```
2. En VS Code: **File → Open Folder…** y seleccionar la carpeta raíz del proyecto (la que contiene `pom.xml`).
3. Esperar a que el *Extension Pack for Java* importe el proyecto Maven.
4. Abrir **Run and Debug** (Ctrl+Shift+D), elegir la configuración **"Sistema de Turnos"** y presionar ▶.
   La configuración (`.vscode/launch.json`) usa la terminal integrada para poder escribir en el menú y ejecuta desde la carpeta raíz, de modo que se use `data/turnos.db`.

> Importante: el programa debe ejecutarse **desde la carpeta raíz del proyecto**, porque la base de datos se ubica en la ruta relativa `data/turnos.db`.

### Ejecutar desde la terminal

```bash
mvn compile exec:java
```

O generar un JAR ejecutable con las dependencias incluidas:

```bash
mvn package
```

```bash
java -jar target/proyectofinal-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Base de datos

- Archivo: `data/turnos.db` (se entrega con los 5 clientes, los 3 servicios y sin turnos).
- Script: `sql/crear_y_cargar.sql` (tablas `clientes`, `servicios`, `turnos`, claves primarias, claves foráneas y datos iniciales).

### Crear la base de datos desde el script

Opción 1, con la CLI de SQLite (desde la carpeta raíz):

```bash
sqlite3 data/turnos.db < sql/crear_y_cargar.sql
```

Opción 2, automática: si `data/turnos.db` no existe (o no tiene tablas), la aplicación ejecuta `sql/crear_y_cargar.sql` al iniciar y muestra el aviso *"Se creó la base de datos con los datos iniciales."*.

Para volver a empezar desde cero, elimine `data/turnos.db` y use cualquiera de las dos opciones. El script es idempotente (`CREATE TABLE IF NOT EXISTS` / `INSERT OR IGNORE`): **nunca borra datos existentes**, y la aplicación tampoco restablece la base al iniciar.

## Estructura del proyecto

```
proyectofinal/
├── pom.xml
├── data/turnos.db
├── sql/crear_y_cargar.sql
├── docs/
│   ├── diagrama_uml.md        Diagrama UML de clases (Mermaid)
│   ├── diagrama_er.md         Diagrama entidad-relación (Mermaid)
│   ├── informe.md             Resultado de la secuencia de comprobación
│   └── evidencias/            Salidas de consola de la comprobación
└── src/main/java/estudiantes/proyectofinal/
    ├── Proyectofinal.java     Punto de entrada (arma dependencias y abre el menú)
    ├── modelo/                Cliente, Servicio, Turno, EstadoTurno, ResumenServicio
    ├── estructuras/           Nodo<T>, ColaTurnos (cola enlazada), NodoArbol, ArbolClientes (BST)
    ├── datos/                 ConexionSQLite, InicializadorBD, interfaces DAO
    │   └── sqlite/            Implementaciones JDBC de los DAO
    ├── negocio/               ClienteService, ServicioService, TurnoService, ReporteService
    ├── vista/                 Consola, MenuPrincipal, MenuClientes, MenuServicios, PresentadorTurnos
    └── util/                  Fechas
```

## Decisiones de diseño

- **Cola propia** (`ColaTurnos` + `Nodo<T>`): encolar, desencolar, ver primero, buscar, cancelar por número (conservando el orden), recorrer y comprobar si está vacía. No se usa `LinkedList` ni `ArrayDeque`.
- **BST propio** (`ArbolClientes` + `NodoArbol`): clave = código del cliente. Inserción, búsqueda y recorrido inorden recursivos. Los clientes inactivos permanecen en el árbol.
- **Colecciones de Java**: `ArrayList` para resultados de consultas, `LinkedHashMap` en `ReporteService` para agrupar el resumen por servicio (incluye servicios sin turnos en cero) y `HashMap` en `PresentadorTurnos` para resolver nombres por código.
- **Interfaces**: `ClienteDAO`, `ServicioDAO` y `TurnoDAO`, implementadas en `datos.sqlite`. Los servicios de negocio dependen de las interfaces, no de las implementaciones.
- **Consistencia**: cada operación se guarda primero en SQLite y, solo si el guardado tiene éxito, se actualiza la cola o el BST. Si falla, las estructuras en memoria no muestran cambios no guardados.
- **Inicio**: se construye el BST con los clientes de la base y se reconstruye la cola con los turnos `PENDIENTE` ordenados por número.
- **Números de turno**: `INTEGER PRIMARY KEY AUTOINCREMENT`, que nunca se reutilizan.
- **JDBC**: `PreparedStatement` en todas las operaciones con parámetros, `ResultSet` para las consultas, `try-with-resources` para cerrar conexiones y recursos, y `PRAGMA foreign_keys = ON` en cada conexión.

## Distribución del trabajo

| Integrante | Aportes |
|---|---|
| Integrante 1 | `Cliente`, `NodoArbol`, `ArbolClientes`, `ClienteDAO`/`ClienteDAOSQLite`, `ClienteService`, `MenuClientes`. |
| Integrante 2 | `Turno`, `EstadoTurno`, `Nodo`, `ColaTurnos`, `TurnoDAO`/`TurnoDAOSQLite`, `TurnoService`, opciones de turnos del menú principal. |
| Integrante 3 | `Servicio`, `ServicioDAO`/`ServicioDAOSQLite`, `ConexionSQLite`, `InicializadorBD`, script SQL, `ReporteService`, `MenuServicios`, diagramas. |

## Documentación

- [Diagrama UML de clases](docs/diagrama_uml.md)
- [Diagrama entidad-relación](docs/diagrama_er.md)
- [Informe de la secuencia de comprobación](docs/informe.md)
