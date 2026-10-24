package estudiantes.proyectofinal;

import estudiantes.proyectofinal.datos.ClienteDAO;
import estudiantes.proyectofinal.datos.InicializadorBD;
import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.ServicioDAO;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.datos.sqlite.ClienteDAOSQLite;
import estudiantes.proyectofinal.datos.sqlite.ServicioDAOSQLite;
import estudiantes.proyectofinal.datos.sqlite.TurnoDAOSQLite;
import estudiantes.proyectofinal.estructuras.ArbolClientes;
import estudiantes.proyectofinal.estructuras.ColaTurnos;
import estudiantes.proyectofinal.negocio.ClienteService;
import estudiantes.proyectofinal.negocio.ReporteService;
import estudiantes.proyectofinal.negocio.ServicioService;
import estudiantes.proyectofinal.negocio.TurnoService;
import estudiantes.proyectofinal.vista.Consola;
import estudiantes.proyectofinal.vista.EntradaFinalizadaException;
import estudiantes.proyectofinal.vista.MenuPrincipal;

/**
 * Punto de entrada: crea las dependencias, carga el BST y la cola desde SQLite e inicia el menú.
 */
public class Proyectofinal {

    public static void main(String[] args) {
        Consola consola = new Consola(System.in);
        try {
            if (InicializadorBD.inicializarSiEsNecesario()) {
                consola.aviso("Se creó la base de datos con los datos iniciales.");
            }

            ClienteDAO clienteDAO = new ClienteDAOSQLite();
            ServicioDAO servicioDAO = new ServicioDAOSQLite();
            TurnoDAO turnoDAO = new TurnoDAOSQLite();

            ClienteService clienteService = new ClienteService(clienteDAO, turnoDAO, new ArbolClientes());
            ServicioService servicioService = new ServicioService(servicioDAO, turnoDAO);
            TurnoService turnoService = new TurnoService(turnoDAO, clienteService, servicioService, new ColaTurnos());
            ReporteService reporteService = new ReporteService(servicioDAO, turnoDAO);

            int clientes = clienteService.cargarClientes();
            int pendientes = turnoService.cargarPendientes();
            consola.mostrar("Clientes cargados en el BST: " + clientes + " | Turnos pendientes en cola: " + pendientes);

            new MenuPrincipal(consola, clienteService, servicioService, turnoService, reporteService).iniciar();
        } catch (PersistenciaException e) {
            consola.error(e.getMessage());
            if (e.getCause() != null) {
                consola.error("Detalle: " + e.getCause().getMessage());
            }
        } catch (EntradaFinalizadaException e) {
            consola.mostrar("");
            consola.aviso(e.getMessage() + " Programa terminado.");
        }
    }
}
