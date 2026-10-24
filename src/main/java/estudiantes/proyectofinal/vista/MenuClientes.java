package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.modelo.Cliente;
import estudiantes.proyectofinal.negocio.ClienteService;
import estudiantes.proyectofinal.negocio.NegocioException;
import java.util.List;

/**
 * Submenú de gestión de clientes.
 */
public class MenuClientes extends MenuBase {

    private final ClienteService clienteService;

    public MenuClientes(Consola consola, ClienteService clienteService) {
        super(consola);
        this.clienteService = clienteService;
    }

    public void mostrar() {
        int opcion;
        do {
            consola.titulo("GESTIÓN DE CLIENTES");
            consola.mostrar("1. Registrar cliente");
            consola.mostrar("2. Consultar todos los clientes (inorden BST)");
            consola.mostrar("3. Buscar cliente por código (BST)");
            consola.mostrar("4. Actualizar nombre y teléfono");
            consola.mostrar("5. Activar / desactivar cliente");
            consola.mostrar("0. Regresar al menú principal");
            opcion = consola.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> ejecutarSeguro(this::registrar);
                case 2 -> listar();
                case 3 -> ejecutarSeguro(this::buscar);
                case 4 -> ejecutarSeguro(this::actualizar);
                case 5 -> ejecutarSeguro(this::cambiarEstado);
                case 0 -> { }
                default -> consola.error("La opción " + opcion + " no existe.");
            }
        } while (opcion != 0);
    }

    private void registrar() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del cliente: ");
        String nombre = consola.leerTexto("Nombre completo: ");
        String telefono = consola.leerTexto("Teléfono (8 dígitos): ");
        Cliente cliente = clienteService.registrar(codigo, nombre, telefono);
        consola.exito("Cliente registrado: " + cliente);
    }

    private void listar() {
        List<Cliente> clientes = clienteService.listar();
        if (clientes.isEmpty()) {
            consola.aviso("No hay clientes registrados.");
            return;
        }
        imprimirEncabezado();
        clientes.forEach(this::imprimirFila);
        consola.mostrar("Total de clientes: " + clientes.size());
    }

    private void buscar() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del cliente a buscar: ");
        Cliente cliente = clienteService.buscar(codigo);
        imprimirEncabezado();
        imprimirFila(cliente);
    }

    private void actualizar() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del cliente a actualizar: ");
        Cliente actual = clienteService.buscar(codigo);
        consola.mostrar("Datos actuales: " + actual);
        String nombre = consola.leerTexto("Nuevo nombre completo: ");
        String telefono = consola.leerTexto("Nuevo teléfono (8 dígitos): ");
        Cliente cliente = clienteService.actualizar(codigo, nombre, telefono);
        consola.exito("Cliente actualizado: " + cliente);
    }

    private void cambiarEstado() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del cliente: ");
        Cliente actual = clienteService.buscar(codigo);
        consola.mostrar("Estado actual: " + actual.getEstadoTexto());
        int opcion = consola.leerEntero("1. Activar  2. Desactivar: ");
        if (opcion != 1 && opcion != 2) {
            consola.error("La opción " + opcion + " no existe.");
            return;
        }
        Cliente cliente = clienteService.cambiarEstado(codigo, opcion == 1);
        consola.exito("El cliente " + codigo + " ahora está " + cliente.getEstadoTexto().toLowerCase() + ".");
    }

    private void imprimirEncabezado() {
        consola.formato("%-8s %-30s %-10s %-8s%n", "Código", "Nombre", "Teléfono", "Estado");
        consola.mostrar("-".repeat(60));
    }

    private void imprimirFila(Cliente cliente) {
        consola.formato("%-8d %-30s %-10s %-8s%n", cliente.getCodigo(), cliente.getNombre(),
                cliente.getTelefono(), cliente.getEstadoTexto());
    }
}
