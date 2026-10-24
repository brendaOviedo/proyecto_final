package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.modelo.Cliente;
import estudiantes.proyectofinal.modelo.Servicio;
import estudiantes.proyectofinal.modelo.Turno;
import estudiantes.proyectofinal.negocio.ClienteService;
import estudiantes.proyectofinal.negocio.ServicioService;
import estudiantes.proyectofinal.util.Fechas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Presenta turnos en forma de tabla, mostrando el nombre del cliente y del servicio.
 * Usa HashMap para resolver los nombres por código sin consultar la base de datos por cada fila.
 */
public class PresentadorTurnos {

    private static final String FORMATO_FILA = "%-6s %-24s %-28s %-10s %-19s %-19s%n";

    private final Consola consola;
    private final ClienteService clienteService;
    private final ServicioService servicioService;

    public PresentadorTurnos(Consola consola, ClienteService clienteService, ServicioService servicioService) {
        this.consola = consola;
        this.clienteService = clienteService;
        this.servicioService = servicioService;
    }

    public void imprimirTabla(List<Turno> turnos, String mensajeSinDatos) throws PersistenciaException {
        if (turnos.isEmpty()) {
            consola.aviso(mensajeSinDatos);
            return;
        }
        Map<Integer, String> clientes = nombresClientes();
        Map<Integer, String> servicios = nombresServicios();
        consola.formato(FORMATO_FILA, "Turno", "Cliente", "Servicio", "Estado", "Generado", "Atendido/Cancelado");
        consola.mostrar("-".repeat(111));
        for (Turno turno : turnos) {
            consola.formato(FORMATO_FILA,
                    turno.getNumero(),
                    describir(turno.getCodigoCliente(), clientes),
                    describir(turno.getCodigoServicio(), servicios),
                    turno.getEstado(),
                    Fechas.formatear(turno.getFechaGeneracion()),
                    turno.getFechaFinalizacion() == null ? "-" : Fechas.formatear(turno.getFechaFinalizacion()));
        }
        consola.mostrar("Total: " + turnos.size());
    }

    /** Descripción de una línea: número, cliente y servicio. */
    public String describir(Turno turno) throws PersistenciaException {
        return "Turno #" + turno.getNumero()
                + " | Cliente: " + describir(turno.getCodigoCliente(), nombresClientes())
                + " | Servicio: " + describir(turno.getCodigoServicio(), nombresServicios());
    }

    private Map<Integer, String> nombresClientes() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Cliente cliente : clienteService.listar()) {
            nombres.put(cliente.getCodigo(), cliente.getNombre());
        }
        return nombres;
    }

    private Map<Integer, String> nombresServicios() throws PersistenciaException {
        Map<Integer, String> nombres = new HashMap<>();
        for (Servicio servicio : servicioService.listar()) {
            nombres.put(servicio.getCodigo(), servicio.getNombre());
        }
        return nombres;
    }

    private String describir(int codigo, Map<Integer, String> nombres) {
        return codigo + " - " + nombres.getOrDefault(codigo, "(desconocido)");
    }
}
