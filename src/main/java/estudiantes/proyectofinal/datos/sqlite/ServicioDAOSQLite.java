package estudiantes.proyectofinal.datos.sqlite;

import estudiantes.proyectofinal.datos.ConexionSQLite;
import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.ServicioDAO;
import estudiantes.proyectofinal.modelo.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC/SQLite de {@link ServicioDAO}.
 */
public class ServicioDAOSQLite implements ServicioDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO servicios (codigo, nombre, estado) VALUES (?, ?, ?)";
    private static final String SQL_ACTUALIZAR_NOMBRE =
            "UPDATE servicios SET nombre = ? WHERE codigo = ?";
    private static final String SQL_ACTUALIZAR_ESTADO =
            "UPDATE servicios SET estado = ? WHERE codigo = ?";
    private static final String SQL_BUSCAR =
            "SELECT codigo, nombre, estado FROM servicios WHERE codigo = ?";
    private static final String SQL_LISTAR =
            "SELECT codigo, nombre, estado FROM servicios ORDER BY codigo";

    @Override
    public void insertar(Servicio servicio) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            ps.setString(3, EstadoRegistro.aTexto(servicio.isActivo()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el servicio " + servicio.getCodigo() + ".", e);
        }
    }

    @Override
    public void actualizarNombre(int codigo, String nombre) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_ACTUALIZAR_NOMBRE)) {
            ps.setString(1, nombre);
            ps.setInt(2, codigo);
            verificarUnaFila(ps.executeUpdate(), codigo);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el servicio " + codigo + ".", e);
        }
    }

    @Override
    public void actualizarEstado(int codigo, boolean activo) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
            ps.setString(1, EstadoRegistro.aTexto(activo));
            ps.setInt(2, codigo);
            verificarUnaFila(ps.executeUpdate(), codigo);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo cambiar el estado del servicio " + codigo + ".", e);
        }
    }

    @Override
    public Servicio buscarPorCodigo(int codigo) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_BUSCAR)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el servicio " + codigo + ".", e);
        }
    }

    @Override
    public List<Servicio> listarTodos() throws PersistenciaException {
        List<Servicio> servicios = new ArrayList<>();
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicios.add(mapear(rs));
            }
            return servicios;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar los servicios.", e);
        }
    }

    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(rs.getInt("codigo"), rs.getString("nombre"), EstadoRegistro.esActivo(rs.getString("estado")));
    }

    private void verificarUnaFila(int filas, int codigo) throws PersistenciaException {
        if (filas != 1) {
            throw new PersistenciaException("El servicio " + codigo + " no existe en la base de datos.");
        }
    }
}
