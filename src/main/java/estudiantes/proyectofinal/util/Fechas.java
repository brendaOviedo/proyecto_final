package estudiantes.proyectofinal.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para convertir fechas entre LocalDateTime y el texto almacenado en SQLite.
 */
public final class Fechas {

    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Fechas() {
    }

    /** Fecha y hora actual sin fracciones de segundo. */
    public static LocalDateTime ahora() {
        return LocalDateTime.now().withNano(0);
    }

    public static String formatear(LocalDateTime fecha) {
        return fecha == null ? null : fecha.format(FORMATO);
    }

    public static LocalDateTime parsear(String texto) {
        return texto == null || texto.isBlank() ? null : LocalDateTime.parse(texto, FORMATO);
    }
}
