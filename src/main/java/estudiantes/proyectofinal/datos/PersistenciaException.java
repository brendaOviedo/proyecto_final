package estudiantes.proyectofinal.datos;

/**
 * Error al leer o guardar información en la base de datos.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
}
