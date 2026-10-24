package estudiantes.proyectofinal.datos.sqlite;

/**
 * Conversión entre el estado activo/inactivo del modelo y el texto guardado en las tablas.
 */
final class EstadoRegistro {

    static final String ACTIVO = "ACTIVO";
    static final String INACTIVO = "INACTIVO";

    private EstadoRegistro() {
    }

    static String aTexto(boolean activo) {
        return activo ? ACTIVO : INACTIVO;
    }

    static boolean esActivo(String texto) {
        return ACTIVO.equalsIgnoreCase(texto);
    }
}
