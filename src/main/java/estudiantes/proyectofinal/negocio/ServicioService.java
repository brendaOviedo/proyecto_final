package estudiantes.proyectofinal.negocio;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.ServicioDAO;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.modelo.Servicio;
import java.util.List;

/**
 * Reglas de negocio de servicios.
 */
public class ServicioService {

    private final ServicioDAO servicioDAO;
    private final TurnoDAO turnoDAO;

    public ServicioService(ServicioDAO servicioDAO, TurnoDAO turnoDAO) {
        this.servicioDAO = servicioDAO;
        this.turnoDAO = turnoDAO;
    }

    public Servicio registrar(int codigo, String nombre) throws NegocioException, PersistenciaException {
        Validaciones.validarCodigo(codigo, "servicio");
        String nombreLimpio = Validaciones.validarTextoObligatorio(nombre, "nombre");
        if (servicioDAO.buscarPorCodigo(codigo) != null) {
            throw new NegocioException("Ya existe un servicio con el código " + codigo + ".");
        }
        Servicio servicio = new Servicio(codigo, nombreLimpio, true);
        servicioDAO.insertar(servicio);
        return servicio;
    }

    public List<Servicio> listar() throws PersistenciaException {
        return servicioDAO.listarTodos();
    }

    public Servicio buscar(int codigo) throws NegocioException, PersistenciaException {
        Servicio servicio = servicioDAO.buscarPorCodigo(codigo);
        if (servicio == null) {
            throw new NegocioException("No existe un servicio con el código " + codigo + ".");
        }
        return servicio;
    }

    public Servicio actualizar(int codigo, String nombre) throws NegocioException, PersistenciaException {
        Servicio servicio = buscar(codigo);
        String nombreLimpio = Validaciones.validarTextoObligatorio(nombre, "nombre");
        servicioDAO.actualizarNombre(codigo, nombreLimpio);
        servicio.setNombre(nombreLimpio);
        return servicio;
    }

    public Servicio cambiarEstado(int codigo, boolean activo) throws NegocioException, PersistenciaException {
        Servicio servicio = buscar(codigo);
        if (servicio.isActivo() == activo) {
            throw new NegocioException("El servicio " + codigo + " ya se encuentra " + servicio.getEstadoTexto().toLowerCase() + ".");
        }
        if (!activo && turnoDAO.contarPendientesPorServicio(codigo) > 0) {
            throw new NegocioException("No se puede desactivar el servicio " + codigo + " porque tiene turnos pendientes.");
        }
        servicioDAO.actualizarEstado(codigo, activo);
        servicio.setActivo(activo);
        return servicio;
    }
}
