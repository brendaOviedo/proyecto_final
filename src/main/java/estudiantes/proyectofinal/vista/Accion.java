package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.negocio.NegocioException;

/**
 * Acción de un menú que puede fallar por una regla de negocio o un error de persistencia.
 */
@FunctionalInterface
public interface Accion {

    void ejecutar() throws NegocioException, PersistenciaException;
}
