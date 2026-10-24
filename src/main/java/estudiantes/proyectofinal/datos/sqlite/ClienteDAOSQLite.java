package estudiantes.proyectofinal.datos.sqlite;

import estudiantes.proyectofinal.datos.ClienteDAO;
import estudiantes.proyectofinal.datos.ConexionSQLite;
import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC/SQLite de {@link ClienteDAO}.
 */
public class ClienteDAOSQLite implements ClienteDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO clientes (codigo, nombre, telefono, estado) VALUES (?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR_DATOS =
            "UPDATE clientes SET nombre = ?, telefono = ? WHERE codigo = ?";
    private static final String SQL_ACTUALIZAR_ESTADO =
            "UPDATE clientes SET estado = ? WHERE codigo = ?";
    private static final String SQL_LISTAR =
            "SELECT codigo, nombre, telefono, estado FROM clientes ORDER BY codigo";

    @Override
    public void insertar(Cliente cliente) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, cliente.getCodigo());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, EstadoRegistro.aTexto(cliente.isActivo()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el cliente " + cliente.getCodigo() + ".", e);
        }
    }

    @Override
    public void actualizarDatos(int codigo, String nombre, String telefono) throws PersistenciaException {
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_ACTUALIZAR_DATOS)) {
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setInt(3, codigo);
            verificarUnaFila(ps.executeUpdate(), codigo);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el cliente " + codigo + ".", e);
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
            throw new PersistenciaException("No se pudo cambiar el estado del cliente " + codigo + ".", e);
        }
    }

    @Override
    public List<Cliente> listarTodos() throws PersistenciaException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conexion = ConexionSQLite.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        EstadoRegistro.esActivo(rs.getString("estado"))));
            }
            return clientes;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron cargar los clientes.", e);
        }
    }

    private void verificarUnaFila(int filas, int codigo) throws PersistenciaException {
        if (filas != 1) {
            throw new PersistenciaException("El cliente " + codigo + " no existe en la base de datos.");
        }
    }
}
