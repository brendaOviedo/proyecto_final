package estudiantes.proyectofinal.datos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Crea conexiones JDBC a la base de datos SQLite data/turnos.db con las claves foráneas habilitadas.
 * Quien solicita la conexión es responsable de cerrarla (try-with-resources).
 */
public final class ConexionSQLite {

    public static final Path RUTA_BD = Path.of("data", "turnos.db");
    private static final String URL = "jdbc:sqlite:" + RUTA_BD.toString().replace('\\', '/');

    private ConexionSQLite() {
    }

    public static Connection obtenerConexion() throws PersistenciaException {
        try {
            Files.createDirectories(RUTA_BD.getParent());
            Connection conexion = DriverManager.getConnection(URL);
            try (Statement sentencia = conexion.createStatement()) {
                sentencia.execute("PRAGMA foreign_keys = ON");
            }
            return conexion;
        } catch (IOException | SQLException e) {
            throw new PersistenciaException("No fue posible conectar con la base de datos " + RUTA_BD, e);
        }
    }
}
