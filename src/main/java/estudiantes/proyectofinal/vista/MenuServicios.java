package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.modelo.Servicio;
import estudiantes.proyectofinal.negocio.NegocioException;
import estudiantes.proyectofinal.negocio.ServicioService;
import java.util.List;

/**
 * Submenú de gestión de servicios.
 */
public class MenuServicios extends MenuBase {

    private final ServicioService servicioService;

    public MenuServicios(Consola consola, ServicioService servicioService) {
        super(consola);
        this.servicioService = servicioService;
    }

    public void mostrar() {
        int opcion;
        do {
            consola.titulo("GESTIÓN DE SERVICIOS");
            consola.mostrar("1. Registrar servicio");
            consola.mostrar("2. Listar servicios");
            consola.mostrar("3. Actualizar nombre");
            consola.mostrar("4. Activar / desactivar servicio");
            consola.mostrar("0. Regresar al menú principal");
            opcion = consola.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> ejecutarSeguro(this::registrar);
                case 2 -> ejecutarSeguro(this::listar);
                case 3 -> ejecutarSeguro(this::actualizar);
                case 4 -> ejecutarSeguro(this::cambiarEstado);
                case 0 -> { }
                default -> consola.error("La opción " + opcion + " no existe.");
            }
        } while (opcion != 0);
    }

    private void registrar() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del servicio: ");
        String nombre = consola.leerTexto("Nombre del servicio: ");
        Servicio servicio = servicioService.registrar(codigo, nombre);
        consola.exito("Servicio registrado: " + servicio);
    }

    private void listar() throws PersistenciaException {
        List<Servicio> servicios = servicioService.listar();
        if (servicios.isEmpty()) {
            consola.aviso("No hay servicios registrados.");
            return;
        }
        consola.formato("%-8s %-30s %-8s%n", "Código", "Nombre", "Estado");
        consola.mostrar("-".repeat(48));
        for (Servicio servicio : servicios) {
            consola.formato("%-8d %-30s %-8s%n", servicio.getCodigo(), servicio.getNombre(), servicio.getEstadoTexto());
        }
    }

    private void actualizar() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del servicio a actualizar: ");
        Servicio actual = servicioService.buscar(codigo);
        consola.mostrar("Datos actuales: " + actual);
        String nombre = consola.leerTexto("Nuevo nombre: ");
        Servicio servicio = servicioService.actualizar(codigo, nombre);
        consola.exito("Servicio actualizado: " + servicio);
    }

    private void cambiarEstado() throws NegocioException, PersistenciaException {
        int codigo = consola.leerEntero("Código del servicio: ");
        Servicio actual = servicioService.buscar(codigo);
        consola.mostrar("Estado actual: " + actual.getEstadoTexto());
        int opcion = consola.leerEntero("1. Activar  2. Desactivar: ");
        if (opcion != 1 && opcion != 2) {
            consola.error("La opción " + opcion + " no existe.");
            return;
        }
        Servicio servicio = servicioService.cambiarEstado(codigo, opcion == 1);
        consola.exito("El servicio " + codigo + " ahora está " + servicio.getEstadoTexto().toLowerCase() + ".");
    }
}
