package estudiantes.proyectofinal.negocio;

import estudiantes.proyectofinal.datos.ClienteDAO;
import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.estructuras.ArbolClientes;
import estudiantes.proyectofinal.modelo.Cliente;
import java.util.List;

/**
 * Reglas de negocio de clientes. Mantiene sincronizados el BST y la base de datos:
 * primero se guarda en SQLite y solo si tiene éxito se modifica el árbol.
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;
    private final TurnoDAO turnoDAO;
    private final ArbolClientes arbol;

    public ClienteService(ClienteDAO clienteDAO, TurnoDAO turnoDAO, ArbolClientes arbol) {
        this.clienteDAO = clienteDAO;
        this.turnoDAO = turnoDAO;
        this.arbol = arbol;
    }

    /** Construye el BST con los clientes almacenados en la base de datos. */
    public int cargarClientes() throws PersistenciaException {
        for (Cliente cliente : clienteDAO.listarTodos()) {
            arbol.insertar(cliente);
        }
        return arbol.getCantidad();
    }

    public Cliente registrar(int codigo, String nombre, String telefono)
            throws NegocioException, PersistenciaException {
        Validaciones.validarCodigo(codigo, "cliente");
        String nombreLimpio = Validaciones.validarTextoObligatorio(nombre, "nombre completo");
        String telefonoLimpio = Validaciones.validarTelefono(telefono);
        if (arbol.buscar(codigo) != null) {
            throw new NegocioException("Ya existe un cliente con el código " + codigo + ".");
        }
        Cliente cliente = new Cliente(codigo, nombreLimpio, telefonoLimpio, true);
        clienteDAO.insertar(cliente);
        arbol.insertar(cliente);
        return cliente;
    }

    /** Busca un cliente en el BST; lanza excepción si no está registrado. */
    public Cliente buscar(int codigo) throws NegocioException {
        Cliente cliente = arbol.buscar(codigo);
        if (cliente == null) {
            throw new NegocioException("No existe un cliente con el código " + codigo + ".");
        }
        return cliente;
    }

    /** Clientes ordenados por código mediante el recorrido inorden del BST. */
    public List<Cliente> listar() {
        return arbol.inorden();
    }

    public Cliente actualizar(int codigo, String nombre, String telefono)
            throws NegocioException, PersistenciaException {
        Cliente cliente = buscar(codigo);
        String nombreLimpio = Validaciones.validarTextoObligatorio(nombre, "nombre completo");
        String telefonoLimpio = Validaciones.validarTelefono(telefono);
        clienteDAO.actualizarDatos(codigo, nombreLimpio, telefonoLimpio);
        cliente.setNombre(nombreLimpio);
        cliente.setTelefono(telefonoLimpio);
        return cliente;
    }

    public Cliente cambiarEstado(int codigo, boolean activo) throws NegocioException, PersistenciaException {
        Cliente cliente = buscar(codigo);
        if (cliente.isActivo() == activo) {
            throw new NegocioException("El cliente " + codigo + " ya se encuentra " + cliente.getEstadoTexto().toLowerCase() + ".");
        }
        if (!activo && turnoDAO.contarPendientesPorCliente(codigo) > 0) {
            throw new NegocioException("No se puede desactivar al cliente " + codigo + " porque tiene un turno pendiente.");
        }
        clienteDAO.actualizarEstado(codigo, activo);
        cliente.setActivo(activo);
        return cliente;
    }
}
