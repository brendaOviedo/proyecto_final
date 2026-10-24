package estudiantes.proyectofinal.datos.sqlite;

import estudiantes.proyectofinal.datos.ConexionSQLite;
import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.modelo.EstadoTurno;
import estudiantes.proyectofinal.modelo.Turno;
import estudiantes.proyectofinal.util.Fechas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC/SQLite de {@link TurnoDAO}.
 * El número de turno es INTEGER PRIMARY KEY AUTOINCREMENT, por lo que SQLite nunca lo reutiliza.
 */
public class TurnoDAOSQLite implements TurnoDAO {

    private static final String COLUMNAS =
            "numero, codigo_cliente, codigo_servicio, fecha_generacion, estado, fecha_finalizacion";
    private static final String SQL_INSERTAR =
            "INSERT INTO turnos (codigo_cliente, codigo_servicio, fecha_generacion, estado) VALUES (?, ?, ?, 'PENDIENTE')";
    private static final String SQL_FINALIZAR =
            "UPDATE turnos SET estado = ?, fecha_finalizacion = ? WHERE numero = ? AND estado = 'PENDIENTE'";
    private static final String SQL_BUSCAR =
            "SELECT " + COLUMNAS + " FROM turnos WHERE numero = ?";
    private static final String SQL_LISTAR_POR_ESTADO =
            "SELECT " + COLUMNAS + " FROM turnos WHERE estado = ? ORDER BY numero";
    private static final String SQL_LISTAR_POR_CLIENTE =
            "SELECT " + COLUMNAS + " FROM turnos WHERE codigo_cliente = ? ORDER BY numero";
    private static final String SQL_LISTAR_TODOS =
            "SELECT " + COLUMNAS + " FROM turnos ORDER BY numero";
    private static final String SQL_CONTAR_PENDIENTES_CLIENTE =
            "SELECT COUNT(*) FROM turnos WHERE codigo_cliente = ? AND estado = 'PENDIENTE'";
    private static final String SQL_CONTAR_PENDIENTES_SERVICIO =
            "SELECT COUNT(*) FROM turnos WHERE codigo_servicio = ? AND estado = 'PENDIENTE'";

    @Override
    public Turno insertar(int codigoCliente, int codigoServicio, LocalDateTime fechaGeneracion)
            throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, codigoCliente);
            ps.setInt(2, codigoServicio);
            ps.setString(3, Fechas.formatear(fechaGeneracion));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new PersistenciaException("La base de datos no devolvió el número del turno.");
                }
                return new Turno(claves.getInt(1), codigoCliente, codigoServicio, fechaGeneracion,
                        EstadoTurno.PENDIENTE, null);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el turno.", e);
        }
    }

    @Override
    public boolean finalizar(int numero, EstadoTurno nuevoEstado, LocalDateTime fecha) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_FINALIZAR)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, Fechas.formatear(fecha));
            ps.setInt(3, numero);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el turno " + numero + ".", e);
        }
    }

    @Override
    public Turno buscarPorNumero(int numero) throws PersistenciaException {
        List<Turno> resultado = consultar(SQL_BUSCAR, numero);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public List<Turno> listarPorEstado(EstadoTurno estado) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR_POR_ESTADO)) {
            ps.setString(1, estado.name());
            return leerTurnos(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar los turnos " + estado + ".", e);
        }
    }

    @Override
    public List<Turno> listarPorCliente(int codigoCliente) throws PersistenciaException {
        return consultar(SQL_LISTAR_POR_CLIENTE, codigoCliente);
    }

    @Override
    public List<Turno> listarTodos() throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR_TODOS)) {
            return leerTurnos(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar los turnos.", e);
        }
    }

    @Override
    public int contarPendientesPorCliente(int codigoCliente) throws PersistenciaException {
        return contar(SQL_CONTAR_PENDIENTES_CLIENTE, codigoCliente);
    }

    @Override
    public int contarPendientesPorServicio(int codigoServicio) throws PersistenciaException {
        return contar(SQL_CONTAR_PENDIENTES_SERVICIO, codigoServicio);
    }

    /** Ejecuta una consulta de turnos que recibe un único parámetro entero. */
    private List<Turno> consultar(String sql, int parametro) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            return leerTurnos(ps);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar los turnos.", e);
        }
    }

    private int contar(String sql, int parametro) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron contar los turnos pendientes.", e);
        }
    }

    private List<Turno> leerTurnos(PreparedStatement ps) throws SQLException {
        List<Turno> turnos = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                turnos.add(new Turno(
                        rs.getInt("numero"),
                        rs.getInt("codigo_cliente"),
                        rs.getInt("codigo_servicio"),
                        Fechas.parsear(rs.getString("fecha_generacion")),
                        EstadoTurno.valueOf(rs.getString("estado")),
                        Fechas.parsear(rs.getString("fecha_finalizacion"))));
            }
        }
        return turnos;
    }
}
