package estudiantes.proyectofinal.datos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Crea las tablas y carga los datos iniciales solo cuando la base de datos todavía no existe.
 * Nunca borra ni restablece información existente.
 */
public final class InicializadorBD {

    public static final Path RUTA_SCRIPT = Path.of("sql", "crear_y_cargar.sql");

    private InicializadorBD() {
    }

    /** @return true si fue necesario crear la base de datos desde el script */
    public static boolean inicializarSiEsNecesario() throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion()) {
            if (existeTabla(conexion, "clientes")) {
                return false;
            }
            ejecutarScript(conexion);
            return true;
        } catch (SQLException e) {
            throw new PersistenciaException("No fue posible inicializar la base de datos.", e);
        }
    }

    private static boolean existeTabla(Connection conexion, String tabla) throws SQLException {
        DatabaseMetaData metadatos = conexion.getMetaData();
        try (ResultSet rs = metadatos.getTables(null, null, tabla, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static void ejecutarScript(Connection conexion) throws PersistenciaException, SQLException {
        String script;
        try {
            script = Files.readString(RUTA_SCRIPT, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("No se encontró el script " + RUTA_SCRIPT
                    + ". Ejecute la aplicación desde la carpeta raíz del proyecto.", e);
        }
        conexion.setAutoCommit(false);
        try (Statement sentencia = conexion.createStatement()) {
            for (String instruccion : separarInstrucciones(script)) {
                sentencia.execute(instruccion);
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    /** Quita los comentarios de línea y separa el script por punto y coma. */
    private static String[] separarInstrucciones(String script) {
        StringBuilder limpio = new StringBuilder();
        for (String linea : script.split("\\R")) {
            String recortada = linea.strip();
            if (!recortada.startsWith("--") && !recortada.toUpperCase().startsWith("PRAGMA")) {
                limpio.append(linea).append('\n');
            }
        }
        return Arrays.stream(limpio.toString().split(";"))
                .map(String::strip)
                .filter(instruccion -> !instruccion.isEmpty())
                .toArray(String[]::new);
    }
}
