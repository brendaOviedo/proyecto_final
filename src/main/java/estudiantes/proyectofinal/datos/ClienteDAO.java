package estudiantes.proyectofinal.datos;

import estudiantes.proyectofinal.modelo.Cliente;
import java.util.List;

/**
 * Operaciones de persistencia de clientes.
 */
public interface ClienteDAO {

    void insertar(Cliente cliente) throws PersistenciaException;

    void actualizarDatos(int codigo, String nombre, String telefono) throws PersistenciaException;

    void actualizarEstado(int codigo, boolean activo) throws PersistenciaException;

    List<Cliente> listarTodos() throws PersistenciaException;
}
