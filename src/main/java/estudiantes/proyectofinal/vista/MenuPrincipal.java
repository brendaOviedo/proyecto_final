package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.modelo.Cliente;
import estudiantes.proyectofinal.modelo.ResumenServicio;
import estudiantes.proyectofinal.modelo.Turno;
import estudiantes.proyectofinal.negocio.ClienteService;
import estudiantes.proyectofinal.negocio.NegocioException;
import estudiantes.proyectofinal.negocio.ReporteService;
import estudiantes.proyectofinal.negocio.ServicioService;
import estudiantes.proyectofinal.negocio.TurnoService;
import java.util.List;

/**
 * Menú principal del sistema de turnos.
 */
public class MenuPrincipal extends MenuBase {

    public static final String NOMBRE_OFICINA = "OFICINA DE SERVICIOS INTEGRALES";

    private final ClienteService clienteService;
    private final TurnoService turnoService;
    private final ReporteService reporteService;
    private final MenuClientes menuClientes;
    private final MenuServicios menuServicios;
    private final PresentadorTurnos presentador;

    public MenuPrincipal(Consola consola, ClienteService clienteService, ServicioService servicioService,
            TurnoService turnoService, ReporteService reporteService) {
        super(consola);
        this.clienteService = clienteService;
        this.turnoService = turnoService;
        this.reporteService = reporteService;
        this.menuClientes = new MenuClientes(consola, clienteService);
        this.menuServicios = new MenuServicios(consola, servicioService);
        this.presentador = new PresentadorTurnos(consola, clienteService, servicioService);
    }

    public void iniciar() {
        int opcion;
        do {
            consola.titulo(NOMBRE_OFICINA + " - SISTEMA DE TURNOS");
            consola.mostrar(" 1. Gestión de clientes");
            consola.mostrar(" 2. Gestión de servicios");
            consola.mostrar(" 3. Generar turno");
            consola.mostrar(" 4. Atender siguiente turno");
            consola.mostrar(" 5. Cancelar turno");
            consola.mostrar(" 6. Consultar turnos pendientes");
            consola.mostrar(" 7. Consultar turnos atendidos");
            consola.mostrar(" 8. Consultar turnos cancelados");
            consola.mostrar(" 9. Historial de un cliente");
            consola.mostrar("10. Resumen por servicio");
            consola.mostrar(" 0. Salir");
            opcion = consola.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> menuClientes.mostrar();
                case 2 -> menuServicios.mostrar();
                case 3 -> ejecutarSeguro(this::generarTurno);
                case 4 -> ejecutarSeguro(this::atenderSiguiente);
                case 5 -> ejecutarSeguro(this::cancelarTurno);
                case 6 -> ejecutarSeguro(this::consultarPendientes);
                case 7 -> ejecutarSeguro(this::consultarAtendidos);
                case 8 -> ejecutarSeguro(this::consultarCancelados);
                case 9 -> ejecutarSeguro(this::consultarHistorial);
                case 10 -> ejecutarSeguro(this::mostrarResumen);
                case 0 -> consola.mostrar("Gracias por utilizar el sistema. ¡Hasta pronto!");
                default -> consola.error("La opción " + opcion + " no existe.");
            }
        } while (opcion != 0);
    }

    private void generarTurno() throws NegocioException, PersistenciaException {
        consola.titulo("GENERAR TURNO");
        int codigoCliente = consola.leerEntero("Código del cliente: ");
        int codigoServicio = consola.leerEntero("Código del servicio: ");
        Turno turno = turnoService.generar(codigoCliente, codigoServicio);
        consola.exito("Turno generado. " + presentador.describir(turno));
    }

    private void atenderSiguiente() throws NegocioException, PersistenciaException {
        consola.titulo("ATENDER SIGUIENTE TURNO");
        Turno turno = turnoService.atenderSiguiente();
        if (turno == null) {
            consola.aviso("No existen turnos pendientes.");
            return;
        }
        consola.exito("Atendiendo: " + presentador.describir(turno));
    }

    private void cancelarTurno() throws NegocioException, PersistenciaException {
        consola.titulo("CANCELAR TURNO");
        int numero = consola.leerEntero("Número de turno a cancelar: ");
        Turno turno = turnoService.cancelar(numero);
        consola.exito("Turno cancelado. " + presentador.describir(turno));
    }

    private void consultarPendientes() throws PersistenciaException {
        consola.titulo("TURNOS PENDIENTES (orden de atención)");
        presentador.imprimirTabla(turnoService.listarPendientes(), "No existen turnos pendientes.");
    }

    private void consultarAtendidos() throws PersistenciaException {
        consola.titulo("TURNOS ATENDIDOS");
        presentador.imprimirTabla(turnoService.listarAtendidos(), "No existen turnos atendidos.");
    }

    private void consultarCancelados() throws PersistenciaException {
        consola.titulo("TURNOS CANCELADOS");
        presentador.imprimirTabla(turnoService.listarCancelados(), "No existen turnos cancelados.");
    }

    private void consultarHistorial() throws NegocioException, PersistenciaException {
        consola.titulo("HISTORIAL DE UN CLIENTE");
        int codigo = consola.leerEntero("Código del cliente: ");
        Cliente cliente = clienteService.buscar(codigo);
        consola.mostrar("Cliente: " + cliente);
        presentador.imprimirTabla(turnoService.historialCliente(codigo), "El cliente no tiene turnos registrados.");
    }

    private void mostrarResumen() throws PersistenciaException {
        consola.titulo("RESUMEN POR SERVICIO");
        List<ResumenServicio> resumenes = reporteService.resumenPorServicio();
        String formato = "%-28s %10s %10s %10s %7s%n";
        consola.formato(formato, "Servicio", "Pendientes", "Atendidos", "Cancelados", "Total");
        consola.mostrar("-".repeat(69));
        for (ResumenServicio resumen : resumenes) {
            imprimirResumen(formato, resumen);
        }
        consola.mostrar("-".repeat(69));
        imprimirResumen(formato, reporteService.calcularTotal(resumenes));
    }

    private void imprimirResumen(String formato, ResumenServicio resumen) {
        consola.formato(formato, resumen.getNombreServicio(), resumen.getPendientes(), resumen.getAtendidos(),
                resumen.getCancelados(), resumen.getTotal());
    }
}
