package estudiantes.proyectofinal.negocio;

/**
 * Validaciones de datos de entrada comunes a clientes y servicios.
 */
final class Validaciones {

    private static final String PATRON_TELEFONO = "\\d{8}";

    private Validaciones() {
    }

    static void validarCodigo(int codigo, String entidad) throws NegocioException {
        if (codigo <= 0) {
            throw new NegocioException("El código del " + entidad + " debe ser un número entero positivo.");
        }
    }

    static String validarTextoObligatorio(String texto, String campo) throws NegocioException {
        if (texto == null || texto.isBlank()) {
            throw new NegocioException("El campo " + campo + " es obligatorio.");
        }
        return texto.strip();
    }

    static String validarTelefono(String telefono) throws NegocioException {
        String limpio = validarTextoObligatorio(telefono, "teléfono");
        if (!limpio.matches(PATRON_TELEFONO)) {
            throw new NegocioException("El teléfono debe contener exactamente 8 dígitos.");
        }
        return limpio;
    }
}
