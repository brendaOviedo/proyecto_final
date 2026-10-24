package estudiantes.proyectofinal.vista;

import estudiantes.proyectofinal.datos.PersistenciaException;
import estudiantes.proyectofinal.negocio.NegocioException;

/**
 * Comportamiento común de los menús: ejecutar una acción mostrando los errores sin detener el programa.
 */
abstract class MenuBase {

    protected final Consola consola;

    protected MenuBase(Consola consola) {
        this.consola = consola;
    }

    protected void ejecutarSeguro(Accion accion) {
        try {
            accion.ejecutar();
        } catch (NegocioException e) {
            consola.error(e.getMessage());
        } catch (PersistenciaException e) {
            String detalle = e.getCause() != null ? " Detalle: " + e.getCause().getMessage() : "";
            consola.error(e.getMessage() + detalle);
        }
    }
}
