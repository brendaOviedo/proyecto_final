package estudiantes.proyectofinal.negocio;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.datos.ServicioDAO;
import estudiantes.proyectofinal.datos.TurnoDAO;
import estudiantes.proyectofinal.modelo.ResumenServicio;
import estudiantes.proyectofinal.modelo.Servicio;
import estudiantes.proyectofinal.modelo.Turno;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Genera reportes agrupando información con colecciones de Java.
 */
public class ReporteService {

    private final ServicioDAO servicioDAO;
    private final TurnoDAO turnoDAO;

    public ReporteService(ServicioDAO servicioDAO, TurnoDAO turnoDAO) {
        this.servicioDAO = servicioDAO;
        this.turnoDAO = turnoDAO;
    }

    /**
     * Cantidad de turnos por servicio y estado. Se usa un LinkedHashMap indexado por código de servicio:
     * primero se agregan todos los servicios en cero (para que aparezcan aunque no tengan turnos)
     * y después se cuentan los turnos.
     */
    public List<ResumenServicio> resumenPorServicio() throws PersistenciaException {
        Map<Integer, ResumenServicio> resumenes = new LinkedHashMap<>();
        for (Servicio servicio : servicioDAO.listarTodos()) {
            resumenes.put(servicio.getCodigo(), new ResumenServicio(servicio.getCodigo(), servicio.getNombre()));
        }
        for (Turno turno : turnoDAO.listarTodos()) {
            ResumenServicio resumen = resumenes.get(turno.getCodigoServicio());
            if (resumen != null) {
                resumen.contar(turno.getEstado());
            }
        }
        return new ArrayList<>(resumenes.values());
    }

    /** Suma las filas del resumen para la fila de totales. */
    public ResumenServicio calcularTotal(List<ResumenServicio> resumenes) {
        ResumenServicio total = new ResumenServicio(0, "Total");
        for (ResumenServicio resumen : resumenes) {
            total.sumar(resumen);
        }
        return total;
    }
}
