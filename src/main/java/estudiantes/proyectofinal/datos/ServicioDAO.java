package estudiantes.proyectofinal.datos;

import estudiantes.proyectofinal.modelo.Servicio;
import java.util.List;

/**
 * Operaciones de persistencia de servicios.
 */
public interface ServicioDAO {

    void insertar(Servicio servicio) throws PersistenciaException;

    void actualizarNombre(int codigo, String nombre) throws PersistenciaException;

    void actualizarEstado(int codigo, boolean activo) throws PersistenciaException;

    /** @return el servicio o null si no existe */
    Servicio buscarPorCodigo(int codigo) throws PersistenciaException;

    List<Servicio> listarTodos() throws PersistenciaException;
}
