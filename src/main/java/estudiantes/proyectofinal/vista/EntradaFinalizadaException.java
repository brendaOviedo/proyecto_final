package estudiantes.proyectofinal.vista;

/**
 * Se lanza cuando la entrada estándar se cierra (por ejemplo, Ctrl+Z / Ctrl+D) para terminar el programa ordenadamente.
 */
public class EntradaFinalizadaException extends RuntimeException {

    public EntradaFinalizadaException() {
        super("La entrada de datos finalizó.");
    }
}
