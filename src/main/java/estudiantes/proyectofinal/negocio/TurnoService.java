package estudiantes.proyectofinal.negocio;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.estructuras.ColaTurnos;
import estudiantes.proyectofinal.modelo.Cliente;
import estudiantes.proyectofinal.modelo.EstadoTurno;
import estudiantes.proyectofinal.modelo.Servicio;
import estudiantes.proyectofinal.modelo.Turno;
import estudiantes.proyectofinal.util.Fechas;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Coordina la generación, atención y cancelación de turnos.
 * Cada cambio se guarda primero en SQLite; la cola solo se modifica si el guardado tuvo éxito.
 */
public class TurnoService {

    private final TurnoDAO turnoDAO;
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final ColaTurnos cola;

    public TurnoService(TurnoDAO turnoDAO, ClienteService clienteService, ServicioService servicioService,
            ColaTurnos cola) {
        this.turnoDAO = turnoDAO;
        this.clienteService = clienteService;
        this.servicioService = servicioService;
        this.cola = cola;
    }

    /** Reconstruye la cola con los turnos pendientes guardados, ordenados por número. */
    public int cargarPendientes() throws PersistenciaException {
        for (Turno turno : turnoDAO.listarPorEstado(EstadoTurno.PENDIENTE)) {
            cola.encolar(turno);
        }
        return cola.getTamanio();
    }

    public Turno generar(int codigoCliente, int codigoServicio) throws NegocioException, PersistenciaException {
        Cliente cliente = clienteService.buscar(codigoCliente);
        if (!cliente.isActivo()) {
            throw new NegocioException("El cliente " + codigoCliente + " está inactivo y no puede recibir turnos.");
        }
        Servicio servicio = servicioService.buscar(codigoServicio);
        if (!servicio.isActivo()) {
            throw new NegocioException("El servicio " + codigoServicio + " está inactivo.");
        }
        if (cola.contieneCliente(codigoCliente) || turnoDAO.contarPendientesPorCliente(codigoCliente) > 0) {
            throw new NegocioException("El cliente " + codigoCliente + " ya tiene un turno pendiente.");
        }
        Turno turno = turnoDAO.insertar(codigoCliente, codigoServicio, Fechas.ahora());
        cola.encolar(turno);
        return turno;
    }

    /**
     * Atiende el primer turno de la cola (FIFO).
     *
     * @return el turno atendido o null si no hay turnos pendientes
     */
    public Turno atenderSiguiente() throws NegocioException, PersistenciaException {
        Turno primero = cola.verPrimero();
        if (primero == null) {
            return null;
        }
        LocalDateTime ahora = Fechas.ahora();
        guardarFinalizacion(primero, EstadoTurno.ATENDIDO, ahora);
        primero.marcarAtendido(ahora);
        cola.desencolar();
        return primero;
    }

    public Turno cancelar(int numero) throws NegocioException, PersistenciaException {
        Turno turno = cola.buscar(numero);
        if (turno == null) {
            Turno guardado = turnoDAO.buscarPorNumero(numero);
            if (guardado == null) {
                throw new NegocioException("No existe un turno con el número " + numero + ".");
            }
            throw new NegocioException("El turno " + numero + " no se puede cancelar porque está "
                    + guardado.getEstado() + ".");
        }
        LocalDateTime ahora = Fechas.ahora();
        guardarFinalizacion(turno, EstadoTurno.CANCELADO, ahora);
        turno.marcarCancelado(ahora);
        cola.cancelar(numero);
        return turno;
    }

    private void guardarFinalizacion(Turno turno, EstadoTurno estado, LocalDateTime fecha)
            throws NegocioException, PersistenciaException {
        if (!turnoDAO.finalizar(turno.getNumero(), estado, fecha)) {
            throw new NegocioException("El turno " + turno.getNumero() + " ya no está pendiente en la base de datos.");
        }
    }

    /** Turnos pendientes en orden de atención, obtenidos al recorrer la cola. */
    public List<Turno> listarPendientes() {
        return cola.aLista();
    }

    public List<Turno> listarAtendidos() throws PersistenciaException {
        return turnoDAO.listarPorEstado(EstadoTurno.ATENDIDO);
    }

    public List<Turno> listarCancelados() throws PersistenciaException {
        return turnoDAO.listarPorEstado(EstadoTurno.CANCELADO);
    }

    public List<Turno> historialCliente(int codigoCliente) throws NegocioException, PersistenciaException {
        clienteService.buscar(codigoCliente);
        return turnoDAO.listarPorCliente(codigoCliente);
    }
}
