package estudiantes.proyectofinal.datos;

import estudiantes.proyectofinal.modelo.EstadoTurno;
import estudiantes.proyectofinal.modelo.Turno;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Operaciones de persistencia de turnos.
 */
public interface TurnoDAO {

    /** Inserta un turno PENDIENTE y devuelve el turno con el número generado por la base de datos. */
    Turno insertar(int codigoCliente, int codigoServicio, LocalDateTime fechaGeneracion) throws PersistenciaException;

    /**
     * Cambia un turno PENDIENTE al estado final indicado.
     *
     * @return false si el turno no existía o ya no estaba pendiente
     */
    boolean finalizar(int numero, EstadoTurno nuevoEstado, LocalDateTime fecha) throws PersistenciaException;

    /** @return el turno o null si no existe */
    Turno buscarPorNumero(int numero) throws PersistenciaException;

    /** Turnos con el estado indicado, ordenados por número. */
    List<Turno> listarPorEstado(EstadoTurno estado) throws PersistenciaException;

    List<Turno> listarPorCliente(int codigoCliente) throws PersistenciaException;

    List<Turno> listarTodos() throws PersistenciaException;

    int contarPendientesPorCliente(int codigoCliente) throws PersistenciaException;

    int contarPendientesPorServicio(int codigoServicio) throws PersistenciaException;
}
