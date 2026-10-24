package estudiantes.proyectofinal.negocio;

/**
 * Violación de una regla de negocio o dato de entrada inválido.
 */
public class NegocioException extends Exception {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
